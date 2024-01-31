package com.cleanPhone.mobileCleaner.filestorage;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Utility {
    public static boolean checkStorageAccessPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.checkCallingOrSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED || context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED;

        }
        return true;
    }

    public static ArrayList<FileListItem> prepareFileListEntries(ArrayList<FileListItem> arrayList, File file, ExtensionFilter extensionFilter) {
        File[] listFiles;
        try {
            for (File file2 : file.listFiles(extensionFilter)) {
                if (file2.canRead()) {
                    FileListItem fileListItem = new FileListItem();
                    fileListItem.setFilename(file2.getName());
                    fileListItem.setDirectory(file2.isDirectory());
                    fileListItem.setLocation(file2.getAbsolutePath());
                    fileListItem.setTime(file2.lastModified());
                    arrayList.add(fileListItem);
                }
            }
            try {
                Collections.sort(arrayList);
                return arrayList;
            } catch (Exception e) {
                e.printStackTrace();
                return arrayList;
            }
        } catch (NullPointerException e2) {
            e2.printStackTrace();
            return new ArrayList<>();
        }
    }
}
