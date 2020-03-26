package com.cnergee.mypage.fcm_notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import android.util.Log;

import com.cnergee.mypage.NotificationListActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;
import cnergee.krishna.ookla.odigitel.R;


public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {

    private static final String TAG = "MyAndroidFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat

        try {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
            //create notification
            createNotification(remoteMessage.getNotification().getBody());

        }catch (Exception e){
            e.printStackTrace();
        }

       // createNotification();
    }

    private void createNotification(String messageBody) {
        Intent intent = new Intent( this , NotificationListActivity. class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
        PendingIntent.FLAG_ONE_SHOT);

        Log.e("MessageBody",":"+messageBody);


        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("ODiGitel Broadband Notification")
                        .setContentText(messageBody)
                        .setAutoCancel( true )
                        .setSound(notificationSoundURI)
                        .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
}

