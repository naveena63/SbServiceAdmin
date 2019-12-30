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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashesha.sbservicesadmin.Dashboard.NavDashboardActivity;
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

public class OpenJobsActivity extends AppCompatActivity {
    private ArrayList<String> serviceId;
    private ArrayList<String> name;
    private ArrayList<String> serviceDate;
    ArrayList<AllJobsModel> arrayList;


    List<AllJobsModel> openJobsModelList;
    AllJobsModel openJobsModel;
    private static final String KEY_EMPID = "emp_id";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String PREFERENCE = "AGENT";
    String logintype;
    Context context;
    RecyclerView recyclerView;
    OpenJobAdapter openJobAdapter;
    PrefManager prefManager;
    TextView no_packages_available;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_jobs);
        recyclerView = findViewById(R.id.recyclerView);
        prefManager = new PrefManager(this);
        openJobsModelList = new ArrayList<>();
        name = new ArrayList<>();
        serviceId = new ArrayList<>();
        serviceDate = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.open_jobs));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        context = this;
        recyclerView.setLayoutManager(new LinearLayoutManager(OpenJobsActivity.this,
                LinearLayoutManager.VERTICAL, false));
        no_packages_available = findViewById(R.id.no_packages_available);
        sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        logintype = sharedpreferences.getString("login_type", "");
        if (logintype.equalsIgnoreCase("admin")) {

            adminOpenJobs();

        }
        if (logintype.equalsIgnoreCase("employee")) {

            empOpenJobs();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (logintype.equalsIgnoreCase("admin")) {

            adminOpenJobs();

        }
        if (logintype.equalsIgnoreCase("employee")) {

            empOpenJobs();
        }

    }


    private void adminOpenJobs() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.ADMIN_OPEN_LEADS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("openJobsActivty", "openjbs" + response);
                        arrayList = new ArrayList<>();
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                JSONArray jsonArrayadminopenLeads = response.getJSONArray("adminOpenJobs");

                                for (int i = 0; i < jsonArrayadminopenLeads.length(); i++) {
                                    JSONObject jsonObject = jsonArrayadminopenLeads.getJSONObject(i);
                                    String order_id = jsonObject.getString("order_id");
                                    String name = jsonObject.getString("name");
                                    openJobsModel = new AllJobsModel();
                                    openJobsModel.setOrder_id(order_id);
                                    openJobsModel.setName(name);
                                    Log.e("dhdhdhd", "adminopenLeads " + order_id);
                                    Log.e("dhdhdhd", "adminoetLeads " + name);

                                    JSONArray jsonArrayOrder_products = jsonObject.getJSONArray("order_products");
                                    for (int j = 0; j < jsonArrayOrder_products.length(); j++) {
                                        JSONObject jsonObjectData = jsonArrayOrder_products.getJSONObject(j);
                                        String catNAme = jsonObjectData.getString("category_name");
                                        String service_date = jsonObjectData.getString("service_date");
                                        String time = jsonObjectData.getString("time_slot_name");
                                        openJobsModel.setCategory_name(catNAme);
                                        openJobsModel.setService_date(service_date);
                                        openJobsModel.setTime_slot(time);

                                        Log.e("dhdhdhd", "order_products " + catNAme);
                                        Log.e("dhdhdhd", "order_products " + service_date);
                                    }
                                    arrayList.add(openJobsModel);
                                }

                                if (arrayList.size() > 0) {
                                    openJobAdapter = new OpenJobAdapter(OpenJobsActivity.this, arrayList);
                                    openJobAdapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(openJobAdapter);
                                    no_packages_available.setVisibility(View.GONE);
                                }
                            } else {
                                no_packages_available.setText("No open leads");
                                no_packages_available.setVisibility(View.VISIBLE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("dhdhdhd", "try " + e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("dhdhdhd", "error" + error.toString());

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void empOpenJobs() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMP_OPEN_LEADS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    Log.e("open", "openleads" + response);
                    if (status.equals("success")) {
                        JSONArray jsonarryEmpData = jsonObject.getJSONArray("empOpenJobs");
                        for (int i = 0; i < jsonarryEmpData.length(); i++) {
                            JSONObject jsonobjectEmpdata = jsonarryEmpData.getJSONObject(i);
                            String order_id = jsonobjectEmpdata.getString("order_id");
                            String name = jsonobjectEmpdata.getString("name");
                            openJobsModel = new AllJobsModel();
                            openJobsModel.setOrder_id(order_id);
                            openJobsModel.setName(name);
                            Log.e("empopenname", "order_products " + name);

                            JSONArray jsonArrayOrder_products = jsonobjectEmpdata.getJSONArray("order_products");
                            for (int j = 0; j < jsonArrayOrder_products.length(); j++) {
                                JSONObject jsonObjectData = jsonArrayOrder_products.getJSONObject(j);
                                String catNAme = jsonObjectData.getString("category_name");
                                String service_date = jsonObjectData.getString("service_date");
                                String time = jsonObjectData.getString("time_slot_name");
                                openJobsModel.setCategory_name(catNAme);
                                openJobsModel.setService_date(service_date);
                                openJobsModel.setTime_slot(time);

                                Log.e("upcomingproduct", "order_products " + catNAme);
                                Log.e("upcomingproduct", "order_products " + service_date);
                            }
                            arrayList.add(openJobsModel);
                        }

                        if (arrayList.size() > 0) {
                            openJobAdapter = new OpenJobAdapter(OpenJobsActivity.this, arrayList);
                            openJobAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(openJobAdapter);
                            no_packages_available.setVisibility(View.GONE);
                        }
                    } else {
                        no_packages_available.setText("there are no orders in future");
                        no_packages_available.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error", "try " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("error", "error" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", prefManager.getEmployeeId());
                Log.d("open EmpId", "empId" + new PrefManager(OpenJobsActivity.this).getEmployeeId());
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(OpenJobsActivity.this, NavDashboardActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}



