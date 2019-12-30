package com.ashesha.sbservicesadmin.SideNavAllJobs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllEmployeesActivity extends AppCompatActivity {

    List<AllEmployeesModel> allEmployeesModelList;
    AllEmployeesAdapter allEmployeesAdapter;
    private RecyclerView recyclerView;
    AllEmployeesModel allEmployeesModel;
    Context context;

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_employees);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("All Employees");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        prefManager = new PrefManager(this);
        context = AllEmployeesActivity.this;

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllEmployeesActivity.this));
        empData();
    }

    private void empData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.ADMIN_Employees, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                allEmployeesModelList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("employees");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Log.e("admindetails", "AdminDetails:" + response);
                            allEmployeesModel = new AllEmployeesModel();

                            String admin_empid = jsonObject1.getString("emp_id");
                            prefManager.storeValue(AppConstants.ADMIN_EMP_ID, admin_empid);
                            prefManager.setAdminEmpId(admin_empid);

                            Log.e("adminEmpid", "AdminEmpid" + prefManager.getAdminEmpId());
                            allEmployeesModel.setEmpName(jsonObject1.getString("emp_name"));
                            allEmployeesModel.setEmFName(jsonObject1.getString("fname"));
                            allEmployeesModel.setAdminEmpId(jsonObject1.getString("emp_id"));
                            allEmployeesModel.setEmpCompnyNum(jsonObject1.getString("emp_cnum"));
                            allEmployeesModel.setEmpPersnlNum(jsonObject1.getString("emp_pnum"));
                            allEmployeesModel.setEmpRatng(jsonObject1.getString("emp_rating"));
                            allEmployeesModel.setEmpEmail(jsonObject1.getString("emp_email"));
                            allEmployeesModel.setEmpAdhar(jsonObject1.getString("aadhar"));
                            allEmployeesModel.setEmpPan(jsonObject1.getString("pancard"));
                            allEmployeesModel.setBank(jsonObject1.getString("bank"));
                            allEmployeesModel.setEmpAccntMony(jsonObject1.getString("emp_accnt_money"));
                            allEmployeesModel.setEmpAddress(jsonObject1.getString("address"));
                            allEmployeesModelList.add(allEmployeesModel);
                        }
                        allEmployeesAdapter = new AllEmployeesAdapter(AllEmployeesActivity.this, allEmployeesModelList);
                        recyclerView.setAdapter(allEmployeesAdapter);

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AllEmployeesActivity.this, NavDashboardActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

}
