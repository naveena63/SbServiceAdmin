package com.ashesha.sbservicesadmin.AllLeads;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class TodayLeadFragment extends Fragment {
    private ArrayList<String> serviceId;
    private ArrayList<String> name;
    private ArrayList<String> serviceDate;
    ArrayList<AllLeadModel> arrayList;
    List<AllLeadModel> todayLeadModelList;

    AllLeadModel todayLeadModel;
    RecyclerView recyclerView;
    TodayLeadAdapter todayLeadAdapter;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String logintype;
    String PREFERENCE = "AGENT";
    Context context;
    PrefManager prefManager;
    TextView no_packages_available;

    RequestQueue requestQueue;

    public TodayLeadFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_jobs, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        no_packages_available = view.findViewById(R.id.no_packages_available);
        prefManager = new PrefManager(getActivity());
        todayLeadModelList = new ArrayList<>();
        name = new ArrayList<>();
        serviceId = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity());
        serviceDate = new ArrayList<>();
        context = getActivity();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        logintype = sharedpreferences.getString("login_type", "");
        if (logintype.equalsIgnoreCase("admin")) {
            adminloadData();
        }
        if (logintype.equalsIgnoreCase("employee")) {
            emploadData();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (logintype.equalsIgnoreCase("admin")) {
            adminloadData();
        }
        if (logintype.equalsIgnoreCase("employee")) {
            emploadData();
        }

    }

    private void adminloadData() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.ADMIN_TODAY_LEADS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        arrayList = new ArrayList<>();
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                JSONArray jsonArrayadminPresentLeads = response.getJSONArray("adminPresentLeads");

                                for (int i = 0; i < jsonArrayadminPresentLeads.length(); i++) {
                                    JSONObject jsonObject = jsonArrayadminPresentLeads.getJSONObject(i);
                                    String order_id = jsonObject.getString("order_id");
                                    String name = jsonObject.getString("name");
                                    todayLeadModel = new AllLeadModel();
                                    todayLeadModel.setOrder_id(order_id);
                                    todayLeadModel.setName(name);
                                    prefManager.storeValue(AppConstants.ORDER_ID, jsonObject.getString("order_id"));
                                    prefManager.setOrderId(order_id);
                                    Log.e("dhdhdhd", "adminPresentLeads " + order_id);
                                    Log.e("dhdhdhd", "adminPresentLeads " + name);

                                    JSONArray jsonArrayOrder_products = jsonObject.getJSONArray("order_products");
                                    for (int j = 0; j < jsonArrayOrder_products.length(); j++) {
                                        JSONObject jsonObjectData = jsonArrayOrder_products.getJSONObject(j);
                                        String catNAme = jsonObjectData.getString("category_name");
                                        String service_date = jsonObjectData.getString("service_date");
                                        String time = jsonObjectData.getString("time_slot_name");
                                        todayLeadModel.setCategory_name(catNAme);
                                        todayLeadModel.setService_date(service_date);
                                        todayLeadModel.setTime_slot(time);

                                        Log.e("dhdhdhd", "order_products " + catNAme);
                                        Log.e("dhdhdhd", "order_products " + service_date);
                                    }
                                    arrayList.add(todayLeadModel);
                                }

                                if (arrayList.size() > 0) {
                                    todayLeadAdapter = new TodayLeadAdapter(getActivity(), arrayList);
                                    todayLeadAdapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(todayLeadAdapter);
                                    no_packages_available.setVisibility(View.GONE);
                                }
                            } else {
                                no_packages_available.setText("no today leads");
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

    private void emploadData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMP_TODAY_LEADS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    Log.e("presentemp", "presnetempleads" + response);
                    if (status.equals("success")) {
                        JSONArray jsonarryEmpData = jsonObject.getJSONArray("employeePresentLeads");
                        for (int i = 0; i < jsonarryEmpData.length(); i++) {
                            JSONObject jsonobjectEmpdata = jsonarryEmpData.getJSONObject(i);
                            String order_id = jsonobjectEmpdata.getString("order_id");
                            String name = jsonobjectEmpdata.getString("name");
                            todayLeadModel = new AllLeadModel();
                            todayLeadModel.setOrder_id(order_id);
                            todayLeadModel.setName(name);
                            Log.e("empPresentname", "order_products " + name);

                            JSONArray jsonArrayOrder_products = jsonobjectEmpdata.getJSONArray("order_products");
                            for (int j = 0; j < jsonArrayOrder_products.length(); j++) {
                                JSONObject jsonObjectData = jsonArrayOrder_products.getJSONObject(j);
                                String catNAme = jsonObjectData.getString("category_name");
                                String service_date = jsonObjectData.getString("service_date");
                                String time = jsonObjectData.getString("time_slot_name");
                                todayLeadModel.setCategory_name(catNAme);
                                todayLeadModel.setService_date(service_date);
                                todayLeadModel.setTime_slot(time);

                                Log.e("dhdhdhd", "order_products " + catNAme);
                                Log.e("dhdhdhd", "order_products " + service_date);
                            }
                            arrayList.add(todayLeadModel);
                        }
                        if (arrayList.size() > 0) {
                            todayLeadAdapter = new TodayLeadAdapter(getActivity(), arrayList);
                            todayLeadAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(todayLeadAdapter);
                            no_packages_available.setVisibility(View.GONE);
                        }
                    } else {
                        no_packages_available.setText(jsonObject.getString("0"));
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", prefManager.getEmployeeId());
                Log.d("presentLead EmpId", "empId" + new PrefManager(getActivity()).getEmployeeId());
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }
}