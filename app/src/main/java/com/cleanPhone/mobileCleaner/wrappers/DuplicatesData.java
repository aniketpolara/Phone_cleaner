package com.cleanPhone.mobileCleaner.wrappers;

import android.content.Context;
import android.util.Log;

import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.similerphotos.DuplicateGroup;
import com.cleanPhone.mobileCleaner.similerphotos.ImageDetail;

import java.util.ArrayList;

public class DuplicatesData {
    public int currentGroupChildIndex;
    public int currentGroupIndex;
    public ArrayList<ImageDetail> currentList;
    public ArrayList<String> headerlist;
    public boolean isBackAfterPreviewDelete;
    public long recoveredSpace;
    public int totalDuplicates;
    public long totalDuplicatesSize;
    public long totalselected = 0;
    public long totalselectedSize = 0;
    public boolean fromCamera = true;
    public long totalDeletedSize = 0;
    public ArrayList<ImageDetail> showImageList = new ArrayList<>();
    public ArrayList<DuplicateGroup> grouplist = new ArrayList<>();
    public ArrayList<ImageDetail> imageList = new ArrayList<>();
    public boolean alreadyScanned = false;
    public boolean isBackAfterDelete = false;
    public boolean proceededToAd = false;
    public boolean fromselectedFolder = false;

    public void clearAll() {
        ArrayList<DuplicateGroup> arrayList = this.grouplist;
        if (arrayList != null) {
            arrayList.clear();
        }
        ArrayList<ImageDetail> arrayList2 = this.imageList;
        if (arrayList2 != null) {
            arrayList2.clear();
        }
    }

    public void fillDisplayedImageList() {
        this.showImageList.clear();
        for (int i = 0; i < this.grouplist.size(); i++) {
            for (int i2 = 0; i2 < this.grouplist.get(i).children.size(); i2++) {
                if (i2 != 0) {
                    this.showImageList.add(this.grouplist.get(i).children.get(i2));
                }
            }
        }
    }

    public void fillheaderList(Context context) {
        this.headerlist = null;
        this.headerlist = new ArrayList<>();
        int i = 0;
        while (i < this.grouplist.size()) {
            i++;
            ArrayList<String> arrayList = this.headerlist;
            arrayList.add(context.getString(R.string.mbc_group) + i);
        }
    }

    public void filterCameraImages() {
        ArrayList arrayList = new ArrayList();
        Log.d("DUPSS", this.imageList.size() + " imageList size");
        for (int i = 0; i < this.imageList.size(); i++) {
            if (this.imageList.get(i).path.toLowerCase().contains("/dcim/camera/") || this.imageList.get(i).path.toLowerCase().contains("/dcim/100media/") || this.imageList.get(i).path.toLowerCase().contains("/dcim/100andro")) {
                arrayList.add(this.imageList.get(i));
            }
        }
        this.imageList.clear();
        this.imageList.addAll(arrayList);
        Log.d("DUPSS", arrayList.size() + " temp size");
    }

    public void filterGroupList() {
        int size = this.grouplist.size();
        int i = 0;
        while (i < size) {
            if (this.grouplist.get(i).children.size() < 2) {
                this.grouplist.remove(i);
                size--;
            } else {
                i++;
            }
        }
    }

    public boolean isLockAnyImage(ArrayList<ImageDetail> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).lockImg) {
                return true;
            }
        }
        return false;
    }

    public void markAll() {
        this.totalselectedSize = 0L;
        this.totalselected = 0L;
        for (int i = 0; i < this.grouplist.size(); i++) {
            for (int i2 = 0; i2 < this.grouplist.get(i).children.size(); i2++) {
                if (i2 != 0) {
                    this.grouplist.get(i).children.get(i2).ischecked = true;
                    this.totalselected++;
                    this.totalselectedSize = this.grouplist.get(i).children.get(i2).size + this.totalselectedSize;
                } else {
                    this.grouplist.get(i).children.get(i2).ischecked = false;
                }
            }
        }
    }

    public void refresh() {
        this.totalDuplicates = 0;
        this.totalDuplicatesSize = 0L;
        Log.i("DUPSS", this.grouplist.size() + "");
        for (int i = 0; i < this.grouplist.size(); i++) {
            for (int i2 = 0; i2 < this.grouplist.get(i).children.size(); i2++) {
                this.totalDuplicatesSize += this.grouplist.get(i).children.get(i2).size;
                if (i2 != 0) {
                    this.totalDuplicates++;
                }
            }
        }
    }

    public void removeNode(ImageDetail imageDetail) {
        this.recoveredSpace += imageDetail.size;
    }

    public void reset() {
        this.recoveredSpace = 0L;
        unmarkAll();
    }

    public void selectNode(ImageDetail imageDetail) {
        this.totalselectedSize += imageDetail.size;
        this.totalselected++;
    }

    public ArrayList<String> selectedForDelete() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < this.grouplist.size(); i++) {
            for (int i2 = 0; i2 < this.grouplist.get(i).children.size(); i2++) {
                if (this.grouplist.get(i).children.get(i2).ischecked) {
                    arrayList.add(this.grouplist.get(i).children.get(i2).path);
                }
            }
        }
        return arrayList;
    }

    public void unmarkAll() {
        this.totalselected = 0L;
        this.totalselectedSize = 0L;
        for (int i = 0; i < this.grouplist.size(); i++) {
            for (int i2 = 0; i2 < this.grouplist.get(i).children.size(); i2++) {
                this.grouplist.get(i).children.get(i2).ischecked = false;
            }
        }
    }

    public void unselectNode(ImageDetail imageDetail) {
        this.totalselectedSize -= imageDetail.size;
        this.totalselected--;
    }
}
