package com.cleanPhone.mobileCleaner.utility;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;

import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.similerphotos.ImageResizer;

import java.lang.ref.WeakReference;

public class CacheActivity extends ParentActivity implements AbsListView.OnScrollListener {
    private LruCache<String, Bitmap> mMemoryCache;
    public boolean mPauseWork = false;
    private final Object mPauseWorkLock = new Object();

    public static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources resources, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(resources, bitmap);
            this.bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
        }

        public BitmapWorkerTask a() {
            return this.bitmapWorkerTaskReference.get();
        }
    }

    public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String type;
        private int data = 0;
        private String img_path = null;

        public BitmapWorkerTask(ImageView imageView, String str) {
            this.imageViewReference = new WeakReference<>(imageView);
            this.type = str;
        }

        @Override
        public Bitmap doInBackground(String... strArr) {
            Bitmap bitmap;
            this.data = Integer.parseInt(strArr[0]);
            this.img_path = strArr[1];
            if (this.type.equalsIgnoreCase("videos")) {
                bitmap = MediaStore.Video.Thumbnails.getThumbnail(CacheActivity.this.getContentResolver(), this.data, 3, null);
                if (this.img_path.endsWith("7163110.mp4")) {
                    CacheActivity.this.checkThumbnailOfVideo(this.data, this.img_path, bitmap);
                }
                if (bitmap == null) {
                    bitmap = ThumbnailUtils.createVideoThumbnail(this.img_path, 3);
                }
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(CacheActivity.this.getResources(), R.drawable.my_files);
                }
            } else {
                Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(CacheActivity.this.getContentResolver(), this.data, 3, null);
                if (thumbnail == null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options.inJustDecodeBounds = true;
                    bitmap = BitmapFactory.decodeFile(this.img_path, options);
                    options.inSampleSize = ImageResizer.calculateInSampleSize(options, 50, 50);
                    options.inJustDecodeBounds = false;
                } else {
                    bitmap = thumbnail;
                }
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(CacheActivity.this.getResources(), R.drawable.gallery_image);
                }
            }
            CacheActivity.this.addBitmapToMemoryCache(String.valueOf(strArr[0]), bitmap);
            return bitmap;
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
            if (this != CacheActivity.getBitmapWorkerTask(imageView) || imageView == null) {
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


    public void checkThumbnailOfVideo(int i, String str, Bitmap bitmap) {
        if (MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), i, 3, null) == null) {
            ThumbnailUtils.createVideoThumbnail(str, 3);
        }
    }


    public static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                return ((AsyncDrawable) drawable).a();
            }
            return null;
        }
        return null;
    }

    private void initCache() {
        BitmapFactory.decodeResource(getResources(), R.drawable.ic_android);
        this.mMemoryCache = new LruCache<String, Bitmap>(((int) (Runtime.getRuntime().maxMemory() / 1024)) / 2) {
            @Override
            @SuppressLint({"NewApi"})
            public int sizeOf(@NonNull String str, @NonNull Bitmap bitmap) {
                if (Build.VERSION.SDK_INT >= 12) {
                    return bitmap.getByteCount() / 1024;
                }
                return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
            }
        };
    }


    public void addBitmapToMemoryCache(String str, Bitmap bitmap) {
        if (getBitmapFromMemCache(str) == null) {
            try {
                this.mMemoryCache.put(str, bitmap);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getBitmapFromMemCache(String str) {
        return this.mMemoryCache.get(str);
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
