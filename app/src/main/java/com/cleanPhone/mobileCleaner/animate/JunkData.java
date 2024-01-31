package com.cleanPhone.mobileCleaner.animate;

import static java.lang.Short.MAX_VALUE;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.cleanPhone.mobileCleaner.JunkCleanScreen;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.JunkListWrapper;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JunkData {
    private static final long SIZE_LIMIT = 10485760;
    public long APK_CACHE_SIZE;
    public long APP_CACHE_SIZE;
    public long BOOST_CACHE_SIZE;
    public long EMPTY_CACHE_SIZE;
    public long SYS_CACHE_SIZE;
    public long TEMP_CACHE_SIZE;
    public Context f4779a;
    public long boostsizechecked;
    public boolean cancel;
    public HashMap<String, ArrayList<JunkListWrapper>> junkDataMap;
    private JunkDeleteTask junkDeleteTask;
    public ArrayList<String> listDataHeader;
    public int midState;
    private int partialDataSize;
    public boolean removeBoost;
    public boolean sys_cache_deleted;
    public long totSelectedToDeleteSize;
    public int totalDeletedCount;
    public long totalDeletedSize;
    public int totalSelectedCategories;
    public int totalcount;
    public int totalemptyfoldersDeleted;
    public int totalfolders;
    public int totaljunkcount;
    public long totalsize;
    public int totselectedTodelete;
    public boolean cache_checked = true;
    public boolean emptyfolder_checked = true;
    public boolean sys_checked = true;
    public boolean temp_checked = true;
    public boolean apk_checked = false;
    public boolean boost_checked = true;
    public boolean partialDelete = false;
    private ArrayList<JunkListWrapper> dataTodelete = new ArrayList<>();
    public boolean removeSysCache = false;
    public boolean allChecked = false;

    public class JunkDeleteTask extends AsyncTask<String, Long, String> {
        public ProUpdate f4780a;
        private boolean deleteRemaining;
        private int totalemptyfolders;

        public JunkDeleteTask(Context context, ProUpdate proUpdate, boolean z) {
            JunkData.this.f4779a = context;
            this.deleteRemaining = z;
            this.f4780a = proUpdate;
        }

        private void clearAllCache() {
            PackageManager packageManager = JunkData.this.f4779a.getPackageManager();
            for (Method method : packageManager.getClass().getDeclaredMethods()) {
                if (method.getName().equals("freeStorage")) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (method.getParameterTypes().length == 2) {
                            try {
                                method.invoke(packageManager, 0L, null);
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                        return;
                    }
                    try {
                        method.invoke(packageManager, Long.valueOf((long) MAX_VALUE), null);
                        return;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
            }
        }

        @Override
        public void onCancelled() {
            super.onCancelled();
            if (JunkData.this.cancel) {
                this.f4780a.onUpdate(-3L, -3L, -3L, -3L);
            }
        }

        @Override
        public String doInBackground(String... strArr) {
            boolean z;
            int i;
            String str;
            ActivityManager activityManager = null;
            String str2;
            ArrayList<JunkListWrapper> arrayList;
            ActivityManager activityManager2;
            String str3;
            String str4;
            String str5;
            String str6;
            String str7;
            String str8;
            String str9;
            int i2 = Build.VERSION.SDK_INT;
            if (i2 < 23) {
                clearAllCache();
            } else {
                JunkData junkData = JunkData.this;
                if (junkData.sys_checked && junkData.cache_checked && Util.isConnectingToInternet(junkData.f4779a)) {
                    if (i2 > 19) {
                        GlobalData.cacheCheckedAboveMarshmallow = true;
                    } else {
                        GlobalData.cacheCheckedAboveMarshmallow = false;
                    }
                } else if (JunkData.this.cache_checked) {
                    GlobalData.cacheCheckedAboveMarshmallow = true;
                }
            }
            ActivityManager activityManager3 = (ActivityManager) JunkData.this.f4779a.getSystemService(Context.ACTIVITY_SERVICE);
            boolean z2 = this.deleteRemaining;
            String str10 = JunkCleanScreen.CACHEUSER;
            String str11 = JunkCleanScreen.BOOSTFILES;
            String str12 = "cancel == true ";
            String str13 = SharedPrefUtil.RAMATPAUSE;
            String str14 = SharedPrefUtil.RAMPAUSE;
            String str15 = SharedPrefUtil.LASTCOOLTIME;
            String str16 = "JJJJJJJJJJJJ";
            if (z2) {
                JunkData junkData2 = JunkData.this;
                junkData2.partialDelete = false;
                int i3 = junkData2.partialDataSize;
                while (i3 < JunkData.this.dataTodelete.size()) {
                    Log.d(str16, "cancel val " + JunkData.this.cancel);
                    JunkData junkData3 = JunkData.this;
                    if (!junkData3.cancel) {
                        if (((JunkListWrapper) junkData3.dataTodelete.get(i3)).ischecked) {
                            str7 = str12;
                            str9 = str16;
                            publishProgress(Long.valueOf(((i3 + 1) * 100) / JunkData.this.dataTodelete.size()), Long.valueOf(JunkData.this.totalDeletedSize), Long.valueOf(JunkData.this.totalDeletedCount), Long.valueOf(JunkData.this.totalsize));
                            JunkData junkData4 = JunkData.this;
                            junkData4.totalDeletedCount++;
                            str8 = str13;
                            junkData4.totalDeletedSize += ((JunkListWrapper) junkData4.dataTodelete.get(i3)).size;
                            if (!((JunkListWrapper) JunkData.this.dataTodelete.get(i3)).type.equalsIgnoreCase(JunkCleanScreen.TEMP)) {
                                if (((JunkListWrapper) JunkData.this.dataTodelete.get(i3)).type.equalsIgnoreCase(JunkCleanScreen.BOOSTFILES)) {
                                    activityManager3.killBackgroundProcesses(((JunkListWrapper) JunkData.this.dataTodelete.get(i3)).name);
                                    JunkData junkData5 = JunkData.this;
                                    junkData5.boostsizechecked += ((JunkListWrapper) junkData5.dataTodelete.get(i3)).size;
                                } else if (((JunkListWrapper) JunkData.this.dataTodelete.get(i3)).type.equalsIgnoreCase(JunkCleanScreen.CACHEUSER)) {
                                    for (int i4 = 0; i4 < ((JunkListWrapper) JunkData.this.dataTodelete.get(i3)).fileslist.size(); i4++) {
                                        File file = new File("" + ((JunkListWrapper) JunkData.this.dataTodelete.get(i3)).fileslist.get(i4));
                                        if (file.exists()) {
                                            file.delete();
                                            JunkData.this.f4779a.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
                                        }
                                    }
                                } else {
                                    File file2 = new File("" + ((JunkListWrapper) JunkData.this.dataTodelete.get(i3)).path);
                                    if (file2.isDirectory()) {
                                        JunkData junkData6 = JunkData.this;
                                        junkData6.deleteFromFolder(((JunkListWrapper) junkData6.dataTodelete.get(i3)).path);
                                    }
                                    if (((JunkListWrapper) JunkData.this.dataTodelete.get(i3)).type.equalsIgnoreCase(JunkCleanScreen.EMPTY)) {
                                        JunkData.this.totalemptyfoldersDeleted++;
                                    }
                                    if (file2.exists()) {
                                        file2.delete();
                                        JunkData.this.f4779a.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2)));
                                    }
                                }
                            } else {
                                File file3 = new File(new File("" + ((JunkListWrapper) JunkData.this.dataTodelete.get(i3)).path).getParent());
                                if (file3.isDirectory()) {
                                    JunkData.this.deleteFromFolder(file3.getPath());
                                }
                            }
                            try {
                                Thread.sleep(50L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            str7 = str12;
                            str8 = str13;
                            str9 = str16;
                        }
                        i3++;
                        str12 = str7;
                        str16 = str9;
                        str13 = str8;
                    } else {
                        Log.d(str16, str12);
                        return null;
                    }
                }
                String str17 = str13;
                JunkData junkData7 = JunkData.this;
                if (junkData7.boost_checked) {
                    junkData7.killAllProcesses();
                    SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(JunkData.this.f4779a);
                    sharedPrefUtil.saveString(SharedPrefUtil.LASTBOOSTTIME, "" + System.currentTimeMillis());
                    sharedPrefUtil.saveString(SharedPrefUtil.LASTCOOLTIME, "" + System.currentTimeMillis());
                    sharedPrefUtil.saveString(SharedPrefUtil.RAMPAUSE, "" + System.currentTimeMillis());
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    JunkData junkData8 = JunkData.this;
                    sb.append(junkData8.getRamSize(junkData8.f4779a));
                    sharedPrefUtil.saveString(str17, sb.toString());
                }
                return null;
            }
            String str18 = "cancel == true ";
            String str19 = SharedPrefUtil.RAMATPAUSE;
            String str20 = "JJJJJJJJJJJJ";
            this.totalemptyfolders = 0;
            JunkData.this.dataTodelete = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            JunkData junkData9 = JunkData.this;
            if (junkData9.removeBoost) {
                junkData9.totalSelectedCategories = 4;
            }
            if (junkData9.removeSysCache) {
                junkData9.totalSelectedCategories = 3;
            }
            int i5 = 0;
            while (i5 < JunkData.this.junkDataMap.size()) {
                if (i5 < JunkData.this.listDataHeader.size()) {
                    JunkData junkData10 = JunkData.this;
                    ArrayList<JunkListWrapper> arrayList3 = junkData10.junkDataMap.get(junkData10.listDataHeader.get(i5));
                    int i6 = 0;
                    while (i6 < arrayList3.size()) {
                        if (arrayList3.get(i6).ischecked) {
                            str4 = str19;
                            JunkData.this.dataTodelete.add(arrayList3.get(i6));
                            if (arrayList3.get(i6).type.equalsIgnoreCase(JunkCleanScreen.EMPTY)) {
                                this.totalemptyfolders++;
                            }
                            if (!arrayList2.contains(arrayList3.get(i6).name + "" + arrayList3.get(i6).path)) {
                                arrayList2.add(arrayList3.get(i6).name + "" + arrayList3.get(i6).path);
                            }
                            str5 = str14;
                            str6 = str15;
                            arrayList = arrayList3;
                            activityManager2 = activityManager3;
                            str3 = str10;
                            JunkData.this.totSelectedToDeleteSize += arrayList3.get(i6).size;
                            JunkData.this.totselectedTodelete++;
                        } else {
                            arrayList = arrayList3;
                            activityManager2 = activityManager3;
                            str3 = str10;
                            str4 = str19;
                            str5 = str14;
                            str6 = str15;
                        }
                        i6++;
                        str15 = str6;
                        str14 = str5;
                        str19 = str4;
                        activityManager3 = activityManager2;
                        arrayList3 = arrayList;
                        str10 = str3;
                    }
                }
                i5++;
                str15 = str15;
                str14 = str14;
                str19 = str19;
                activityManager3 = activityManager3;
                str10 = str10;
            }
            ActivityManager activityManager4 = activityManager3;
            String str21 = str10;
            String str22 = str19;
            String str23 = str14;
            String str24 = str15;
            JunkData junkData11 = JunkData.this;
            int i7 = (junkData11.totselectedTodelete * 100) / junkData11.totaljunkcount;
            if (Util.isAdsFree(junkData11.f4779a) || !new SharedPrefUtil(JunkData.this.f4779a).getBoolean("RVJ_AD") || ((i7 <= 20 && JunkData.this.totSelectedToDeleteSize <= JunkData.SIZE_LIMIT) || !Util.isConnectingToInternet(JunkData.this.f4779a))) {
                z = true;
            } else {
                z = true;
                JunkData.this.partialDelete = true;
            }
            JunkData junkData12 = JunkData.this;
            JunkData junkData13 = JunkData.this;
            if (junkData13.partialDelete) {
                i = junkData13.totselectedTodelete < 11 ? 50 : 10;
                junkData13.midState = i;
            } else {
                i = 100;
            }
            junkData13.totselectedTodelete = arrayList2.size();
            if (JunkData.this.dataTodelete.size() != 0) {
                JunkData junkData14 = JunkData.this;
                junkData14.partialDataSize = (junkData14.dataTodelete.size() * i) / 100;
                int i8 = 0;
                while (i8 < JunkData.this.partialDataSize) {
                    JunkData junkData15 = JunkData.this;
                    if (!junkData15.cancel) {
                        String str25 = str18;
                        String str26 = str20;
                        if (((JunkListWrapper) junkData15.dataTodelete.get(i8)).ischecked) {
                            publishProgress(Long.valueOf(((i8 + 1) * 100) / JunkData.this.dataTodelete.size()), Long.valueOf(JunkData.this.totalDeletedSize), Long.valueOf(JunkData.this.totalDeletedCount), Long.valueOf(JunkData.this.totalsize));
                            JunkData junkData16 = JunkData.this;
                            junkData16.totalDeletedCount++;
                            str = str11;
                            junkData16.totalDeletedSize += ((JunkListWrapper) junkData16.dataTodelete.get(i8)).size;
                            try {
                                if (!((JunkListWrapper) JunkData.this.dataTodelete.get(i8)).type.equalsIgnoreCase(JunkCleanScreen.TEMP)) {
                                    if (((JunkListWrapper) JunkData.this.dataTodelete.get(i8)).type.equalsIgnoreCase(str)) {
                                        activityManager = activityManager4;
                                        activityManager.killBackgroundProcesses(((JunkListWrapper) JunkData.this.dataTodelete.get(i8)).name);
                                        JunkData junkData17 = JunkData.this;
                                        str20 = str26;
                                        str = str;
                                        junkData17.boostsizechecked += ((JunkListWrapper) junkData17.dataTodelete.get(i8)).size;
                                    } else {
                                        str20 = str26;
                                        str = str;
                                        activityManager = activityManager4;
                                        str2 = str21;
                                        if (((JunkListWrapper) JunkData.this.dataTodelete.get(i8)).type.equalsIgnoreCase(str2)) {
                                            for (int i9 = 0; i9 < ((JunkListWrapper) JunkData.this.dataTodelete.get(i8)).fileslist.size(); i9++) {
                                                File file4 = new File("" + ((JunkListWrapper) JunkData.this.dataTodelete.get(i8)).fileslist.get(i9));
                                                if (file4.exists()) {
                                                    file4.delete();
                                                    JunkData.this.f4779a.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file4)));
                                                }
                                            }
                                        } else {
                                            File file5 = new File("" + ((JunkListWrapper) JunkData.this.dataTodelete.get(i8)).path);
                                            if (file5.isDirectory()) {
                                                JunkData junkData18 = JunkData.this;
                                                junkData18.deleteFromFolder(((JunkListWrapper) junkData18.dataTodelete.get(i8)).path);
                                            }
                                            if (((JunkListWrapper) JunkData.this.dataTodelete.get(i8)).type.equalsIgnoreCase(JunkCleanScreen.EMPTY)) {
                                                JunkData.this.totalemptyfoldersDeleted++;
                                            }
                                            if (file5.exists()) {
                                                file5.delete();
                                                JunkData.this.f4779a.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file5)));
                                            }
                                        }
                                        Thread.sleep(100L);
                                    }
                                } else {
                                    File file6 = new File(new File("" + ((JunkListWrapper) JunkData.this.dataTodelete.get(i8)).path).getParent());
                                    if (file6.isDirectory()) {
                                        JunkData.this.deleteFromFolder(file6.getPath());
                                    }
                                    str20 = str26;
                                    activityManager = activityManager4;
                                }
                                Thread.sleep(100L);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            str2 = str21;
                        } else {
                            str20 = str26;
                            str = str11;
                            activityManager = activityManager4;
                            str2 = str21;
                        }
                        i8++;
                        str18 = str25;
                        str21 = str2;
                        activityManager4 = activityManager;
                        str11 = str;
                    } else {
                        Log.d(str20, str18);
                        return null;
                    }
                }
                JunkData junkData19 = JunkData.this;
                if (junkData19.boost_checked && !junkData19.partialDelete) {
                    SharedPrefUtil sharedPrefUtil2 = new SharedPrefUtil(JunkData.this.f4779a);
                    sharedPrefUtil2.saveString(SharedPrefUtil.LASTBOOSTTIME, "" + System.currentTimeMillis());
                    sharedPrefUtil2.saveString(str24, "" + System.currentTimeMillis());
                    sharedPrefUtil2.saveString(str23, "" + System.currentTimeMillis());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("");
                    JunkData junkData20 = JunkData.this;
                    sb2.append(junkData20.getRamSize(junkData20.f4779a));
                    sharedPrefUtil2.saveString(str22, sb2.toString());
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(String str) {
            super.onPostExecute( str);
            Log.d("JJJJJJJJJJJJ", "onpost cancel " + JunkData.this.cancel);
            JunkData junkData = JunkData.this;
            if (junkData.cancel) {
                this.f4780a.onUpdate(-3L, -3L, -3L, -3L);
            } else if (junkData.partialDelete) {
                this.f4780a.onUpdate(-1L, -1L, -1L, -1L);
            } else {
                this.f4780a.onUpdate(-2L, -2L, -2L, -2L);
                if (JunkData.this.allChecked) {
                    SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(JunkData.this.f4779a);
                    sharedPrefUtil.saveString(SharedPrefUtil.JUNCCLEANTIME, "" + System.currentTimeMillis());
                }
            }
        }

        @Override
        public void onProgressUpdate(Long... lArr) {
            super.onProgressUpdate(lArr);
            ProUpdate proUpdate = this.f4780a;
            if (proUpdate == null || lArr == null || lArr.length <= 0) {
                return;
            }
            try {
                proUpdate.onUpdate(lArr[0].longValue(), lArr[1].longValue(), lArr[2].longValue(), lArr[3].longValue());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public interface ProUpdate {
        void onUpdate(long j, long j2, long j3, long j4);
    }

    public JunkData(HashMap<String, ArrayList<JunkListWrapper>> hashMap) {
        this.junkDataMap = hashMap;
    }

    public String deleteFromFolder(String str) {
        File file = new File(str);
        if (file.isDirectory()) {
            Log.d("TTTTTTTT", "is directory");
        }
        String[] list = file.list();
        if (!file.isDirectory() || list == null) {
            return "";
        }
        for (int i = 0; i < list.length; i++) {
            if (list[i].toLowerCase().endsWith("log") || list[i].toLowerCase().endsWith("xlog") || list[i].equalsIgnoreCase("log") || list[i].toLowerCase().endsWith("tmp") || list[i].toLowerCase().endsWith("dat") || list[i].toLowerCase().endsWith("journal") || list[i].toLowerCase().endsWith("cuid") || list[i].toLowerCase().endsWith("bat") || list[i].toLowerCase().endsWith("dk") || list[i].toLowerCase().endsWith("xml") || list[i].toLowerCase().endsWith("nomedia") || list[i].toLowerCase().endsWith("bin") || list[i].toLowerCase().endsWith("thumbnail") || list[i].toLowerCase().endsWith("thumbnails") || list[i].toLowerCase().endsWith("js") || list[i].toLowerCase().endsWith("css") || list[i].toLowerCase().endsWith("file") || list[i].toLowerCase().endsWith("idx")) {
                File file2 = new File(str + DialogConfigs.DIRECTORY_SEPERATOR + list[i]);
                file2.delete();
                this.f4779a.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2)));
            }
        }
        return "";
    }

    public void killAllProcesses() {
        try {
            List<ApplicationInfo> installedApplications = this.f4779a.getPackageManager().getInstalledApplications(0);
            ActivityManager activityManager = (ActivityManager) this.f4779a.getSystemService(Context.ACTIVITY_SERVICE);
            ArrayList ignoredData = ((ParentActivity) this.f4779a).getIgnoredData();
            for (ApplicationInfo applicationInfo : installedApplications) {
                if ((applicationInfo.flags & 1) != 1 && !applicationInfo.packageName.equals("com.mobiclean.phoneclean")) {
                    if (!ignoredData.contains("" + applicationInfo.packageName)) {
                        activityManager.killBackgroundProcesses(applicationInfo.packageName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean showSavedTemp(String str, String str2) {
        if (str == null && str2 == null) {
            return false;
        }
        if (Util.checkTimeDifference("" + System.currentTimeMillis(), str) > GlobalData.coolPause) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(System.currentTimeMillis());
            return Util.checkTimeDifference(sb.toString(), str2) <= ((long) GlobalData.boostPause);
        }
        return true;
    }

    public void cancelCleaning() {
        JunkDeleteTask junkDeleteTask;
        if (this.junkDeleteTask.getStatus() != AsyncTask.Status.RUNNING || (junkDeleteTask = this.junkDeleteTask) == null) {
            return;
        }
        this.cancel = true;
        junkDeleteTask.cancel(true);
    }

    public void deleteJunk(Context context, ProUpdate proUpdate, boolean z) {
        JunkDeleteTask junkDeleteTask = new JunkDeleteTask(context, proUpdate, z);
        this.junkDeleteTask = junkDeleteTask;
        junkDeleteTask.execute(new String[0]);
    }

    public int getRamSize(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(memoryInfo);
        long j = memoryInfo.totalMem;
        return Math.round((float) (((j - memoryInfo.availMem) * 100) / j)) - 1;
    }

    public long getSizeAsPerOS(long j) {
        return j;
    }

    public boolean isAlreadyBoosted(Context context) {
        SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(context);
        return showSavedTemp(sharedPrefUtil.getString(SharedPrefUtil.LASTCOOLTIME), sharedPrefUtil.getString(SharedPrefUtil.LASTBOOSTTIME));
    }
}
