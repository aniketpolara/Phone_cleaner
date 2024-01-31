package com.cleanPhone.mobileCleaner;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.room.RoomDatabase;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.google.firebase.messaging.ServiceStarter;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.animate.CircleAnimationUtil;
import com.cleanPhone.mobileCleaner.animate.CircleProgressView;
import com.cleanPhone.mobileCleaner.animate.JunkData;
import com.cleanPhone.mobileCleaner.processes.AndroidAppProcess;
import com.cleanPhone.mobileCleaner.processes.AppNames;
import com.cleanPhone.mobileCleaner.utility.AndroidProcesses;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.PermitActivity;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.AppDetails;
import com.cleanPhone.mobileCleaner.wrappers.JunkListWrapper;
import com.cleanPhone.mobileCleaner.wrappers.PackageInfoStruct;
import com.cleanPhone.mobileCleaner.wrappers.ProcessWrapper;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import okhttp3.internal.cache.DiskLruCache;


public class JunkScanActivity extends PermitActivity implements View.OnClickListener {
    private static final String CACHEFOLDER = "/Android/data/";
    private static final int REQUEST_PERMISSIONS = 20;
    public static final String SYSTEMCACHE = "appcache";
    private static final String TAG = "JunkScanActivity";
    private static RelativeLayout hiddenPermissionLayout;
    private TextView ads_message;
    int click = 0;
    int numOfClick = 3;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    private TextView ads_title;
    private ArrayList<JunkListWrapper> apkList;
    private long apksize;
    private ArrayList<JunkListWrapper> appCacheList;
    private ArrayList<String> appSpecificCacheList;
    private long appSpecificCacheSize;
    private CircleProgressView arcProgress_outer;
    private ArrayList<JunkListWrapper> boostList;
    private long cacheSize;
    private int deviceHeight;
    private int deviceWidth;
    private ProgressDialog dialogStopWait;
    private ArrayList<JunkListWrapper> emptyFolderList;
    private boolean fromnoti;
    private Handler handlerProgress;
    private ImageView imgClose;
    private ArrayList<PackageInfoStruct> installedApps;
    public Context j;
    private String lastpname;
    public HashMap<String, ArrayList<JunkListWrapper>> listDataChild;
    private ArrayList<String> logList;
    public RelativeLayout m;
    public RelativeLayout n;
    public FrameLayout o;
    public TextView p;
    private int progress;
    private ProgressDialog progressDialog;
    public TextView q;
    private RelativeLayout r1_apk;
    private RelativeLayout r1_app_cache;
    private RelativeLayout r1_boost;
    private RelativeLayout r1_temp_files;
    private RelativeLayout r1_unused;
    private boolean redirectToHome;
    private boolean redirectToNoti;
    private RelativeLayout rl_sys_cache;
    private AsyncTask<String, String, String> scanJunkTask;
    private String status;
    private ArrayList<JunkListWrapper> sysCacheList;
    private long sysCacheSize;
    private ArrayList<JunkListWrapper> tempList;
    private Toolbar toolbar;
    int admob = 3;
    private int totalselectedJunk;
    private TextView tv_apk;
    private TextView tv_app_cache;
    private TextView tv_boost;
    private TextView tv_category_text;
    private TextView tv_junk_count;
    private TextView tv_junk_processing;
    private TextView tv_percentage;
    private TextView tv_symbol;
    private TextView tv_sys_cache;
    private TextView tv_temp_files;
    private TextView tv_unused;
    public ImageView appcache, tempFile, apkFiles, emptyFolder, memoryBoost, back;
    public int v;
    private volatile boolean lastPackagefound = false;
    private long allSystemCacheSize = 0;
    private long allApkSize = 0;
    private long allTempSize = 0;
    private long allEmptySize = 0;
    private long allcacheUserSize = 0;
    private long appCache = 0;
    private long allBoostSize = 0;
    private final String APK = "apk";
    private final String EMPTY = JunkCleanScreen.EMPTY;
    private final String TEMP = JunkCleanScreen.TEMP;
    private final String CACHEUSER = JunkCleanScreen.CACHEUSER;
    private final String BOOSTFILES = JunkCleanScreen.BOOSTFILES;
    public long totalselectedJunkSize = 0;
    private ArrayList<String> alluserCachedfiles = new ArrayList<>();
    private int filecount = 0;
    private boolean isAborted = false;
    private boolean isWaitScreenShown = false;
    private boolean isInBackground = false;
    private boolean removeBoost = false;
    private boolean isAnim = true;
    public String k = "";
    public long l = 0;
    private boolean notProceeded = true;
    public long s = 0;
    public ArrayList<String> t = new ArrayList<>();
    public String u = "";
    private int tempProgress = 0;

    public class AnonymousClass7 extends AsyncTask<String, String, String> {
        public AnonymousClass7() {
        }

        private boolean addSizeIfAlreadyExists(JunkListWrapper junkListWrapper, long j) {
            for (int i = 0; i < JunkScanActivity.this.boostList.size(); i++) {
                String str = ((JunkListWrapper) JunkScanActivity.this.boostList.get(i)).pkg;
                if (str.equalsIgnoreCase("" + junkListWrapper.pkg)) {
                    junkListWrapper.size = ((JunkListWrapper) JunkScanActivity.this.boostList.get(i)).size + j;
                    JunkScanActivity.this.boostList.set(i, junkListWrapper);
                    return true;
                }
            }
            return false;
        }

        private void afterExecution() {
            JunkScanActivity.this.removeHandler();
            if (JunkScanActivity.this.isAborted) {
                return;
            }
            try {
                JunkScanActivity.this.getWindow().clearFlags(128);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JunkScanActivity.this.fillDataInHashMap();
            JunkScanActivity.this.r1_boost.setAlpha(1.0f);
            JunkScanActivity.this.tv_junk_processing.setText(String.format(JunkScanActivity.this.getResources().getString(R.string.mbc_junk).replace("DO_NOT_TRANSLATE", "%s"), Util.convertBytesJunk(JunkScanActivity.this.totalselectedJunkSize)));
            JunkScanActivity.this.isAborted = true;
            if (JunkScanActivity.this.isInBackground) {
                return;
            }
            Log.e(TAG, "afterExecution: " + "intent junk clean screen");
            JunkScanActivity.this.startActivity(new Intent(JunkScanActivity.this, JunkCleanScreen.class).putExtra(GlobalData.REDIRECTNOTI, true).putExtra("BOOSTED", JunkScanActivity.this.removeBoost).putExtra("file_processed", JunkScanActivity.this.l));
            JunkScanActivity.this.finish();
        }

        private void calculateRAMtoBoost() {
            List<AndroidAppProcess> list;
            int i;
            int i2;
            ActivityManager activityManager;
            Debug.MemoryInfo[] memoryInfoArr;
            AndroidAppProcess androidAppProcess;
            int i3;
            String str;
            String str2;
            String str3;
            CharSequence applicationLabel = null;
            StringBuilder sb = null;
            String str4 = null;
            String sb2 = null;
            int i4;
            int i5;
            boolean z;
            int i6;
            int i7;
            Debug.MemoryInfo[] memoryInfoArr2;
            ArrayList arrayList;
            String str5;
            String str6;
            ActivityManager activityManager2;
            String str7;
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo;
            String str8 = null;
            boolean z2;
            int i8;
            AnonymousClass7 anonymousClass7 = this;
            if (JunkScanActivity.this.isAborted) {
                return;
            }
            JunkScanActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JunkScanActivity.this.tv_apk.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    TextView textView = JunkScanActivity.this.tv_apk;
                    textView.setText("" + Util.convertBytesJunk(JunkScanActivity.this.apksize));
                    JunkScanActivity.this.tv_apk.clearAnimation();
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setDuration(300L);
                    alphaAnimation.setStartOffset(10L);
                    alphaAnimation.setRepeatMode(2);
                    alphaAnimation.setRepeatCount(-1);
                    JunkScanActivity.this.tv_unused.startAnimation(alphaAnimation);
                }
            });
            ActivityManager activityManager3 = (ActivityManager) JunkScanActivity.this.j.getSystemService(Context.ACTIVITY_SERVICE);
            PackageManager packageManager = JunkScanActivity.this.getPackageManager();
            try {
                List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager3.getRunningAppProcesses();
                ArrayList<PackageInfoStruct> installedSystemApps = new AppDetails(JunkScanActivity.this.j).getInstalledSystemApps();
                String str9 = "com.mobiclean.phoneclean";
                String str10 = "Google play services";
                String str11 = "";
                int i9 = 1;
                if (runningAppProcesses.size() > 1) {
                    ArrayList ignoredData = JunkScanActivity.this.getIgnoredData();
                    for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo2 : runningAppProcesses) {
                        if (JunkScanActivity.this.isAborted) {
                            return;
                        }
                        if (!ignoredData.contains(runningAppProcessInfo2.processName)) {
                            int[] iArr = new int[i9];
                            iArr[0] = runningAppProcessInfo2.pid;
                            Debug.MemoryInfo[] processMemoryInfo = activityManager3.getProcessMemoryInfo(iArr);
                            int length = processMemoryInfo.length;
                            int i10 = 0;
                            while (i10 < length) {
                                Debug.MemoryInfo memoryInfo = processMemoryInfo[i10];
                                ProcessWrapper processWrapper = new ProcessWrapper();
                                if (JunkScanActivity.this.isAborted) {
                                    return;
                                }
                                try {
                                    activityManager2 = activityManager3;
                                } catch (Exception e) {
                                    e = e;
                                    i6 = i10;
                                    i7 = length;
                                    memoryInfoArr2 = processMemoryInfo;
                                    arrayList = ignoredData;
                                    str5 = str11;
                                    str6 = str10;
                                    activityManager2 = activityManager3;
                                    str7 = str9;
                                    runningAppProcessInfo = runningAppProcessInfo2;
                                }
                                try {
                                    CharSequence applicationLabel2 = packageManager.getApplicationLabel(packageManager.getApplicationInfo(runningAppProcessInfo2.processName, 128));
                                    str8 = str11 + ((Object) applicationLabel2);
                                    processWrapper.appname = str8;
                                } catch (Exception e2) {
//                                    e = e2;
                                    i6 = i10;
                                    i7 = length;
                                    memoryInfoArr2 = processMemoryInfo;
                                    runningAppProcessInfo = runningAppProcessInfo2;
                                    arrayList = ignoredData;
                                    str5 = str11;
                                    str6 = str10;
                                    str7 = str9;
//                                    e.printStackTrace();
                                    processWrapper.appname = runningAppProcessInfo.processName;
                                    i10 = i6 + 1;
                                    runningAppProcessInfo2 = runningAppProcessInfo;
                                    ignoredData = arrayList;
                                    activityManager3 = activityManager2;
                                    length = i7;
                                    str9 = str7;
                                    processMemoryInfo = memoryInfoArr2;
                                    str11 = str5;
                                    str10 = str6;
                                    i9 = 1;
                                }
                                if (str8.equalsIgnoreCase(str10) || processWrapper.appname.equalsIgnoreCase("Google play store")) {
                                    i6 = i10;
                                    i7 = length;
                                } else {
                                    processWrapper.name = runningAppProcessInfo2.processName;
                                    int i11 = 0;
                                    while (true) {
                                        if (i11 >= installedSystemApps.size()) {
                                            i6 = i10;
                                            i7 = length;
                                            z2 = false;
                                            break;
                                        } else if (JunkScanActivity.this.isAborted) {
                                            return;
                                        } else {
                                            i6 = i10;
                                            StringBuilder sb3 = new StringBuilder();
                                            sb3.append(str11);
                                            i7 = length;
                                            sb3.append(installedSystemApps.get(i11).appname);
                                            sb3.append("  == ");
                                            sb3.append(processWrapper.appname);
                                            Log.d("COMPARE ", sb3.toString());
                                            if (installedSystemApps.get(i11).appname.equalsIgnoreCase(str11 + processWrapper.appname)) {
                                                if (Util.notInIgnoreList(str11 + processWrapper.appname)) {
                                                    z2 = true;
                                                    break;
                                                }
                                            }
                                            i11++;
                                            i10 = i6;
                                            length = i7;
                                        }
                                    }
                                    if (!z2 && !processWrapper.name.equalsIgnoreCase(str9) && ((i8 = runningAppProcessInfo2.importance) == 400 || i8 == 200 || i8 == 300)) {
                                        long totalPss = memoryInfo.getTotalPss() * 1024;
                                        processWrapper.size = totalPss;
                                        if (totalPss != 0) {
                                            processWrapper.pid = runningAppProcessInfo2.pid;
                                            String str12 = processWrapper.name;
                                            memoryInfoArr2 = processMemoryInfo;
                                            runningAppProcessInfo = runningAppProcessInfo2;
                                            arrayList = ignoredData;
                                            str5 = str11;
                                            str7 = str9;
                                            str6 = str10;
                                            addJunk(str12, JunkCleanScreen.BOOSTFILES, processWrapper.appname, totalPss, str12, null, null);
                                            i10 = i6 + 1;
                                            runningAppProcessInfo2 = runningAppProcessInfo;
                                            ignoredData = arrayList;
                                            activityManager3 = activityManager2;
                                            length = i7;
                                            str9 = str7;
                                            processMemoryInfo = memoryInfoArr2;
                                            str11 = str5;
                                            str10 = str6;
                                            i9 = 1;
                                        }
                                    }
                                }
                                memoryInfoArr2 = processMemoryInfo;
                                runningAppProcessInfo = runningAppProcessInfo2;
                                arrayList = ignoredData;
                                str5 = str11;
                                str6 = str10;
                                str7 = str9;
                                i10 = i6 + 1;
                                runningAppProcessInfo2 = runningAppProcessInfo;
                                ignoredData = arrayList;
                                activityManager3 = activityManager2;
                                length = i7;
                                str9 = str7;
                                processMemoryInfo = memoryInfoArr2;
                                str11 = str5;
                                str10 = str6;
                                i9 = 1;
                            }
                        }
                    }
                    return;
                }
                String str13 = "";
                String str14 = "Google play services";
                ActivityManager activityManager4 = activityManager3;
                String str15 = "com.mobiclean.phoneclean";
                int i12 = 128;
                if (Build.VERSION.SDK_INT > 23) {
                    ActivityManager.MemoryInfo memoryInfo2 = new ActivityManager.MemoryInfo();
                    ((ActivityManager) JunkScanActivity.this.j.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(memoryInfo2);
                    addJunk(JunkScanActivity.this.getString(R.string.mbc_memory_boost), JunkCleanScreen.BOOSTFILES, JunkScanActivity.this.getString(R.string.mbc_memory_boost), memoryInfo2.totalMem - memoryInfo2.availMem, JunkScanActivity.this.getString(R.string.mbc_memory_boost), null, null);
                    return;
                }
                List<AndroidAppProcess> runningAppProcesses2 = AndroidProcesses.getRunningAppProcesses();
                if (JunkScanActivity.this.isAborted) {
                    return;
                }
                Collections.sort(runningAppProcesses2, new Comparator<AndroidAppProcess>() {
                    @Override
                    public int compare(AndroidAppProcess androidAppProcess2, AndroidAppProcess androidAppProcess3) {
                        AnonymousClass7 anonymousClass72 = AnonymousClass7.this;
                        String name = anonymousClass72.getName(JunkScanActivity.this.j, androidAppProcess2);
                        AnonymousClass7 anonymousClass73 = AnonymousClass7.this;
                        return name.compareToIgnoreCase(anonymousClass73.getName(JunkScanActivity.this.j, androidAppProcess3));
                    }
                });
                ArrayList ignoredData2 = JunkScanActivity.this.getIgnoredData();
                int i13 = 0;
                while (i13 < runningAppProcesses2.size() && !JunkScanActivity.this.isAborted) {
                    AndroidAppProcess androidAppProcess2 = runningAppProcesses2.get(i13);
                    ActivityManager activityManager5 = activityManager4;
                    Debug.MemoryInfo[] processMemoryInfo2 = activityManager5.getProcessMemoryInfo(new int[]{androidAppProcess2.pid});
                    int length2 = processMemoryInfo2.length;
                    int i14 = 0;
                    while (i14 < length2) {
                        Debug.MemoryInfo memoryInfo3 = processMemoryInfo2[i14];
                        if (ignoredData2.contains(androidAppProcess2.getPackageName())) {
                            list = runningAppProcesses2;
                            i = i14;
                            i2 = length2;
                            activityManager = activityManager5;
                            memoryInfoArr = processMemoryInfo2;
                            androidAppProcess = androidAppProcess2;
                            i3 = i13;
                            str = str15;
                            str2 = str13;
                            str3 = str14;
                        } else if (JunkScanActivity.this.isAborted) {
                            return;
                        } else {
                            ProcessWrapper processWrapper2 = new ProcessWrapper();
                            try {
                                applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(androidAppProcess2.name, i12));
                                sb = new StringBuilder();
                                i3 = i13;
                                str4 = str13;
                            } catch (Exception unused) {
                                list = runningAppProcesses2;
                                i = i14;
                                i2 = length2;
                                activityManager = activityManager5;
                                memoryInfoArr = processMemoryInfo2;
                                androidAppProcess = androidAppProcess2;
                                i3 = i13;
                                str = str15;
                                str2 = str13;
                            }
                            try {
                                sb.append(str4);
                                sb.append(applicationLabel);
                                sb2 = sb.toString();
                                processWrapper2.appname = sb2;
                                str3 = str14;
                            } catch (Exception unused2) {
                                list = runningAppProcesses2;
                                i = i14;
                                i2 = length2;
                                activityManager = activityManager5;
                                memoryInfoArr = processMemoryInfo2;
                                androidAppProcess = androidAppProcess2;
                                str2 = str4;
                                str = str15;
                                str3 = str14;
                                processWrapper2.appname = androidAppProcess.name;
                                i14 = i + 1;
                                androidAppProcess2 = androidAppProcess;
                                str14 = str3;
                                i13 = i3;
                                runningAppProcesses2 = list;
                                length2 = i2;
                                activityManager5 = activityManager;
                                processMemoryInfo2 = memoryInfoArr;
                                str13 = str2;
                                str15 = str;
                                i12 = 128;
                                anonymousClass7 = this;
                            }
                            try {
                            } catch (Exception unused3) {
                                list = runningAppProcesses2;
                                i = i14;
                                i2 = length2;
                                activityManager = activityManager5;
                                memoryInfoArr = processMemoryInfo2;
                                androidAppProcess = androidAppProcess2;
                                str2 = str4;
                                str = str15;
                                processWrapper2.appname = androidAppProcess.name;
                                i14 = i + 1;
                                androidAppProcess2 = androidAppProcess;
                                str14 = str3;
                                i13 = i3;
                                runningAppProcesses2 = list;
                                length2 = i2;
                                activityManager5 = activityManager;
                                processMemoryInfo2 = memoryInfoArr;
                                str13 = str2;
                                str15 = str;
                                i12 = 128;
                                anonymousClass7 = this;
                            }
                            if (sb2.equalsIgnoreCase(str3) || processWrapper2.appname.equalsIgnoreCase("Google play store")) {
                                list = runningAppProcesses2;
                                i = i14;
                                i2 = length2;
                                activityManager = activityManager5;
                                memoryInfoArr = processMemoryInfo2;
                                androidAppProcess = androidAppProcess2;
                                str2 = str4;
                                str = str15;
                            } else {
                                processWrapper2.name = androidAppProcess2.name;
                                list = runningAppProcesses2;
                                int i15 = 0;
                                while (true) {
                                    if (i15 >= installedSystemApps.size()) {
                                        i4 = i14;
                                        i5 = length2;
                                        z = false;
                                        break;
                                    } else if (JunkScanActivity.this.isAborted) {
                                        return;
                                    } else {
                                        String str16 = installedSystemApps.get(i15).appname;
                                        i4 = i14;
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append(str4);
                                        i5 = length2;
                                        sb4.append(processWrapper2.appname);
                                        if (str16.equalsIgnoreCase(sb4.toString())) {
                                            if (Util.notInIgnoreList(str4 + processWrapper2.appname)) {
                                                z = true;
                                                break;
                                            }
                                        }
                                        i15++;
                                        i14 = i4;
                                        length2 = i5;
                                    }
                                }
                                if (z) {
                                    memoryInfoArr = processMemoryInfo2;
                                    androidAppProcess = androidAppProcess2;
                                    str2 = str4;
                                    i = i4;
                                    i2 = i5;
                                    str = str15;
                                } else {
                                    String str17 = str15;
                                    if (processWrapper2.name.equalsIgnoreCase(str17)) {
                                        memoryInfoArr = processMemoryInfo2;
                                        str = str17;
                                        androidAppProcess = androidAppProcess2;
                                        str2 = str4;
                                        i = i4;
                                        i2 = i5;
                                    } else {
                                        processWrapper2.pid = androidAppProcess2.pid;
                                        long totalPss2 = memoryInfo3.getTotalPss() * 1024;
                                        processWrapper2.size = totalPss2;
                                        String str18 = processWrapper2.appname;
                                        String str19 = processWrapper2.name;
                                        i = i4;
                                        i2 = i5;
                                        activityManager = activityManager5;
                                        memoryInfoArr = processMemoryInfo2;
                                        str = str17;
                                        androidAppProcess = androidAppProcess2;
                                        str2 = str4;
                                        addJunk(str18, JunkCleanScreen.BOOSTFILES, str19, totalPss2, str19, null, null);
                                        i14 = i + 1;
                                        androidAppProcess2 = androidAppProcess;
                                        str14 = str3;
                                        i13 = i3;
                                        runningAppProcesses2 = list;
                                        length2 = i2;
                                        activityManager5 = activityManager;
                                        processMemoryInfo2 = memoryInfoArr;
                                        str13 = str2;
                                        str15 = str;
                                        i12 = 128;
                                        anonymousClass7 = this;
                                    }
                                }
                                activityManager = activityManager5;
                                i14 = i + 1;
                                androidAppProcess2 = androidAppProcess;
                                str14 = str3;
                                i13 = i3;
                                runningAppProcesses2 = list;
                                length2 = i2;
                                activityManager5 = activityManager;
                                processMemoryInfo2 = memoryInfoArr;
                                str13 = str2;
                                str15 = str;
                                i12 = 128;
                                anonymousClass7 = this;
                            }
                        }
                        i14 = i + 1;
                        androidAppProcess2 = androidAppProcess;
                        str14 = str3;
                        i13 = i3;
                        runningAppProcesses2 = list;
                        length2 = i2;
                        activityManager5 = activityManager;
                        processMemoryInfo2 = memoryInfoArr;
                        str13 = str2;
                        str15 = str;
                        i12 = 128;
                        anonymousClass7 = this;
                    }
                    i13++;
                    anonymousClass7 = this;
                    activityManager4 = activityManager5;
                    i12 = 128;
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }

        private void getpackageSize() {
            if (JunkScanActivity.this.isAborted) {
                return;
            }
            PackageManager packageManager = JunkScanActivity.this.getPackageManager();
            ArrayList ignoredData = JunkScanActivity.this.getIgnoredData();
            if (JunkScanActivity.this.installedApps.size() > 0) {
                JunkScanActivity junkScanActivity = JunkScanActivity.this;
                junkScanActivity.lastpname = ((PackageInfoStruct) junkScanActivity.installedApps.get(JunkScanActivity.this.installedApps.size() - 1)).pname;
            }
            float currentValue = JunkScanActivity.this.arcProgress_outer.getCurrentValue();
            JunkScanActivity.this.arcProgress_outer.setValue(currentValue);
            JunkScanActivity.this.tv_percentage.setText(String.valueOf(currentValue));
            for (int i = 0; i < JunkScanActivity.this.installedApps.size(); i++) {
                if (JunkScanActivity.this.isAborted) {
                    return;
                }
                if (!ignoredData.contains(((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i)).pname)) {
                    try {
                        packageManager.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class).invoke(packageManager, ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i)).pname, new cachePackState(i, currentValue));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            while (!JunkScanActivity.this.lastPackagefound) {
                if (JunkScanActivity.this.isAborted) {
                    return;
                }
            }
            if (JunkScanActivity.this.isAborted) {
                return;
            }
            JunkScanActivity.this.allSystemCacheSize = 0L;
            for (int i2 = 0; i2 < JunkScanActivity.this.installedApps.size() && !JunkScanActivity.this.isAborted; i2++) {
                PackageInfoStruct packageInfoStruct = (PackageInfoStruct) JunkScanActivity.this.installedApps.get(i2);
                long j = packageInfoStruct.cachesize;
                if (j > 0) {
                    String str = packageInfoStruct.appname;
                    addJunk(str, JunkScanActivity.SYSTEMCACHE, str, j, packageInfoStruct.pname, null, null);
                    JunkScanActivity.this.allSystemCacheSize += packageInfoStruct.cachesize;
                }
            }
        }

        private void searchJunk(String str) {
            CharSequence charSequence;
            int i;
            boolean z;
            int i2;
            int i3;
            CharSequence charSequence2;
            if (JunkScanActivity.this.isAborted) {
                return;
            }
            if (JunkScanActivity.this.t.size() > 0) {
                JunkScanActivity.this.allTempSize += JunkScanActivity.this.s;
                JunkScanActivity junkScanActivity = JunkScanActivity.this;
                String str2 = junkScanActivity.u;
                String str3 = junkScanActivity.t.get(0);
                JunkScanActivity junkScanActivity2 = JunkScanActivity.this;
                addJunk(str2, JunkCleanScreen.TEMP, str3, junkScanActivity2.s, null, junkScanActivity2.t.get(0), JunkScanActivity.this.t);
            }
            JunkScanActivity junkScanActivity3 = JunkScanActivity.this;
            junkScanActivity3.s = 0L;
            junkScanActivity3.t.clear();
            JunkScanActivity.this.u = "";
            File file = new File(str);
            if (file.exists()) {
                File[] listFiles = file.listFiles();
                if (JunkScanActivity.this.filecount % 25 == 0) {
                    JunkScanActivity.this.sleep(1);
                }
                JunkScanActivity.O(JunkScanActivity.this);
                if (listFiles != null) {
                    if (listFiles.length != 0 || file.isHidden() || file.getPath().contains("/.")) {
                        charSequence = "/.";
                        i = 1;
                        z = true;
                    } else {
                        JunkScanActivity.this.allEmptySize += 0;
                        charSequence = "/.";
                        i = 1;
                        addJunk(file.getName(), JunkCleanScreen.EMPTY, str, 0L, null, str, null);
                        z = false;
                    }
                    if (z) {
                        int length = listFiles.length;
                        int i4 = 0;
                        while (i4 < length) {
                            File file2 = listFiles[i4];
                            JunkScanActivity junkScanActivity4 = JunkScanActivity.this;
                            long j = junkScanActivity4.l + 1;
                            junkScanActivity4.l = j;
                            if (j % 1000 == 0) {
                                junkScanActivity4.sleep(i);
                            }
                            String[] strArr = new String[3];
                            strArr[0] = "";
                            strArr[i] = "";
                            strArr[2] = JunkScanActivity.this.l + " " + JunkScanActivity.this.k;
                            publishProgress(strArr);
                            if (JunkScanActivity.this.isAborted) {
                                return;
                            }
                            String extension = FilenameUtils.getExtension(file2.getPath());
                            if (file2.isFile() && extension.equalsIgnoreCase("apk")) {
                                if (!file2.getPath().contains("" + GlobalData.backuppath)) {
                                    JunkScanActivity.this.allApkSize += file2.length();
                                    try {
                                        i2 = i4;
                                        i3 = length;
                                        addJunk(file2.getName(), "apk", file2.getPath(), file2.length(), JunkScanActivity.this.getPackageManager().getPackageArchiveInfo(file2.getPath(), 0).packageName, file2.getPath(), null);
                                    } catch (Exception e) {
                                        i2 = i4;
                                        i3 = length;
                                        e.printStackTrace();
                                    }
                                    charSequence2 = charSequence;
                                    i4 = i2 + 1;
                                    charSequence = charSequence2;
                                    length = i3;
                                }
                            }
                            i2 = i4;
                            i3 = length;
                            charSequence2 = charSequence;
                            if (!file2.getPath().contains(charSequence2) && !file2.isHidden() && file2.isFile()) {
                                if (JunkScanActivity.this.logList.contains("" + extension)) {
                                    JunkScanActivity.this.s += file2.length();
                                    JunkScanActivity.this.t.add(file2.getPath());
                                    JunkScanActivity.this.u = "" + file2.getParent();
                                    i4 = i2 + 1;
                                    charSequence = charSequence2;
                                    length = i3;
                                }
                            }
                            if (file2.isDirectory()) {
                                searchJunk(file2.getPath());
                            }
                            i4 = i2 + 1;
                            charSequence = charSequence2;
                            length = i3;
                        }
                    }
                }
            }
        }

        private void sortUserCacheAccordingToApps() {
            if (JunkScanActivity.this.isAborted) {
                return;
            }
            ArrayList ignoredData = JunkScanActivity.this.getIgnoredData();
            JunkScanActivity.this.installedApps = new AppDetails(JunkScanActivity.this).getInstalledApps();
            for (int i = 0; i < JunkScanActivity.this.installedApps.size(); i++) {
                if (JunkScanActivity.this.isAborted) {
                    return;
                }
                if (ignoredData.contains(((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i)).pname)) {
                    ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i)).userCachefilesList = new ArrayList<>();
                } else {
                    JunkScanActivity.this.appSpecificCacheSize = 0L;
                    JunkScanActivity.this.appSpecificCacheList = null;
                    JunkScanActivity.this.appSpecificCacheList = new ArrayList();
                    usercacheFolders(Environment.getExternalStorageDirectory().getPath() + JunkScanActivity.CACHEFOLDER + ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i)).pname);
                    ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i)).userCachefilesList = new ArrayList<>();
                    ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i)).userCachefilesList.addAll(JunkScanActivity.this.appSpecificCacheList);
                    ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i)).userCacheSize = JunkScanActivity.this.appSpecificCacheSize;
                }
            }
            for (int i2 = 0; i2 < JunkScanActivity.this.installedApps.size() && !JunkScanActivity.this.isAborted; i2++) {
                if (((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i2)).userCachefilesList.size() > 0) {
                    JunkScanActivity.this.appCache += ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i2)).userCacheSize;
                    addJunk(((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i2)).appname, JunkCleanScreen.CACHEUSER, "", ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i2)).userCacheSize, ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i2)).pname, null, ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(i2)).userCachefilesList);
                }
            }
        }

        private void usercacheFolders(String str) {
            File[] listFiles;
            File file = new File(str);
            Log.e("PPP", str + "");
            if (!file.exists() || (listFiles = file.listFiles()) == null || listFiles.length <= 0) {
                return;
            }
            for (File file2 : listFiles) {
                if (file2.isDirectory()) {
                    if (file2.getPath().endsWith("/.cache") || file2.getPath().endsWith("/cache")) {
                        JunkScanActivity.this.l++;
                        publishProgress("", "", JunkScanActivity.this.l + " " + JunkScanActivity.this.k);
                        JunkScanActivity.this.getFilesFromCacheFolder(file2.getPath());
                        GlobalData.allcacheFoldersPath.add(file2.getPath());
                    }
                    usercacheFolders(file2.getPath());
                }
            }
        }

        public void addJunk(String str, String str2, String str3, long j, String str4, String str5, ArrayList<String> arrayList) {
            if (JunkScanActivity.this.isAborted) {
                return;
            }
            if (str4 != null) {
                try {
                    if (str4.equalsIgnoreCase("Clean up master") || str4.equalsIgnoreCase("com.mobiclean.phoneclean")) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            JunkListWrapper junkListWrapper = new JunkListWrapper();
            junkListWrapper.name = "" + str;
            junkListWrapper.type = "" + str2;
            junkListWrapper.appname = "" + str3;
            junkListWrapper.size = j;
            junkListWrapper.pkg = str4;
            junkListWrapper.path = str5;
            junkListWrapper.fileslist = arrayList;
            char c2 = 0;
            if (str2.equalsIgnoreCase("apk")) {
                junkListWrapper.ischecked = false;
            }
            if (!str2.equalsIgnoreCase("apk")) {
                JunkScanActivity junkScanActivity = JunkScanActivity.this;
                junkScanActivity.totalselectedJunkSize += j;
                junkScanActivity.totalselectedJunk++;
            }
            switch (str2.hashCode()) {
                case 96796:
                    if (str2.equals("apk")) {
                        c2 = 2;
                        break;
                    }
                    c2 = '\uffff';
                    break;
                case 3556308:
                    if (str2.equals(JunkCleanScreen.TEMP)) {
                        c2 = 3;
                        break;
                    }
                    c2 = '\uffff';
                    break;
                case 93922211:
                    if (str2.equals(JunkCleanScreen.BOOSTFILES)) {
                        c2 = 5;
                        break;
                    }
                    c2 = '\uffff';
                    break;
                case 96634189:
                    if (str2.equals(JunkCleanScreen.EMPTY)) {
                        c2 = 4;
                        break;
                    }
                    c2 = '\uffff';
                    break;
                case 342649495:
                    if (str2.equals(JunkCleanScreen.CACHEUSER)) {
                        break;
                    }
                    c2 = '\uffff';
                    break;
                case 1170956801:
                    if (str2.equals(JunkScanActivity.SYSTEMCACHE)) {
                        c2 = 1;
                        break;
                    }
                    c2 = '\uffff';
                    break;
                default:
                    c2 = '\uffff';
                    break;
            }
            if (c2 == 0) {
                JunkScanActivity.this.appCacheList.add(junkListWrapper);
            } else if (c2 == 1) {
                JunkScanActivity.this.sysCacheList.add(junkListWrapper);
            } else if (c2 == 2) {
                Log.e("-----jiunk ", "" + junkListWrapper.pkg + "==com.mobiclean.phoneclean");
                if (junkListWrapper.pkg.equalsIgnoreCase("com.mobiclean.phoneclean")) {
                    return;
                }
                JunkScanActivity.this.apkList.add(junkListWrapper);
            } else if (c2 == 3) {
                JunkScanActivity.this.tempList.add(junkListWrapper);
            } else if (c2 == 4) {
                JunkScanActivity.this.emptyFolderList.add(junkListWrapper);
            } else if (c2 != 5) {
            } else {
                JunkScanActivity.this.allBoostSize += junkListWrapper.size;
                JunkScanActivity.this.boostList.add(junkListWrapper);
            }
        }

        public String getName(Context context, AndroidAppProcess androidAppProcess) {
            try {
                return AppNames.getLabel(context.getPackageManager(), androidAppProcess.getPackageInfo(context, 0));
            } catch (PackageManager.NameNotFoundException unused) {
                return androidAppProcess.name;
            }
        }

        @Override
        public void onCancelled() {
            super.onCancelled();
            if (JunkScanActivity.this.isAborted) {
                if (JunkScanActivity.this.dialogStopWait != null && JunkScanActivity.this.dialogStopWait.isShowing() && !JunkScanActivity.this.isFinishing()) {
                    JunkScanActivity.this.dialogStopWait.dismiss();
                }
                JunkScanActivity.this.goBackToHome();
            }
        }

        @Override
        public void onPreExecute() {
            try {
                GlobalData.allcacheFoldersPath.clear();
                JunkScanActivity.this.getWindow().addFlags(128);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JunkScanActivity.this.totalselectedJunkSize = 0L;
        }

        @Override
        public String doInBackground(String... strArr) {
            JunkScanActivity junkScanActivity = JunkScanActivity.this;
            junkScanActivity.k = junkScanActivity.getString(R.string.mbc_file_processed);
            if (JunkScanActivity.this.isAborted) {
                return null;
            }
            JunkScanActivity.this.fillLogList();
            sortUserCacheAccordingToApps();
            JunkScanActivity.this.animateCardView(JunkCleanScreen.CACHEUSER);
            JunkScanActivity.this.sleep(1000);
            JunkScanActivity.this.status = "Calculating cache..";
            if (Build.VERSION.SDK_INT <= 19) {
                getpackageSize();
                JunkScanActivity.this.animateCardView(JunkScanActivity.SYSTEMCACHE);
                JunkScanActivity.this.sleep(1000);
                JunkScanActivity.this.status = "Searching junk files..";
            }
            JunkScanActivity.this.isAnim = false;
            JunkScanActivity.this.animateCardView(JunkCleanScreen.TEMP);
            searchJunk("" + Environment.getExternalStorageDirectory().getPath());
            JunkScanActivity.this.isAnim = true;
            JunkScanActivity.this.animateCardView(JunkCleanScreen.TEMP);
            JunkScanActivity.this.sleep(1100);
            JunkScanActivity.this.animateCardView("apk");
            JunkScanActivity.this.sleep(1100);
            JunkScanActivity.this.animateCardView(JunkCleanScreen.EMPTY);
            JunkScanActivity.this.sleep(1600);
            if (JunkScanActivity.this.removeBoost) {
                JunkScanActivity.this.sleep(ServiceStarter.ERROR_UNKNOWN);
                return null;
            }
            calculateRAMtoBoost();
            JunkScanActivity.this.animateCardView(JunkCleanScreen.BOOSTFILES);
            JunkScanActivity.this.sleep(3500);
            JunkScanActivity.this.status = "Space to be optimized.";
            return null;
        }

        @Override
        public void onPostExecute(String str) {
            if (JunkScanActivity.this.dialogStopWait.isShowing()) {
                JunkScanActivity.this.dialogStopWait.dismiss();
            }
            afterExecution();
        }

        @Override
        public void onProgressUpdate(String... strArr) {
            super.onProgressUpdate(strArr);
            if (!TextUtils.isEmpty(strArr[0])) {
                TextView textView = JunkScanActivity.this.tv_junk_count;
                textView.setText("" + Util.convertBytesJunk(Long.parseLong(strArr[0])));
            }
            if (TextUtils.isEmpty(strArr[2])) {
                return;
            }
            try {
                TextView textView2 = JunkScanActivity.this.tv_junk_processing;
                textView2.setText("" + strArr[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class cachePackState extends IPackageStatsObserver.Stub {
        private int index;
        private float tempProgress;

        public cachePackState(int i, float f) {
            this.index = i;
            this.tempProgress = f;
        }

        private void fillCacheSizes(PackageStats packageStats) {
            if (Build.VERSION.SDK_INT >= 23) {
                ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(this.index)).cachesize = packageStats.cacheSize + packageStats.externalCacheSize;
            } else {
                ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(this.index)).cachesize = packageStats.cacheSize + packageStats.externalCacheSize;
            }
            ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(this.index)).dataSize = packageStats.dataSize;
            ((PackageInfoStruct) JunkScanActivity.this.installedApps.get(this.index)).appsize = packageStats.externalCodeSize;
            float size = 16.0f / JunkScanActivity.this.installedApps.size();
            float currentValue = JunkScanActivity.this.arcProgress_outer.getCurrentValue();
            this.tempProgress = currentValue;
            final float f = currentValue + size;
            JunkScanActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (JunkScanActivity.this.isAborted) {
                        return;
                    }
                    JunkScanActivity junkScanActivity = JunkScanActivity.this;
                    junkScanActivity.l++;
                    junkScanActivity.tv_junk_processing.setText(JunkScanActivity.this.l + " " + JunkScanActivity.this.k);
                    JunkScanActivity.this.arcProgress_outer.setValue(f);
                    JunkScanActivity.this.tv_percentage.setText(String.valueOf(f));
                    JunkScanActivity.this.arcProgress_outer.setValue((float) ((int) f));
                }
            });
            String str = packageStats.packageName;
            if (str.equalsIgnoreCase("" + JunkScanActivity.this.lastpname)) {
                JunkScanActivity.this.lastPackagefound = true;
            }
        }

        @Override
        public void onGetStatsCompleted(PackageStats packageStats, boolean z) throws RemoteException {
            fillCacheSizes(packageStats);
        }
    }

    public static int O(JunkScanActivity junkScanActivity) {
        int i = junkScanActivity.filecount;
        junkScanActivity.filecount = i + 1;
        return i;
    }


    public void animateCardView(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!JunkScanActivity.this.isAborted) {
                    String str2 = str;
                    str2.hashCode();
                    char c2 = '\uffff';
                    switch (str2.hashCode()) {
                        case 96796:
                            if (str2.equals("apk")) {
                                c2 = 0;
                                break;
                            }
                            break;
                        case 3556308:
                            if (str2.equals(JunkCleanScreen.TEMP)) {
                                c2 = 1;
                                break;
                            }
                            break;
                        case 93922211:
                            if (str2.equals(JunkCleanScreen.BOOSTFILES)) {
                                c2 = 2;
                                break;
                            }
                            break;
                        case 96634189:
                            if (str2.equals(JunkCleanScreen.EMPTY)) {
                                c2 = 3;
                                break;
                            }
                            break;
                        case 342649495:
                            if (str2.equals(JunkCleanScreen.CACHEUSER)) {
                                c2 = 4;
                                break;
                            }
                            break;
                        case 1170956801:
                            if (str2.equals(JunkScanActivity.SYSTEMCACHE)) {
                                c2 = 5;
                                break;
                            }
                            break;
                    }
                    switch (c2) {
                        case 0:
                            JunkScanActivity.this.blinkImageView("4");
                            break;
                        case 1:
                            JunkScanActivity.this.blinkImageView(ExifInterface.GPS_MEASUREMENT_3D);
                            break;
                        case 2:
                            JunkScanActivity.this.blinkImageView("6");
                            break;
                        case 3:
                            JunkScanActivity.this.blinkImageView("5");
                            break;
                        case 4:
                            JunkScanActivity.this.blinkImageView(ExifInterface.GPS_MEASUREMENT_2D);
                            break;
                        case 5:
                            JunkScanActivity.this.sysCacheSize = 0L;
                            for (int i = 0; i < JunkScanActivity.this.sysCacheList.size(); i++) {
                                JunkScanActivity.this.sysCacheSize += ((JunkListWrapper) JunkScanActivity.this.sysCacheList.get(i)).size;
                            }
                            JunkScanActivity.this.tv_junk_count.setText("" + Util.convertBytesJunk(JunkScanActivity.this.sysCacheSize));
                            new CircleAnimationUtil().attachActivity(JunkScanActivity.this).setTargetView(JunkScanActivity.this.tv_junk_count).setDestView(JunkScanActivity.this.tv_app_cache).startAnimation();
                            break;
                    }
                }
                JunkScanActivity junkScanActivity = JunkScanActivity.this;
                junkScanActivity.v = (int) junkScanActivity.arcProgress_outer.getCurrentValue();
                if (str.equalsIgnoreCase(JunkCleanScreen.BOOSTFILES)) {
                    JunkScanActivity.this.progress = 100;
                } else {
                    int i2 = JunkScanActivity.this.removeBoost ? 20 : 16;
                    if (JunkScanActivity.this.isAnim) {
                        JunkScanActivity.this.progress += i2;
                    }
                }
                try {
                    JunkScanActivity.this.handlerProgress.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JunkScanActivity junkScanActivity2 = JunkScanActivity.this;
                                if (junkScanActivity2.v < junkScanActivity2.progress) {
                                    JunkScanActivity junkScanActivity3 = JunkScanActivity.this;
                                    int i3 = junkScanActivity3.v + 1;
                                    junkScanActivity3.v = i3;
                                    if (i3 > 100) {
                                        junkScanActivity3.v = 100;
                                    }
                                    junkScanActivity3.arcProgress_outer.setValue(JunkScanActivity.this.v);
                                    JunkScanActivity.this.tv_percentage.setText(String.valueOf(JunkScanActivity.this.v));
                                    JunkScanActivity junkScanActivity4 = JunkScanActivity.this;
                                    if (junkScanActivity4.v >= 100) {
                                        junkScanActivity4.handlerProgress.postDelayed(this, 30L);
                                    }
                                    JunkScanActivity.this.removeHandler();
                                }
                            } catch (Exception unused) {
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void blinkImageView(String str) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300L);
        alphaAnimation.setStartOffset(10L);
        alphaAnimation.setRepeatMode(2);
        alphaAnimation.setRepeatCount(-1);
        if (str.equalsIgnoreCase("1")) {
            if (!GlobalData.dontAnimate) {
                this.appcache.startAnimation(alphaAnimation);
            }
            this.rl_sys_cache.setAlpha(1.0f);
            this.r1_unused.setAlpha(0.3f);
            this.r1_apk.setAlpha(0.3f);
            this.r1_temp_files.setAlpha(0.3f);
            this.r1_boost.setAlpha(0.3f);
            return;
        }
        if (str.equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
            if (!GlobalData.dontAnimate) {
                this.appcache.startAnimation(alphaAnimation);
            }
            this.cacheSize = 0L;
            for (int i = 0; i < this.appCacheList.size(); i++) {
                this.cacheSize += this.appCacheList.get(i).size;
            }
            this.appcache.setVisibility(View.INVISIBLE);
            this.tv_sys_cache.setVisibility(View.VISIBLE);
            this.tv_sys_cache.setText("" + Util.convertBytesJunk(this.appCache));
            this.appcache.clearAnimation();
//            this.r1_app_cache.setAlpha(1.0f);
            this.r1_temp_files.setAlpha(0.3f);
            this.r1_apk.setAlpha(0.3f);
            this.r1_unused.setAlpha(0.3f);
            this.r1_boost.setAlpha(0.3f);
            this.appCache = 0L;
            for (int i2 = 0; i2 < this.appCacheList.size(); i2++) {
                this.appCache += this.appCacheList.get(i2).size;
            }
            this.tv_junk_count.setVisibility(View.VISIBLE);
            this.tv_junk_count.setText("" + Util.convertBytesJunk(this.appCache));
            new CircleAnimationUtil().attachActivity(this).setTargetView(this.tv_junk_count).setDestView(this.tv_sys_cache).startAnimation();
        } else if (str.equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_3D)) {
            if (!GlobalData.dontAnimate) {
                this.tempFile.startAnimation(alphaAnimation);
            }
            this.tempFile.setVisibility(View.INVISIBLE);
            this.appcache.clearAnimation();
            this.r1_temp_files.setAlpha(1.0f);
            this.r1_apk.setAlpha(0.3f);
            this.r1_unused.setAlpha(0.3f);
            this.r1_boost.setAlpha(0.3f);
            if (this.isAnim) {
                this.tv_junk_count.setText("" + Util.convertBytesJunk(this.allTempSize));
                new CircleAnimationUtil().attachActivity(this).setTargetView(this.tv_junk_count).setDestView(this.tv_temp_files).startAnimation();
            }
        } else if (str.equalsIgnoreCase("4")) {
            if (!GlobalData.dontAnimate) {
                this.apkFiles.startAnimation(alphaAnimation);
            }
            this.tempFile.setVisibility(View.INVISIBLE);
            this.tv_temp_files.setVisibility(View.VISIBLE);
            this.tv_temp_files.setText("" + Util.convertBytesJunk(this.allTempSize));
            this.tempFile.clearAnimation();
            this.r1_apk.setAlpha(1.0f);
            this.r1_unused.setAlpha(0.3f);
            this.r1_boost.setAlpha(0.3f);
            this.apksize = 0L;
            for (int i3 = 0; i3 < this.apkList.size(); i3++) {
                this.apksize += this.apkList.get(i3).size;
            }
            this.tv_junk_count.setText("" + Util.convertBytesJunk(this.apksize));
            new CircleAnimationUtil().attachActivity(this).setTargetView(this.tv_junk_count).setDestView(this.tv_apk).startAnimation();
        } else if (str.equalsIgnoreCase("5")) {
            if (!GlobalData.dontAnimate) {
                this.tv_unused.startAnimation(alphaAnimation);
            }
            this.apkFiles.setVisibility(View.INVISIBLE);
            this.tv_apk.setVisibility(View.VISIBLE);
            this.tv_apk.setText("" + Util.convertBytesJunk(this.apksize));
            this.apkFiles.clearAnimation();
            this.r1_unused.setAlpha(1.0f);
            this.r1_boost.setAlpha(0.3f);
            this.tv_junk_count.setText("" + this.emptyFolderList.size());
            new CircleAnimationUtil().attachActivity(this).setTargetView(this.tv_junk_count).setDestView(this.tv_unused).startAnimation();
        } else if (str.equalsIgnoreCase("6")) {
            this.r1_boost.setAlpha(1.0f);
            if (!GlobalData.dontAnimate) {
                this.memoryBoost.startAnimation(alphaAnimation);
            }
            this.emptyFolder.setVisibility(View.INVISIBLE);
            this.tv_unused.setVisibility(View.VISIBLE);
            this.tv_unused.clearAnimation();
            this.emptyFolder.setVisibility(View.INVISIBLE);
            this.tv_unused.setText("" + this.emptyFolderList.size());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    JunkScanActivity.this.tv_junk_count.setText(Util.convertBytesJunk(JunkScanActivity.this.allBoostSize));
                    new CircleAnimationUtil().attachActivity(JunkScanActivity.this).setTargetView(JunkScanActivity.this.tv_junk_count).setDestView(JunkScanActivity.this.tv_boost).startAnimation();
                    JunkScanActivity.this.tv_boost.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    JunkScanActivity.this.tv_boost.setText(Util.convertBytesJunk(JunkScanActivity.this.allBoostSize));
                }
            }, 1000L);
        }
    }

    private void clearNotification() {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(ServiceStarter.ERROR_UNKNOWN);
            notificationManager.cancel(RoomDatabase.MAX_BIND_PARAMETER_CNT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void fillDataInHashMap() {
        String[] stringArray;
        Util.appendLogmobiclean(TAG, " method:fillDataInHashMap ", "");
        int i = Build.VERSION.SDK_INT;
        if (i > 19) {
            stringArray = getResources().getStringArray(R.array.junkmodulesaboven);
            if (this.removeBoost) {
                stringArray = getResources().getStringArray(R.array.junkmodulesabovenlessboost);
            }
        } else {
            stringArray = getResources().getStringArray(R.array.junkmodules);
            if (this.removeBoost) {
                stringArray = getResources().getStringArray(R.array.junkmodulesaboveseven);
            }
        }
        ArrayList arrayList = new ArrayList();
        arrayList.clear();
        Collections.addAll(arrayList, stringArray);
        this.listDataChild = null;
        HashMap<String, ArrayList<JunkListWrapper>> hashMap = new HashMap<>();
        this.listDataChild = hashMap;
        hashMap.clear();
        if (i > 19) {
            this.listDataChild.put((String) arrayList.get(0), this.appCacheList);
            this.listDataChild.put((String) arrayList.get(1), this.tempList);
            this.listDataChild.put((String) arrayList.get(2), this.apkList);
            this.listDataChild.put((String) arrayList.get(3), this.emptyFolderList);
            if (!this.removeBoost) {
                this.listDataChild.put((String) arrayList.get(4), this.boostList);
            }
        } else {
            this.listDataChild.put((String) arrayList.get(0), this.appCacheList);
            this.listDataChild.put((String) arrayList.get(1), this.sysCacheList);
            this.listDataChild.put((String) arrayList.get(2), this.tempList);
            this.listDataChild.put((String) arrayList.get(3), this.apkList);
            this.listDataChild.put((String) arrayList.get(4), this.emptyFolderList);
            if (!this.removeBoost) {
                this.listDataChild.put((String) arrayList.get(5), this.boostList);
            }
        }
        MobiClean.getInstance().junkData = new JunkData(this.listDataChild);
        try {
            ArrayList<JunkListWrapper> arrayList2 = GlobalData.cacheContainingApps;
            if (arrayList2 != null && arrayList2.size() == 0) {
                GlobalData.cacheContainingApps.addAll(this.sysCacheList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.appCacheList = null;
        this.tempList = null;
        this.apkList = null;
        this.emptyFolderList = null;
        this.boostList = null;
        this.sysCacheList = null;
    }


    public void fillLogList() {
        ArrayList<String> arrayList = new ArrayList<>();
        this.logList = arrayList;
        arrayList.add("xlog");
        this.logList.add("log");
        this.logList.add("tmp");
        this.logList.add("dat");
        this.logList.add(DiskLruCache.JOURNAL_FILE);
        this.logList.add("cuid");
        this.logList.add("bat");
        this.logList.add("dk");
        this.logList.add("xml");
        this.logList.add("nomedia");
        this.logList.add("bin");
        this.logList.add("js");
        this.logList.add("css");
        this.logList.add("file");
        this.logList.add("idx");
    }


    public void getFilesFromCacheFolder(String str) {
        File[] listFiles;
        File file = new File(str);
        if (!file.exists() || (listFiles = file.listFiles()) == null || listFiles.length == 0) {
            return;
        }
        for (File file2 : listFiles) {
            if (file2.isDirectory()) {
                getFilesFromCacheFolder(file2.getPath());
            } else {
                this.allcacheUserSize += file2.length();
                this.alluserCachedfiles.add(file2.getPath());
                this.appSpecificCacheList.add(file2.getPath());
                this.appSpecificCacheSize += file2.length();
            }
        }
    }


    public void goBackToHome() {
        finish();
        if (this.fromnoti || this.redirectToHome || this.redirectToNoti) {
            startActivity(new Intent(this.j, HomeActivity.class));
        }
    }

    private void googleTracking(String str, String str2) {
    }

    private boolean isAlreadyBoosted() {
        SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(this);
        return showSavedTemp(sharedPrefUtil.getString(SharedPrefUtil.LASTCOOLTIME), sharedPrefUtil.getString(SharedPrefUtil.LASTBOOSTTIME));
    }

    private void redirectToHome() {
        this.redirectToHome = getIntent().getBooleanExtra(GlobalData.REDIRECTHOME, false);
        this.redirectToNoti = getIntent().getBooleanExtra(GlobalData.REDIRECTNOTI, false);
    }


    public void removeHandler() {
        try {
            this.handlerProgress.sendEmptyMessage(0);
        } catch (Exception unused) {
        }
    }

    private void scanJunkData() {
        this.tv_junk_processing.setText(R.string.mbc_processing);
        this.tv_junk_count.setVisibility(View.INVISIBLE);
        blinkImageView("1");
        final Handler handler = new Handler();
        try {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    JunkScanActivity.this.tempProgress += 3;
                    if (JunkScanActivity.this.arcProgress_outer.getCurrentValue() < 15.0f) {
                        JunkScanActivity.this.arcProgress_outer.setValue(JunkScanActivity.this.tempProgress);
                        JunkScanActivity.this.tv_percentage.setText(String.valueOf(JunkScanActivity.this.tempProgress));
                        handler.postDelayed(this, 2000L);
                    }
                }
            }, 2000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.scanJunkTask = new AnonymousClass7().execute(new String[0]);
    }

    private void setDefaultDimension() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceWidth = displayMetrics.widthPixels;
        try {
            int i = ParentActivity.displaymetrics.heightPixels;
            View view = (View) this.arcProgress_outer.getParent();
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            int i3 = this.deviceWidth;
            layoutParams.width = (i3 * 45) / 100;
            layoutParams.height = (i3 * 45) / 100;
            view.setLayoutParams(layoutParams);
            int i4 = (this.deviceWidth * 2) / 100;
            view.setPadding(i4, i4, i4, i4);
            this.arcProgress_outer.setBarWidth(i4);
            this.arcProgress_outer.setRimWidth(i4);
            this.arcProgress_outer.setRimColor(Color.parseColor("#1c58b1"));
            this.arcProgress_outer.setBarColor(Color.parseColor("#0495ED"));
            this.arcProgress_outer.setTextSize(0);
            this.arcProgress_outer.setTextColor(Color.parseColor("#1c58b1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean showSavedTemp(String str, String str2) {
        if (str == null && str2 == null) {
            return false;
        }
        if (Util.checkTimeDifference("" + System.currentTimeMillis(), str) > GlobalData.coolPause) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(System.currentTimeMillis());
            return Util.checkTimeDifference(sb.toString(), str2) <= ((long) GlobalData.boostPause);
        }
        return true;
    }


    public void showStopWaitdialog() {
        this.isWaitScreenShown = true;
        this.dialogStopWait.setCancelable(false);
        this.dialogStopWait.setCanceledOnTouchOutside(false);
        ProgressDialog progressDialog = this.dialogStopWait;
        progressDialog.setMessage("" + getResources().getString(R.string.mbc_stopping_scan) + ", " + getResources().getString(R.string.mbc_please_wait));
        this.dialogStopWait.show();
        this.dialogStopWait.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
    }


    public void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void trackIfFromNotification() {
        if (getIntent().getBooleanExtra("FROMNOTI", false)) {
            googleTracking("PUSH_NOTI_CLICKED", "" + JunkScanActivity.class.getName());
        }
    }

    private void viewBoundsIds() {
        this.back = (ImageView) findViewById(R.id.iv_back);
        this.tv_percentage = (TextView) findViewById(R.id.tv_per);
        this.tv_symbol = (TextView) findViewById(R.id.tv_sym);
        this.n = (RelativeLayout) findViewById(R.id.layout_one);
        this.m = (RelativeLayout) findViewById(R.id.layout_two);
        this.o = (FrameLayout) findViewById(R.id.frame_mid_laysss);
        this.p = (TextView) findViewById(R.id.ads_btn_countinue);
        this.q = (TextView) findViewById(R.id.ads_btn_cancel);
        this.ads_title = (TextView) findViewById(R.id.dialog_title);
        this.ads_message = (TextView) findViewById(R.id.dialog_msg);
        this.q.setOnClickListener(this);
        this.p.setOnClickListener(this);
        this.arcProgress_outer = (CircleProgressView) findViewById(R.id.circleView);
        this.appcache = (ImageView) findViewById(R.id.iv_app_cache);
        this.tempFile = (ImageView) findViewById(R.id.iv_temp_file);
        this.emptyFolder = (ImageView) findViewById(R.id.iv_empty_folder);
        this.apkFiles = (ImageView) findViewById(R.id.iv_apk_file);
        this.memoryBoost = (ImageView) findViewById(R.id.iv_memory_boost);
        this.tv_sys_cache = (TextView) findViewById(R.id.txt_sys_cache);
        this.tv_unused = (TextView) findViewById(R.id.txt_empty_folder);
        this.tv_apk = (TextView) findViewById(R.id.txt_apk_file);
        this.tv_temp_files = (TextView) findViewById(R.id.txt_temp_file);
        this.tv_boost = (TextView) findViewById(R.id.txt_memory_boost);
        this.tv_junk_count = (TextView) findViewById(R.id.junk_count_text);
        this.tv_junk_processing = (TextView) findViewById(R.id.junk_processing_text);
        this.tv_category_text = (TextView) findViewById(R.id.junk_category_text);
        hiddenPermissionLayout = (RelativeLayout) findViewById(R.id.hiddenpermissionlayout);
        this.imgClose = (ImageView) findViewById(R.id.permission_close_btn);
        this.rl_sys_cache = (RelativeLayout) findViewById(R.id.rl_system_cache);
        this.r1_unused = (RelativeLayout) findViewById(R.id.rl_empty_folder);
        this.r1_apk = (RelativeLayout) findViewById(R.id.rl_apk_file);
        this.r1_temp_files = (RelativeLayout) findViewById(R.id.rl_temp_files);
        this.r1_boost = (RelativeLayout) findViewById(R.id.rl_memory_boost);
        this.j = this;
        this.apkList = new ArrayList<>();
        this.appCacheList = new ArrayList<>();
        this.boostList = new ArrayList<>();
        this.emptyFolderList = new ArrayList<>();
        this.sysCacheList = new ArrayList<>();
        this.tempList = new ArrayList<>();
        this.handlerProgress = new Handler();
        this.dialogStopWait = new ProgressDialog(this.j);
        this.rl_sys_cache.setAlpha(0.3f);
//        this.r1_app_cache.setAlpha(0.3f);
        this.r1_unused.setAlpha(0.3f);
        this.r1_apk.setAlpha(0.3f);
        this.r1_temp_files.setAlpha(0.3f);
        this.r1_boost.setAlpha(0.3f);
    }

    @Override
    public void onBackPressed() {
        if (this.isAborted && this.isWaitScreenShown) {
            Util.appendLogmobiclean(TAG, "junk returning as onBackPressed wait stop already open", GlobalData.FILE_NAME);
            return;
        }
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



                ((ImageView) dialog.findViewById(R.id.dialog_img)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dg_junk_cleaner));
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.mbc_junk_scanning_title));
        if (super.checkStoragePermissions()) {
            ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(String.format(getResources().getString(R.string.mbc_simple_back_press), getString(R.string.mbc_junk_scanning_txt)));
        } else {
            ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(String.format(getResources().getString(R.string.mbc_simple_back_press), ""));
        }
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JunkScanActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JunkScanActivity.this.multipleClicked()) {
                    return;
                }
                dialog.dismiss();
                JunkScanActivity.this.isAborted = true;
                JunkScanActivity.this.removeHandler();
                try {
                    if (JunkScanActivity.this.scanJunkTask != null && JunkScanActivity.this.scanJunkTask.getStatus() == AsyncTask.Status.RUNNING) {
                        JunkScanActivity.this.isAborted = true;
                        JunkScanActivity.this.scanJunkTask.cancel(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (JunkScanActivity.this.scanJunkTask == null || JunkScanActivity.this.scanJunkTask.getStatus() != AsyncTask.Status.RUNNING) {
                    JunkScanActivity.this.goBackToHome();
                } else {
                    JunkScanActivity.this.showStopWaitdialog();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (multipleClicked()) {
            return;
        }
        if (view.getId() == R.id.ads_btn_cancel) {
            try {
                RelativeLayout relativeLayout = this.m;
                if (relativeLayout != null && relativeLayout.getVisibility() == View.VISIBLE) {
                    this.n.setVisibility(View.VISIBLE);
                    this.m.setVisibility(View.GONE);
                    GlobalData.dontAnimate = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (view.getId() == R.id.ads_btn_countinue) {
            this.m.setVisibility(View.GONE);
            GlobalData.dontAnimate = false;
            this.n.setVisibility(View.VISIBLE);
            this.isAborted = true;
            removeHandler();
            try {
                AsyncTask<String, String, String> asyncTask = this.scanJunkTask;
                if (asyncTask != null && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                    this.isAborted = true;
                    this.scanJunkTask.cancel(true);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            AsyncTask<String, String, String> asyncTask2 = this.scanJunkTask;
            if (asyncTask2 != null && asyncTask2.getStatus() == AsyncTask.Status.RUNNING) {
                showStopWaitdialog();
                return;
            }
            finish();
            if (this.redirectToHome || this.redirectToNoti) {
                startActivity(new Intent(this.j, HomeActivity.class));
            }
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 21) {
            getWindow().requestFeature(12);
            getWindow().setAllowReturnTransitionOverlap(true);
        }
        GlobalData.SETAPPLAnguage(this);
        GlobalData.dontAnimate = false;
        getWindow().addFlags(2097280);
        super.onCreate(bundle);
        setContentView(R.layout.activity_junk_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar;
        viewBoundsIds();
        this.isWaitScreenShown = false;
        this.fromnoti = getIntent().getBooleanExtra("FROMNOTI", false);
        redirectToHome();
        if (isAlreadyBoosted()) {
            this.removeBoost = true;
        }
        super.requestAppPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, R.string.mbc_runtime_permissions_txt, 20);
        setDefaultDimension();
        if (super.checkStoragePermissions()) {
            scanJunkData();
        }
        this.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JunkScanActivity.this.finish();
            }
        });
        clearNotification();
        trackIfFromNotification();
        new SharedPrefUtil(this).saveLastTimeUsed(SharedPrefUtil.LUSED_JUNK, System.currentTimeMillis());

        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        showInterstialAds(JunkScanActivity.this);

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
        onBackPressed();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.isInBackground = true;
    }

    @Override
    public void onPermissionsGranted(int i) {
        if (hiddenPermissionLayout != null) {
            viewBoundsIds();
            hiddenPermissionLayout.setVisibility(View.GONE);
            scanJunkData();
        }
    }

    @Override
    public void onResume() {
        RelativeLayout relativeLayout;
        super.onResume();
        if (ParentActivity.displaymetrics == null) {
            ParentActivity.displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(ParentActivity.displaymetrics);
        }
        this.isInBackground = false;
        if (this.v >= 100) {
            Log.e(TAG, "onResume: " + "JunkCleanScreen Goooooooo");

                Intent intent = new Intent(JunkScanActivity.this, JunkCleanScreen.class);
                intent.putExtra("file_processed", l);
                intent.putExtra(GlobalData.REDIRECTNOTI, true);
                Log.e(TAG, "onResume: file processes====" + l);
                Log.e(TAG, "onResume: glpbal redirect" + "true");
                startActivity(intent);
                finish();



        }
        if (super.checkStoragePermissions() && (relativeLayout = hiddenPermissionLayout) != null && relativeLayout.getVisibility() == View.VISIBLE) {
            hiddenPermissionLayout.setVisibility(View.GONE);
            scanJunkData();
        }
    }

    public void loadinit() {
        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(JunkScanActivity.this, "ca-app-pub-3940256099942544/1033173712", adRequest,
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