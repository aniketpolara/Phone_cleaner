package com.cleanPhone.mobileCleaner.wrappers;

import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.cleanPhone.mobileCleaner.utility.GlobalData;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public class AppManagerData {
    private String lastpname;
    public int totSelectedForBackup;
    private volatile boolean lastPackagefound = false;
    public long totsize = 0;
    public long totalBackUp = 0;
    public ArrayList<PackageInfoStruct> installedApps = new ArrayList<>();
    public ArrayList<RestoreWrapper> restoreData = new ArrayList<>();


    public class cachePackState extends IPackageStatsObserver.Stub {
        private cachePackState() {
        }

        private void fillCacheSizes(PackageStats packageStats) {
            int i = 0;
            while (true) {
                if (i >= AppManagerData.this.installedApps.size()) {
                    break;
                }
                if (AppManagerData.this.installedApps.get(i).pname.equalsIgnoreCase("" + packageStats.packageName)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        long j = packageStats.externalCacheSize;
                        AppManagerData.this.installedApps.get(i).movesize = j + j;
                        AppManagerData.this.installedApps.get(i).cachesize = packageStats.cacheSize + packageStats.externalCacheSize;
                    } else {
                        AppManagerData.this.installedApps.get(i).movesize = packageStats.codeSize;
                        AppManagerData.this.installedApps.get(i).appsize = packageStats.cacheSize + packageStats.externalCacheSize;
                    }
                    AppManagerData.this.installedApps.get(i).dataSize = packageStats.dataSize;
                    AppManagerData.this.installedApps.get(i).appsize = packageStats.cacheSize + packageStats.externalCacheSize + packageStats.dataSize + packageStats.externalDataSize + packageStats.codeSize;
                    AppManagerData appManagerData = AppManagerData.this;
                    appManagerData.totsize += appManagerData.installedApps.get(i).appsize;
                    AppManagerData.this.installedApps.get(i).apksize = packageStats.codeSize;
                } else {
                    i++;
                }
            }
            if (packageStats.packageName.equalsIgnoreCase("" + AppManagerData.this.lastpname)) {
                AppManagerData.this.lastPackagefound = true;
            }
        }

        @Override
        public void onGetStatsCompleted(PackageStats packageStats, boolean z) throws RemoteException {
            fillCacheSizes(packageStats);
        }
    }

    private boolean exists(String str) {
        for (int i = 0; i < this.restoreData.size(); i++) {
            String str2 = this.restoreData.get(i).packagename;
            if (str2.equalsIgnoreCase(str + "")) {
                return true;
            }
        }
        return false;
    }

    public void addToBackedUp(PackageInfoStruct packageInfoStruct) {
        try {
            if (exists(packageInfoStruct.pname)) {
                return;
            }
            RestoreWrapper restoreWrapper = new RestoreWrapper();
            restoreWrapper.packagename = packageInfoStruct.pname;
            restoreWrapper.size = packageInfoStruct.apksize;
            restoreWrapper.appname = packageInfoStruct.appname;
            restoreWrapper.path = packageInfoStruct.apkPath;
            this.restoreData.add(restoreWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkInstalled(PackageInfoStruct packageInfoStruct) {
        packageInfoStruct.ischecked = true;
        this.totSelectedForBackup++;
    }

    public void checkRestored() {
    }

    public void deleteRestoredApp(String str) {
        for (int i = 0; i < this.restoreData.size(); i++) {
            String str2 = this.restoreData.get(i).packagename;
            if (str2.equalsIgnoreCase(str + "")) {
                this.restoreData.remove(i);
                return;
            }
        }
    }

    public void getInstalledAppsData(Context context) {
        this.installedApps = new AppDetails(context).getInstalledUserApps();
        if (Build.VERSION.SDK_INT <= 25) {
            PackageManager packageManager = context.getPackageManager();
            for (int i = 0; i < this.installedApps.size(); i++) {
                try {
                    ArrayList<PackageInfoStruct> arrayList = this.installedApps;
                    this.lastpname = arrayList.get(arrayList.size() - 1).pname;
                    packageManager.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class).invoke(packageManager, this.installedApps.get(i).pname, new cachePackState());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                } catch (NoSuchMethodException e3) {
                    e3.printStackTrace();
                } catch (SecurityException e4) {
                    e4.printStackTrace();
                } catch (InvocationTargetException e5) {
                    e5.printStackTrace();
                }
            }
            do {
            } while (!this.lastPackagefound);
            return;
        }
        for (int i2 = 0; i2 < this.installedApps.size(); i2++) {
            try {
                File file = new File(this.installedApps.get(i2).apkPath);
                this.installedApps.get(i2).appsize = file.length();
                this.installedApps.get(i2).apksize = file.length();
                this.totsize += this.installedApps.get(i2).appsize;
            } catch (Exception e6) {
                e6.printStackTrace();
                return;
            }
        }
    }

    public void getRestoreData(Context context) {
        File[] listFiles;
        this.restoreData.clear();
        PackageManager packageManager = context.getPackageManager();
        File file = new File(Environment.getExternalStorageDirectory() + GlobalData.backuppath);
        if (!file.exists() || (listFiles = file.listFiles()) == null || listFiles.length == 0) {
            return;
        }
        for (File file2 : listFiles) {
            String extension = FilenameUtils.getExtension(file2.getPath());
            if (file2.isFile() && extension.equalsIgnoreCase("apk")) {
                RestoreWrapper restoreWrapper = new RestoreWrapper();
                try {
                    PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo("" + file2.getPath(), 0);
                    restoreWrapper.packagename = packageArchiveInfo.packageName;
                    restoreWrapper.size = file2.length();
                    restoreWrapper.appname = packageArchiveInfo.applicationInfo.loadLabel(packageManager).toString();
                    restoreWrapper.path = file2.getPath();
                    this.restoreData.add(restoreWrapper);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeFromInstalled(String str) {
        if (this.installedApps != null) {
            for (int i = 0; i < this.installedApps.size(); i++) {
                if (this.installedApps.get(i).pname.equalsIgnoreCase(str + "")) {
                    this.totsize -= this.installedApps.get(i).appsize;
                    this.installedApps.remove(i);
                    return;
                }
            }
        }
    }

    public void unCheckInstalled(PackageInfoStruct packageInfoStruct) {
        packageInfoStruct.ischecked = false;
        this.totSelectedForBackup--;
    }

    public void unCheckRestored() {
    }

    public void uncheckAllBackedUp() {
        if (this.restoreData == null) {
            return;
        }
        for (int i = 0; i < this.restoreData.size(); i++) {
            this.restoreData.get(i).ischecked = false;
        }
    }

    public void uncheckAllInstalled() {
        if (this.installedApps == null) {
            return;
        }
        for (int i = 0; i < this.installedApps.size(); i++) {
            this.installedApps.get(i).ischecked = false;
        }
    }
}
