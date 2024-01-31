package com.cleanPhone.mobileCleaner.fragment;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.facebook.login.widget.ToolTipPopup;
import com.google.android.gms.ads.AdRequest;
import com.cleanPhone.mobileCleaner.AnimatedBoostActivity;
import com.cleanPhone.mobileCleaner.CommonResultActivity;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.JunkScanActivity;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.animate.CircleProgressView;
import com.cleanPhone.mobileCleaner.batmanager.BatterySaverActivity;
import com.cleanPhone.mobileCleaner.bservices.AppForegroundService;
import com.cleanPhone.mobileCleaner.gmebooster.GameBoostActivity;
import com.cleanPhone.mobileCleaner.similerphotos.DuplicacyMidSettings;
import com.cleanPhone.mobileCleaner.tools.SpaceManagerActivity;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.text.DecimalFormat;

public class HomeFragment extends Fragment implements View.OnClickListener, NestedScrollView.OnScrollChangeListener {
    public Context V;
    public ImageView W;
    public SharedPrefUtil Y;
    public TextView Z;
    public TextView a0;
    public TextView b0;
    public TextView c0;
    public TextView d0;
    public TextView e0;
    public int f0;
    public int X = 0;
    int admob = 2;
    int click = 0;
    int numOfClick = 3;
    AdRequest adRequest;
    View tv_space;
    private CircleProgressView circleProgressView;
    private RelativeLayout circulerLayout;
    private int deviceHeight;
    private int deviceWidth;
    private ImageView iv_home_rocket;
    private LinearLayout layout_modules;
    private String memAvail;
    private int memPerExternal;
    private ProgressBar pbar_ram, pbar_memory;
    private int preProgressRam;
    private int preProgressSpace;
    private long ramsaveSize;
    private RelativeLayout realTimeLayout;
    private TextView tv_percentage;
    private TextView tv_ramused;
    private TextView tv_spaceused;
    private TextView tv_symbol;
    private TextView tv_totram;
    private TextView tv_totspace;
    private View view;
    private final String TAG = "HomeFragment";
    private long mLastClickTime = 0;
    private boolean notProceeded = true;


    private void animateProgress() {
        this.circleProgressView.setSpinnerStrokeCap(Paint.Cap.ROUND);
        this.circleProgressView.setBarStrokeCap(Paint.Cap.ROUND);
    }


    @SuppressLint("ResourceType")
    private void init(View view) {
        this.tv_ramused = (TextView) view.findViewById(R.id.tv_ramused);
        this.tv_totram = (TextView) view.findViewById(R.id.tv_totram);
        this.tv_totspace = (TextView) view.findViewById(R.id.tv_totspace);
        this.tv_percentage = (TextView) view.findViewById(R.id.tv_per);
        this.tv_symbol = (TextView) view.findViewById(R.id.tv_sym);
        this.tv_spaceused = (TextView) view.findViewById(R.id.tv_spaceused);
        this.realTimeLayout = (RelativeLayout) view.findViewById(R.id.real_time_homelayout);
        this.circulerLayout = (RelativeLayout) view.findViewById(R.id.circulerlayout);
        this.pbar_ram = (ProgressBar) view.findViewById(R.id.pbar_ram);
        this.pbar_memory = (ProgressBar) view.findViewById(R.id.pbar_memory);
        this.circleProgressView = (CircleProgressView) view.findViewById(R.id.circleView);
        ((TextView) view.findViewById(R.id.tv_device)).setText(getString(R.string.mbc_analyzing).replace("DO_NOT_TRANSLATE", Util.capitalizeFirstLetter(Build.BRAND)));


        Context context = getActivity();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());

        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        long usedavailable = bytesAvailable - stat.getAvailableBytes();


        long size = memoryInfo.totalMem;
        long available = memoryInfo.availMem;
        long used = size - available;

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if (size < sizeMb)
            tv_totram.setText(df.format(size / sizeKb) + " Kb");
        else if (size < sizeGb)
            tv_totram.setText(df.format(size / sizeMb) + " Mb");
        else if (size < sizeTerra)
            tv_totram.setText(df.format(size / sizeGb) + " Gb");

        if (used < sizeMb)
            tv_ramused.setText(df.format(used / sizeKb) + " Kb" + "/");
        else if (used < sizeGb)
            tv_ramused.setText(df.format(used / sizeMb) + " Mb" + "/");
        else if (used < sizeTerra)
            tv_ramused.setText(df.format(used / sizeGb) + " Gb" + "/");

        if (usedavailable < sizeMb)
            tv_spaceused.setText(df.format(usedavailable / sizeKb) + " Kb" + "/");
        else if (usedavailable < sizeGb)
            tv_spaceused.setText(df.format(usedavailable / sizeMb) + " Mb" + "/");
        else if (usedavailable < sizeTerra)
            tv_spaceused.setText(df.format(usedavailable / sizeGb) + " Gb" + "/");

        if (bytesAvailable < sizeMb)
            tv_totspace.setText(df.format(bytesAvailable / sizeKb) + " Kb");
        else if (bytesAvailable < sizeGb)
            tv_totspace.setText(df.format(bytesAvailable / sizeMb) + " Mb");
        else if (bytesAvailable < sizeTerra)
            tv_totspace.setText(df.format(bytesAvailable / sizeGb) + " Gb");

        long progress = (used * 100) / size;
        HomeFragment.this.pbar_ram.setProgress((int) progress);
        HomeFragment.this.circleProgressView.setValueAnimated(progress, 1000L);
        HomeFragment.this.tv_percentage.setText((int) progress + "");
        long pro_storage = (usedavailable * 100) / bytesAvailable;
        HomeFragment.this.pbar_memory.setProgress((int) pro_storage);


    }

    public void E0() {
        Util.isHome = false;
        try {
            Util.appendLogmobiclean(this.TAG, "junk_cleaner click home screen", GlobalData.FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(this.V);
        if (Util.checkTimeDifference("" + System.currentTimeMillis(), sharedPrefUtil.getString(SharedPrefUtil.JUNCCLEANTIME)) > GlobalData.junccleanPause) {
            startActivity(new Intent(requireContext(), JunkScanActivity.class));
            return;
        }
        Intent intent = new Intent(requireContext(), CommonResultActivity.class);
        GlobalData.cacheCheckedAboveMarshmallow = false;
        try {
            MobiClean.getInstance().junkData.sys_cache_deleted = true;
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        intent.putExtra("DATA", getResources().getString(R.string.mbc_cleaning_aborted));
        intent.putExtra("TYPE", "JUNK_ALLREADY");
        GlobalData.loadAds = false;
        startActivity(intent);
    }


    private void rocketAnimation() {
    }

    private void setAnim() {
        animateProgress();
    }

    private void setDeviceDimensions() {
        Util.appendLogmobiclean(this.TAG, "method:setDeviceDimensions", "");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.V.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
    }

    private void setLayoutParams() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.circulerLayout.getLayoutParams();
        int i = ParentActivity.displaymetrics.widthPixels;
        layoutParams.height = (i * 47) / 100;
        layoutParams.width = (i * 47) / 100;
        this.circulerLayout.setLayoutParams(layoutParams);
        int i2 = (int) ((ParentActivity.displaymetrics.widthPixels * 2.5f) / 100.0f);
        this.circulerLayout.setPadding(i2, i2, i2, i2);
        this.circleProgressView.setBarWidth(i2);
        this.circleProgressView.setRimWidth(i2);
        this.circleProgressView.setRimColor(Color.parseColor("#1c58b1"));
        this.circleProgressView.setBarColor(Color.parseColor("#0495ED"));
        this.circleProgressView.setTextSize(0);
        this.circleProgressView.setText("12");
    }

    public void setProgress() {
        this.preProgressSpace = this.memPerExternal;
        new Handler();
        this.preProgressRam = (int) this.ramsaveSize;
        this.f0 = 0;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                HomeFragment homeFragment = HomeFragment.this;
                int i = homeFragment.f0 + 1;
                homeFragment.f0 = i;
                if (i <= homeFragment.preProgressRam) {
                    handler.postDelayed(this, 15L);
                }
            }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.circulerlayout:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }

                startActivity(new Intent(getActivity(), SpaceManagerActivity.class));


                return;
            case R.id.card_battery_saver:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }
                startActivity(new Intent(getActivity(), BatterySaverActivity.class));
            case R.id.card_junk_cleaner:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }

                startActivity(new Intent(getActivity(), JunkScanActivity.class));


            case R.id.card_phone_boost:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }

                startActivity(new Intent(getActivity(), AnimatedBoostActivity.class));


                return;
            case R.id.card_similar_photos:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }
                GlobalData.fromSpacemanager = false;
                startActivity(new Intent(getActivity(), DuplicacyMidSettings.class));


                return;
            case R.id.card_social_cleaner:
                if (((HomeActivity) getActivity()).multipleClicked()) {
                    return;
                }
                Util.isHome = false;

                startActivity(new Intent(getActivity(), GameBoostActivity.class));


                return;
            default:
        }
    }


    @SuppressLint("ResourceType")
    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
        this.V = requireActivity();
        this.Y = new SharedPrefUtil(this.V);
        Util.isHome = true;
        ((TextView) getActivity().findViewById(R.id.tv_title)).setTypeface(Typeface.create("sans-serif-thi", Typeface.NORMAL));
        setDeviceDimensions();
        init(this.view);
        setLayoutParams();
        setAnim();
        rocketAnimation();
        try {
            setProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Util.isAdsFree(getActivity());
        } catch (Exception e2) {
            e2.printStackTrace();
        }

        this.view.findViewById(R.id.card_junk_cleaner).setOnClickListener(this);
        this.view.findViewById(R.id.circulerlayout).setOnClickListener(this);
        this.realTimeLayout.setOnClickListener(this);
        this.view.findViewById(R.id.card_battery_saver).setOnClickListener(this);
        this.view.findViewById(R.id.card_social_cleaner).setOnClickListener(this);
        this.view.findViewById(R.id.card_similar_photos).setOnClickListener(this);
        this.view.findViewById(R.id.card_phone_boost).setOnClickListener(this);
        this.Y.getBoolean(SharedPrefUtil.APP_NEXT_AD_SHOW);
        this.Y.getBoolean_lock(SharedPrefUtil.SHOW_CHARGEING);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, ToolTipPopup.DEFAULT_POPUP_DISPLAY_TIME);

        showInterstialAds(getActivity());
        LinearLayout llBanner = view.findViewById(R.id.llBanner);
        DH_GllobalItem.fbAdaptiveBanner(getActivity(), llBanner, new DH_GllobalItem.BannerCallback() {
            @Override
            public void onAdLoad() {
                llBanner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFail() {
                llBanner.setVisibility(View.GONE);
            }
        });

        return this.view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (DH_GllobalItem.fbNative == null) {
            DH_GllobalItem.loadNativeAd(getActivity());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("FACEBOOK_ADS", "Home screen destroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        this.notProceeded = false;
        Log.i("FACEBOOK_ADS", "Home screen pause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Util.isHome = true;
        this.mLastClickTime = 0L;
        this.notProceeded = true;
        Log.i("FACEBOOK_ADS", "Home Screen resume");
        setAdsVisibility();
        DH_GllobalItem.errorAds(getActivity());


    }

    @Override
    public void onScrollChange(NestedScrollView nestedScrollView, int i, int i2, int i3, int i4) {
    }

    public void setAdsVisibility() {
    }

    public void setRealTimeProtectionText(boolean z) {
        if (z) {
            if (Build.VERSION.SDK_INT >= 26) {
                Intent intent = new Intent(getActivity(), AppForegroundService.class);
                intent.setAction(AppForegroundService.ACTION_START_FOREGROUND_SERVICE);
                getActivity().startService(intent);
            }
        } else if (Build.VERSION.SDK_INT >= 26) {
            Intent intent2 = new Intent(getActivity(), AppForegroundService.class);
            intent2.setAction(AppForegroundService.ACTION_STOP_FOREGROUND_SERVICE);
            getActivity().startService(intent2);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
    }

}