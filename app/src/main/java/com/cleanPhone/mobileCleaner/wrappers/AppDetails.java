package com.cleanPhone.mobileCleaner.wrappers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.ListView;

import com.cleanPhone.mobileCleaner.antimalware.FilesInclude;
import com.cleanPhone.mobileCleaner.utility.GlobalData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AppDetails {
    public Context f5360a;
    public ListView list;
    public ArrayList<PackageInfoStruct> res = new ArrayList<>();

    public AppDetails(Context context) {
        this.f5360a = context;
    }

    public ArrayList<PackageInfoStruct> getAVIgnoredApps(Context context) {
        ArrayList<PackageInfoStruct> allInstalledPkgListDetail = getAllInstalledPkgListDetail(context);
        try {
            ArrayList arrayList = (ArrayList) GlobalData.getObj(context, "av_white");
            if (arrayList != null) {
                for (int i = 0; i < arrayList.size(); i++) {
                    String pkgName = ((FilesInclude) arrayList.get(i)).getPkgName();
                    for (int i2 = 0; i2 < allInstalledPkgListDetail.size(); i2++) {
                        String str = allInstalledPkgListDetail.get(i2).pname;
                        if (str.equalsIgnoreCase("" + pkgName)) {
                            allInstalledPkgListDetail.get(i2).ignored = true;
                        }
                    }
                }
            }
        } catch (Exception unused) {
        }
        return allInstalledPkgListDetail;
    }

    public ArrayList<String> getAllInstalledPkgList() {
        ArrayList<String> arrayList = new ArrayList<>();
        Context context = this.f5360a;
        if (context == null) {
            return arrayList;
        }
        try {
            List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < installedPackages.size(); i++) {
                PackageInfo packageInfo = installedPackages.get(i);
                if (packageInfo.versionName != null) {
                    arrayList.add(packageInfo.packageName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public ArrayList<PackageInfoStruct> getAllInstalledPkgListDetail(Context context) {
        ArrayList<PackageInfoStruct> arrayList = new ArrayList<>();
        Context context2 = this.f5360a;
        if (context2 == null) {
            return arrayList;
        }
        try {
            List<PackageInfo> installedPackages = context2.getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < installedPackages.size(); i++) {
                PackageInfo packageInfo = installedPackages.get(i);
                PackageInfoStruct packageInfoStruct = new PackageInfoStruct();
                if (packageInfo.versionName != null) {
                    String str = packageInfo.packageName;
                    if (!str.equalsIgnoreCase("" + context.getPackageName())) {
                        String charSequence = packageInfo.applicationInfo.loadLabel(this.f5360a.getPackageManager()).toString();
                        packageInfoStruct.appname = charSequence;
                        if (!charSequence.contains(".")) {
                            packageInfoStruct.pname = packageInfo.packageName;
                            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                            packageInfoStruct.datadir = applicationInfo.dataDir;
                            packageInfoStruct.saurcedir = applicationInfo.sourceDir;
                            packageInfoStruct.apkPath = applicationInfo.publicSourceDir;
                            packageInfoStruct.versionName = packageInfo.versionName;
                            packageInfoStruct.versionCode = packageInfo.versionCode;
                            packageInfoStruct.installLocation = packageInfo.installLocation;
                            arrayList.add(packageInfoStruct);
                        }
                    }
                }
            }
            try {
                Collections.sort(arrayList, new Comparator<PackageInfoStruct>() {
                    @Override
                    public int compare(PackageInfoStruct packageInfoStruct2, PackageInfoStruct packageInfoStruct3) {
                        return packageInfoStruct2.appname.compareToIgnoreCase(packageInfoStruct3.appname);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public ArrayList<PackageInfoStruct> getApplicationsInfo(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        intent.addCategory("android.intent.category.LAUNCHER");
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
        try {
            Collections.sort(queryIntentActivities, new ResolveInfo.DisplayNameComparator(packageManager));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            PackageInfoStruct packageInfoStruct = new PackageInfoStruct();
            packageInfoStruct.appname = resolveInfo.activityInfo.applicationInfo.loadLabel(packageManager).toString();
            try {
                packageManager.getApplicationIcon(resolveInfo.activityInfo.packageName);
                String str = resolveInfo.activityInfo.packageName;
                packageInfoStruct.pname = str;
                try {
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (!str.equalsIgnoreCase("com.mobiclean.phoneclean") && !packageInfoStruct.appname.equalsIgnoreCase("Google play services") && !packageInfoStruct.appname.equalsIgnoreCase("Google play store") && !packageInfoStruct.appname.equalsIgnoreCase("Google play music")) {
                    ApplicationInfo applicationInfo = resolveInfo.activityInfo.applicationInfo;
                    packageInfoStruct.datadir = applicationInfo.dataDir;
                    packageInfoStruct.saurcedir = applicationInfo.sourceDir;
                    packageInfoStruct.apkPath = applicationInfo.publicSourceDir;
                    packageInfoStruct.ischecked = true;
                    this.res.add(packageInfoStruct);
                }
            } catch (PackageManager.NameNotFoundException e3) {
                e3.printStackTrace();
            }
        }
        return this.res;
    }

    public ArrayList<PackageInfoStruct> getIgnoredApps(Context context) {
        ArrayList<PackageInfoStruct> allInstalledPkgListDetail = getAllInstalledPkgListDetail(context);
        try {
            ArrayList arrayList = (ArrayList) GlobalData.getObj(context, "ignored_apps");
            if (arrayList != null) {
                for (int i = 0; i < arrayList.size(); i++) {
                    String str = (String) arrayList.get(i);
                    for (int i2 = 0; i2 < allInstalledPkgListDetail.size(); i2++) {
                        String str2 = allInstalledPkgListDetail.get(i2).pname;
                        if (str2.equalsIgnoreCase("" + str)) {
                            allInstalledPkgListDetail.get(i2).ignored = true;
                        }
                    }
                }
            }
        } catch (Exception unused) {
        }
        return allInstalledPkgListDetail;
    }

    public ArrayList<PackageInfoStruct> getInstalledApps() {
        List<PackageInfo> installedPackages = this.f5360a.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo packageInfo = installedPackages.get(i);
            if (packageInfo.versionName != null) {
                PackageInfoStruct packageInfoStruct = new PackageInfoStruct();
                String charSequence = packageInfo.applicationInfo.loadLabel(this.f5360a.getPackageManager()).toString();
                packageInfoStruct.appname = charSequence;
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!charSequence.equalsIgnoreCase("Google play services")) {
                    if (!packageInfoStruct.appname.equalsIgnoreCase("Google play store")) {
                        if (packageInfoStruct.appname.equalsIgnoreCase("APC Rebrand")) {
                        }
                        packageInfoStruct.pname = packageInfo.packageName;
                        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                        packageInfoStruct.datadir = applicationInfo.dataDir;
                        packageInfoStruct.saurcedir = applicationInfo.sourceDir;
                        packageInfoStruct.apkPath = applicationInfo.publicSourceDir;
                        packageInfoStruct.versionName = packageInfo.versionName;
                        packageInfoStruct.versionCode = packageInfo.versionCode;
                        this.res.add(packageInfoStruct);
                    }
                }
            }
        }
        return this.res;
    }

    public ArrayList<PackageInfoStruct> getInstalledAppsWithNames() {
        PackageManager packageManager = this.f5360a.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo packageInfo = installedPackages.get(i);
            if (packageInfo.versionName != null) {
                PackageInfoStruct packageInfoStruct = new PackageInfoStruct();
                packageInfoStruct.appname = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                try {
                    packageManager.getApplicationIcon(packageInfo.packageName);
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!packageInfoStruct.appname.equalsIgnoreCase("Google play services")) {
                        if (packageInfoStruct.appname.equalsIgnoreCase("Google play store")) {
                        }
                        packageInfoStruct.pname = packageInfo.packageName;
                        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                        packageInfoStruct.datadir = applicationInfo.dataDir;
                        packageInfoStruct.saurcedir = applicationInfo.sourceDir;
                        packageInfoStruct.apkPath = applicationInfo.publicSourceDir;
                        packageInfoStruct.versionName = packageInfo.versionName;
                        packageInfoStruct.versionCode = packageInfo.versionCode;
                        this.res.add(packageInfoStruct);
                    }
                } catch (PackageManager.NameNotFoundException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return this.res;
    }

    public ArrayList<PackageInfoStruct> getInstalledGameUserApps() {
        try {
            List<PackageInfo> installedPackages = this.f5360a.getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < installedPackages.size(); i++) {
                PackageInfo packageInfo = installedPackages.get(i);
                PackageInfoStruct packageInfoStruct = new PackageInfoStruct();
                if (!packageInfo.applicationInfo.sourceDir.startsWith("/data/app/")) {
                    Log.d("Systemmm ", "Systemm");
                } else {
                    Log.d("Systemmm ", "no system");
                    if (packageInfo.versionName != null) {
                        String charSequence = packageInfo.applicationInfo.loadLabel(this.f5360a.getPackageManager()).toString();
                        packageInfoStruct.appname = charSequence;
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (!charSequence.equalsIgnoreCase("Google play services")) {
                            if (!packageInfoStruct.appname.equalsIgnoreCase("Google play store")) {
                                if (!packageInfoStruct.appname.equalsIgnoreCase("APC Rebrand")) {
                                    if (packageInfoStruct.appname.equals("APC Rebrand")) {
                                    }
                                    packageInfoStruct.pname = packageInfo.packageName;
                                    try {
                                        packageInfoStruct.appsize = this.f5360a.getPackageManager().getApplicationInfo(packageInfoStruct.pname, 0).publicSourceDir.length();
                                    } catch (PackageManager.NameNotFoundException e2) {
                                        e2.printStackTrace();
                                    }
                                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                                    packageInfoStruct.datadir = applicationInfo.dataDir;
                                    packageInfoStruct.saurcedir = applicationInfo.sourceDir;
                                    packageInfoStruct.apkPath = applicationInfo.publicSourceDir;
                                    packageInfoStruct.versionName = packageInfo.versionName;
                                    packageInfoStruct.versionCode = packageInfo.versionCode;
                                    packageInfoStruct.ischecked = true;
                                    this.res.add(packageInfoStruct);
                                }
                            }
                        }
                    }
                }
            }
            Log.d("------ ", "SIZE OF RES " + this.res.size());
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return this.res;
    }

    public ArrayList<String> getInstalledPackeageNames() {
        List<PackageInfo> installedPackages = this.f5360a.getPackageManager().getInstalledPackages(0);
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo packageInfo = installedPackages.get(i);
            if (packageInfo.versionName != null) {
                arrayList.add(packageInfo.packageName);
            }
        }
        return arrayList;
    }

    public ArrayList<PackageInfoStruct> getInstalledSystemApps() {
        try {
            List<PackageInfo> installedPackages = this.f5360a.getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < installedPackages.size(); i++) {
                PackageInfo packageInfo = installedPackages.get(i);
                if (packageInfo.versionName != null) {
                    PackageInfoStruct packageInfoStruct = new PackageInfoStruct();
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    if ((applicationInfo.flags & 1) != 0) {
                        packageInfoStruct.appname = applicationInfo.loadLabel(this.f5360a.getPackageManager()).toString();
                        String str = packageInfo.packageName;
                        packageInfoStruct.pname = str;
                        ApplicationInfo applicationInfo2 = packageInfo.applicationInfo;
                        packageInfoStruct.datadir = applicationInfo2.dataDir;
                        packageInfoStruct.saurcedir = applicationInfo2.sourceDir;
                        packageInfoStruct.apkPath = applicationInfo2.publicSourceDir;
                        packageInfoStruct.versionName = packageInfo.versionName;
                        packageInfoStruct.versionCode = packageInfo.versionCode;
                        packageInfoStruct.installLocation = packageInfo.installLocation;
                        if (!str.equalsIgnoreCase("com.mobiclean.phoneclean")) {
                            this.res.add(packageInfoStruct);
                            Log.d("PackageInfoStruct", packageInfoStruct.appname + "  " + packageInfoStruct.pname);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.res;
    }

    public ArrayList<PackageInfoStruct> getInstalledUserApps() {
        try {
            List<PackageInfo> installedPackages = this.f5360a.getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < installedPackages.size(); i++) {
                PackageInfo packageInfo = installedPackages.get(i);
                if (packageInfo.versionName != null) {
                    PackageInfoStruct packageInfoStruct = new PackageInfoStruct();
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    if ((applicationInfo.flags & 1) == 0) {
                        String charSequence = applicationInfo.loadLabel(this.f5360a.getPackageManager()).toString();
                        packageInfoStruct.appname = charSequence;
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (!charSequence.equalsIgnoreCase("Google play services")) {
                            if (packageInfoStruct.appname.equalsIgnoreCase("Google play store")) {
                            }
                            String str = packageInfo.packageName;
                            packageInfoStruct.pname = str;
                            ApplicationInfo applicationInfo2 = packageInfo.applicationInfo;
                            packageInfoStruct.datadir = applicationInfo2.dataDir;
                            packageInfoStruct.saurcedir = applicationInfo2.sourceDir;
                            packageInfoStruct.apkPath = applicationInfo2.publicSourceDir;
                            packageInfoStruct.versionName = packageInfo.versionName;
                            packageInfoStruct.versionCode = packageInfo.versionCode;
                            packageInfoStruct.installLocation = packageInfo.installLocation;
                            if (!str.equalsIgnoreCase("com.mobiclean.phoneclean") && !packageInfoStruct.pname.equalsIgnoreCase("com.domobile.applock")) {
                                this.res.add(packageInfoStruct);
                            }
                        }
                    }
                }
            }
            try {
                Collections.sort(this.res, new Comparator<PackageInfoStruct>() {
                    @Override
                    public int compare(PackageInfoStruct packageInfoStruct2, PackageInfoStruct packageInfoStruct3) {
                        return packageInfoStruct2.appname.compareToIgnoreCase(packageInfoStruct3.appname);
                    }
                });
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return this.res;
    }

    public ArrayList<String> getInstalledUserAppsPkgs() {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            List<PackageInfo> installedPackages = this.f5360a.getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < installedPackages.size(); i++) {
                PackageInfo packageInfo = installedPackages.get(i);
                if (packageInfo.versionName != null) {
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    if ((applicationInfo.flags & 1) == 0) {
                        String charSequence = applicationInfo.loadLabel(this.f5360a.getPackageManager()).toString();
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (!charSequence.equalsIgnoreCase("Google play services")) {
                            if (charSequence.equalsIgnoreCase("Google play store")) {
                            }
                            String str = packageInfo.packageName;
                            if (!str.equalsIgnoreCase("com.mobiclean.phoneclean")) {
                                arrayList.add(str);
                            }
                        }
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }
}
