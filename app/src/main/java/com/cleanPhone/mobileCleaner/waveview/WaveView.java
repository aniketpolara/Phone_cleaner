package com.cleanPhone.mobileCleaner.waveview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    private float mAmplitudeRatio;
    private int mBehindWaveColor;
    private Paint mBorderPaint;
    private float mDefaultWaterLevel;
    private int mFrontWaveColor;
    private Matrix mShaderMatrix;
    private ShapeType mShapeType;
    private boolean mShowWave;
    private Paint mViewPaint;
    private float mWaterLevelRatio;
    private float mWaveLengthRatio;
    private BitmapShader mWaveShader;
    private float mWaveShiftRatio;
    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#28FFFFFF");
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#3CFFFFFF");
    public static final ShapeType DEFAULT_WAVE_SHAPE = ShapeType.CIRCLE;

    public static  class AnonymousClass1 {
        public static final int[] f5359a;

        static {
            int[] iArr = new int[ShapeType.values().length];
            f5359a = iArr;
            try {
                iArr[ShapeType.CIRCLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5359a[ShapeType.SQUARE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public enum ShapeType {
        CIRCLE,
        SQUARE
    }

    public WaveView(Context context) {
        super(context);
        this.mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
        this.mWaveLengthRatio = 1.0f;
        this.mWaterLevelRatio = 0.5f;
        this.mWaveShiftRatio = 0.0f;
        this.mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR;
        this.mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR;
        this.mShapeType = DEFAULT_WAVE_SHAPE;
        init();
    }

    private void createShader() {
        double width = 6.283185307179586d / getWidth();
        float height = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        this.mDefaultWaterLevel = getHeight() * 0.5f;
        float width2 = getWidth();
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setStrokeWidth(2.0f);
        paint.setAntiAlias(true);
        int width3 = getWidth() + 1;
        int height2 = getHeight() + 1;
        float[] fArr = new float[width3];
        paint.setColor(this.mBehindWaveColor);
        int i = 0;
        while (i < width3) {
            double d2 = width;
            float sin = (float) (this.mDefaultWaterLevel + (height * Math.sin(i * width)));
            float f = i;
            int i2 = i;
            float[] fArr2 = fArr;
            canvas.drawLine(f, sin, f, height2, paint);
            fArr2[i2] = sin;
            i = i2 + 1;
            fArr = fArr2;
            width = d2;
        }
        float[] fArr3 = fArr;
        paint.setColor(this.mFrontWaveColor);
        int i3 = (int) (width2 / 4.0f);
        for (int i4 = 0; i4 < width3; i4++) {
            float f2 = i4;
            canvas.drawLine(f2, fArr3[(i4 + i3) % width3], f2, height2, paint);
        }
        BitmapShader bitmapShader = new BitmapShader(createBitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        this.mWaveShader = bitmapShader;
        this.mViewPaint.setShader(bitmapShader);
    }

    private void init() {
        this.mShaderMatrix = new Matrix();
        Paint paint = new Paint();
        this.mViewPaint = paint;
        paint.setAntiAlias(true);
    }


    @Override
    public void onDraw(Canvas canvas) {
        if (this.mShowWave && this.mWaveShader != null) {
            if (this.mViewPaint.getShader() == null) {
                this.mViewPaint.setShader(this.mWaveShader);
            }
            this.mShaderMatrix.setScale(this.mWaveLengthRatio / 1.0f, this.mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO, 0.0f, this.mDefaultWaterLevel);
            this.mShaderMatrix.postTranslate(this.mWaveShiftRatio * getWidth(), (0.5f - this.mWaterLevelRatio) * getHeight());
            this.mWaveShader.setLocalMatrix(this.mShaderMatrix);
            Paint paint = this.mBorderPaint;
            float strokeWidth = paint == null ? 0.0f : paint.getStrokeWidth();
            int i = AnonymousClass1.f5359a[this.mShapeType.ordinal()];
            if (i == 1) {
                if (strokeWidth > 0.0f) {
                    canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, ((getWidth() - strokeWidth) / 2.0f) - 1.0f, this.mBorderPaint);
                }
                canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, (getWidth() / 2.0f) - strokeWidth, this.mViewPaint);
                return;
            } else if (i != 2) {
                return;
            } else {
                if (strokeWidth > 0.0f) {
                    float f = strokeWidth / 2.0f;
                    canvas.drawRect(f, f, (getWidth() - f) - 0.5f, (getHeight() - f) - 0.5f, this.mBorderPaint);
                }
                canvas.drawRect(strokeWidth, strokeWidth, getWidth() - strokeWidth, getHeight() - strokeWidth, this.mViewPaint);
                return;
            }
        }
        this.mViewPaint.setShader(null);
    }

    @Override
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        createShader();
    }

    public void setAmplitudeRatio(float f) {
        if (this.mAmplitudeRatio != f) {
            this.mAmplitudeRatio = f;
            invalidate();
        }
    }

    public void setBorder(int i, int i2) {
        if (this.mBorderPaint == null) {
            Paint paint = new Paint();
            this.mBorderPaint = paint;
            paint.setAntiAlias(true);
            this.mBorderPaint.setStyle(Paint.Style.STROKE);
        }
        this.mBorderPaint.setColor(i2);
        this.mBorderPaint.setStrokeWidth(i);
        invalidate();
    }

    public void setShapeType(ShapeType shapeType) {
        this.mShapeType = shapeType;
        invalidate();
    }

    public void setShowWave(boolean z) {
        this.mShowWave = z;
    }

    public void setWaterLevelRatio(float f) {
        if (this.mWaterLevelRatio != f) {
            this.mWaterLevelRatio = f;
            invalidate();
        }
    }

    public void setWaveColor(int i, int i2) {
        this.mBehindWaveColor = i;
        this.mFrontWaveColor = i2;
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }
        this.mWaveShader = null;
        createShader();
        invalidate();
    }

    public void setWaveLengthRatio(float f) {
        this.mWaveLengthRatio = f;
    }

    public void setWaveShiftRatio(float f) {
        if (this.mWaveShiftRatio != f) {
            this.mWaveShiftRatio = f;
            invalidate();
        }
    }

    public WaveView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
        this.mWaveLengthRatio = 1.0f;
        this.mWaterLevelRatio = 0.5f;
        this.mWaveShiftRatio = 0.0f;
        this.mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR;
        this.mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR;
        this.mShapeType = DEFAULT_WAVE_SHAPE;
        init();
    }

    public WaveView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
        this.mWaveLengthRatio = 1.0f;
        this.mWaterLevelRatio = 0.5f;
        this.mWaveShiftRatio = 0.0f;
        this.mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR;
        this.mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR;
        this.mShapeType = DEFAULT_WAVE_SHAPE;
        init();
    }
}
