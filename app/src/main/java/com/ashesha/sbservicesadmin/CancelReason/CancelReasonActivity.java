package com.ashesha.sbservicesadmin.CancelReason;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashesha.sbservicesadmin.Dashboard.NavDashboardActivity;
import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.SideNavAllJobs.CancelledJobActivity;
import com.ashesha.sbservicesadmin.Utils.AppConstants;
import com.ashesha.sbservicesadmin.Utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CancelReasonActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    List<CancelReasonModel> cancelReasonModelList;
    CancelReasonAdapter cancelReasonAdapter;
    CancelReasonModel cancelReasonModel;
    RecyclerView recyclerView;
    PrefManager prefManager;
    EditText descText;
    Button savedButton;
    final String KEY_TEXT = "text";
    boolean isValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_reason);
        prefManager = new PrefManager(this);
        descText = findViewById(R.id.DescText);
        recyclerView = findViewById(R.id.recyclerView);
        savedButton = findViewById(R.id.svaedButn);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.resonancel));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(CancelReasonActivity.this));
        canCelReason();
        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCancelReason();
              /*  if((cancelReasonAdapter.checkbox).isChecked())
                {
                    saveCancelReason();
                }
                else if(!(cancelReasonAdapter.checkbox).isChecked()){
                    Toast.makeText(CancelReasonActivity.this, "select one", Toast.LENGTH_SHORT).show();
                }
*/
            }
        });
    }

    void saveCancelReason() {
        final String text = descText.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.SAVE_CANCELED_REASON,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            String msg = object.getString("msg");
                            if (status.equals("success")) {
                                Intent intent = new Intent(CancelReasonActivity.this, CancelledJobActivity.class);
                                startActivity(intent);
                                Toast.makeText(CancelReasonActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CancelReasonActivity.this, "Something Went wrong.. try again", Toast.LENGTH_LONG).show();
                        Log.i("notlogin", "_---------------------------------" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("order_id", prefManager.getOrderId());
                map.put("reason_id", prefManager.getReson_id());
                map.put(KEY_TEXT, text);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void canCelReason() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.REASON_CANCEELED_TEXT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cancelReasonModelList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    Log.i("reasonForCancel", "Reason Cancel" + response);

                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("all_reasons");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject json = jsonArray.getJSONObject(i);
                            String descrption = json.getString("description");
                            String reasoonId = json.getString("id");
                            cancelReasonModel = new CancelReasonModel();
                            cancelReasonModel.setDescription(descrption);
                            cancelReasonModel.setId(reasoonId);
                            Log.i("reasonId", "Reasonid" + reasoonId);
                            cancelReasonModelList.add(cancelReasonModel);

                            prefManager.storeValue(AppConstants.REASON_ID, reasoonId);
                            prefManager.setReson_id(reasoonId);
                        }
                        cancelReasonAdapter = new CancelReasonAdapter(CancelReasonActivity.this, cancelReasonModelList);
                        recyclerView.setAdapter(cancelReasonAdapter);
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

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(CancelReasonActivity.this);
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