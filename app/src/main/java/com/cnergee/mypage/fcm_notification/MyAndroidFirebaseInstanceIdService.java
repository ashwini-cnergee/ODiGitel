package com.cnergee.mypage.fcm_notification;


import android.content.SharedPreferences;
import android.util.Log;

import com.cnergee.mypage.utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import cnergee.krishna.ookla.odigitel.R;

public class MyAndroidFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyAndroidFCMIIDService";

    @Override
    public void onTokenRefresh() {

        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Utils.log(TAG, "Refreshed token: " + refreshedToken);

        SharedPreferences sharedPreferences_ = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_name), 0);
        SharedPreferences.Editor editor=sharedPreferences_.edit();
        editor.putString("Gcm_reg_id",refreshedToken);
        editor.commit();
    }

    private void sendRegistrationToServer(String token) {
        //Implement this method if you want to store the token on your server
    }
}