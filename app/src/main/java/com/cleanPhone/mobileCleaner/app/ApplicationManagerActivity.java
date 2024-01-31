package com.cleanPhone.mobileCleaner.app;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.utility.DetermineTextSize;
import com.cleanPhone.mobileCleaner.utility.PermitActivity;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppManagerData;

import java.util.ArrayList;
import java.util.List;


public class ApplicationManagerActivity extends PermitActivity {
    public static boolean restored = false;
    private Context context;
    private RelativeLayout hiddenPermissionLayout;
    private ProgressDialog progressDialog;
    private TabLayout tabLayout;
    private TextView unitTv;
    private ViewPager viewPager;
    public ImageView back;
    int admob = 2;

    public static class AppFragmentUninstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList;
        private final List<String> mFragmentTitleList;

        public ViewPagerAdapter(ApplicationManagerActivity applicationManagerActivity, FragmentManager fragmentManager) {
            super(fragmentManager);
            this.mFragmentList = new ArrayList();
            this.mFragmentTitleList = new ArrayList();
        }

        public void b(Fragment fragment, String str) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(str);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        @Override
        public Fragment getItem(int i) {
            return this.mFragmentList.get(i);
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return this.mFragmentTitleList.get(i);
        }
    }

    private void initialize() {
        this.hiddenPermissionLayout = (RelativeLayout) findViewById(R.id.hiddenpermissionlayout);
        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        this.unitTv = (TextView) findViewById(R.id.junkdisplay_sizetv_unit);
        this.back = (ImageView) findViewById(R.id.iv_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getIntent().getLongExtra("SIZE", 0L);
        ((TextView) findViewById(R.id.junkdisplay_sizetv)).setTextSize(0, DetermineTextSize.determineTextSize(((TextView) findViewById(R.id.junkdisplay_sizetv)).getTypeface(), (ParentActivity.displaymetrics.heightPixels * 9) / 100));
        this.unitTv.setTextSize(0, DetermineTextSize.determineTextSize(((TextView) findViewById(R.id.junkdisplay_sizetv)).getTypeface(), (ParentActivity.displaymetrics.heightPixels * 5) / 100));
        findViewById(R.id.permission_close_btn).setOnClickListener(new View.OnClickListener() { // from class: com.mobiclean.phoneclean.app.ApplicationManagerActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ApplicationManagerActivity.this.finish();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void scanapps() {
        new ApplicationManagerWork(this) {
            @Override
            public void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            public void onPostExecute(AppManagerData appManagerData) {
                super.onPostExecute(appManagerData);
                ApplicationManagerActivity applicationManagerActivity = ApplicationManagerActivity.this;
                applicationManagerActivity.setupViewPager(applicationManagerActivity.viewPager);
                ApplicationManagerActivity.this.setText();
                ApplicationManagerActivity.this.tabLayout.setupWithViewPager(ApplicationManagerActivity.this.viewPager);
                ApplicationManagerActivity.this.tabLayout.getTabAt(0).setText(String.format(ApplicationManagerActivity.this.getResources().getString(R.string.mbc_install).replace("DO_NOT_TRANSLATE", "%d"), Integer.valueOf(MobiClean.getInstance().appManagerData.installedApps.size())));
            }
        }.execute(new String[0]);
    }

    private void setDeviceDimensions() {
        if (ParentActivity.displaymetrics == null) {
            ParentActivity.displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(ParentActivity.displaymetrics);
        }
    }

    public void setText() {
        long j = MobiClean.getInstance().appManagerData.totsize;
        ((TextView) findViewById(R.id.junkdisplay_sizetv)).setText(Util.convertBytes_only(j));
        String convertBytes_unit = Util.convertBytes_unit(j);
        TextView textView = this.unitTv;
        textView.setText("" + convertBytes_unit);
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        viewPagerAdapter.b(new ApplicationFragmentView(), getString(R.string.mbc_installed));
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_app_manager);
        restored = false;
        this.context = this;
        setDeviceDimensions();
        initialize();
        scanapps();

        showInterstialAds(ApplicationManagerActivity.this);
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


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        finish();
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onPermissionsGranted(int i) {
        RelativeLayout relativeLayout = this.hiddenPermissionLayout;
        if (relativeLayout != null) {
            relativeLayout.setVisibility(View.GONE);
            this.context = this;
            setDeviceDimensions();
            scanapps();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (restored) {
            try {
                scanapps();
                restored = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
