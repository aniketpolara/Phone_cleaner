package com.cleanPhone.mobileCleaner.utility;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.work.WorkRequest;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.similerphotos.MySharedPreference;
import com.cleanPhone.mobileCleaner.wrappers.JunkListWrapper;
import com.cleanPhone.mobileCleaner.wrappers.ProcessWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class GlobalData {
    public static int APP_INDEX = 0;
    public static String APP_OPEN = "ca-app-pub-8283322695506975/5282360424";
    public static boolean AUTO_START_SHOWN = false;
    public static final String BANNER = "banner";
    public static final String COUNTRY_URL = "http://cf.mobiclean.co/ProductPrice.svc/FetchProdPrice/";
    public static final int CREATE_GROUPS = 8769;
    public static final String DB_NAME = "completedatabase.db";
    public static final String DB_UPDATE_URL = "http://cdn1.mobiclean.co/mob.mc/dbupdate/databaseupdate.xml";
    public static final int EB_CODE_NOTIFICATION = 777;
    public static final int EB_CODE_NOTIFICATION_REMOVE = 7771;
    public static final String FIRST_BOOSTNOTI_TIME = "06:00";
    public static final String FIRST_NOTI_TIME1 = "14:00";
    public static final String GAME_BOOST_SHORTCUT = "GAME_BOOST_SHORTCUT";
    public static final String HEADER_NOTI_TRACK = "HEADER_NOTI_TRACK";
    public static final String INTENT_FILTER_NOTI_CANCEL = "receivers.NotificationIconReceiver.cancel";
    public static final String IgnoreList = "ignore";
    public static final String NOTI_RESULT_BACK = "NOTI_RESULT_BACK";
    public static final String NotificationList = "notification_list";
    public static final String REDIRECTHOME = "REDIRECTHOME";
    public static final String REDIRECTNOTI = "REDIRECTNOTI";
    public static final int SCAN_IMAGES = 4490;
    public static final String SECOND_BOOSTNOTI_TIME = "18:00";
    public static final String SmartFile = "smart_selection";
    public static String backuppath = "/MobiClean/backup/";
    public static boolean cacheCheckedAboveMarshmallow;
    public static String cpu_temperature;
    public static int deviceHeight;
    public static int deviceWidth;
    public static boolean fromDatausageSetting;
    public static boolean fromNotificationsetting;
    public static boolean isExecuting;
    public static boolean notificationBack;
    public static String temperaryTemp;
    public static long uninstalledAppSize;
    public static ArrayList<JunkListWrapper> cacheContainingApps = new ArrayList<>();
    public static ArrayList<ProcessWrapper> processDataList = new ArrayList<>();
    public static ArrayList<String> allcacheFoldersPath = new ArrayList<>();
    public static int imageCriteia = 0;
    public static int videoCriteia = 0;
    public static int audioCriteia = 0;
    public static int fileCriteia = 0;
    public static int otherCriteia = 0;
    public static int apkCriteria = 0;
    public static int allCriteria = 0;
    public static int junccleanPause = 120000;
    public static int boostPause = 120000;
    public static boolean afterDelete = false;
    public static int duplicacyLevel = 75;
    public static int duplicacyTime = 7;
    public static int duplicacyDist = 7;
    public static int minimumSize = 32;
    public static int imageSize = 64;
    public static boolean fromdup = true;
    public static long coolPause = 120000;
    public static ArrayList<String> nongameApps = new ArrayList<>();
    public static boolean showIronsrc = false;
    public static boolean fromSocialCleaning = false;
    public static boolean phone_state_Permission = false;
    public static boolean loadAds = true;
    public static boolean backPressedResult = false;
    public static boolean backfrom_settings = false;
    public static String FILE_NAME = "CleanerLogging.txt";
    public static boolean isDebug = false;
    public static long photocount = WorkRequest.MIN_BACKOFF_MILLIS;
    public static boolean fromSpacemanager = false;
    public static volatile boolean shouldContinue = true;
    public static boolean proceededToAd = false;
    public static boolean imageviewed = false;
    int admob = 3;
    public static boolean start_app = false;
    public static String DBUPDATE_TIME = "1:00";
    public static boolean isSplashAdLoad = false;
    public static String xObtk = BuildConfig.API_KEY;
    public static String rGisV = BuildConfig.API_KEY;
    public static boolean dontRedirect = false;
    public static boolean dontAnimate = false;
    public static boolean onAppmanager = false;
    public static boolean returnedAfterDeletion = false;
    public static boolean returnedAfterSocialDeletion = false;

    public static void SETAPPLAnguage(Context context) {
        Locale locale;
        try {
            if (MySharedPreference.getLngIndex(context) > 0) {
                String str = context.getResources().getStringArray(R.array.country_code)[MySharedPreference.getLngIndex(context)];
                if (str.contains("zh-rCN")) {
                    locale = Locale.SIMPLIFIED_CHINESE;
                } else if (str.contains("pt-rBR")) {
                    locale = new Locale("pt", "BR");
                } else if (str.contains("pt-rPT")) {
                    locale = new Locale("pt", "PT");
                } else {
                    locale = new Locale(str);
                }
                Log.e("locale ", "" + locale);
                Locale.setDefault(locale);
                Configuration configuration = new Configuration();
                configuration.locale = locale;
                context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
                return;
            }
            String[] stringArray = context.getResources().getStringArray(R.array.country_list);
            String displayLanguage = Locale.getDefault().getDisplayLanguage(Locale.ENGLISH);
            for (int i = 0; i < stringArray.length; i++) {
                if (TextUtils.equals(displayLanguage, stringArray[i])) {
                    MySharedPreference.setLngIndex(context, i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int StringItirator(String str) {
        int i = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (str.charAt(i2) == '/') {
                i++;
            }
        }
        return i;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String str2 = "";
        boolean z = true;
        for (char c2 : str.toCharArray()) {
            if (z && Character.isLetter(c2)) {
                str2 = str2 + Character.toUpperCase(c2);
                z = false;
            } else {
                if (Character.isWhitespace(c2)) {
                    z = true;
                }
                str2 = str2 + c2;
            }
        }
        return str2;
    }

    public static boolean deleteObj(Context context, String str) {
        try {
            return context.getFileStreamPath(str).delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getDefaultBatterySetting(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("name", context.getResources().getString(R.string.mbc_defaults));
            jSONObject.put("wifi", false);
            jSONObject.put("autosync", false);
            jSONObject.put("blootooth", false);
            jSONObject.put("brightness", false);
            jSONObject.put("timeout", false);
            jSONObject.put("vibration", false);
            jSONObject.put("autowifi", true);
            jSONObject.put("autoautosync", true);
            jSONObject.put("autoblootooth", true);
            jSONObject.put("autobrightness", true);
            jSONObject.put("autotimeout", true);
            jSONObject.put("autovibration", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public static String getDeviceName() {
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        if (str2.startsWith(str)) {
            return capitalize(str2);
        }
        if (str.equalsIgnoreCase("HTC")) {
            return "HTC " + str2;
        }
        return capitalize(str) + " " + str2;
    }

    public static Object getObj(Context context, String str) throws IOException, ClassNotFoundException {
        FileInputStream openFileInput = context.openFileInput(str.toString());
        ObjectInputStream objectInputStream = new ObjectInputStream(openFileInput);
        Object readObject = objectInputStream.readObject();
        objectInputStream.close();
        openFileInput.close();
        return readObject;
    }

    public static String getSleepBatterySetting(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("name", context.getResources().getString(R.string.mbc_battery_saver));
            jSONObject.put("wifi", true);
            jSONObject.put("autosync", true);
            jSONObject.put("blootooth", true);
            jSONObject.put("brightness", true);
            jSONObject.put("timeout", true);
            jSONObject.put("vibration", true);
            jSONObject.put("autowifi", true);
            jSONObject.put("autoautosync", true);
            jSONObject.put("autoblootooth", true);
            jSONObject.put("autobrightness", true);
            jSONObject.put("autotimeout", true);
            jSONObject.put("autovibration", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public static void imageSpace(Context context) {
        int count = 0;
        Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data", "_display_name", "_size"}, null, null, null);
        int i = 0;
        long j = 0;
        if (query != null) {
            if (query.getCount() == 0) {
                MySharedPreference.setImageOccupiedSpace(context, "img_space", 0L);
            }
            try {
                try {
                    count = query.getCount();
                } finally {
                    if (query != null) {
                        query.close();
                    }
                }
            } catch (Exception unused) {
            }
            try {
                int columnIndex = query.getColumnIndex("_size");
                query.moveToFirst();
                while (i < query.getCount()) {
                    query.moveToPosition(i);
                    j += query.getLong(columnIndex);
                    i++;
                }
                if (query != null) {
                    query.close();
                }
                i = count;
            } catch (Exception unused2) {
                i = count;
                if (permissionForStorageGiven(context)) {
                }
            }
        }
        if (permissionForStorageGiven(context)) {
            return;
        }
        MySharedPreference.setImageOccupiedSpace(context, "img_space", j);
        MySharedPreference.setImageCounter(context, "img_count", i);
    }

    public static boolean isEmulated(String str) {
        return str.equals("sdcardfs") || str.equals("fuse");
    }

    public static boolean isMyServiceRunning(Context context, Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isObjExist(Context context, String str) {
        File fileStreamPath = context.getFileStreamPath(str);
        return fileStreamPath != null && fileStreamPath.exists();
    }

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
        if (arrayList.size() == 1 && StringItirator(arrayList.get(0)) > 2) {
            arrayList.add(string);
        }
        return arrayList;
    }

    public static boolean permissionForStorageGiven(Context context) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 30) {
            return Environment.isExternalStorageManager();
        }
        return i < 23 || ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public static void saveObj(Context context, String str, Object obj) throws Exception {
        FileOutputStream openFileOutput = context.openFileOutput(str, 0);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(openFileOutput);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        openFileOutput.close();
    }

    public static String storageCardPath() {
        try {
            return Environment.getExternalStorageDirectory().getCanonicalPath();
        } catch (Exception unused) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }

    public static String withSlash(String str) {
        if (str.length() <= 0 || str.charAt(str.length() - 1) == '/') {
            return str;
        }
        return str + IOUtils.DIR_SEPARATOR_UNIX;
    }
}