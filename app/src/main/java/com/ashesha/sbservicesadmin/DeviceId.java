package com.ashesha.sbservicesadmin;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ashesha.sbservicesadmin.Utils.AppConstants;
import com.ashesha.sbservicesadmin.Utils.PrefManager;

public class DeviceId extends AppCompatActivity {
    TextView DeviceID;
    String unique_id;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_id);
        DeviceID = (TextView)findViewById(R.id.textView1);
        prefManager=new PrefManager(this);
        unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("deviceId","deviceis"+unique_id);
        DeviceID.setText(unique_id);
    }
}