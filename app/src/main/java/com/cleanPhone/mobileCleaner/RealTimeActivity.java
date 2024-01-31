package com.cleanPhone.mobileCleaner;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;

public class RealTimeActivity extends ParentActivity implements View.OnClickListener {
    private Intent intent;
    private SharedPrefUtil sharedPrefUtil;
    String prevStarted = "yes";
    int admob = 2;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;

    @Override
    public void onClick(View view) {
        if (multipleClicked()) {
            return;
        }
        int id = view.getId();
        if (id == R.id.disable_btn) {
            this.sharedPrefUtil.setRealTimeProtectionState(false);
            this.sharedPrefUtil.saveBoolean(SharedPrefUtil.RTP_QUERRY, true);
            this.intent = new Intent(this, HomeActivity.class);
            findViewById(R.id.pbarlayout).setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

//                                RealTimeActivity.this.finish();
                                RealTimeActivity realTimeActivity = RealTimeActivity.this;
                                realTimeActivity.startActivity(realTimeActivity.intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

                }
            }, 5000L);
        } else if (id != R.id.enable_scan_btn) {
            if (id != R.id.start_btn) {
                return;
            }
            this.sharedPrefUtil.setRealTimeProtectionState(true);
            this.sharedPrefUtil.saveBoolean(SharedPrefUtil.RTP_QUERRY, true);
            this.intent = new Intent(this, HomeActivity.class);
            findViewById(R.id.pbarlayout).setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RealTimeActivity.this.findViewById(R.id.pbarlayout).setVisibility(View.GONE);
                    RealTimeActivity.this.finish();
                    RealTimeActivity realTimeActivity = RealTimeActivity.this;

                        realTimeActivity.startActivity(realTimeActivity.intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));


                }
            }, 5000L);
        } else {
            this.sharedPrefUtil.setRealTimeProtectionState(true);
            this.sharedPrefUtil.saveBoolean(SharedPrefUtil.RTP_QUERRY, true);
            Intent intent = new Intent(this, HomeActivity.class);
            this.intent = intent;
            intent.putExtra("SCAN", true);
            this.intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            findViewById(R.id.pbarlayout).setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                        RealTimeActivity.this.finish();
                        RealTimeActivity realTimeActivity = RealTimeActivity.this;
                        realTimeActivity.startActivity(realTimeActivity.intent);

                }
            }, 5000L);
        }
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_real_time);
        sendAnalytics("SCREEN_VISIT_RealTimeActivity");
        this.sharedPrefUtil = new SharedPrefUtil(this);
        sendAnalytics("calling_loadfullad_REALTIME");


        findViewById(R.id.start_btn).setOnClickListener(this);
        findViewById(R.id.disable_btn).setOnClickListener(this);
        findViewById(R.id.enable_scan_btn).setOnClickListener(this);

        showInterstialAds(RealTimeActivity.this);
        LinearLayout llBanner = findViewById(R.id.llBanner);
        DH_GllobalItem.fbAdaptiveBanner(this, llBanner, new DH_GllobalItem.BannerCallback() {
            @Override
            public void onAdLoad() {
                llBanner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFail() {
                llBanner.setVisibility(View.GONE);
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, false);
            editor.apply();
        } else {

            moveToSecondary();
        }


    }

    public void moveToSecondary() {
        Boolean check = this.sharedPrefUtil.getBoolean(SharedPrefUtil.RTP_QUERRY);
        if (check == true) {

                        Intent intent = new Intent(RealTimeActivity.this, HomeActivity.class);
                        startActivity(intent);

        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent i=new Intent(RealTimeActivity.this,StartActivity.class);
        startActivity(i);
    }
}
