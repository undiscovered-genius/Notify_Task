package com.example.tasknotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int notificationId = intent.getIntExtra("NotificationId",0);
        String message = intent.getStringExtra("todo");

        Intent mainIntent = new Intent(context, TaskActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,mainIntent,0);
        NotificationManager myNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Its Time!")
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_MAX);
//        Notification notification = new Notification(android.R.drawable.ic_dialog_info, "Notify Alarm strart", System.currentTimeMillis());
//        myNotificationManager.notify(notificationId, notification);
         myNotificationManager.notify((int)notificationId, builder.build());
//
    }
}
