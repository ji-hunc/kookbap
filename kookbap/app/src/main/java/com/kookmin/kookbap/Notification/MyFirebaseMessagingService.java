package com.kookmin.kookbap.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kookmin.kookbap.MainActivity;
import com.kookmin.kookbap.R;
import com.kookmin.kookbap.SettingFragment;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
//    private Context context = SettingFragment.settingContext;
    private static final String TAG = "FirebaseMsgService";
    private String msg, title;

    @Override
    public void onNewToken(@NonNull String token){
        super.onNewToken(token);
        Log.d("firebase", "Refreshed token: " + token);
        // token을 서버로 전송

    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel("TestID") == null) {
                NotificationChannel channel = new NotificationChannel("TestID", "TestName", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), "TestID");
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.presence_online);
//                .setContentIntent(pendingIntent);

        Notification notification = builder.build();

        SharedPreferences noticePrf = getSharedPreferences("isNoticeOn", MODE_PRIVATE);
        Boolean isNoticeOn = noticePrf.getBoolean("isNoticeOn", false);

        if (isNoticeOn){
            notificationManager.notify(1, notification);
        }
    }
}