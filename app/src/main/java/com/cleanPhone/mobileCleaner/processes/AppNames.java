package com.cleanPhone.mobileCleaner.processes;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Hashtable;


public class AppNames {

    public static final Hashtable<String, String> f4986a = new Hashtable<>();

    public static String getLabel(PackageManager packageManager, PackageInfo packageInfo) {
        Hashtable<String, String> hashtable = f4986a;
        if (hashtable.containsKey(packageInfo.packageName)) {
            return hashtable.get(packageInfo.packageName);
        }
        String charSequence = packageInfo.applicationInfo.loadLabel(packageManager).toString();
        hashtable.put(packageInfo.packageName, charSequence);
        return charSequence;
    }
}
