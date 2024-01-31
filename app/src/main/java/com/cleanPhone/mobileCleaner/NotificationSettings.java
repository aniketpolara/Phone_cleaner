package com.cleanPhone.mobileCleaner;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.codersroute.flexiblewidgets.FlexibleSwitch;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;

public class NotificationSettings extends ParentActivity {
    public Context j;
    public ScrollView k;
    private TextView tv_charging_noti_detail;
    private TextView tv_cpu_cooler_noti_detail;
    private TextView tv_duplicates_noti_detail;
    private TextView tv_junk_noti_detail;
    private TextView tv_noti_cleaner_noti_detail;
    private TextView tv_ram_noti_detail;
    private TextView tv_space_noti_detail;
    private TextView tv_turbo_cleaner_noti_detail;
    private TextView tv_whtsapp_noti_detail;
    public SharedPrefUtil sharedPrefUtil;
    public ImageView back;
    int admob = 3;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onCreate(Bundle bundle) {
        FlexibleSwitch switchCompat = null;
        FlexibleSwitch switchCompat2 = null;

        FlexibleSwitch switchCompat3 = null;
        String str = null;
        FlexibleSwitch switchCompat4 = null;
        String str2 = null;
        String str3 = null;
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_notification_settings);



        showInterstialAds(NotificationSettings.this);

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


        Util.saveScreen(getClass().getName(), this);
        sendAnalytics("SCREEN_VISIT_" + getClass().getSimpleName());
        this.j = this;

        this.k = (ScrollView) findViewById(R.id.scrol_view_set);
        this.back = (ImageView) findViewById(R.id.iv_back);
        this.tv_junk_noti_detail = (TextView) findViewById(R.id.tv_junk_noti_detail);
        this.tv_ram_noti_detail = (TextView) findViewById(R.id.tv_ram_noti_detail);
        this.tv_duplicates_noti_detail = (TextView) findViewById(R.id.tv_duplicates_noti_detail);
        this.tv_cpu_cooler_noti_detail = (TextView) findViewById(R.id.tv_cpu_cooler_noti_detail);
        this.tv_turbo_cleaner_noti_detail = (TextView) findViewById(R.id.tv_turbo_cleaner_noti_detail);
        this.tv_whtsapp_noti_detail = (TextView) findViewById(R.id.tv_whtsapp_noti_detail);
        this.tv_noti_cleaner_noti_detail = (TextView) findViewById(R.id.tv_noti_cleaner_noti_detail);
        FlexibleSwitch switchCompat5 = (FlexibleSwitch) findViewById(R.id.noti_child_switch1);
        FlexibleSwitch switchCompat6 = (FlexibleSwitch) findViewById(R.id.noti_child_switch2);
        FlexibleSwitch switchCompat7 = (FlexibleSwitch) findViewById(R.id.noti_child_switch3);
        FlexibleSwitch switchCompat8 = (FlexibleSwitch) findViewById(R.id.noti_child_switch8);
        FlexibleSwitch switchCompat9 = (FlexibleSwitch) findViewById(R.id.noti_child_switch5);
        FlexibleSwitch switchCompat13 = (FlexibleSwitch) findViewById(R.id.noti_child_switch10);
        FlexibleSwitch switchCompat14 = (FlexibleSwitch) findViewById(R.id.noti_child_switch11);
        if (displaymetrics == null) {
            displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        }
        TextView textView = (TextView) findViewById(R.id.toolbar_title_noti);
        textView.setText(R.string.mbc_title_activity_setting);
        if (Util.isConnectingToInternet(this)) {
            Util.isAdsFree(this.j);
        }
        sharedPrefUtil = new SharedPrefUtil(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        if (!sharedPrefUtil.getBooleanToggle(SharedPrefUtil.NOTIJUNK)) {
            this.tv_junk_noti_detail.setText(getString(R.string.mbc_junk_noti_detail));
        } else {
            this.tv_junk_noti_detail.setText(getString(R.string.mbc_junk_noti_detail_off));
        }
        if (!sharedPrefUtil.getBooleanToggle(SharedPrefUtil.NOTIBOOST)) {
            this.tv_ram_noti_detail.setText(getString(R.string.mbc_ram_noti_detail));
        } else {
            this.tv_ram_noti_detail.setText(getString(R.string.mbc_ram_noti_detail_off));
        }
        if (!sharedPrefUtil.getBooleanToggle(SharedPrefUtil.NOTIDUP)) {
            this.tv_duplicates_noti_detail.setText(getString(R.string.mbc_duplicates_noti_detail));
        } else {
            this.tv_duplicates_noti_detail.setText(getString(R.string.mbc_duplicates_noti_detail_off));
        }

        if (!sharedPrefUtil.getBooleanToggle(SharedPrefUtil.NOTICOOLER)) {
            TextView textView3 = this.tv_cpu_cooler_noti_detail;
            str = SharedPrefUtil.NOTICOOLER;
            textView3.setText(getString(R.string.mbc_cpu_cooler_noti_detail));
        } else {
            str = SharedPrefUtil.NOTICOOLER;
            this.tv_cpu_cooler_noti_detail.setText(getString(R.string.mbc_cpu_cooler_noti_detail_off));
        }
        if (!sharedPrefUtil.getBooleanToggle("notiwh")) {
            switchCompat4 = switchCompat9;
            this.tv_whtsapp_noti_detail.setText(getString(R.string.mbc_whtsapp_noti_detail));
        } else {
            switchCompat4 = switchCompat9;
            this.tv_whtsapp_noti_detail.setText(getString(R.string.mbc_whtsapp_noti_detail_off));
        }
        if (!sharedPrefUtil.getBooleanToggle("notinc")) {
            TextView textView4 = this.tv_noti_cleaner_noti_detail;
            str2 = SharedPrefUtil.NOTISOCIALCLEANING;
            textView4.setText(getString(R.string.mbc_noti_cleaner_noti_detail));
        } else {
            str2 = SharedPrefUtil.NOTISOCIALCLEANING;
            this.tv_noti_cleaner_noti_detail.setText(getString(R.string.mbc_noti_cleaner_noti_detail_off));
        }
        switchCompat5.setChecked(sharedPrefUtil.getBooleanToggle(SharedPrefUtil.NOTIJUNK));
        switchCompat13.setChecked(sharedPrefUtil.getBooleanToggle("notiwh"));
        switchCompat14.setChecked(sharedPrefUtil.getBooleanToggle("notinc"));
        switchCompat6.setChecked(sharedPrefUtil.getBooleanToggle(SharedPrefUtil.NOTIBOOST));
        switchCompat7.setChecked(sharedPrefUtil.getBooleanToggle(SharedPrefUtil.NOTIDUP));
        FlexibleSwitch switchCompat162 = switchCompat4;
        switchCompat162.setChecked(sharedPrefUtil.getBooleanToggle(str2));
        FlexibleSwitch switchCompat172 = switchCompat3;
        FlexibleSwitch switchCompat182 = switchCompat;
        FlexibleSwitch switchCompat192 = switchCompat2;

        switchCompat5.addOnStatusChangedListener(new FlexibleSwitch.OnStatusChangedListener() {
            @Override
            public void onStatusChanged(boolean z) {
                sharedPrefUtil.saveBooleanToggle(SharedPrefUtil.NOTIJUNK, z);
                if (z) {
                    NotificationSettings.this.tv_junk_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_junk_noti_detail));
                } else {
                    NotificationSettings.this.tv_junk_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_junk_noti_detail_off));
                }
            }
        });
        switchCompat6.addOnStatusChangedListener(new FlexibleSwitch.OnStatusChangedListener() {
            @Override
            public void onStatusChanged(boolean z) {
                sharedPrefUtil.saveBooleanToggle(SharedPrefUtil.NOTIBOOST, z);
                if (z) {
                    NotificationSettings.this.tv_ram_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_ram_noti_detail));
                } else {
                    NotificationSettings.this.tv_ram_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_ram_noti_detail_off));
                }
            }
        });

        switchCompat7.addOnStatusChangedListener(new FlexibleSwitch.OnStatusChangedListener() {
            @Override
            public void onStatusChanged(boolean z) {
                sharedPrefUtil.saveBooleanToggle(SharedPrefUtil.NOTIDUP, z);
                if (z) {
                    NotificationSettings.this.tv_duplicates_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_duplicates_noti_detail));
                } else {
                    NotificationSettings.this.tv_duplicates_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_duplicates_noti_detail_off));
                }
            }
        });


        switchCompat162.addOnStatusChangedListener(new FlexibleSwitch.OnStatusChangedListener() {
            @Override
            public void onStatusChanged(boolean z) {
                sharedPrefUtil.saveBooleanToggle(SharedPrefUtil.NOTISOCIALCLEANING, z);
                if (z) {
                    NotificationSettings.this.tv_turbo_cleaner_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_turbo_cleaner_noti_detail));
                } else {
                    NotificationSettings.this.tv_turbo_cleaner_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_turbo_cleaner_noti_detail_off));
                }
            }
        });

        switchCompat8.addOnStatusChangedListener(new FlexibleSwitch.OnStatusChangedListener() { // from class: com.mobiclean.phoneclean.NotificationSettings.7
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onStatusChanged(boolean z) {
                sharedPrefUtil.saveBooleanToggle(SharedPrefUtil.NOTICOOLER, z);
                if (z) {
                    NotificationSettings.this.tv_cpu_cooler_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_cpu_cooler_noti_detail));
                } else {
                    NotificationSettings.this.tv_cpu_cooler_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_cpu_cooler_noti_detail_off));
                }
            }
        });
        switchCompat13.addOnStatusChangedListener(new FlexibleSwitch.OnStatusChangedListener() {// from class: com.mobiclean.phoneclean.NotificationSettings.8
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onStatusChanged(boolean z) {
                sharedPrefUtil.saveBooleanToggle("notiwh", z);
                if (z) {
                    NotificationSettings.this.tv_whtsapp_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_whtsapp_noti_detail));
                } else {
                    NotificationSettings.this.tv_whtsapp_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_whtsapp_noti_detail_off));
                }
            }
        });
        switchCompat14.addOnStatusChangedListener(new FlexibleSwitch.OnStatusChangedListener() { // from class: com.mobiclean.phoneclean.NotificationSettings.9
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onStatusChanged(boolean z) {
                sharedPrefUtil.saveBooleanToggle("notinc", z);
                if (z) {
                    NotificationSettings.this.tv_noti_cleaner_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_noti_cleaner_noti_detail));
                } else {
                    NotificationSettings.this.tv_noti_cleaner_noti_detail.setText(NotificationSettings.this.getString(R.string.mbc_noti_cleaner_noti_detail_off));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        finish();
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.k.scrollTo(0, 0);
    }


}