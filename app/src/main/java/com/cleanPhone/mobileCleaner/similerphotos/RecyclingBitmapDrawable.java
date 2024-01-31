package com.cleanPhone.mobileCleaner.similerphotos;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class RecyclingBitmapDrawable extends BitmapDrawable {
    private int mCacheRefCount;
    private int mDisplayRefCount;
    private boolean mHasBeenDisplayed;

    public RecyclingBitmapDrawable(Resources resources, Bitmap bitmap) {
        super(resources, bitmap);
        this.mCacheRefCount = 0;
        this.mDisplayRefCount = 0;
    }

    private synchronized void checkState() {
        if (this.mCacheRefCount <= 0 && this.mDisplayRefCount <= 0 && this.mHasBeenDisplayed && hasValidBitmap()) {
            getBitmap().recycle();
        }
    }

    private synchronized boolean hasValidBitmap() {
        boolean z = false;
        Bitmap bitmap = getBitmap();
        if (bitmap != null) {
            z = bitmap.isRecycled() ? false : true;
        }
        return z;
    }

    public void setIsCached(boolean z) {
        synchronized (this) {
            if (z) {
                this.mCacheRefCount++;
            } else {
                this.mCacheRefCount--;
            }
        }
        checkState();
    }

    public void setIsDisplayed(boolean z) {
        synchronized (this) {
            if (z) {
                this.mDisplayRefCount++;
                this.mHasBeenDisplayed = true;
            } else {
                this.mDisplayRefCount--;
            }
        }
        checkState();
    }
}
