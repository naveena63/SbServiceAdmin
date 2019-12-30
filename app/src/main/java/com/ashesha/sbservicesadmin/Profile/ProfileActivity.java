package com.ashesha.sbservicesadmin.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    TextView empName, empCompnyEmail, empAddrs, empMobile, empFather, personalempEmail, personalNumber, companymobilenum, adharcardNum, panCardNum;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String logintype;
    String PREFERENCE = "AGENT";
    Context context;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        empName = findViewById(R.id.empName);
        empCompnyEmail = findViewById(R.id.personalempEmail);
        empAddrs = findViewById(R.id.address);
        personalNumber = findViewById(R.id.personalnumber);
        companymobilenum = findViewById(R.id.companymobilenum);
        profileImage = findViewById(R.id.profileImage);
        empFather = findViewById(R.id.empFather);
        adharcardNum = findViewById(R.id.adharcardNum);
        panCardNum = findViewById(R.id.panCardNum);

        context = ProfileActivity.this;
        sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        logintype = sharedpreferences.getString("login_type", "");
        if (logintype.equalsIgnoreCase("admin")) {

        }
        if (logintype.equalsIgnoreCase("employee")) {
            loadEmpData();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (logintype.equalsIgnoreCase("admin")) {

        }
        if (logintype.equalsIgnoreCase("employee")) {
            loadEmpData();
        }

    }

    private void loadEmpData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMP_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("employee_data");
                        String name = jsonObject1.getString("emp_name");
                        String email = jsonObject1.getString("emp_email");
                        String personalNum = jsonObject1.getString("emp_pnum");
                        String companyNumber = jsonObject1.getString("emp_cnum");
                        String addrerss = jsonObject1.getString("address");
                        String faherName = jsonObject1.getString("fname");
                        String adharCard = jsonObject1.getString("aadhar");
                        String pancard = jsonObject1.getString("pancard");
                        String image = jsonObject1.getString("emp_img");
                        Glide.with(ProfileActivity.this)
                                .load("https://sbservices.in/" + image)
                                .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background).
                                        error(R.drawable.ic_launcher_background))
                                .into(profileImage);

                        Log.e("imagepath", "https://sbservices.in/" + image);
                        empName.setText(name);
                        personalNumber.setText(personalNum);
                        companymobilenum.setText(companyNumber);
                        empCompnyEmail.setText(email);
                        empAddrs.setText(addrerss);
                        empFather.setText(faherName);
                        adharcardNum.setText(adharCard);
                        panCardNum.setText(pancard);
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
                params.put("emp_id", new PrefManager(ProfileActivity.this).getEmployeeId());
                return params;
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
