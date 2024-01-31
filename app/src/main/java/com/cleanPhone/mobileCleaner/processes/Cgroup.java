package com.cleanPhone.mobileCleaner.processes;

import android.os.Parcel;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public final class Cgroup extends ProcFile {
    public static final Creator<Cgroup> CREATOR = new Creator<Cgroup>() {

        @Override
        public Cgroup createFromParcel(Parcel parcel) {
            return new Cgroup(parcel);
        }

        @Override
        public Cgroup[] newArray(int i) {
            return new Cgroup[i];
        }
    };
    public final ArrayList<ControlGroup> groups;

    public static Cgroup get(int i) throws IOException {
        return new Cgroup(String.format("/proc/%d/cgroup", Integer.valueOf(i)));
    }

    public ControlGroup getGroup(String str) {
        Iterator<ControlGroup> it = this.groups.iterator();
        while (it.hasNext()) {
            ControlGroup next = it.next();
            for (String str2 : next.subsystems.split(",")) {
                if (str2.equals(str)) {
                    return next;
                }
            }
        }
        return null;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeTypedList(this.groups);
    }

    private Cgroup(String str) throws IOException {
        super(str);
        String[] split = this.content.split(IOUtils.LINE_SEPARATOR_UNIX);
        this.groups = new ArrayList<>();
        for (String str2 : split) {
            try {
                this.groups.add(new ControlGroup(str2));
            } catch (Exception unused) {
            }
        }
    }

    private Cgroup(Parcel parcel) {
        super(parcel);
        this.groups = parcel.createTypedArrayList(ControlGroup.CREATOR);
    }
}
