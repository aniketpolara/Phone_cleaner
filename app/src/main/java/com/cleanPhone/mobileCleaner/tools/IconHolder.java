package com.cleanPhone.mobileCleaner.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.Utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class IconHolder {
    public int f5188a;
    private final Context mContext;
    private final boolean mUseThumbs;
    private Handler mWorkerHandler;
    private HandlerThread mWorkerThread;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        private synchronized void processResult(LoadResult loadResult) {
            try {
                IconHolder.this.mAppIcons.put(loadResult.f5192a, loadResult.b);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Map.Entry entry : IconHolder.this.mRequests.entrySet()) {
                ImageView imageView = (ImageView) entry.getKey();
                String str = (String) entry.getValue();
                if (Build.VERSION.SDK_INT >= 19) {
                    if (Objects.equals(str, loadResult.f5192a)) {
                        imageView.setImageBitmap(loadResult.b);
                        IconHolder.this.mRequests.remove(imageView);
                        break;
                    }
                } else if (str.equals(loadResult.f5192a)) {
                    imageView.setImageBitmap(loadResult.b);
                    IconHolder.this.mRequests.remove(imageView);
                    break;
                }
            }
        }

        @Override
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 2) {
                processResult((LoadResult) message.obj);
                sendEmptyMessageDelayed(3, 3000L);
            } else if (i != 3) {
            } else {
                IconHolder.this.shutdownWorker();
            }
        }
    };
    private boolean isImage = false;
    private boolean isVideo = false;
    private Map<ImageView, String> mRequests = new HashMap();
    private final Map<String, Bitmap> mIcons = new HashMap();
    private final Map<String, Bitmap> mAppIcons = new LinkedHashMap<String, Bitmap>(500, 0.75f, true) { // from class: com.mobiclean.phoneclean.tools.IconHolder.2
        private static final long serialVersionUID = 1;

        @Override
        public boolean removeEldestEntry(Entry<String, Bitmap> entry) {
            return size() > 500;
        }
    };

    public static class LoadResult {
        public String f5192a;
        public Bitmap b;

        private LoadResult() {
        }
    }

    public IconHolder(Context context, boolean z, boolean z2) {
        this.mContext = context;
        this.mUseThumbs = z;
        this.f5188a = ((z2 ? 150 : 50) * (context.getResources().getDisplayMetrics().densityDpi / 160)) / 4;
    }


    private Bitmap getVideoDrawable(String str) throws OutOfMemoryError {
        try {
            return ThumbnailUtils.createVideoThumbnail(str, 3);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap loadImage(String str) throws OutOfMemoryError {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            int i = this.f5188a;
            options.inSampleSize = Utils.calculateInSampleSize(options, i, i);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(str, options);
        } catch (Exception unused) {
            return ((BitmapDrawable) ContextCompat.getDrawable(this.mContext, R.drawable.gallery_image)).getBitmap();
        }
    }

    public void shutdownWorker() {
        HandlerThread handlerThread = this.mWorkerThread;
        if (handlerThread != null) {
            handlerThread.getLooper().quit();
            this.mWorkerHandler = null;
            this.mWorkerThread = null;
        }
    }

    public void cleanup() {
        this.mRequests.clear();
        this.mIcons.clear();
        this.mAppIcons.clear();
        shutdownWorker();
    }


    public void setImage(boolean z) {
        this.isImage = z;
    }

    public void setVideo(boolean z) {
        this.isVideo = z;
    }

    public Bitmap loadDrawable(String str) {
        try {
            if (this.isImage) {
                return loadImage(str);
            }
            if (this.isVideo) {
                return getVideoDrawable(str);
            }
            return null;
        } catch (OutOfMemoryError unused) {
            cleanup();
            shutdownWorker();
            return null;
        }
    }
}
