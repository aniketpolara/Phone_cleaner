package com.cleanPhone.mobileCleaner.wrappers;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.Date;


public class BigSizeFilesWrapper implements Parcelable {
    public static final Creator<BigSizeFilesWrapper> CREATOR = new Creator<BigSizeFilesWrapper>() {
        public BigSizeFilesWrapper createFromParcel(Parcel parcel) {
            return new BigSizeFilesWrapper(parcel);
        }

        @Override
        public BigSizeFilesWrapper[] newArray(int i) {
            return new BigSizeFilesWrapper[i];
        }
    };
    public long dateTaken;
    public String ext;
    public File file;
    public int id;
    public boolean ischecked;
    public Date lastModDate;
    public long lastModified;
    public String name;
    public int orientation;
    public String path;
    public long size;
    private String sizegroup;
    public long totalSize;
    public String type;

    public BigSizeFilesWrapper() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.lastModified);
        parcel.writeInt(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.path);
        parcel.writeInt(this.orientation);
        parcel.writeLong(this.size);
        parcel.writeString(this.type);
        parcel.writeString(this.sizegroup);
        parcel.writeByte(this.ischecked ? (byte) 1 : (byte) 0);
        parcel.writeString(this.ext);
        parcel.writeLong(this.totalSize);
    }

    public BigSizeFilesWrapper(File file) {
        this.file = file;
        this.name = file.getName();
        this.path = file.getAbsolutePath();
        this.size = file.length();
        this.ischecked = false;
        this.lastModified = file.lastModified();
        this.lastModDate = new Date(file.lastModified());
    }

    public BigSizeFilesWrapper(Parcel parcel) {
        this.lastModified = parcel.readLong();
        this.id = parcel.readInt();
        this.name = parcel.readString();
        this.path = parcel.readString();
        this.orientation = parcel.readInt();
        this.size = parcel.readLong();
        this.type = parcel.readString();
        this.sizegroup = parcel.readString();
        this.ischecked = parcel.readByte() != 0;
        this.ext = parcel.readString();
        this.totalSize = parcel.readLong();
    }
}
