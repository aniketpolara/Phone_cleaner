package com.cleanPhone.mobileCleaner.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {
    private static final String LOG = "IMAGE_UTIL_EXCEPTION";
    private static final String appProfilePath = "/jewellery_app/profile/";

    public static Bitmap decodeFile(File file) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            int i = 1;
            options.inJustDecodeBounds = true;
            options.inPreferQualityOverSpeed = false;
            options.inScaled = false;
            BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            while ((options.outWidth / i) / 2 >= 256 && (options.outHeight / i) / 2 >= 256) {
                i *= 2;
            }
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize = i;
            return BitmapFactory.decodeStream(new FileInputStream(file), null, options2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Bitmap getBitmap(Bitmap bitmap, int i, int i2) {
        try {
            return Bitmap.createScaledBitmap(bitmap, i, i2, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        InputStream openInputStream = context.getContentResolver().openInputStream(uri);
        if (openInputStream == null) {
            Log.d(LOG, "nullllllllllllll>>>>>>>>>>>>>>>>222");
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream, null, options);
        if (decodeStream == null) {
            Log.d(LOG, "nullllllllllllll>>>>>>>>>>>>>>>>");
        }
        openInputStream.close();
        return decodeStream;
    }

    private static Bitmap getscalledBitmp(Bitmap bitmap, int i, int i2) {
        Bitmap bitmap2 = null;
        while (true) {
            for (boolean z = true; z; z = false) {
                try {
                    bitmap2 = Bitmap.createScaledBitmap(bitmap, i, i2, true);
                } catch (OutOfMemoryError e) {
                    i = (i * 90) / 100;
                    i2 = (i2 * 90) / 100;
                    e.printStackTrace();
                }
            }
            return bitmap2;
        }
    }


    private static void setDeviceDimensions(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        GlobalData.deviceWidth = displayMetrics.widthPixels;
        GlobalData.deviceHeight = displayMetrics.heightPixels;
    }

    public static void setScalledBackground(View view, Context context, int i, int i2, int i3, int i4) {
        Bitmap createScaledBitmap;
        try {
            if (GlobalData.deviceWidth == 0 || GlobalData.deviceHeight == 0) {
                setDeviceDimensions(context);
            }
            int i5 = (int) ((GlobalData.deviceWidth * i2) / 100.0f);
            int i6 = i3 == 0 ? i5 : (int) ((GlobalData.deviceHeight * i3) / 100.0f);
            Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), i);
            if (i4 == 1) {
                createScaledBitmap = getscalledBitmp(decodeResource, i5, i6);
            } else if (i4 == 2) {
                createScaledBitmap = ScalingUtilities.createScaledBitmap(decodeResource, i5, i6, ScalingUtilities.ScalingLogic.FIT);
            } else {
                createScaledBitmap = ScalingUtilities.createScaledBitmap(decodeResource, i5, i6, ScalingUtilities.ScalingLogic.CROP);
            }
            if (view instanceof ImageButton) {
                ((ImageButton) view).setImageBitmap(createScaledBitmap);
            } else if (view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(createScaledBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void setScalledBackgroundOfSDCardImage(View view, Context context, String str, int i, int i2, int i3) {
        Bitmap createScaledBitmap;
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
            int i4 = (int) ((displayMetrics.widthPixels * i) / 100.0f);
            int i5 = (int) ((displayMetrics.heightPixels * i2) / 100.0f);
            Bitmap decodeFile = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + appProfilePath + str);
            if (i3 == 1) {
                createScaledBitmap = getscalledBitmp(decodeFile, i4, i5);
            } else if (i3 == 2) {
                createScaledBitmap = ScalingUtilities.createScaledBitmap(decodeFile, i4, i5, ScalingUtilities.ScalingLogic.FIT);
            } else {
                createScaledBitmap = ScalingUtilities.createScaledBitmap(decodeFile, i4, i5, ScalingUtilities.ScalingLogic.CROP);
            }
            if (view instanceof ImageButton) {
                ((ImageButton) view).setImageBitmap(createScaledBitmap);
            } else if (view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(createScaledBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void setScalledBackgroundPixels(View view, Context context, Bitmap bitmap, int i, int i2, int i3) {
        Bitmap createScaledBitmap;
        try {
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(new DisplayMetrics());
            if (i3 == 1) {
                createScaledBitmap = getscalledBitmp(bitmap, i, i2);
            } else if (i3 == 2) {
                createScaledBitmap = ScalingUtilities.createScaledBitmap(bitmap, i, i2, ScalingUtilities.ScalingLogic.FIT);
            } else {
                createScaledBitmap = ScalingUtilities.createScaledBitmap(bitmap, i, i2, ScalingUtilities.ScalingLogic.CROP);
            }
            if (view instanceof ImageButton) {
                ((ImageButton) view).setImageBitmap(createScaledBitmap);
            } else if (view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(createScaledBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static boolean storeImage(Bitmap bitmap, String str) {
        File file = new File(Environment.getExternalStorageDirectory() + appProfilePath);
        file.mkdirs();
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file + DialogConfigs.DIRECTORY_SEPERATOR + str));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving imaged file: " + e.getMessage());
            return false;
        } catch (IOException e2) {
            Log.w("TAG", "Error saving imaged file: " + e2.getMessage());
            return false;
        }
    }

    public static void setScalledBackgroundPixels(View view, Context context, int i, int i2, int i3, int i4) {
        Bitmap createScaledBitmap;
        try {
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(new DisplayMetrics());
            Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), i);
            if (i4 == 1) {
                createScaledBitmap = getscalledBitmp(decodeResource, i2, i3);
            } else if (i4 == 2) {
                createScaledBitmap = ScalingUtilities.createScaledBitmap(decodeResource, i2, i3, ScalingUtilities.ScalingLogic.FIT);
            } else {
                createScaledBitmap = ScalingUtilities.createScaledBitmap(decodeResource, i2, i3, ScalingUtilities.ScalingLogic.CROP);
            }
            if (view instanceof ImageButton) {
                ((ImageButton) view).setImageBitmap(createScaledBitmap);
            } else if (view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(createScaledBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void setScalledBackground(View view, Context context, Bitmap bitmap, int i, int i2, int i3) {
        Bitmap createScaledBitmap;
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
            int i4 = (int) ((displayMetrics.widthPixels * i) / 100.0f);
            int i5 = (int) ((displayMetrics.heightPixels * i2) / 100.0f);
            if (i3 == 1) {
                createScaledBitmap = getscalledBitmp(bitmap, i4, i5);
            } else if (i3 == 2) {
                createScaledBitmap = ScalingUtilities.createScaledBitmap(bitmap, i4, i5, ScalingUtilities.ScalingLogic.FIT);
            } else {
                createScaledBitmap = ScalingUtilities.createScaledBitmap(bitmap, i4, i5, ScalingUtilities.ScalingLogic.CROP);
            }
            if (view instanceof ImageButton) {
                ((ImageButton) view).setImageBitmap(createScaledBitmap);
            } else if (view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(createScaledBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
