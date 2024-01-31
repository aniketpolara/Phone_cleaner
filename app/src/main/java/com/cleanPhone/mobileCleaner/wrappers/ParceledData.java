package com.cleanPhone.mobileCleaner.wrappers;

import android.os.Parcel;
import android.os.Parcelable;


public class ParceledData implements Parcelable {
    public static final Creator<ParceledData> CREATOR = new Creator<ParceledData>() {
        @Override
        public ParceledData createFromParcel(Parcel parcel) {
            return new ParceledData(parcel);
        }

        @Override
        public ParceledData[] newArray(int i) {
            return new ParceledData[i];
        }
    };
    public int age;
    public String name;

    public ParceledData(Parcel parcel) {
        this.name = parcel.readString();
        this.age = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeInt(this.age);
    }

    public ParceledData(String str, int i) {
        this.name = str;
        this.age = i;
    }
}
