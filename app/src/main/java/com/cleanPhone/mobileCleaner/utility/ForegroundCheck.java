package com.cleanPhone.mobileCleaner.utility;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class ForegroundCheck implements Application.ActivityLifecycleCallbacks {
    public static final String TAG = ForegroundCheck.class.getName();
    private static ForegroundCheck instance;
    private Runnable check;
    private boolean foreground = false;
    private boolean paused = true;
    private Handler handler = new Handler();
    private List<Listener> listeners = new CopyOnWriteArrayList();

    public interface Listener {
        void onBecameBackground();

        void onBecameForeground();
    }

    public static ForegroundCheck get(Application application) {
        if (instance == null) {
            init(application);
        }
        return instance;
    }

    public static ForegroundCheck init(Application application) {
        if (instance == null) {
            ForegroundCheck foregroundCheck = new ForegroundCheck();
            instance = foregroundCheck;
            application.registerActivityLifecycleCallbacks(foregroundCheck);
        }
        return instance;
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public boolean isBackground() {
        return !this.foreground;
    }

    public boolean isForeground() {
        return this.foreground;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
        this.paused = true;
        Runnable runnable = this.check;
        if (runnable != null) {
            this.handler.removeCallbacks(runnable);
        }
        Handler handler = this.handler;
        Runnable runnable2 = new Runnable() {
            @Override // java.lang.Runnable
            public void run() {
                if (ForegroundCheck.this.foreground && ForegroundCheck.this.paused) {
                    ForegroundCheck.this.foreground = false;
                    Log.i(ForegroundCheck.TAG, "went background");
                    for (Listener listener : ForegroundCheck.this.listeners) {
                        try {
                            listener.onBecameBackground();
                        } catch (Exception e) {
                            Log.e(ForegroundCheck.TAG, "Listener threw exception!", e);
                        }
                    }
                    return;
                }
                Log.i(ForegroundCheck.TAG, "still foreground");
            }
        };
        this.check = runnable2;
        handler.postDelayed(runnable2, 500L);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        this.paused = false;
        boolean z = !this.foreground;
        this.foreground = true;
        Runnable runnable = this.check;
        if (runnable != null) {
            this.handler.removeCallbacks(runnable);
        }
        if (z) {
            Log.i(TAG, "went foreground");
            for (Listener listener : this.listeners) {
                try {
                    listener.onBecameForeground();
                } catch (Exception e) {
                    Log.e(TAG, "Listener threw exception!", e);
                }
            }
            return;
        }
        Log.i(TAG, "still foreground");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public static ForegroundCheck get(Context context) {
        ForegroundCheck foregroundCheck = instance;
        if (foregroundCheck == null) {
            Context applicationContext = context.getApplicationContext();
            if (applicationContext instanceof Application) {
                init((Application) applicationContext);
            }
            throw new IllegalStateException("Foreground is not initialised and cannot obtain the Application object");
        }
        return foregroundCheck;
    }

    public static ForegroundCheck get() {
        if (instance == null) {
            instance = new ForegroundCheck();
        }
        return instance;
    }
}
