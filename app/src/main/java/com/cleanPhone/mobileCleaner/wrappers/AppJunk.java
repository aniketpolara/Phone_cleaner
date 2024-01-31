package com.cleanPhone.mobileCleaner.wrappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AppJunk {
    public long appJunkSize;
    public long appJunkTotal;
    public String appName;
    public ArrayList<MediaJunkData> mediaJunkData = new ArrayList<>();
    public long selectedCount;
    public long selectedSize;

    public AppJunk(String str) {
        this.appName = str;
    }

    public void refresh() {
        this.appJunkSize = 0L;
        for (int i = 0; i < this.mediaJunkData.size(); i++) {
            this.mediaJunkData.get(i).totSelectedSize = 0L;
            this.mediaJunkData.get(i).totSelectedCount = 0L;
            ArrayList<BigSizeFilesWrapper> arrayList = this.mediaJunkData.get(i).dataList;
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                this.appJunkSize += arrayList.get(i2).size;
                arrayList.get(i2).ischecked = false;
            }
        }
    }

    public void select(BigSizeFilesWrapper bigSizeFilesWrapper) {
        this.selectedCount++;
        this.selectedSize += bigSizeFilesWrapper.size;
    }

    public void selectAll() {
        this.selectedSize = 0L;
        this.selectedCount = 0L;
        for (int i = 0; i < this.mediaJunkData.size(); i++) {
            for (int i2 = 0; i2 < this.mediaJunkData.get(i).dataList.size(); i2++) {
                if (this.mediaJunkData.get(i).dataList.get(i2).ischecked) {
                    this.selectedCount++;
                    this.selectedSize += this.mediaJunkData.get(i).dataList.get(i2).size;
                }
            }
        }
    }

    public void sort(int i) {
        int i2 = 0;
        if (i == 0) {
            while (i2 < this.mediaJunkData.size()) {
                try {
                    Collections.sort(this.mediaJunkData.get(i2).dataList, new Comparator<BigSizeFilesWrapper>() {
                        @Override
                        public int compare(BigSizeFilesWrapper bigSizeFilesWrapper, BigSizeFilesWrapper bigSizeFilesWrapper2) {
                            return bigSizeFilesWrapper.name.trim().compareToIgnoreCase(bigSizeFilesWrapper2.name.trim());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i2++;
            }
        } else if (i == 1) {
            while (i2 < this.mediaJunkData.size()) {
                try {
                    Collections.sort(this.mediaJunkData.get(i2).dataList, new Comparator<BigSizeFilesWrapper>() {
                        @Override
                        public int compare(BigSizeFilesWrapper bigSizeFilesWrapper, BigSizeFilesWrapper bigSizeFilesWrapper2) {
                            return Long.valueOf(bigSizeFilesWrapper2.size).compareTo(Long.valueOf(bigSizeFilesWrapper.size));
                        }
                    });
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                i2++;
            }
        } else if (i != 2) {
        } else {
            while (i2 < this.mediaJunkData.size()) {
                try {
                    Collections.sort(this.mediaJunkData.get(i2).dataList, new Comparator<BigSizeFilesWrapper>() {
                        @Override
                        public int compare(BigSizeFilesWrapper bigSizeFilesWrapper, BigSizeFilesWrapper bigSizeFilesWrapper2) {
                            return bigSizeFilesWrapper2.lastModified > bigSizeFilesWrapper.lastModified ? 1 : -1;
                        }
                    });
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                i2++;
            }
        }
    }

    public void unselect(BigSizeFilesWrapper bigSizeFilesWrapper) {
        this.selectedCount--;
        this.selectedSize -= bigSizeFilesWrapper.size;
    }

    public void unselectAll() {
        this.selectedSize = 0L;
        this.selectedCount = 0L;
        for (int i = 0; i < this.mediaJunkData.size(); i++) {
            for (int i2 = 0; i2 < this.mediaJunkData.get(i).dataList.size(); i2++) {
                if (this.mediaJunkData.get(i).dataList.get(i2).ischecked) {
                    this.selectedCount++;
                    this.selectedSize += this.mediaJunkData.get(i).dataList.get(i2).size;
                }
            }
        }
    }
}
