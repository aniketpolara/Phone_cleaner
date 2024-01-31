package com.cleanPhone.mobileCleaner.bservices;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;


public class InstalledAppDetails extends IntentService {
    private SharedPrefUtil sharedPrefUtil;
    private String token;

    public InstalledAppDetails() {
        super("SendInstallDetail");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        try {
            SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(getBaseContext());
            this.sharedPrefUtil = sharedPrefUtil;
            String string = sharedPrefUtil.getString(SharedPrefUtil.PUSHTOKEN);
            this.token = string;
            if (string == null) {
                Log.d("CCCCCCCCCCCC", "TOKEN null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
