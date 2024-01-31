package com.cleanPhone.mobileCleaner.processes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ProcFile extends File implements Parcelable {
    public static final Creator<ProcFile> CREATOR = new Creator<ProcFile>() {

        @Override
        public ProcFile createFromParcel(Parcel parcel) {
            return new ProcFile(parcel);
        }


        @Override
        public ProcFile[] newArray(int i) {
            return new ProcFile[i];
        }
    };
    public final String content;

    public ProcFile(String str) throws IOException {
        super(str);
        this.content = readFile(str);
    }

    public static String readFile(String str) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader(str));
            try {
                String str2 = "";
                for (String readLine = bufferedReader2.readLine(); readLine != null; readLine = bufferedReader2.readLine()) {
                    sb.append(str2);
                    sb.append(readLine);
                    str2 = IOUtils.LINE_SEPARATOR_UNIX;
                }
                String sb2 = sb.toString();
                try {
                    bufferedReader2.close();
                } catch (IOException unused) {
                }
                return sb2;
            } catch (Throwable th) {
                th = th;
                bufferedReader = bufferedReader2;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException unused2) {
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {

        }
        return str;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public long length() {
        return this.content.length();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getAbsolutePath());
        parcel.writeString(this.content);
    }

    public ProcFile(Parcel parcel) {
        super(parcel.readString());
        this.content = parcel.readString();
    }
}
