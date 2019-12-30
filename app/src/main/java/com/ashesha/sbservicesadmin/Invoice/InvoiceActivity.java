package com.ashesha.sbservicesadmin.Invoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.ashesha.sbservicesadmin.MainActivity;
import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.SideNavAllJobs.CancelledJobActivity;
import com.ashesha.sbservicesadmin.SideNavAllJobs.ViewCancelledActivity;
import com.ashesha.sbservicesadmin.Utils.AppConstants;
import com.ashesha.sbservicesadmin.Utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceActivity extends AppCompatActivity {
    List<InvoiceModel> invoiceModelList;
    private static final String KEY_ORDERID = "order_id";
    InvoiceModel invoiceModel;
    InvoiceAdapter invoiceAdapter;
    RecyclerView recyclerView;
    TextView subTotal, custmrPain, total,discount,paymentType,rewards;
    PrefManager prefManager;
    Button invoiceButton, paymentLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        prefManager = new PrefManager(this);
        invoiceModelList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        subTotal = findViewById(R.id.subTotal);
        custmrPain = findViewById(R.id.custmr_paid);
        total = findViewById(R.id.total);
        invoiceButton = findViewById(R.id.butonInvoice);
        paymentLink = findViewById(R.id.paymentLink);
        discount = findViewById(R.id.discount);
        rewards = findViewById(R.id.rewards);
        paymentType = findViewById(R.id.onlineType);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Invoice");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        invoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senInvoice();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));
        paymentLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PAYMENT_LINK, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("paymentLink", "PyamentLinkResponse" + response);
                            String status = jsonObject.getString("status");
                          //  String msg = jsonObject.getString("msg");
                            //String paymentData = jsonObject.getString("payment_data");
                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(InvoiceActivity.this, "", Toast.LENGTH_SHORT).show();
                                Intent intent =new Intent(InvoiceActivity.this, MainActivity.class);
                                startActivity(intent);

                            } else if (status.equals("error")) {
                                Toast.makeText(InvoiceActivity.this, "", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InvoiceActivity.this, "Something Went wrong.. try again", Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("order_id", prefManager.getOrderId());
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(InvoiceActivity.this);
                requestQueue.add(stringRequest);
            }
        });
        loadData();
    }

    private void senInvoice() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.SEND_INVOICE_MAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            String msg = object.getString("msg");
                            if (status.equals("success")) {
                                Toast.makeText(InvoiceActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Intent intent =new Intent(InvoiceActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("login", "_--------------Login Response----------------" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(InvoiceActivity.this, "Something Went wrong.. try again", Toast.LENGTH_LONG).show();
                        Log.i("notlogin", "_---------------------------------" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("order_id", prefManager.getOrderId());
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.INVOICE_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("invoice", "invoice" + response);
                    String status = jsonObject.getString("status");
                    String custmrpaid = jsonObject.getString("display_amount");
                    String totalAmount = jsonObject.getString("grand_total");
                    String Discount = jsonObject.getString("coupon_amount");
                    String PaymentType = jsonObject.getString("payment_type");
                    String subtotal = jsonObject.getString("total_amount");
                    String rews = jsonObject.getString("reward_points");
                    subTotal.setText(subtotal+".00");
                    custmrPain.setText(custmrpaid);
                    total.setText(totalAmount+".00");
                    discount.setText(Discount);
                    rewards.setText(rews);
                    paymentType.setText(PaymentType);
                    if (status.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("order_products");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            invoiceModel = new InvoiceModel();
                            invoiceModel.setCategory_name(jsonObject1.getString("category_name"));
                            invoiceModel.setPrice(jsonObject1.getString("price"));
                            invoiceModel.setQty(jsonObject1.getString("qty"));
                            invoiceModel.setPackage_name(jsonObject1.getString("package_name"));
                            invoiceModelList.add(invoiceModel);
                        }
                        invoiceAdapter = new InvoiceAdapter(InvoiceActivity.this, invoiceModelList);
                        recyclerView.setAdapter(invoiceAdapter);
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
                Map param = new HashMap<String, String>();
                param.put(KEY_ORDERID, prefManager.getOrderId());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
