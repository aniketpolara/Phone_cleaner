package com.cleanPhone.mobileCleaner.socialmedia;


import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.SocialAnimationActivity;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;
import com.cleanPhone.mobileCleaner.wrappers.BigSizeFilesWrapper;

import java.util.ArrayList;


public class SocialmediaModule {
    public String TAG = "SocialmediaModule";
    public ArrayList<SocialMedia> arrContents = new ArrayList<>();
    public MediaList currentList;
    private ArrayList<BigSizeFilesWrapper> dataToDelete;
    public long recoveredSize;
    public int selectedCount;
    private long selectedForDeleteSize;
    public long selectedSize;
    public long totalSize;

    public SocialmediaModule() {
        Util.appendLogmobiclean(this.TAG, " Constractor calling SocialmediaModule", GlobalData.FILE_NAME);
        this.dataToDelete = new ArrayList<>();
        this.currentList = null;
    }

    private long getAllAppMediaSize(SocialMedia socialMedia) {
        long j = 0;
        for (int i = 0; i < socialMedia.arrContents.size(); i++) {
            j += socialMedia.arrContents.get(i).totalSize;
        }
        return j;
    }

    public void addDataForDeletion(BigSizeFilesWrapper bigSizeFilesWrapper) {
        if (bigSizeFilesWrapper.ischecked) {
            return;
        }
        this.dataToDelete.add(bigSizeFilesWrapper);
        this.selectedForDeleteSize += bigSizeFilesWrapper.size;
        this.selectedCount++;
        bigSizeFilesWrapper.ischecked = true;
    }

    public ArrayList<SocialMedia> getAllAppsHavingData() {
        ArrayList<SocialMedia> arrayList = new ArrayList<>();
        for (int i = 0; i < this.arrContents.size(); i++) {
            if (MobiClean.getInstance().socialModule.getAllAppMediaSize(this.arrContents.get(i)) > 0) {
                arrayList.add(this.arrContents.get(i));
            }
        }
        return arrayList;
    }

    public void removeDataFromDeletionList(BigSizeFilesWrapper bigSizeFilesWrapper) {
        for (int i = 0; i < this.dataToDelete.size(); i++) {
            if (this.dataToDelete.get(i).id == bigSizeFilesWrapper.id) {
                this.dataToDelete.remove(i);
                this.selectedCount--;
                this.selectedForDeleteSize -= bigSizeFilesWrapper.size;
            }
        }
    }

    public void updateSelf() {
        this.totalSize = 0L;
        this.selectedSize = 0L;
        this.selectedCount = 0;
        for (int i = 0; i < this.arrContents.size(); i++) {
            SocialMedia socialMedia = this.arrContents.get(i);
            for (int i2 = 0; i2 < socialMedia.arrContents.size(); i2++) {
                MediaList mediaList = socialMedia.arrContents.get(i2);
                if (mediaList.mediaType != SocialAnimationActivity.FileTypes.ALL) {
                    this.selectedSize += mediaList.selectedSize;
                    this.selectedCount += mediaList.selectedCount;
                    this.totalSize += mediaList.totalSize;
                }
            }
        }
    }
}
