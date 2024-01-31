package com.cleanPhone.mobileCleaner.socialmedia;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.share.internal.ShareConstants;

import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.listadapt.SocialResultAdapter;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.util.ArrayList;

public class SocialScanResultActivity extends ParentActivity implements View.OnClickListener {
    private TextView ads_message;
    private TextView ads_title;
    private int deviceHeight;
    private int deviceWidth;
    private DisplayMetrics displaymetrics;
    public RelativeLayout j;
    public RelativeLayout k;
    public FrameLayout l;
    public TextView m;
    private SocialResultAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mediaNameRecyclerView;
    public TextView n;
    private TextView tvTotalSize;
    private TextView unitTv;

    private void clearNotification() {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.cancel(832);
                notificationManager.cancel(700);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        this.displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(this.displaymetrics);
        }
        DisplayMetrics displayMetrics = this.displaymetrics;
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
        this.j = (RelativeLayout) findViewById(R.id.layout_one);
        this.k = (RelativeLayout) findViewById(R.id.layout_two);
        this.l = (FrameLayout) findViewById(R.id.frame_mid_laysss);
        this.m = (TextView) findViewById(R.id.ads_btn_countinue);
        this.n = (TextView) findViewById(R.id.ads_btn_cancel);
        this.ads_title = (TextView) findViewById(R.id.dialog_title);
        this.ads_message = (TextView) findViewById(R.id.dialog_msg);
        this.n.setOnClickListener(this);
        this.m.setOnClickListener(this);

        this.tvTotalSize = (TextView) findViewById(R.id.junkdisplay_sizetv);
        this.unitTv = (TextView) findViewById(R.id.junkdisplay_sizetv_unit);
        if (getIntent().getBooleanExtra(ShareConstants.TITLE, false)) {
            ((TextView) findViewById(R.id.toolbar_title)).setText(getString(R.string.mbc_turbo_cleaner_social));
        }
    }

    private void update() {
        if (this.tvTotalSize != null) {
            MobiClean.getInstance().mediaAppModule.refresh();
            this.tvTotalSize.setText(Util.convertBytes_only(MobiClean.getInstance().mediaAppModule.totalSize));
            TextView textView = this.tvTotalSize;
            String convertBytes_unit = Util.convertBytes_unit(MobiClean.getInstance().mediaAppModule.totalSize);
            TextView textView2 = this.unitTv;
            textView2.setText("" + convertBytes_unit);
            this.mAdapter = new SocialResultAdapter(this, MobiClean.getInstance().mediaAppModule.getAppsHavingData());
            this.mediaNameRecyclerView.setLayoutManager(this.mLayoutManager);
            this.mediaNameRecyclerView.setItemAnimator(new DefaultItemAnimator());
            this.mediaNameRecyclerView.setAdapter(this.mAdapter);
        }
    }


    @Override
    public void onBackPressed() {
        ArrayList arrayList;
        Util.appendLogmobiclean("SocialScanResultAct", "CalculateToalSizeTask---AsyncTask---onPreExecute", GlobalData.FILE_NAME);
        GlobalData.backPressedResult = true;
        try {
            arrayList = MobiClean.getInstance().mediaAppModule.getAppsHavingData();
        } catch (Exception e) {
            e.printStackTrace();
            arrayList = null;
        }
        if (arrayList != null && arrayList.size() != 0) {
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
            View space;
             if (getIntent().getBooleanExtra("FROMHOME", false)) {
                ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_turbo_cleaner));
                try {
                    ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_social_cleaner));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_turbo_cleaner_social));
                try {
                    ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.secure_back));
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
            ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(String.format(getResources().getString(R.string.mbc_simple_back_press), getResources().getString(R.string.mbc_junk_result_txt)));
            dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SocialScanResultActivity.this.multipleClicked()) {
                        return;
                    }
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SocialScanResultActivity.this.multipleClicked()) {
                        return;
                    }
                    MobiClean.getInstance().socialModule = null;
                    System.runFinalization();
                    Runtime.getRuntime().runFinalization();
                    System.gc();
                    dialog.dismiss();
                    SocialScanResultActivity.this.finish();
                    if (SocialScanResultActivity.this.getIntent().getBooleanExtra("FROMNOTI", true)) {
                        SocialScanResultActivity.this.startActivity(new Intent(SocialScanResultActivity.this, HomeActivity.class));
                    }
                }
            });
            dialog.show();
            return;
        }
        MobiClean.getInstance().socialModule = null;
        System.runFinalization();
        Runtime.getRuntime().runFinalization();
        System.gc();
        finish();
        if (getIntent().getBooleanExtra("FROMNOTI", true)) {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        if (multipleClicked()) {
            return;
        }
        if (view.getId() == R.id.ads_btn_cancel) {
            this.k.setVisibility(View.GONE);
            this.j.setVisibility(View.VISIBLE);
        }
        if (view.getId() == R.id.ads_btn_countinue) {
            MobiClean.getInstance().socialModule = null;
            System.runFinalization();
            Runtime.getRuntime().runFinalization();
            System.gc();
            RelativeLayout relativeLayout = this.k;
            if (relativeLayout != null && relativeLayout.getVisibility() == View.VISIBLE) {
                this.j.setVisibility(View.VISIBLE);
                this.k.setVisibility(View.GONE);
            }
            finish();
            if (getIntent().getBooleanExtra("FROMNOTI", true)) {
                startActivity(new Intent(this, HomeActivity.class));
            }
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_social_scan_result);
        initialize();
        ImageView back = findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        showInterstialAds(SocialScanResultActivity.this);

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

        try {
            this.mAdapter = new SocialResultAdapter(this, MobiClean.getInstance().mediaAppModule.getAppsHavingData());
            this.mLayoutManager = new LinearLayoutManager(getApplicationContext());
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            this.mediaNameRecyclerView = recyclerView;
            recyclerView.setLayoutManager(this.mLayoutManager);
            this.mediaNameRecyclerView.setItemAnimator(new DefaultItemAnimator());
            this.mediaNameRecyclerView.setAdapter(this.mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearNotification();
        sendAnalytics("SCREEN_VISIT_" + getClass().getSimpleName());
    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
