package com.cleanPhone.mobileCleaner.socialmedia;

import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SocialMedia {
    public String TAG = "SocialMedia";
    public List<MediaList> arrContents = new CopyOnWriteArrayList();
    public Map<Integer, MediaList> hmFileTypeToMediaList;
    public String name;

    public SocialMedia(String str) {
        this.name = str;
    }

    public void selectAll() {
        Util.appendLogmobiclean(this.TAG, " method selectAll call ", GlobalData.FILE_NAME);
        for (int i = 0; i < this.arrContents.size(); i++) {
            this.arrContents.get(i).selectAll();
        }
    }

    public void unSelectAll() {
        Util.appendLogmobiclean(this.TAG, " method unSelectAll call ", GlobalData.FILE_NAME);
        for (int i = 0; i < this.arrContents.size(); i++) {
            this.arrContents.get(i).unSelectAll();
        }
    }

    public void updateHashMapFileTypeToMediaList() {
        this.hmFileTypeToMediaList = new ConcurrentHashMap(this.arrContents.size());
        for (MediaList mediaList : this.arrContents) {
            try {
                this.hmFileTypeToMediaList.put(Integer.valueOf(mediaList.mediaType.ordinal()), mediaList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
