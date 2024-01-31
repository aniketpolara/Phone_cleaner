package com.cleanPhone.mobileCleaner.ads;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.cleanPhone.mobileCleaner.MobiClean;

import org.jetbrains.annotations.NotNull;


public class DH_ColorpolyAppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {

    private static final String string_LOG_TAG = "AppOpenManagerResume";
    public static boolean dh_bool_isFace = false;
    static MobiClean dh_myapp_MyApplication;
    private static Activity dh_acti_CurrentActivity;
    private static AppOpenAd dh_app_OpenAd = null;
    private static AppOpenAd.AppOpenAdLoadCallback dh_aolc_LoadCallback;
    private static boolean dh_bool_ShowingAd = false;

    public DH_ColorpolyAppOpenManager(MobiClean PH_myApplication1) {
        dh_myapp_MyApplication = PH_myApplication1;
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public static void showAdIfAvailable() {
        if (!dh_bool_ShowingAd && isAdAvailable()) {
            Log.d(string_LOG_TAG, "Will show ad.");

            FullScreenContentCallback dh_fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            dh_app_OpenAd  = null;
                            dh_bool_ShowingAd = false;
                            fetchAd();

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            dh_bool_ShowingAd = true;
                        }
                    };
            dh_app_OpenAd.setFullScreenContentCallback(dh_fullScreenContentCallback);
            dh_app_OpenAd.show(dh_acti_CurrentActivity);

        } else {
            Log.d(string_LOG_TAG, "Can not show ad.");
//            if (MagicSplashActivity.onResumeOpenADs) {
            fetchAd();
//
        }
    }

    private static AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    public static boolean isAdAvailable() {
        return dh_app_OpenAd != null;
    }

    public static void fetchAd() {
        if (DH_GllobalItem.str_Admob_openAds_id == null || DH_GllobalItem.str_Admob_openAds_id.equals("")) {
            DH_GllobalItem.firebaseInit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fetchAd();
                }
            }, 2000);
        } else {
            if (isAdAvailable()) {
                return;
            }

            dh_aolc_LoadCallback =
                    new AppOpenAd.AppOpenAdLoadCallback() {

                        @Override
                        public void onAdFailedToLoad(@NonNull @NotNull LoadAdError dh_loadAdError) {
                            super.onAdFailedToLoad(dh_loadAdError);
                        }

                        @Override
                        public void onAdLoaded(@NonNull @NotNull AppOpenAd dh_ad) {
                            super.onAdLoaded(dh_app_OpenAd);
                            dh_app_OpenAd = dh_ad;
                        }


                    };

            RequestConfiguration dh_requestConfiguration = MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_MA)
                    .build();
            MobileAds.setRequestConfiguration(dh_requestConfiguration);

            AdRequest dh_request = getAdRequest();
            AppOpenAd.load(
                    dh_myapp_MyApplication, DH_GllobalItem.str_Admob_openAds_id, dh_request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, dh_aolc_LoadCallback);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Log.d(string_LOG_TAG, "onStart");


        showAdIfAvailable();

    }

    @Override
    public void onActivityCreated(Activity dh_activity, Bundle dh_savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity dh_activity) {
        dh_acti_CurrentActivity = dh_activity;
        Log.d(string_LOG_TAG, "Will show ad. onstart");
//        showAdIfAvailable();
    }

    @Override
    public void onActivityResumed(Activity dh_activity) {
        dh_acti_CurrentActivity = dh_activity;
        Log.d(string_LOG_TAG, "Will show ad. resumed");
    }

    @Override
    public void onActivityStopped(Activity dh_activity) {
    }

    @Override
    public void onActivityPaused(Activity dh_activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity dh_activity, Bundle dh_bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity dh_activity) {
        dh_acti_CurrentActivity = null;
    }
}