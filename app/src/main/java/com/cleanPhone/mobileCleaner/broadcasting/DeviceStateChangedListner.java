package com.cleanPhone.mobileCleaner.broadcasting;

import android.telephony.PhoneStateListener;

import com.cleanPhone.mobileCleaner.MobiClean;


public class DeviceStateChangedListner extends PhoneStateListener {
    @Override
    public void onCallStateChanged(int i, String str) {
        super.onCallStateChanged(i, str);
        MobiClean.getInstance().lastState = i;
        if (i == 1) {
            MobiClean.getInstance().iscall_rec = true;
            MobiClean.getInstance().iscall_ser = true;
        } else if (i != 2 && i == 0) {
            MobiClean.getInstance().iscall_rec = false;
            MobiClean.getInstance().iscall_ser = false;
        }
    }
}
