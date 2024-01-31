package com.cleanPhone.mobileCleaner;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.util.ArrayList;

public class ParentActivity extends AppCompatActivity {
    public static final String TAG = "Parent";
    public static DisplayMetrics displaymetrics;

    public Context h;
    private FirebaseAnalytics mFirebaseAnalytics;
    private long mLastClickTime;

    public void clearNotification(int[] iArr) {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                for (int i : iArr) {
                    notificationManager.cancel(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList getIgnoredData() {
        try {
            return (ArrayList) GlobalData.getObj(this.h, "ignored_apps");
        } catch (Exception e) {
            ArrayList arrayList = new ArrayList();
            e.printStackTrace();
            return arrayList;
        }
    }


    public boolean multipleClicked() {
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.mLastClickTime;
        Log.e("TTTTTTTTT", "diff " + elapsedRealtime);
        this.mLastClickTime = SystemClock.elapsedRealtime();
        return elapsedRealtime < 600;
    }

    @Override
    public void onCreate(Bundle bundle) {
        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        super.onCreate(bundle);
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Util.isHome = false;
        this.h = this;
    }

    public void sendAnalytics(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("ad_events", str);
        this.mFirebaseAnalytics.logEvent(str, bundle);
        Util.appendLogmobicleanTest("ParentActivity", str, "analyticslog.txt");
        Log.e("ANALYTIXX", str);
    }
}
