package com.cleanPhone.mobileCleaner.processes;

import android.os.Parcel;

import java.io.IOException;

public final class Statm extends ProcFile {
    public static final Creator<Statm> CREATOR = new Creator<Statm>() {

        @Override
        public Statm createFromParcel(Parcel parcel) {
            return new Statm(parcel);
        }

        @Override
        public Statm[] newArray(int i) {
            return new Statm[i];
        }
    };
    public final String[] fields;

    public static Statm get(int i) throws IOException {
        return new Statm(String.format("/proc/%d/statm", Integer.valueOf(i)));
    }


    public long getSize() {
        return Long.parseLong(this.fields[0]) * 1024;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeStringArray(this.fields);
    }

    private Statm(String str) throws IOException {
        super(str);
        this.fields = this.content.split("\\s+");
    }

    private Statm(Parcel parcel) {
        super(parcel);
        this.fields = parcel.createStringArray();
    }
}
