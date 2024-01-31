package com.cleanPhone.mobileCleaner.bservices;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.cleanPhone.mobileCleaner.AnimatedBoostActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.coolers.CoolerCPUAnimationActivity;
import com.cleanPhone.mobileCleaner.utility.ForegroundCheck;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.ShowNotification;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CoolAndBoostService extends IntentService {
    private static final int REQCODE_RAM = 600;
    public int f4861a;
    private int boostSize;
    private int limit;
    private long ramFillSpace;
    private SharedPrefUtil sharedPref;

    public CoolAndBoostService(String str) {
        super(str);
        this.boostSize = 0;
        this.limit = 15;
        this.f4861a = 0;
    }

    @SuppressLint("WrongConstant")
    private void CustomNotification_cpu() {
        PendingIntent activity;
        try {
            this.sharedPref.saveBoolean(SharedPrefUtil.RAM_SHOWN, false);
            Context baseContext = getBaseContext();
            Intent intent = new Intent(baseContext, CoolerCPUAnimationActivity.class);
            intent.putExtra("FROMNOTI", true);
            intent.addFlags(268435456);
            if (Build.VERSION.SDK_INT >= 23) {
                activity = PendingIntent.getActivity(this, 0, intent, 201326592);
            } else {
                activity = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            new ShowNotification().showNotificationWithButton(baseContext, activity, getString(R.string.mbc_effectively_lower_temp), "", getString(R.string.mbc_cool_down), REQCODE_RAM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkRam() {
        Util.appendLogmobiclean("CoolAndBoostService", "ram service run at :", "");
        if (ForegroundCheck.get().isForeground()) {
            return;
        }
        String string = this.sharedPref.getString(SharedPrefUtil.LASTBOOSTTIME);
        boolean z = true;
        if (string != null) {
            if (Util.checkTimeDifference("" + System.currentTimeMillis(), string) <= GlobalData.boostPause) {
                z = false;
            }
        }
        if (checkRamSize() && z) {
            getRamSize();
            boolean booleanToggle = new SharedPrefUtil(getBaseContext()).getBooleanToggle(SharedPrefUtil.NOTIBOOST);
            boolean booleanToggle2 = new SharedPrefUtil(getBaseContext()).getBooleanToggle(SharedPrefUtil.NOTICOOLER);
            if (booleanToggle || booleanToggle2) {
                if (booleanToggle && booleanToggle2) {
                    if (this.sharedPref.getBoolean(SharedPrefUtil.RAM_SHOWN)) {
                        CustomNotification_cpu();
                    } else {
                        CustomNotification_ram();
                    }
                } else if (!booleanToggle) {
                    CustomNotification_cpu();
                } else {
                    CustomNotification_ram();
                }
            }
        }
    }

    private boolean checkRamSize() {
        @SuppressLint("WrongConstant") ActivityManager activityManager = (ActivityManager) getSystemService("activity");
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        if (activityManager != null) {
            activityManager.getMemoryInfo(memoryInfo);
        }
        long j = memoryInfo.totalMem;
        this.f4861a = Math.round((float) (((j - memoryInfo.availMem) * 100) / j));
        Util.appendLog("RAM service battery > 9 app not in foreground " + new SimpleDateFormat("hh:mm:ss").format(new Date(System.currentTimeMillis())) + " RAM = " + this.f4861a, "servicelogs.txt");
        return this.f4861a >= 50;
    }

    private void getRamSize() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        if (activityManager != null) {
            activityManager.getMemoryInfo(memoryInfo);
        }
        long j = memoryInfo.totalMem;
        long j2 = j / 1048576;
        long j3 = memoryInfo.availMem;
        long j4 = j3 / 1048576;
        this.ramFillSpace = (j - j3) / 1048576;
        this.ramFillSpace = (j - j3) / 1048576;
        long j5 = ((j - j3) * 100) / j;
    }

    private void setDeviceDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
    }

    @SuppressLint("WrongConstant")
    public void CustomNotification_ram() {
        PendingIntent activity;
        try {
            this.sharedPref.saveBoolean(SharedPrefUtil.RAM_SHOWN, true);
            Context baseContext = getBaseContext();
            Intent intent = new Intent(this, AnimatedBoostActivity.class);
            intent.addFlags(268468224);
            intent.putExtra("FROMNOTIFICATION", true);
            intent.putExtra("FROMNOTI", true);
            intent.putExtra(GlobalData.REDIRECTNOTI, true);
            intent.putExtra("KILLPROCESSES", true);
            intent.putExtra("SAVED_MEMORY", "0 MB");
            intent.putExtra("TYPE", "BOOST");
            intent.putExtra("MSG", "Successfully Boosted");
            if (Build.VERSION.SDK_INT >= 23) {
                activity = PendingIntent.getActivity(this, 0, intent, 201326592);
            } else {
                activity = PendingIntent.getActivity(this, 0, intent, 134217728);
            }
            new ShowNotification().showNotificationWithButton(baseContext, activity, getString(R.string.mbc_device_runningslow), "", getString(R.string.mbc_optimize), REQCODE_RAM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHandleIntent(Intent intent) {
        this.sharedPref = new SharedPrefUtil(getBaseContext());
        setDeviceDimensions();
        checkRam();
    }

}
