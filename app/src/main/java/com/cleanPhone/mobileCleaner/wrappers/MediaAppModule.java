package com.cleanPhone.mobileCleaner.wrappers;

import java.util.ArrayList;

public class MediaAppModule {
    public long recoveredSize;
    public ArrayList<AppJunk> socialApp = new ArrayList<>();
    public long totalCount;
    public long totalSize;

    public ArrayList getAppsHavingData() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.socialApp.size(); i++) {
            if (this.socialApp.get(i).appJunkSize > 0) {
                arrayList.add(this.socialApp.get(i));
            }
        }
        return arrayList;
    }

    public void refresh() {
        this.totalSize = 0L;
        for (int i = 0; i < this.socialApp.size(); i++) {
            this.socialApp.get(i).refresh();
            this.totalSize += this.socialApp.get(i).appJunkSize;
        }
    }
}
