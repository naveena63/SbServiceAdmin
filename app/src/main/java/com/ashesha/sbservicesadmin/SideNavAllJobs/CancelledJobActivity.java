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

public class CancelledJobActivity extends AppCompatActivity {
    private ArrayList<String> serviceId;
    private ArrayList<String> name;
    private ArrayList<String> serviceDate;
    ArrayList<AllJobsModel> arrayList;
    List<AllJobsModel> cancelledJobModelList;
    AllJobsModel cancelledJobModel;
    private static final String KEY_EMPID = "emp_id";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String PREFERENCE = "AGENT";
    String logintype;
    Context context;
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    CancelledjobAdapter cancelledjobAdapter;
    PrefManager prefManager;
    TextView no_packages_available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_job);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.cancelled_jobs));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
requestQueue=Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recyclerView);
        prefManager = new PrefManager(this);
        cancelledJobModelList = new ArrayList<>();
        name = new ArrayList<>();
        serviceId = new ArrayList<>();
        serviceDate = new ArrayList<>();
        no_packages_available = findViewById(R.id.no_packages_available);
        context = this;

        recyclerView.setLayoutManager(new LinearLayoutManager(CancelledJobActivity.this,
                LinearLayoutManager.VERTICAL, false));
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.ADMIN_CANCELLED_LEADS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("CancelledsActivty","Canceleld"+response);
                        arrayList = new ArrayList<>();
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                JSONArray jsonArrayadminCancelledLeads = response.getJSONArray("adminCancelledJobs");

                                for (int i = 0; i < jsonArrayadminCancelledLeads.length(); i++) {
                                    JSONObject jsonObject = jsonArrayadminCancelledLeads.getJSONObject(i);
                                    String order_id = jsonObject.getString("order_id");
                                    String name = jsonObject.getString("name");
                                    cancelledJobModel = new AllJobsModel();
                                    cancelledJobModel.setOrder_id(order_id);
                                    cancelledJobModel.setName(name);
                                    Log.e("dhdhdhd", "admincacelledLeads " + order_id);
                                    Log.e("dhdhdhd", "adminCanceeledLeads " + name);

                                    JSONArray jsonArrayOrder_products = jsonObject.getJSONArray("order_products");
                                    for (int j = 0; j < jsonArrayOrder_products.length(); j++) {
                                        JSONObject jsonObjectData = jsonArrayOrder_products.getJSONObject(j);
                                        String catNAme = jsonObjectData.getString("category_name");
                                        String service_date = jsonObjectData.getString("service_date");
                                        String time = jsonObjectData.getString("time_slot_name");
                                        cancelledJobModel.setCategory_name(catNAme);
                                        cancelledJobModel.setService_date(service_date);
                                        cancelledJobModel.setTime_slot(time);

                                        Log.e("catname", "order_products " + catNAme);
                                        Log.e("servname", "order_products " + service_date);
                                    }
                                    arrayList.add(cancelledJobModel);
                                }

                                if (arrayList.size() > 0) {
                                    cancelledjobAdapter = new CancelledjobAdapter(CancelledJobActivity.this, arrayList);
                                    cancelledjobAdapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(cancelledjobAdapter);
                                    no_packages_available.setVisibility(View.GONE);
                                }}
                            else {
                                no_packages_available.setText("No ancelled leads");
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


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMP_CANCELLED_LEADS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    Log.e("canceeled", "cancelled" + response);
                    if (status.equals("success")) {
                        JSONArray jsonarryEmpData = jsonObject.getJSONArray("empCancelledJobs");
                        for (int i = 0; i < jsonarryEmpData.length(); i++) {
                            JSONObject jsonobjectEmpdata = jsonarryEmpData.getJSONObject(i);
                            String order_id = jsonobjectEmpdata.getString("order_id");
                            String name = jsonobjectEmpdata.getString("name");
                            cancelledJobModel = new AllJobsModel();
                            cancelledJobModel.setOrder_id(order_id);
                            cancelledJobModel.setName(name);
                            Log.e("empname", "order_products " + name);

                            JSONArray jsonArrayOrder_products = jsonobjectEmpdata.getJSONArray("order_products");
                            for (int j = 0; j < jsonArrayOrder_products.length(); j++) {
                                JSONObject jsonObjectData = jsonArrayOrder_products.getJSONObject(j);
                                String catNAme = jsonObjectData.getString("category_name");
                                String service_date = jsonObjectData.getString("service_date");
                                String time = jsonObjectData.getString("time_slot_name");
                                cancelledJobModel.setCategory_name(catNAme);
                                cancelledJobModel.setService_date(service_date);
                                cancelledJobModel.setTime_slot(time);

                                Log.e("caneeled", "order_products " + catNAme);
                                Log.e("cancelled", "order_products " + service_date);
                            }
                            arrayList.add(cancelledJobModel);
                        }

                        if (arrayList.size() > 0) {
                            cancelledjobAdapter = new CancelledjobAdapter(CancelledJobActivity.this, arrayList);
                            cancelledjobAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(cancelledjobAdapter);
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
                params.put("emp_id",  new PrefManager(CancelledJobActivity.this).getEmployeeId());
                Log.d("closed EmpId", "empId" + new PrefManager(CancelledJobActivity.this).getEmployeeId());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CancelledJobActivity.this, NavDashboardActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

}


