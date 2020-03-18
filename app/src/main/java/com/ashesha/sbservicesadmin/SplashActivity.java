package com.ashesha.sbservicesadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ashesha.sbservicesadmin.Dashboard.NavDashboardActivity;
import com.ashesha.sbservicesadmin.Logins.MainLoginsActivity;
import com.ashesha.sbservicesadmin.Utils.AppConstants;
import com.ashesha.sbservicesadmin.Utils.GlobalVariable;
import com.ashesha.sbservicesadmin.Utils.PrefManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class SplashActivity extends AppCompatActivity {


    Handler handler;
    AppCompatImageView ivImage;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefManager = new PrefManager(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                Log.d("devicetokjen", "devicesToken" + deviceToken);

                prefManager.storeValue(AppConstants.REFRESH_TOKEN, deviceToken);
                prefManager.setToken(deviceToken);
                Log.d("token", "token" + prefManager.getToken());
               /* SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(getString(R.string.FCM_TOKEN),deviceToken);
                editor.commit();*/
                // Do whatever you want with your token now
                // i.e. store it on SharedPreferences or DB
                // or directly send it to server
            }
        });


        String unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("uniqueId", "uniqueId" + unique_id);
        prefManager.storeValue(AppConstants.DEVICE_ID, unique_id);
        prefManager.setDeviceId(unique_id);

        getSupportActionBar().hide();
        //  ivImage=findViewById(R.id.logo_one);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        getSupportActionBar().hide();
        GlobalVariable.deviceWidth = displayMetrics.widthPixels;
        GlobalVariable.deviceHeight = displayMetrics.heightPixels;

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!prefManager.getBoolean(AppConstants.APP_USER_LOGIN)) {
                    Intent intent = new Intent(SplashActivity.this, MainLoginsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, NavDashboardActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                }
            }
        }, 2000);
    }


}

