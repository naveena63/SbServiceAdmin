package com.ashesha.sbservicesadmin.Sms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashesha.sbservicesadmin.MainActivity;
import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.Utils.AppConstants;
import com.ashesha.sbservicesadmin.Utils.PrefManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SmsActivity extends AppCompatActivity {
    EditText edittext;
    Button button;
    private static final String KEY_TEXT = "text";
    private static final String ORDER_ID = "order_id";
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Send Message");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        edittext = findViewById(R.id.editText);
        button = findViewById(R.id.testbutton);
        prefManager = new PrefManager(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();

            }
        });

    }

    private void sendMessage() {
        final String text = edittext.getText().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.SMS_TEXT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("resposne", "response" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String msg = object.getString("msg");
                    if (status.equals("success")) {
                        Intent intent=new Intent (SmsActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(SmsActivity.this,msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SmsActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }

        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(KEY_TEXT, text);
                map.put(ORDER_ID, prefManager.getOrderId());

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SmsActivity.this);
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
