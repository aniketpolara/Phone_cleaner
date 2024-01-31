package com.cleanPhone.mobileCleaner.animate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.cleanPhone.mobileCleaner.R;


public class RadarScanView extends View {
    private int centerX;
    private int centerY;
    private int circleColor;
    private int defaultHeight;
    private int defaultWidth;
    private Handler handler;
    private Paint mPaintCircle;
    private Paint mPaintRadar;
    private Matrix matrix;
    private int radarColor;
    private int radarRadius;
    private Runnable run;
    private int start;
    private int tailColor;

    public RadarScanView(Context context) {
        super(context);
        this.circleColor = Color.parseColor("#0d89e6");
        this.radarColor = Color.parseColor("#99a2a2a2");
        this.tailColor = Color.parseColor("#50aaaaaa");
        this.handler = new Handler();
        this.run = new Runnable() {
            @Override
            public void run() {
                RadarScanView.b(RadarScanView.this, 2);
                RadarScanView.this.matrix = new Matrix();
                RadarScanView.this.matrix.postRotate(RadarScanView.this.start, RadarScanView.this.centerX, RadarScanView.this.centerY);
                RadarScanView.this.postInvalidate();
                RadarScanView.this.handler.postDelayed(RadarScanView.this.run, 10L);
            }
        };
        init(null, context);
    }

    public static  int b(RadarScanView radarScanView, int i) {
        int i2 = radarScanView.start + i;
        radarScanView.start = i2;
        return i2;
    }

    private int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    @SuppressLint("ResourceType")
    private void init(AttributeSet attributeSet, Context context) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{R.style.RadarScanView});
            try {
                this.circleColor = obtainStyledAttributes.getColor(0, this.circleColor);
                this.radarColor = obtainStyledAttributes.getColor(1, this.radarColor);
                this.tailColor = obtainStyledAttributes.getColor(2, this.tailColor);
            } catch (Exception e) {
                e.printStackTrace();
            }
            obtainStyledAttributes.recycle();
        }
        initPaint();
        this.defaultWidth = dip2px(context, 300.0f);
        this.defaultHeight = dip2px(context, 300.0f);
        this.matrix = new Matrix();
        this.handler.post(this.run);
    }

    private void initPaint() {
        Paint paint = new Paint();
        this.mPaintCircle = paint;
        paint.setColor(this.circleColor);
        this.mPaintCircle.setAntiAlias(true);
        this.mPaintCircle.setStyle(Paint.Style.STROKE);
        this.mPaintCircle.setStrokeWidth(2.0f);
        Paint paint2 = new Paint();
        this.mPaintRadar = paint2;
        paint2.setColor(this.radarColor);
        this.mPaintRadar.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(this.centerX, this.centerY, this.radarRadius / 7, this.mPaintCircle);
        canvas.drawCircle(this.centerX, this.centerY, this.radarRadius / 4, this.mPaintCircle);
        canvas.drawCircle(this.centerX, this.centerY, this.radarRadius / 3, this.mPaintCircle);
        canvas.drawCircle(this.centerX, this.centerY, (this.radarRadius * 3) / 7, this.mPaintCircle);
        this.mPaintRadar.setShader(new SweepGradient(this.centerX, this.centerY, Color.parseColor("#fceefd"), Color.parseColor("#8acdff")));
        canvas.concat(this.matrix);
        canvas.drawCircle(this.centerX, this.centerY, (this.radarRadius * 3) / 7, this.mPaintRadar);
    }

    @Override
    public void onMeasure(int i, int i2) {
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        if (mode != 1073741824) {
            int i3 = this.defaultWidth;
            size = mode == Integer.MIN_VALUE ? Math.min(i3, size) : i3;
        }
        int mode2 = MeasureSpec.getMode(i2);
        int size2 = MeasureSpec.getSize(i2);
        if (mode2 != 1073741824) {
            int i4 = this.defaultHeight;
            size2 = mode2 == Integer.MIN_VALUE ? Math.min(i4, size2) : i4;
        }
        setMeasuredDimension(size, size2);
    }

    @Override
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.centerX = i / 2;
        this.centerY = i2 / 2;
        this.radarRadius = Math.min(i, i2);
    }

    public RadarScanView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.circleColor = Color.parseColor("#0d89e6");
        this.radarColor = Color.parseColor("#99a2a2a2");
        this.tailColor = Color.parseColor("#50aaaaaa");
        this.handler = new Handler();
        this.run = new Runnable() {
            @Override
            public void run() {
                RadarScanView.b(RadarScanView.this, 2);
                RadarScanView.this.matrix = new Matrix();
                RadarScanView.this.matrix.postRotate(RadarScanView.this.start, RadarScanView.this.centerX, RadarScanView.this.centerY);
                RadarScanView.this.postInvalidate();
                RadarScanView.this.handler.postDelayed(RadarScanView.this.run, 10L);
            }
        };
        init(attributeSet, context);
    }

    public RadarScanView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.circleColor = Color.parseColor("#0d89e6");
        this.radarColor = Color.parseColor("#99a2a2a2");
        this.tailColor = Color.parseColor("#50aaaaaa");
        this.handler = new Handler();
        this.run = new Runnable() {
            @Override
            public void run() {
                RadarScanView.b(RadarScanView.this, 2);
                RadarScanView.this.matrix = new Matrix();
                RadarScanView.this.matrix.postRotate(RadarScanView.this.start, RadarScanView.this.centerX, RadarScanView.this.centerY);
                RadarScanView.this.postInvalidate();
                RadarScanView.this.handler.postDelayed(RadarScanView.this.run, 10L);
            }
        };
        init(attributeSet, context);
    }

}
