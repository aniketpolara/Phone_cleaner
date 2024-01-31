package com.cleanPhone.mobileCleaner.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.cleanPhone.mobileCleaner.R;

import java.io.File;
import java.text.SimpleDateFormat;

public class Utils {
    private static final SimpleDateFormat DATE_NO_MINUTES = new SimpleDateFormat("MMM dd, yyyy");
    private static final SimpleDateFormat DATE_WITH_MINUTES = new SimpleDateFormat("MMM dd yyyy | KK:mm a");

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
        }
        return i5;
    }

    public static float clamp(float f, float f2, float f3) {
        return Math.min(Math.max(f, f3), f2);
    }

    public static int dpToPx(int i, Context context) {
        return Math.round(i * (context.getResources().getDisplayMetrics().xdpi / 160.0f));
    }

    public static int getColor(Context context, @ColorRes int i) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getColor(i);
        }
        return context.getResources().getColor(i);
    }

    public static float getViewRawY(View view) {
        int[] iArr = {0, (int) view.getY()};
        ((View) view.getParent()).getLocationInWindow(iArr);
        return iArr[1];
    }

    public static void setTint(Context context, CheckBox checkBox, int i) {
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 21) {
            return;
        }
        @SuppressLint("ResourceType") ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{-16842912}, new int[]{16842912}}, new int[]{getColor(context, 17170432), i});
        if (i2 >= 21) {
            checkBox.setButtonTintList(colorStateList);
            return;
        }
        Drawable wrap = DrawableCompat.wrap(ContextCompat.getDrawable(checkBox.getContext(), R.drawable.abc_btn_check_material));
        DrawableCompat.setTintList(wrap, colorStateList);
        checkBox.setButtonDrawable(wrap);
    }

    public String getDate(File file) {
        return getDate(file.lastModified());
    }

    public boolean isAtleastKitkat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static String getDate(long j) {
        return DATE_WITH_MINUTES.format(Long.valueOf(j));
    }

    public static String getDate(long j, String str) {
        String format = DATE_NO_MINUTES.format(Long.valueOf(j));
        return format.substring(format.length() + (-4), format.length()).equals(str) ? format.substring(0, format.length() - 6) : format;
    }
}
