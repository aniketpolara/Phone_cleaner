package com.cleanPhone.mobileCleaner.similerphotos;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.ImageUtil;

import java.lang.ref.WeakReference;

public abstract class ImageWorker {
    private static final String TAG = "ImageWorker";
    private int HGT;
    public Context b;
    private ImageCache mImageCache;
    public Resources mResources;
    private boolean mFadeInBitmap = true;
    private boolean mExitTasksEarly = false;
    public boolean mPauseWork = false;
    private final Object mPauseWorkLock = new Object();
    public int f5092c = (GlobalData.deviceWidth * 23) / 100;

    public static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources resources, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(resources, bitmap);
            this.bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return this.bitmapWorkerTaskReference.get();
        }
    }

    public class BitmapWorkerTask extends AsyncTask<Void, Void, BitmapDrawable> {
        private final Context context;
        private final int id;
        private final WeakReference<ImageView> imageViewReference;
        private Object mData;

        public BitmapWorkerTask(Object obj, ImageView imageView, Context context, int i) {
            this.mData = obj;
            this.context = context;
            this.id = i;
            this.imageViewReference = new WeakReference<>(imageView);
        }

        private ImageView getAttachedImageView() {
            ImageView imageView = this.imageViewReference.get();
            if (this == ImageWorker.getBitmapWorkerTask(imageView)) {
                return imageView;
            }
            return null;
        }

        @Override
        public BitmapDrawable doInBackground(Void... voidArr) {
            String valueOf = String.valueOf(this.mData);
            synchronized (ImageWorker.this.mPauseWorkLock) {
                while (ImageWorker.this.mPauseWork && !isCancelled()) {
                    try {
                        ImageWorker.this.mPauseWorkLock.wait();
                    } catch (InterruptedException unused) {
                    }
                }
            }
            BitmapDrawable bitmapDrawable = null;
            Bitmap bitmapFromDiskCache = (ImageWorker.this.mImageCache == null || isCancelled() || getAttachedImageView() == null || ImageWorker.this.mExitTasksEarly) ? null : ImageWorker.this.mImageCache.getBitmapFromDiskCache(valueOf);
            if (bitmapFromDiskCache == null && !isCancelled() && getAttachedImageView() != null && !ImageWorker.this.mExitTasksEarly) {
                bitmapFromDiskCache = ImageWorker.this.processBitmap(this.mData, this.context, this.id);
            }
            if (bitmapFromDiskCache != null) {
                if (ImageResizer.hasHoneycomb()) {
                    bitmapDrawable = new BitmapDrawable(ImageWorker.this.mResources, bitmapFromDiskCache);
                } else {
                    bitmapDrawable = new RecyclingBitmapDrawable(ImageWorker.this.mResources, bitmapFromDiskCache);
                }
                if (ImageWorker.this.mImageCache != null) {
                    ImageWorker.this.mImageCache.addBitmapToCache(valueOf, bitmapDrawable);
                }
            }
            return bitmapDrawable;
        }

        @Override
        public void onCancelled(BitmapDrawable bitmapDrawable) {
            super.onCancelled( bitmapDrawable);
            synchronized (ImageWorker.this.mPauseWorkLock) {
                ImageWorker.this.mPauseWorkLock.notifyAll();
            }
        }

        @Override
        public void onPostExecute(BitmapDrawable bitmapDrawable) {
            bitmapDrawable = (isCancelled() || ImageWorker.this.mExitTasksEarly) ? null : null;
            ImageView attachedImageView = getAttachedImageView();
            if (bitmapDrawable == null || attachedImageView == null) {
                return;
            }
            ImageWorker.this.setImageDrawable(attachedImageView, bitmapDrawable);
        }
    }


    public class CacheAsyncTask extends AsyncTask<Object, Void, Void> {
        public CacheAsyncTask() {
        }

        @Override
        public Void doInBackground(Object... objArr) {
            int intValue = ((Integer) objArr[0]).intValue();
            if (intValue == 0) {
                ImageWorker.this.clearCacheInternal();
                return null;
            } else if (intValue == 1) {
                ImageWorker.this.initDiskCacheInternal();
                return null;
            } else if (intValue == 2) {
                ImageWorker.this.flushCacheInternal();
                return null;
            } else if (intValue != 3) {
                return null;
            } else {
                ImageWorker.this.closeCacheInternal();
                return null;
            }
        }
    }

    public ImageWorker(Context context) {
        this.mResources = context.getResources();
    }


    public static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                return ((AsyncDrawable) drawable).getBitmapWorkerTask();
            }
            return null;
        }
        return null;
    }


    public void setImageDrawable(ImageView imageView, Drawable drawable) {
        if (this.mFadeInBitmap) {
            ImageUtil.setScalledBackground(imageView, this.b, ((BitmapDrawable) drawable).getBitmap(), 23, this.HGT, 3);
        } else {
            ImageUtil.setScalledBackground(imageView, this.b, ((BitmapDrawable) drawable).getBitmap(), 23, this.HGT, 3);
        }
    }



    public void clearCacheInternal() {
        ImageCache imageCache = this.mImageCache;
        if (imageCache != null) {
            imageCache.clearCache();
        }
    }


    public void closeCacheInternal() {
        ImageCache imageCache = this.mImageCache;
        if (imageCache != null) {
            imageCache.close();
            this.mImageCache = null;
        }
    }

    public void flushCacheInternal() {
        ImageCache imageCache = this.mImageCache;
        if (imageCache != null) {
            imageCache.flush();
        }
    }

    public ImageCache getImageCache() {
        return this.mImageCache;
    }

    public void initDiskCacheInternal() {
        ImageCache imageCache = this.mImageCache;
        if (imageCache != null) {
            imageCache.initDiskCache();
        }
    }

    public abstract Bitmap processBitmap(Object obj, Context context, int i);

}
