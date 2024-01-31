package com.cleanPhone.mobileCleaner.batmanager;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.cleanPhone.mobileCleaner.CommonResultActivity;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.animate.GravView;
import com.cleanPhone.mobileCleaner.processes.AndroidAppProcess;
import com.cleanPhone.mobileCleaner.utility.AndroidProcesses;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.waveview.WaveView;
import com.cleanPhone.mobileCleaner.wrappers.AppDetails;
import com.cleanPhone.mobileCleaner.wrappers.PackageInfoStruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BatteryBoostAnim extends ParentActivity {
    public static final String PRODUCT_CODE = "61428";
    private String TAG = BatteryBoostAnim.class.getSimpleName();
    private AppDetails appDetails;
    private ArrayList<PackageInfoStruct> appsToKill;
    private int bLevel;
    private Context context;
    private int deviceHeight;
    private int deviceWidth;
    public GravView i;
    private ImageView icon_fb;
    private ImageView icon_insta;
    private ImageView icon_snap;
    private ImageView icon_twitter;
    private ImageView icon_whtsapp;
    private ArrayList<String> iconsToDisplay;
    private int increasedTimeAfterBoost;
    private ArrayList<PackageInfoStruct> installedUserApps;
    private ImageView iv_battery;
    public GravView j;
    private List<AndroidAppProcess> processes;
    private WaveView wave;

    private void bubbleAnimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BatteryBoostAnim.this.i.start();
                BatteryBoostAnim.this.j.start();
            }
        }, 1000L);
    }

    private boolean checkBatteryAlreadyBoosted() {
        String string = new SharedPrefUtil(this).getString("LASTBOOSTTIME_BAT");
        if (string != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(System.currentTimeMillis());
            return Util.checkTimeDifference(sb.toString(), string) <= ((long) GlobalData.boostPause);
        }
        return false;
    }

    private void clearNotification() {
        try {
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(3767);
        } catch (Exception e) {
            Util.appendLogmobiclean(this.TAG, " method:clearNotification exception !!!!!!!!!", "");
            e.printStackTrace();
        }
        getIntent().getBooleanExtra("FROMPLUGIN", false);
    }


    public Drawable getADrawable(int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return getResources().getDrawable(i, getTheme());
        }
        return getResources().getDrawable(i);
    }

    private void getDimension() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
        String str = this.TAG;
        Log.w(str, "---deviceWidth----" + this.deviceWidth + "-------deviceHeight-------" + this.deviceHeight);
        setDimension();
    }


    public int getIncreasedTimeAfterBoost() {
        if (Build.VERSION.SDK_INT > 24) {
            return this.processes.size() * getPercentIncrease();
        }
        return randInt(5, 10) * 2;
    }

    private int getPercentIncrease() {
        return this.bLevel <= 10 ? 3 : 5;
    }

    private void iconAlphaAnim() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.icon_fb, "alpha", 0.1f, 1.0f);
        ofFloat.setDuration(2000L);
        ofFloat.start();
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.icon_insta, "alpha", 0.1f, 1.0f);
        ofFloat2.setDuration(2000L);
        ofFloat2.start();
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.icon_snap, "alpha", 0.1f, 1.0f);
        ofFloat3.setDuration(2000L);
        ofFloat3.start();
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this.icon_twitter, "alpha", 0.1f, 1.0f);
        ofFloat4.setDuration(2000L);
        ofFloat4.start();
        ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(this.icon_whtsapp, "alpha", 0.1f, 1.0f);
        ofFloat5.setDuration(2000L);
        ofFloat5.start();
    }


    public void iconsAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, ((-this.deviceHeight) * 20) / 100);
        translateAnimation.setDuration(3000L);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setFillAfter(true);
        this.icon_insta.startAnimation(translateAnimation);
        TranslateAnimation translateAnimation2 = new TranslateAnimation(0.0f, (this.deviceWidth * 25) / 100, 0.0f, ((-this.deviceHeight) * 24) / 100);
        translateAnimation2.setDuration(3000L);
        translateAnimation2.setInterpolator(new LinearInterpolator());
        translateAnimation2.setFillAfter(true);
        this.icon_fb.startAnimation(translateAnimation2);
        TranslateAnimation translateAnimation3 = new TranslateAnimation(0.0f, (this.deviceWidth * 20) / 100, 0.0f, ((-this.deviceHeight) * 40) / 100);
        translateAnimation3.setDuration(3000L);
        translateAnimation3.setInterpolator(new LinearInterpolator());
        translateAnimation3.setFillAfter(true);
        this.icon_snap.startAnimation(translateAnimation3);
        TranslateAnimation translateAnimation4 = new TranslateAnimation(0.0f, ((-this.deviceWidth) * 25) / 100, 0.0f, ((-this.deviceHeight) * 20) / 100);
        translateAnimation4.setDuration(3000L);
        translateAnimation4.setInterpolator(new LinearInterpolator());
        translateAnimation4.setFillAfter(true);
        this.icon_twitter.startAnimation(translateAnimation4);
        TranslateAnimation translateAnimation5 = new TranslateAnimation(0.0f, ((-this.deviceWidth) * 20) / 100, 0.0f, ((-this.deviceHeight) * 40) / 100);
        translateAnimation5.setDuration(3000L);
        translateAnimation5.setInterpolator(new LinearInterpolator());
        translateAnimation5.setFillAfter(true);
        this.icon_whtsapp.startAnimation(translateAnimation5);
    }

    private void iffromShortcut() {
        getIntent().getBooleanExtra("FRMSHORT", false);
        getIntent().getBooleanExtra(GlobalData.HEADER_NOTI_TRACK, false);
    }

    private void init() {
        this.context = this;
        this.i = (GravView) findViewById(R.id.grav);
        this.j = (GravView) findViewById(R.id.grav_full);
        this.iv_battery = (ImageView) findViewById(R.id.iv_battery);
        this.wave = (WaveView) findViewById(R.id.wave);
        this.icon_insta = (ImageView) findViewById(R.id.icon_insta);
        this.icon_fb = (ImageView) findViewById(R.id.icon_fb);
        this.icon_snap = (ImageView) findViewById(R.id.icon_snap);
        this.icon_whtsapp = (ImageView) findViewById(R.id.icon_whtsapp);
        this.icon_twitter = (ImageView) findViewById(R.id.icon_twitter);
    }


    @SuppressLint("StaticFieldLeak")
    public void processListCalculation() {
        new AsyncTask<String, String, String>() {
            @Override
            public void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            public String doInBackground(String... strArr) {
                BatteryBoostAnim.this.processes = AndroidProcesses.getRunningAppProcesses();
                BatteryBoostAnim batteryBoostAnim = BatteryBoostAnim.this;
                batteryBoostAnim.appDetails = new AppDetails(batteryBoostAnim);
                ArrayList<PackageInfoStruct> installedUserApps = BatteryBoostAnim.this.appDetails.getInstalledUserApps();
                BatteryBoostAnim.this.installedUserApps = new ArrayList();
                BatteryBoostAnim.this.appsToKill = new ArrayList();
                BatteryBoostAnim.this.iconsToDisplay = new ArrayList();
                for (int i = 0; i < installedUserApps.size(); i++) {
                    if (!installedUserApps.get(i).pname.equalsIgnoreCase("com.mobiclean.phoneclean")) {
                        BatteryBoostAnim.this.appsToKill.add(installedUserApps.get(i));
                        BatteryBoostAnim.this.installedUserApps.add(installedUserApps.get(i));
                        BatteryBoostAnim.this.iconsToDisplay.add(installedUserApps.get(i).pname);
                    }
                }
                if (Build.VERSION.SDK_INT <= 23) {
                    for (int i2 = 0; i2 < BatteryBoostAnim.this.appsToKill.size(); i2++) {
                        ((ActivityManager) BatteryBoostAnim.this.getSystemService(Context.ACTIVITY_SERVICE)).killBackgroundProcesses(((PackageInfoStruct) BatteryBoostAnim.this.appsToKill.get(i2)).pname);
                    }
                } else {
                    List<ApplicationInfo> installedApplications = BatteryBoostAnim.this.getPackageManager().getInstalledApplications(0);
                    ActivityManager activityManager = (ActivityManager) BatteryBoostAnim.this.getSystemService(Context.ACTIVITY_SERVICE);
                    ArrayList ignoredData = BatteryBoostAnim.this.getIgnoredData();
                    for (ApplicationInfo applicationInfo : installedApplications) {
                        if ((applicationInfo.flags & 1) != 1 && !applicationInfo.packageName.equals("com.mobiclean.phoneclean")) {
                            if (!ignoredData.contains("" + applicationInfo.packageName)) {
                                activityManager.killBackgroundProcesses(applicationInfo.packageName);
                            }
                        }
                    }
                }
                if (BatteryBoostAnim.this.iconsToDisplay.size() < 4) {
                    for (int i3 = 0; i3 < BatteryBoostAnim.this.installedUserApps.size() && BatteryBoostAnim.this.iconsToDisplay.size() != 4; i3++) {
                        String str = "" + ((PackageInfoStruct) BatteryBoostAnim.this.installedUserApps.get(i3)).pname;
                        if (!str.equalsIgnoreCase("com.testapp.batterysaverapp") && !BatteryBoostAnim.this.iconsToDisplay.contains(str)) {
                            BatteryBoostAnim.this.iconsToDisplay.add(str);
                        }
                    }
                }
                SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(BatteryBoostAnim.this);
                if (Build.VERSION.SDK_INT < 24) {
                    sharedPrefUtil.saveInt(SharedPrefUtil.TIME_SAVED, BatteryBoostAnim.this.processes.size() / 10);
                } else {
                    sharedPrefUtil.saveInt(SharedPrefUtil.TIME_SAVED, BatteryBoostAnim.randInt(5, 10));
                }
                Log.d("PSIZEE", "" + sharedPrefUtil.getInt(SharedPrefUtil.TIME_SAVED));
                BatteryBoostAnim batteryBoostAnim2 = BatteryBoostAnim.this;
                batteryBoostAnim2.increasedTimeAfterBoost = batteryBoostAnim2.getIncreasedTimeAfterBoost();
                sharedPrefUtil.saveInt(SharedPrefUtil.TIME_SAVED, BatteryBoostAnim.this.increasedTimeAfterBoost);
                return null;
            }

            @Override
            public void onPostExecute(String str) {
                Bitmap bitmap;
                super.onPostExecute( str);
                ImageView[] imageViewArr = {BatteryBoostAnim.this.icon_fb, BatteryBoostAnim.this.icon_insta, BatteryBoostAnim.this.icon_snap, BatteryBoostAnim.this.icon_twitter, BatteryBoostAnim.this.icon_whtsapp};
                PackageManager packageManager = BatteryBoostAnim.this.getPackageManager();
                int i = 0;
                for (int i2 = 0; i2 < 5; i2++) {
                    try {
                        bitmap = ((BitmapDrawable) packageManager.getApplicationIcon(((AndroidAppProcess) BatteryBoostAnim.this.processes.get(i2)).getPackageName())).getBitmap();
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        bitmap = ((BitmapDrawable) BatteryBoostAnim.this.getADrawable(R.drawable.ic_android)).getBitmap();
                    }
                    imageViewArr[i2].setImageBitmap(bitmap);
                }
                if (i < 4) {
                    for (int i3 = i - 1; i3 < 5; i3++) {
                        Bitmap bitmap2 = null;
                        try {
                            bitmap2 = ((BitmapDrawable) packageManager.getApplicationIcon((String) BatteryBoostAnim.this.iconsToDisplay.get(i3))).getBitmap();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            try {
                                bitmap2 = ((BitmapDrawable) BatteryBoostAnim.this.getADrawable(R.drawable.ic_android)).getBitmap();
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                        try {
                            imageViewArr[i3].setImageBitmap(bitmap2);
                        } catch (Exception e4) {
                            e4.printStackTrace();
                        }
                    }
                }
                BatteryBoostAnim.this.icon_whtsapp.setVisibility(View.VISIBLE);
                BatteryBoostAnim.this.icon_twitter.setVisibility(View.VISIBLE);
                BatteryBoostAnim.this.icon_snap.setVisibility(View.VISIBLE);
                BatteryBoostAnim.this.icon_insta.setVisibility(View.VISIBLE);
                BatteryBoostAnim.this.icon_fb.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void run() {
                        SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(BatteryBoostAnim.this);
                        sharedPrefUtil.saveString("LASTBOOSTTIME_BAT", "" + System.currentTimeMillis());
                        Intent intent = new Intent(BatteryBoostAnim.this, CommonResultActivity.class);
                        intent.putExtra(GlobalData.REDIRECTNOTI, true);
                        intent.putExtra("FROMNOTI", true);
                        intent.putExtra("DATA", BatteryBoostAnim.this.getString(R.string.mbc_one_battery_boost));
                        intent.putExtra("TYPE", "BOOST");
                        BatteryBoostAnim.this.finish();
                        BatteryBoostAnim.this.startActivity(intent);
                    }
                }, 5000L);
            }
        }.execute(new String[0]);
    }

    public static int randInt(int i, int i2) {
        return new Random().nextInt((i2 - i) + 1) + i;
    }

    private void setDimension() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.iv_battery.getLayoutParams();
        layoutParams.width = (this.deviceWidth * 35) / 100;
        int i = this.deviceHeight;
        layoutParams.height = (i * 35) / 100;
        layoutParams.setMargins(0, (i * 20) / 100, 0, 0);
        this.iv_battery.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.wave.getLayoutParams();
        layoutParams2.width = (int) ((this.deviceWidth * 23.5d) / 100.0d);
        int i2 = this.deviceHeight;
        layoutParams2.height = (i2 * 23) / 100;
        layoutParams2.setMargins(0, (i2 * 27) / 100, 0, 0);
        this.wave.setLayoutParams(layoutParams2);
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) this.i.getLayoutParams();
        layoutParams3.width = (this.deviceWidth * 23) / 100;
        int i3 = this.deviceHeight;
        layoutParams3.height = (i3 * 23) / 100;
        layoutParams3.setMargins(0, (i3 * 26) / 100, 0, 0);
        this.i.setLayoutParams(layoutParams3);
        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) this.icon_insta.getLayoutParams();
        layoutParams4.width = (this.deviceWidth * 11) / 100;
        int i4 = this.deviceHeight;
        layoutParams4.height = (i4 * 11) / 100;
        layoutParams4.setMargins(0, 0, 0, (i4 * 30) / 100);
        this.icon_insta.setLayoutParams(layoutParams4);
        RelativeLayout.LayoutParams layoutParams5 = (RelativeLayout.LayoutParams) this.icon_fb.getLayoutParams();
        int i5 = this.deviceWidth;
        layoutParams5.width = (i5 * 11) / 100;
        int i6 = this.deviceHeight;
        layoutParams5.height = (i6 * 11) / 100;
        layoutParams5.setMargins((i5 * 23) / 100, 0, 0, (i6 * 20) / 100);
        this.icon_fb.setLayoutParams(layoutParams5);
        RelativeLayout.LayoutParams layoutParams6 = (RelativeLayout.LayoutParams) this.icon_whtsapp.getLayoutParams();
        int i7 = this.deviceWidth;
        layoutParams6.width = (i7 * 11) / 100;
        int i8 = this.deviceHeight;
        layoutParams6.height = (i8 * 11) / 100;
        layoutParams6.setMargins(0, 0, (i7 * 30) / 100, (i8 * 12) / 100);
        this.icon_whtsapp.setLayoutParams(layoutParams6);
        RelativeLayout.LayoutParams layoutParams7 = (RelativeLayout.LayoutParams) this.icon_snap.getLayoutParams();
        int i9 = this.deviceWidth;
        layoutParams7.width = (i9 * 11) / 100;
        int i10 = this.deviceHeight;
        layoutParams7.height = (i10 * 11) / 100;
        layoutParams7.setMargins((i9 * 28) / 100, 0, 0, (i10 * 8) / 100);
        this.icon_snap.setLayoutParams(layoutParams7);
        RelativeLayout.LayoutParams layoutParams8 = (RelativeLayout.LayoutParams) this.icon_twitter.getLayoutParams();
        int i11 = this.deviceWidth;
        layoutParams8.width = (i11 * 11) / 100;
        int i12 = this.deviceHeight;
        layoutParams8.height = (i12 * 11) / 100;
        layoutParams8.setMargins(0, 0, (i11 * 25) / 100, (i12 * 25) / 100);
        this.icon_twitter.setLayoutParams(layoutParams8);
    }


    public void shakeAnimation() {
        Animation loadAnimation = AnimationUtils.loadAnimation(this.context, R.anim.shake);
        this.icon_insta.startAnimation(loadAnimation);
        this.icon_fb.startAnimation(loadAnimation);
        this.icon_snap.startAnimation(loadAnimation);
        this.icon_twitter.startAnimation(loadAnimation);
        this.icon_whtsapp.startAnimation(loadAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BatteryBoostAnim.this.iconsAnimation();
            }
        }, 1500L);
    }

    private void waveAnimation() {
        this.wave.setWaveColor(Color.parseColor("#00db15"), Color.parseColor("#04b40d"));
        this.wave.setShapeType(WaveView.ShapeType.SQUARE);
        this.wave.setWaterLevelRatio(0.3f);
        new WaveHelper(this.wave, this.context).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(BatteryBoostAnim.this.wave, "waterLevelRatio", 0.3f, 0.6f);
                ofFloat.setDuration(2500L);
                ofFloat.setInterpolator(new DecelerateInterpolator());
                ofFloat.start();
            }
        }, 5000L);
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.battery_boost_anim);
        init();
        getDimension();
        if (!checkBatteryAlreadyBoosted()) {
            waveAnimation();
            bubbleAnimation();
            iconAlphaAnim();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BatteryBoostAnim.this.shakeAnimation();
                }
            }, 2000L);
        }
        try {
            getWindow().addFlags(128);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.context = this;
        this.bLevel = getIntent().getIntExtra("LEVEL", 0);
        getIntent().getBooleanExtra("FROM_NOTI", false);
        init();
        if (!checkBatteryAlreadyBoosted()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BatteryBoostAnim.this.processListCalculation();
                }
            }, 1000L);
        } else {
            Intent intent = new Intent(this, CommonResultActivity.class);
            intent.putExtra(GlobalData.REDIRECTNOTI, true);
            intent.putExtra("FROMNOTI", true);
            intent.putExtra("DATA", getString(R.string.mbc_one_battery_boost));
            intent.putExtra("TYPE", "BOOST");
            finish();
            startActivity(intent);
        }
        clearNotification();
        iffromShortcut();
    }
}
