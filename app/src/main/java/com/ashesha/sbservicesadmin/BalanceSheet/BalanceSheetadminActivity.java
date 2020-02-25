package com.ashesha.sbservicesadmin.BalanceSheet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceSheetadminActivity extends AppCompatActivity {
    List<BalanceSheetModel> balanceSheetModelList;
    BalanceSheetAdapter balanceSheetAdapter;
    BalanceSheetModel balanceSheetModel;
    RecyclerView recyclerView;
    PrefManager prefManager;
    TextView textViewBalance, no_packages_available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_sheet);
        prefManager = new PrefManager(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Balance Sheet");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        textViewBalance = findViewById(R.id.textViewBalance);
        no_packages_available = findViewById(R.id.no_packages_available);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        balanceSheet();
    }
    private void balanceSheet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.BALANCE_SHEET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                balanceSheetModelList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    Log.i("balance", "Reasonbalance" + response);

                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("balence_sheet");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject json = jsonArray.getJSONObject(i);
                            String orderId = json.getString("order_id");
                            String previous_balence = json.getString("previous_balence");
                            String reason = json.getString("reason");
                            String total_balence = json.getString("total_balence");

                            balanceSheetModel = new BalanceSheetModel();
                            textViewBalance.setText("Wallet Balance Is:"+ total_balence);
                            balanceSheetModel.setOrder_id(orderId);
                            balanceSheetModel.setPrevious_balance(previous_balence);
                            balanceSheetModel.setReason(reason);
                            balanceSheetModel.setTotalbalance(total_balence);

                       /*     balanceSheetAdapter = new BalanceSheetAdapter(BalanceSheetActivity.this, balanceSheetModelList);
                            balanceSheetAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(balanceSheetAdapter);*/
                            balanceSheetModelList.add(balanceSheetModel);
                            if (balanceSheetModelList.size() > 0) {
                                balanceSheetAdapter = new BalanceSheetAdapter(BalanceSheetadminActivity.this, balanceSheetModelList);
                                balanceSheetAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(balanceSheetAdapter);
                                no_packages_available.setVisibility(View.GONE);
                            }

                        }
                    } else {
                        no_packages_available.setText(jsonObject.getString("msg"));
                        no_packages_available.setVisibility(View.VISIBLE);
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", prefManager.getAdminEmpId());
                Log.e("balance_emp_id","emp_id"+ prefManager.getAdminEmpId());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(BalanceSheetadminActivity.this);
        requestQueue.add(stringRequest);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}