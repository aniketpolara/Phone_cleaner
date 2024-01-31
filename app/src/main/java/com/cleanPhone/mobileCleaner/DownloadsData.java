package com.cleanPhone.mobileCleaner;

import android.content.Context;
import android.os.Environment;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.cleanPhone.mobileCleaner.wrappers.DownloadWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class DownloadsData {
    public int totalDeleted;
    public int totalSelected;
    public ArrayList<DownloadWrapper> downloadsList = new ArrayList<>();

    public long f4598a = 0;

    public DownloadsData(Context context) {
    }

    private boolean isMediaAvailable(String str) {
        return "mounted".equals(str);
    }

    public ArrayList<DownloadWrapper> a() {
        ArrayList<DownloadWrapper> arrayList = new ArrayList<>();
        Iterator<DownloadWrapper> it = this.downloadsList.iterator();
        while (it.hasNext()) {
            DownloadWrapper next = it.next();
            if (next.checked) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public void getDownloadedFiles(Context context, DownloadDataFetchedListener downloadDataFetchedListener) {
        this.downloadsList = null;
        this.downloadsList = new ArrayList<>();
        long j = 0;
        if (isMediaAvailable(Environment.getExternalStorageState())) {
            File[] listFiles = new File(Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS).listFiles();
            if (listFiles != null) {
                for (File file : listFiles) {
                    DownloadWrapper downloadWrapper = new DownloadWrapper();
                    if (file != null && !file.isDirectory() && !file.isHidden()) {
                        String absolutePath = file.getAbsolutePath();
                        downloadWrapper.isFolder = file.isDirectory();
                        downloadWrapper.path = absolutePath;
                        long length = file.length();
                        downloadWrapper.size = length;
                        j += length;
                        downloadWrapper.ext = FilenameUtils.getExtension(absolutePath);
                        downloadWrapper.name = FilenameUtils.getName(absolutePath);
                        this.downloadsList.add(downloadWrapper);
                    }
                }
            }
        }
        downloadDataFetchedListener.onFetched(this.downloadsList.size(), j);
    }
}
