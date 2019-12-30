package com.ashesha.sbservicesadmin.Dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.Utils.AppConstants;
import com.ashesha.sbservicesadmin.Utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminteamActivity extends AppCompatActivity {


    private ArrayList<String> name;
    private ArrayList<String> date;
    private ArrayList<String> jobId;
    private ArrayList<String> amount;
    ArrayList<AdminteamModel> arrayList;
    List<AdminteamModel> adminteamModelList;

    AdminteamModel adminteamModel;
    RecyclerView recyclerView;
    AdminTeamAdapter adminTeamAdapter;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String logintype;
    String PREFERENCE = "AGENT";
    Context context;
    PrefManager prefManager;
    TextView no_packages_available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminteam);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminteamActivity.this,
                LinearLayoutManager.VERTICAL, false));
        no_packages_available = findViewById(R.id.no_packages_available);
        context = AdminteamActivity.this; context = AdminteamActivity.this;
        adminteamModelList = new ArrayList<>();
        name = new ArrayList<>();
        date = new ArrayList<>();
        jobId = new ArrayList<>();
        amount = new ArrayList<>();
        prefManager = new PrefManager(this);


        sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        logintype = sharedpreferences.getString("login_type", "");
        if (logintype.equalsIgnoreCase("admin")) {
            adminloadData();
        }
        if (logintype.equalsIgnoreCase("employee")) {

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (logintype.equalsIgnoreCase("admin")) {
            adminloadData();
        }
        if (logintype.equalsIgnoreCase("employee")) {

        }

    }


    private void adminloadData() {

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(this));

        StringRequest sr = new StringRequest(Request.Method.GET, AppConstants.ADMIN_TEAM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("Responce", "" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String myResponce = jsonObject.getString("status");
                    arrayList = new ArrayList<>();

                    if (myResponce.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("employees_data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String name = jsonObject1.getString("emp_name");
                            String emp_id = jsonObject1.getString("emp_id");
                            String emp_accnt_money = jsonObject1.getString("emp_accnt_money");

                            adminteamModel = new AdminteamModel();
                            adminteamModel.setEmpName(name);
                            adminteamModel.setEmpid(emp_id);
                            adminteamModel.setEmpAccountMoney(emp_accnt_money);




                            arrayList.add(adminteamModel);
                        }
                        if (arrayList.size() > 0) {
                            adminTeamAdapter = new AdminTeamAdapter(AdminteamActivity.this, arrayList);
                            adminTeamAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adminTeamAdapter);
                            no_packages_available.setVisibility(View.GONE);
                        }
                    } else {
                        no_packages_available.setText(jsonObject.getString("0"));
                        no_packages_available.setVisibility(View.VISIBLE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("user_id","CUST34051");

                return params;
            }


        };
        requestQueue.add(sr);
    }
}
