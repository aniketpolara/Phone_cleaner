package com.cleanPhone.mobileCleaner.similerphotos;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.FileDescriptor;


public class ImageResizer extends ImageWorker {
    private static final String TAG = "ImageResizer";
    public int mImageHeight;
    public int mImageWidth;

    public ImageResizer(Context context, int i, int i2) {
        super(context);
        setImageSize(i, i2);
    }

    @TargetApi(11)
    private static void addInBitmapOptions(BitmapFactory.Options options, ImageCache imageCache) {
        Bitmap bitmapFromReusableSet;
        options.inMutable = true;
        if (imageCache == null || (bitmapFromReusableSet = imageCache.getBitmapFromReusableSet(options)) == null) {
            return;
        }
        options.inBitmap = bitmapFromReusableSet;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 > i2 && i7 / i5 > i) {
                i5 *= 2;
            }
            for (long j = (i4 * i3) / i5; j > i * i2 * 2; j /= 2) {
                i5 *= 2;
            }
        }
        return i5;
    }

    public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int i, int i2, ImageCache imageCache) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        if (hasHoneycomb()) {
            addInBitmapOptions(options, imageCache);
        }
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources resources, int i, int i2, int i3, ImageCache imageCache) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inSampleSize = calculateInSampleSize(options, i2, i3);
        if (hasHoneycomb()) {
            addInBitmapOptions(options, imageCache);
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, i, options);
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;
    }

    private Bitmap processBitmap(int i) {
        return decodeSampledBitmapFromResource(this.mResources, i, this.mImageWidth, this.mImageHeight, getImageCache());
    }

    public void setImageSize(int i, int i2) {
        this.mImageWidth = i;
        this.mImageHeight = i2;
    }

    public ImageResizer(Context context, int i) {
        super(context);
        setImageSize(i);
    }

    public void setImageSize(int i) {
        setImageSize(i, i);
    }

    @Override
    public Bitmap processBitmap(Object obj, Context context, int i) {
        return processBitmap(Integer.parseInt(String.valueOf(obj)));
    }
}
