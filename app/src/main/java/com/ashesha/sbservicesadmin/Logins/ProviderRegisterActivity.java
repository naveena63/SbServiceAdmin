package com.ashesha.sbservicesadmin.Logins;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.ashesha.sbservicesadmin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProviderRegisterActivity extends AppCompatActivity {
    public static final String KEY_NAME = "username";
    public static final String KEY_MAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_SERVICE = "service";
    public static final String KEY_CITY= "city";
    public static final String KEY_TOKEN = "c0304a62dd289bdc7364fb974c2091f6";
EditText name,email,phone,service,city;
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_register);
name=findViewById(R.id.your_full_name);
        email=findViewById(R.id.your_email);
phone=findViewById(R.id.your_phonenumber);
service=findViewById(R.id.your_service);
        city=findViewById(R.id.your_city);
        button=findViewById(R.id.buttonRegister);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int verify = validate();
                if (verify == 0) {
                    register();
                } else {
                    Toast.makeText(ProviderRegisterActivity.this, "enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int validate() {
        int flag = 0;
        if (name.getText().toString().isEmpty()) {
            name.setError(("enter Name"));
            name.requestFocus();
            flag = 1;

        } else if (email.getText().toString().isEmpty()) {
            email.setError("enter email");
            email.requestFocus();
            flag = 1;
        } else if (phone.getText().toString().isEmpty()) {
            phone.setError(getString(R.string.enter_valid_number));
            phone.requestFocus();
            flag = 1;

        } else if (phone.length() != 10) {
            phone.requestFocus();
            phone.setError(getString(R.string.error_invalid_mobile_number));
        }else if (service.length() == 10) {
            service.requestFocus();
            service.setError("enter your service");
        }
        else if (city.length() == 10) {
            city.requestFocus();
            city.setError("enter City");
        }
        return flag;
    }

    private void register() {

        final String username = name.getText().toString();
        final String useremail = email.getText().toString();
        final String userphone = phone.getText().toString();
        final String userservice = service.getText().toString();
        final String userCity = city.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sbservices.in/api/service-provider-registration",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);

                            String status = object.getString("status");


                            if (status.equals("1")) {
                                Intent intent=new Intent(ProviderRegisterActivity.this,MainLoginsActivity.class);
                                startActivity(intent);
                                Toast.makeText(ProviderRegisterActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
 else if (status.equals("0")) {
                                Toast.makeText(ProviderRegisterActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("android", "_--------------Registration Response----------------" + response);
                        // Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ProviderRegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.i("An", "_-------------Error--------------------" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_NAME, username);
                map.put(KEY_MAIL, useremail);
                map.put(KEY_PHONE, userphone);
                map.put(KEY_CITY, userCity);
                map.put(KEY_SERVICE, userservice);
                map.put("token", "c0304a62dd289bdc7364fb974c2091f6");
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


            }
}
