package com.cleanPhone.mobileCleaner.progress;

import android.content.res.Resources;

public final class Utils {
    private Utils() {
    }

    public static float dp2px(Resources resources, float f) {
        return (f * resources.getDisplayMetrics().density) + 0.5f;
    }

    public static float sp2px(Resources resources, float f) {
        return f * resources.getDisplayMetrics().scaledDensity;
    }
}
