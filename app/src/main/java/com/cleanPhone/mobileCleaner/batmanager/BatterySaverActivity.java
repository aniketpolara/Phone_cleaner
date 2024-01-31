package com.cleanPhone.mobileCleaner.batmanager;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.cleanPhone.mobileCleaner.CommonResultActivity;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.customview.RevealView;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BatterySaverActivity extends ParentActivity implements View.OnClickListener {
    private TextView ads_message;
    private TextView ads_title;
    private int deviceHeight;
    private int deviceWidth;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    private Handler handler;
    private List<ResolveInfo> installedAppList;
    private RevealView iv_battery;
    public Toolbar j;
    public Context k;
    public FrameLayout l;
    public ImageView back;
    int admob = 3;

    private RelativeLayout layout_one;
    public TextView m;
    public TextView n;
    private int percentage;
    private Timer timer;
    private int level_value = 0;
    private String TAG = "BatterySaverActivity";

    private void getDeviceDimension() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
        String str = this.TAG;
        Log.w(str, "deviceWidth" + this.deviceWidth);
        String str2 = this.TAG;
        Log.w(str2, "deviceHeight" + this.deviceWidth);
        setDimension();
        RevealView revealView = this.iv_battery;
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.battery_booster_animation_h);
        int i = this.deviceHeight;
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeResource, (i * 30) / 100, (i * 30) / 100, false);
        int i2 = this.deviceHeight;
        revealView.setSecondBitmap(createScaledBitmap, (i2 * 30) / 100, (i2 * 30) / 100);
        this.percentage = 100;
        Timer timer = new Timer();
        this.timer = timer;
        timer.schedule(new TimerTask() { // from class: com.mobiclean.phoneclean.batmanager.BatterySaverActivity.3
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                BatterySaverActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (BatterySaverActivity.this.percentage >= 0) {
                            BatterySaverActivity.this.iv_battery.setPercentage(BatterySaverActivity.this.percentage);
                            BatterySaverActivity.r(BatterySaverActivity.this);
                            return;
                        }
                        BatterySaverActivity.this.navigateToCommonResultScreen();
                        cancel();
                    }
                });
            }
        }, 0L, 50L);
    }

    private void getId() {
        this.handler = new Handler();
        this.layout_one = (RelativeLayout) findViewById(R.id.layout_one);
        this.l = (FrameLayout) findViewById(R.id.frame_mid_laysss);
        this.m = (TextView) findViewById(R.id.ads_btn_countinue);
        this.n = (TextView) findViewById(R.id.ads_btn_cancel);
        this.ads_title = (TextView) findViewById(R.id.dialog_title);
        this.ads_message = (TextView) findViewById(R.id.dialog_msg);
        this.back = (ImageView) findViewById(R.id.iv_back);
        this.n.setOnClickListener(this);
        this.m.setOnClickListener(this);
        this.iv_battery = (RevealView) findViewById(R.id.iv_battery);

    }

    public void navigateToCommonResultScreen() {

            Intent intent = new Intent(BatterySaverActivity.this, CommonResultActivity.class);
            intent.putExtra(GlobalData.REDIRECTNOTI, true);
            intent.putExtra("FROMNOTI", true);
            intent.putExtra("DATA", getString(R.string.mbc_one_battery_boost));
            intent.putExtra("TYPE", "BOOST");
            finish();
            startActivity(intent);




    }

    public static int r(BatterySaverActivity batterySaverActivity) {
        int i = batterySaverActivity.percentage;
        batterySaverActivity.percentage = i - 1;
        return i;
    }

    private void redirectToNoti() {
        getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
    }

    private void setDimension() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.iv_battery.getLayoutParams();
        int i = this.deviceHeight;
        layoutParams.width = (i * 30) / 100;
        layoutParams.height = (i * 30) / 100;
        layoutParams.setMargins(0, (i * 25) / 100, 0, 0);
        this.iv_battery.setLayoutParams(layoutParams);
    }

    public void finishBatteryAnimation() {
    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
        }
        dialog.setContentView(R.layout.new_dialog_junk_cancel);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -1);
        dialog.getWindow().setGravity(17);

        ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_battery_booster));
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_battery_saver));
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(String.format(getResources().getString(R.string.mbc_simple_back_press), getResources().getString(R.string.mbc_battery_scan_txt)));
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BatterySaverActivity.this.multipleClicked() || BatterySaverActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BatterySaverActivity.this.multipleClicked()) {
                    return;
                }
                BatterySaverActivity.this.timer.cancel();
                dialog.dismiss();
                Util.appendLogmobiclean(BatterySaverActivity.this.TAG, "backpressed pDialog finish ", "");
                GlobalData.processDataList.clear();
                BatterySaverActivity.this.finish();
            }
        });
        Toast.makeText(this.k, getString(R.string.battery_boost_process_wait), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        view.getId();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_battery_saver);
        Util.saveScreen(getClass().getName(), this);


        showInterstialAds(BatterySaverActivity.this);

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

        this.k = this;
        getId();
        redirectToNoti();
        getDeviceDimension();
        new SharedPrefUtil(this).saveLastTimeUsed(SharedPrefUtil.LUSED_BATTERY, System.currentTimeMillis());
        this.installedAppList = Util.getInstalledAppList(this.k);
        sendAnalytics("SCREEN_VISIT_F_" + getClass().getSimpleName());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.level_value = 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}