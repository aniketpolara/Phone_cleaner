package com.cleanPhone.mobileCleaner.utility;

import android.graphics.Paint;
import android.graphics.Typeface;

public class DetermineTextSize {
    private static float calculateHeight(Paint.FontMetrics fontMetrics) {
        return fontMetrics.bottom - fontMetrics.top;
    }

    public static int determineTextSize(Typeface typeface, float f) {
        Paint paint = new Paint();
        paint.setTypeface(typeface);
        int i = (int) f;
        paint.setTextSize(i);
        int i2 = i;
        for (float calculateHeight = calculateHeight(paint.getFontMetrics()); i2 != 0 && calculateHeight > f; calculateHeight = calculateHeight(paint.getFontMetrics())) {
            paint.setTextSize(i2);
            i2--;
        }
        return i2 == 0 ? i : i2;
    }
}
