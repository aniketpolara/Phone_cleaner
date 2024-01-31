package com.cleanPhone.mobileCleaner.alramsservices;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.batmanager.BatteryBoostAnim;
import com.cleanPhone.mobileCleaner.utility.GlobalData;

public class BatteryNotificationService extends IntentService {
    private int batterylevel;

    public class Power {
        public Power(BatteryNotificationService batteryNotificationService) {
        }

        public boolean isConnected(Context context) {
            try {
                int intExtra = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("plugged", -1);
                return intExtra == 1 || intExtra == 2 || intExtra == 4;
            } catch (Exception unused) {
                return false;
            }
        }
    }

    public BatteryNotificationService(String str) {
        super(str);
    }

    @SuppressLint("WrongConstant")
    private void showNotification(PendingIntent pendingIntent, String str, String str2, int i) {
        PendingIntent broadcast;
        Notification build;
        try {
            RemoteViews remoteViews = new RemoteViews(getBaseContext().getPackageName(), (int) R.layout.notification_layout);
            remoteViews.setTextViewText(R.id.upper_text, str);
            remoteViews.setTextViewText(R.id.bottom_text, str2);
            remoteViews.setViewVisibility(R.id.btn_ok, View.VISIBLE);
            remoteViews.setOnClickPendingIntent(R.id.btn_ok, pendingIntent);
            Intent intent = new Intent(GlobalData.INTENT_FILTER_NOTI_CANCEL);
            int i2 = Build.VERSION.SDK_INT;
            if (i2 >= 23) {
                broadcast = PendingIntent.getBroadcast(getBaseContext(), 3767, intent, 1140850688);
            } else {
                broadcast = PendingIntent.getBroadcast(getBaseContext(), 3767, intent, BasicMeasure.EXACTLY);
            }
            remoteViews.setOnClickPendingIntent(R.id.btn_cancel, broadcast);
            if (i2 > 19) {
                build = new Notification.Builder(getBaseContext()).setAutoCancel(true).setSmallIcon(R.drawable.cum_small_icon).setContentIntent(pendingIntent).setContentTitle(str).setContentText(str2).addAction(0, "", broadcast).addAction(0, "", pendingIntent).setColor(Color.parseColor("#ffffff")).build();
            } else {
                build = new Notification.Builder(getBaseContext()).setAutoCancel(true).setSmallIcon(R.drawable.cum_small_icon).setContentIntent(pendingIntent).setContentTitle(str).setContentText(str2).addAction(0, "", broadcast).build();
            }
            int i3 = build.defaults | 1;
            build.defaults = i3;
            int i4 = i3 | 4;
            build.defaults = i4;
            build.defaults = i4 | 2;
            build.flags |= 16;
            build.bigContentView = remoteViews;
            ((NotificationManager) getBaseContext().getSystemService("notification")).notify(i, build);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onHandleIntent(@Nullable Intent intent) {
        PendingIntent activity;
        try {
            this.batterylevel = getBaseContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra(FirebaseAnalytics.Param.LEVEL, -1);
            Log.i("CHECKB", "" + this.batterylevel);
            if (this.batterylevel >= 30 || new Power(this).isConnected(getBaseContext())) {
                return;
            }
            Intent intent2 = new Intent(getBaseContext(), BatteryBoostAnim.class);
            intent2.putExtra("FROMPLUGIN", true);
            intent2.putExtra("TXT", "" + getBaseContext().getString(R.string.mbc_analyze_battery));
            intent2.addFlags(268468224);
            if (Build.VERSION.SDK_INT >= 23) {
                activity = PendingIntent.getActivity(getBaseContext(), 0, intent2, PendingIntent.FLAG_IMMUTABLE);
            } else {
                activity = PendingIntent.getActivity(getBaseContext(), 0, intent2, 0);
            }
            showNotification(activity, getBaseContext().getString(R.string.mbc_battery_low), getBaseContext().getString(R.string.mbc_bat_low_alert), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
