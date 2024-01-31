package com.cleanPhone.mobileCleaner.similerphotos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.util.Log;

import com.facebook.appevents.internal.ViewHierarchyConstants;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.DuplicatesData;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

public class DuplicacyRefreshAsyncTask extends android.os.AsyncTask<String, String, String> {
    private static final String APP_FOLDER = "DCIM/FreeUpSpace/compress/compressed images";
    private static final String APP_FOLDER2 = "DCIM/FreeUpSpace/resize/resized-images";
    private static final String APP_FOLDER_VIDEO = "DCIM/FreeUpSpace/compress/compressed videos";
    public static volatile boolean stopExecuting = false;
    private EventBus bus = EventBus.getDefault();
    private Context context;

    public DuplicacyRefreshAsyncTask(Context context) {
        this.context = context;
        MobiClean.getInstance().duplicatesData = new DuplicatesData();
    }

    private boolean checkIsExistsInAppFolder(String str) {
        try {
            String substring = str.substring(0, str.lastIndexOf(47));
            if (substring.endsWith(APP_FOLDER) || substring.endsWith(APP_FOLDER_VIDEO)) {
                return true;
            }
            return substring.endsWith(APP_FOLDER2);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private Float convertToDegree(String str) {
        if (str == null) {
            return null;
        }
        String[] split = str.split(",", 3);
        String[] split2 = split[0].split(DialogConfigs.DIRECTORY_SEPERATOR, 2);
        Double valueOf = Double.valueOf(new Double(split2[0]).doubleValue() / new Double(split2[1]).doubleValue());
        String[] split3 = split[1].split(DialogConfigs.DIRECTORY_SEPERATOR, 2);
        Double valueOf2 = Double.valueOf(new Double(split3[0]).doubleValue() / new Double(split3[1]).doubleValue());
        String[] split4 = split[2].split(DialogConfigs.DIRECTORY_SEPERATOR, 2);
        return new Float(valueOf.doubleValue() + (valueOf2.doubleValue() / 60.0d) + (Double.valueOf(new Double(split4[0]).doubleValue() / new Double(split4[1]).doubleValue()).doubleValue() / 3600.0d));
    }


    @SuppressLint("RestrictedApi")
    private void fetchAllImagesUrlRefill() {
        String str;
        int i=0;
        int i2=0;
        int i3=0;
        int i4 = 0;
        ExifInterface exifInterface = null;
        String str2 = null;
        String sb = null;
        int parseInt = 0;
        DuplicacyRefreshAsyncTask duplicacyRefreshAsyncTask = this;
        String str3 = "";
        Cursor cursor = null;
        try {
            try {
                cursor = duplicacyRefreshAsyncTask.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data", "_display_name", "_size", "latitude", "longitude", "datetaken", ViewHierarchyConstants.DIMENSION_WIDTH_KEY, ViewHierarchyConstants.DIMENSION_HEIGHT_KEY, "_data"}, null, null, "datetaken DESC");
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndex("_id");
                    int columnIndex2 = cursor.getColumnIndex("_data");
                    int columnIndex3 = cursor.getColumnIndex("_size");
                    int columnIndex4 = cursor.getColumnIndex("_display_name");
                    int columnIndex5 = cursor.getColumnIndex("datetaken");
                    @SuppressLint("RestrictedApi") int columnIndex6 = cursor.getColumnIndex(ViewHierarchyConstants.DIMENSION_WIDTH_KEY);
                    @SuppressLint("RestrictedApi") int columnIndex7 = cursor.getColumnIndex(ViewHierarchyConstants.DIMENSION_HEIGHT_KEY);
                    int count = cursor.getCount();
                    char c2 = 0;
                    int i5 = 0;
                    while (i5 < count) {
                        try {
                            String[] strArr = new String[1];
                            strArr[c2] = str3 + i5;
                            duplicacyRefreshAsyncTask.publishProgress(strArr);
                        } catch (Exception e) {
                            e = e;
                            str = str3;
                            i = columnIndex;
                            i2 = columnIndex2;
                        }
                        if (!stopExecuting) {
                            cursor.moveToPosition(i5);
                            String string = cursor.getString(columnIndex2);
                            File file = new File(string);
                            if (file.exists() && !duplicacyRefreshAsyncTask.checkIsExistsInAppFolder(string)) {
                                ImageDetail imageDetail = new ImageDetail();
                                imageDetail.name = cursor.getString(columnIndex4);
                                imageDetail.id = cursor.getInt(columnIndex);
                                imageDetail.path = cursor.getString(columnIndex2);
                                i = columnIndex;
                                int i6 = columnIndex2;
                                try {
                                    imageDetail.size = cursor.getLong(columnIndex3);
                                    String string2 = cursor.getString(columnIndex5);
                                    imageDetail.createionDate = string2;
                                    imageDetail.createDateInMSecs = Long.valueOf(Long.parseLong(string2));
                                    imageDetail.addDateInMSec = Long.valueOf(file.lastModified());
                                    i3 = cursor.getInt(columnIndex6);
                                    i4 = cursor.getInt(columnIndex7);
                                    exifInterface = new ExifInterface(string);
                                    str2 = str3 + duplicacyRefreshAsyncTask.convertToDegree(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LATITUDE));
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append(str3);
                                    str = str3;
                                    try {
                                        sb2.append(duplicacyRefreshAsyncTask.convertToDegree(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LONGITUDE)));
                                        sb = sb2.toString();
                                        try {
                                            imageDetail.width = Integer.parseInt(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_IMAGE_WIDTH));
                                            parseInt = Integer.parseInt(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_IMAGE_LENGTH));
                                            imageDetail.height = parseInt;
                                        } catch (Exception e2) {
                                            i2 = i6;
                                            e2.printStackTrace();
                                        }
                                    } catch (Exception e3) {
                                        i2 = i6;
                                        i5++;
                                        columnIndex2 = i2;
                                        columnIndex = i;
                                        str3 = str;
                                        c2 = 0;
                                        duplicacyRefreshAsyncTask = this;
                                    }
                                } catch (Exception e4) {
                                    str = str3;
                                }
                                if (imageDetail.width == 0 || parseInt == 0) {
                                    if (i4 == 0 || i3 == 0) {
                                        i2 = i6;
                                        i5++;
                                        columnIndex2 = i2;
                                        columnIndex = i;
                                        str3 = str;
                                        c2 = 0;
                                        duplicacyRefreshAsyncTask = this;
                                    } else {
                                        imageDetail.width = i3;
                                        imageDetail.height = i4;
                                    }
                                }
                                imageDetail.lat = str2;
                                imageDetail.lon = sb;
                                i2 = i6;
                                imageDetail.ext = FilenameUtils.getExtension(cursor.getString(i2));
                                int i7 = imageDetail.width;
                                int i8 = GlobalData.minimumSize;
                                if (i7 < i8 || imageDetail.height < i8) {
                                    imageDetail.skipImage = true;
                                }
                                imageDetail.exif = exifInterface;
                                MobiClean.getInstance().duplicatesData.imageList.add(imageDetail);
                                i5++;
                                columnIndex2 = i2;
                                columnIndex = i;
                                str3 = str;
                                c2 = 0;
                                duplicacyRefreshAsyncTask = this;
                            }
                        }
                        str = str3;
                        i = columnIndex;
                        i2 = columnIndex2;
                        i5++;
                        columnIndex2 = i2;
                        columnIndex = i;
                        str3 = str;
                        c2 = 0;
                        duplicacyRefreshAsyncTask = this;
                    }
                }
                if (cursor == null) {
                    return;
                }
            } catch (Exception e5) {
                e5.printStackTrace();
                if (cursor == null) {
                    return;
                }
            }
            cursor.close();
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    @SuppressLint("RestrictedApi")
    private void fetchAllImagesUrlRefillCamera() {
        String str=null;
        int i=0;
        int i2=0;
        int i3=0;
        int i4 = 0;
        ExifInterface exifInterface = null;
        String str2 = null;
        StringBuilder sb = null;
        String sb2 = null;
        int parseInt = 0;
        DuplicacyRefreshAsyncTask duplicacyRefreshAsyncTask = this;
        Log.i("CALLLED", "here");
        Cursor cursor = null;
        try {
            try {
                cursor = duplicacyRefreshAsyncTask.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data", "_display_name", "_size", "latitude", "longitude", "datetaken", ViewHierarchyConstants.DIMENSION_WIDTH_KEY, ViewHierarchyConstants.DIMENSION_HEIGHT_KEY, "_data"}, "_data like ? OR _data like ? OR _data like ?", new String[]{"%/dcim/camera/%", "%/dcim/camera2/%", "%/dcim/100andro/%"}, "datetaken DESC");
                String str3 = "";
                if (cursor != null) {
                    Log.i("CURSORCOUNT", "" + cursor.getCount());
                }
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndex("_id");
                    int columnIndex2 = cursor.getColumnIndex("_data");
                    int columnIndex3 = cursor.getColumnIndex("_size");
                    int columnIndex4 = cursor.getColumnIndex("_display_name");
                    int columnIndex5 = cursor.getColumnIndex("datetaken");
                    int columnIndex6 = cursor.getColumnIndex(ViewHierarchyConstants.DIMENSION_WIDTH_KEY);
                    int columnIndex7 = cursor.getColumnIndex(ViewHierarchyConstants.DIMENSION_HEIGHT_KEY);
                    int count = cursor.getCount();
                    char c2 = 0;
                    int i5 = 0;
                    while (i5 < count) {
                        try {
                            String[] strArr = new String[1];
                            strArr[c2] = str3 + i5;
                            duplicacyRefreshAsyncTask.publishProgress(strArr);
                        } catch (Exception e) {
                            e = e;
                            str = str3;
                            i = columnIndex;
                            i2 = columnIndex2;
                        }
                        if (!stopExecuting) {
                            cursor.moveToPosition(i5);
                            String string = cursor.getString(columnIndex2);
                            File file = new File(string);
                            if (file.exists() && !duplicacyRefreshAsyncTask.checkIsExistsInAppFolder(string)) {
                                ImageDetail imageDetail = new ImageDetail();
                                imageDetail.name = cursor.getString(columnIndex4);
                                imageDetail.id = cursor.getInt(columnIndex);
                                imageDetail.path = cursor.getString(columnIndex2);
                                i = columnIndex;
                                int i6 = columnIndex2;
                                try {
                                    imageDetail.size = cursor.getLong(columnIndex3);
                                    String string2 = cursor.getString(columnIndex5);
                                    imageDetail.createionDate = string2;
                                    imageDetail.createDateInMSecs = Long.valueOf(Long.parseLong(string2));
                                    imageDetail.addDateInMSec = Long.valueOf(file.lastModified());
                                    i3 = cursor.getInt(columnIndex6);
                                    i4 = cursor.getInt(columnIndex7);
                                    exifInterface = new ExifInterface(string);
                                    str2 = str3 + duplicacyRefreshAsyncTask.convertToDegree(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LATITUDE));
                                    sb = new StringBuilder();
                                    sb.append(str3);
                                    str = str3;
                                } catch (Exception e2) {
                                    str = str3;
                                }
                                try {
                                    sb.append(duplicacyRefreshAsyncTask.convertToDegree(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LONGITUDE)));
                                    sb2 = sb.toString();
                                    try {
                                        imageDetail.width = Integer.parseInt(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_IMAGE_WIDTH));
                                        parseInt = Integer.parseInt(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_IMAGE_LENGTH));
                                        imageDetail.height = parseInt;
                                    } catch (Exception e3) {
                                        i2 = i6;
                                        e3.printStackTrace();
                                    }
                                } catch (Exception e4) {
                                    i2 = i6;
                                    i5++;
                                    columnIndex2 = i2;
                                    columnIndex = i;
                                    str3 = str;
                                    c2 = 0;
                                    duplicacyRefreshAsyncTask = this;
                                }
                                if (imageDetail.width == 0 || parseInt == 0) {
                                    if (i4 == 0 || i3 == 0) {
                                        i2 = i6;
                                        i5++;
                                        columnIndex2 = i2;
                                        columnIndex = i;
                                        str3 = str;
                                        c2 = 0;
                                        duplicacyRefreshAsyncTask = this;
                                    } else {
                                        imageDetail.width = i3;
                                        imageDetail.height = i4;
                                    }
                                }
                                imageDetail.lat = str2;
                                imageDetail.lon = sb2;
                                i2 = i6;
                                imageDetail.ext = FilenameUtils.getExtension(cursor.getString(i2));
                                int i7 = imageDetail.width;
                                int i8 = GlobalData.minimumSize;
                                if (i7 < i8 || imageDetail.height < i8) {
                                    imageDetail.skipImage = true;
                                }
                                imageDetail.exif = exifInterface;
                                MobiClean.getInstance().duplicatesData.imageList.add(imageDetail);
                                i5++;
                                columnIndex2 = i2;
                                columnIndex = i;
                                str3 = str;
                                c2 = 0;
                                duplicacyRefreshAsyncTask = this;
                            }
                        }
                        str = str3;
                        i = columnIndex;
                        i2 = columnIndex2;
                        i5++;
                        columnIndex2 = i2;
                        columnIndex = i;
                        str3 = str;
                        c2 = 0;
                        duplicacyRefreshAsyncTask = this;
                    }
                }
                if (cursor == null) {
                    return;
                }
            } catch (Exception e5) {
                e5.printStackTrace();
                if (0 == 0) {
                    return;
                }
            }
            cursor.close();
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    private void fetchAllImagesUrlRefillFromFolder() {
        int i=0;
        String str=null;
        int i2=0;
        int i3=0;
        int i4 = 0;
        int i5=0;
        ExifInterface exifInterface = null;
        String sb = null;
        String sb2=null;
        int parseInt = 0;
        DuplicacyRefreshAsyncTask duplicacyRefreshAsyncTask = this;
        Log.i("CALLLED", "fetchAllImagesUrlRefillFromFolder");
        Cursor cursor = null;
        try {
            try {
                @SuppressLint("RestrictedApi") String[] strArr = {"_id", "_data", "_display_name", "_size", "latitude", "longitude", "datetaken", ViewHierarchyConstants.DIMENSION_WIDTH_KEY, ViewHierarchyConstants.DIMENSION_HEIGHT_KEY, "_data"};
                String[] strArr2 = new String[DuplicacyMidSettings.listItem.size()];
                StringBuilder sb3 = new StringBuilder();
                char c2 = 0;
                int i6 = 0;
                while (true) {
                    i = 1;
                    if (i6 >= DuplicacyMidSettings.listItem.size()) {
                        break;
                    }
                    strArr2[i6] = "%" + DuplicacyMidSettings.listItem.get(i6).getPath() + "%";
                    sb3.append("_data like ?");
                    if (DuplicacyMidSettings.listItem.size() - 1 != i6) {
                        sb3.append(" OR ");
                    }
                    Log.i("FromFolder", strArr2[i6]);
                    i6++;
                }
                cursor = duplicacyRefreshAsyncTask.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, sb3.toString().trim(), strArr2, "datetaken DESC");
                String str2 = "";
                if (cursor != null) {
                    Log.i("CURSORCOUNT", "" + cursor.getCount());
                }
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndex("_id");
                    int columnIndex2 = cursor.getColumnIndex("_data");
                    int columnIndex3 = cursor.getColumnIndex("_size");
                    int columnIndex4 = cursor.getColumnIndex("_display_name");
                    int columnIndex5 = cursor.getColumnIndex("datetaken");
                    @SuppressLint("RestrictedApi") int columnIndex6 = cursor.getColumnIndex(ViewHierarchyConstants.DIMENSION_WIDTH_KEY);
                    @SuppressLint("RestrictedApi") int columnIndex7 = cursor.getColumnIndex(ViewHierarchyConstants.DIMENSION_HEIGHT_KEY);
                    int count = cursor.getCount();
                    int i7 = 0;
                    while (i7 < count) {
                        try {
                            String[] strArr3 = new String[i];
                            strArr3[c2] = str2 + i7;
                            duplicacyRefreshAsyncTask.publishProgress(strArr3);
                        } catch (Exception e) {
                            e = e;
                            str = str2;
                            i2 = columnIndex;
                            i3 = columnIndex4;
                        }
                        if (!stopExecuting) {
                            cursor.moveToPosition(i7);
                            String string = cursor.getString(columnIndex2);
                            File file = new File(string);
                            if (file.exists() && !duplicacyRefreshAsyncTask.checkIsExistsInAppFolder(string)) {
                                ImageDetail imageDetail = new ImageDetail();
                                imageDetail.name = cursor.getString(columnIndex4);
                                imageDetail.id = cursor.getInt(columnIndex);
                                imageDetail.path = cursor.getString(columnIndex2);
                                i3 = columnIndex4;
                                try {
                                    imageDetail.size = cursor.getLong(columnIndex3);
                                    String string2 = cursor.getString(columnIndex5);
                                    imageDetail.createionDate = string2;
                                    imageDetail.createDateInMSecs = Long.valueOf(Long.parseLong(string2));
                                    i4 = cursor.getInt(columnIndex6);
                                    i5 = cursor.getInt(columnIndex7);
                                    imageDetail.addDateInMSec = Long.valueOf(file.lastModified());
                                    exifInterface = new ExifInterface(string);
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append(str2);
                                    i2 = columnIndex;
                                    try {
                                        sb4.append(duplicacyRefreshAsyncTask.convertToDegree(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LATITUDE)));
                                        sb = sb4.toString();
                                        StringBuilder sb5 = new StringBuilder();
                                        sb5.append(str2);
                                        str = str2;
                                        try {
                                            sb5.append(duplicacyRefreshAsyncTask.convertToDegree(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LONGITUDE)));
                                            sb2 = sb5.toString();
                                            try {
                                                imageDetail.width = Integer.parseInt(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_IMAGE_WIDTH));
                                                parseInt = Integer.parseInt(exifInterface.getAttribute(androidx.exifinterface.media.ExifInterface.TAG_IMAGE_LENGTH));
                                                imageDetail.height = parseInt;
                                            } catch (Exception e2) {
                                                e2.printStackTrace();
                                            }
                                        } catch (Exception e3) {
                                            i7++;
                                            duplicacyRefreshAsyncTask = this;
                                            columnIndex4 = i3;
                                            columnIndex = i2;
                                            str2 = str;
                                            c2 = 0;
                                            i = 1;
                                        }
                                    } catch (Exception e4) {
                                        str = str2;
                                    }
                                } catch (Exception e5) {
                                    str = str2;
                                    i2 = columnIndex;
                                }
                                if (imageDetail.width == 0 || parseInt == 0) {
                                    if (i5 != 0 && i4 != 0) {
                                        imageDetail.width = i4;
                                        imageDetail.height = i5;
                                    }
                                    i7++;
                                    duplicacyRefreshAsyncTask = this;
                                    columnIndex4 = i3;
                                    columnIndex = i2;
                                    str2 = str;
                                    c2 = 0;
                                    i = 1;
                                }
                                imageDetail.lat = sb;
                                imageDetail.lon = sb2;
                                imageDetail.ext = FilenameUtils.getExtension(cursor.getString(columnIndex2));
                                int i8 = imageDetail.width;
                                int i9 = GlobalData.minimumSize;
                                if (i8 < i9 || imageDetail.height < i9) {
                                    try {
                                        imageDetail.skipImage = true;
                                    } catch (Exception e6) {
                                        i7++;
                                        duplicacyRefreshAsyncTask = this;
                                        columnIndex4 = i3;
                                        columnIndex = i2;
                                        str2 = str;
                                        c2 = 0;
                                        i = 1;
                                    }
                                }
                                imageDetail.exif = exifInterface;
                                MobiClean.getInstance().duplicatesData.imageList.add(imageDetail);
                                i7++;
                                duplicacyRefreshAsyncTask = this;
                                columnIndex4 = i3;
                                columnIndex = i2;
                                str2 = str;
                                c2 = 0;
                                i = 1;
                            }
                        }
                        str = str2;
                        i2 = columnIndex;
                        i3 = columnIndex4;
                        i7++;
                        duplicacyRefreshAsyncTask = this;
                        columnIndex4 = i3;
                        columnIndex = i2;
                        str2 = str;
                        c2 = 0;
                        i = 1;
                    }
                }
                if (cursor == null) {
                    return;
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Exception e7) {
            e7.printStackTrace();
            if (0 == 0) {
                return;
            }
        }
        cursor.close();
    }

    @Override
    public void onPreExecute() {
        stopExecuting = false;
        Util.appendLogmobiclean("DuplicatesRefreshAsyncTask", " DuplicacyRefreshAsyncTask  onpre", "");
        super.onPreExecute();
    }

    @Override
    public String doInBackground(String... strArr) {
        if (MobiClean.getInstance().duplicatesData == null) {
            return null;
        }
        MobiClean.getInstance().duplicatesData.imageList.clear();
        Util.appendLogmobiclean("DuplicatesRefreshAsyncTask", " DuplicacyRefreshAsyncTask  doinback", "");
        if (MobiClean.getInstance().duplicatesData.fromselectedFolder) {
            fetchAllImagesUrlRefillFromFolder();
        } else if (!MobiClean.getInstance().duplicatesData.fromCamera) {
            fetchAllImagesUrlRefill();
        } else {
            fetchAllImagesUrlRefillCamera();
        }
        if (MobiClean.getInstance().duplicatesData == null) {
        }
        return null;
    }


    @Override
    public void onPostExecute(String str) {
        super.onPostExecute( str);
        Util.appendLogmobiclean("DuplicatesRefreshAsyncTask", " DuplicacyRefreshAsyncTask  onpost", "");
        if (MobiClean.getInstance().duplicatesData == null || stopExecuting) {
            return;
        }
        this.bus.post(Integer.valueOf((int) GlobalData.CREATE_GROUPS));
    }
}