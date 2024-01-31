package com.cleanPhone.mobileCleaner.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.webkit.MimeTypeMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.work.Data;

import com.facebook.appevents.AppEventsConstants;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.google.firebase.messaging.ServiceStarter;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.similerphotos.MySharedPreference;
import com.cleanPhone.mobileCleaner.wrappers.LocationModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Util {
    private static final String FILE_AD = "ispaid.txt";
    public static boolean f5356a = false;
    public static boolean isHome = false;
    public static ArrayList<LocationModel> locationModelArrayList;
    private String[] audioArray = {"0 MB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "7 MB", "10 MB", "12 MB", "15 MB", "20 MB", "25 MB", "50 MB", "100 MB"};
    private static int[] imageArraySize = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25};
    private static int[] videoArraySize = {0, 5, 10, 15, 25, 30, 50, 75, 100, 200, 350, ServiceStarter.ERROR_UNKNOWN, 750, 1024};
    private static int[] audioArraysize = {0, 1, 2, 3, 4, 5, 7, 10, 12, 15, 20, 25, 50, 100};
    private static int[] filesArraySize = {0, 50, 100, ServiceStarter.ERROR_UNKNOWN, 1024, 2048, 3072, 4096, 5120, Data.MAX_DATA_BYTES, 15360, 20480, 25600, 51200};
    private static int[] othersArraysize = {0, 1, 2, 5, 10, 20, 30, 50, 75, 100, 200, ServiceStarter.ERROR_UNKNOWN, 750, 1024};

    public static int actionbarsize(Context context) {
        int i;
        TypedValue typedValue = new TypedValue();
        try {
            i = context.getTheme().resolveAttribute(16843499, typedValue, true) ? TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics()) + 10 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            i = (displayMetrics.heightPixels * 12) / 100;
        }
        Log.d("height : action ", "" + i);
        return i;
    }

    public static void appendLog(String str, String str2) {
        if (GlobalData.isDebug) {
            File file = new File(Environment.getExternalStorageDirectory() + DialogConfigs.DIRECTORY_SEPERATOR + str2);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
                bufferedWriter.append((CharSequence) str);
                bufferedWriter.newLine();
                bufferedWriter.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void appendLogmobiclean(String str, String str2, String str3) {
        File file = new File(Environment.getExternalStorageDirectory() + "/mobiclean.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.append((CharSequence) (str + " == " + str2 + IOUtils.LINE_SEPARATOR_UNIX));
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void appendLogmobicleanTest(String str, String str2, String str3) {
        if (GlobalData.isDebug) {
            File file = new File(Environment.getExternalStorageDirectory() + DialogConfigs.DIRECTORY_SEPERATOR + str3);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
                bufferedWriter.append((CharSequence) (str + " == " + str2 + IOUtils.LINE_SEPARATOR_UNIX));
                bufferedWriter.newLine();
                bufferedWriter.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void appendLogmobicleanbug(String str, String str2) {
        if (GlobalData.isDebug) {
            File file = new File(Environment.getExternalStorageDirectory() + "/bug.txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
                bufferedWriter.append((CharSequence) (str + " == " + str2 + IOUtils.LINE_SEPARATOR_UNIX));
                bufferedWriter.newLine();
                bufferedWriter.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static String capitalizeFirstLetter(String str) {
        String str2;
        try {
            str2 = str.substring(0, 1).toUpperCase() + str.substring(1);
        } catch (Exception e) {
            e.printStackTrace();
            str2 = null;
        }
        return str2 == null ? str : str2;
    }

    public static boolean checkEmail(String str) {
        return (str == null || TextUtils.isEmpty(str) || !Patterns.EMAIL_ADDRESS.matcher(str).matches()) ? false : true;
    }

    public static long checkTimeDifference(String str, String str2) {
        long time;
        long time2;
        if (str != null && str2 != null) {
            long parseLong = Long.parseLong(str);
            long parseLong2 = Long.parseLong(str2);
            if (parseLong >= 0 && parseLong2 >= 0) {
                Date date = new Date(parseLong);
                Date date2 = new Date(parseLong2);
                if (parseLong > parseLong2) {
                    time = date.getTime();
                    time2 = date2.getTime();
                } else {
                    time = date2.getTime();
                    time2 = date2.getTime();
                }
                return TimeUnit.SECONDS.toSeconds(time - time2);
            }
        }
        return 900000000L;
    }

    public static String convertBytes(long size) {
        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if(size < sizeMb)
            return df.format(size / sizeKb)+ " Kb";
        else if(size < sizeGb)
            return df.format(size / sizeMb) + " Mb";
        else if(size < sizeTerra)
            return df.format(size / sizeGb) + " Gb";

        return "";
    }

    public static String convertBytesJunk(long size) {
        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if(size < sizeMb)
            return df.format(size / sizeKb)+ " Kb";
        else if(size < sizeGb)
            return df.format(size / sizeMb) + " Mb";
        else if(size < sizeTerra)
            return df.format(size / sizeGb) + " Gb";

        return "";
    }

    public static String convertBytesSocial_only(long j) {
        if (j <= 0) {
            return AppEventsConstants.EVENT_PARAM_VALUE_NO;
        }
        double d2 = j;
        int log10 = (int) (Math.log10(d2) / Math.log10(1024.0d));
        FilesListActivity.exten_type = new String[]{"B", "KB", "MB", "GB", "TB"}[log10];
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###");
        return decimalFormat.format(d2 / Math.pow(1024.0d, log10));
    }

    public static String convertBytes_only(long size) {
        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if(size < sizeMb)
            return df.format(size / sizeKb);
        else if(size < sizeGb)
            return df.format(size / sizeMb);
        else if(size < sizeTerra)
            return df.format(size / sizeGb);

        return "";
    }

    public static String convertBytes_only_social(long j) {
        if (j <= 0) {
            return AppEventsConstants.EVENT_PARAM_VALUE_NO;
        }
        double d2 = j;
        int log10 = (int) (Math.log10(d2) / Math.log10(1024.0d));
        FilesListActivity.exten_type = new String[]{"B", "KB", "MB", "GB", "TB"}[log10];
        String format = new DecimalFormat("#,##0.#").format(d2 / Math.pow(1024.0d, log10));
        return format.contains(",") ? format.replace(",", "") : format;
    }

    public static String convertBytes_unit(long size) {
        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if(size < sizeMb)
            return " Kb";
        else if(size < sizeGb)
            return " Mb";
        else if(size < sizeTerra)
            return " Gb";

        return "";
    }

    @TargetApi(21)
    public static String convertToPath(Uri uri, Context context) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String str;
        String treeDocumentId = DocumentsContract.getTreeDocumentId(uri);
        if (treeDocumentId != null) {
            String[] split = treeDocumentId.split(":");
            String str2 = split.length > 0 ? split[0] : null;
            Map<String, String> secondaryMountedVolumesMap = getSecondaryMountedVolumesMap(context);
            String str3 = (secondaryMountedVolumesMap == null || str2 == null) ? null : secondaryMountedVolumesMap.get(str2);
            if (str3 != null) {
                if (split.length == 2) {
                    str = DialogConfigs.DIRECTORY_SEPERATOR + split[1];
                } else {
                    str = "";
                }
                return str3 + str;
            }
        }
        return null;
    }

    public static boolean copyFile(String str, String str2) {
        try {
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileInputStream fileInputStream = new FileInputStream(str);
            FileOutputStream fileOutputStream = new FileOutputStream(str2 + DialogConfigs.DIRECTORY_SEPERATOR + str.substring(str.lastIndexOf(DialogConfigs.DIRECTORY_SEPERATOR)));
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("EXCEPTIONNNNN", e.getMessage());
            return false;
        } catch (Exception e2) {
            Log.e("EXCEPTIONNNNN", e2.getMessage());
            return false;
        }
    }

    public static void deleteFileFromInternal(Context context, String str) {
        new File(context.getCacheDir(), str).delete();
    }

    public static int dpToPx(int i) {
        return (int) (i * Resources.getSystem().getDisplayMetrics().density);
    }

    public static double getBatteryCapacity(Context context) {
        Object obj;
        try {
            obj = Class.forName("com.android.internal.os.PowerProfile").getConstructor(Context.class).newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
            obj = null;
        }
        try {
            return ((Double) Class.forName("com.android.internal.os.PowerProfile").getMethod("getAveragePower", String.class).invoke(obj, "battery.capacity")).doubleValue();
        } catch (Exception e2) {
            e2.printStackTrace();
            return FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE;
        }
    }

    public static String getDurationBreakdown(long j, boolean z) {
        if (j <= 1000) {
            return "1 sec";
        }
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        long hours = timeUnit.toHours(j);
        TimeUnit timeUnit2 = TimeUnit.HOURS;
        long millis = j - timeUnit2.toMillis(hours);
        long hours2 = timeUnit.toHours(millis);
        long millis2 = millis - timeUnit2.toMillis(hours2);
        long minutes = timeUnit.toMinutes(millis2);
        long seconds = timeUnit.toSeconds(millis2 - TimeUnit.MINUTES.toMillis(minutes));
        if (z && minutes >= 30) {
            hours++;
        }
        StringBuilder sb = new StringBuilder();
        int i = (hours > 0L ? 1 : (hours == 0L ? 0 : -1));
        if (i > 0) {
            sb.append(hours);
            sb.append(" hr");
            if (hours > 1) {
                sb.append("s");
            }
        }
        int i2 = (hours2 > 0L ? 1 : (hours2 == 0L ? 0 : -1));
        if (i2 > 0) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(hours2);
            sb.append(" hr");
        }
        int i3 = (minutes > 0L ? 1 : (minutes == 0L ? 0 : -1));
        if (i3 > 0) {
            if (i2 > 0 || i > 0) {
                sb.append(" ");
            }
            if (i2 <= 0 && i <= 0) {
                sb.append(minutes);
                sb.append(" min");
            } else if (!z) {
                sb.append(minutes);
                sb.append(" min");
            }
        }
        if (i2 == 0 && i3 == 0 && seconds > 0) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(seconds);
            sb.append(" sec");
            if (seconds > 1) {
                sb.append("");
            }
        }
        return sb.toString();
    }

    public static List<ResolveInfo> getInstalledAppList(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        intent.addCategory("android.intent.category.LAUNCHER");
        return context.getPackageManager().queryIntentActivities(intent, 0);
    }

    public static String getMimeType(String str) {
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        if (fileExtensionFromUrl != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl);
        }
        return null;
    }

    public static Map<String, String> getSecondaryMountedVolumesMap(Context context) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Object[] objArr = (Object[]) storageManager.getClass().getMethod("getVolumeList", new Class[0]).invoke(storageManager, new Object[0]);
        HashMap hashMap = new HashMap();
        for (Object obj : objArr) {
            String str = (String) obj.getClass().getMethod("getState", new Class[0]).invoke(obj, new Object[0]);
            if (!((Boolean) obj.getClass().getMethod("isPrimary", new Class[0]).invoke(obj, new Object[0])).booleanValue() && str.equals("mounted")) {
                String str2 = (String) obj.getClass().getMethod("getPath", new Class[0]).invoke(obj, new Object[0]);
                String str3 = (String) obj.getClass().getMethod("getUuid", new Class[0]).invoke(obj, new Object[0]);
                if (str3 != null && str2 != null) {
                    hashMap.put(str3, str2);
                }
            }
        }
        return hashMap;
    }

    private static String getToday(String str) {
        return new SimpleDateFormat(str).format(new Date());
    }

    private static int getValueFromSeekbar(int i) {
        if (i < 8) {
            return 0;
        }
        if (i < 8 || i >= 16) {
            if (i < 16 || i >= 24) {
                if (i < 24 || i >= 32) {
                    if (i < 32 || i >= 40) {
                        if (i < 40 || i >= 48) {
                            if (i < 48 || i >= 56) {
                                if (i < 56 || i >= 64) {
                                    if (i < 64 || i >= 72) {
                                        if (i < 72 || i >= 80) {
                                            if (i < 80 || i >= 88) {
                                                if (i < 88 || i >= 96) {
                                                    if (i < 96 || i > 99) {
                                                        return i > 99 ? 13 : 0;
                                                    }
                                                    return 12;
                                                }
                                                return 11;
                                            }
                                            return 10;
                                        }
                                        return 9;
                                    }
                                    return 8;
                                }
                                return 7;
                            }
                            return 6;
                        }
                        return 5;
                    }
                    return 4;
                }
                return 3;
            }
            return 2;
        }
        return 1;
    }


    public static boolean hasFroyo() {
        return false;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= 9;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= 12;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= 16;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static boolean hasPermission(Context context, String... strArr) {
        if (Build.VERSION.SDK_INT < 23 || context == null || strArr == null) {
            return true;
        }
        for (String str : strArr) {
            if (ContextCompat.checkSelfPermission(context, str) != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAVFree(Context context) {
        if (isAdsFree(context)) {
            return true;
        }
        return MySharedPreference.isAVFree(context);
    }

    public static boolean isAdsFree(Context context) {
        return new SharedPrefUtil(context).getBoolean(SharedPrefUtil.ADS_FREE);
    }

    private static boolean isAppInstalled(String str, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static boolean isConnectingToInternet(Context context) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null && (allNetworkInfo = connectivityManager.getAllNetworkInfo()) != null) {
            for (NetworkInfo networkInfo : allNetworkInfo) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isExpire(String str) {
        if (!str.isEmpty() && !str.trim().equals("")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss a");
            String today = getToday("MMM-dd-yyyy hh:mm:ss a");
            try {
                Date parse = simpleDateFormat.parse(str);
                Date parse2 = simpleDateFormat.parse(today);
                if (parse2.compareTo(parse) < 0) {
                    return false;
                }
                if (parse.compareTo(parse2) == 0) {
                    if (parse.getTime() < parse2.getTime()) {
                        return false;
                    }
                    if (parse.getTime() == parse2.getTime()) {
                    }
                }
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isSplashAdShow(Context context) {
        return new SharedPrefUtil(context).getBoolean(SharedPrefUtil.SPLASH_AD_SHOW);
    }

    public static boolean isUpdateType(Context context) {
        return new SharedPrefUtil(context).getBoolean(SharedPrefUtil.UPDATE_TYPE);
    }

    public static boolean notInIgnoreList(String str) {
        String[] strArr = {"Facebook", "WhatsApp", "Amazon", "Twitter", "Instagram", "Linkedin", "Gmail", "Messenger", "Hike", "Hangouts", "Youtube", "Truecaller", "Google+"};
        for (int i = 0; i < 13; i++) {
            String str2 = strArr[i];
            if (str2.equalsIgnoreCase("" + str)) {
                return false;
            }
        }
        return true;
    }

    public static void openChromeCustomTabUrl(String str, Context context) {
        try {
            if (isAppInstalled("com.android.chrome", context.getPackageManager())) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorAccent));
                CustomTabsIntent build = builder.build();
                build.intent.setPackage("com.android.chrome");
                build.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                build.launchUrl(context, Uri.parse(str));
            } else {
                CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
                builder2.setToolbarColor(ContextCompat.getColor(context, R.color.colorAccent));
                CustomTabsIntent build2 = builder2.build();
                build2.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                build2.launchUrl(context, Uri.parse(str));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readFromFile(Context context, String str) {
        try {
            FileInputStream openFileInput = context.openFileInput(str);
            if (openFileInput == null) {
                return "";
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openFileInput));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                } else {
                    openFileInput.close();
                    return sb.toString();
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            return "";
        } catch (IOException e2) {
            Log.e("login activity", "Can not read file: " + e2.toString());
            return "";
        }
    }

    public static void saveFileToInternal(Context context, String str, String str2) {
        try {
            File file = new File(context.getCacheDir(), str);
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            Log.d("HEEEEE", "2222 " + file.getPath());
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
            dataOutputStream.write(str2.getBytes());
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static void saveImageToInternal(Context context, String str, Bitmap bitmap) {
        try {
            File file = new File(context.getFilesDir(), "imgad.jpg");
            if (bitmap == null) {
                return;
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void saveScreen(String str, Context context) {
        try {
            if (str.contains(".")) {
                str = str.substring(str.lastIndexOf(".") + 1, str.length());
            }
            String format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US).format(Calendar.getInstance().getTime());
            SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(context);
            sharedPrefUtil.saveString(SharedPrefUtil.LAST_SCREEN, "" + str);
            sharedPrefUtil.saveString(SharedPrefUtil.LAST_TIME_VISIT, "" + format);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setAdsFree(boolean z, Context context) {
        new SharedPrefUtil(context).saveBoolean(SharedPrefUtil.ADS_FREE, z);
    }

    public static void setSavedPreferences(Context context) {
        SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(context);
        try {
            if (sharedPrefUtil.getString("IMAGE_SETTING") != null) {
                GlobalData.imageCriteia = 0;
            }
            if (sharedPrefUtil.getString("VIDEO_SETTING") != null) {
                GlobalData.videoCriteia = 0;
            }
            if (sharedPrefUtil.getString("AUDIO_SETTING") != null) {
                GlobalData.audioCriteia = 0;
            }
            if (sharedPrefUtil.getString("FILE_SETTING") != null) {
                GlobalData.fileCriteia = 0;
            }
            if (sharedPrefUtil.getString("OTHER_SETTING") != null) {
                GlobalData.otherCriteia = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSplashAdShow(boolean z, Context context) {
        new SharedPrefUtil(context).saveBoolean(SharedPrefUtil.SPLASH_AD_SHOW, z);
    }

    public static void setUpdateType(boolean z, Context context) {
        new SharedPrefUtil(context).saveBoolean(SharedPrefUtil.UPDATE_TYPE, z);
    }

    public static boolean splashshow(Context context) {
        return new SharedPrefUtil(context).getBoolean(SharedPrefUtil.SPLASH_FIRST);
    }

    public static void writeInInternal(Context context, String str, String str2) {
        try {
            File file = new File(context.getCacheDir(), str);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
                bufferedWriter.append((CharSequence) str2);
                bufferedWriter.newLine();
                bufferedWriter.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static void writelog() {
    }
}
