package com.cleanPhone.mobileCleaner.socialmedia;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.LruCache;

import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;

import java.lang.ref.WeakReference;

public class CacheGridActivity extends AppCompatActivity implements AbsListView.OnScrollListener {
    private LruCache<String, Bitmap> mMemoryCache;
    public boolean mPauseWork = false;
    private final Object mPauseWorkLock = new Object();
    private Bitmap mPlaceHolderBitmap;

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


    public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private int data = 0;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            this.imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        public Bitmap doInBackground(Integer... numArr) {
            this.data = numArr[0].intValue();
            Bitmap decodeResource = BitmapFactory.decodeResource(CacheGridActivity.this.getResources(), this.data);
            CacheGridActivity.this.addBitmapToMemoryCache(String.valueOf(numArr[0]), decodeResource);
            return decodeResource;
        }

        @Override
        public void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            WeakReference<ImageView> weakReference = this.imageViewReference;
            if (weakReference == null || bitmap == null) {
                return;
            }
            ImageView imageView = weakReference.get();
            if (this != CacheGridActivity.getBitmapWorkerTask(imageView) || imageView == null) {
                return;
            }
            imageView.setImageBitmap(bitmap);
        }
    }

    public static boolean cancelPotentialWork(int i, ImageView imageView) {
        BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            if (bitmapWorkerTask.data == i) {
                return false;
            }
            bitmapWorkerTask.cancel(true);
        }
        return true;
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

    private void initCache() {
        this.mPlaceHolderBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.empty_photo);
        this.mMemoryCache = new LruCache<String, Bitmap>( ((int) (Runtime.getRuntime().maxMemory() / 1024)) / 2) { // from class: com.mobiclean.phoneclean.socialmedia.CacheGridActivity.1
            @Override
            @SuppressLint({"NewApi"})
            public int sizeOf(String str, Bitmap bitmap) {
                if (Build.VERSION.SDK_INT >= 12) {
                    return bitmap.getByteCount() / 1024;
                }
                return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String str, Bitmap bitmap) {
        if (getBitmapFromMemCache(str) == null) {
            this.mMemoryCache.put(str, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String str) {
        return this.mMemoryCache.get(str);
    }

    public void loadBitmap(int i, ImageView imageView) {
        Bitmap bitmapFromMemCache = getBitmapFromMemCache(String.valueOf(i));
        if (bitmapFromMemCache != null) {
            imageView.setImageBitmap(bitmapFromMemCache);
        } else if (cancelPotentialWork(i, imageView)) {
            BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(imageView);
            imageView.setImageDrawable(new AsyncDrawable(getResources(), this.mPlaceHolderBitmap, bitmapWorkerTask));
            bitmapWorkerTask.execute(Integer.valueOf(i));
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        initCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mMemoryCache.evictAll();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onPause() {
        super.onPause();
        setPauseWork(false);
    }

    @Override
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        setPauseWork(true);
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == 2) {
            if (Build.VERSION.SDK_INT < 11) {
                setPauseWork(true);
                return;
            }
            return;
        }
        setPauseWork(false);
    }

    public void setPauseWork(boolean z) {
        synchronized (this.mPauseWorkLock) {
            this.mPauseWork = z;
            if (!z) {
                this.mPauseWorkLock.notifyAll();
            }
        }
    }
}
