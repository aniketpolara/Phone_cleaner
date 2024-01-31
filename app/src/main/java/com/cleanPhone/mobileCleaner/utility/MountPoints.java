package com.cleanPhone.mobileCleaner.utility;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.cleanPhone.mobileCleaner.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;


public class MountPoints {
    public static final ArrayList<String> mountPoints(Context context) {
        ArrayList<String> arrayList = new ArrayList<>();
        String string = context.getString(R.string.mbc_app_storage);
        if (MountPoint.getHoneycombSdcard(context) == null && Build.VERSION.SDK_INT <= 22) {
            arrayList.add(string);
        }
        Map<String, MountPoint> mountPoints = MountPoint.getMountPoints(context);
        for (MountPoint mountPoint : mountPoints.values()) {
            arrayList.add(mountPoint.root);
            if (mountPoints.size() == 0) {
                return arrayList;
            }
        }
        if (arrayList.size() == 1 && GlobalData.StringItirator(arrayList.get(0)) > 2) {
            arrayList.add(string);
        }
        return arrayList;
    }

    public static ArrayList<String> returnMountPOints(Context context) {
        String deviceName = GlobalData.getDeviceName();
        ArrayList<String> arrayList = new ArrayList<>();
        int i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            try{
                Iterator<File> it = StorageHelper.getStorages(true).iterator();
                while (it.hasNext()) {
                    arrayList.add(it.next().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return arrayList;
        } else if (i < 21 && !deviceName.contains("Xiaomi") && !deviceName.contains("HUAWEI")) {
            return GlobalData.mountPoints(context);
        } else {
            Boolean valueOf = Boolean.valueOf(Environment.getExternalStorageState().equals("mounted"));
            String str = System.getenv("PRIMARY_STORAGE");
            String str2 = System.getenv("SECONDARY_STORAGE");
            if (str2 == null || str2.length() == 0) {
                str2 = System.getenv("EXTERNAL_SDCARD_STORAGE");
            }
            if (str == null || str.length() == 0) {
                str = System.getenv("EXTERNAL_STORAGE");
            }
            if (str == null && str2 == null) {
                str2 = Environment.getExternalStorageDirectory().toString();
            }
            if (str2 == null) {
                str2 = System.getenv("PHONE_STORAGE");
            }
            arrayList.add(str);
            arrayList.add(str2);
            if (valueOf.booleanValue() && str2 == null) {
                arrayList.clear();
                Iterator<File> it2 = StorageHelper.getStorages(true).iterator();
                ArrayList arrayList2 = new ArrayList();
                while (it2.hasNext()) {
                    String file = it2.next().toString();
                    File file2 = new File(file);
                    if (arrayList2.size() > 0) {
                        if (!arrayList2.contains(Long.valueOf(DiskUtils.getTotalMemorySize(file2)))) {
                            arrayList2.add(Long.valueOf(DiskUtils.getTotalMemorySize(file2)));
                            arrayList.add(file);
                        }
                    } else {
                        arrayList2.add(Long.valueOf(DiskUtils.getTotalMemorySize(file2)));
                        arrayList.add(file);
                    }
                }
                Collections.reverse(arrayList);
                return arrayList;
            }
            return arrayList;
        }
    }
}
