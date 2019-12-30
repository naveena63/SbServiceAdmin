package com.ashesha.sbservicesadmin.SideNavAllJobs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.Sms.SmsActivity;
import com.ashesha.sbservicesadmin.Utils.AppConstants;
import com.ashesha.sbservicesadmin.Utils.PrefManager;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ViewCancelledActivity extends AppCompatActivity  {
    TextView orderStatus, time, address, category_name, package_name, name, leadassign_text, amount, number, date, doornumber, landmark;
    PrefManager prefManager;
    Button call, full_details, text, mapBtn;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cancelled);
        context=this;
prefManager=new PrefManager(this);
        orderStatus = findViewById(R.id.orderStatus);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        address = findViewById(R.id.address);
        leadassign_text = findViewById(R.id.leadassign_text);
        category_name = findViewById(R.id.category_name);
        package_name = findViewById(R.id.package_name);
        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        number = findViewById(R.id.number);
        doornumber = findViewById(R.id.doorNumber);
        landmark = findViewById(R.id.landmark);
        text = findViewById(R.id.text);
        full_details = findViewById(R.id.full_details);
        mapBtn = findViewById(R.id.mapButton);
        call = findViewById(R.id.call);
        full_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewCancelledActivity.this, ViewInvoiceActivity.class);
                startActivity(i);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:9514222226"));
                context.startActivity(callIntent);

            }
        });

        loadAdminViewData();
    }
        private void loadAdminViewData() {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.VIEW_LEAD, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.e("view", "view" + response);
                        String status = jsonObject.getString("status");
                        if (status.equals("success")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("order_data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String order = jsonObject1.getString("order_status");
                                String timeSlot = jsonObject1.getString("time_slot_name");
                                String dates = jsonObject1.getString("service_date");
                                String custmorname = jsonObject1.getString("name");
                                String addresses = jsonObject1.getString("shipping_address");
                                String ctegoryName = jsonObject1.getString("category_name");
                                String pacakgeName = jsonObject1.getString("package_name");
                                String totalAmount = jsonObject1.getString("total_order_amount");
                                String numMobile = jsonObject1.getString("mobile");
                                String door = jsonObject1.getString("door_no");
                                String land = jsonObject1.getString("location");

                                orderStatus.setText(order);
                                time.setText(timeSlot);
                                address.setText(addresses);
                                category_name.setText(ctegoryName);
                                package_name.setText(pacakgeName);
                                name.setText(custmorname);
                                number.setText(numMobile);
                                doornumber.setText("DoorNum:"+door);
                                landmark.setText("landMark:" + land);
                                date.setText(dates + ",");
                                amount.setText("(" + totalAmount + ")");

                            }
                        }

                    } catch (Exception e) {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ViewCancelledActivity.this, "something went wrong" + error.toString(), Toast.LENGTH_SHORT).show();

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<>();
                    param.put("order_id", prefManager.getOrderId());
                    Log.e("order_id", "order_id" + prefManager.getOrderId());
                    return param;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }


}
