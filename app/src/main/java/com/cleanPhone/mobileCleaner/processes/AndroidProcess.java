package com.cleanPhone.mobileCleaner.processes;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.IOException;


public class AndroidProcess implements Parcelable {
    public static final Creator<AndroidProcess> CREATOR = new Creator<AndroidProcess>() {
        @Override
        public AndroidProcess createFromParcel(Parcel parcel) {
            return new AndroidProcess(parcel);
        }


        @Override
        public AndroidProcess[] newArray(int i) {
            return new AndroidProcess[i];
        }
    };
    public final String name;
    public final int pid;

    public AndroidProcess(int i) throws IOException {
        this.pid = i;
        this.name = a(i);
    }

    public static String a(int i) throws IOException {
        String str;
        try {
            str = ProcFile.readFile(String.format("/proc/%d/cmdline", Integer.valueOf(i))).trim();
        } catch (IOException unused) {
            str = null;
        }
        return TextUtils.isEmpty(str) ? Stat.get(i).getComm() : str;
    }

    public Cgroup cgroup() throws IOException {
        return Cgroup.get(this.pid);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public String read(String str) throws IOException {
        return ProcFile.readFile(String.format("/proc/%d/%s", Integer.valueOf(this.pid), str));
    }

    public Stat stat() throws IOException {
        return Stat.get(this.pid);
    }


    public Status status() throws IOException {
        return Status.get(this.pid);
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeInt(this.pid);
    }

    public AndroidProcess(Parcel parcel) {
        this.name = parcel.readString();
        this.pid = parcel.readInt();
    }
}
