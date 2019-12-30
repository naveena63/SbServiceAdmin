package com.ashesha.sbservicesadmin.FirebaseNotifications;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.Utils.AppConstants;
import com.ashesha.sbservicesadmin.Utils.PrefManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
PrefManager prefManager;
    public MyFirebaseMessagingService() {

    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("message", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d("meassge", "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("message", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void handleNow() {
    }

    private void scheduleJob() {
    }

    @Override
    public void onNewToken(String token) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(getString(R.string.FCM_TOKEN),deviceToken);
                editor.commit();
                Log.d("deviceToken","devoicetoken"+deviceToken);

            }
        });
        Log.d("refreshedToken", "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
    }

}