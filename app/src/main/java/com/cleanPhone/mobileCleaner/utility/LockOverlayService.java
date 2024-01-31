package com.cleanPhone.mobileCleaner.utility;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.broadcasting.DeviceStateChangedListner;

public class LockOverlayService extends Service {
    private static final String TAG = "LockOverlayService";
    public SharedPrefUtil f5307a;
    public Context b;
    private BroadcastReceiver overlayReceiver = new BroadcastReceiver() { // from class: com.mobiclean.phoneclean.utility.LockOverlayService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            LockOverlayService lockOverlayService = LockOverlayService.this;
            lockOverlayService.showCharging = lockOverlayService.f5307a.getBoolean_lock(SharedPrefUtil.SHOW_CHARGEING);
            if (!LockOverlayService.this.showCharging) {
                LockOverlayService.this.stopSelf();
            } else if (MobiClean.getInstance().iscall_ser) {
            } else {
                String action = intent.getAction();
                boolean inKeyguardRestrictedInputMode = ((KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE)).inKeyguardRestrictedInputMode();
                int intExtra = intent.getIntExtra("plugged", 0);
                if (!inKeyguardRestrictedInputMode || action.equals("android.intent.action.SCREEN_ON") || action.equals("android.intent.action.SCREEN_OFF") || action.equals("android.intent.action.ACTION_POWER_CONNECTED") || intExtra != 2) {
                }
            }
        }
    };
    private DeviceStateChangedListner phoneStateListener;
    private boolean showCharging;

    private void registerOverlayReceiver(Intent intent) {
        boolean boolean_lock = this.f5307a.getBoolean_lock(SharedPrefUtil.SHOW_CHARGEING);
        this.showCharging = boolean_lock;
        if (!boolean_lock) {
            stopSelf();
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.overlayReceiver, intentFilter);
    }

    private void unregisterOverlayReceiver() {
        try {
            unregisterReceiver(this.overlayReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.phoneStateListener = new DeviceStateChangedListner();
        try {
            ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).listen(this.phoneStateListener, 32);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        try {
            ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).listen(this.phoneStateListener, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            unregisterOverlayReceiver();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        this.b = getBaseContext();
        this.f5307a = new SharedPrefUtil(this.b);
        registerOverlayReceiver(intent);
        return Service.START_STICKY;
    }
}
