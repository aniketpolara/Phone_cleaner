package com.cleanPhone.mobileCleaner.coolers;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.cleanPhone.mobileCleaner.CommonResultActivity;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.JunkCleanScreen;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.customview.RevealView;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.processes.AndroidAppProcess;
import com.cleanPhone.mobileCleaner.processes.AppNames;
import com.cleanPhone.mobileCleaner.utility.AndroidProcesses;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppDetails;
import com.cleanPhone.mobileCleaner.wrappers.PackageInfoStruct;
import com.cleanPhone.mobileCleaner.wrappers.ProcessWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CoolerCPUAnimationActivity extends ParentActivity implements View.OnClickListener {
    private TextView ads_message;
    private TextView ads_title;
    private int deviceHeight;
    private int deviceWidth;
    private Handler handler;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    private RevealView iv_rocket;
    public Context j;
    public TextView k;
    public TextView m;
    private int max;
    private int maxcooled;
    public RelativeLayout n;
    private boolean noti_result_back;
    public RelativeLayout o;
    public FrameLayout p;
    private int percentage;
    public TextView q;
    private RelativeLayout relativeLayout;
    public TextView s;
    public ImageView back;
    private int progress = 1;
    public boolean l = false;
    private String progressStatus = "";
    private boolean backProceed = false;
    private ArrayList<String> values = new ArrayList<>();
    int admob = 1;

    public static int A(CoolerCPUAnimationActivity coolerCPUAnimationActivity) {
        int i = coolerCPUAnimationActivity.percentage;
        coolerCPUAnimationActivity.percentage = i - 1;
        return i;
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
        String str = "\nstart -> ";
        for (int i = 0; i < 30; i++) {
            StringBuilder sb = new StringBuilder();
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
                    sb.append(readLine);
                }
                if (Integer.parseInt(sb.toString()) > 1000) {
                    this.values.add("" + (Integer.parseInt(sb.toString()) / 1000));
                } else {
                    this.values.add("" + sb.toString());
                }
                str = str + ", " + sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        check_max();
    }

    private void getid() {

        this.iv_rocket = (RevealView) findViewById(R.id.iv_rocket);
        this.back = (ImageView) findViewById(R.id.iv_back);
        this.n = (RelativeLayout) findViewById(R.id.layout_one);
        this.o = (RelativeLayout) findViewById(R.id.layout_two);
        this.p = (FrameLayout) findViewById(R.id.frame_mid_laysss);
        this.q = (TextView) findViewById(R.id.ads_btn_countinue);
        this.s = (TextView) findViewById(R.id.ads_btn_cancel);
        this.ads_title = (TextView) findViewById(R.id.dialog_title);
        this.ads_message = (TextView) findViewById(R.id.dialog_msg);
        this.s.setOnClickListener(this);
        this.q.setOnClickListener(this);
        this.k = (TextView) findViewById(R.id.text_scanning);
        this.relativeLayout = (RelativeLayout) findViewById(R.id.top_layo);

    }


    public void gobacktoHomePage() {
        if (this.noti_result_back) {
            startActivity(new Intent(this.j, HomeActivity.class));
        } else {
            finish();
        }
    }

    public static int r(CoolerCPUAnimationActivity coolerCPUAnimationActivity) {
        int i = coolerCPUAnimationActivity.progress;
        coolerCPUAnimationActivity.progress = i + 1;
        return i;
    }

    private void redirectToNoti() {
        this.noti_result_back = getIntent().getBooleanExtra(GlobalData.NOTI_RESULT_BACK, false);
    }

    private void setdimensiton() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.deviceHeight = displayMetrics.heightPixels;
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.iv_rocket.getLayoutParams();
        int i2 = this.deviceHeight;
        layoutParams2.height = (i2 * 25) / 100;
        layoutParams2.width = (i2 * 25) / 100;
        this.iv_rocket.setLayoutParams(layoutParams2);
    }

    @SuppressLint("StaticFieldLeak")
    private void setprocesslist() {
        new AsyncTask<String, String, String>() {
            public String getName(Context context, AndroidAppProcess androidAppProcess) {
                try {
                    return AppNames.getLabel(context.getPackageManager(), androidAppProcess.getPackageInfo(context, 0));
                } catch (PackageManager.NameNotFoundException unused) {
                    return androidAppProcess.name;
                }
            }

            @Override // android.os.AsyncTask
            public void onPostExecute(String str) {
            }

            @Override
            public void onPreExecute() {
                GlobalData.processDataList.clear();
                super.onPreExecute();
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            public String doInBackground(String... strArr) {
                PackageManager packageManager;
                ArrayList<PackageInfoStruct> arrayList;
                List<AndroidAppProcess> list;
                boolean z;
                Debug.MemoryInfo[] memoryInfoArr;
                int i;
                boolean z2;
                int i2;
                ActivityManager activityManager = (ActivityManager) CoolerCPUAnimationActivity.this.j.getSystemService(Context.ACTIVITY_SERVICE);
                PackageManager packageManager2 = CoolerCPUAnimationActivity.this.getPackageManager();
                try {
                    List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
                    ArrayList<PackageInfoStruct> installedSystemApps = new AppDetails(CoolerCPUAnimationActivity.this.j).getInstalledSystemApps();
                    int i3 = 128;
                    char c2 = 0;
                    int i4 = 1;
                    if (runningAppProcesses.size() > 1) {
                        Iterator<ActivityManager.RunningAppProcessInfo> it = runningAppProcesses.iterator();
                        while (it.hasNext()) {
                            ActivityManager.RunningAppProcessInfo next = it.next();
                            if (!CoolerCPUAnimationActivity.this.getIgnoredData().contains(next.processName)) {
                                int[] iArr = new int[i4];
                                iArr[c2] = next.pid;
                                Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(iArr);
                                int length = processMemoryInfo.length;
                                int i5 = 0;
                                while (i5 < length) {
                                    Debug.MemoryInfo memoryInfo = processMemoryInfo[i5];
                                    ProcessWrapper processWrapper = new ProcessWrapper();
                                    Iterator<ActivityManager.RunningAppProcessInfo> it2 = it;
                                    try {
                                        CharSequence applicationLabel = packageManager2.getApplicationLabel(packageManager2.getApplicationInfo(next.processName, i3));
                                        String str = "" + ((Object) applicationLabel);
                                        processWrapper.appname = str;
                                        if (str.equalsIgnoreCase("Google play services") || processWrapper.appname.equalsIgnoreCase("Google play store")) {
                                            memoryInfoArr = processMemoryInfo;
                                            i = length;
                                        } else {
                                            int i6 = 0;
                                            while (true) {
                                                if (i6 >= installedSystemApps.size()) {
                                                    memoryInfoArr = processMemoryInfo;
                                                    i = length;
                                                    z2 = false;
                                                    break;
                                                }
                                                String str2 = installedSystemApps.get(i6).appname;
                                                memoryInfoArr = processMemoryInfo;
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("");
                                                i = length;
                                                sb.append(processWrapper.appname);
                                                if (str2.equalsIgnoreCase(sb.toString())) {
                                                    if (Util.notInIgnoreList("" + processWrapper.appname)) {
                                                        z2 = true;
                                                        break;
                                                    }
                                                }
                                                i6++;
                                                processMemoryInfo = memoryInfoArr;
                                                length = i;
                                            }
                                            if (!z2) {
                                                String str3 = next.processName;
                                                processWrapper.name = str3;
                                                if (!str3.equalsIgnoreCase("com.mobiclean.phoneclean") && ((i2 = next.importance) == 400 || i2 == 200 || i2 == 300)) {
                                                    processWrapper.size = memoryInfo.getTotalPss() * 1024;
                                                    processWrapper.pid = next.pid;
                                                    publishProgress("" + processWrapper.size, "Process : " + processWrapper.name);
                                                    GlobalData.processDataList.add(processWrapper);
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        memoryInfoArr = processMemoryInfo;
                                        i = length;
                                        e.printStackTrace();
                                        processWrapper.appname = next.processName;
                                    }
                                    i5++;
                                    it = it2;
                                    processMemoryInfo = memoryInfoArr;
                                    length = i;
                                    i3 = 128;
                                    c2 = 0;
                                }
                                i4 = 1;
                            }
                        }
                        return null;
                    }
                    List<AndroidAppProcess> runningAppProcesses2 = AndroidProcesses.getRunningAppProcesses();
                    Log.d("CCHECK", runningAppProcesses2.size() + "");
                    ArrayList ignoredData = CoolerCPUAnimationActivity.this.getIgnoredData();
                    int i7 = 0;
                    while (i7 < runningAppProcesses2.size()) {
                        AndroidAppProcess androidAppProcess = runningAppProcesses2.get(i7);
                        if (!ignoredData.contains(androidAppProcess.getPackageName())) {
                            int i8 = 0;
                            Debug.MemoryInfo[] processMemoryInfo2 = activityManager.getProcessMemoryInfo(new int[]{androidAppProcess.pid});
                            int length2 = processMemoryInfo2.length;
                            while (i8 < length2) {
                                Debug.MemoryInfo memoryInfo2 = processMemoryInfo2[i8];
                                ProcessWrapper processWrapper2 = new ProcessWrapper();
                                ActivityManager activityManager2 = activityManager;
                                try {
                                    list = runningAppProcesses2;
                                } catch (Exception e2) {
                                    packageManager = packageManager2;
                                    arrayList = installedSystemApps;
                                    list = runningAppProcesses2;
                                }
                                try {
                                    CharSequence applicationLabel2 = packageManager2.getApplicationLabel(packageManager2.getApplicationInfo(androidAppProcess.name, 128));
                                    String str4 = "" + ((Object) applicationLabel2);
                                    processWrapper2.appname = str4;
                                    if (str4.equalsIgnoreCase("Google play services") || processWrapper2.appname.equalsIgnoreCase("Google play store")) {
                                        packageManager = packageManager2;
                                        arrayList = installedSystemApps;
                                    } else {
                                        processWrapper2.name = androidAppProcess.name;
                                        int i9 = 0;
                                        while (true) {
                                            if (i9 >= installedSystemApps.size()) {
                                                packageManager = packageManager2;
                                                arrayList = installedSystemApps;
                                                z = false;
                                                break;
                                            }
                                            String str5 = installedSystemApps.get(i9).appname;
                                            packageManager = packageManager2;
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("");
                                            arrayList = installedSystemApps;
                                            sb2.append(processWrapper2.appname);
                                            if (str5.equalsIgnoreCase(sb2.toString())) {
                                                if (Util.notInIgnoreList("" + processWrapper2.appname)) {
                                                    z = true;
                                                    break;
                                                }
                                            }
                                            i9++;
                                            packageManager2 = packageManager;
                                            installedSystemApps = arrayList;
                                        }
                                        if (!z && !processWrapper2.name.equalsIgnoreCase("com.mobiclean.phoneclean")) {
                                            long totalPss = memoryInfo2.getTotalPss() * 1024;
                                            processWrapper2.size = totalPss;
                                            if (totalPss != 0) {
                                                Log.d("SIZEEEE", "" + Util.convertBytes(processWrapper2.size));
                                                processWrapper2.pid = androidAppProcess.pid;
                                                GlobalData.processDataList.add(processWrapper2);
                                            }
                                        }
                                    }
                                } catch (Exception e3) {
                                    packageManager = packageManager2;
                                    arrayList = installedSystemApps;
                                    processWrapper2.appname = androidAppProcess.name;
                                    i8++;
                                    activityManager = activityManager2;
                                    runningAppProcesses2 = list;
                                    packageManager2 = packageManager;
                                    installedSystemApps = arrayList;
                                }
                                i8++;
                                activityManager = activityManager2;
                                runningAppProcesses2 = list;
                                packageManager2 = packageManager;
                                installedSystemApps = arrayList;
                            }
                        }
                        i7++;
                        activityManager = activityManager;
                        runningAppProcesses2 = runningAppProcesses2;
                        packageManager2 = packageManager2;
                        installedSystemApps = installedSystemApps;
                    }
                    return null;
                } catch (Exception e4) {
                    e4.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onProgressUpdate(String... strArr) {
                super.onProgressUpdate(strArr);
            }
        }.execute(new String[0]);
    }


    public boolean showSavedTemp(String str, String str2) {
        if (str == null && str2 == null) {
            return false;
        }
        if (checkTimeDifference("" + System.currentTimeMillis(), str) > GlobalData.coolPause) {
            if (checkTimeDifference("" + System.currentTimeMillis(), str2) > GlobalData.boostPause) {
                return false;
            }
        }
        return true;
    }

    public void check_max() {
        this.max = 0;
        this.maxcooled = 0;
        boolean z = false;
        int i = 100;
        for (int i2 = 0; i2 < this.values.size(); i2++) {
            try {
                if (Integer.parseInt(this.values.get(i2).trim()) > 45) {
                    z = true;
                }
                if (Integer.parseInt(this.values.get(i2).trim()) > this.max && Integer.parseInt(this.values.get(i2).trim()) <= 45) {
                    this.max = Integer.parseInt(this.values.get(i2));
                }
                if (Integer.parseInt(this.values.get(i2).trim()) < i && Integer.parseInt(this.values.get(i2).trim()) > 0) {
                    i = Integer.parseInt(this.values.get(i2));
                }
                if (Integer.parseInt(this.values.get(i2).trim()) <= 40 && Integer.parseInt(this.values.get(i2).trim()) >= 35 && Integer.parseInt(this.values.get(i2).trim()) > this.maxcooled) {
                    this.maxcooled = Integer.parseInt(this.values.get(i2).trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.maxcooled == 0) {
            this.maxcooled = 40;
        }
        if (this.max == 0) {
            this.max = 45;
        }
        if (!z || this.max > 40) {
            return;
        }
        this.max = 45;
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


        ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_cpu_colored));
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_cpu_cooler));
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(String.format(getResources().getString(R.string.mbc_simple_back_press), getResources().getString(R.string.mbc_cpu_scan_txt)));

        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CoolerCPUAnimationActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
                CoolerCPUAnimationActivity.this.l = false;
            }
        });

        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CoolerCPUAnimationActivity.this.multipleClicked()) {
                    return;
                }
                CoolerCPUAnimationActivity.this.handler.removeMessages(0);
                dialog.dismiss();
                CoolerCPUAnimationActivity.this.gobacktoHomePage();
            }
        });
        dialog.show();
        this.l = true;
    }

    @Override
    public void onClick(View view) {
        if (multipleClicked()) {
            return;
        }
        if (view.getId() == R.id.ads_btn_cancel) {
            this.o.setVisibility(View.GONE);
            this.l = false;
            this.n.setVisibility(View.VISIBLE);
        }
        if (view.getId() == R.id.ads_btn_countinue) {
            RelativeLayout relativeLayout = this.o;
            if (relativeLayout != null && relativeLayout.getVisibility() == View.VISIBLE) {
                this.n.setVisibility(View.VISIBLE);
                this.o.setVisibility(View.GONE);
                this.l = false;
            }
            finish();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_cpu_cooler_animation);
        Util.isHome = false;
        GlobalData.processDataList.clear();
        this.j = this;
        getid();
        redirectToNoti();
        setdimensiton();



        if (Build.VERSION.SDK_INT <= 23) {
            setprocesslist();
            cputemp();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Handler handler = new Handler();
        this.handler = handler;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                CoolerCPUAnimationActivity coolerCPUAnimationActivity = CoolerCPUAnimationActivity.this;
                if (coolerCPUAnimationActivity.l) {
                    coolerCPUAnimationActivity.handler.postDelayed(this, 100L);
                    return;
                }

                CoolerCPUAnimationActivity.r(coolerCPUAnimationActivity);
                if (CoolerCPUAnimationActivity.this.progress > 100) {
                    try {
                        Log.e("-----progress == 100", "" + CoolerCPUAnimationActivity.this.progress);
                        SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(CoolerCPUAnimationActivity.this);
                        if (!CoolerCPUAnimationActivity.this.showSavedTemp(sharedPrefUtil.getString(SharedPrefUtil.LASTCOOLTIME), sharedPrefUtil.getString(SharedPrefUtil.LASTBOOSTTIME))) {
                            if (CoolerCPUAnimationActivity.this.max != 0 && CoolerCPUAnimationActivity.this.max <= 40) {

                                CoolerCPUAnimationActivity.this.progressStatus = "normal";
                                CoolerCPUAnimationActivity coolerCPUAnimationActivity2 = CoolerCPUAnimationActivity.this;
                                if (coolerCPUAnimationActivity2.l) {
                                    return;
                                }
                                coolerCPUAnimationActivity2.progress = 0;
                                if (CoolerCPUAnimationActivity.this.max == 0) {
                                    CoolerCPUAnimationActivity.this.max = 39;
                                }
                                GlobalData.temperaryTemp = CoolerCPUAnimationActivity.this.max + "";
                                Intent intent = new Intent(CoolerCPUAnimationActivity.this, CommonResultActivity.class);
                                intent.putExtra("DATA", CoolerCPUAnimationActivity.this.getResources().getString(R.string.mbc_normaltempdevices));
                                intent.putExtra("TYPE", "COOLER");
                                GlobalData.loadAds = false;
                                    CoolerCPUAnimationActivity.this.startActivity(intent);
                                    CoolerCPUAnimationActivity.this.finish();


                                return;
                            }

                            CoolerCPUAnimationActivity.this.progressStatus = "list";
                            CoolerCPUAnimationActivity coolerCPUAnimationActivity3 = CoolerCPUAnimationActivity.this;
                            if (coolerCPUAnimationActivity3.l) {
                                return;
                            }
                            coolerCPUAnimationActivity3.progress = 0;

                            if (CoolerCPUAnimationActivity.this.noti_result_back) {
                                Intent intent2 = new Intent(CoolerCPUAnimationActivity.this.getApplicationContext(), CoolerCPUScreen.class);
                                intent2.putExtra(GlobalData.NOTI_RESULT_BACK, true);

                                    CoolerCPUAnimationActivity.this.startActivity(intent2);
                                    CoolerCPUAnimationActivity.this.finish();





                                return;
                            }

                                CoolerCPUAnimationActivity.this.startActivity(new Intent(CoolerCPUAnimationActivity.this.getApplicationContext(), CoolerCPUScreen.class));
                                CoolerCPUAnimationActivity.this.finish();


                            return;
                        }

                        CoolerCPUAnimationActivity.this.progressStatus = "normal";
                        CoolerCPUAnimationActivity coolerCPUAnimationActivity4 = CoolerCPUAnimationActivity.this;
                        if (coolerCPUAnimationActivity4.l) {
                            return;
                        }
                        coolerCPUAnimationActivity4.progress = 0;
                        if (CoolerCPUAnimationActivity.this.maxcooled == 0) {
                            CoolerCPUAnimationActivity.this.maxcooled = 40;
                        }
                        if (sharedPrefUtil.getString(SharedPrefUtil.AFTERCOOLTEMP) == null) {
                            sharedPrefUtil.saveString(SharedPrefUtil.AFTERCOOLTEMP, "" + CoolerCPUAnimationActivity.this.maxcooled);
                        }
                        Intent intent3 = new Intent(CoolerCPUAnimationActivity.this, CommonResultActivity.class);
                        intent3.putExtra("DATA", CoolerCPUAnimationActivity.this.getResources().getString(R.string.mbc_normaltempdevices));
                        intent3.putExtra("TYPE", "COOLER");
                        GlobalData.loadAds = false;


                            CoolerCPUAnimationActivity.this.startActivity(intent3);
                            CoolerCPUAnimationActivity.this.finish();



                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(CoolerCPUAnimationActivity.this.progress + "% " + CoolerCPUAnimationActivity.this.getResources().getString(R.string.mbc_completed));
                spannableStringBuilder.setSpan(new StyleSpan(1), 0, CoolerCPUAnimationActivity.this.getResources().getString(R.string.mbc_completed).length(), 33);
                spannableStringBuilder.setSpan(new RelativeSizeSpan(1.0f), 0, CoolerCPUAnimationActivity.this.getResources().getString(R.string.mbc_completed).length(), 33);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(-1), 0, CoolerCPUAnimationActivity.this.getResources().getString(R.string.mbc_completed).length(), 33);
                if (CoolerCPUAnimationActivity.this.progress <= 100) {
                    TextView textView = CoolerCPUAnimationActivity.this.k;
                    textView.setText("" + ((Object) spannableStringBuilder));
                }
                CoolerCPUAnimationActivity.this.handler.postDelayed(this, 100L);
            }
        }, 110L);

        new SharedPrefUtil(this).saveLastTimeUsed(SharedPrefUtil.LUSED_COOLER, System.currentTimeMillis());
        RevealView revealView = this.iv_rocket;
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.cpu_cooler_h);
        int i = this.deviceHeight;
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeResource, (i * 25) / 100, (i * 25) / 100, false);
        int i2 = this.deviceHeight;
        revealView.setSecondBitmap(createScaledBitmap, (i2 * 25) / 100, (i2 * 25) / 100);
        this.percentage = 100;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                CoolerCPUAnimationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (CoolerCPUAnimationActivity.this.percentage >= 0) {
                            CoolerCPUAnimationActivity.this.iv_rocket.setPercentage(CoolerCPUAnimationActivity.this.percentage);
                            CoolerCPUAnimationActivity.A(CoolerCPUAnimationActivity.this);
                            return;
                        }
                        cancel();
                    }
                });
            }
        }, 0L, 100L);
        clearNotification(new int[]{600});
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
    public void onPause() {
        super.onPause();
        this.l = true;
        this.backProceed = true;
    }

    @Override
    public void onResume() {
        this.l = false;
        this.backProceed = false;

        if (this.progress >= 101) {

            if (this.progressStatus.equalsIgnoreCase("list")) {
                this.progress = 0;
                if (this.noti_result_back) {
                    Intent intent = new Intent(getApplicationContext(), CoolerCPUScreen.class);
                    intent.putExtra(GlobalData.NOTI_RESULT_BACK, true);

                        startActivity(intent);
                        finish();



                } else {

                        startActivity(new Intent(getApplicationContext(), CoolerCPUScreen.class));
                        finish();




                }

            } else if (this.progressStatus.equalsIgnoreCase("normal")) {
                this.progress = 0;
                Intent intent2 = new Intent(this, CommonResultActivity.class);
                intent2.putExtra("DATA", getResources().getString(R.string.mbc_normaltempdevices));
                intent2.putExtra("TYPE", "COOLER");
                GlobalData.loadAds = false;

                    startActivity(intent2);
                    finish();



            } else {
                Log.e("-----progress == 100", "" + this.progress);
            }
        }
        super.onResume();
    }

    public void loadinit() {
        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
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