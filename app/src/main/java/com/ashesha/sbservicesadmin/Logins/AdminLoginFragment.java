package com.ashesha.sbservicesadmin.Logins;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashesha.sbservicesadmin.Dashboard.NavDashboardActivity;
import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.Utils.AppConstants;
import com.ashesha.sbservicesadmin.Utils.PrefManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminLoginFragment extends Fragment {
    View rootView;
    EditText email, password;
    TextView playsotr;
    Button button;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    PrefManager prefManager;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String PREFERENCE = "AGENT";
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_admin_login, container, false);
        context = getActivity();

        sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        prefManager = new PrefManager(getActivity());
        button = rootView.findViewById(R.id.signupButton);

        email = rootView.findViewById(R.id.email);
        password = rootView.findViewById(R.id.password);
        playsotr = rootView.findViewById(R.id.palystore_text);
        playsotr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = "com.app.asheshaTech.sbservices"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int verify = validate();
                if (verify == 0) {
                    adminLogin();
                }
            }
        });
        return rootView;
    }

    private void adminLogin() {
        final String emal = email.getText().toString();
        final String pasword = password.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.ADMIN_FRAGMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("admin_profile");
                        String adminId = jsonObject1.getString("id");
                        String name = jsonObject1.getString("name");
                        String email = jsonObject1.getString("email");
                        String mobile = jsonObject1.getString("mobile");
//                        String password = jsonObject1.getString("password");
                        String login_type = jsonObject1.getString("login_type");
                        editor.putString("login_type", login_type);
                        editor.commit();
                        prefManager.getEmployeeId();
                        Log.e("prefEmployee", "Pref" + prefManager.getEmployeeId());

                        Log.e("n", sharedpreferences.getString("login_type", ""));
                        prefManager.storeValue(AppConstants.APP_USER_LOGIN, true);
                        prefManager.storeValue(AppConstants.APP_LOGIN_ADMIN_ID, adminId);
                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_NAME, jsonObject1.getString("name"));
                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_EMAIL, jsonObject1.getString("email"));
                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_MOBILE, jsonObject1.getString("mobile"));
                        Intent intent = new Intent(getActivity(), NavDashboardActivity.class);

                        intent.putExtra("login_from", "admin");
                        intent.putExtra("login_type", sharedpreferences.getString("login_type", ""));
                        startActivity(intent);

                        Toast.makeText(getActivity(), "Successfully Login", Toast.LENGTH_SHORT).show();
                        prefManager.setAdminId(adminId);
                        Log.i("AdminId", "AdminId" + prefManager.getAdminId());
                        prefManager.setAdminEmailId(email);
                        prefManager.setAdminName(name);
                        prefManager.setAdminPhoneNumber(mobile);
                        prefManager.setLoginType(login_type);

                    } else if (status.equals("0")) {
                        Toast.makeText(getActivity(), "Your Email Address is Wrong", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("error")) {
                        Toast.makeText(getActivity(), "Password Wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i("Admin_login", "_-------------Admin Login Response----------------" + response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Something Went wrong.. try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(KEY_EMAIL, emal);
                map.put(KEY_PASSWORD, pasword);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private int validate() {
        int flag = 0;
        if (password.getText().toString().isEmpty()) {
            password.setError(getString(R.string.enter_password));
            password.requestFocus();
            flag = 1;
        } else if (email.getText().toString().isEmpty()) {
            email.setError("Enter email Address");
            email.requestFocus();
            flag = 1;
        }
        return flag;
    }

}


