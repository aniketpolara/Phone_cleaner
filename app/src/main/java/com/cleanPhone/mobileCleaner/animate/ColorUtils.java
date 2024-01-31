package com.cleanPhone.mobileCleaner.animate;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public class ColorUtils {
    public static int getRGBGradient(@ColorInt int i, @ColorInt int i2, float f) {
        int[] iArr = {interpolate(Color.red(i), Color.red(i2), f), interpolate(Color.green(i), Color.green(i2), f), interpolate(Color.blue(i), Color.blue(i2), f)};
        return Color.argb(255, iArr[0], iArr[1], iArr[2]);
    }

    private static int interpolate(float f, float f2, float f3) {
        return Math.round((f * f3) + (f2 * (1.0f - f3)));
    }
}
