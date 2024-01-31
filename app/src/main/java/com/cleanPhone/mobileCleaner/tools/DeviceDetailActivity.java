package com.cleanPhone.mobileCleaner.tools;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.exifinterface.media.ExifInterface;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.ProgressBarAnimation;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.bservices.BatteryParamStatus;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

import kotlin.text.Typography;

public class DeviceDetailActivity extends ParentActivity {
    private String TAG = DeviceDetailActivity.class.getSimpleName();
    private String availableRAMSize;
    private BroadcastReceiver batteryInfoReceiver;
    private boolean bluetooth_enable;
    private int deviceHeight;
    private int deviceWidth;
    private boolean gps_enable;
    public TextView j;
    public TextView k;
    public TextView l;
    int admob = 1;

    View space;
    public TextView m;
    private String memAvail;
    private int memPerExternal;
    private String memTotal;
    public TextView n;
    public TextView o;
    public TextView usedram, progress_size_text, progress_size_unit, progress_storage_size, progress_storage_unit;
    private ProgressBar progress_ram;
    private ProgressBar progress_storage;
    public TextView q;
    private long ramSaveSize;
    private String remainingRAM;
    private String remainingSpace;
    public TextView s;
    public TextView t;
    private String totRam;
    private TextView tvtot_ram;
    private TextView tvtot_space;
    public long u;
    private RelativeLayout up_rel_lay;
    public Context v;
    public LinearLayout w;
    private boolean wifi_enable;
    public ImageView back;


    public class CheckValueTask extends AsyncTask<String, String, String> {
        public ProgressDialog f5149a;

        private CheckValueTask() {
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            ProgressDialog progressDialog = new ProgressDialog(DeviceDetailActivity.this.v, R.style.AppCompatAlertDialogStyle);
            this.f5149a = progressDialog;
            progressDialog.setMessage(DeviceDetailActivity.this.getString(R.string.mbc_loading));
            this.f5149a.show();
        }

        @Override
        public String doInBackground(String... strArr) {
            DeviceDetailActivity deviceDetailActivity = DeviceDetailActivity.this;
            deviceDetailActivity.wifi_enable = BatteryParamStatus.wifi_check(deviceDetailActivity.v);
            DeviceDetailActivity deviceDetailActivity2 = DeviceDetailActivity.this;
            deviceDetailActivity2.gps_enable = BatteryParamStatus.gps_check(deviceDetailActivity2.v);
            DeviceDetailActivity deviceDetailActivity3 = DeviceDetailActivity.this;
            deviceDetailActivity3.bluetooth_enable = BatteryParamStatus.bluetooth_check(deviceDetailActivity3.v);
            return null;
        }

        @Override
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            ProgressDialog progressDialog = this.f5149a;
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (DeviceDetailActivity.this.bluetooth_enable) {
                DeviceDetailActivity deviceDetailActivity = DeviceDetailActivity.this;
                deviceDetailActivity.n.setText(deviceDetailActivity.getResources().getString(R.string.mbc_on));

            } else {
                DeviceDetailActivity deviceDetailActivity2 = DeviceDetailActivity.this;
                deviceDetailActivity2.n.setText(deviceDetailActivity2.getResources().getString(R.string.mbc_off));
            }
            if (DeviceDetailActivity.this.wifi_enable) {
                DeviceDetailActivity deviceDetailActivity3 = DeviceDetailActivity.this;
                deviceDetailActivity3.m.setText(deviceDetailActivity3.getResources().getString(R.string.mbc_on));

            } else {
                DeviceDetailActivity deviceDetailActivity4 = DeviceDetailActivity.this;
                deviceDetailActivity4.m.setText(deviceDetailActivity4.getResources().getString(R.string.mbc_off));
            }
            if (DeviceDetailActivity.this.gps_enable) {
                DeviceDetailActivity deviceDetailActivity5 = DeviceDetailActivity.this;
                deviceDetailActivity5.o.setText(deviceDetailActivity5.getResources().getString(R.string.mbc_on));
                return;
            }
            DeviceDetailActivity deviceDetailActivity6 = DeviceDetailActivity.this;
            deviceDetailActivity6.o.setText(deviceDetailActivity6.getResources().getString(R.string.mbc_off));
        }
    }


    private double getBatteryCapacity() {
        try {
            return ((Double) Class.forName("com.android.internal.os.PowerProfile").getMethod("getBatteryCapacity", new Class[0]).invoke(Class.forName("com.android.internal.os.PowerProfile").getConstructor(Context.class).newInstance(this.v), new Object[0])).doubleValue();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE;
        } catch (InstantiationException e3) {
            e3.printStackTrace();
            return FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE;
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
            return FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            return FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE;
        }
    }

    private void getDimension() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
    }


    private void getid() {
        this.j = (TextView) findViewById(R.id.model_name);
        this.k = (TextView) findViewById(R.id.version_name);
        this.l = (TextView) findViewById(R.id.uptime_sys);
        this.w = (LinearLayout) findViewById(R.id.down_ll_lay);
        this.m = (TextView) findViewById(R.id.tv_wifi_status);
        this.n = (TextView) findViewById(R.id.tv_bluetooth_status);
        this.o = (TextView) findViewById(R.id.tv_gps_status);
        this.progress_ram = (ProgressBar) findViewById(R.id.progress_ram);
        this.progress_storage = (ProgressBar) findViewById(R.id.progress_storage);
        this.usedram = (TextView) findViewById(R.id.used_ram);
        this.progress_size_text = (TextView) findViewById(R.id.progress_size_text);
        this.q = (TextView) findViewById(R.id.avail_ram);
        this.s = (TextView) findViewById(R.id.used_space);
        this.t = (TextView) findViewById(R.id.avail_space);
        this.up_rel_lay = (RelativeLayout) findViewById(R.id.up_rel_lay);
        this.up_rel_lay = (RelativeLayout) findViewById(R.id.rel_tot);
        this.tvtot_ram = (TextView) findViewById(R.id.tvtot_ram);
        this.tvtot_space = (TextView) findViewById(R.id.tot_space);
        this.up_rel_lay = (RelativeLayout) findViewById(R.id.up_rel_lay);
        this.back = (ImageView) findViewById(R.id.iv_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void setProgress() {
        TextView textView = this.tvtot_space;
        textView.setText("" + this.memTotal);
        TextView textView2 = this.t;
        textView2.setText("" + this.remainingSpace);
        TextView textView3 = this.s;
        textView3.setText("" + this.memAvail);
        TextView textView4 = this.tvtot_ram;
        textView4.setText("" + this.totRam);
        TextView textView5 = this.q;
        textView5.setText("" + this.remainingRAM);
        this.progress_ram.setProgress((int) ramSaveSize
        );
        this.usedram.setText(this.availableRAMSize);
        this.progress_storage.setProgress(this.memPerExternal);
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.new_activity_device_detail);
        Util.saveScreen(getClass().getName(), this);

        this.v = this;

        showInterstialAds(DeviceDetailActivity.this);

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
            new CheckValueTask().execute(new String[0]);
            int parseInt = Integer.parseInt(String.valueOf(getBatteryCapacity()).split("\\.")[0]);
            ((TextView) findViewById(R.id.uptime_sys)).setText(String.valueOf(parseInt) + " mAh");
        } catch (Exception e) {
            e.printStackTrace();
        }
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.mobiclean.phoneclean.tools.DeviceDetailActivity.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                int intExtra = intent.getIntExtra("status", -1);
                String str = DeviceDetailActivity.this.TAG;
                Log.e(str, "Battery Status : " + intExtra);
                if (intExtra == 1) {
                    ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_status)).setText(DeviceDetailActivity.this.getString(R.string.unknown));
                } else if (intExtra == 2) {
                    ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_status)).setText(DeviceDetailActivity.this.getString(R.string.mbc_charging));
                } else if (intExtra == 3) {
                    ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_status)).setText(DeviceDetailActivity.this.getString(R.string.discharging));
                } else if (intExtra == 4) {
                    ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_status)).setText(DeviceDetailActivity.this.getString(R.string.not_charging));
                } else if (intExtra == 5) {
                    ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_status)).setText(DeviceDetailActivity.this.getString(R.string.full));
                }
                int intExtra2 = intent.getIntExtra(FirebaseAnalytics.Param.LEVEL, -1);
                ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_level)).setText(String.valueOf(intExtra2) + "%");
                float intExtra3 = (float) (((double) intent.getIntExtra("voltage", -1)) * 0.001d);
                ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_voltage)).setText(String.valueOf(intExtra3) + ExifInterface.GPS_MEASUREMENT_INTERRUPTED);
                ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_temp)).setText(String.valueOf(((float) intent.getIntExtra("temperature", -1)) / 10.0f) + " " + Typography.degree + "C");
                switch (intent.getIntExtra("health", -1)) {
                    case 1:
                        ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_health)).setText(DeviceDetailActivity.this.getString(R.string.unknown));
                        return;
                    case 2:
                        ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_health)).setText(DeviceDetailActivity.this.getString(R.string.good));
                        return;
                    case 3:
                        ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_health)).setText(DeviceDetailActivity.this.getString(R.string.overheat));
                        return;
                    case 4:
                        ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_health)).setText(DeviceDetailActivity.this.getString(R.string.dead));
                        return;
                    case 5:
                        ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_health)).setText(DeviceDetailActivity.this.getString(R.string.over_voltage));
                        return;
                    case 6:
                        ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_health)).setText(DeviceDetailActivity.this.getString(R.string.unspecified_failure));
                        return;
                    case 7:
                        ((TextView) DeviceDetailActivity.this.findViewById(R.id.tv_battery_health)).setText(DeviceDetailActivity.this.getString(R.string.cold));
                        return;
                    default:
                        return;
                }
            }
        };
        this.batteryInfoReceiver = broadcastReceiver;
        registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));

        usedram = findViewById(R.id.used_ram);
        this.q = (TextView) findViewById(R.id.avail_ram);
        this.s = (TextView) findViewById(R.id.used_space);
        this.t = (TextView) findViewById(R.id.avail_space);

        this.tvtot_ram = (TextView) findViewById(R.id.tvtot_ram);
        this.tvtot_space = (TextView) findViewById(R.id.tot_space);
        this.progress_ram = (ProgressBar) findViewById(R.id.progress_ram);
        progress_size_text = (TextView) findViewById(R.id.progress_size_text);
        this.progress_storage = (ProgressBar) findViewById(R.id.progress_storage);
        this.progress_size_unit = (TextView) findViewById(R.id.progress_size_unit);
        this.progress_storage_size = (TextView) findViewById(R.id.progress_storage_size);
        this.progress_storage_unit = (TextView) findViewById(R.id.progress_storage_unit);





        Context context = getApplicationContext();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());

        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        long usedavailable = bytesAvailable - stat.getAvailableBytes();
        long spaceavailble = bytesAvailable - usedavailable;


        long size = memoryInfo.totalMem;
        long available = memoryInfo.availMem;
        long used = size - available;

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if (size < sizeMb)
            tvtot_ram.setText(df.format(size / sizeKb) + " KB");
        else if (size < sizeGb)
            tvtot_ram.setText(df.format(size / sizeMb) + " MB");
        else if (size < sizeTerra)
            tvtot_ram.setText(df.format(size / sizeGb) + " GB");

        if (used < sizeMb) {
            progress_size_text.setText(df.format(used / sizeKb));
            progress_size_unit.setText(" KB");
            usedram.setText(df.format(used / sizeKb) + " KB");
        } else if (used < sizeGb) {
            progress_size_text.setText(df.format(used / sizeMb));
            progress_size_unit.setText(" MB");
            usedram.setText(df.format(used / sizeMb) + " MB");
        } else if (used < sizeTerra) {
            progress_size_text.setText(df.format(used / sizeGb));
            progress_size_unit.setText(" GB");
            usedram.setText(df.format(used / sizeGb) + " GB");
        }

        if (available < sizeMb) {
            q.setText(df.format(available / sizeKb) + " KB");
        } else if (available < sizeGb) {
            q.setText(df.format(available / sizeMb) + " MB");
        } else if (available < sizeTerra) {
            q.setText(df.format(available / sizeGb) + " GB");
        }


        if (usedavailable < sizeMb) {
            progress_storage_size.setText(df.format(usedavailable / sizeKb));
            progress_storage_unit.setText(" KB");
            s.setText(df.format(usedavailable / sizeKb) + " KB");
        } else if (usedavailable < sizeGb) {
            progress_storage_size.setText(df.format(usedavailable / sizeMb));
            progress_storage_unit.setText(" MB");
            s.setText(df.format(usedavailable / sizeMb) + " MB");
        } else if (usedavailable < sizeTerra) {
            progress_storage_size.setText(df.format(usedavailable / sizeGb));
            progress_storage_unit.setText(" GB");
            s.setText(df.format(usedavailable / sizeGb) + " GB");
        }

        if (bytesAvailable < sizeMb)
            tvtot_space.setText(df.format(bytesAvailable / sizeKb) + " KB");
        else if (bytesAvailable < sizeGb)
            tvtot_space.setText(df.format(bytesAvailable / sizeMb) + " MB");
        else if (bytesAvailable < sizeTerra)
            tvtot_space.setText(df.format(bytesAvailable / sizeGb) + " GB");

        if (spaceavailble < sizeMb)
            t.setText(df.format(spaceavailble / sizeKb) + " KB");
        else if (spaceavailble < sizeGb)
            t.setText(df.format(spaceavailble / sizeMb) + " MB");
        else if (spaceavailble < sizeTerra)
            t.setText(df.format(spaceavailble / sizeGb) + " GB");


        long progress = (used * 100) / size;
        ProgressBarAnimation mProgressAnimation = new ProgressBarAnimation(progress_ram, 3000);
        mProgressAnimation.setProgress((int) progress);

        long pro_storage = (usedavailable * 100) / bytesAvailable;
        ProgressBarAnimation mProgressAnimationStorage = new ProgressBarAnimation(progress_storage, 3000);
        mProgressAnimationStorage.setProgress((int) pro_storage);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BroadcastReceiver broadcastReceiver = this.batteryInfoReceiver;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onResume() {
        this.v = this;
        getid();
        getDimension();
        TextView textView = this.j;
        textView.setText("" + Build.MODEL);
        TextView textView2 = this.k;
        textView2.setText("" + Build.VERSION.RELEASE);
        try {
            new CheckValueTask().execute(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }
}
