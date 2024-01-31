package com.cleanPhone.mobileCleaner;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;

public class AllSettings extends ParentActivity {
    RelativeLayout rl_native;
    View space;
//    int admob = 2;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
//    AdRequest adRequest;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_notification_alert_setting);


        showInterstialAds(AllSettings.this);

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

        findViewById(R.id.tv_notisetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  AllSettings.this.startActivity(new Intent(AllSettings.this, NotificationSettings.class));

            }
        });
        findViewById(R.id.tv_ignoresetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AllSettings.this.startActivity(new Intent(AllSettings.this, IgnoreListActivity.class));



            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllSettings.this.startActivity(new Intent(AllSettings.this, HomeActivity.class));
            }
        });
        sendAnalytics("SCREEN_VISIT_" + getClass().getSimpleName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        finish();
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}