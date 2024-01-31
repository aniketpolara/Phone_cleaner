package com.cleanPhone.mobileCleaner.wrappers;

import java.io.Serializable;
import java.util.ArrayList;

public class PackageInfoStruct implements Serializable {
    public long apksize;
    public long appsize;
    public long cachesize;
    public long crc;
    public long dataSize;
    public String id;
    public int installLocation;
    public boolean isMalware;
    public boolean ischecked;
    public String malwareCategory;
    public String malwareid;
    public long movesize;
    public long userCacheSize;
    public ArrayList<String> userCachefilesList;
    public String appname = "";
    public String pname = "";
    public String apkPath = "";
    public String versionName = "";
    public int versionCode = 0;
    public String datadir = "";
    public String saurcedir = "";
    public boolean isSystemApp = false;
    public boolean ignored = false;
}
