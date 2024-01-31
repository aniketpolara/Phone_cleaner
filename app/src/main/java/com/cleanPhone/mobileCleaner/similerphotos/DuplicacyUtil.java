package com.cleanPhone.mobileCleaner.similerphotos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.util.Log;

import com.facebook.appevents.AppEventsConstants;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.ImageUtil;

import java.io.File;


public class DuplicacyUtil {
    public static float calculateHamingScore(ImageDetail imageDetail, ImageDetail imageDetail2, int i, int i2) {
        int i3 = i2 * 1000;
        if (!imageDetail2.skipImage && GlobalData.shouldContinue) {
            if (GlobalData.duplicacyTime <= 0 || checkTimeDifference(imageDetail.createDateInMSecs.longValue(), imageDetail2.createDateInMSecs.longValue()) <= i3) {
                if (GlobalData.duplicacyDist > 0 && !imageDetail.lat.equalsIgnoreCase("null") && !imageDetail2.lat.equalsIgnoreCase("null") && distance(imageDetail, imageDetail2) > i) {
                    imageDetail2.match = "0.7";
                    return 0.7f;
                }
                if (imageDetail.hash == null) {
                    imageDetail.hash = calculateImageHash(imageDetail.path, imageDetail.exif);
                }
                if (imageDetail.hash == null) {
                    return 0.7f;
                }
                if (imageDetail2.hash == null) {
                    imageDetail2.hash = calculateImageHash(imageDetail2.path, imageDetail2.exif);
                }
                if (imageDetail2.hash == null) {
                    return 0.7f;
                }
                float f = 0.0f;
                int length = (int) (imageDetail.hash.length() * 0.100000024f);
                for (int i4 = 0; i4 < imageDetail.hash.length(); i4++) {
                    try {
                        if (imageDetail.hash.charAt(i4) != imageDetail2.hash.charAt(i4)) {
                            f += 1.0f;
                            if (f > length) {
                                return 0.7f;
                            }
                        }
                    } catch (Exception unused) {
                        return 0.7f;
                    }
                }
                return (imageDetail.hash.length() - f) / imageDetail.hash.length();
            }
            return 0.7f;
        }
        return 0.7f;
    }

    public static String calculateImageHash(String str, ExifInterface exifInterface) {
        Bitmap imageNode = getImageNode(str, exifInterface);
        if (imageNode != null) {
            Bitmap fastblur = ImageOperations.fastblur(imageNode, 1.0f, 7);
            if (GlobalData.shouldContinue) {
                Bitmap createGrayscale = ImageOperations.createGrayscale(fastblur);
                int i = 6;
                int width = createGrayscale.getWidth();
                int height = createGrayscale.getHeight();
                StringBuffer stringBuffer = new StringBuffer();
                int i2 = height / 6;
                int i3 = width / 6;
                int[] iArr = new int[i2 * i3];
                int i4 = 0;
                int i5 = 0;
                while (true) {
                    String str2 = ",";
                    if (i5 >= i) {
                        break;
                    }
                    int i6 = i4;
                    int i7 = 0;
                    while (i7 < i) {
                        int i8 = ((i5 + 1) * i2) - 1;
                        int i9 = i3 * i7;
                        int i10 = i7 + 1;
                        int i11 = (i3 * i10) - 1;
                        int[] iArr2 = new int[width * height];
                        String str3 = str2;
                        int i12 = i5;
                        int[] iArr3 = iArr;
                        createGrayscale.getPixels(iArr2, 0, width, 0, 0, width, height);
                        int i13 = 0;
                        int i14 = 0;
                        for (int i15 = i2 * i5; i15 <= i8; i15++) {
                            for (int i16 = i9; i16 <= i11; i16++) {
                                i14++;
                                int i17 = (i15 * width) + i16;
                                int red = ((Color.red(iArr2[i17]) + Color.green(iArr2[i17])) + Color.blue(iArr2[i17])) / 3;
                                i13 += red;
                                stringBuffer.append(red + str3);
                            }
                        }
                        str2 = str3;
                        iArr3[i6] = i13 / i14;
                        i6++;
                        i7 = i10;
                        i5 = i12;
                        iArr = iArr3;
                        i = 6;
                    }
                    i5++;
                    i4 = i6;
                    i = 6;
                }
                int[] iArr4 = iArr;
                if (GlobalData.shouldContinue) {
                    String[] split = stringBuffer.toString().split(",");
                    StringBuffer stringBuffer2 = new StringBuffer();
                    int i18 = 0;
                    int i19 = 0;
                    for (int i20 = 0; i20 < 6; i20++) {
                        int i21 = 0;
                        while (i21 < 6) {
                            int i22 = ((i20 + 1) * i2) - 1;
                            int i23 = i3 * i21;
                            i21++;
                            int i24 = (i3 * i21) - 1;
                            int i25 = iArr4[i18];
                            for (int i26 = i2 * i20; i26 <= i22; i26++) {
                                int i27 = i23;
                                while (i27 <= i24) {
                                    int i28 = i19 + 1;
                                    if (Integer.valueOf(split[i19]).intValue() > i25) {
                                        stringBuffer2.append("1");
                                    } else {
                                        stringBuffer2.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                                    }
                                    i27++;
                                    i19 = i28;
                                }
                            }
                            i18++;
                        }
                    }
                    return stringBuffer2.toString();
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public static long checkTimeDifference(long j, long j2) {
        return Math.abs(j - j2);
    }

    private static double distance(ImageDetail imageDetail, ImageDetail imageDetail2) {
        Log.i("DOUBLE", imageDetail.lat + "   " + imageDetail2.lon);
        double d2 = -1.0d;
        if (imageDetail.lon == null || imageDetail2.lon == null) {
            return -1.0d;
        }
        try {
            double radians = Math.toRadians(Double.parseDouble(imageDetail2.lat) - Double.parseDouble(imageDetail.lat)) / 2.0d;
            double radians2 = Math.toRadians(Double.parseDouble(imageDetail2.lon) - Double.parseDouble(imageDetail.lon)) / 2.0d;
            double sin = (Math.sin(radians) * Math.sin(radians)) + (Math.cos(Math.toRadians(Double.parseDouble(imageDetail.lat))) * Math.cos(Math.toRadians(Double.parseDouble(imageDetail2.lat))) * Math.sin(radians2) * Math.sin(radians2));
            d2 = (float) (Math.atan2(Math.sqrt(sin), Math.sqrt(1.0d - sin)) * 2.0d * 6371000.0d);
            imageDetail2.distdiffer = "" + d2;
            return d2;
        } catch (Exception e) {
            e.printStackTrace();
            return d2;
        }
    }

    private static Bitmap getImageNode(String str, ExifInterface exifInterface) {
        try {
            byte[] thumbnail = exifInterface.getThumbnail();
            if (thumbnail != null) {
                return BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
            }
            return ImageUtil.decodeFile(new File(str));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
