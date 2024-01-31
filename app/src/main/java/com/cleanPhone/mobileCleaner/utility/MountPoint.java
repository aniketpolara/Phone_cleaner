package com.cleanPhone.mobileCleaner.utility;

import android.content.Context;
import android.os.StatFs;
import android.util.Log;

import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MountPoint {
    private static MountPoint defaultStorage;
    private static MountPoint honeycombSdcard;
    public String fsType;
    public boolean hasApps2SD;
    public String root;
    public boolean rootRequired;
    public String title;
    private static Map<String, MountPoint> mountPoints = new TreeMap();
    private static Map<String, MountPoint> rootedMountPoints = new TreeMap();
    public static boolean init = false;

    public static int f5309a = 0;

    public MountPoint(String str, String str2, boolean z, boolean z2, String str3) {
        this.title = str;
        this.root = str2;
        this.hasApps2SD = z;
        this.rootRequired = z2;
        this.fsType = str3;
    }

    public static MountPoint forPath(Context context, String str) {
        Log.d("diskusage", "Looking for mount point for path: " + str);
        initMountPoints(context);
        String withSlash = GlobalData.withSlash(str);
        MountPoint mountPoint = null;
        for (MountPoint mountPoint2 : mountPoints.values()) {
            if (withSlash.contains(GlobalData.withSlash(mountPoint2.root)) && (mountPoint == null || mountPoint.root.length() < mountPoint2.root.length())) {
                Log.d("diskusage", "MATCH:" + mountPoint2.root);
                mountPoint = mountPoint2;
            }
        }
        for (MountPoint mountPoint3 : rootedMountPoints.values()) {
            if (withSlash.contains(GlobalData.withSlash(mountPoint3.root)) && (mountPoint == null || mountPoint.root.length() < mountPoint3.root.length())) {
                Log.d("diskusage", "MATCH:" + mountPoint3.root);
                mountPoint = mountPoint3;
            }
        }
        if (mountPoint == null) {
            Log.d("diskusage", "Use honeycomb hack for /data");
            return mountPoints.get("/data");
        }
        return mountPoint;
    }



    public static MountPoint getHoneycombSdcard(Context context) {
        initMountPoints(context);
        return honeycombSdcard;
    }

    public static Map<String, MountPoint> getMountPoints(Context context) {
        initMountPoints(context);
        return mountPoints;
    }


    private static void initMountPoints(Context context) {
        MountPoint mountPoint = null;
        if (init) {
            return;
        }
        init = true;
        String storageCardPath = GlobalData.storageCardPath();
        Log.d("diskusage", "StoragePath: " + storageCardPath);
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        if (storageCardPath != null) {
            MountPoint mountPoint2 = new MountPoint(titleStorageCard(context), storageCardPath, false, false, "");
            defaultStorage = mountPoint2;
            arrayList.add(mountPoint2);
            mountPoints.put(storageCardPath, defaultStorage);
        }
        try {
            f5309a = 0;
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/mounts"));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                f5309a += readLine.length();
                Log.d("diskusage", "line: " + readLine);
                String[] split = readLine.split(" +");
                if (split.length >= 3) {
                    String str = split[1];
                    Log.d("diskusage", "Mount point: " + str);
                    String str2 = split[2];
                    StatFs statFs = null;
                    try {
                        statFs = new StatFs(str);
                    } catch (Exception unused) {
                    }
                    if ((str2.equals("vfat") || str2.equals("tntfs") || str2.equals("exfat") || str2.equals("texfat") || GlobalData.isEmulated(str2)) && !str.startsWith("/mnt/asec") && !str.startsWith("/firmware") && !str.startsWith("/mnt/secure") && !str.startsWith("/data/mac") && !str.startsWith("/mnt/knox") && statFs != null && (!str.endsWith("/legacy") || !GlobalData.isEmulated(str2))) {
                        Log.d("diskusage", "Mount point is good");
                        arrayList.add(new MountPoint(str, str, false, false, str2));
                    }
                    Log.d("diskusage", String.format("Excluded based on fsType=%s or black list", str2));
                    hashSet.add(str);
                    if (str.equals(storageCardPath)) {
                        arrayList.remove(defaultStorage);
                        mountPoints.remove(str);
                    }
                    if (!str.startsWith("/mnt/asec/")) {
                        arrayList.add(new MountPoint(str, str, false, true, str2));
                    }
                }
            }
            bufferedReader.close();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                MountPoint mountPoint3 = (MountPoint) it.next();
                String str3 = mountPoint3.root + DialogConfigs.DIRECTORY_SEPERATOR;
                ArrayList arrayList2 = new ArrayList();
                String name = new File(mountPoint3.root).getName();
                Iterator it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    if (((MountPoint) it2.next()).root.startsWith(str3)) {
                        arrayList2.add(name + DialogConfigs.DIRECTORY_SEPERATOR + mountPoint.root.substring(str3.length()));
                    }
                }
                Iterator it3 = hashSet.iterator();
                boolean z = false;
                while (it3.hasNext()) {
                    String str4 = (String) it3.next();
                    if (str4.equals(str3 + ".android_secure")) {
                        z = true;
                    }
                    if (str4.startsWith(str3)) {
                        arrayList2.add(name + DialogConfigs.DIRECTORY_SEPERATOR + str4.substring(str3.length()));
                    }
                }
                String str5 = mountPoint3.root;
                MountPoint mountPoint4 = new MountPoint(str5, str5, z, mountPoint3.rootRequired, mountPoint3.fsType);
                if (mountPoint3.rootRequired) {
                    rootedMountPoints.put(mountPoint3.root, mountPoint4);
                } else {
                    mountPoints.put(mountPoint3.root, mountPoint4);
                }
            }
        } catch (Exception e) {
            Log.e("diskusage", "Failed to get mount points", e);
        }
    }

    private static String titleStorageCard(Context context) {
        return context.getString(R.string.mbc_storage_card);
    }

    public String getRoot() {
        return this.root;
    }
}
