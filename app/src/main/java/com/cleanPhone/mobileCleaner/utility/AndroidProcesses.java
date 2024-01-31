package com.cleanPhone.mobileCleaner.utility;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.cleanPhone.mobileCleaner.processes.AndroidAppProcess;
import com.cleanPhone.mobileCleaner.processes.AndroidProcess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AndroidProcesses {
    private static final int AID_READPROC = 3009;
    public static final String TAG = "AndroidProcesses";
    private static boolean loggingEnabled;

    public static final class ProcessComparator implements Comparator<AndroidProcess> {
        @Override
        public int compare(AndroidProcess androidProcess, AndroidProcess androidProcess2) {
            return androidProcess.name.compareToIgnoreCase(androidProcess2.name);
        }
    }

    public AndroidProcesses() {
        throw new AssertionError("no instances");
    }

    public static List<ActivityManager.RunningAppProcessInfo> getRunningAppProcessInfo(Context context) {
        if (Build.VERSION.SDK_INT >= 22) {
            List<AndroidAppProcess> runningAppProcesses = getRunningAppProcesses();
            ArrayList arrayList = new ArrayList();
            for (AndroidAppProcess androidAppProcess : runningAppProcesses) {
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo(androidAppProcess.name, androidAppProcess.pid, null);
                runningAppProcessInfo.uid = androidAppProcess.uid;
                arrayList.add(runningAppProcessInfo);
            }
            return arrayList;
        }
        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
    }

    public static List<AndroidAppProcess> getRunningAppProcesses() {
        File[] listFiles;
        ArrayList arrayList = new ArrayList();
        for (File file : new File("/proc").listFiles()) {
            if (file.isDirectory()) {
                try {
                    int parseInt = Integer.parseInt(file.getName());
                    try {
                        arrayList.add(new AndroidAppProcess(parseInt));
                    } catch (IOException e) {
                        log(e, "Error reading from /proc/%d.", Integer.valueOf(parseInt));
                    }
                } catch (AndroidAppProcess.NotAndroidAppProcessException | NumberFormatException unused) {
                }
            }
        }
        return arrayList;
    }


    public static void log(String str, Object... objArr) {
        if (loggingEnabled) {
            if (objArr.length != 0) {
                str = String.format(str, objArr);
            }
            Log.d(TAG, str);
        }
    }


    public static void log(Throwable th, String str, Object... objArr) {
        if (loggingEnabled) {
            if (objArr.length != 0) {
                str = String.format(str, objArr);
            }
            Log.d(TAG, str, th);
        }
    }
}
