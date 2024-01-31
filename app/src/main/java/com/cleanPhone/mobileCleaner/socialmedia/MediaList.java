package com.cleanPhone.mobileCleaner.socialmedia;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.cleanPhone.mobileCleaner.SocialAnimationActivity;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.BigSizeFilesWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MediaList {
    public String TAG = "MediaList";
    public ArrayList<BigSizeFilesWrapper> arrContents;
    private Context cntx;
    public ArrayList<BigSizeFilesWrapper> filesList;
    public SocialAnimationActivity.FileTypes mediaType;
    private int recoveredCount;
    public long recoveredSize;
    public int selectedCount;
    public long selectedSize;
    public String title;
    public int totalCount;
    public long totalSize;


    public MediaList(Context context, String str, SocialAnimationActivity.FileTypes fileTypes) {
        Util.appendLogmobiclean("MediaList", " Constractor  MediaList with 5 perameter  ", GlobalData.FILE_NAME);
        this.title = str;
        this.cntx = context;
        this.mediaType = fileTypes;
        this.arrContents = new ArrayList<>();
        this.filesList = new ArrayList<>();
    }

    private void addChild(BigSizeFilesWrapper bigSizeFilesWrapper) {
        this.totalCount++;
        this.totalSize += bigSizeFilesWrapper.size;
        this.arrContents.add(bigSizeFilesWrapper);
    }

    public void deleteNode(BigSizeFilesWrapper bigSizeFilesWrapper) {
        Util.appendLogmobiclean(this.TAG, " method deleteNode  ", GlobalData.FILE_NAME);
        this.selectedCount--;
        long j = this.selectedSize;
        long j2 = bigSizeFilesWrapper.size;
        this.selectedSize = j - j2;
        this.totalCount--;
        this.totalSize -= j2;
        this.recoveredCount++;
        this.recoveredSize += j2;
    }

    public void fetchAPK(long j, HashMap<String, Boolean> hashMap) {
        try {
            ContentResolver contentResolver = this.cntx.getContentResolver();
            Uri contentUri = MediaStore.Files.getContentUri("external");
            Cursor query = contentResolver.query(contentUri, new String[]{"_id", "_data", "_display_name", "_size", "date_added"}, null, null, "_size DESC");
            if (query != null) {
                int columnIndex = query.getColumnIndex("_data");
                int columnIndex2 = query.getColumnIndex("_size");
                query.getColumnIndex("_display_name");
                int columnIndex3 = query.getColumnIndex("_id");
                int columnIndex4 = query.getColumnIndex("date_added");
                for (int i = 0; i < query.getCount(); i++) {
                    query.moveToPosition(i);
                    String string = query.getString(columnIndex);
                    if (!string.contains("" + GlobalData.backuppath)) {
                        long j2 = query.getLong(columnIndex2);
                        int i2 = query.getInt(columnIndex3);
                        long j3 = query.getLong(columnIndex4);
                        String substring = string.substring(string.lastIndexOf(DialogConfigs.DIRECTORY_SEPERATOR) + 1);
                        if (TextUtils.isEmpty(substring)) {
                            substring = new File(string).getName();
                        }
                        if (j2 >= j) {
                            BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                            String extension = FilenameUtils.getExtension(string);
                            bigSizeFilesWrapper.ext = extension;
                            bigSizeFilesWrapper.name = substring;
                            bigSizeFilesWrapper.id = i2;
                            bigSizeFilesWrapper.path = string;
                            bigSizeFilesWrapper.size = j2;
                            bigSizeFilesWrapper.dateTaken = j3;
                            Boolean bool = hashMap.get(extension.toLowerCase());
                            if (bool == null) {
                                bool = Boolean.FALSE;
                            }
                            if (bool.booleanValue()) {
                                File file = new File(string);
                                if (file.exists() && !file.isDirectory() && !file.isHidden() && !file.getPath().contains("/.")) {
                                    addChild(bigSizeFilesWrapper);
                                }
                            }
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
    }

    public void fetchAllFiles(ArrayList<BigSizeFilesWrapper> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            addChild(arrayList.get(i));
        }
    }

    public void fetchAudio() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        try {
            Cursor query = this.cntx.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data", "_display_name", "_size", "date_added"}, null, null, "_size DESC");
            int i6 = 0;
            if (query != null) {
                i4 = query.getColumnIndex("_data");
                i2 = query.getColumnIndex("_size");
                i3 = query.getColumnIndex("_display_name");
                i5 = query.getColumnIndex("_id");
                i = query.getColumnIndex("date_added");
            } else {
                i = 0;
                i2 = 0;
                i3 = 0;
                i4 = 0;
                i5 = 0;
            }
            long j = 0;
            if (query != null) {
                while (i6 < query.getCount()) {
                    query.moveToPosition(i6);
                    String string = query.getString(i4);
                    long j2 = query.getLong(i2);
                    String string2 = query.getString(i3);
                    if (TextUtils.isEmpty(string2)) {
                        string2 = new File(string).getName();
                    }
                    int i7 = query.getInt(i5);
                    int i8 = i2;
                    int i9 = i3;
                    long j3 = query.getLong(i);
                    if (j2 >= j) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                        bigSizeFilesWrapper.name = string2;
                        bigSizeFilesWrapper.id = i7;
                        bigSizeFilesWrapper.path = string;
                        bigSizeFilesWrapper.size = j2;
                        bigSizeFilesWrapper.dateTaken = j3;
                        addChild(bigSizeFilesWrapper);
                    }
                    i6++;
                    i2 = i8;
                    i3 = i9;
                    j = 0;
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchFilesForExtns(long j, HashMap<String, Boolean> hashMap) {
        try {
            Cursor query = this.cntx.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id", "_data", "_display_name", "_size", "date_added"}, null, null, "_size DESC");
            if (query != null) {
                int columnIndex = query.getColumnIndex("_data");
                int columnIndex2 = query.getColumnIndex("_size");
                query.getColumnIndex("_display_name");
                int columnIndex3 = query.getColumnIndex("_id");
                int columnIndex4 = query.getColumnIndex("date_added");
                for (int i = 0; i < query.getCount(); i++) {
                    query.moveToPosition(i);
                    String string = query.getString(columnIndex);
                    long j2 = query.getLong(columnIndex2);
                    int i2 = query.getInt(columnIndex3);
                    long j3 = query.getLong(columnIndex4);
                    String substring = string.substring(string.lastIndexOf(DialogConfigs.DIRECTORY_SEPERATOR) + 1);
                    if (TextUtils.isEmpty(substring)) {
                        substring = new File(string).getName();
                    }
                    if (j2 >= j) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                        String extension = FilenameUtils.getExtension(string);
                        bigSizeFilesWrapper.ext = extension;
                        bigSizeFilesWrapper.name = substring;
                        bigSizeFilesWrapper.id = i2;
                        bigSizeFilesWrapper.path = string;
                        bigSizeFilesWrapper.size = j2;
                        bigSizeFilesWrapper.dateTaken = j3;
                        Boolean bool = hashMap.get(extension.toLowerCase());
                        if (bool == null) {
                            bool = Boolean.FALSE;
                        }
                        if (bool.booleanValue()) {
                            File file = new File(string);
                            if (file.exists() && !file.isDirectory() && !file.isHidden() && !file.getPath().contains("/.")) {
                                addChild(bigSizeFilesWrapper);
                            }
                        }
                    }
                }
            }
            if (query != null) {
                query.close();
            }
            Log.d("SIZE", "files " + this.arrContents.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchFilesForNotInExtns(long j, HashMap<String, Boolean> hashMap) {
        try {
            Cursor query = this.cntx.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id", "_data", "_display_name", "_size", "date_added"}, null, null, "_size DESC");
            if (query != null) {
                int columnIndex = query.getColumnIndex("_data");
                int columnIndex2 = query.getColumnIndex("_size");
                query.getColumnIndex("_display_name");
                int columnIndex3 = query.getColumnIndex("_id");
                int columnIndex4 = query.getColumnIndex("date_added");
                for (int i = 0; i < query.getCount(); i++) {
                    query.moveToPosition(i);
                    String string = query.getString(columnIndex);
                    if (string == null || !string.contains(GlobalData.backuppath)) {
                        long j2 = query.getLong(columnIndex2);
                        int i2 = query.getInt(columnIndex3);
                        long j3 = query.getLong(columnIndex4);
                        String substring = string != null ? string.substring(string.lastIndexOf(DialogConfigs.DIRECTORY_SEPERATOR) + 1) : null;
                        if (TextUtils.isEmpty(substring) && string != null) {
                            substring = new File(string).getName();
                        }
                        if (j2 >= j) {
                            BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                            String extension = FilenameUtils.getExtension(string);
                            bigSizeFilesWrapper.ext = extension;
                            bigSizeFilesWrapper.name = substring;
                            bigSizeFilesWrapper.id = i2;
                            bigSizeFilesWrapper.path = string;
                            bigSizeFilesWrapper.size = j2;
                            bigSizeFilesWrapper.dateTaken = j3;
                            Boolean bool = extension != null ? hashMap.get(extension.toLowerCase()) : null;
                            if (bool == null) {
                                bool = Boolean.FALSE;
                            }
                            if (!bool.booleanValue()) {
                                File file = string != null ? new File(string) : null;
                                if (file != null && file.exists() && !file.isDirectory() && !file.isHidden() && !file.getPath().contains("/.")) {
                                    addChild(bigSizeFilesWrapper);
                                }
                            }
                        }
                    }
                }
            }
            if (query != null) {
                query.close();
            }
            Log.d("SIZE", "files " + this.arrContents.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchImages() {
        try {
            Cursor query = this.cntx.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data", "_display_name", "_size", "datetaken"}, null, null, "_size DESC");
            if (query != null) {
                int columnIndex = query.getColumnIndex("_data");
                int columnIndex2 = query.getColumnIndex("_size");
                int columnIndex3 = query.getColumnIndex("_display_name");
                int columnIndex4 = query.getColumnIndex("_id");
                int columnIndex5 = query.getColumnIndex("datetaken");
                long j = 0;
                int i = 0;
                while (i < query.getCount()) {
                    query.moveToPosition(i);
                    String string = query.getString(columnIndex);
                    long j2 = query.getLong(columnIndex2);
                    String string2 = query.getString(columnIndex3);
                    long j3 = query.getLong(columnIndex5);
                    if (TextUtils.isEmpty(string2)) {
                        string2 = new File(string).getName();
                    }
                    int i2 = columnIndex5;
                    int i3 = query.getInt(columnIndex4);
                    if (j2 >= j) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                        bigSizeFilesWrapper.name = string2;
                        bigSizeFilesWrapper.id = i3;
                        bigSizeFilesWrapper.path = string;
                        bigSizeFilesWrapper.size = j2;
                        bigSizeFilesWrapper.dateTaken = j3;
                        addChild(bigSizeFilesWrapper);
                    }
                    i++;
                    columnIndex5 = i2;
                    j = 0;
                }
                query.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("SIZE", "image " + this.arrContents.size());
    }

    public void fetchVideos() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        try {
            Cursor query = this.cntx.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data", "_display_name", "_size", "datetaken"}, null, null, "_size DESC");
            if (query != null) {
                i4 = query.getColumnIndex("_data");
                i2 = query.getColumnIndex("_size");
                i3 = query.getColumnIndex("_display_name");
                i5 = query.getColumnIndex("_id");
                i = query.getColumnIndex("datetaken");
            } else {
                i = 0;
                i2 = 0;
                i3 = 0;
                i4 = 0;
                i5 = 0;
            }
            if (query != null) {
                for (int i6 = 0; i6 < query.getCount(); i6++) {
                    query.moveToPosition(i6);
                    String string = query.getString(i4);
                    long j = query.getLong(i2);
                    String string2 = query.getString(i3);
                    if (TextUtils.isEmpty(string2)) {
                        string2 = new File(string).getName();
                    }
                    int i7 = query.getInt(i5);
                    long j2 = query.getLong(i);
                    if (TextUtils.isEmpty(string2)) {
                        if (TextUtils.isEmpty(string)) {
                            string = "";
                            string2 = string;
                        } else {
                            string2 = new File(string).getName();
                        }
                    }
                    if (j >= 0) {
                        BigSizeFilesWrapper bigSizeFilesWrapper = new BigSizeFilesWrapper();
                        bigSizeFilesWrapper.name = string2;
                        bigSizeFilesWrapper.id = i7;
                        bigSizeFilesWrapper.path = string;
                        bigSizeFilesWrapper.size = j;
                        bigSizeFilesWrapper.dateTaken = j2;
                        addChild(bigSizeFilesWrapper);
                    }
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("SIZE", "video " + this.arrContents.size());
    }

    public String getSelectedTotalString() {
        Util.appendLogmobiclean(this.TAG, " method getSelectedTotalString  ", GlobalData.FILE_NAME);
        return "" + this.selectedCount + DialogConfigs.DIRECTORY_SEPERATOR + this.totalCount;
    }

    public void initRecoveredBeforeDelete() {
        this.recoveredCount = 0;
        this.recoveredSize = 0L;
    }

    public void refresh() {
        for (int i = 0; i < this.arrContents.size(); i++) {
            this.arrContents.get(i).ischecked = false;
        }
        this.selectedSize = 0L;
        this.selectedCount = 0;
    }

    public void selectAll(ArrayList<BigSizeFilesWrapper> arrayList) {
        long j = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            BigSizeFilesWrapper bigSizeFilesWrapper = arrayList.get(i);
            bigSizeFilesWrapper.ischecked = true;
            j += bigSizeFilesWrapper.size;
        }
        this.selectedCount = arrayList.size();
        this.selectedSize = j;
    }

    public void selectNodeAtIndex(int i) {
        Util.appendLogmobiclean(this.TAG, " method selectNodeAtIndex  ", GlobalData.FILE_NAME);
        BigSizeFilesWrapper bigSizeFilesWrapper = this.filesList.get(i);
        bigSizeFilesWrapper.ischecked = true;
        this.selectedCount++;
        this.selectedSize += bigSizeFilesWrapper.size;
    }

    public void unSelectAll() {
        Util.appendLogmobiclean(this.TAG, " method unSelectAll call ", GlobalData.FILE_NAME);
        for (int i = 0; i < this.arrContents.size(); i++) {
            this.arrContents.get(i).ischecked = false;
        }
        this.selectedCount = 0;
        this.selectedSize = 0L;
    }

    public void unSelectNodeAtIndex(int i) {
        Util.appendLogmobiclean(this.TAG, " method unSelectNodeAtIndex  ", GlobalData.FILE_NAME);
        BigSizeFilesWrapper bigSizeFilesWrapper = this.filesList.get(i);
        bigSizeFilesWrapper.ischecked = false;
        this.selectedCount--;
        this.selectedSize -= bigSizeFilesWrapper.size;
    }

    public void selectAll() {
        Util.appendLogmobiclean(this.TAG, " method selectAll call ", GlobalData.FILE_NAME);
        for (int i = 0; i < this.arrContents.size(); i++) {
            this.arrContents.get(i).ischecked = true;
        }
        this.selectedCount = this.totalCount;
        this.selectedSize = this.totalSize;
    }
}
