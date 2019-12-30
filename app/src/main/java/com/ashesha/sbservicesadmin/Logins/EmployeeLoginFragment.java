package com.ashesha.sbservicesadmin.Logins;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class EmployeeLoginFragment extends Fragment {

    View rootView;
    EditText mobile_number, password;
    Button button;
PrefManager prefManager;
    private static final String EMP_MOBILE_NUM = "emp_num";
    private static final String EMP_PASSWORD = "password";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String PREFERENCE = "AGENT";
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_employee_login, container, false);
        context=getActivity();

        sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        button = rootView.findViewById(R.id.signupButton);
        prefManager=new PrefManager(getActivity());
        mobile_number = rootView.findViewById(R.id.mobile_number);
        password = rootView.findViewById(R.id.password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int verify = validate();
                if (verify == 0) {
                    employeeLogin();
                } else {

                }

            }
        });
        return rootView;
    }


    private void employeeLogin() {

        final String mobile = mobile_number.getText().toString();
        final String pasword = password.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMPLOYEE_FRAGMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("Employee_profile");

                        String emp_id = jsonObject1.getString("emp_id");
                        String empName = jsonObject1.getString("emp_name");
                        String empEmail = jsonObject1.getString("emp_email");
                        String empCompanyNum = jsonObject1.getString("emp_cnum");
                        String login_type=jsonObject1.getString("login_type");

                        editor.putString("login_type",login_type);
                        editor.commit();
                        Log.e("naveena",sharedpreferences.getString("login_type",""));

                        prefManager.storeValue(AppConstants.APP_USER_LOGIN, true);
                        prefManager.storeValue(AppConstants.APP_LOGIN_EMPLOYEE_ID, emp_id);
                      //  prefManager.storeValue(AppConstants.APP_EMPLOYEE_NAME, jsonObject1.getString("emp_name"));
                      //  prefManager.storeValue(AppConstants.APP_EMPLOYEE_COMP_MOBNUM, jsonObject1.getString("emp_cnum"));
                      //  prefManager.storeValue(AppConstants.APP_EMPLOYEE_EMAIL, jsonObject1.getString("emp_email"));
                        Intent intent = new Intent(getActivity(), NavDashboardActivity.class);
                        intent.putExtra("login_from", "employee");
                        intent.putExtra("login_type",sharedpreferences.getString("login_type",""));
                        startActivity(intent);
                        prefManager.setEmployeeId(emp_id);
                        Log.i("EmployeeId","EmployeeId"+prefManager.getEmployeeId());
                        prefManager.setEmployeeName(empName);
                        prefManager.setEmployeeEmail(empEmail);
                        prefManager.setEmployeeCompanyNum(empCompanyNum);
                    }
                    else if (status.equals("0")) {
                            Toast.makeText(getActivity(), "Your Mobile Number is Wrong", Toast.LENGTH_SHORT).show();
                        } else if (status.equals("error")) {
                            Toast.makeText(getActivity(), "Password Wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                Log.i("EMP_LOGIN", "_-------------emp Login Response----------------" + response);
                }
            },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {
                    Toast.makeText(getActivity(), "Something Went wrong.. try again", Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(EMP_MOBILE_NUM, mobile);
                map.put(EMP_PASSWORD, pasword);
                map.put("token", prefManager.getToken());
                return map;
            }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private int validate() {
        return 0;
    }
}