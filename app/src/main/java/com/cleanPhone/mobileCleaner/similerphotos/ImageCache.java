package com.cleanPhone.mobileCleaner.similerphotos;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import androidx.collection.LruCache;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.MessageDigestAlgorithms;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import kotlin.UByte;


public class ImageCache {
    private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    private static final int DEFAULT_COMPRESS_QUALITY = 70;
    private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
    private static final int DEFAULT_DISK_CACHE_SIZE = 10485760;
    private static final boolean DEFAULT_INIT_DISK_CACHE_ON_CREATE = false;
    private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
    private static final int DEFAULT_MEM_CACHE_SIZE = 5120;
    private static final int DISK_CACHE_INDEX = 0;
    private static final String TAG = "ImageCache";
    private ImageCacheParams mCacheParams;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private DiskLruCache mDiskLruCache;
    private LruCache<String, BitmapDrawable> mMemoryCache;
    private Set<SoftReference<Bitmap>> mReusableBitmaps;

    public static class ImageCacheParams {
        public File diskCacheDir;
        public int memCacheSize = ImageCache.DEFAULT_MEM_CACHE_SIZE;
        public int diskCacheSize = ImageCache.DEFAULT_DISK_CACHE_SIZE;
        public Bitmap.CompressFormat compressFormat = ImageCache.DEFAULT_COMPRESS_FORMAT;
        public int compressQuality = 70;
        public boolean memoryCacheEnabled = true;
        public boolean diskCacheEnabled = true;
        public boolean initDiskCacheOnCreate = false;

        public ImageCacheParams(Context context, String str) {
            File diskCacheDir = ImageCache.getDiskCacheDir(context, str);
            if (!diskCacheDir.exists()) {
                diskCacheDir.mkdirs();
            }
            this.diskCacheDir = diskCacheDir;
        }

    }

    public static class RetainFragment extends Fragment {
        private Object mObject;

        public Object getObject() {
            return this.mObject;
        }

        @Override
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setRetainInstance(true);
        }

        public void setObject(Object obj) {
            this.mObject = obj;
        }
    }

    private ImageCache(ImageCacheParams imageCacheParams) {
        init(imageCacheParams);
    }

    private static int bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
            if (hexString.length() == 1) {
                sb.append('0');
            }
            sb.append(hexString);
        }
        return Integer.parseInt(sb.toString());
    }

    @TargetApi(19)
    private static boolean canUseForInBitmap(Bitmap bitmap, BitmapFactory.Options options) {
        if (!Util.hasKitKat()) {
            return bitmap.getWidth() == options.outWidth && bitmap.getHeight() == options.outHeight && options.inSampleSize == 1;
        }
        int i = options.outWidth;
        int i2 = options.inSampleSize;
        return ((i / i2) * (options.outHeight / i2)) * getBytesPerPixel(bitmap.getConfig()) <= bitmap.getAllocationByteCount();
    }

    private static RetainFragment findOrCreateRetainFragment(FragmentManager fragmentManager) {
        RetainFragment retainFragment = (RetainFragment) fragmentManager.findFragmentByTag(TAG);
        if (retainFragment == null) {
            RetainFragment retainFragment2 = new RetainFragment();
            fragmentManager.beginTransaction().add(retainFragment2, TAG).commitAllowingStateLoss();
            return retainFragment2;
        }
        return retainFragment;
    }

    @TargetApi(19)
    public static int getBitmapSize(BitmapDrawable bitmapDrawable) {
        Bitmap bitmap = bitmapDrawable.getBitmap();
        if (Util.hasKitKat()) {
            return bitmap.getAllocationByteCount();
        }
        if (Util.hasHoneycombMR1()) {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    private static int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        }
        if (config == Bitmap.Config.RGB_565 || config == Bitmap.Config.ARGB_4444) {
            return 2;
        }
        if (config == Bitmap.Config.ALPHA_8) {
        }
        return 1;
    }

    public static File getDiskCacheDir(Context context, String str) {
        String path;
        try {
            try {
                if (!"mounted".equals(Environment.getExternalStorageState()) && isExternalStorageRemovable()) {
                    path = context.getCacheDir().getPath();
                    return new File(path + File.separator + str);
                }
                path = getExternalCacheDir(context).getPath();
                return new File(path + File.separator + str);
            } catch (Exception unused) {
                return new File(context.getCacheDir().getPath() + File.separator + str);
            }
        } catch (Exception unused2) {
            return new File(getExternalCacheDir(context).getPath() + File.separator + str);
        }
    }

    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        if (Util.hasFroyo()) {
            return context.getExternalCacheDir();
        }
        return new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/cache/"));
    }

    public static ImageCache getInstance(FragmentManager fragmentManager, ImageCacheParams imageCacheParams) {
        RetainFragment findOrCreateRetainFragment = findOrCreateRetainFragment(fragmentManager);
        ImageCache imageCache = (ImageCache) findOrCreateRetainFragment.getObject();
        if (imageCache == null) {
            ImageCache imageCache2 = new ImageCache(imageCacheParams);
            findOrCreateRetainFragment.setObject(imageCache2);
            return imageCache2;
        }
        return imageCache;
    }

    @TargetApi(9)
    public static long getUsableSpace(File file) {
        if (Util.hasGingerbread()) {
            return file.getUsableSpace();
        }
        StatFs statFs = new StatFs(file.getPath());
        return statFs.getBlockSize() * statFs.getAvailableBlocks();
    }

    public static Object hashKeyForDisk(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            messageDigest.update(str.getBytes());
            return bytesToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException unused) {
            return String.valueOf(str.hashCode());
        }
    }

    private void init(ImageCacheParams imageCacheParams) {
        this.mCacheParams = imageCacheParams;
        if (imageCacheParams.memoryCacheEnabled) {
            if (ImageResizer.hasHoneycomb()) {
                this.mReusableBitmaps = Collections.synchronizedSet(new HashSet());
            }
            this.mMemoryCache = new LruCache<String, BitmapDrawable>(this.mCacheParams.memCacheSize) {
                @Override
                public void entryRemoved(boolean z, String str, BitmapDrawable bitmapDrawable, BitmapDrawable bitmapDrawable2) {
                    if (RecyclingBitmapDrawable.class.isInstance(bitmapDrawable)) {
                        ((RecyclingBitmapDrawable) bitmapDrawable).setIsCached(false);
                    } else if (ImageResizer.hasHoneycomb()) {
                        ImageCache.this.mReusableBitmaps.add(new SoftReference(bitmapDrawable.getBitmap()));
                    }
                }

                @Override
                public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                    int bitmapSize = ImageCache.getBitmapSize(bitmapDrawable) / 1024;
                    if (bitmapSize == 0) {
                        return 1;
                    }
                    return bitmapSize;
                }
            };
        }
        if (imageCacheParams.initDiskCacheOnCreate) {
            initDiskCache();
        }
    }

    @TargetApi(9)
    public static boolean isExternalStorageRemovable() {
        if (Util.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public void addBitmapToCache(String str, BitmapDrawable bitmapDrawable) {
        if (str == null || bitmapDrawable == null) {
            return;
        }
        if (this.mMemoryCache != null) {
            if (RecyclingBitmapDrawable.class.isInstance(bitmapDrawable)) {
                ((RecyclingBitmapDrawable) bitmapDrawable).setIsCached(true);
            }
            this.mMemoryCache.put(str, bitmapDrawable);
        }
        synchronized (this.mDiskCacheLock) {
            if (this.mDiskLruCache != null) {
                int hashKeyForDisk = (int) hashKeyForDisk(str);
                OutputStream outputStream = null;
                try {
                    try {
                        DiskLruCache.Snapshot snapshot = this.mDiskLruCache.get(String.valueOf(hashKeyForDisk));
                        if (snapshot == null) {
                            DiskLruCache.Editor edit = this.mDiskLruCache.edit(String.valueOf(hashKeyForDisk));
                            if (edit != null) {
                                outputStream = edit.newOutputStream(0);
                                Bitmap bitmap = bitmapDrawable.getBitmap();
                                ImageCacheParams imageCacheParams = this.mCacheParams;
                                bitmap.compress(imageCacheParams.compressFormat, imageCacheParams.compressQuality, outputStream);
                                edit.commit();
                                outputStream.close();
                            }
                        } else {
                            snapshot.getInputStream(0).close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "addBitmapToCache - " + e);
                    }
                } catch (Exception e2) {
                    Log.e(TAG, "addBitmapToCache - " + e2);
                    if (0 != 0) {
                        try {
                            outputStream.close();
                        } catch (IOException unused) {
                        }
                    }
                }
            }
        }
    }

    public void clearCache() {
        LruCache<String, BitmapDrawable> lruCache = this.mMemoryCache;
        if (lruCache != null) {
            lruCache.evictAll();
        }
        synchronized (this.mDiskCacheLock) {
            this.mDiskCacheStarting = true;
            DiskLruCache diskLruCache = this.mDiskLruCache;
            if (diskLruCache != null && !diskLruCache.isClosed()) {
                try {
                    this.mDiskLruCache.delete();
                } catch (IOException unused) {
                }
                this.mDiskLruCache = null;
                initDiskCache();
            }
        }
    }

    public void close() {
        synchronized (this.mDiskCacheLock) {
            DiskLruCache diskLruCache = this.mDiskLruCache;
            if (diskLruCache != null) {
                try {
                    if (!diskLruCache.isClosed()) {
                        this.mDiskLruCache.close();
                        this.mDiskLruCache = null;
                    }
                } catch (IOException e) {
                    Log.e(TAG, "close - " + e);
                }
            }
        }
    }

    public void flush() {
        synchronized (this.mDiskCacheLock) {
            DiskLruCache diskLruCache = this.mDiskLruCache;
            if (diskLruCache != null) {
                try {
                    diskLruCache.flush();
                } catch (IOException unused) {
                }
            }
        }
    }

    public Bitmap getBitmapFromDiskCache(String str) {
        Bitmap bitmap;
        InputStream inputStream;
        Bitmap bitmap2;
        int hashKeyForDisk = (int) hashKeyForDisk(str);
        synchronized (this.mDiskCacheLock) {
            while (this.mDiskCacheStarting) {
                try {
                    this.mDiskCacheLock.wait();
                } catch (InterruptedException unused) {
                }
            }
            DiskLruCache diskLruCache = this.mDiskLruCache;
            bitmap = null;
            bitmap = null;
            bitmap = null;
            bitmap = null;
            Bitmap decodeSampledBitmapFromDescriptor = null;
            InputStream inputStream2 = null;
            int r2 = 0;
            try {
                if (diskLruCache != null) {
                    try {
                        DiskLruCache.Snapshot snapshot = diskLruCache.get(String.valueOf(hashKeyForDisk));
                        if (snapshot != null) {
                            inputStream = snapshot.getInputStream(0);
                            if (inputStream != null) {
                                try {
                                    decodeSampledBitmapFromDescriptor = ImageResizer.decodeSampledBitmapFromDescriptor(((FileInputStream) inputStream).getFD(), Integer.MAX_VALUE, Integer.MAX_VALUE, this);
                                } catch (IOException e) {
                                    e = e;
                                    Log.e(TAG, "getBitmapFromDiskCache - " + e);
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                        } catch (Exception unused2) {
                                        }
                                    }
                                    return bitmap;
                                }
                            }
                            Bitmap bitmap3 = decodeSampledBitmapFromDescriptor;
                            inputStream2 = inputStream;
                            bitmap2 = bitmap3;
                        } else {
                            bitmap2 = null;
                        }
                        if (inputStream2 != null) {
                            try {
                                inputStream2.close();
                            } catch (Exception unused3) {
                            }
                        }
                        bitmap = bitmap2;
                    } catch (IOException e2) {
                        inputStream = null;
                    } catch (Throwable th) {
                        th = th;
                        if (r2 != 0) {
                            try {
                            } catch (Exception unused4) {
                            }
                        }
                        throw th;
                    }
                }
            } catch (Throwable th2) {
                r2 = hashKeyForDisk;
            }
        }
        return bitmap;
    }

    public BitmapDrawable getBitmapFromMemCache(String str) {
        LruCache<String, BitmapDrawable> lruCache = this.mMemoryCache;
        if (lruCache != null) {
            return lruCache.get(str);
        }
        return null;
    }

    public Bitmap getBitmapFromReusableSet(BitmapFactory.Options options) {
        Set<SoftReference<Bitmap>> set = this.mReusableBitmaps;
        Bitmap bitmap = null;
        if (set != null && !set.isEmpty()) {
            synchronized (this.mReusableBitmaps) {
                Iterator<SoftReference<Bitmap>> it = this.mReusableBitmaps.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Bitmap bitmap2 = it.next().get();
                    if (bitmap2 != null && bitmap2.isMutable()) {
                        if (canUseForInBitmap(bitmap2, options)) {
                            it.remove();
                            bitmap = bitmap2;
                            break;
                        }
                    } else {
                        it.remove();
                    }
                }
            }
        }
        return bitmap;
    }

    public void initDiskCache() {
        synchronized (this.mDiskCacheLock) {
            DiskLruCache diskLruCache = this.mDiskLruCache;
            if (diskLruCache == null || diskLruCache.isClosed()) {
                ImageCacheParams imageCacheParams = this.mCacheParams;
                File file = imageCacheParams.diskCacheDir;
                if (imageCacheParams.diskCacheEnabled && file != null) {
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    long usableSpace = getUsableSpace(file);
                    int i = this.mCacheParams.diskCacheSize;
                    if (usableSpace > i) {
                        try {
                            this.mDiskLruCache = DiskLruCache.open(file, 1, 1, i);
                        } catch (IOException e) {
                            this.mCacheParams.diskCacheDir = null;
                            Log.e(TAG, "initDiskCache - " + e);
                        }
                    }
                }
            }
            this.mDiskCacheStarting = false;
            this.mDiskCacheLock.notifyAll();
        }
    }
}