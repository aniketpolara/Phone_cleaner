package com.cleanPhone.mobileCleaner;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;


import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.customview.RevealView;
import com.cleanPhone.mobileCleaner.processes.AndroidAppProcess;
import com.cleanPhone.mobileCleaner.progress.ArcProgress;
import com.cleanPhone.mobileCleaner.utility.AndroidProcesses;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppDetails;
import com.cleanPhone.mobileCleaner.wrappers.PackageInfoStruct;
import com.cleanPhone.mobileCleaner.wrappers.ProcessWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AnimatedBoostActivity extends ParentActivity implements View.OnClickListener {
    private static final String TAG = "AnimatedBoostVertiseScreen";
    private TextView ads_message;
    private TextView ads_title;
    private TextView boosted_device_name;
    private Context context;
    private ArcProgress cvBoost;
    private ArcProgress cvBoostOuter;
    private String data;

    private int deviceHeight;

    private int deviceWidth;
    private Handler handlerProgress;
    private RevealView iv_rocket;
    public TextView l;
    ImageView back;
    private RelativeLayout linear_main_layout;
    public Handler n;

    int click = 0;
    int numOfClick = 3;

    private boolean noti_result_back;
    public Dialog o;
    public RelativeLayout p;
    private int percentage;
    private int progress;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    public RelativeLayout q;
    private boolean redirectToHome;
    private boolean redirectToNoti;
    private RelativeLayout rocket_lay;
    public FrameLayout s;
    public TextView t;
    private long totBoostSize;
    public TextView u;
    private long ramsaveSize = 0;
    private long ramFillSpace = 0;
    private long sizebfore = 0;
    private long saved = 0;
    public ArrayList<ProcessWrapper> j = new ArrayList<>();
    public boolean k = false;
    private boolean isInbackground = false;
    private String statusProgress = "";
    private volatile boolean processesReceived = false;
    public volatile boolean m = false;
    private boolean switched = false;
    private boolean proceeded = false;
    public int v = 0;
    public int w = 0;


    public void backToHome() {
        finish();
        if (this.redirectToHome || this.redirectToNoti || this.noti_result_back) {
            startActivity(new Intent(this.context, HomeActivity.class));
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void getProcessesList() {

        new AsyncTask<String, String, String>() {
            @Override
            public void onPreExecute() {
                AnimatedBoostActivity.this.totBoostSize = 0L;
                AnimatedBoostActivity animatedBoostActivity = AnimatedBoostActivity.this;
                animatedBoostActivity.v = 0;
                animatedBoostActivity.w = 0;
                animatedBoostActivity.j.clear();
                super.onPreExecute();
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            public String doInBackground(String... strArr) {
                int length;
                int i;
                Debug.MemoryInfo memoryInfo;
                ProcessWrapper processWrapper;
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = null;

                int i2;
                int i3;
                ActivityManager activityManager = (ActivityManager) AnimatedBoostActivity.this.context.getSystemService(ACTIVITY_SERVICE);
                PackageManager packageManager = AnimatedBoostActivity.this.getPackageManager();
                try {
                    List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
                    ActivityManager.RunningAppProcessInfo runningAppProcessInfo2 = new ActivityManager.RunningAppProcessInfo();
                    AnimatedBoostActivity.this.v = runningAppProcesses.size();
                    AnimatedBoostActivity animatedBoostActivity = AnimatedBoostActivity.this;
                    int i4 = 1;
                    if (animatedBoostActivity.v > 1) {
                        ArrayList ignoredData = animatedBoostActivity.getIgnoredData();
                        for (runningAppProcessInfo2 = runningAppProcessInfo; ; ) {
                            if (!ignoredData.contains(runningAppProcessInfo2.processName)) {
                                int[] iArr = new int[i4];
                                iArr[0] = runningAppProcessInfo2.pid;
                                Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(iArr);
                                length = processMemoryInfo.length;
                                i = 0;
                                while (i < length) {
                                    memoryInfo = processMemoryInfo[i];
                                    processWrapper = new ProcessWrapper();
                                    AnimatedBoostActivity.this.w += i4;
                                    try {
                                        CharSequence applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(runningAppProcessInfo2.processName, 128));
                                        String str = "" + ((Object) applicationLabel);
                                        processWrapper.appname = str;
                                        if (!str.equalsIgnoreCase("Google play services")) {
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        processWrapper.appname = runningAppProcessInfo2.processName;
                                    }
                                    runningAppProcessInfo = runningAppProcessInfo2;
                                    i2 = length;
                                    i++;
                                    length = i2;
                                    runningAppProcessInfo2 = runningAppProcessInfo;
                                    i4 = 1;
                                }
                            }
                        }
                    }
                    return null;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(String str) {
                new AsyncTask<String, String, String>() {
                    @Override
                    public void onPostExecute(String str2) {
                    }

                    @Override
                    public void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    public String doInBackground(String... strArr) {
                        SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(AnimatedBoostActivity.this);
                        String str2 = SharedPrefUtil.LASTBOOSTTIME;
                        sharedPrefUtil.saveString(str2, "" + System.currentTimeMillis());
                        AnimatedBoostActivity.this.getRamSize();
                        AnimatedBoostActivity animatedBoostActivity = AnimatedBoostActivity.this;
                        animatedBoostActivity.sizebfore = animatedBoostActivity.ramFillSpace;
                        AnimatedBoostActivity.this.kill_services();
                        sharedPrefUtil.saveString(SharedPrefUtil.RAMPAUSE, "" + System.currentTimeMillis());
                        AnimatedBoostActivity.this.getRamSize();
                        sharedPrefUtil.saveString(SharedPrefUtil.RAMATPAUSE, "" + Math.round((float) AnimatedBoostActivity.this.ramsaveSize));
                        long j = AnimatedBoostActivity.this.ramFillSpace;
                        AnimatedBoostActivity animatedBoostActivity2 = AnimatedBoostActivity.this;
                        animatedBoostActivity2.saved = animatedBoostActivity2.sizebfore - j;
                        return null;
                    }
                }.execute(new String[0]);
            }

            @Override
            public void onProgressUpdate(String... strArr) {
                super.onProgressUpdate(strArr);
            }
        }.execute(new String[0]);
    }


    public void getRamSize() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        if (activityManager != null) {
            activityManager.getMemoryInfo(memoryInfo);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            long j = memoryInfo.totalMem;
            long j2 = j / 1048576;
            long j3 = memoryInfo.availMem;
            long j4 = j3 / 1048576;
            this.ramFillSpace = j - j3;
            this.ramsaveSize = ((j - j3) * 100) / j;
        }
    }

    private void iffromShortcut() {
        getIntent().getBooleanExtra("FRMSHORT", false);
    }

    private void init() {
        Util.appendLogmobiclean(TAG, " init ", "");
        this.context = this;

        String capitalizeFirstLetter = Util.capitalizeFirstLetter(Build.BRAND);
        ((TextView) findViewById(R.id.booststatus)).setText(String.format(getResources().getString(R.string.mbc_analyzing).replace("DO_NOT_TRANSLATE", "%s"), capitalizeFirstLetter.substring(0, 1).toUpperCase() + capitalizeFirstLetter.substring(1)));
        this.p = (RelativeLayout) findViewById(R.id.layout_one);
        this.q = (RelativeLayout) findViewById(R.id.layout_two);
        this.s = (FrameLayout) findViewById(R.id.frame_mid_laysss);
        this.t = (TextView) findViewById(R.id.ads_btn_countinue);
        this.u = (TextView) findViewById(R.id.ads_btn_cancel);
        this.ads_title = (TextView) findViewById(R.id.dialog_title);
        this.ads_message = (TextView) findViewById(R.id.dialog_msg);
        back = findViewById(R.id.iv_back);
        this.u.setOnClickListener(this);
        this.t.setOnClickListener(this);
        this.handlerProgress = new Handler();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar = progressBar;
        progressBar.getProgressDrawable().setColorFilter(-1, PorterDuff.Mode.SRC_IN);
        this.iv_rocket = (RevealView) findViewById(R.id.iv_rocket);
        this.rocket_lay = (RelativeLayout) findViewById(R.id.rocket_lay);
    }


    public void kill_services() {
        List<ApplicationInfo> installedApplications = getPackageManager().getInstalledApplications(0);
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ArrayList ignoredData = getIgnoredData();
        for (ApplicationInfo applicationInfo : installedApplications) {
            if ((applicationInfo.flags & 1) != 1 && !applicationInfo.packageName.equals("com.mobiclean.phoneclean")) {
                if (!ignoredData.contains("" + applicationInfo.packageName) && activityManager != null) {
                    activityManager.killBackgroundProcesses(applicationInfo.packageName);
                }
            }
        }
    }

    public static int p(AnimatedBoostActivity animatedBoostActivity) {
        int i = animatedBoostActivity.percentage;
        animatedBoostActivity.percentage = i - 1;
        return i;
    }

    private void performScanningAsPerCondition() {
        Util.appendLogmobiclean(TAG, " method:performScanningAsPerCondition ", "");
        String string = new SharedPrefUtil(this).getString(SharedPrefUtil.LASTBOOSTTIME);
        if (string != null) {
            if (Util.checkTimeDifference("" + System.currentTimeMillis(), string) > GlobalData.boostPause) {
                if (Build.VERSION.SDK_INT <= 23) {
                    setprocesslist();
                    return;
                } else {
                    getProcessesList();
                    return;
                }
            }
            this.k = true;
            this.processesReceived = true;
        } else if (Build.VERSION.SDK_INT <= 23) {
            setprocesslist();
        } else {
            getProcessesList();
        }
    }

    private void redirectToHome() {
        this.redirectToHome = getIntent().getBooleanExtra(GlobalData.REDIRECTHOME, false);
        this.redirectToNoti = getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
        this.noti_result_back = getIntent().getBooleanExtra(GlobalData.NOTI_RESULT_BACK, false);
    }

    private void rocketAnimation() {
        RevealView revealView = this.iv_rocket;
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.boost_animation_h);
        int i = this.deviceHeight;
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeResource, (i * 30) / 100, (i * 30) / 100, false);
        int i2 = this.deviceHeight;
        revealView.setSecondBitmap(createScaledBitmap, (i2 * 30) / 100, (i2 * 30) / 100);
        this.percentage = 100;
        new Timer().schedule(new TimerTask() { // from class: com.mobiclean.phoneclean.AnimatedBoostActivity.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                AnimatedBoostActivity.this.runOnUiThread(new Runnable() { // from class: com.mobiclean.phoneclean.AnimatedBoostActivity.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (AnimatedBoostActivity.this.percentage >= 0) {
                            AnimatedBoostActivity.this.iv_rocket.setPercentage(AnimatedBoostActivity.this.percentage);
                            AnimatedBoostActivity.p(AnimatedBoostActivity.this);
                            return;
                        }
                        cancel();
                    }
                });
            }
        }, 0L, 55L);
    }

    private void setDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.rocket_lay.getLayoutParams();
        layoutParams.setMargins(0, (this.deviceHeight * 20) / 100, 0, 0);
        this.rocket_lay.setLayoutParams(layoutParams);
        int i = this.deviceHeight;
        layoutParams.height = (i * 30) / 100;
        layoutParams.width = (i * 30) / 100;
        this.iv_rocket.setLayoutParams((RelativeLayout.LayoutParams) this.iv_rocket.getLayoutParams());
        Util.appendLogmobiclean(TAG, " method:setDimensions ", "");
        try {
            int i2 = displaymetrics.heightPixels;
            if ((i2 <= 780 || i2 >= 1000) && ((i2 > 1000 && i2 <= 1442) || i2 <= 1443)) {
            }
            Log.e("-----------", "" + displaymetrics.heightPixels + IOUtils.LINE_SEPARATOR_UNIX + displaymetrics.widthPixels);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void setprocesslist() {
        Util.appendLogmobiclean(TAG, "setprocesslist", "");
        new AsyncTask<String, String, String>() {
            @Override
            public void onPreExecute() {
                Util.appendLogmobiclean(AnimatedBoostActivity.TAG, "onPreExecute", "");
                GlobalData.processDataList.clear();
                super.onPreExecute();
            }

            @Override
            public String doInBackground(String... strArr) {
                List<AndroidAppProcess> list;
                ArrayList arrayList;
                ArrayList<PackageInfoStruct> arrayList2;
                PackageManager packageManager;
                boolean z;
                ArrayList<PackageInfoStruct> arrayList3;
                int i;
                Debug.MemoryInfo[] memoryInfoArr = new Debug.MemoryInfo[0];
                boolean z2;
                int i2;
                Util.appendLogmobiclean(AnimatedBoostActivity.TAG, "doInBackground", "");
                ActivityManager activityManager = (ActivityManager) AnimatedBoostActivity.this.context.getSystemService(ACTIVITY_SERVICE);
                PackageManager packageManager2 = AnimatedBoostActivity.this.getPackageManager();
                try {
                    List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
                    ArrayList<PackageInfoStruct> installedSystemApps = new AppDetails(AnimatedBoostActivity.this.context).getInstalledSystemApps();
                    char c2 = 0;
                    int i3 = 1;
                    if (runningAppProcesses.size() > 1) {
                        ArrayList ignoredData = AnimatedBoostActivity.this.getIgnoredData();
                        Util.appendLogmobiclean(AnimatedBoostActivity.TAG, " doInBackground runningProcesses > 1 " + runningAppProcesses.size(), "");
                        Iterator<ActivityManager.RunningAppProcessInfo> it = runningAppProcesses.iterator();
                        while (it.hasNext()) {
                            ActivityManager.RunningAppProcessInfo next = it.next();
                            if (!ignoredData.contains(next.processName)) {
                                int[] iArr = new int[i3];
                                iArr[c2] = next.pid;
                                Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(iArr);
                                int length = processMemoryInfo.length;
                                int i4 = 0;
                                while (i4 < length) {
                                    Debug.MemoryInfo memoryInfo = processMemoryInfo[i4];
                                    Iterator<ActivityManager.RunningAppProcessInfo> it2 = it;
                                    ProcessWrapper processWrapper = new ProcessWrapper();
                                    ArrayList arrayList4 = ignoredData;
                                    try {
                                        i = length;
                                        try {
                                            CharSequence applicationLabel = packageManager2.getApplicationLabel(packageManager2.getApplicationInfo(next.processName, PackageManager.GET_META_DATA));
                                            String str = "" + ((Object) applicationLabel);
                                            processWrapper.appname = str;
                                            if (str.equalsIgnoreCase("Google play services") || processWrapper.appname.equalsIgnoreCase("Google play store")) {
                                                arrayList3 = installedSystemApps;
                                                memoryInfoArr = processMemoryInfo;
                                            } else {
                                                int i5 = 0;
                                                while (true) {
                                                    if (i5 >= installedSystemApps.size()) {
                                                        arrayList3 = installedSystemApps;
                                                        memoryInfoArr = processMemoryInfo;
                                                        z2 = false;
                                                        break;
                                                    }
                                                    String str2 = installedSystemApps.get(i5).appname;
                                                    memoryInfoArr = processMemoryInfo;
                                                    StringBuilder sb = new StringBuilder();
                                                    sb.append("");
                                                    arrayList3 = installedSystemApps;
                                                    sb.append(processWrapper.appname);
                                                    if (str2.equalsIgnoreCase(sb.toString())) {
                                                        if (Util.notInIgnoreList("" + processWrapper.appname)) {
                                                            z2 = true;
                                                            break;
                                                        }
                                                    }
                                                    i5++;
                                                    processMemoryInfo = memoryInfoArr;
                                                    installedSystemApps = arrayList3;
                                                }
                                                if (!z2) {
                                                    String str3 = next.processName;
                                                    processWrapper.name = str3;
                                                    if (!str3.equalsIgnoreCase("com.mobiclean.phoneclean") && ((i2 = next.importance) == 400 || i2 == 200 || i2 == 300)) {
                                                        processWrapper.size = memoryInfo.getTotalPss() * 1024;
                                                        processWrapper.pid = next.pid;
                                                        publishProgress("" + processWrapper.size, "Process : " + processWrapper.name);
                                                        processWrapper.ischecked = true;
                                                        GlobalData.processDataList.add(processWrapper);
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            e = e;
                                            arrayList3 = installedSystemApps;
                                            memoryInfoArr = processMemoryInfo;
                                            e.printStackTrace();
                                            processWrapper.appname = next.processName;
                                            i4++;
                                            it = it2;
                                            ignoredData = arrayList4;
                                            length = i;
                                            processMemoryInfo = memoryInfoArr;
                                            installedSystemApps = arrayList3;
                                        }
                                    } catch (Exception e2) {
//                                        e = e2;
                                        arrayList3 = installedSystemApps;
                                        i = length;
                                    }
                                    i4++;
                                    it = it2;
                                    ignoredData = arrayList4;
                                    length = i;
                                    processMemoryInfo = memoryInfoArr;
                                    installedSystemApps = arrayList3;
                                }
                                c2 = 0;
                                i3 = 1;
                            }
                        }
                    } else {
                        ArrayList<PackageInfoStruct> arrayList5 = installedSystemApps;
                        Util.appendLogmobiclean(AnimatedBoostActivity.TAG, " doInBackground runningProcesses == 1 ", "");
                        List<AndroidAppProcess> runningAppProcesses2 = AndroidProcesses.getRunningAppProcesses();
                        Log.d("BCHECK", runningAppProcesses2.size() + "");
                        ArrayList ignoredData2 = AnimatedBoostActivity.this.getIgnoredData();
                        int i6 = 0;
                        while (i6 < runningAppProcesses2.size()) {
                            AndroidAppProcess androidAppProcess = runningAppProcesses2.get(i6);
                            if (!ignoredData2.contains(androidAppProcess.getPackageName())) {
                                int i7 = 0;
                                Debug.MemoryInfo[] processMemoryInfo2 = activityManager.getProcessMemoryInfo(new int[]{androidAppProcess.pid});
                                int length2 = processMemoryInfo2.length;
                                while (i7 < length2) {
                                    Debug.MemoryInfo memoryInfo2 = processMemoryInfo2[i7];
                                    ProcessWrapper processWrapper2 = new ProcessWrapper();
                                    ActivityManager activityManager2 = activityManager;
                                    try {
                                        list = runningAppProcesses2;
                                    } catch (Exception unused) {
                                        list = runningAppProcesses2;
                                    }
                                    try {
                                        CharSequence applicationLabel2 = packageManager2.getApplicationLabel(packageManager2.getApplicationInfo(androidAppProcess.name, PackageManager.GET_META_DATA));
                                        String str4 = "" + ((Object) applicationLabel2);
                                        processWrapper2.appname = str4;
                                        if (str4.equalsIgnoreCase("Google play services") || processWrapper2.appname.equalsIgnoreCase("Google play store")) {
                                            arrayList = ignoredData2;
                                            arrayList2 = arrayList5;
                                            packageManager = packageManager2;
                                        } else {
                                            processWrapper2.name = androidAppProcess.name;
                                            int i8 = 0;
                                            while (true) {
                                                if (i8 >= arrayList5.size()) {
                                                    arrayList = ignoredData2;
                                                    arrayList2 = arrayList5;
                                                    packageManager = packageManager2;
                                                    z = false;
                                                    break;
                                                }
                                                ArrayList<PackageInfoStruct> arrayList6 = arrayList5;
                                                packageManager = packageManager2;
                                                String str5 = arrayList6.get(i8).appname;
                                                arrayList2 = arrayList6;
                                                StringBuilder sb2 = new StringBuilder();
                                                sb2.append("");
                                                arrayList = ignoredData2;
                                                sb2.append(processWrapper2.appname);
                                                if (str5.equalsIgnoreCase(sb2.toString())) {
                                                    if (Util.notInIgnoreList("" + processWrapper2.appname)) {
                                                        z = true;
                                                        break;
                                                    }
                                                }
                                                i8++;
                                                packageManager2 = packageManager;
                                                ignoredData2 = arrayList;
                                                arrayList5 = arrayList2;
                                            }
                                            if (!z && !processWrapper2.name.equalsIgnoreCase("com.mobiclean.phoneclean")) {
                                                long totalPss = memoryInfo2.getTotalPss() * 1024;
                                                processWrapper2.size = totalPss;
                                                if (totalPss != 0) {
                                                    Log.d("SIZEEEE", "" + Util.convertBytes(processWrapper2.size));
                                                    processWrapper2.pid = androidAppProcess.pid;
                                                    processWrapper2.ischecked = true;
                                                    GlobalData.processDataList.add(processWrapper2);
                                                }
                                            }
                                        }
                                    } catch (Exception unused2) {
                                        arrayList = ignoredData2;
                                        arrayList2 = arrayList5;
                                        packageManager = packageManager2;
                                        processWrapper2.appname = androidAppProcess.name;
                                        i7++;
                                        activityManager = activityManager2;
                                        runningAppProcesses2 = list;
                                        packageManager2 = packageManager;
                                        ignoredData2 = arrayList;
                                        arrayList5 = arrayList2;
                                    }
                                    i7++;
                                    activityManager = activityManager2;
                                    runningAppProcesses2 = list;
                                    packageManager2 = packageManager;
                                    ignoredData2 = arrayList;
                                    arrayList5 = arrayList2;
                                }
                            }
                            i6++;
                            activityManager = activityManager;
                            runningAppProcesses2 = runningAppProcesses2;
                            packageManager2 = packageManager2;
                            ignoredData2 = ignoredData2;
                            arrayList5 = arrayList5;
                        }
                        Log.d("BCHECK", GlobalData.processDataList.size() + "");
                    }
                } catch (Exception e3) {
                    Util.appendLogmobiclean(AnimatedBoostActivity.TAG, "doInBackground exception !!!!!!!!!", "");
                    e3.printStackTrace();
                }
                Util.appendLogmobiclean(AnimatedBoostActivity.TAG, "doInBackground ends", "");
                return null;
            }

            @Override
            public void onPostExecute(String str) {
                Util.appendLogmobiclean(AnimatedBoostActivity.TAG, "onPostExecute", "");
                AnimatedBoostActivity.this.processesReceived = true;
            }

            @Override
            public void onProgressUpdate(String... strArr) {
                super.onProgressUpdate(strArr);
            }
        }.execute(new String[0]);
    }


    public void switchScreen() {
        if (this.proceeded) {
            return;
        }
        this.proceeded = true;
        if (this.k) {
            this.statusProgress = "alreadyBoost";
            if (this.isInbackground) {
                return;
            }
                Util.appendLogmobiclean(TAG, "alreadyBoost", "");
                Intent intent = new Intent(AnimatedBoostActivity.this, CommonResultActivity.class);
                intent.putExtra(GlobalData.REDIRECTNOTI, true);
                intent.putExtra("DATA", getResources().getString(R.string.mbc_phone_boosted));
                intent.putExtra("TYPE", "BOOST");
                finish();
                GlobalData.afterDelete = true;
                GlobalData.loadAds = false;
                startActivity(intent);
        }
        this.statusProgress = "normal";
        if (this.isInbackground) {
            return;
        }
        if (Build.VERSION.SDK_INT <= 23) {
            if (GlobalData.processDataList.size() == 0) {

                    Util.appendLogmobiclean(TAG, " statusProgress = normal", "");
                    Intent intent2 = new Intent(AnimatedBoostActivity.this, CommonResultActivity.class);
                    intent2.putExtra("DATA", getResources().getString(R.string.mbc_phone_boosted));
                    intent2.putExtra("TYPE", "BOOST");
                    intent2.putExtra(GlobalData.REDIRECTNOTI, true);
                    GlobalData.afterDelete = true;
                    GlobalData.loadAds = false;
                    Util.appendLogmobiclean(TAG, " CommonResultVertiseScreen called ", "");
                    startActivity(intent2);


            } else {
                    Util.appendLogmobiclean(TAG, " RamBoostAppsScreen called ", "");
                    Intent intent3 = new Intent(AnimatedBoostActivity.this, RamBoostAppsScreen.class);
                    intent3.putExtra(GlobalData.REDIRECTNOTI, true);
                    intent3.putExtra(GlobalData.NOTI_RESULT_BACK, true);
                    startActivity(intent3);



            }
            finish();
        }

    }

    private void trackIfFromNotification() {
        Util.appendLogmobiclean(TAG, " method:trackIfFromNotification", "");
        getIntent().getBooleanExtra("FROMNOTI", false);
    }


    public void update_progress() {
        this.n.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AnimatedBoostActivity.this.progress >= 100) {
                    if (AnimatedBoostActivity.this.progress == 100) {
                        if (!AnimatedBoostActivity.this.switched) {
                            AnimatedBoostActivity.this.switchScreen();
                            AnimatedBoostActivity.this.switched = true;
                        }
                        try {
                            AnimatedBoostActivity.this.n.sendEmptyMessage(0);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    try {
                        AnimatedBoostActivity.this.n.sendEmptyMessage(0);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else if (AnimatedBoostActivity.this.m) {
                } else {
                    AnimatedBoostActivity.this.progress++;
                    Log.e("======", "" + AnimatedBoostActivity.this.progress);
                    AnimatedBoostActivity.this.progressBar.setProgress(AnimatedBoostActivity.this.progress);
                    if (Build.VERSION.SDK_INT <= 23) {
                        if (AnimatedBoostActivity.this.progress >= 80 && !AnimatedBoostActivity.this.processesReceived) {
                            AnimatedBoostActivity.this.n.postDelayed(this, 2000L);
                            return;
                        } else {
                            AnimatedBoostActivity.this.n.postDelayed(this, 90L);
                            return;
                        }
                    }
                    AnimatedBoostActivity.this.n.postDelayed(this, 90L);
                }
            }
        }, 700L);
    }

    @Override
    public void onBackPressed() {
        this.m = true;
        Dialog dialog = new Dialog(this);
        this.o = dialog;
        dialog.requestWindowFeature(1);
        if (this.o.getWindow() != null) {
            this.o.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            this.o.getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
        }
        this.o.setContentView(R.layout.new_dialog_junk_cancel);
        this.o.setCancelable(false);
        this.o.setCanceledOnTouchOutside(false);
        this.o.getWindow().setLayout(-1, -1);
        this.o.getWindow().setGravity(17);

        ((ImageView) this.o.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_phone_boost));
        ((TextView) this.o.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_phone_boost_small));
        ((TextView) this.o.findViewById(R.id.dialog_msg)).setText(String.format(getResources().getString(R.string.mbc_simple_back_press), getResources().getString(R.string.mbc_boost_scan_txt)));
        this.o.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AnimatedBoostActivity.this.multipleClicked()) {
                    return;
                }
                AnimatedBoostActivity.this.m = false;
                Log.e("=========", "onBackPressed free update_progress() cancle button click");
                AnimatedBoostActivity.this.update_progress();
                AnimatedBoostActivity.this.o.dismiss();
            }
        });
        this.o.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AnimatedBoostActivity.this.multipleClicked()) {
                    return;
                }
                AnimatedBoostActivity.this.handlerProgress.removeMessages(0);
                AnimatedBoostActivity.this.m = false;
                AnimatedBoostActivity.this.o.dismiss();
                Util.appendLogmobiclean(AnimatedBoostActivity.TAG, "backpressed pDialog finish ", "");
                GlobalData.processDataList.clear();
                AnimatedBoostActivity.this.backToHome();
            }
        });
        this.o.show();
    }

    @Override
    public void onClick(View view) {
        if (multipleClicked()) {
            return;
        }
        if (view.getId() == R.id.ads_btn_cancel) {
            Log.e("=========", "onBackPressed Advance Native Ads update_progress() cancle button click");
            this.m = false;
            this.q.setVisibility(View.GONE);
            this.p.setVisibility(View.VISIBLE);
            update_progress();
        }
        if (view.getId() == R.id.ads_btn_countinue) {
            this.m = false;
            this.q.setVisibility(View.VISIBLE);
            this.p.setVisibility(View.GONE);
            this.handlerProgress.removeMessages(0);
            Util.appendLogmobiclean(TAG, "backpressed pDialog finish ", "");
            GlobalData.processDataList.clear();
            finish();
            if (this.redirectToHome || this.redirectToNoti || this.noti_result_back) {
                startActivity(new Intent(this.context, HomeActivity.class));
            }
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_boost_animation_screen);


        showInterstialAds(AnimatedBoostActivity.this);

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

        Log.e("TAG", "onCreate: " + "AnimatedBoost Activity");
        Util.isHome = false;
        GlobalData.processDataList.clear();
        init();
        setDimensions();
        performScanningAsPerCondition();
        this.n = new Handler();
        this.progress = 0;
        update_progress();
        clearNotification(new int[]{600});
        trackIfFromNotification();
        Util.appendLogmobiclean(TAG, " oncreate ends", "");
        redirectToHome();
        iffromShortcut();
        rocketAnimation();
        sendAnalytics("SCREEN_VISIT_F_" + getClass().getSimpleName());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("=========", "onPause() ");
        this.m = true;
        this.isInbackground = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Util.appendLogmobiclean(TAG, " onResume", "");
        this.isInbackground = false;
        RelativeLayout relativeLayout = this.q;
        if (relativeLayout != null && relativeLayout.getVisibility() == View.VISIBLE) {
            Log.e("=======", "pDialog.isShowing()");
            this.m = true;
        } else {
            Dialog dialog = this.o;
            if (dialog != null && dialog.isShowing()) {
                this.m = true;
                Log.e("=======", "pDialog.isShowing()");
            } else {
                Log.e("=======", "pDialog.isShowing() else part");
                this.m = false;
                update_progress();
            }
        }
//        switchScreen();
//        this.switched = true;
    }


//    public void loadinit() {
//        adRequest = new AdRequest.Builder().build();
//        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
//                new InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                        mInterstitialAd = interstitialAd;
//                        return;
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        Log.d(TAG, loadAdError.toString());
//                        mInterstitialAd = null;
//                    }
//                });
//    }
}
