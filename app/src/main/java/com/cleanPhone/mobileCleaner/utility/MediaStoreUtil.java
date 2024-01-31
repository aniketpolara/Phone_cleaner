package com.cleanPhone.mobileCleaner.utility;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public final class MediaStoreUtil {

    private MediaStoreUtil() {
        throw new UnsupportedOperationException();
    }



    public static Uri getUriFromFile(Context context, String str) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(MediaStore.Files.getContentUri("external"), new String[]{"_id"}, "_data = ?", new String[]{str}, "date_added desc");
        if (query == null) {
            return null;
        }
        query.moveToFirst();
        if (query.isAfterLast()) {
            query.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put("_data", str);
            return contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues);
        }
        @SuppressLint("Range") Uri build = MediaStore.Files.getContentUri("external").buildUpon().appendPath(Integer.toString(query.getInt(query.getColumnIndex("_id")))).build();
        query.close();
        return build;
    }


}
