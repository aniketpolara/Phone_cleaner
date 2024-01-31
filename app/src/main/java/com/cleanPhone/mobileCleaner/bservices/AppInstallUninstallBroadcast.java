package com.cleanPhone.mobileCleaner.bservices;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.alramsservices.AlarmNotiService;
import com.cleanPhone.mobileCleaner.antimalware.ResActivity;
import com.cleanPhone.mobileCleaner.antimalware.ScanService;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.ShowNotification;
import com.cleanPhone.mobileCleaner.utility.WriteStatus;
import com.cleanPhone.mobileCleaner.wrappers.ApkDeleteWrapper;
import com.cleanPhone.mobileCleaner.wrappers.AppDetails;
import com.cleanPhone.mobileCleaner.wrappers.RestoreWrapper;

import java.io.File;
import java.util.ArrayList;

public class AppInstallUninstallBroadcast extends BroadcastReceiver {
    public static final int BACKGROUND_NOTI = 411;
    private static final int REQCODE_AV = 603;
    public static final int UNKNOWN_CODE = 342;
    public static ArrayList<ApkDeleteWrapper> unUsedApks = new ArrayList<>();
    public Context f4856a;
    private long allcacheUserSize;
    private ArrayList<String> alluserCachedfiles;
    private String appLabel;
    private ArrayList<ApplicationInfo> appinfolist;
    private String applicationName;
    private CharSequence appname;
    private String appname_install;
    private long appsize;
    public long f4857c;
    private ArrayList<File> deletefilelist;
    private Dialog dialog;
    private Drawable icon;
    private ArrayList<String> logList;
    private boolean otherfilesExists;
    private PackageManager pm;
    private double scale;
    private SharedPrefUtil sharedPrefUtil;
    private String title;
    private boolean updated;
    private String packname = "";
    private String[] whiteListapps = {"FreeUpSpace", "Resize Photos", "Duplicate Photos Cleaner", "BullGuard Mobile Security"};
    private int battery_limit = 15;
    private Handler handler = new Handler();
    public ArrayList<ApkDeleteWrapper> b = new ArrayList<>();

    private boolean checkfromBackup(String str) {
        File[] listFiles;
        PackageManager packageManager = this.f4856a.getPackageManager();
        File file = new File(str);
        if (file.exists() && (listFiles = file.listFiles()) != null && listFiles.length != 0) {
            for (File file2 : listFiles) {
                String extension = FilenameUtils.getExtension(file2.getPath());
                if (file2.isFile() && extension.equalsIgnoreCase("apk")) {
                    new RestoreWrapper();
                    try {
                        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo("" + file2.getPath(), 0);
                        if (this.packname.equalsIgnoreCase("" + packageArchiveInfo.packageName)) {
                            return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    private boolean doesntContain(ArrayList<ApkDeleteWrapper> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            String str = arrayList.get(i).pkg;
            if (str.equalsIgnoreCase("" + this.packname)) {
                return false;
            }
        }
        return true;
    }

    private void getApkCount(String str) {
        File[] listFiles = new File(str).listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    getApkCount(file.getPath());
                } else if (file.isFile() && file.getName().endsWith(".apk")) {
                    PackageInfo packageArchiveInfo = this.pm.getPackageArchiveInfo("" + file.getPath(), 0);
                    if (packageArchiveInfo != null) {
                        String str2 = packageArchiveInfo.applicationInfo.packageName;
                        if (!TextUtils.isEmpty(str2)) {
                            ApkDeleteWrapper apkDeleteWrapper = new ApkDeleteWrapper();
                            apkDeleteWrapper.path = file.getPath();
                            apkDeleteWrapper.size = file.length();
                            apkDeleteWrapper.appname = getAppname(str2);
                            apkDeleteWrapper.pkg = str2;
                            this.b.add(apkDeleteWrapper);
                        }
                    }
                }
            }
        }
    }

    private String getApks() {
        this.f4857c = 0L;
        String str = "";
        for (int i = 0; i < unUsedApks.size(); i++) {
            this.f4857c += unUsedApks.get(i).size;
            str = i == 0 ? str + unUsedApks.get(i).appname : str + "," + unUsedApks.get(i).appname;
        }
        return str;
    }

    private String getAppname(String str) {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = this.f4856a.getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            applicationInfo = null;
        }
        if (applicationInfo != null) {
            try {
                return "" + ((Object) applicationInfo.loadLabel(packageManager));
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }



    public void scanApp() {
        @SuppressLint("StaticFieldLeak") ScanService scanService = new ScanService(this.f4856a) {
            @SuppressLint({"WrongConstant", "StaticFieldLeak"})
            @Override
            public void onPostExecute(ArrayList arrayList) {
                PendingIntent activity;
                PendingIntent activity2;
                super.onPostExecute( arrayList);
                try {
                    if (arrayList.size() <= 0) {
                        Intent launchIntentForPackage = AppInstallUninstallBroadcast.this.pm.getLaunchIntentForPackage(AppInstallUninstallBroadcast.this.packname);
                        launchIntentForPackage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (Build.VERSION.SDK_INT >= 23) {
                            activity = PendingIntent.getActivity(AppInstallUninstallBroadcast.this.f4856a, 0, launchIntentForPackage, 201326592);
                        } else {
                            activity = PendingIntent.getActivity(AppInstallUninstallBroadcast.this.f4856a, 0, launchIntentForPackage, PendingIntent.FLAG_UPDATE_CURRENT);
                        }
                        PendingIntent pendingIntent = activity;
                        ShowNotification showNotification = new ShowNotification();
                        Context context = AppInstallUninstallBroadcast.this.f4856a;
                        showNotification.showNotificationWithOutButton(context, pendingIntent, AppInstallUninstallBroadcast.this.applicationName + " " + AppInstallUninstallBroadcast.this.f4856a.getString(R.string.mbc_av_noti_down_two), "", AppInstallUninstallBroadcast.this.f4856a.getString(R.string.mbc_tap_fix), AppInstallUninstallBroadcast.REQCODE_AV);
                        return;
                    }
                    Intent intent = new Intent(AppInstallUninstallBroadcast.this.f4856a, ResActivity.class);
                    intent.putExtra("results", MobiClean.getInstance().resultMap);
                    MobiClean.getInstance().resultMap.put(AppInstallUninstallBroadcast.this.f4856a.getString(R.string.mbc_infected_apps), arrayList);
                    try {
                        GlobalData.saveObj(AppInstallUninstallBroadcast.this.f4856a, "infected_list", MobiClean.getInstance().resultMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    intent.addFlags(268435456);
                    if (Build.VERSION.SDK_INT >= 23) {
                        activity2 = PendingIntent.getActivity(AppInstallUninstallBroadcast.this.f4856a, 0, intent, 201326592);
                    } else {
                        activity2 = PendingIntent.getActivity(AppInstallUninstallBroadcast.this.f4856a, 0, intent, 134217728);
                    }
                    PendingIntent pendingIntent2 = activity2;
                    ShowNotification showNotification2 = new ShowNotification();
                    Context context2 = AppInstallUninstallBroadcast.this.f4856a;
                    showNotification2.showNotificationWithButton(context2, pendingIntent2, AppInstallUninstallBroadcast.this.applicationName + " " + AppInstallUninstallBroadcast.this.f4856a.getString(R.string.mbc_av_noti_down_one), "", AppInstallUninstallBroadcast.this.f4856a.getString(R.string.mbc_tap_fix), AppInstallUninstallBroadcast.REQCODE_AV);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        };
        scanService.execute("" + this.packname);
    }

    private void searchForAllApks() {
        ArrayList arrayList;
        getApkCount(Environment.getExternalStorageDirectory().getPath());
        try {
            arrayList = (ArrayList) GlobalData.getObj(this.f4856a, "apktodelete");
        } catch (Exception e) {
            ArrayList arrayList2 = new ArrayList();
            e.printStackTrace();
            arrayList = arrayList2;
        }
        ArrayList<String> installedUserAppsPkgs = new AppDetails(this.f4856a).getInstalledUserAppsPkgs();
        for (int i = 0; i < installedUserAppsPkgs.size(); i++) {
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                if (new File(((ApkDeleteWrapper) arrayList.get(i2)).path).exists()) {
                    String str = ((ApkDeleteWrapper) arrayList.get(i2)).pkg;
                    if (str.equalsIgnoreCase("" + installedUserAppsPkgs.get(i))) {
                        unUsedApks.add((ApkDeleteWrapper) arrayList.get(i2));
                    }
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void showNotification(PendingIntent pendingIntent, String str, String str2, int i) {
        PendingIntent broadcast;
        Notification build;
        RemoteViews remoteViews = new RemoteViews(this.f4856a.getPackageName(), (int) R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.upper_text, str);
        remoteViews.setTextViewText(R.id.bottom_text, str2);
        remoteViews.setViewVisibility(R.id.btn_ok, View.VISIBLE);
        remoteViews.setOnClickPendingIntent(R.id.btn_ok, pendingIntent);
        Intent intent = new Intent(GlobalData.INTENT_FILTER_NOTI_CANCEL);
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 23) {
            broadcast = PendingIntent.getBroadcast(this.f4856a, 3767, intent, 1140850688);
        } else {
            broadcast = PendingIntent.getBroadcast(this.f4856a, 3767, intent, BasicMeasure.EXACTLY);
        }
        remoteViews.setOnClickPendingIntent(R.id.btn_cancel, broadcast);
        if (i2 > 19) {
            build = new Notification.Builder(this.f4856a).setAutoCancel(true).setSmallIcon(R.drawable.cum_small_icon).setContentIntent(pendingIntent).setContentTitle(str).setContentText(str2).addAction(0, "", broadcast).addAction(0, "", pendingIntent).setColor(Color.parseColor("#ffffff")).build();
        } else {
            build = new Notification.Builder(this.f4856a).setAutoCancel(true).setSmallIcon(R.drawable.cum_small_icon).setContentIntent(pendingIntent).setContentTitle(str).setContentText(str2).addAction(0, "", broadcast).build();
        }
        NotificationManager notificationManager = (NotificationManager) this.f4856a.getSystemService("notification");
        int i3 = build.defaults | 1;
        build.defaults = i3;
        int i4 = i3 | 4;
        build.defaults = i4;
        build.defaults = i4 | 2;
        build.flags |= 16;
        build.bigContentView = remoteViews;
        if (notificationManager != null) {
            notificationManager.notify(i, build);
        }
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int i;
        this.f4856a = context;
        this.pm = context.getPackageManager();
        this.alluserCachedfiles = new ArrayList<>();
        this.allcacheUserSize = 0L;
        this.otherfilesExists = false;
        this.updated = false;
        this.sharedPrefUtil = new SharedPrefUtil(context);
        new AlarmNotiService().checkAlarms(context);
        try {
            Intent registerReceiver = context.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            if (registerReceiver != null) {
                i = registerReceiver.getIntExtra(FirebaseAnalytics.Param.LEVEL, -1);
                this.scale = registerReceiver.getIntExtra("scale", -1);
            } else {
                i = 0;
            }
            if (i >= 0) {
                int i2 = (this.scale > FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE ? 1 : (this.scale == FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE ? 0 : -1));
            }
        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }

        if (intent == null || intent.getAction() == null || TextUtils.isEmpty(intent.getAction())) {
            return;
        }
        if (!intent.getAction().equals("android.intent.action.PACKAGE_REPLACED") && intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
            this.updated = true;
        }

        String action = intent.getAction();
        action.hashCode();
        if (action.equals("android.intent.action.PACKAGE_REPLACED")) {
            MobiClean.getInstance().isUpdate = true;
        } else if (action.equals("android.intent.action.PACKAGE_ADDED")) {
            MobiClean.getInstance().isPackageAdded = true;

            if (this.sharedPrefUtil.isRealTimeProtectionEnabled()) {
                this.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ApplicationInfo applicationInfo;
                        try {
                            PackageManager packageManager = context.getPackageManager();
                            AppInstallUninstallBroadcast.this.packname = intent.getData().getEncodedSchemeSpecificPart();
                            try {
                                applicationInfo = packageManager.getApplicationInfo(AppInstallUninstallBroadcast.this.packname, 0);
                            } catch (PackageManager.NameNotFoundException unused) {
                                applicationInfo = null;
                            }
                            AppInstallUninstallBroadcast.this.applicationName = (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "app");
                            AppInstallUninstallBroadcast.this.scanApp();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }, 5000L);
            }
        }
        try {
            WriteStatus.sendDetail(context, false);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
