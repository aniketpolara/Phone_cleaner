package com.cleanPhone.mobileCleaner.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.antimalware.Crc64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;

public final class FileUtil {
    private static final String PRIMARY_VOLUME_NAME = "primary";

    private FileUtil() {
        throw new UnsupportedOperationException();
    }

    public static boolean IsDeletionBelow6() {
        return Build.VERSION.SDK_INT <= 21;
    }

    public static long convertCRC64(String str) {
        if (str == null) {
            return 0L;
        }
        return Crc64.hashByAlgo1(str.toLowerCase().getBytes());
    }

    @RequiresApi(api = 21)
    public static boolean deleteFile(Context context, @NonNull File file) {
        if (file.delete()) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            DocumentFile documentFile = getDocumentFile(context, file, false, true);
            return documentFile != null && documentFile.delete();
        }
        return !file.exists();
    }

    public static char[] genrateCPlusChar(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            sb.append(str.charAt(i));
            sb.append("\u0000");
        }
        return sb.toString().toCharArray();
    }

    @RequiresApi(api = 21)
    private static DocumentFile getDocumentFile(Context context, @NonNull File file, boolean z, boolean z2) {
        Uri[] treeUris = getTreeUris(context);
        if (treeUris.length == 0) {
            return null;
        }
        try {
            String canonicalPath = file.getCanonicalPath();
            String str = null;
            Uri uri = null;
            for (int i = 0; str == null && i < treeUris.length; i++) {
                String fullPathFromTreeUri = getFullPathFromTreeUri(treeUris[i], context);
                if (fullPathFromTreeUri != null && canonicalPath.startsWith(fullPathFromTreeUri)) {
                    uri = treeUris[i];
                    str = fullPathFromTreeUri;
                }
            }
            if (str == null) {
                uri = treeUris[0];
                str = getExtSdCardFolder(context, file);
            }
            if (str == null) {
                return null;
            }
            String substring = canonicalPath.substring(str.length() + 1);
            DocumentFile fromTreeUri = DocumentFile.fromTreeUri(context, uri);
            String[] split = substring.split("\\/");
            for (int i2 = 0; i2 < split.length; i2++) {
                DocumentFile findFile = fromTreeUri.findFile(split[i2]);
                if (findFile != null) {
                    fromTreeUri = findFile;
                } else if (i2 < split.length - 1) {
                    if (!z2) {
                        return null;
                    }
                    fromTreeUri = fromTreeUri.createDirectory(split[i2]);
                } else if (z) {
                    fromTreeUri = fromTreeUri.createDirectory(split[i2]);
                } else {
                    fromTreeUri = fromTreeUri.createFile("image", split[i2]);
                }
            }
            return fromTreeUri;
        } catch (IOException unused) {
            return null;
        }
    }

    @RequiresApi(21)
    private static String getDocumentPathFromTreeUri(Uri uri) {
        String[] split = DocumentsContract.getTreeDocumentId(uri).split(":");
        if (split.length >= 2 && split[1] != null) {
            return split[1];
        }
        return File.separator;
    }

    @RequiresApi(19)
    public static String getExtSdCardFolder(Context context, @NonNull File file) {
        String[] extSdCardPaths;
        try {
            for (String str : getExtSdCardPaths(context)) {
                if (file.getCanonicalPath().startsWith(str)) {
                    return str;
                }
            }
        } catch (IOException unused) {
        }
        return null;
    }

    @RequiresApi(19)
    private static String[] getExtSdCardPaths(Context context) {
        File[] externalFilesDirs;
        ArrayList arrayList = new ArrayList();
        for (File file : context.getExternalFilesDirs("external")) {
            if (file != null && !file.equals(context.getExternalFilesDir("external"))) {
                int lastIndexOf = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (lastIndexOf < 0) {
                    Log.w("FileUtil", "Unexpected external file dir: " + file.getAbsolutePath());
                } else {
                    String substring = file.getAbsolutePath().substring(0, lastIndexOf);
                    try {
                        substring = new File(substring).getCanonicalPath();
                    } catch (IOException unused) {
                    }
                    arrayList.add(substring);
                }
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    @Nullable
    @RequiresApi(api = 21)
    private static String getFullPathFromTreeUri(@Nullable Uri uri, Context context) {
        if (uri == null) {
            return null;
        }
        String volumePath = getVolumePath(getVolumeIdFromTreeUri(uri), context);
        if (volumePath == null) {
            return File.separator;
        }
        String str = File.separator;
        if (volumePath.endsWith(str)) {
            volumePath = volumePath.substring(0, volumePath.length() - 1);
        }
        String documentPathFromTreeUri = getDocumentPathFromTreeUri(uri);
        if (documentPathFromTreeUri.endsWith(str)) {
            documentPathFromTreeUri = documentPathFromTreeUri.substring(0, documentPathFromTreeUri.length() - 1);
        }
        if (documentPathFromTreeUri.length() > 0) {
            if (documentPathFromTreeUri.startsWith(str)) {
                return volumePath + documentPathFromTreeUri;
            }
            return volumePath + str + documentPathFromTreeUri;
        }
        return volumePath;
    }

    @NonNull
    public static String getSdCardPath() {
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            return new File(absolutePath).getCanonicalPath();
        } catch (IOException e) {
            Log.e("FileUtil", "Could not get SD directory", e);
            return absolutePath;
        }
    }

    public static Uri getSharedPreferenceUri(int i, Context context) {
        String string = getSharedPreferences(context).getString(context.getString(i), null);
        if (string == null) {
            return null;
        }
        return Uri.parse(string);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Uri[] getTreeUris(Context context) {
        ArrayList arrayList = new ArrayList();
        Uri sharedPreferenceUri = getSharedPreferenceUri(R.string.mbc_key_internal_uri_extsdcard_photos, context);
        if (sharedPreferenceUri != null) {
            arrayList.add(sharedPreferenceUri);
        }
        Uri sharedPreferenceUri2 = getSharedPreferenceUri(R.string.mbc_key_internal_uri_extsdcard_input, context);
        if (sharedPreferenceUri2 != null) {
            arrayList.add(sharedPreferenceUri2);
        }
        return (Uri[]) arrayList.toArray(new Uri[arrayList.size()]);
    }

    @RequiresApi(21)
    private static String getVolumeIdFromTreeUri(Uri uri) {
        String[] split = DocumentsContract.getTreeDocumentId(uri).split(":");
        if (split.length > 0) {
            return split[0];
        }
        return null;
    }

    private static String getVolumePath(String str, Context context) {
        if (Build.VERSION.SDK_INT < 21) {
            return null;
        }
        try {
            StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Class<?> cls = Class.forName("android.os.storage.StorageVolume");
            Method method = storageManager.getClass().getMethod("getVolumeList", new Class[0]);
            Method method2 = cls.getMethod("getUuid", new Class[0]);
            Method method3 = cls.getMethod("getPath", new Class[0]);
            Method method4 = cls.getMethod("isPrimary", new Class[0]);
            Object invoke = method.invoke(storageManager, new Object[0]);
            int length = Array.getLength(invoke);
            for (int i = 0; i < length; i++) {
                Object obj = Array.get(invoke, i);
                String str2 = (String) method2.invoke(obj, new Object[0]);
                if (((Boolean) method4.invoke(obj, new Object[0])).booleanValue() && PRIMARY_VOLUME_NAME.equals(str)) {
                    return (String) method3.invoke(obj, new Object[0]);
                }
                if (str2 != null && str2.equals(str)) {
                    return (String) method3.invoke(obj, new Object[0]);
                }
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public static boolean isKitKat() {
        return Build.VERSION.SDK_INT == 19;
    }

    @RequiresApi(19)
    public static boolean isOnExtSdCard(Context context, @NonNull File file) {
        return getExtSdCardFolder(context, file) != null;
    }

    public static boolean isSystemAndroid5() {
        return Build.VERSION.SDK_INT >= 21;
    }

    public static boolean isWritable(@NonNull File file) {
        boolean exists = file.exists();
        try {
            new FileOutputStream(file, true).close();
        } catch (IOException unused) {
        }
        boolean canWrite = file.canWrite();
        if (!exists) {
            file.delete();
        }
        return canWrite;
    }

    @RequiresApi(api = 21)
    public static boolean isWritableNormalOrSaf(Context context, @Nullable File file) {
        File file2;
        Util.appendLogmobiclean("IsWritable", "method isWritableNormalOrSaf", GlobalData.FILE_NAME);
        boolean z = false;
        if (file != null && file.exists() && file.isDirectory()) {
            int i = 0;
            do {
                StringBuilder sb = new StringBuilder();
                sb.append("AugendiagnoseDummyFile");
                i++;
                sb.append(i);
                file2 = new File(file, sb.toString());
            } while (file2.exists());
            if (isWritable(file2)) {
                return true;
            }
            try {
                DocumentFile documentFile = getDocumentFile(context, file2, false, false);
                if (documentFile == null) {
                    return false;
                }
                if (documentFile.canWrite() && file2.exists()) {
                    z = true;
                }
                documentFile.delete();
            } catch (Exception unused) {
            }
        }
        return z;
    }
}
