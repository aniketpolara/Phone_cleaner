package com.cleanPhone.mobileCleaner.processes;

import android.os.Parcel;
import android.os.Parcelable;

public class ControlGroup implements Parcelable {
    public static final Creator<ControlGroup> CREATOR = new Creator<ControlGroup>() {

        @Override
        public ControlGroup createFromParcel(Parcel parcel) {
            return new ControlGroup(parcel);
        }

        @Override
        public ControlGroup[] newArray(int i) {
            return new ControlGroup[i];
        }
    };
    public final String group;
    public final int id;
    public final String subsystems;

    public ControlGroup(String str) throws NumberFormatException, IndexOutOfBoundsException {
        String[] split = str.split(":");
        this.id = Integer.parseInt(split[0]);
        this.subsystems = split[1];
        this.group = split[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return String.format("%d:%s:%s", Integer.valueOf(this.id), this.subsystems, this.group);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.subsystems);
        parcel.writeString(this.group);
    }

    public ControlGroup(Parcel parcel) {
        this.id = parcel.readInt();
        this.subsystems = parcel.readString();
        this.group = parcel.readString();
    }
}
