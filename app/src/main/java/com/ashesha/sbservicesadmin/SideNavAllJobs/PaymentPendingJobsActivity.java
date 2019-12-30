package com.ashesha.sbservicesadmin.SideNavAllJobs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

public class PaymentPendingJobsActivity extends AppCompatActivity {
    private ArrayList<String> serviceId;
    private ArrayList<String> name;
    private ArrayList<String> serviceDate;
    ArrayList<AllJobsModel> arrayList;

    RequestQueue requestQueue;
    List<AllJobsModel> pendingJobModelList;
    AllJobsModel pendingJobModel;
    private static final String KEY_EMPID = "emp_id";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String PREFERENCE = "AGENT";
    String logintype;
    Context context;
    RecyclerView recyclerView;
    PaymentPendingJobsAdapter paymentPendingJobsAdapter;
    PrefManager prefManager;
    TextView no_packages_available;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payent_pending_jobs);
        recyclerView = findViewById(R.id.recyclerView);
        requestQueue= Volley.newRequestQueue(this);
        prefManager=new PrefManager(this);
        pendingJobModelList = new ArrayList<>();
        name = new ArrayList<>();
        serviceId = new ArrayList<>();
        serviceDate = new ArrayList<>();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.paymentpending_jobs));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        context=this;
        recyclerView.setLayoutManager(new LinearLayoutManager(PaymentPendingJobsActivity.this,
                LinearLayoutManager.VERTICAL, false));
        no_packages_available=findViewById(R.id.no_packages_available);
        sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        logintype = sharedpreferences.getString("login_type", "");
        if (logintype.equalsIgnoreCase("admin")) {

            adminPendingJobs();

        }
        if (logintype.equalsIgnoreCase("employee")) {

            empPaymentPendingJobs();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (logintype.equalsIgnoreCase("admin")) {

            adminPendingJobs();

        }
        if (logintype.equalsIgnoreCase("employee")) {

            empPaymentPendingJobs();
        }

    }



    private void adminPendingJobs() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.ADMIN_PAYMENT_PENDING, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("pendingJobsActivty","pendingJobs"+response);
                        arrayList = new ArrayList<>();
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                JSONArray jsonArrayadminPendingLeads = response.getJSONArray("adminPaymentPendingJobs");

                                for (int i = 0; i < jsonArrayadminPendingLeads.length(); i++) {
                                    JSONObject jsonObject = jsonArrayadminPendingLeads.getJSONObject(i);
                                    String order_id = jsonObject.getString("order_id");
                                    String name = jsonObject.getString("name");
                                    pendingJobModel = new AllJobsModel();
                                    pendingJobModel.setOrder_id(order_id);
                                    pendingJobModel.setName(name);
                                    Log.e("dhdhdhd", "adminpendingLeads " + order_id);
                                    Log.e("dhdhdhd", "adminpendingLeads " + name);

                                    JSONArray jsonArrayOrder_products = jsonObject.getJSONArray("order_products");
                                    for (int j = 0; j < jsonArrayOrder_products.length(); j++) {
                                        JSONObject jsonObjectData = jsonArrayOrder_products.getJSONObject(j);
                                        String catNAme = jsonObjectData.getString("category_name");
                                        String service_date = jsonObjectData.getString("service_date");
                                        String time = jsonObjectData.getString("time_slot_name");
                                        pendingJobModel.setCategory_name(catNAme);
                                        pendingJobModel.setService_date(service_date);
                                        pendingJobModel.setTime_slot(time);

                                        Log.e("dhdhdhd", "order_products " + catNAme);
                                        Log.e("dhdhdhd", "order_products " + service_date);
                                    }
                                    arrayList.add(pendingJobModel);
                                }

                                if (arrayList.size() > 0) {
                                    paymentPendingJobsAdapter = new PaymentPendingJobsAdapter(PaymentPendingJobsActivity.this, arrayList);
                                    paymentPendingJobsAdapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(paymentPendingJobsAdapter);
                                    no_packages_available.setVisibility(View.GONE);
                                }}
                            else {
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



    public void empPaymentPendingJobs(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMP_PAYMENT_PENDING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    Log.e("pending", "pndingleleads" + response);
                    if (status.equals("success")) {
                        JSONArray jsonarryEmpData = jsonObject.getJSONArray("empPaymentPendingJobs");
                        for (int i = 0; i < jsonarryEmpData.length(); i++) {
                            JSONObject jsonobjectEmpdata = jsonarryEmpData.getJSONObject(i);
                            String order_id = jsonobjectEmpdata.getString("order_id");
                            String name = jsonobjectEmpdata.getString("name");
                            pendingJobModel = new AllJobsModel();
                            pendingJobModel.setOrder_id(order_id);
                            pendingJobModel.setName(name);
                            Log.e("empname", "order_products " + name);

                            JSONArray jsonArrayOrder_products = jsonobjectEmpdata.getJSONArray("order_products");
                            for (int j = 0; j < jsonArrayOrder_products.length(); j++) {
                                JSONObject jsonObjectData = jsonArrayOrder_products.getJSONObject(j);
                                String catNAme = jsonObjectData.getString("category_name");
                                String service_date = jsonObjectData.getString("service_date");
                                String time = jsonObjectData.getString("time_slot_name");
                                pendingJobModel.setCategory_name(catNAme);
                                pendingJobModel.setService_date(service_date);
                                pendingJobModel.setTime_slot(time);

                                Log.e("upcomingproduct", "order_products " + catNAme);
                                Log.e("upcomingproduct", "order_products " + service_date);
                            }
                            arrayList.add(pendingJobModel);
                        }

                        if (arrayList.size() > 0) {
                            paymentPendingJobsAdapter = new PaymentPendingJobsAdapter(PaymentPendingJobsActivity.this, arrayList);                            paymentPendingJobsAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(paymentPendingJobsAdapter);
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
                params.put("emp_id", new PrefManager(PaymentPendingJobsActivity.this).getEmployeeId());
                Log.d("PayemntPending EmpId", "empId" + new PrefManager(PaymentPendingJobsActivity.this).getEmployeeId());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PaymentPendingJobsActivity.this, NavDashboardActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }


}