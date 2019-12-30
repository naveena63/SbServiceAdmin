package com.ashesha.sbservicesadmin.SideNavAllJobs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.ashesha.sbservicesadmin.Invoice.InvoiceActivity;
import com.ashesha.sbservicesadmin.Invoice.InvoiceAdapter;
import com.ashesha.sbservicesadmin.Invoice.InvoiceModel;
import com.ashesha.sbservicesadmin.MainActivity;
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

public class ViewInvoiceActivity extends AppCompatActivity {
    List<InvoiceModel> invoiceModelList;
    private static final String KEY_ORDERID = "order_id";
    InvoiceModel invoiceModel;
    InvoiceAdapter invoiceAdapter;
    RecyclerView recyclerView;
    TextView subTotal, custmrPain, total, discount, paymentType;
    PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice);
        prefManager = new PrefManager(this);
        invoiceModelList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        subTotal = findViewById(R.id.subTotal);
        custmrPain = findViewById(R.id.custmr_paid);
        total = findViewById(R.id.total);



        discount = findViewById(R.id.discount);
        paymentType = findViewById(R.id.onlineType);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Invoice");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(ViewInvoiceActivity.this));


        loadData();
    }


    private void loadData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.INVOICE_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.i("invoice", "invoice" + response);
                    String status = jsonObject.getString("status");
                    String subtotal = jsonObject.getString("total_amount");
                    String custmrpaid = jsonObject.getString("display_amount");
                    String totalAmount = jsonObject.getString("grand_total");
                    String Discount = jsonObject.getString("coupon_amount");
                    String PaymentType = jsonObject.getString("payment_type");

                    subTotal.setText(subtotal);
                    custmrPain.setText(custmrpaid);
                    total.setText(totalAmount);
                    discount.setText(Discount);
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
                        invoiceAdapter = new InvoiceAdapter(ViewInvoiceActivity.this, invoiceModelList);
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