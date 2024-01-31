package com.cleanPhone.mobileCleaner.utility;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.PrintStream;

public class DiskUtils {
    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String formatSize(long j) {
        String str;
        if (j >= 1024) {
            j /= 1024;
            if (j >= 1024) {
                j /= 1024;
                str = "MB";
            } else {
                str = "KB";
            }
        } else {
            str = null;
        }
        StringBuilder sb = new StringBuilder(Long.toString(j));
        for (int length = sb.length() - 3; length > 0; length -= 3) {
            sb.insert(length, ',');
        }
        if (str != null) {
            sb.append(str);
        }
        return sb.toString();
    }

    @SuppressLint({"NewApi"})
    public static long getAvailableMemorySize(File file) {
        StatFs statFs = new StatFs(file.getPath());
        return statFs.getAvailableBlocks() * statFs.getBlockSize();
    }

    @SuppressLint({"NewApi"})
    public static long getTotalMemorySize(File file) {
        PrintStream printStream = System.out;
        printStream.println("path.getPath() = " + file.getPath());
        StatFs statFs = new StatFs(file.getPath());
        return statFs.getBlockCount() * statFs.getBlockSize();
    }
}
