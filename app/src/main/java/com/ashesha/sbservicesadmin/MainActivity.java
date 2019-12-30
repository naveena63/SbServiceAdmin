package com.ashesha.sbservicesadmin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashesha.sbservicesadmin.CancelReason.CancelReasonActivity;
import com.ashesha.sbservicesadmin.Dashboard.NavDashboardActivity;
import com.ashesha.sbservicesadmin.Invoice.InvoiceActivity;
import com.ashesha.sbservicesadmin.SideNavAllJobs.AssignjobsActivity;
import com.ashesha.sbservicesadmin.SideNavAllJobs.CancelledJobActivity;
import com.ashesha.sbservicesadmin.SideNavAllJobs.CancelledjobAdapter;
import com.ashesha.sbservicesadmin.SideNavAllJobs.ClosedJobsActivity;
import com.ashesha.sbservicesadmin.SideNavAllJobs.PaymentPendingJobsActivity;
import com.ashesha.sbservicesadmin.Sms.SmsActivity;
import com.ashesha.sbservicesadmin.Utils.AppConstants;
import com.ashesha.sbservicesadmin.Utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    ArrayList<String> nameEmp;
    TextView orderStatus, time, address, category_name, package_name, name, leadassign_text, amount, number, date, doornumber, landmark;
    String logintype;
    Context context;

    LinearLayout linearLayout;
    RelativeLayout relative;
    PrefManager prefManager;
    Button call, full_details, text, mapBtn, jobStart, closedJob, canceljob, assignJobbutton;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String PREFERENCE = "AGENT";

    List<SpinnerModel> spinnerModelList;
    private String employeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_view_layout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.view));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        context = MainActivity.this;
        prefManager = new PrefManager(this);
        orderStatus = findViewById(R.id.orderStatus);
        mapBtn = findViewById(R.id.mapButton);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        spinner = findViewById(R.id.spinner);
        address = findViewById(R.id.address);
        linearLayout = findViewById(R.id.linear);
        relative = findViewById(R.id.relativeLayout);
        call = findViewById(R.id.call);
        leadassign_text = findViewById(R.id.leadassign_text);
        jobStart = findViewById(R.id.startJob);
        closedJob = findViewById(R.id.closedJob);
        canceljob = findViewById(R.id.canceljob);
        category_name = findViewById(R.id.category_name);
        package_name = findViewById(R.id.package_name);
        name = findViewById(R.id.name);
        text = findViewById(R.id.text);
        full_details = findViewById(R.id.full_details);
        amount = findViewById(R.id.amount);
        number = findViewById(R.id.number);
        doornumber = findViewById(R.id.doorNumber);
        landmark = findViewById(R.id.landmark);
        assignJobbutton = findViewById(R.id.leadAssignButn);
        nameEmp = new ArrayList<>();

        full_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, InvoiceActivity.class);
                startActivity(i);
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SmsActivity.class);
                startActivity(i);
            }
        });
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String employees = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
               // NavDashboardActivity.selected_spinner_pos = position;
                //spinner.setSelection(position);

                if (position!=0)
                {
                    Integer pos=position-1;
                    try {
                        employeId=spinnerModelList.get(pos).getEmpId();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    assignJobbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                prefManager.setSpinner_Emp_id(spinnerModelList.get(pos).getEmpId());
                            leadAssign(employeId);
                        //    Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
                           // Log.e("spinTestpositinId", "params empId" + position);
                        }
                    });
                }
                Log.e("spinTestpositinId", "position" + position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        jobStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobStart.setBackgroundDrawable(getResources().getDrawable(R.color.red));
                closedJob.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
                canceljob.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
                orderStatus.setText("Job Started");
                JobStart();

            }
        });

        closedJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobStart.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
                closedJob.setBackgroundDrawable(getResources().getDrawable(R.color.red));
                canceljob.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
                orderStatus.setText("Job Completed");
                closedJobs();
                jobStart.setEnabled(false);
            }
        });
        canceljob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobStart.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
                closedJob.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
                canceljob.setBackgroundDrawable(getResources().getDrawable(R.color.red));
                Intent intent = new Intent(MainActivity.this, CancelReasonActivity.class);
                startActivity(intent);
                orderStatus.setText("Cancel Job");
            }
        });
        sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        logintype = sharedpreferences.getString("login_type", "");
        if (logintype.equalsIgnoreCase("admin")) {
            loadAdminViewData();
            relative.setVisibility(View.VISIBLE);
            leadassign_text.setVisibility(View.INVISIBLE);
        
            loadSpinnerData();
        }
        if (logintype.equalsIgnoreCase("employee")) {
            loadAdminViewData();

            leadassign_text.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.INVISIBLE);
            number.setVisibility(View.INVISIBLE);
            assignJobbutton.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (logintype.equalsIgnoreCase("admin")) {
            loadAdminViewData();
            relative.setVisibility(View.VISIBLE);
        }
        if (logintype.equalsIgnoreCase("employee")) {
            number.setVisibility(View.INVISIBLE);
            assignJobbutton.setVisibility(View.INVISIBLE);
            loadAdminViewData();
        }
    }

    //Display Emplopees Names in Spinner
    private void loadSpinnerData() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.EMP_NAMES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                nameEmp = new ArrayList<>();
                spinnerModelList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("emp_names", "empNames" + response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("all employees");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String emp_names = jsonObject1.getString("emp_name");
                            String emp_id = jsonObject1.getString("emp_id");

                            prefManager.storeValue(AppConstants.SPIN_EMP_ID, emp_id);
                            prefManager.setSpinner_Emp_id(emp_id);
                            Log.e("spinnEmpId", "SpinnerEmpid" + prefManager.getSpinner_Emp_id());

                            nameEmp.add(emp_names);
                            SpinnerModel spinnerModel = new SpinnerModel();
                            spinnerModel.setEmpId(emp_id);
                            spinnerModelList.add(spinnerModel);

                        }

                    }
                    nameEmp.add(0, "Select Employee");
                    spinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, nameEmp));
                    spinner.setSelection(NavDashboardActivity.selected_spinner_pos);


//                    ArrayAdapter talentAllAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, nameEmp);
//                    talentAllAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinner.setAdapter(talentAllAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    //Assign Lead
    private void leadAssign(String employeId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.LEAD_ASSIGN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("view", "view" + response);
                    String message = jsonObject.getString("msg");
                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, AssignjobsActivity.class);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "something went wrong" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();

                param.put("emp_id", prefManager.getSpinner_Emp_id());
                param.put("order_id", prefManager.getOrderId());

                Log.e("spinTestpositinId", "params empId" + employeId);
                Log.e("spinTestpositinId", "param OrderId " +  prefManager.getOrderId());

                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    //Admin View details assign job  will be visible
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
                Toast.makeText(MainActivity.this, "something went wrong" + error.toString(), Toast.LENGTH_SHORT).show();

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

    //click on button strat work progress service willcall
    private void JobStart() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.Work_progress, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("view", "view" + response);

                    String message = jsonObject.getString("order_status");
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {

                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "something went wrong" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();

                param.put("emp_id", prefManager.getEmployeeId());
                param.put("work_status", "work proceesing");
                param.put("order_id", prefManager.getOrderId());
                Log.e("order_id", "order_id" + prefManager.getOrderId());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    AlertDialog levelDialog;

    private void closedJobs() {
        final CharSequence[] items = {" Completed With Payment ", " Complete Without Payment "};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Option");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        closedJobSavedOne();
                        break;
                    case 1:
                        closedJobSavedTwo();
                        break;
                }
                levelDialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();

    }

    private void closedJobSavedOne() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.CLOSED_JOBS_STATUS_WITH_PAYMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("closed", "view" + response);

                    String message = jsonObject.getString("msg1");
                    String message1 = jsonObject.getString("msg2");
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(context, "" + message + message1, Toast.LENGTH_SHORT).show();

                        closedJob.setEnabled(false);
                        Intent intent =new Intent(MainActivity.this, ClosedJobsActivity.class);
                        startActivity(intent);
                    }

                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "something went wrong" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();


                param.put("closedJob_status", "services completed with payment");
                param.put("order_id", prefManager.getOrderId());
                Log.e("order_id", "order_id" + prefManager.getOrderId());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void closedJobSavedTwo() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.CLOSED_JOBS_STATUS_WITHOUT_PAYMENt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("closed", "view" + response);

                    String message = jsonObject.getString("msg1");
                    String message1 = jsonObject.getString("msg2");
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(context, "" + message + message1, Toast.LENGTH_SHORT).show();
                        closedJob.setEnabled(false);
                        Intent intent =new Intent(MainActivity.this, PaymentPendingJobsActivity.class);
                        startActivity(intent);
                    }

                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "something went wrong" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<>();


                param.put("closedJob_status", "services completed without payment");
                param.put("order_id", prefManager.getOrderId());
                Log.e("order_id", "order_id" + prefManager.getOrderId());
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
