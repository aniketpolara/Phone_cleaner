package com.cleanPhone.mobileCleaner.processes;

import android.os.Parcel;

import java.io.IOException;

public final class Stat extends ProcFile {
    public static final Creator<Stat> CREATOR = new Creator<Stat>() {

        @Override
        public Stat createFromParcel(Parcel parcel) {
            return new Stat(parcel);
        }


        @Override
        public Stat[] newArray(int i) {
            return new Stat[i];
        }
    };
    private final String[] fields;

    public static Stat get(int i) throws IOException {
        return new Stat(String.format("/proc/%d/stat", Integer.valueOf(i)));
    }


    public int flags() {
        return Integer.parseInt(this.fields[8]);
    }

    public String getComm() {
        return this.fields[1].replace("(", "").replace(")", "");
    }

    public int getPid() {
        return Integer.parseInt(this.fields[0]);
    }

    public int nice() {
        return Integer.parseInt(this.fields[18]);
    }

    public int policy() {
        return Integer.parseInt(this.fields[40]);
    }

    public char state() {
        return this.fields[2].charAt(0);
    }

    public long stime() {
        return Long.parseLong(this.fields[14]);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeStringArray(this.fields);
    }

    private Stat(String str) throws IOException {
        super(str);
        this.fields = this.content.split("\\s+");
    }

    private Stat(Parcel parcel) {
        super(parcel);
        this.fields = parcel.createStringArray();
    }
}
