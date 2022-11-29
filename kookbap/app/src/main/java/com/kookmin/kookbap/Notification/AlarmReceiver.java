package com.kookmin.kookbap.Notification;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.app.PendingIntent.FLAG_MUTABLE;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.kookmin.kookbap.MainActivity;
import com.kookmin.kookbap.R;
import com.kookmin.kookbap.RecommendMenuFragment;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver(){ }

    NotificationManager manager;
    NotificationCompat.Builder builder;

    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";


    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        builder = null;
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        //알림창 클릭 시 activity 화면 부름
        RecommendMenuFragment recommendMenuFragment = new RecommendMenuFragment();
        Intent intent2 = new Intent(context, MainActivity.class);
        intent2.putExtra("notificationClicked", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent2, FLAG_MUTABLE);

        //알림창 제목
        builder.setContentTitle("KookBob");
        //알림창 내용
        builder.setContentText("jihun님, 오늘의 추천 메뉴 확인하세요!");
        //알림창 아이콘
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        //알림창 터치시 자동 삭제
        builder.setAutoCancel(true);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        manager.notify(0,notification);

    }
}