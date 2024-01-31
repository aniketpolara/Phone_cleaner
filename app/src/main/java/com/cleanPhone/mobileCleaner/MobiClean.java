package com.cleanPhone.mobileCleaner;

import android.app.Activity;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LifecycleObserver;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;
import com.cleanPhone.mobileCleaner.ads.DH_ColorpolyAppOpenManager;
import com.cleanPhone.mobileCleaner.animate.JunkData;
import com.cleanPhone.mobileCleaner.antimalware.DBXMLData;
import com.cleanPhone.mobileCleaner.antimalware.FilesInclude;
import com.cleanPhone.mobileCleaner.similerphotos.MySharedPreference;
import com.cleanPhone.mobileCleaner.socialmedia.SocialMediaNew;
import com.cleanPhone.mobileCleaner.socialmedia.SocialmediaModule;
import com.cleanPhone.mobileCleaner.utility.ForegroundCheck;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.LockOverlayService;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.wrappers.AppManagerData;
import com.cleanPhone.mobileCleaner.wrappers.DuplicatesData;
import com.cleanPhone.mobileCleaner.wrappers.MediaAppModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MobiClean extends MultiDexApplication implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    public static long REFRESH_TIME = 60;
    public static final String TAG = MobiClean.class.getSimpleName();
    public static boolean isFirstLaunch = false;
    private static volatile MobiClean mInstance = null;
    private static SharedPrefUtil sharedPrefUtil = null;
    public AppManagerData appManagerData;
    public DBXMLData dbxmlData;
    public DownloadsData downloadsData;
    public JunkData junkData;
    public KeyguardManager.KeyguardLock kl;
    public KeyguardManager km;
    public MediaAppModule mediaAppModule;
    public SocialmediaModule socialModule;
    public SocialMediaNew socialModuleNew;
    public SocialmediaModule spaceManagerModule;
    public boolean isPackageAdded = false;
    public boolean isUpdate = false;
    public DuplicatesData duplicatesData = new DuplicatesData();
    public int lastState = 0;
    public boolean iscall_rec = false;
    public boolean iscall_ser = false;
    public HashMap<String, ArrayList<FilesInclude>> resultMap = new HashMap<>();
    private BroadcastReceiver myLanguageChangeReceiver = new BroadcastReceiver() { // from class: com.mobiclean.phoneclean.MobiClean.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            try {
                MobiClean.this.h(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public static synchronized MobiClean getInstance() {
        MobiClean mobiClean;
        synchronized (MobiClean.class) {
            if (mInstance == null) {
                mInstance = new MobiClean();
            }
            mobiClean = mInstance;
        }
        return mobiClean;
    }

    public void h(Context context) {
        try {
            String country = Locale.getDefault().getCountry();
            String[] stringArray = getResources().getStringArray(R.array.country_code);
            String language = Locale.getDefault().getLanguage();
            Locale locale = new Locale(country);
            for (int i = 0; i < stringArray.length; i++) {
                if (stringArray[i].equals(language)) {
                    Locale.setDefault(locale);
                    Configuration configuration = new Configuration();
                    configuration.locale = locale;
                    MySharedPreference.setLngIndex(context, i);
                    getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MultiDex.install(getApplicationContext());
        sharedPrefUtil = new SharedPrefUtil(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        FirebaseApp.initializeApp(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AudienceNetworkAds.initialize(this);
//        AdSettings.clearTestDevices();

//        AdSettings.setTestMode(BuildConfig.DEBUG);
//        AdSettings.addTestDevice("41736E61E36522006C0C637ACFDACDA4");

        new DH_ColorpolyAppOpenManager(this);

        ForegroundCheck.init(this);
        GlobalData.SETAPPLAnguage(this);
        try {
            registerReceiver(this.myLanguageChangeReceiver, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        this.km = keyguardManager;
        if (keyguardManager != null) {
            this.kl = keyguardManager.newKeyguardLock("MyKeyguardLock");
        }
        if (!sharedPrefUtil.getBoolean(SharedPrefUtil.SHOW_CHARGEING) || GlobalData.isMyServiceRunning(this, LockOverlayService.class)) {
            return;
        }
        startService(new Intent(this, LockOverlayService.class));


    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}



