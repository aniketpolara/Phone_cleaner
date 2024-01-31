package com.cleanPhone.mobileCleaner.utility;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class ScalingUtilities {
    public enum ScalingLogic {
        CROP,
        FIT
    }

    public static Rect calculateDstRect(int i, int i2, int i3, int i4, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            float f = i / i2;
            float f2 = i3;
            float f3 = i4;
            if (f > f2 / f3) {
                return new Rect(0, 0, i3, (int) (f2 / f));
            }
            return new Rect(0, 0, (int) (f3 * f), i4);
        }
        return new Rect(0, 0, i3, i4);
    }

    public static int calculateSampleSize(int i, int i2, int i3, int i4, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            if (i / i2 > i3 / i4) {
                return i / i3;
            }
            return i2 / i4;
        } else if (i / i2 > i3 / i4) {
            return i2 / i4;
        } else {
            return i / i3;
        }
    }

    public static Rect calculateSrcRect(int i, int i2, int i3, int i4, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            float f = i;
            float f2 = i2;
            float f3 = i3 / i4;
            if (f / f2 > f3) {
                int i5 = (int) (f2 * f3);
                int i6 = (i - i5) / 2;
                return new Rect(i6, 0, i5 + i6, i2);
            }
            int i7 = (int) (f / f3);
            int i8 = (i2 - i7) / 2;
            return new Rect(0, i8, i, i7 + i8);
        }
        return new Rect(0, 0, i, i2);
    }

    public static Bitmap createScaledBitmap(Bitmap bitmap, int i, int i2, ScalingLogic scalingLogic) {
        Rect calculateSrcRect = calculateSrcRect(bitmap.getWidth(), bitmap.getHeight(), i, i2, scalingLogic);
        Rect calculateDstRect = calculateDstRect(bitmap.getWidth(), bitmap.getHeight(), i, i2, scalingLogic);
        System.gc();
        int width = calculateDstRect.width();
        int height = calculateDstRect.height();
        Bitmap bitmap2 = null;
        while (true) {
            for (boolean z = true; z; z = false) {
                try {
                    bitmap2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    new Canvas(bitmap2).drawBitmap(bitmap, calculateSrcRect, calculateDstRect, new Paint(2));
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    width = (width * 90) / 100;
                    height = (height * 90) / 100;
                }
            }
            return bitmap2;
        }
    }

    public static Bitmap decodeResource(Resources resources, int i, int i2, int i3, ScalingLogic scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, i2, i3, scalingLogic);
        return BitmapFactory.decodeResource(resources, i, options);
    }
}
