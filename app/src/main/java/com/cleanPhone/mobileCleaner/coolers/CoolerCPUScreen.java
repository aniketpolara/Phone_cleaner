package com.cleanPhone.mobileCleaner.coolers;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.login.widget.ToolTipPopup;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.JunkCleanScreen;
import com.cleanPhone.mobileCleaner.JunkDeleteAnimationScreen;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.wrappers.ProcessWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CoolerCPUScreen extends ParentActivity {
    private static final int MAXCOOLED_VAL = 40;
    private static final int MAX_HOT = 45;
    private LinearLayout btn_cooldowm;
    private Context context;
    private int deviceHeight;
    private ProgressDialog dialog;
    private View divider_view;
    private LinearLayout layout_middel;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    private LinearLayout layoutupper;
    private int maxcooled;
    private float mmmmmm;
    private boolean noti_result_back;
    private ArrayList<ProcessWrapper> processList;
    private ListView runningapplist;
    private SharedPrefUtil sharedPrefUtil;
    private TextView t_cpuuses;
    private StringBuilder text;
    int admob = 2;
    private LinearLayout tvDesc;
    private TextView tv_app_count;
    private TextView tv_temp;
    private TextView tv_tempstatus;
    private TextView tv_text;
    private ArrayList<String> values = new ArrayList<>();
    public int j = 0;

    public class ProcessListAdapter extends BaseAdapter {
        public ImageView f4878a;
        public TextView b;
        public PackageManager f4879c;
        private LayoutInflater mInflater;

        public ProcessListAdapter(Context context) {
            this.f4879c = context.getPackageManager();
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return CoolerCPUScreen.this.processList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = this.mInflater.inflate(R.layout.cupcoller_listitem, (ViewGroup) null);
            this.f4878a = (ImageView) inflate.findViewById(R.id.cpucolerfirst_itemicon);
            this.b = (TextView) inflate.findViewById(R.id.cpucolerfirst_processname);
            ProcessWrapper processWrapper = (ProcessWrapper) CoolerCPUScreen.this.processList.get(i);
            TextView textView = this.b;
            textView.setText("" + processWrapper.appname);
            try {
                this.f4878a.setImageDrawable(this.f4879c.getApplicationInfo(processWrapper.name, 0).loadIcon(this.f4879c));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return inflate;
        }
    }

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

    private void cputemp() {
        this.values.clear();
        for (int i = 0; i < 30; i++) {
            this.text = new StringBuilder();
            File file = new File("sys/devices/virtual/thermal/thermal_zone" + i + DialogConfigs.DIRECTORY_SEPERATOR, JunkCleanScreen.TEMP);
            if (!file.exists()) {
                break;
            }
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    this.text.append(readLine);
                }
                if (Integer.parseInt(this.text.toString()) > 1000) {
                    ArrayList<String> arrayList = this.values;
                    arrayList.add("" + (Integer.parseInt(this.text.toString()) / 1000));
                } else {
                    ArrayList<String> arrayList2 = this.values;
                    arrayList2.add("" + this.text.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        check_max();
    }

    private void ifnoProcessFound() {
        if (this.processList.size() == 0) {
            this.layout_middel.setVisibility(View.GONE);
            this.tvDesc.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, ToolTipPopup.DEFAULT_POPUP_DISPLAY_TIME);
        }
    }

    private void init() {
        this.layoutupper = (LinearLayout) findViewById(R.id.cpucollerfirst_toplayout);

        this.layout_middel = (LinearLayout) findViewById(R.id.cpucollerfirst_middellayout);
        this.runningapplist = (ListView) findViewById(R.id.listview_cpu_cooler_first);
        this.tv_app_count = (TextView) findViewById(R.id.textview_count_cpucoolerfirst);
        this.tv_temp = (TextView) findViewById(R.id.cpucollerfirst_temp);
        this.tv_tempstatus = (TextView) findViewById(R.id.symbol);
        this.t_cpuuses = (TextView) findViewById(R.id.cpucoolerfirst_usage);
        this.tv_text = (TextView) findViewById(R.id.textview_processservies2);
        this.btn_cooldowm = (LinearLayout) findViewById(R.id.btn_cpu_cooler_first);
        this.tvDesc = (LinearLayout) findViewById(R.id.tv_cpucoller);
        if (Build.VERSION.SDK_INT > 23) {
            this.layout_middel.setVisibility(View.GONE);
            this.tvDesc.setVisibility(View.VISIBLE);
        }
    }

    public void kill_servieses() {
        List<ApplicationInfo> installedApplications = getPackageManager().getInstalledApplications(0);
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList ignoredData = getIgnoredData();
        for (ApplicationInfo applicationInfo : installedApplications) {
            if ((applicationInfo.flags & 1) != 1 && !applicationInfo.packageName.equals("com.mobiclean.phoneclean")) {
                if (!ignoredData.contains("" + applicationInfo.packageName) && activityManager != null) {
                    activityManager.killBackgroundProcesses(applicationInfo.packageName);
                }
            }
        }
    }

    private boolean notExists(String str) {
        for (int i = 0; i < this.processList.size(); i++) {
            try {
                if (str.equalsIgnoreCase("" + this.processList.get(i).appname)) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }
        return true;
    }

    public void readUsage() {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("/proc/stat", "r");
            String[] split = randomAccessFile.readLine().split(" +");
            long parseLong = Long.parseLong(split[4]);
            long parseLong2 = Long.parseLong(split[2]) + Long.parseLong(split[3]) + Long.parseLong(split[5]) + Long.parseLong(split[6]) + Long.parseLong(split[7]) + Long.parseLong(split[8]);
            try {
                Thread.sleep(360L);
            } catch (Exception unused) {
            }
            randomAccessFile.seek(0L);
            String readLine = randomAccessFile.readLine();
            randomAccessFile.close();
            String[] split2 = readLine.split(" +");
            long parseLong3 = Long.parseLong(split2[4]);
            long parseLong4 = Long.parseLong(split2[2]) + Long.parseLong(split2[3]) + Long.parseLong(split2[5]) + Long.parseLong(split2[6]) + Long.parseLong(split2[7]) + Long.parseLong(split2[8]);
            this.mmmmmm = ((float) (parseLong4 - parseLong2)) / ((float) ((parseLong4 + parseLong3) - (parseLong2 + parseLong)));
        } catch (IOException e2) {
        }
    }

    private void redirectToNoti() {
        this.noti_result_back = getIntent().getBooleanExtra(GlobalData.NOTI_RESULT_BACK, false);
    }

    private void setDeviceDimensions() {
        ParentActivity.displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ParentActivity.displaymetrics);
        this.deviceHeight = ParentActivity.displaymetrics.heightPixels;
    }

    private void setDimensions() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.layoutupper.getLayoutParams();
        layoutParams.height = (this.deviceHeight * 16) / 100;
        this.layoutupper.setLayoutParams(layoutParams);
    }


    public void check_max() {
        this.j = 0;
        this.maxcooled = 0;
        boolean z = false;
        int i = 100;
        for (int i2 = 0; i2 < this.values.size(); i2++) {
            try {
                String trim = this.values.get(i2).trim();
                if (Integer.parseInt(trim) > 45) {
                    z = true;
                }
                if (Integer.parseInt(trim) > this.j && Integer.parseInt(trim) <= 45) {
                    this.j = Integer.parseInt(this.values.get(i2));
                }
                if (Integer.parseInt(trim) < i && Integer.parseInt(trim) > 0) {
                    i = Integer.parseInt(this.values.get(i2));
                }
                if (Integer.parseInt(trim) <= 40 && Integer.parseInt(trim) >= 35 && Integer.parseInt(trim) > this.maxcooled) {
                    this.maxcooled = Integer.parseInt(trim);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean finalZ = z;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (CoolerCPUScreen.this.maxcooled == 0) {
                    CoolerCPUScreen.this.maxcooled = 40;
                }
                CoolerCPUScreen coolerCPUScreen = CoolerCPUScreen.this;
                if (coolerCPUScreen.j == 0) {
                    coolerCPUScreen.j = 45;
                }
                if (finalZ && coolerCPUScreen.j <= 40) {
                    coolerCPUScreen.j = 45;
                }
                TextView textView = coolerCPUScreen.tv_temp;
                textView.setText("" + CoolerCPUScreen.this.j);
                String string = CoolerCPUScreen.this.sharedPrefUtil.getString(SharedPrefUtil.LASTCOOLTIME);
                if (CoolerCPUScreen.checkTimeDifference("" + System.currentTimeMillis(), string) >= GlobalData.coolPause) {
                    CoolerCPUScreen coolerCPUScreen2 = CoolerCPUScreen.this;
                    if (coolerCPUScreen2.j > 40) {
                        TextView textView2 = coolerCPUScreen2.tv_temp;
                        textView2.setText("" + CoolerCPUScreen.this.j);
                        CoolerCPUScreen.this.btn_cooldowm.setEnabled(true);
                    }
                }
                TextView textView3 = CoolerCPUScreen.this.tv_temp;
                textView3.setText("" + CoolerCPUScreen.this.sharedPrefUtil.getString(SharedPrefUtil.AFTERCOOLTEMP));
                Intent intent = new Intent(CoolerCPUScreen.this.context, JunkDeleteAnimationScreen.class);
                intent.putExtra("DATA", "" + CoolerCPUScreen.this.maxcooled);
                intent.putExtra("TYPE", "COOLER");
                CoolerCPUScreen.this.btn_cooldowm.setEnabled(true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (this.noti_result_back) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
        super.onBackPressed();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_cpu_cooler);
        boolean z = true;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        this.processList = new ArrayList<>();
        for (int i = 0; i < GlobalData.processDataList.size(); i++) {
            if (notExists(GlobalData.processDataList.get(i).appname)) {
                this.processList.add(GlobalData.processDataList.get(i));
            }
        }
        Log.d("CALLED", "here " + GlobalData.processDataList.size() + "  " + this.processList.size());
        this.context = this;
        this.sharedPrefUtil = new SharedPrefUtil(this.context);
        setDeviceDimensions();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT <= 23) {
                    CoolerCPUScreen coolerCPUScreen = CoolerCPUScreen.this;
                    ProcessListAdapter processListAdapter = new ProcessListAdapter(coolerCPUScreen);
                    CoolerCPUScreen.this.runningapplist.setAdapter((ListAdapter) processListAdapter);
                    CoolerCPUScreen.this.tv_app_count.setText(String.valueOf(processListAdapter.getCount()));
                    CoolerCPUScreen.this.tv_text.setText(CoolerCPUScreen.this.getResources().getString(R.string.mbc_cpuheating));
                }
            }
        }, 500L);
        init();
        redirectToNoti();
        setDimensions();

        cputemp();
        AdRequest adRequest = new AdRequest.Builder().build();


        showInterstialAds(CoolerCPUScreen.this);

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
            String string = this.sharedPrefUtil.getString(SharedPrefUtil.LASTCOOLTIME);
            if (string != null) {
                if (checkTimeDifference("" + System.currentTimeMillis(), string) <= GlobalData.coolPause) {
                    z = false;
                }
            }
            if (tv_temp == null) {
                tv_temp.setText("0");
            }
            if (!z) {
                TextView textView = this.tv_temp;
                textView.setText("" + this.sharedPrefUtil.getString(SharedPrefUtil.AFTERCOOLTEMP));
            } else {
                TextView textView2 = this.tv_temp;
                textView2.setText("" + (Integer.parseInt(this.text.toString().trim()) / 1000));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.btn_cooldowm.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                if (CoolerCPUScreen.this.multipleClicked()) {
                    return;
                }
                new AsyncTask<String, String, String>() {
                    @Override
                    public void onPreExecute() {
                        CoolerCPUScreen coolerCPUScreen = CoolerCPUScreen.this;
                        coolerCPUScreen.dialog = ProgressDialog.show(coolerCPUScreen.context, "", CoolerCPUScreen.this.getResources().getString(R.string.mbc_coolingdown), true, true);
                        super.onPreExecute();
                    }

                    @Override
                    public String doInBackground(String... strArr) {
                        try {
                            if (CoolerCPUScreen.this.processList.size() == 1) {
                                CoolerCPUScreen.this.kill_servieses();
                                return null;
                            }
                            for (int i2 = 0; i2 < CoolerCPUScreen.this.processList.size(); i2++) {
                                ((ActivityManager) CoolerCPUScreen.this.context.getSystemService(Context.ACTIVITY_SERVICE)).killBackgroundProcesses(((ProcessWrapper) CoolerCPUScreen.this.processList.get(i2)).name);
                            }
                            return null;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public void onPostExecute(String str) {
                        CoolerCPUScreen.this.dialog.dismiss();
                        SharedPrefUtil sharedPrefUtil = CoolerCPUScreen.this.sharedPrefUtil;
                        String str2 = SharedPrefUtil.LASTBOOSTTIME;
                        sharedPrefUtil.saveString(str2, "" + System.currentTimeMillis());
                        SharedPrefUtil sharedPrefUtil2 = CoolerCPUScreen.this.sharedPrefUtil;
                        sharedPrefUtil2.saveString(SharedPrefUtil.AFTERCOOLTEMP, "" + CoolerCPUScreen.this.maxcooled);
                        SharedPrefUtil sharedPrefUtil3 = CoolerCPUScreen.this.sharedPrefUtil;
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(System.currentTimeMillis());
                        sharedPrefUtil3.saveString(SharedPrefUtil.LASTCOOLTIME, sb.toString());

                            CoolerCPUScreen.this.finish();
                            Intent intent = new Intent(CoolerCPUScreen.this.context, JunkDeleteAnimationScreen.class);
                            intent.putExtra("DATA", "" + CoolerCPUScreen.this.maxcooled);
                            intent.putExtra("TYPE", "COOLER");
                            CoolerCPUScreen.this.startActivity(intent);



                        super.onPostExecute(str);
                    }
                }.execute(new String[0]);
            }
        });
        new Timer().schedule(new TimerTask() { // from class: com.mobiclean.phoneclean.coolers.CoolerCPUScreen.3
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                CoolerCPUScreen.this.readUsage();
                CoolerCPUScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String replace = new DecimalFormat(".0#").format(CoolerCPUScreen.this.mmmmmm).replace(",", "").replace(".", "");
                            if (replace.startsWith("-")) {
                                CoolerCPUScreen.this.t_cpuuses.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                            } else {
                                TextView textView3 = CoolerCPUScreen.this.t_cpuuses;
                                textView3.setText("" + replace);
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
        }, 3000L, 5000L);
        try {
            if (Integer.parseInt(this.tv_temp.getText().toString()) > 40) {
                this.tv_tempstatus.setText(R.string.mbc_high);
            } else {
                this.tv_tempstatus.setText(R.string.mbc_normal);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        ifnoProcessFound();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    GlobalData.cpu_temperature = CoolerCPUScreen.this.tv_temp.getText().toString();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }, 1000L);
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
        super.onResume();
    }

    public void loadinit() {
        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(CoolerCPUScreen.this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        return;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }
}