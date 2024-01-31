package com.cleanPhone.mobileCleaner.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class RevealView extends ImageView {
    public int f4882a;
    public int b;
    private float mAnimationPercentage;
    private Paint mPaint;
    private Path mPath;
    private Bitmap secondBitmap;

    public RevealView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setPercentage(0);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        this.mPath = new Path();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(((BitmapDrawable) getDrawable()).getBitmap(), this.f4882a, this.b, false);
        if (createScaledBitmap == null || this.secondBitmap == null) {
            return;
        }
        canvas.drawBitmap(createScaledBitmap, 0.0f, 0.0f, (Paint) null);
        this.mPath.reset();
        this.mPath.moveTo(0.0f, this.b * this.mAnimationPercentage);
        this.mPath.lineTo(this.f4882a, this.b * this.mAnimationPercentage);
        this.mPath.lineTo(this.f4882a, this.b);
        this.mPath.lineTo(0.0f, this.b);
        this.mPath.close();
        canvas.drawPath(this.mPath, this.mPaint);
        canvas.clipPath(this.mPath);
        canvas.drawBitmap(this.secondBitmap, 0.0f, 0.0f, (Paint) null);
    }

    public void setPercentage(int i) {
        if (i > 100) {
            i = 100;
        }
        this.mAnimationPercentage = i / 100.0f;
        invalidate();
    }

    public void setSecondBitmap(Bitmap bitmap, int i, int i2) {
        this.secondBitmap = bitmap;
        this.f4882a = i;
        this.b = i2;
    }

    public RevealView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public RevealView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }
}
