package com.cleanPhone.mobileCleaner.bservices;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;


public class CheckStatusOfServices extends IntentService {
    public CheckStatusOfServices(String str) {
        super(str);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        Log.d("TIMMMMM1", "calllled");
        new SharedPrefUtil(getBaseContext()).saveBoolean(SharedPrefUtil.RETEN_TRACKED, true);
    }

    public CheckStatusOfServices() {
        super("CheckStatusOfServices");
    }

}
