package com.cleanPhone.mobileCleaner.utility;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.cleanPhone.mobileCleaner.R;


public class TimerView extends View {
    private static final int ARC_START_ANGLE = 45;
    private static final float THICKNESS_SCALE = 0.02f;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private RectF mCircleInnerBounds;
    private RectF mCircleOuterBounds;
    private Paint mCirclePaint;
    private float mCircleSweepAngle;
    private Paint mEraserPaint;
    private ValueAnimator mTimerAnimator;



    public void drawProgress(float f) {
        this.mCircleSweepAngle = f * 360.0f;
        invalidate();
    }

    private void updateBounds() {
        float width = getWidth() * THICKNESS_SCALE;
        this.mCircleOuterBounds = new RectF(0.0f, 0.0f, getWidth(), getHeight());
        RectF rectF = this.mCircleOuterBounds;
        this.mCircleInnerBounds = new RectF(rectF.left + width, rectF.top + width, rectF.right - width, rectF.bottom - width);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        float f = this.mCircleSweepAngle;
        if (f > 0.0f) {
            this.mCanvas.drawArc(this.mCircleOuterBounds, 45.0f, f, true, this.mCirclePaint);
            this.mCanvas.drawOval(this.mCircleInnerBounds, this.mEraserPaint);
        }
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, (Paint) null);
    }

    @Override
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i);
    }

    @Override
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i > 0 && i2 > 0 && (i != i3 || i2 != i4)) {
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            this.mBitmap = createBitmap;
            createBitmap.eraseColor(0);
            this.mCanvas = new Canvas(this.mBitmap);
        }
        updateBounds();
    }

    public void start(int i) {
        stop();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mTimerAnimator = ofFloat;
        ofFloat.setDuration(i);
        this.mTimerAnimator.setInterpolator(new LinearInterpolator());
        this.mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                TimerView.this.drawProgress(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        this.mTimerAnimator.start();
    }

    public void stop() {
        ValueAnimator valueAnimator = this.mTimerAnimator;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return;
        }
        this.mTimerAnimator.cancel();
        this.mTimerAnimator = null;
        drawProgress(0.0f);
    }

    public TimerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @SuppressLint({"RestrictedApi", "ResourceAsColor"})
    public TimerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray obtainStyledAttributes;
        @SuppressLint("RestrictedApi") int i2 = R.color.corner_color;
        Paint paint = new Paint();
        this.mCirclePaint = paint;
        paint.setAntiAlias(true);
        this.mCirclePaint.setColor(Color.WHITE);
        Paint paint2 = new Paint();
        this.mEraserPaint = paint2;
        paint2.setAntiAlias(true);
        this.mEraserPaint.setColor(0);
        this.mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }
}
