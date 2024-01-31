package com.cleanPhone.mobileCleaner.utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cleanPhone.mobileCleaner.R;

public class ShowNotification {
    private static final String CHANNEL_ID = "ch_id";

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            String string = context.getString(R.string.channel_name);
            String string2 = context.getString(R.string.channel_description);
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, string, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(string2);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }

    public void show(Context context, String str, String str2, Intent intent, int i, String str3) {
        PendingIntent activity;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, str3);
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 23) {
            activity = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            activity = PendingIntent.getActivity(context, 0, intent, 0);
        }
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(str);
        bigTextStyle.bigText(str2);
        builder.setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setSound(null).setContentTitle(str).setContentText(str2).setStyle(bigTextStyle).setSound(null).setPriority(2).setContentIntent(activity);
        Notification build = builder.build();
        if (i2 >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(str3, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        notificationManager.notify(i, build);
    }

    public void showNotificationWithButton(Context context, PendingIntent pendingIntent, String str, String str2, String str3, int i) {
        try {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), (int) R.layout.notification_layout);
            remoteViews.setTextViewText(R.id.upper_text, str);
            remoteViews.setTextViewText(R.id.bottom_text, "");
            remoteViews.setTextViewText(R.id.btn_noti_clean, str3);
            remoteViews.setOnClickPendingIntent(R.id.btn_noti_clean, pendingIntent);
            NotificationCompat.Builder priority = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setContentIntent(pendingIntent).setCustomContentView(remoteViews).setPriority(0);
            priority.setDefaults(7);
            createNotificationChannel(context);
            NotificationManagerCompat.from(context).notify(i, priority.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNotificationWithOutButton(Context context, PendingIntent pendingIntent, String str, String str2, String str3, int i) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), (int) R.layout.notification_layout_withoutbtn);
        remoteViews.setTextViewText(R.id.upper_text, str);
        remoteViews.setTextViewText(R.id.bottom_text, str2);
        NotificationCompat.Builder priority = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setContentIntent(pendingIntent).setCustomContentView(remoteViews).setPriority(0);
        priority.setDefaults(7);
        createNotificationChannel(context);
        NotificationManagerCompat.from(context).notify(i, priority.build());
    }
}
