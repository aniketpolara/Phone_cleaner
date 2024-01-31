package com.cleanPhone.mobileCleaner.processes;

import android.os.Parcel;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;

import java.io.IOException;


public final class Status extends ProcFile {
    public static final Creator<Status> CREATOR = new Creator<Status>() {

        @Override
        public Status createFromParcel(Parcel parcel) {
            return new Status(parcel);
        }


        @Override
        public Status[] newArray(int i) {
            return new Status[i];
        }
    };

    public static Status get(int i) throws IOException {
        return new Status(String.format("/proc/%d/status", Integer.valueOf(i)));
    }


    public int getUid() {
        try {
            return Integer.parseInt(getValue("Uid").split("\\s+")[0]);
        } catch (Exception unused) {
            return -1;
        }
    }

    public String getValue(String str) {
        String[] split;
        for (String str2 : this.content.split(IOUtils.LINE_SEPARATOR_UNIX)) {
            if (str2.startsWith(str + ":")) {
                return str2.split(str + ":")[1].trim();
            }
        }
        return null;
    }

    private Status(String str) throws IOException {
        super(str);
    }

    private Status(Parcel parcel) {
        super(parcel);
    }
}
