package com.cleanPhone.mobileCleaner;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;


import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Splash extends ParentActivity {
    private static final String TAG = "Splash";
    public boolean j = false;
    public int k = 0;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;



    public static long checkTimeDifference(String str, String str2) {
        long time;
        long time2;
        if (str == null || str2 == null) {
            return 90000000L;
        }
        long parseLong = Long.parseLong(str);
        long parseLong2 = Long.parseLong(str2);
        if (parseLong < 0 || parseLong2 < 0) {
            return 900000000L;
        }
        Date date = new Date(parseLong);
        Date date2 = new Date(parseLong2);
        if (parseLong > parseLong2) {
            time = date.getTime();
            time2 = date2.getTime();
        } else {
            time = date2.getTime();
            time2 = date2.getTime();
        }
        return TimeUnit.SECONDS.toSeconds(time - time2);
    }


    public static void showdialog_sdcard(Context context, final DialogListners dialogListners) {
        Util.appendLogmobicleanbug(TAG, "in sd card dialog method");
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
        }
        dialog.setContentView(R.layout.dialog_sdcard);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -1);
        dialog.getWindow().setGravity(17);
        ((TextView) dialog.findViewById(R.id.ll_no_txt)).setText(context.getString(R.string.mbc_notnow));
        ((TextView) dialog.findViewById(R.id.ll_yes_txt)).setText(context.getString(R.string.mbc_continues));
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.appendLogmobicleanbug(Splash.TAG, "sd card dialog dismiss");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Util.appendLogmobicleanbug(Splash.TAG, "sd card ok click");
                dialogListners.clickOK();
            }
        });
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        GlobalData.SETAPPLAnguage(this);
        sendAnalytics("SCREEN_VISIT_SPLASH");
        Util.appendLogmobicleanTest("", "SCREEN_VISIT_SPLASH", "adsfull.txt");

                    startActivity(new Intent(Splash.this, StartActivity.class));
                    finish();
                }



    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        Application application = getApplication();

        if (!(application instanceof MobiClean)) {
            Log.e("FAILED-->", "Failed to cast application to MyApplication.");
            return;
        }
        super.onStart();
    }



}
