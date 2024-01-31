package com.cleanPhone.mobileCleaner.ads;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.StartActivity;

public class SplashActivity extends AppCompatActivity {


    public static boolean isNetworkAvailable(Context activity) {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }


    Dialog InternetDialog;
    View is_layoutView;

    public void showDialog() {
        InternetDialog = new Dialog(SplashActivity.this);
        is_layoutView = getLayoutInflater().inflate(R.layout.is_dialog_network, null);
        InternetDialog.setContentView(is_layoutView);

//        TextView txtVideoName = (TextView) this.is_layoutView.findViewById(R.id.is_txtVideoName);
//        TextView txtWait = (TextView) this.is_layoutView.findViewById(R.id.is_txtWait);
        RelativeLayout is_txtRetry = (RelativeLayout) this.is_layoutView.findViewById(R.id.retrynm);
        InternetDialog.setCancelable(false);
        is_txtRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DH_GllobalItem.isNetworkAvailable(SplashActivity.this)) {
                    InternetDialog.dismiss();
                    declare();
                } else {
                    Toast.makeText(SplashActivity.this, "Please Connect Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //For Notch-------------------------------------------------------------------------------------------
        InternetDialog.getWindow().setBackgroundDrawable((Drawable) new ColorDrawable(0));
        InternetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        InternetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            InternetDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            InternetDialog.getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        try {
            View is_decorView = InternetDialog.getWindow().getDecorView();
            int is_uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            is_decorView.setSystemUiVisibility(is_uiOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!InternetDialog.isShowing()) {
            InternetDialog.show();
        }
        //End-------------------------------------------------------------------------------------------------
//        showDialog();
    }

    private void declare() {
        if (isNetworkAvailable(getApplicationContext())) {
            FirebaseApp.initializeApp(SplashActivity.this);
            firebaseInit();
            dh_runnable_Main5000 = new Runnable() {
                @Override
                public void run() {
                    Log.d("SplashCheck", "finish 8 second then");
//                    if (!loadFiretime) {
                    Log.d("SplashCheck", "finish 8 second start main");
                    bool_ShowMainAds = true;
                    bool_Firsthandcall = true;
                    dh_load_Callback = null;
                    Intent dh_intent = new Intent(SplashActivity.this, StartActivity.class);
                    startActivity(dh_intent);
                    finish();
//                    }
                }
            };


            dh_handler_Main5000 = new Handler();
//        dh_handler_Main5000.postDelayed(dh_runnable_Main5000, 30000);
            dh_handler_Main5000.postDelayed(dh_runnable_Main5000, 3000);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DH_GllobalItem.rconfin_FirebaseRemoteConfig.fetch(0);
                    DH_GllobalItem.rconfin_FirebaseRemoteConfig.fetchAndActivate()
                            .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                @SuppressLint("LongLogTag")
                                @Override
                                public void onComplete(@NonNull Task<Boolean> dh_task) {
                                    if (dh_task.isSuccessful()) {
                                        boolean updated = dh_task.getResult();
                                        Log.d(string_LOG_TAG, "update  refresh--" + updated);
//
                                        DH_GllobalItem.initadskey();
                                        bool_LoadFiretime = true;
                                        if (!bool_Firsthandcall) {
                                            fetchAd();
                                            Log.d(string_LOG_TAG, "start fetchAd()-refresh");
                                        }

                                        DH_GllobalItem.loadNativeAd(SplashActivity.this);
                                        DH_GllobalItem.NativeAds(SplashActivity.this);
                                        DH_GllobalItem.fbInterstitialAds(SplashActivity.this);
                                        DH_GllobalItem.loadNativeAd(SplashActivity.this);



                                    } else {
                                        Log.d(string_LOG_TAG, "Fetch Failed");
                                    }
                                }
                            });
                }
            }, 1000);

        } else {
            Toast.makeText(SplashActivity.this, "Please Connect Internet", Toast.LENGTH_SHORT).show();
        }


    }


    public static final String string_LOG_TAG = "AppOpenManager";
    public static boolean bool_LoadFiretime = false;
    public static boolean bool_ShowMainAds = false;
    private static boolean bool_Firsthandcall = false;
    private static boolean bool_IsShowingAd = false;
    public boolean bool_IsAdfail = false;
    Handler dh_handler_Main5000;
    Runnable dh_runnable_Main5000;
    private AppOpenAd dh_app_OpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback dh_load_Callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (isNetworkAvailable(SplashActivity.this)) {

            FirebaseApp.initializeApp(SplashActivity.this);
            firebaseInit();
            dh_runnable_Main5000 = new Runnable() {
                @Override
                public void run() {
                    Log.d("SplashCheck", "finish 8 second then");
//                    if (!loadFiretime) {
                    Log.d("SplashCheck", "finish 8 second start main");
                    bool_ShowMainAds = true;
                    bool_Firsthandcall = true;
                    dh_load_Callback = null;
                    Intent dh_intent = new Intent(SplashActivity.this, StartActivity.class);
                    startActivity(dh_intent);
                    finish();
//                    }
                }
            };
            dh_handler_Main5000 = new Handler();
//        dh_handler_Main5000.postDelayed(dh_runnable_Main5000, 30000);
            dh_handler_Main5000.postDelayed(dh_runnable_Main5000, 3000);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DH_GllobalItem.rconfin_FirebaseRemoteConfig.fetch(0);
                    DH_GllobalItem.rconfin_FirebaseRemoteConfig.fetchAndActivate()
                            .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                @SuppressLint("LongLogTag")
                                @Override
                                public void onComplete(@NonNull Task<Boolean> dh_task) {
                                    if (dh_task.isSuccessful()) {
                                        boolean updated = dh_task.getResult();
                                        Log.d(string_LOG_TAG, "update  refresh--" + updated);
//
                                        DH_GllobalItem.initadskey();
                                        bool_LoadFiretime = true;
                                        if (!bool_Firsthandcall) {
                                            fetchAd();
                                            Log.d(string_LOG_TAG, "start fetchAd()-refresh");
                                        }

                                        DH_GllobalItem.loadNativeAd(SplashActivity.this);
                                        DH_GllobalItem.NativeAds(SplashActivity.this);
                                        DH_GllobalItem.fbInterstitialAds(SplashActivity.this);
                                        DH_GllobalItem.admobInterstitialAds(SplashActivity.this);
                                        DH_GllobalItem.admobInterstitialAds2(SplashActivity.this);
                                        DH_GllobalItem.admobInterstitialAds3(SplashActivity.this);
                                        DH_GllobalItem.loadNativeAd(SplashActivity.this);

                                        Log.d(string_LOG_TAG, "Fetch Succeeded...");
                                        Log.d(string_LOG_TAG, "call2 admob_openAds_id---" + DH_GllobalItem.str_Admob_openAds_id);
                                        Log.d(string_LOG_TAG, "call2 admob_interstial_id---" + DH_GllobalItem.str_Admob_interstial_id);
                                        Log.d(string_LOG_TAG, "call2 admob_interstial_id2---" + DH_GllobalItem.str_Admob_interstial_id2);
                                        Log.d(string_LOG_TAG, "call2 admob_interstial_id3--" + DH_GllobalItem.str_Admob_interstial_id3);
                                        Log.d(string_LOG_TAG, "call2 admob_interstitial_loadtime_1--" + DH_GllobalItem.str_Admob_interstitial_loadtime_1);
                                        Log.d(string_LOG_TAG, "call2 admob_interstitial_loadtime_2--" + DH_GllobalItem.str_Admob_interstitial_loadtime_2);
                                        Log.d(string_LOG_TAG, "call2 admob_interstitial_loadtime_3--" + DH_GllobalItem.str_Admob_interstitial_loadtime_3);
                                        Log.d(string_LOG_TAG, "call2 admob_adaptive_banner--" + DH_GllobalItem.str_Admob_adaptive_banner);
                                        Log.d(string_LOG_TAG, "call2 admob_adaptive_banner2--" + DH_GllobalItem.str_Admob_adaptive_banner2);
                                        Log.d(string_LOG_TAG, "call2 admob_adaptive_banner3--" + DH_GllobalItem.str_Admob_adaptive_banner3);


                                    } else {
                                        Log.d(string_LOG_TAG, "Fetch Failed");
                                    }
                                }
                            });
                }
            }, 1000);
        } else {
            showDialog();
        }

    }

    private void firebaseInit() {
        DH_GllobalItem.rconfin_FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        DH_GllobalItem.rconfin_FirebaseRemoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().setFetchTimeoutInSeconds(0).build());
        DH_GllobalItem.rconfin_FirebaseRemoteConfig.fetch(0);
        DH_GllobalItem.rconfin_FirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> dh_task) {
                        if (dh_task.isSuccessful()) {
                            boolean updated = dh_task.getResult();
                            Log.d(string_LOG_TAG, "first-update---" + updated);

                            DH_GllobalItem.initadskey();
                            DH_GllobalItem.loadNativeAd(SplashActivity.this);
                            DH_GllobalItem.NativeAds(SplashActivity.this);
                            DH_GllobalItem.fbInterstitialAds(SplashActivity.this);
                            DH_GllobalItem.admobInterstitialAds(SplashActivity.this);
                            DH_GllobalItem.admobInterstitialAds2(SplashActivity.this);
                            DH_GllobalItem.admobInterstitialAds3(SplashActivity.this);
//                            sm_GllobalItem.loadFirstNativeAd(sm_SplashActivity.this);
                            DH_GllobalItem.loadNativeAd(SplashActivity.this);

//                            sm_GllobalItem.loadSecondNativeAd(sm_SplashActivity.this);
//                            sm_GllobalItem.loadThirdNativeAd(sm_SplashActivity.this);
//
//                            bool_LoadFiretime = true;
//                            if (!bool_Firsthandcall) {
//                                Log.d("AppOpenManager", "firsthandcall_oncreate");
//                                fetchAd();
//                                Log.d(string_LOG_TAG, "start fetchAd()-refresh");
//
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        if (dh_handler_Main5000 != null || dh_runnable_Main5000 != null) {
            dh_handler_Main5000.removeCallbacks(dh_runnable_Main5000);
        }

        super.onDestroy();
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    public boolean isAdAvailable() {
        return dh_app_OpenAd != null;
    }

    public void showAdIfAvailable() {
        Log.d(string_LOG_TAG, "call showAdIfAvailable.");
        if (!bool_IsShowingAd && isAdAvailable()) {
            Log.d(string_LOG_TAG, "Will show ad.");

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            dh_app_OpenAd = null;
                            bool_IsShowingAd = false;
                            Log.d(string_LOG_TAG, "onAdDismissedFullScreenContent.");
                            bool_ShowMainAds = true;

                            Log.d("checkintent", "showad_avaliable");
                            Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                            Log.d(string_LOG_TAG, "onAdFailedToShowFullScreenContent.");
                            bool_ShowMainAds = true;
                            try {
                                Log.d("checkintent", "fail_to_load_intent");
                                Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                                startActivity(intent);

                            } catch (Exception e) {
                                e.getLocalizedMessage();
                            }
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(string_LOG_TAG, "onAdShowedFullScreenContent.");
                            bool_IsShowingAd = true;
                            bool_ShowMainAds = false;
                        }
                    };
            if (!bool_Firsthandcall) {
                dh_app_OpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                dh_app_OpenAd.show(SplashActivity.this);
//                appOpenAd.show(JKSplashScreenActivity.this, fullScreenContentCallback);
            }

        } else {
            Log.d(string_LOG_TAG, "Can not show ad.");
            dh_app_OpenAd = null;
            bool_IsShowingAd = false;
            bool_ShowMainAds = true;
            bool_Firsthandcall = true;

            Intent intent = new Intent(SplashActivity.this, StartActivity.class);
            startActivity(intent);

        }
    }


    public void fetchAd() {
        if (isAdAvailable()) {
            Log.d(string_LOG_TAG, "isAdAvailable return");
            return;
        }

        dh_load_Callback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(AppOpenAd ad) {
                Log.d(string_LOG_TAG, "isAdAvailable load");
                dh_app_OpenAd = ad;
                if (dh_handler_Main5000 != null || dh_runnable_Main5000 != null) {
                    dh_handler_Main5000.removeCallbacks(dh_runnable_Main5000);
                }
                showAdIfAvailable();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d(string_LOG_TAG, "error in loading--" + loadAdError.getMessage());
                bool_ShowMainAds = true;
                bool_IsAdfail = true;
                if (dh_handler_Main5000 != null || dh_runnable_Main5000 != null) {
                    dh_handler_Main5000.removeCallbacks(dh_runnable_Main5000);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("checkintent", "onAdFailedToLoad");
                            Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                            startActivity(intent);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                }, 0);
            }
        };

        RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                .toBuilder()
                .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_MA)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);

        AdRequest request = getAdRequest();
        if (!bool_Firsthandcall) {
            if (dh_app_OpenAd == null) {
                AppOpenAd.load(
                        SplashActivity.this, DH_GllobalItem.str_Admob_openAds_id, request,
                        AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, dh_load_Callback);
            }
        } else {
            Log.d(string_LOG_TAG, "Open Ads Not load");
        }
    }

}