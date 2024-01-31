package com.cleanPhone.mobileCleaner.similerphotos;

import android.content.Context;
import android.content.SharedPreferences;

import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;

public class MySharedPreference {
    private static String DEFAULT_PREF = "DEFAULT_PREF";
    private static final String DUPLICATE = "DUPLICATE";
    private static final String DUPLICATED = "DUPLICATED";
    private static final String DUPLICATEDIST = "DUPLICATEDIST";
    private static final String DUPLICATELEVEL = "DUPLICATELEVEL";
    private static final String DUPLICATET = "DUPLICATET";
    private static final String DUPLICATETIME = "DUPLICATETIME";
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sharePref;

    public static boolean a(String str, boolean z) {
        return b().getBoolean(str, z);
    }

    public static SharedPreferences b() {
        return MobiClean.getInstance().getSharedPreferences(DEFAULT_PREF, 0);
    }

    public static int c(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DUPLICATE, 0);
        sharePref = sharedPreferences;
        return sharedPreferences.getInt(DUPLICATELEVEL, 25);
    }

    public static int d(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DUPLICATED, 0);
        sharePref = sharedPreferences;
        return sharedPreferences.getInt(DUPLICATEDIST, 72);
    }

    public static int e(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DUPLICATET, 0);
        sharePref = sharedPreferences;
        return sharedPreferences.getInt(DUPLICATETIME, 72);
    }

    public static int f(String str, int i) {
        return b().getInt(str, i);
    }

    public static int g(String str, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(str, 0);
        sharePref = sharedPreferences;
        return sharedPreferences.getInt(str, 0);
    }

    public static int getLngIndex(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("lng_index", 0);
        sharePref = sharedPreferences;
        return sharedPreferences.getInt("lng_index", 0);
    }

    public static long h(String str, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(str, 0);
        sharePref = sharedPreferences;
        return sharedPreferences.getLong(str, 0L);
    }

    public static boolean i(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DUPLICATED, 0);
        sharePref = sharedPreferences;
        return sharedPreferences.getBoolean("first_auto_mark", false);
    }

    public static boolean isAVFree(Context context) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(DUPLICATED, 0);
            sharePref = sharedPreferences;
            return sharedPreferences.getBoolean("avfree", false);
        }
        return false;
    }

    public static boolean isAdsFree(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DUPLICATED, 0);
        sharePref = sharedPreferences;
        return sharedPreferences.getBoolean(SharedPrefUtil.ADS_FREE, false);
    }


    public static void j(Context context, boolean z) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DUPLICATED, 0);
        sharePref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        edit.putBoolean("first_auto_mark", z);
        editor.apply();
    }

    public static void k(String str, boolean z) {
        b().edit().putBoolean(str, z).apply();
    }

    public static void l(Context context, int i) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DUPLICATE, 0);
        sharePref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        edit.putInt(DUPLICATELEVEL, i);
        editor.apply();
    }

    public static void m(Context context, int i) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DUPLICATED, 0);
        sharePref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        edit.putInt(DUPLICATEDIST, i);
        editor.apply();
    }

    public static void n(Context context, int i) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DUPLICATET, 0);
        sharePref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        edit.putInt(DUPLICATETIME, i);
        editor.apply();
    }

    public static void o(String str, int i) {
        b().edit().putInt(str, i).apply();
    }

    public static void setImageCounter(Context context, String str, int i) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(str, 0);
        sharePref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        edit.putInt(str, i);
        editor.apply();
    }

    public static void setImageOccupiedSpace(Context context, String str, long j) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(str, 0);
        sharePref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        edit.putLong(str, j);
        editor.apply();
    }

    public static void setLngIndex(Context context, int i) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("lng_index", 0);
        sharePref = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        editor = edit;
        edit.putInt("lng_index", i);
        editor.apply();
    }


}
