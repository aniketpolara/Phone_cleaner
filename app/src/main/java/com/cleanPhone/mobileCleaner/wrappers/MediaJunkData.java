package com.cleanPhone.mobileCleaner.wrappers;


import com.cleanPhone.mobileCleaner.ImageFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class MediaJunkData {
    public updateProgress f5362a;
    public String b;
    public String mediaName;
    public int mediaType;
    public long totSelectedCount;
    public long totSelectedSize;
    public long totCount = 0;
    public long totSize = 0;
    public ArrayList<BigSizeFilesWrapper> dataList = new ArrayList<>();


    public interface updateProgress {
        void update(String str);
    }

    public MediaJunkData(int i, String str) {
        this.mediaName = str;
        this.mediaType = i;
    }

    private void addChild(BigSizeFilesWrapper bigSizeFilesWrapper) {
        this.totSize += bigSizeFilesWrapper.size;
        this.totCount++;
        this.dataList.add(bigSizeFilesWrapper);
    }

    public void getFiles(ArrayList<String> arrayList, updateProgress updateprogress, String str) {
        this.b = str;
        this.f5362a = updateprogress;
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            getFiles(new File(it.next()));
        }
    }

    public void select(BigSizeFilesWrapper bigSizeFilesWrapper) {
        this.totSelectedCount++;
        this.totSelectedSize += bigSizeFilesWrapper.size;
    }

    public void selectAll() {
        this.totSelectedSize = 0L;
        this.totSelectedCount = this.dataList.size();
        for (int i = 0; i < this.dataList.size(); i++) {
            this.totSelectedSize += this.dataList.get(i).size;
            this.dataList.get(i).ischecked = true;
        }
    }

    public void unselect(BigSizeFilesWrapper bigSizeFilesWrapper) {
        this.totSelectedCount--;
        this.totSelectedSize -= bigSizeFilesWrapper.size;
    }

    public void unselectAll() {
        this.totSelectedSize = 0L;
        this.totSelectedCount = 0L;
        for (int i = 0; i < this.dataList.size(); i++) {
            this.dataList.get(i).ischecked = false;
        }
    }

    private void getFiles(File file) {
        File[] listFiles;
        if (file == null || !file.exists() || (listFiles = file.listFiles(new ImageFileFilter(this.mediaType))) == null) {
            return;
        }
        for (File file2 : listFiles) {
            if (file2.isDirectory()) {
                getFiles(file2);
            }
            addChild(new BigSizeFilesWrapper(file2));
            updateProgress updateprogress = this.f5362a;
            if (updateprogress != null) {
                updateprogress.update(this.b);
            }
        }
    }
}
