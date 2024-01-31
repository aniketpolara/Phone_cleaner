package com.cleanPhone.mobileCleaner.bservices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.R;

public class AppForegroundService extends Service {
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    public static final String CHANNEL_ID_FOREGROUND = "FOREGROUND_NOTIF";
    private final String TAG = AppForegroundService.class.getSimpleName();
    private AppInstallUninstallBroadcast receiver;

    private void oreoReceivers() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        try {
            this.receiver = new AppInstallUninstallBroadcast();
            intentFilter.addDataScheme("package");
            registerReceiver(this.receiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startForegroundService() {
        PendingIntent activity;
        Log.d(this.TAG, "Start foreground service.");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            activity = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            activity = PendingIntent.getActivity(this, 0, intent, 0);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_FOREGROUND);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(getString(R.string.mbc_app_name));
        bigTextStyle.bigText("" + getString(R.string.mbc_realttimeon));
        builder.setSmallIcon(R.drawable.notification_icon_bg).setLargeIcon(decodeResource).setAutoCancel(true).setSound(null).setContentTitle(getString(R.string.mbc_app_name)).setContentText("").setStyle(bigTextStyle).setSound(null).setPriority(2).setContentIntent(activity);
        Notification build = builder.build();
        if (i >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID_FOREGROUND, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        startForeground(1, build);
    }

    @Override
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(this.TAG, "My foreground service onCreate().");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppInstallUninstallBroadcast appInstallUninstallBroadcast = this.receiver;
        if (appInstallUninstallBroadcast != null) {
            try {
                unregisterReceiver(appInstallUninstallBroadcast);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent == null) {
            oreoReceivers();
            return Service.START_STICKY;
        }
        Log.d(">>>>", "1111111111");
        if (intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_START_FOREGROUND_SERVICE)) {
                Log.i(this.TAG, "Received Start Foreground Intent ");
                oreoReceivers();
                startForegroundService();
            } else if (intent.getAction().equals(ACTION_STOP_FOREGROUND_SERVICE)) {
                Log.i(this.TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                stopSelf();
            }
        }
        return Service.START_STICKY;
    }
}
