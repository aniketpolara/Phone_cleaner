package com.cleanPhone.mobileCleaner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.cleanPhone.mobileCleaner.alramsservices.AlarmNotiService;
import com.cleanPhone.mobileCleaner.bservices.AppForegroundService;
import com.cleanPhone.mobileCleaner.bservices.AppInstallUninstallBroadcast;
import com.cleanPhone.mobileCleaner.bservices.InstalledAppDetails;
import com.cleanPhone.mobileCleaner.fragment.BottomTabFragment;
import com.cleanPhone.mobileCleaner.fragment.HomeFragment;
import com.cleanPhone.mobileCleaner.similerphotos.MySharedPreference;
import com.cleanPhone.mobileCleaner.tracking.CountryCode;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class HomeActivity extends ShareActivity {
    public static final int LANG_CODE = 54;
    private static final String TAG = "HomeActivity";
    public static TextView title;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    public ScrollView B;
    public boolean C;
    public AppUpdateManager appUpdateManager;
    private DrawerLayout drawerLayout;
    private int facePosition;
    private BottomTabFragment fragment;
    private Intent intent;
    public Context j;
    public SharedPrefUtil k;
    public LinearLayout l;
    public LinearLayout m;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mMonth;
    private int mYear;
    public LinearLayout n;
    public LinearLayout o;
    public LinearLayout p;
    private boolean pendingIntroAnimation;
    public LinearLayout q;
    private SwitchCompat realTimeProtectionSwitch;
    public LinearLayout s;
    public LinearLayout t;
    private Toolbar toolbar;
    public LinearLayout u;
    public RelativeLayout v;
    private int v_count;
    public RelativeLayout w;
    public ImageView x;
    public ImageView y;
    public ImageView z;
    private boolean doubleBackToExitPressedOnce = false;
    public String A = "";
    public int REQUEST_CODE = 1234;
    int admob = 2;
    private final String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};


    private void autostartCheck() {
        if (this.k.getBoolean(SharedPrefUtil.HIDE_AUTOSTART)) {
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    HomeActivity.this.intent = new Intent();
                    String str = Build.MANUFACTURER;
                    if ("xiaomi".equalsIgnoreCase(str)) {
                        HomeActivity.this.intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                    } else if ("oppo".equalsIgnoreCase(str)) {
                        HomeActivity.this.intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
                    } else if ("vivo".equalsIgnoreCase(str)) {
                        HomeActivity.this.intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                    }
                    if (HomeActivity.this.getPackageManager().queryIntentActivities(HomeActivity.this.intent, 65536).size() > 0) {
                        HomeActivity.this.showDialogAutoStart(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000L);
    }


    private void copyDBFromAsset() {
        if (new File("/data/data/" + this.j.getPackageName() + "/databases/0" + GlobalData.DB_NAME).exists()) {
            return;
        }
        copyDataBase();
    }
    private void copyDataBase() {
        Log.i("Database", "New database is being copied to device!");
        byte[] bArr = new byte[1024];
        try {
            InputStream open = this.j.getAssets().open("antivirus/av.db");
            new File("/data/data/" + this.j.getPackageName() + "/databases/").mkdirs();
            File file = new File("/data/data/" + this.j.getPackageName() + "/databases/0" + GlobalData.DB_NAME);
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            while (true) {
                int read = open.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.close();
                    fileOutputStream.flush();
                    open.close();
                    Log.i("Database", "New database has been copied to device!");
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gettdimention() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.j.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        MySharedPreference.getLngIndex(this.j);
        if (MySharedPreference.getLngIndex(this.j) != 0) {
            Log.e("LANG if", "" + MySharedPreference.getLngIndex(this.j));
        }
    }


    public void handleRealTimeProtectionText(boolean z) {
        Fragment findFragmentByTag = getSupportFragmentManager().findFragmentByTag("HomeFragmentTag");
        if (findFragmentByTag instanceof HomeFragment) {
            ((HomeFragment) findFragmentByTag).setRealTimeProtectionText(z);
        }
    }

    private void increaseVisitCount() {
        int i = this.k.getInt("v_count");
        this.v_count = i;
        int i2 = i + 1;
        this.v_count = i2;
        this.k.saveInt("v_count", i2);
    }

    private void initBilling() {

    }


    public static boolean isConnectingToInternet(Context context) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null && (allNetworkInfo = connectivityManager.getAllNetworkInfo()) != null) {
            for (NetworkInfo networkInfo : allNetworkInfo) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


    private void setForegroundService() {
        if (Build.VERSION.SDK_INT >= 26) {
            Intent intent = new Intent(this, AppForegroundService.class);
            intent.setAction(AppForegroundService.ACTION_START_FOREGROUND_SERVICE);
            startService(intent);
        }
    }

    private void setListnser() {
        this.x = (ImageView) findViewById(R.id.img);
    }


    public void showDialogAutoStart(boolean z) {
        final Dialog dialog = new Dialog(this.j);
        dialog.requestWindowFeature(1);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        if (dialog.getWindow() != null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
        }
        dialog.setContentView(R.layout.restore_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -1);
        dialog.getWindow().setGravity(17);
        final AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox) dialog.findViewById(R.id.chk_autostart);

        View space;
         CompoundButtonCompat.setButtonTintList(appCompatCheckBox, new ColorStateList(new int[][]{new int[]{-16842912}, new int[]{16842912}}, new int[]{Color.parseColor("#404040"), ContextCompat.getColor(this.j, R.color.colorPrimaryDark)}));
        if (!z) {
            appCompatCheckBox.setVisibility(View.GONE);
        }
        dialog.findViewById(R.id.ll_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HomeActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
                if (appCompatCheckBox.isChecked()) {
                    new SharedPrefUtil(HomeActivity.this).saveBoolean(SharedPrefUtil.HIDE_AUTOSTART, true);
                }
                try {
                    appCompatCheckBox.isChecked();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.findViewById(R.id.ll_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HomeActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
                if (appCompatCheckBox.isChecked()) {
                    new SharedPrefUtil(HomeActivity.this).saveBoolean(SharedPrefUtil.HIDE_AUTOSTART, true);
                    return;
                }
                try {
                    HomeActivity homeActivity = HomeActivity.this;
                    homeActivity.startActivity(homeActivity.intent);
                    appCompatCheckBox.isChecked();
                } catch (Exception unused) {
                }
            }
        });
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialogExit() {
        startActivity(new Intent(HomeActivity.this, StartActivity.class));
        finish();
    }

    public void y(View view) {
        this.appUpdateManager.completeUpdate();
    }

    public void A(AppUpdateInfo appUpdateInfo, int i, Activity activity) {
        try {
            this.appUpdateManager.startUpdateFlowForResult(appUpdateInfo, i, activity, this.REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void facebookProfileVisit() {
        super.facebookProfileVisit();
    }

    @Override
    public int getFacePosition() {
        return this.facePosition;
    }

    @Override
    public String getFacebookPageURL() {
        return super.getFacebookPageURL();
    }

    @Override
    public void instaShare() {
        super.instaShare();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        Locale locale;
        super.onActivityResult(i, i2, intent);
        if (-1 != i2 || i != 54) {
            if (i == this.REQUEST_CODE) {
                if (i2 != -1) {
                    if (this.C) {

                        return;
                    } else if (i2 == 0) {
                        Log.i(TAG, "RESULT_CANCELED ");
                        return;
                    } else if (i == 1) {
                        Log.i(TAG, "RESULT_IN_APP_UPDATE_FAILED ");
                        return;
                    } else {
                        return;
                    }
                }
                Log.i(TAG, "RESULT_OK > ");
            }
        } else if (intent == null) {
        } else {
            int intExtra = intent.getIntExtra(FirebaseAnalytics.Param.INDEX, 0);
            String stringExtra = intent.getStringExtra("lng_code");
            if (stringExtra.contains("zh-rCN")) {
                locale = Locale.SIMPLIFIED_CHINESE;
            } else if (stringExtra.contains("pt-rBR")) {
                locale = new Locale("pt", "BR");
            } else {
                locale = new Locale(stringExtra);
            }
            Locale.setDefault(locale);
            Configuration configuration = new Configuration();
            configuration.locale = locale;
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
            MySharedPreference.setLngIndex(this, intExtra);
            Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(getPackageName());
            if (launchIntentForPackage != null) {
                launchIntentForPackage.addFlags(335544320);
            }
            finishAffinity();
            startActivity(launchIntentForPackage);
        }
    }

    @Override
    public void onBackPressed() {
        sendAnalytics("onbackpressed");
        if (this.fragment == null) {
            sendAnalytics("onbackpressed_frag_null");
            super.onBackPressed();
            return;
        }

        if (Integer.parseInt(this.fragment.tab_value) > 1) {
            sendAnalytics("onbackpressed_currentLayout");
            this.fragment.currentLayout("1", this);
            return;
        }
        Util.appendLogmobicleanTest("", "BACK_PRESS_HOME", "adsfull.txt");
        sendAnalytics("onbackpressed_finish");
        showDialogExit();
    }


    @Override
    @RequiresApi(api = 24)
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);
        this.j = this;
        sendAnalytics("SCREEN_VISIT_HOME");
        if (Util.hasPermission(this.j, this.PERMISSIONS)) {
            Util.appendLogmobiclean(TAG, " homeactivity oncreate starts", "");
        }
        this.toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        this.C = Util.isUpdateType(this.j);
        this.k = new SharedPrefUtil(this.j);
        gettdimention();
        title = (TextView) this.toolbar.findViewById(R.id.tv_title);
         ((ImageView) findViewById(R.id.iv_setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    startActivity(new Intent(HomeActivity.this, AllSettings.class));

            }
        });

        title.setTypeface(Typeface.create(" sans-serif-thin", Typeface.NORMAL));
        Util.setSavedPreferences(this);
        AppUpdateManager create = AppUpdateManagerFactory.create(this.j);
        setListnser();
        GlobalData.SETAPPLAnguage(this);
        if (bundle == null) {
            this.pendingIntroAnimation = true;
            Bundle bundle2 = new Bundle();
            bundle2.putString("tab_frag", "1");
            BottomTabFragment bottomTabFragment = new BottomTabFragment();
            this.fragment = bottomTabFragment;
            bottomTabFragment.setArguments(bundle2);
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.content_frame, this.fragment);
            beginTransaction.commit();
        }
        this.k.getBooleanToggle(SharedPrefUtil.APPFIRSTTIME);
        if (this.k.getBooleanToggle(SharedPrefUtil.APPFIRSTTIME)) {
            this.k.saveBooleanToggle(SharedPrefUtil.APPFIRSTTIME, false);
            new AlarmNotiService().startServices(this);
        } else {
            new AlarmNotiService().checkAlarms(this);
        }

        if (Util.isConnectingToInternet(this.j) && this.k.getBooleanToggle(SharedPrefUtil.DETAIL_NOTSENT)) {
            startService(new Intent(this.j, InstalledAppDetails.class));
        }
        if (!GlobalData.AUTO_START_SHOWN && Build.VERSION.SDK_INT >= 26) {
            autostartCheck();
            GlobalData.AUTO_START_SHOWN = true;
        }
        increaseVisitCount();
        new AlarmNotiService().checkAlarms(this.j);
        clearNotification(new int[]{AppInstallUninstallBroadcast.UNKNOWN_CODE, 261});
        copyDBFromAsset();
        if (this.k.isRealTimeProtectionEnabled()) {
            setForegroundService();
        }
        if (Util.isConnectingToInternet(this.j)) {
            if (this.k.getString(SharedPrefUtil.COUNTRYNAME) == null) {
                startService(new Intent(this, CountryCode.class));
            }
            initBilling();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (this.pendingIntroAnimation) {
            this.pendingIntroAnimation = false;
            return true;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("FACEBOOK_ADS_HOME", "onPause Called");
    }


    @Override
    public void onResume() {
        super.onResume();
        GlobalData.proceededToAd = false;
        if (Util.isAdsFree(this)) {
            return;
        }
    }

    @Override
    public void send_facebook() {
        super.send_facebook();
    }

    @Override
    public void send_twitter() {
        super.send_twitter();
    }

    @Override
    public void send_whatspp() {
        super.send_whatspp();
    }

    @Override
    public void setFacePosition(int i) {
        this.facePosition = i;
    }

    @Override
    public void shareApp() {
        super.shareApp();
    }


}