package com.cleanPhone.mobileCleaner.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.exifinterface.media.ExifInterface;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.wrappers.BigSizeFilesWrapper;

import java.io.File;
import java.util.ArrayList;

public class LargeFile {
    public static int f5194a;
    private static final String[] textFileExtensions = {"pdf", "doc", "docx", "xls", "ppt", "odt", "rtf", "txt", "pptx", "htm", "html", "log", "csv", "dot", "dotx", "docm", "dotm", "xml", "mht", "dic", "xlsx", "msg", "mhtml", "pps", "xltx", "xlt", "xlsm", "xltm", "ppsx", "pptm", "ppsm"};
    private static final String[] textVideoImageAudio = {"mp4", "3gp", "avi", "mpeg", "jpeg", "jpg", "png", "gif", "mp3", "tiff", "tif", "bmp", "svg", "webp", "webm", "flv", "wmv", "f4v", "swf", "asf", "ts", "mkv", "pdf", "doc", "docx", "xls", "ppt", "odt", "rtf", "txt", "pptx", "htm", "html", "log", "csv", "dot", "dotx", "docm", "dotm", "xml", "mht", "dic", "xlsx", "msg", "mhtml", "pps", "xltx", "xlt", "xlsm", "xltm", "ppsx", "pptm", "ppsm","db", "ogg", "m4a"};
    public static long totalSocialSize;

    public static ArrayList<String> SocialFacebookImagesFetch(Context context, int i, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            String[] strArr = {"_id", "_data", "_display_name", "_size"};
            String[] strArr2 = {(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Facebook") + "%"};
            Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, "_data like ?", strArr2, str + " DESC");
            if (str.equalsIgnoreCase("_display_name")) {
                query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, "_data like ?", strArr2, str);
            }
            if (query != null) {
                int columnIndex = query.getColumnIndex("_data");
                int columnIndex2 = query.getColumnIndex("_size");
                int columnIndex3 = query.getColumnIndex("_display_name");
                int columnIndex4 = query.getColumnIndex("_id");
                for (int i2 = 0; i2 < query.getCount(); i2++) {
                    query.moveToPosition(i2);
                    String string = query.getString(columnIndex);
                    long j = query.getLong(columnIndex2);
                    String string2 = query.getString(columnIndex3);
                    int i3 = query.getInt(columnIndex4);
                    Log.d("CRITERIA", GlobalData.imageCriteia + "  " + GlobalData.fromSocialCleaning);
                    if (j >= GlobalData.imageCriteia * 1048576 || GlobalData.fromSocialCleaning) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                        bigSizeFilesWrapper.name = string2;
                        bigSizeFilesWrapper.id = i3;
                        bigSizeFilesWrapper.path = string;
                        bigSizeFilesWrapper.size = j;
                        bigSizeFilesWrapper.type = "images";
                        if (string.toLowerCase().contains("facebook") || string.toLowerCase().contains("twitter") || string.toLowerCase().contains("whatsapp")) {
                            totalSocialSize += j;
                            SocialCleanerListActivity.fbimagelist.add(bigSizeFilesWrapper);
                        }
                    }
                }
            }
            query.close();
            Log.e("facebook image : ", "" + totalSocialSize);
            SocialTwitterImagesFetch(context, i, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static ArrayList<String> SocialProfileImagesFetch(Context context, int i, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            String[] strArr = {"_id", "_data", "_display_name", "_size"};
            String[] strArr2 = {(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Profile Photos") + "%"};
            Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, "_data like ?", strArr2, str + " DESC");
            if (str.equalsIgnoreCase("_display_name")) {
                query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, "_data like ?", strArr2, str);
            }
            if (query != null) {
                int columnIndex = query.getColumnIndex("_data");
                int columnIndex2 = query.getColumnIndex("_size");
                int columnIndex3 = query.getColumnIndex("_display_name");
                int columnIndex4 = query.getColumnIndex("_id");
                for (int i2 = 0; i2 < query.getCount(); i2++) {
                    query.moveToPosition(i2);
                    String string = query.getString(columnIndex);
                    long j = query.getLong(columnIndex2);
                    String string2 = query.getString(columnIndex3);
                    int i3 = query.getInt(columnIndex4);
                    Log.d("CRITERIA", GlobalData.imageCriteia + "  " + GlobalData.fromSocialCleaning);
                    if (j >= GlobalData.imageCriteia * 1048576 || GlobalData.fromSocialCleaning) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                        bigSizeFilesWrapper.name = string2;
                        bigSizeFilesWrapper.id = i3;
                        bigSizeFilesWrapper.path = string;
                        bigSizeFilesWrapper.size = j;
                        bigSizeFilesWrapper.type = "images";
                        if (string.toLowerCase().contains("facebook") || string.toLowerCase().contains("twitter") || string.toLowerCase().contains("whatsapp")) {
                            totalSocialSize += j;
                            SocialCleanerListActivity.whimagelist.add(bigSizeFilesWrapper);
                        }
                    }
                }
            }
            Log.e("wh pro Image", "" + SocialCleanerListActivity.whimagelist.size());
            query.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static ArrayList<String> SocialSentAudioFetch(Context context, int i, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (str.equalsIgnoreCase("1")) {
            searchSentAudioItemFiles(str, Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Audio/Sent", context);
        } else if (str.equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
            searchSentAudioItemFiles(str, Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Voice Notes", context);
        }
        return arrayList;
    }

    public static ArrayList<String> SocialSentImagesFetch(Context context, int i, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (str.equalsIgnoreCase("1")) {
            searchSentItemFiles(str, Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images/Sent", context);
            SocialFacebookImagesFetch(context, i, str);
        } else if (str.equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
            searchSentItemFiles(str, Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video/Sent", context);
        }
        return arrayList;
    }

    public static ArrayList<String> SocialTwitterImagesFetch(Context context, int i, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            String[] strArr = {"_id", "_data", "_display_name", "_size"};
            String[] strArr2 = {(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Twitter") + "%"};
            Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, "_data like ?", strArr2, str + " DESC");
            if (str.equalsIgnoreCase("_display_name")) {
                query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, "_data like ?", strArr2, str);
            }
            if (query != null) {
                int columnIndex = query.getColumnIndex("_data");
                int columnIndex2 = query.getColumnIndex("_size");
                int columnIndex3 = query.getColumnIndex("_display_name");
                int columnIndex4 = query.getColumnIndex("_id");
                for (int i2 = 0; i2 < query.getCount(); i2++) {
                    query.moveToPosition(i2);
                    String string = query.getString(columnIndex);
                    long j = query.getLong(columnIndex2);
                    String string2 = query.getString(columnIndex3);
                    int i3 = query.getInt(columnIndex4);
                    Log.d("CRITERIA", GlobalData.imageCriteia + "  " + GlobalData.fromSocialCleaning);
                    if (j >= GlobalData.imageCriteia * 1048576 || GlobalData.fromSocialCleaning) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                        bigSizeFilesWrapper.name = string2;
                        bigSizeFilesWrapper.id = i3;
                        bigSizeFilesWrapper.path = string;
                        bigSizeFilesWrapper.size = j;
                        bigSizeFilesWrapper.type = "images";
                        if (string.toLowerCase().contains("facebook") || string.toLowerCase().contains("twitter") || string.toLowerCase().contains("whatsapp")) {
                            totalSocialSize += j;
                            SocialCleanerListActivity.twimagelist.add(bigSizeFilesWrapper);
                        }
                    }
                }
            }
            query.close();
            SocialProfileImagesFetch(context, i, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    private static boolean checkInArray(String str) {
        int i = 0;
        while (true) {
            String[] strArr = textFileExtensions;
            if (i >= strArr.length) {
                return false;
            }
            if (str.equalsIgnoreCase(strArr[i])) {
                return true;
            }
            i++;
        }
    }

    public static ArrayList<String> fetchLargeSizeAudio(Context context, int i, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            String[] strArr = {"_id", "_data", "_display_name", "_size"};
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor query = contentResolver.query(uri, strArr, null, null, str + " DESC");
            if (str.equalsIgnoreCase("_display_name")) {
                query = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, strArr, null, null, str);
            }
            int columnIndex = query.getColumnIndex("_data");
            int columnIndex2 = query.getColumnIndex("_size");
            int columnIndex3 = query.getColumnIndex("_display_name");
            int columnIndex4 = query.getColumnIndex("_id");
            if (query != null) {
                for (int i2 = 0; i2 < query.getCount(); i2++) {
                    query.moveToPosition(i2);
                    String string = query.getString(columnIndex);
                    long j = query.getLong(columnIndex2);
                    String string2 = query.getString(columnIndex3);
                    int i3 = query.getInt(columnIndex4);
                    if (j >= GlobalData.audioCriteia * 1048576 || GlobalData.fromSocialCleaning) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                        bigSizeFilesWrapper.name = string2;
                        bigSizeFilesWrapper.id = i3;
                        bigSizeFilesWrapper.path = string;
                        bigSizeFilesWrapper.size = j;
                        bigSizeFilesWrapper.type = "audios";
                    }
                }
            }
            query.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static ArrayList<String> fetchLargeSizeFiles(Context context, int i, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            String[] strArr = {"_id", "_data", "_display_name", "_size"};
            ContentResolver contentResolver = context.getContentResolver();
            Uri contentUri = MediaStore.Files.getContentUri("external");
            Cursor query = contentResolver.query(contentUri, strArr, null, null, str + " DESC");
            if (str.equalsIgnoreCase("_display_name")) {
                query = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), strArr, null, null, str);
            }
            if (query != null) {
                int columnIndex = query.getColumnIndex("_data");
                int columnIndex2 = query.getColumnIndex("_size");
                int columnIndex3 = query.getColumnIndex("_id");
                for (int i2 = 0; i2 < query.getCount(); i2++) {
                    query.moveToPosition(i2);
                    String string = query.getString(columnIndex);
                    long j = query.getLong(columnIndex2);
                    int i3 = query.getInt(columnIndex3);
                    String substring = string.substring(string.lastIndexOf(DialogConfigs.DIRECTORY_SEPERATOR) + 1);
                    if (j >= GlobalData.fileCriteia * 1024 || GlobalData.fromSocialCleaning) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                        bigSizeFilesWrapper.ext = FilenameUtils.getExtension(string);
                        bigSizeFilesWrapper.name = substring;
                        bigSizeFilesWrapper.id = i3;
                        bigSizeFilesWrapper.path = string;
                        bigSizeFilesWrapper.size = j;
                        File file = new File(string);
                        if (checkInArray(bigSizeFilesWrapper.ext) && file.exists() && !file.isHidden() && !file.getPath().contains("/.")) {
                            bigSizeFilesWrapper.type = "files";
                        }
                    }
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static ArrayList<String> fetchLargeSizeFilesOthers(Context context, int i, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            String[] strArr = {"_id", "_data", "_display_name", "_size"};
            ContentResolver contentResolver = context.getContentResolver();
            Uri contentUri = MediaStore.Files.getContentUri("external");
            Cursor query = contentResolver.query(contentUri, strArr, null, null, str + " DESC");
            if (str.equalsIgnoreCase("_display_name")) {
                query = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), strArr, null, null, str);
            }
            if (query != null) {
                int columnIndex = query.getColumnIndex("_data");
                int columnIndex2 = query.getColumnIndex("_size");
                query.getColumnIndex("_display_name");
                int columnIndex3 = query.getColumnIndex("_id");
                for (int i2 = 0; i2 < query.getCount(); i2++) {
                    query.moveToPosition(i2);
                    String string = query.getString(columnIndex);
                    long j = query.getLong(columnIndex2);
                    int i3 = query.getInt(columnIndex3);
                    String substring = string.substring(string.lastIndexOf(DialogConfigs.DIRECTORY_SEPERATOR) + 1);
                    if (j >= GlobalData.otherCriteia * 1048576 || GlobalData.fromSocialCleaning) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                        String extension = FilenameUtils.getExtension(string);
                        bigSizeFilesWrapper.ext = extension;
                        bigSizeFilesWrapper.name = substring;
                        bigSizeFilesWrapper.id = i3;
                        bigSizeFilesWrapper.path = string;
                        bigSizeFilesWrapper.size = j;
                        if (!notAfile(extension)) {
                            File file = new File(string);
                            if (file.exists() && !file.isDirectory() && !file.isHidden() && !file.getPath().contains("/.")) {
                                bigSizeFilesWrapper.type = "others";
                            }
                        }
                    }
                }
            }
            query.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static ArrayList<String> fetchLargeSizeImages(Context context, int i, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            String[] strArr = {"_id", "_data", "_display_name", "_size"};
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor query = contentResolver.query(uri, strArr, null, null, str + " DESC");
            if (str.equalsIgnoreCase("_display_name")) {
                query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, null, null, str);
            }
            if (query != null) {
                int columnIndex = query.getColumnIndex("_data");
                int columnIndex2 = query.getColumnIndex("_size");
                int columnIndex3 = query.getColumnIndex("_display_name");
                int columnIndex4 = query.getColumnIndex("_id");
                for (int i2 = 0; i2 < query.getCount(); i2++) {
                    query.moveToPosition(i2);
                    String string = query.getString(columnIndex);
                    long j = query.getLong(columnIndex2);
                    String string2 = query.getString(columnIndex3);
                    int i3 = query.getInt(columnIndex4);
                    if (j >= GlobalData.imageCriteia * 1048576 || GlobalData.fromSocialCleaning) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                        bigSizeFilesWrapper.name = string2;
                        bigSizeFilesWrapper.id = i3;
                        bigSizeFilesWrapper.path = string;
                        bigSizeFilesWrapper.size = j;
                        bigSizeFilesWrapper.type = "images";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static ArrayList<String> fetchLargeSizeVideos(Context context, int i, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            String[] strArr = {"_id", "_data", "_display_name", "_size"};
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            Cursor query = contentResolver.query(uri, strArr, null, null, str + " DESC");
            if (str.equalsIgnoreCase("_display_name")) {
                query = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr, null, null, str);
            }
            int columnIndex = query.getColumnIndex("_data");
            int columnIndex2 = query.getColumnIndex("_size");
            int columnIndex3 = query.getColumnIndex("_display_name");
            int columnIndex4 = query.getColumnIndex("_id");
            if (query != null) {
                for (int i2 = 0; i2 < query.getCount(); i2++) {
                    query.moveToPosition(i2);
                    String string = query.getString(columnIndex);
                    long j = query.getLong(columnIndex2);
                    String string2 = query.getString(columnIndex3);
                    int i3 = query.getInt(columnIndex4);
                    if (j >= GlobalData.videoCriteia * 1048576 || GlobalData.fromSocialCleaning) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                        bigSizeFilesWrapper.name = string2;
                        bigSizeFilesWrapper.id = i3;
                        bigSizeFilesWrapper.path = string;
                        bigSizeFilesWrapper.size = j;
                        bigSizeFilesWrapper.type = "videos";
                    }
                }
            }
            query.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static Cursor getAudioWhatsApp(String str, Context context, String str2, String[] strArr) {
        String[] strArr2 = {(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Audio") + "%"};
        return str2.equalsIgnoreCase("_display_name") ? context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, strArr, "_data like ?", strArr2, str2) : context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, strArr, "_data like ?", strArr2, str2 + " DESC");
    }

    public static Cursor getDocWhatsApp(String str, Context context, String str2, String[] strArr) {
        String[] strArr2 = {(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Documents") + "%"};
        return context.getContentResolver().query(MediaStore.Files.getContentUri("external"), strArr, "media_type=0", strArr2, str2 + " DESC");
    }

    private static boolean notAfile(String str) {
        int i = 0;
        while (true) {
            String[] strArr = textVideoImageAudio;
            if (i >= strArr.length) {
                return false;
            }
            if (str.equalsIgnoreCase(strArr[i])) {
                return true;
            }
            i++;
        }
    }

    private static void searchSentAudioItemFiles(String str, String str2, Context context) {
        File[] listFiles;
        File file = new File(str2);
        if (!file.exists() || (listFiles = file.listFiles()) == null) {
            return;
        }
        if (listFiles.length != 0 || file.isHidden() || file.getPath().contains("/.")) {
            for (File file2 : listFiles) {
                String extension = FilenameUtils.getExtension(file2.getPath());
                if (str.equalsIgnoreCase("1")) {
                    if (file2.isFile() && extension.equalsIgnoreCase("mp3")) {
                        if (!file2.getPath().contains("" + GlobalData.backuppath)) {
                            BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                            f5194a++;
                            bigSizeFilesWrapper.name = file2.getName();
                            bigSizeFilesWrapper.path = file2.getAbsolutePath();
                            bigSizeFilesWrapper.size = file2.length();
                            bigSizeFilesWrapper.type = "audio";
                            if (str2.toLowerCase().contains("facebook") || str2.toLowerCase().contains("twitter") || str2.toLowerCase().contains("whatsapp")) {
                                totalSocialSize += file2.length();
                                SocialCleanerListActivity.whaudioslist.add(bigSizeFilesWrapper);
                            }
                        }
                    }
                    if (file2.isDirectory()) {
                        searchSentAudioItemFiles(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Audio/Sent", file2.getAbsolutePath(), context);
                    }
                } else {
                    if (file2.isFile() && extension.equalsIgnoreCase("aac")) {
                        if (!file2.getPath().contains("" + GlobalData.backuppath)) {
                            BigSizeFilesWrapper bigSizeFilesWrapper2 = new BigSizeFilesWrapper();
                            f5194a++;
                            bigSizeFilesWrapper2.name = file2.getName();
                            bigSizeFilesWrapper2.path = file2.getAbsolutePath();
                            bigSizeFilesWrapper2.size = file2.length();
                            bigSizeFilesWrapper2.type = "audios";
                            if (str2.toLowerCase().contains("facebook") || str2.toLowerCase().contains("twitter") || str2.toLowerCase().contains("whatsapp")) {
                                totalSocialSize += file2.length();
                                SocialCleanerListActivity.whaudioslist.add(bigSizeFilesWrapper2);
                            }
                            Log.d("CHECKPATH", "" + file2.getName());
                        }
                    }
                    if (file2.isDirectory()) {
                        searchSentAudioItemFiles(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Voice Notes", file2.getPath(), context);
                    }
                }
            }
            Log.e("whats Audio : ", "" + totalSocialSize);
        }
    }

    private static void searchSentDocFiles(String str, String str2, Context context) {
        File[] listFiles;
        File file = new File(str2);
        if (!file.exists() || (listFiles = file.listFiles()) == null) {
            return;
        }
        if (listFiles.length != 0 || file.isHidden() || file.getPath().contains("/.")) {
            for (File file2 : listFiles) {
                String extension = FilenameUtils.getExtension(file2.getPath());
                if (str.equalsIgnoreCase("1")) {
                    if (file2.isFile() && (extension.equalsIgnoreCase("docx") || extension.equalsIgnoreCase("pdf"))) {
                        if (!file2.getPath().contains("" + GlobalData.backuppath)) {
                            BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                            f5194a++;
                            bigSizeFilesWrapper.name = file2.getName();
                            bigSizeFilesWrapper.path = file2.getAbsolutePath();
                            bigSizeFilesWrapper.size = file2.length();
                            bigSizeFilesWrapper.type = "docs";
                            if (str2.toLowerCase().contains("facebook") || str2.toLowerCase().contains("twitter") || str2.toLowerCase().contains("whatsapp")) {
                                totalSocialSize += file2.length();
                                SocialCleanerListActivity.whdoclist.add(bigSizeFilesWrapper);
                            }
                        }
                    }
                    if (file2.isDirectory()) {
                        searchSentItemFiles(str, file2.getAbsolutePath(), context);
                    }
                }
            }
        }
    }

    private static void searchSentItemFiles(String str, String str2, Context context) {
        File[] listFiles;
        File file = new File(str2);
        if (!file.exists() || (listFiles = file.listFiles()) == null) {
            return;
        }
        if (listFiles.length != 0 || file.isHidden() || file.getPath().contains("/.")) {
            for (File file2 : listFiles) {
                String extension = FilenameUtils.getExtension(file2.getPath());
                if (str.equalsIgnoreCase("1")) {
                    if (file2.isFile() && (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg"))) {
                        if (!file2.getPath().contains("" + GlobalData.backuppath)) {
                            BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                            f5194a++;
                            bigSizeFilesWrapper.name = file2.getName();
                            bigSizeFilesWrapper.path = file2.getAbsolutePath();
                            bigSizeFilesWrapper.size = file2.length();
                            bigSizeFilesWrapper.type = "images";
                            totalSocialSize += file2.length();
                            SocialCleanerListActivity.whimagelist.add(bigSizeFilesWrapper);
                            Log.e("wh sent Image", "" + SocialCleanerListActivity.whimagelist.size());
                        }
                    }
                    if (file2.isDirectory()) {
                        searchSentItemFiles(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images/Sent", file2.getAbsolutePath(), context);
                    }
                } else {
                    if (file2.isFile() && extension.equalsIgnoreCase("mp4")) {
                        if (!file2.getPath().contains("" + GlobalData.backuppath)) {
                            BigSizeFilesWrapper bigSizeFilesWrapper2 = new BigSizeFilesWrapper();
                            f5194a++;
                            bigSizeFilesWrapper2.name = file2.getName();
                            bigSizeFilesWrapper2.path = file2.getAbsolutePath();
                            bigSizeFilesWrapper2.size = file2.length();
                            bigSizeFilesWrapper2.type = "videos";
                            totalSocialSize += file2.length();
                            SocialCleanerListActivity.whvideolist.add(bigSizeFilesWrapper2);
                            Log.d("CHECKPATH", "" + file2.getName());
                        }
                    }
                    if (file2.isDirectory()) {
                        searchSentItemFiles(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video/Sent", file2.getPath(), context);
                    }
                }
            }
        }
    }
}
