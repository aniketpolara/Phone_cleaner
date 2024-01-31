package com.cleanPhone.mobileCleaner.filestorage;

import android.os.Environment;

public abstract class DialogConfigs {
    public static String DEFAULT_DIR = null;
    public static final String DIRECTORY_SEPERATOR = "/";
    public static final String STORAGE_DIR;

    static {
        String str = "" + Environment.getExternalStorageDirectory();
        STORAGE_DIR = str;
        DEFAULT_DIR = str;
    }
}
