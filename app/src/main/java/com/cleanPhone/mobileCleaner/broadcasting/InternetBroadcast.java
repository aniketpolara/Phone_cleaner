package com.cleanPhone.mobileCleaner.broadcasting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cleanPhone.mobileCleaner.alramsservices.AlarmNotiService;

public class InternetBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            new AlarmNotiService().checkAlarms(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
        NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(0);
        if (networkInfo.isAvailable() || networkInfo2.isAvailable()) {
            Log.d("Network Available ", "Flag No 1");
        }
    }
}
