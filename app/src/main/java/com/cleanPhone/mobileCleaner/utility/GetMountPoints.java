package com.cleanPhone.mobileCleaner.utility;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class GetMountPoints {
    private Context cntx;

    public GetMountPoints(Context context) {
        this.cntx = context;
    }

    public ArrayList<File> returnAllMountPoints() {
        File[] obbDirs;
        ArrayList<File> arrayList = new ArrayList<>();
        if (Build.MODEL.equals("Micromax Q340")) {
            arrayList.add(new File("/mnt/sdcard"));
            arrayList.add(new File("/mnt/sdcard2"));
        } else if (Build.VERSION.SDK_INT >= 23) {
            for (File file : this.cntx.getObbDirs()) {
                if (file != null && file.exists()) {
                    arrayList.add(new File(file.getAbsolutePath().split("Android")[0]));
                }
            }
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
            if (str != null) {
                arrayList.add(new File(str));
            }
            if (str2 != null) {
                arrayList.add(new File(str2));
            }
            if (valueOf.booleanValue() && str2 == null) {
                arrayList.clear();
                Iterator<File> it = StorageHelper.getStorages(true).iterator();
                ArrayList arrayList2 = new ArrayList();
                while (it.hasNext()) {
                    String file2 = it.next().toString();
                    File file3 = new File(file2);
                    if (arrayList2.size() > 0) {
                        if (!arrayList2.contains(Long.valueOf(DiskUtils.getTotalMemorySize(file3)))) {
                            arrayList2.add(Long.valueOf(DiskUtils.getTotalMemorySize(file3)));
                            arrayList.add(new File(file2));
                        }
                    } else {
                        arrayList2.add(Long.valueOf(DiskUtils.getTotalMemorySize(file3)));
                        arrayList.add(new File(file2));
                    }
                }
            }
        }
        return arrayList;
    }
}
