package com.cleanPhone.mobileCleaner.animate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import com.facebook.appevents.AppEventsConstants;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.text.DecimalFormat;

public class CircleProgressView extends View {
    private static final boolean DEBUG = false;
    private static final String TAG = "CircleView";
    public Direction f4769a;
    public float b;
    public float f4770c;
    public float f4771d;
    private DecimalFormat decimalFormat;
    public float e;
    public float f;
    public float g;
    public float h;
    public float i;
    public float j;
    public float k;
    public boolean l;
    public double m;
    public RectF mActualTextBounds;
    private int mBackgroundCircleColor;
    private Paint mBackgroundCirclePaint;
    private final int mBarColorStandard;
    private int[] mBarColors;
    private Paint mBarPaint;
    private Paint mBarSpinnerPaint;
    private int mBarStartEndLineColor;
    private Paint mBarStartEndLinePaint;
    private float mBarStartEndLineSweep;
    private int mBarStartEndLineWidth;
    private BarStartFinishLiner mBarStartFinishLiner;
    private Paint.Cap mBarStrokeCap;
    private int mBarWidth;
    private int mBlockCount;
    private float mBlockDegree;
    private float mBlockScale;
    private float mBlockScaleDegree;
    public PointF mCenter;
    public RectF mCircleBounds;
    public RectF mCircleInnerContour;
    public RectF mCircleOuterContour;
    private Bitmap mClippingBitmap;
    public RectF mInnerCircleBound;
    private int mInnerContourColor;
    private Paint mInnerContourPaint;
    private float mInnerContourSize;
    private boolean mIsAutoColorEnabled;
    private boolean mIsAutoTextSize;
    public int mLayoutHeight;
    public int mLayoutWidth;
    private Paint mMaskPaint;
    private int mOuterContourColor;
    private Paint mOuterContourPaint;
    private float mOuterContourSize;
    public RectF mOuterTextBounds;
    private float mRelativeUniteSize;
    private int mRimColor;
    private Paint mRimPaint;
    private int mRimWidth;
    private boolean mRoundToBlock;
    private boolean mRoundToWholeNumber;
    private boolean mSeekModeEnabled;
    private Paint mShaderlessBarPaint;
    private boolean mShowBlock;
    private boolean mShowTextWhileSpinning;
    private boolean mShowUnit;
    private int mSpinnerColor;
    private Paint.Cap mSpinnerStrokeCap;
    private int mStartAngle;
    private String mText;
    private int mTextColor;
    private int mTextLength;
    private TextMode mTextMode;
    private Paint mTextPaint;
    private float mTextScale;
    private int mTextSize;
    private int mTouchEventCount;
    private String mUnit;
    public RectF mUnitBounds;
    private int mUnitColor;
    private UnitPosition mUnitPosition;
    private float mUnitScale;
    private Paint mUnitTextPaint;
    private int mUnitTextSize;
    public int n;
    public boolean o;
    private OnProgressChangedListener onProgressChangedListener;
    public AnimationHandling p;
    private float previousProgressChangedValue;
    public AnimationState q;
    public AnimationStateChangedListener r;
    private Typeface textTypeface;
    private Typeface unitTextTypeface;

    public static class AnonymousClass1 {
        public static final  int[] f4772a;
        public static final  int[] b;

        static {
            int[] iArr = new int[TextMode.values().length];
            b = iArr;
            try {
                iArr[TextMode.TEXT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                b[TextMode.PERCENT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                b[TextMode.VALUE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[UnitPosition.values().length];
            f4772a = iArr2;
            try {
                iArr2[UnitPosition.TOP.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f4772a[UnitPosition.BOTTOM.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f4772a[UnitPosition.LEFT_TOP.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f4772a[UnitPosition.RIGHT_TOP.ordinal()] = 4;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f4772a[UnitPosition.LEFT_BOTTOM.ordinal()] = 5;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f4772a[UnitPosition.RIGHT_BOTTOM.ordinal()] = 6;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }


    public interface OnProgressChangedListener {
        void onProgressChanged(float f);
    }

    public CircleProgressView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBarColorStandard = 16711680;
        this.mLayoutHeight = 0;
        this.mLayoutWidth = 0;
        this.mCircleBounds = new RectF();
        this.mInnerCircleBound = new RectF();
        this.mOuterTextBounds = new RectF();
        this.mActualTextBounds = new RectF();
        this.mUnitBounds = new RectF();
        this.mCircleOuterContour = new RectF();
        this.mCircleInnerContour = new RectF();
        this.f4769a = Direction.CW;
        this.b = 0.0f;
        this.f4770c = 0.0f;
        this.f4771d = 0.0f;
        this.e = 100.0f;
        this.f = 0.0f;
        this.g = -1.0f;
        this.h = 0.0f;
        this.i = 42.0f;
        this.j = 0.0f;
        this.k = 7.2f;
        this.l = false;
        this.m = 200.0d;
        this.n = 3;
        this.p = new AnimationHandling(this);
        this.q = AnimationState.IDLE;
        this.mBarWidth = 40;
        this.mRimWidth = 40;
        this.mStartAngle = 270;
        this.mOuterContourSize = 1.0f;
        this.mInnerContourSize = 1.0f;
        this.mBarStartEndLineWidth = 0;
        this.mBarStartFinishLiner = BarStartFinishLiner.NONE;
        this.mBarStartEndLineColor = 16711680;
        this.mBarStartEndLineSweep = 10.0f;
        this.mUnitTextSize = 10;
        this.mTextSize = 10;
        this.mTextScale = 1.0f;
        this.mUnitScale = 1.0f;
        this.mOuterContourColor = 16711680;
        this.mInnerContourColor = 16711680;
        this.mSpinnerColor = 16711680;
        this.mBackgroundCircleColor = 16711680;
        this.mRimColor = 16711680;
        this.mTextColor = 16711680;
        this.mUnitColor = 16711680;
        this.mIsAutoColorEnabled = false;
        this.mBarColors = new int[]{16711680};
        Paint.Cap cap = Paint.Cap.BUTT;
        this.mBarStrokeCap = cap;
        this.mSpinnerStrokeCap = cap;
        this.mBarPaint = new Paint();
        this.mBarSpinnerPaint = new Paint();
        this.mBarStartEndLinePaint = new Paint();
        this.mBackgroundCirclePaint = new Paint();
        this.mRimPaint = new Paint();
        this.mTextPaint = new Paint();
        this.mUnitTextPaint = new Paint();
        this.mOuterContourPaint = new Paint();
        this.mInnerContourPaint = new Paint();
        this.mText = "";
        this.mUnit = "";
        this.mUnitPosition = UnitPosition.RIGHT_TOP;
        this.mTextMode = TextMode.PERCENT;
        this.mShowUnit = false;
        this.mRelativeUniteSize = 1.0f;
        this.mSeekModeEnabled = false;
        this.mShowTextWhileSpinning = false;
        this.mShowBlock = false;
        this.mBlockCount = 18;
        this.mBlockScale = 0.9f;
        float f = 360 / 18;
        this.mBlockDegree = f;
        this.mBlockScaleDegree = f * 0.9f;
        this.mRoundToBlock = false;
        this.mRoundToWholeNumber = false;
        this.decimalFormat = new DecimalFormat(AppEventsConstants.EVENT_PARAM_VALUE_NO);
//        parseAttributes(context.obtainStyledAttributes(attributeSet, new int[]{R.style.CircleProgressView}));
        if (!isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        Paint paint = new Paint(1);
        this.mMaskPaint = paint;
        paint.setFilterBitmap(false);
        this.mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        setupPaints();
        if (this.l) {
            spin();
        }
    }

    public static double calcRotationAngleInDegrees(PointF pointF, PointF pointF2) {
        double degrees = Math.toDegrees(Math.atan2(pointF2.y - pointF.y, pointF2.x - pointF.x));
        return degrees < FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE ? degrees + 360.0d : degrees;
    }

    private RectF calcTextBounds(String str, Paint paint, RectF rectF) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        float width = rect.left + rect.width();
        float height = rect.bottom + (rect.height() * 0.93f);
        RectF rectF2 = new RectF();
        rectF2.left = rectF.left + ((rectF.width() - width) / 2.0f);
        float height2 = rectF.top + ((rectF.height() - height) / 2.0f);
        rectF2.top = height2;
        rectF2.right = rectF2.left + width;
        rectF2.bottom = height2 + height;
        return rectF2;
    }


    private static float calcTextSizeForRect(String str, Paint paint, RectF rectF) {
        Matrix matrix = new Matrix();
        Rect rect = new Rect();
        String replace = str.replace('1', '0');
        paint.getTextBounds(replace, 0, replace.length(), rect);
        matrix.setRectToRect(new RectF(rect), rectF, Matrix.ScaleToFit.CENTER);
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        return paint.getTextSize() * fArr[0];
    }

    private void drawBar(Canvas canvas, float f) {
        float f2 = this.f4769a == Direction.CW ? this.mStartAngle : this.mStartAngle - f;
        if (!this.mShowBlock) {
            if (this.mBarStrokeCap == Paint.Cap.BUTT || f <= 0.0f || this.mBarColors.length <= 1) {
                canvas.drawArc(this.mCircleBounds, f2, f, false, this.mBarPaint);
                return;
            } else if (f > 180.0f) {
                float f3 = f / 2.0f;
                float f4 = f2;
                canvas.drawArc(this.mCircleBounds, f4, f3, false, this.mBarPaint);
                canvas.drawArc(this.mCircleBounds, f4, 1.0f, false, this.mShaderlessBarPaint);
                canvas.drawArc(this.mCircleBounds, f2 + f3, f3, false, this.mBarPaint);
                return;
            } else {
                float f5 = f2;
                canvas.drawArc(this.mCircleBounds, f5, f, false, this.mBarPaint);
                canvas.drawArc(this.mCircleBounds, f5, 1.0f, false, this.mShaderlessBarPaint);
                return;
            }
        }
        drawBlocks(canvas, this.mCircleBounds, f2, f, false, this.mBarPaint);
    }

    private void drawBlocks(Canvas canvas, RectF rectF, float f, float f2, boolean z, Paint paint) {
        float f3 = 0.0f;
        while (f3 < f2) {
            canvas.drawArc(rectF, f + f3, Math.min(this.mBlockScaleDegree, f2 - f3), z, paint);
            f3 += this.mBlockDegree;
        }
    }


    private void drawSpinner(Canvas canvas) {
        float f;
        float f2;
        if (this.h < 0.0f) {
            this.h = 1.0f;
        }
        if (this.f4769a == Direction.CW) {
            f = this.mStartAngle + this.j;
            f2 = this.h;
        } else {
            f = this.mStartAngle;
            f2 = this.j;
        }
        canvas.drawArc(this.mCircleBounds, f - f2, this.h, false, this.mBarSpinnerPaint);
    }

    private void drawStartEndLine(Canvas canvas, float f) {
        if (f == 0.0f) {
            return;
        }
        float f2 = this.f4769a == Direction.CW ? this.mStartAngle : this.mStartAngle - f;
        float f3 = this.mBarStartEndLineSweep;
        float f4 = f2 - (f3 / 2.0f);
        BarStartFinishLiner barStartFinishLiner = this.mBarStartFinishLiner;
        if (barStartFinishLiner == BarStartFinishLiner.START || barStartFinishLiner == BarStartFinishLiner.BOTH) {
            canvas.drawArc(this.mCircleBounds, f4, f3, false, this.mBarStartEndLinePaint);
        }
        BarStartFinishLiner barStartFinishLiner2 = this.mBarStartFinishLiner;
        if (barStartFinishLiner2 == BarStartFinishLiner.END || barStartFinishLiner2 == BarStartFinishLiner.BOTH) {
            canvas.drawArc(this.mCircleBounds, f4 + f, this.mBarStartEndLineSweep, false, this.mBarStartEndLinePaint);
        }
    }

    private void drawTextWithUnit(Canvas canvas) {
        float f;
        float f2;
        float f3;
        String format;
        int i = AnonymousClass1.f4772a[this.mUnitPosition.ordinal()];
        boolean z = true;
        if (i != 1 && i != 2) {
            f = this.mRelativeUniteSize;
            f2 = 0.55f * f;
            f3 = 0.3f;
        } else {
            f = this.mRelativeUniteSize;
            f2 = 0.25f * f;
            f3 = 0.4f;
        }
        float width = (this.mOuterTextBounds.width() * 0.05f) / 2.0f;
        float width2 = f * f3 * this.mOuterTextBounds.width();
        float height = (this.mOuterTextBounds.height() * 0.025f) / 2.0f;
        float height2 = f2 * this.mOuterTextBounds.height();
        if (this.mIsAutoColorEnabled) {
            this.mTextPaint.setColor(calcTextColor(this.b));
        }
        int i2 = AnonymousClass1.b[this.mTextMode.ordinal()];
        if (i2 == 2) {
            format = this.decimalFormat.format((100.0f / this.e) * this.b);
        } else if (i2 != 3) {
            format = this.mText;
            if (format == null) {
                format = "";
            }
        } else {
            format = this.decimalFormat.format(this.b);
        }
        if (this.mTextLength != format.length()) {
            int length = format.length();
            this.mTextLength = length;
            if (length == 1) {
                this.mOuterTextBounds = getInnerCircleRect(this.mCircleBounds);
                RectF rectF = this.mOuterTextBounds;
                float width3 = rectF.left + (rectF.width() * 0.1f);
                RectF rectF2 = this.mOuterTextBounds;
                this.mOuterTextBounds = new RectF(width3, rectF2.top, rectF2.right - (rectF2.width() * 0.1f), this.mOuterTextBounds.bottom);
            } else {
                this.mOuterTextBounds = getInnerCircleRect(this.mCircleBounds);
            }
            if (this.mIsAutoTextSize) {
                setTextSizeAndTextBoundsWithAutoTextSize(width, width2, height, height2, format);
            } else {
                setTextSizeAndTextBoundsWithFixedTextSize(format);
            }
        } else {
            z = false;
        }
        canvas.drawText(format, this.mActualTextBounds.left - (this.mTextPaint.getTextSize() * 0.02f), this.mActualTextBounds.bottom, this.mTextPaint);
        if (this.mShowUnit) {
            if (this.mIsAutoColorEnabled) {
                this.mUnitTextPaint.setColor(calcTextColor(this.b));
            }
            if (z) {
                if (this.mIsAutoTextSize) {
                    setUnitTextBoundsAndSizeWithAutoTextSize(width, width2, height, height2);
                } else {
                    setUnitTextBoundsAndSizeWithFixedTextSize(width * 2.0f, height * 2.0f);
                }
            }
            canvas.drawText(this.mUnit, this.mUnitBounds.left - (this.mUnitTextPaint.getTextSize() * 0.02f), this.mUnitBounds.bottom, this.mUnitTextPaint);
        }
    }

    private RectF getInnerCircleRect(RectF rectF) {
        float f=0;
        float width = (rectF.width() - ((float) (((((rectF.width() - Math.max(this.mBarWidth, this.mRimWidth)) - this.mOuterContourSize) - this.mInnerContourSize) / 2.0d) * Math.sqrt(2.0d)))) / 2.0f;
        float f2 = 1.0f;
        if (isUnitVisible()) {
            switch (AnonymousClass1.f4772a[this.mUnitPosition.ordinal()]) {
                case 1:
                case 2:
                    f2 = 1.1f;
                    f = 0.88f;
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                    f2 = 0.77f;
                    f = 1.33f;
                    break;
            }
            float f3 = f2 * width;
            float f4 = width * f;
            return new RectF(rectF.left + f3, rectF.top + f4, rectF.right - f3, rectF.bottom - f4);
        }
        f = 1.0f;
        float f32 = f2 * width;
        float f42 = width * f;
        return new RectF(rectF.left + f32, rectF.top + f42, rectF.right - f32, rectF.bottom - f42);
    }

    private float getRotationAngleForPointFromStart(PointF pointF) {
        long round = Math.round(calcRotationAngleInDegrees(this.mCenter, pointF));
        return normalizeAngle(this.f4769a == Direction.CW ? (float) (round - this.mStartAngle) : (float) (this.mStartAngle - round));
    }

    private static float normalizeAngle(float f) {
        return ((f % 360.0f) + 360.0f) % 360.0f;
    }

    @SuppressLint("ResourceType")
    private void parseAttributes(TypedArray typedArray) {
        setBarWidth((int) typedArray.getDimension(11, this.mBarWidth));
        setRimWidth((int) typedArray.getDimension(25, this.mRimWidth));
        setSpinSpeed((int) typedArray.getFloat(34, this.k));
        setSpin(typedArray.getBoolean(31, this.l));
        setDirection(Direction.values()[typedArray.getInt(15, 0)]);
        float f = typedArray.getFloat(49, this.b);
        setValue(f);
        this.b = f;
        if (typedArray.hasValue(2) && typedArray.hasValue(3) && typedArray.hasValue(4) && typedArray.hasValue(5)) {
            this.mBarColors = new int[]{typedArray.getColor(2, 16711680), typedArray.getColor(3, 16711680), typedArray.getColor(4, 16711680), typedArray.getColor(5, 16711680)};
        } else if (typedArray.hasValue(2) && typedArray.hasValue(3) && typedArray.hasValue(4)) {
            this.mBarColors = new int[]{typedArray.getColor(2, 16711680), typedArray.getColor(3, 16711680), typedArray.getColor(4, 16711680)};
        } else if (typedArray.hasValue(2) && typedArray.hasValue(3)) {
            this.mBarColors = new int[]{typedArray.getColor(2, 16711680), typedArray.getColor(3, 16711680)};
        } else {
            this.mBarColors = new int[]{typedArray.getColor(2, 16711680), typedArray.getColor(2, 16711680)};
        }
        if (typedArray.hasValue(10)) {
            setBarStrokeCap(StrokeCap.values()[typedArray.getInt(10, 0)].f4785a);
        }
        if (typedArray.hasValue(9) && typedArray.hasValue(6)) {
            setBarStartEndLine((int) typedArray.getDimension(9, 0.0f), BarStartFinishLiner.values()[typedArray.getInt(6, 3)], typedArray.getColor(7, this.mBarStartEndLineColor), typedArray.getFloat(8, this.mBarStartEndLineSweep));
        }
        setSpinBarColor(typedArray.getColor(33, this.mSpinnerColor));
        setSpinningBarLength(typedArray.getFloat(32, this.i));
        if (typedArray.hasValue(40)) {
            setTextSize((int) typedArray.getDimension(40, this.mTextSize));
        }
        if (typedArray.hasValue(46)) {
            setUnitSize((int) typedArray.getDimension(46, this.mUnitTextSize));
        }
        if (typedArray.hasValue(37)) {
            setTextColor(typedArray.getColor(37, this.mTextColor));
        }
        if (typedArray.hasValue(43)) {
            setUnitColor(typedArray.getColor(43, this.mUnitColor));
        }
        if (typedArray.hasValue(0)) {
            setTextColorAuto(typedArray.getBoolean(0, this.mIsAutoColorEnabled));
        }
        if (typedArray.hasValue(1)) {
            setAutoTextSize(typedArray.getBoolean(1, this.mIsAutoTextSize));
        }
        if (typedArray.hasValue(38)) {
            setTextMode(TextMode.values()[typedArray.getInt(38, 0)]);
        }
        if (typedArray.hasValue(44)) {
            setUnitPosition(UnitPosition.values()[typedArray.getInt(44, 3)]);
        }
        if (typedArray.hasValue(36)) {
            setText(typedArray.getString(36));
        }
        setUnitToTextScale(typedArray.getFloat(47, 1.0f));
        setRimColor(typedArray.getColor(24, this.mRimColor));
        setFillCircleColor(typedArray.getColor(16, this.mBackgroundCircleColor));
        setOuterContourColor(typedArray.getColor(22, this.mOuterContourColor));
        setOuterContourSize(typedArray.getDimension(23, this.mOuterContourSize));
        setInnerContourColor(typedArray.getColor(17, this.mInnerContourColor));
        setInnerContourSize(typedArray.getDimension(18, this.mInnerContourSize));
        setMaxValue(typedArray.getFloat(19, this.e));
        setMinValueAllowed(typedArray.getFloat(21, this.f));
        setMaxValueAllowed(typedArray.getFloat(20, this.g));
        setRoundToBlock(typedArray.getBoolean(26, this.mRoundToBlock));
        setRoundToWholeNumber(typedArray.getBoolean(27, this.mRoundToWholeNumber));
        setUnit(typedArray.getString(42));
        setUnitVisible(typedArray.getBoolean(30, this.mShowUnit));
        setTextScale(typedArray.getFloat(39, this.mTextScale));
        setUnitScale(typedArray.getFloat(45, this.mUnitScale));
        setSeekModeEnabled(typedArray.getBoolean(28, this.mSeekModeEnabled));
        setStartAngle(typedArray.getInt(35, this.mStartAngle));
        setShowTextWhileSpinning(typedArray.getBoolean(29, this.mShowTextWhileSpinning));
        if (typedArray.hasValue(12)) {
            setBlockCount(typedArray.getInt(12, 1));
            setBlockScale(typedArray.getFloat(13, 0.9f));
        }
        if (typedArray.hasValue(41)) {
            try {
                this.textTypeface = Typeface.createFromAsset(getContext().getAssets(), typedArray.getString(41));
            } catch (Exception unused) {
            }
        }
        if (typedArray.hasValue(48)) {
            try {
                this.unitTextTypeface = Typeface.createFromAsset(getContext().getAssets(), typedArray.getString(48));
            } catch (Exception unused2) {
            }
        }
        if (typedArray.hasValue(14)) {
            try {
                String string = typedArray.getString(14);
                if (string != null) {
                    this.decimalFormat = new DecimalFormat(string);
                }
            } catch (Exception e) {
                Log.w(TAG, e.getMessage());
            }
        }
        typedArray.recycle();
    }

    private void setSpin(boolean z) {
        this.l = z;
    }

    private void setTextSizeAndTextBoundsWithAutoTextSize(float f, float f2, float f3, float f4, String str) {
        RectF rectF = this.mOuterTextBounds;
        if (this.mShowUnit) {
            int i = AnonymousClass1.f4772a[this.mUnitPosition.ordinal()];
            if (i == 1) {
                RectF rectF2 = this.mOuterTextBounds;
                rectF = new RectF(rectF2.left, rectF2.top + f4 + f3, rectF2.right, rectF2.bottom);
            } else if (i == 2) {
                RectF rectF3 = this.mOuterTextBounds;
                rectF = new RectF(rectF3.left, rectF3.top, rectF3.right, (rectF3.bottom - f4) - f3);
            } else if (i != 3 && i != 5) {
                RectF rectF4 = this.mOuterTextBounds;
                rectF = new RectF(rectF4.left, rectF4.top, (rectF4.right - f2) - f, rectF4.bottom);
            } else {
                RectF rectF5 = this.mOuterTextBounds;
                rectF = new RectF(rectF5.left + f2 + f, rectF5.top, rectF5.right, rectF5.bottom);
            }
        }
        Paint paint = this.mTextPaint;
        paint.setTextSize(calcTextSizeForRect(str, paint, rectF) * this.mTextScale);
        this.mActualTextBounds = calcTextBounds(str, this.mTextPaint, rectF);
    }

    private void setTextSizeAndTextBoundsWithFixedTextSize(String str) {
        this.mTextPaint.setTextSize(this.mTextSize);
        this.mActualTextBounds = calcTextBounds(str, this.mTextPaint, this.mCircleBounds);
    }

    private void setUnitTextBoundsAndSizeWithAutoTextSize(float f, float f2, float f3, float f4) {
        int[] iArr = AnonymousClass1.f4772a;
        int i = iArr[this.mUnitPosition.ordinal()];
        if (i == 1) {
            RectF rectF = this.mOuterTextBounds;
            float f5 = rectF.left;
            float f6 = rectF.top;
            this.mUnitBounds = new RectF(f5, f6, rectF.right, (f4 + f6) - f3);
        } else if (i == 2) {
            RectF rectF2 = this.mOuterTextBounds;
            float f7 = rectF2.left;
            float f8 = rectF2.bottom;
            this.mUnitBounds = new RectF(f7, (f8 - f4) + f3, rectF2.right, f8);
        } else if (i != 3 && i != 5) {
            RectF rectF3 = this.mOuterTextBounds;
            float f9 = rectF3.right;
            float f10 = (f9 - f2) + f;
            float f11 = rectF3.top;
            this.mUnitBounds = new RectF(f10, f11, f9, f4 + f11);
        } else {
            RectF rectF4 = this.mOuterTextBounds;
            float f12 = rectF4.left;
            float f13 = rectF4.top;
            this.mUnitBounds = new RectF(f12, f13, (f2 + f12) - f, f4 + f13);
        }
        Paint paint = this.mUnitTextPaint;
        paint.setTextSize(calcTextSizeForRect(this.mUnit, paint, this.mUnitBounds) * this.mUnitScale);
        this.mUnitBounds = calcTextBounds(this.mUnit, this.mUnitTextPaint, this.mUnitBounds);
        int i2 = iArr[this.mUnitPosition.ordinal()];
        if (i2 == 3 || i2 == 4) {
            float f14 = this.mActualTextBounds.top;
            RectF rectF5 = this.mUnitBounds;
            rectF5.offset(0.0f, f14 - rectF5.top);
        } else if (i2 == 5 || i2 == 6) {
            float f15 = this.mActualTextBounds.bottom;
            RectF rectF6 = this.mUnitBounds;
            rectF6.offset(0.0f, f15 - rectF6.bottom);
        }
    }

    private void setUnitTextBoundsAndSizeWithFixedTextSize(float f, float f2) {
        this.mUnitTextPaint.setTextSize(this.mUnitTextSize);
        this.mUnitBounds = calcTextBounds(this.mUnit, this.mUnitTextPaint, this.mOuterTextBounds);
        int[] iArr = AnonymousClass1.f4772a;
        int i = iArr[this.mUnitPosition.ordinal()];
        if (i == 1) {
            RectF rectF = this.mUnitBounds;
            rectF.offsetTo(rectF.left, (this.mActualTextBounds.top - f2) - rectF.height());
        } else if (i == 2) {
            RectF rectF2 = this.mUnitBounds;
            rectF2.offsetTo(rectF2.left, this.mActualTextBounds.bottom + f2);
        } else if (i != 3 && i != 5) {
            RectF rectF3 = this.mUnitBounds;
            rectF3.offsetTo(this.mActualTextBounds.right + f, rectF3.top);
        } else {
            RectF rectF4 = this.mUnitBounds;
            rectF4.offsetTo((this.mActualTextBounds.left - f) - rectF4.width(), this.mUnitBounds.top);
        }
        int i2 = iArr[this.mUnitPosition.ordinal()];
        if (i2 == 3 || i2 == 4) {
            float f3 = this.mActualTextBounds.top;
            RectF rectF5 = this.mUnitBounds;
            rectF5.offset(0.0f, f3 - rectF5.top);
        } else if (i2 == 5 || i2 == 6) {
            float f4 = this.mActualTextBounds.bottom;
            RectF rectF6 = this.mUnitBounds;
            rectF6.offset(0.0f, f4 - rectF6.bottom);
        }
    }

    private void setupBackgroundCirclePaint() {
        this.mBackgroundCirclePaint.setColor(this.mBackgroundCircleColor);
        this.mBackgroundCirclePaint.setAntiAlias(true);
        this.mBackgroundCirclePaint.setStyle(Paint.Style.FILL);
    }

    private void setupBarPaint() {
        int[] iArr = this.mBarColors;
        if (iArr.length > 1) {
            this.mBarPaint.setShader(new SweepGradient(this.mCircleBounds.centerX(), this.mCircleBounds.centerY(), this.mBarColors, (float[]) null));
            Matrix matrix = new Matrix();
            this.mBarPaint.getShader().getLocalMatrix(matrix);
            matrix.postTranslate(-this.mCircleBounds.centerX(), -this.mCircleBounds.centerY());
            matrix.postRotate(this.mStartAngle);
            matrix.postTranslate(this.mCircleBounds.centerX(), this.mCircleBounds.centerY());
            this.mBarPaint.getShader().setLocalMatrix(matrix);
            this.mBarPaint.setColor(this.mBarColors[0]);
        } else if (iArr.length == 1) {
            this.mBarPaint.setColor(iArr[0]);
            this.mBarPaint.setShader(null);
        } else {
            this.mBarPaint.setColor(16711680);
            this.mBarPaint.setShader(null);
        }
        this.mBarPaint.setAntiAlias(true);
        this.mBarPaint.setStrokeCap(this.mBarStrokeCap);
        this.mBarPaint.setStyle(Paint.Style.STROKE);
        this.mBarPaint.setStrokeWidth(this.mBarWidth);
        if (this.mBarStrokeCap != Paint.Cap.BUTT) {
            Paint paint = new Paint(this.mBarPaint);
            this.mShaderlessBarPaint = paint;
            paint.setShader(null);
            this.mShaderlessBarPaint.setColor(this.mBarColors[0]);
        }
    }

    private void setupBarSpinnerPaint() {
        this.mBarSpinnerPaint.setAntiAlias(true);
        this.mBarSpinnerPaint.setStrokeCap(this.mSpinnerStrokeCap);
        this.mBarSpinnerPaint.setStyle(Paint.Style.STROKE);
        this.mBarSpinnerPaint.setStrokeWidth(this.mBarWidth);
        this.mBarSpinnerPaint.setColor(this.mSpinnerColor);
    }

    private void setupBarStartEndLinePaint() {
        this.mBarStartEndLinePaint.setColor(this.mBarStartEndLineColor);
        this.mBarStartEndLinePaint.setAntiAlias(true);
        this.mBarStartEndLinePaint.setStyle(Paint.Style.STROKE);
        this.mBarStartEndLinePaint.setStrokeWidth(this.mBarStartEndLineWidth);
    }

    private void setupBounds() {
        int min = Math.min(this.mLayoutWidth, this.mLayoutHeight);
        int i = this.mLayoutWidth - min;
        int i2 = (this.mLayoutHeight - min) / 2;
        float paddingTop = getPaddingTop() + i2;
        float paddingBottom = getPaddingBottom() + i2;
        int i3 = i / 2;
        float paddingLeft = getPaddingLeft() + i3;
        float paddingRight = getPaddingRight() + i3;
        int width = getWidth();
        int height = getHeight();
        int i4 = this.mBarWidth;
        int i5 = this.mRimWidth;
        float f = this.mOuterContourSize;
        float f2 = ((float) i4) / 2.0f > (((float) i5) / 2.0f) + f ? i4 / 2.0f : (i5 / 2.0f) + f;
        float f3 = width - paddingRight;
        float f4 = height - paddingBottom;
        this.mCircleBounds = new RectF(paddingLeft + f2, paddingTop + f2, f3 - f2, f4 - f2);
        int i6 = this.mBarWidth;
        this.mInnerCircleBound = new RectF(paddingLeft + i6, paddingTop + i6, f3 - i6, f4 - i6);
        this.mOuterTextBounds = getInnerCircleRect(this.mCircleBounds);
        RectF rectF = this.mCircleBounds;
        float f5 = rectF.left;
        int i7 = this.mRimWidth;
        float f6 = this.mInnerContourSize;
        this.mCircleInnerContour = new RectF(f5 + (i7 / 2.0f) + (f6 / 2.0f), rectF.top + (i7 / 2.0f) + (f6 / 2.0f), (rectF.right - (i7 / 2.0f)) - (f6 / 2.0f), (rectF.bottom - (i7 / 2.0f)) - (f6 / 2.0f));
        RectF rectF2 = this.mCircleBounds;
        float f7 = rectF2.left;
        int i8 = this.mRimWidth;
        float f8 = this.mOuterContourSize;
        this.mCircleOuterContour = new RectF((f7 - (i8 / 2.0f)) - (f8 / 2.0f), (rectF2.top - (i8 / 2.0f)) - (f8 / 2.0f), rectF2.right + (i8 / 2.0f) + (f8 / 2.0f), rectF2.bottom + (i8 / 2.0f) + (f8 / 2.0f));
        this.mCenter = new PointF(this.mCircleBounds.centerX(), this.mCircleBounds.centerY());
    }

    private void setupInnerContourPaint() {
        this.mInnerContourPaint.setColor(this.mInnerContourColor);
        this.mInnerContourPaint.setAntiAlias(true);
        this.mInnerContourPaint.setStyle(Paint.Style.STROKE);
        this.mInnerContourPaint.setStrokeWidth(this.mInnerContourSize);
    }

    private void setupOuterContourPaint() {
        this.mOuterContourPaint.setColor(this.mOuterContourColor);
        this.mOuterContourPaint.setAntiAlias(true);
        this.mOuterContourPaint.setStyle(Paint.Style.STROKE);
        this.mOuterContourPaint.setStrokeWidth(this.mOuterContourSize);
    }

    private void setupRimPaint() {
        this.mRimPaint.setColor(this.mRimColor);
        this.mRimPaint.setAntiAlias(true);
        this.mRimPaint.setStyle(Paint.Style.STROKE);
        this.mRimPaint.setStrokeWidth(this.mRimWidth);
    }

    private void setupTextPaint() {
        this.mTextPaint.setSubpixelText(true);
        this.mTextPaint.setLinearText(true);
        this.mTextPaint.setTypeface(Typeface.MONOSPACE);
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setStyle(Paint.Style.FILL);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize(this.mTextSize);
        Typeface typeface = this.textTypeface;
        if (typeface != null) {
            this.mTextPaint.setTypeface(typeface);
        } else {
            this.mTextPaint.setTypeface(Typeface.MONOSPACE);
        }
    }

    private void setupUnitTextPaint() {
        this.mUnitTextPaint.setStyle(Paint.Style.FILL);
        this.mUnitTextPaint.setAntiAlias(true);
        Typeface typeface = this.unitTextTypeface;
        if (typeface != null) {
            this.mUnitTextPaint.setTypeface(typeface);
        }
    }

    private void triggerOnProgressChanged(float f) {
        OnProgressChangedListener onProgressChangedListener = this.onProgressChangedListener;
        if (onProgressChangedListener == null || f == this.previousProgressChangedValue) {
            return;
        }
        onProgressChangedListener.onProgressChanged(f);
        this.previousProgressChangedValue = f;
    }

    private void triggerReCalcTextSizesAndPositions() {
        this.mTextLength = -1;
        this.mOuterTextBounds = getInnerCircleRect(this.mCircleBounds);
        invalidate();
    }


    public float getCurrentValue() {
        return this.b;
    }

    public int getFillColor() {
        return this.mBackgroundCirclePaint.getColor();
    }

    public float getMaxValue() {
        return this.e;
    }

    public int getTextSize() {
        return this.mTextSize;
    }

    public String getUnit() {
        return this.mUnit;
    }

    public boolean isUnitVisible() {
        return this.mShowUnit;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float f = (360.0f / this.e) * this.b;
        if (this.mBackgroundCircleColor != 0) {
            canvas.drawArc(this.mInnerCircleBound, 360.0f, 360.0f, false, this.mBackgroundCirclePaint);
        }
        if (this.mRimWidth > 0) {
            if (!this.mShowBlock) {
                canvas.drawArc(this.mCircleBounds, 360.0f, 360.0f, false, this.mRimPaint);
            } else {
                drawBlocks(canvas, this.mCircleBounds, this.mStartAngle, 360.0f, false, this.mRimPaint);
            }
        }
        if (this.mOuterContourSize > 0.0f) {
            canvas.drawArc(this.mCircleOuterContour, 360.0f, 360.0f, false, this.mOuterContourPaint);
        }
        if (this.mInnerContourSize > 0.0f) {
            canvas.drawArc(this.mCircleInnerContour, 360.0f, 360.0f, false, this.mInnerContourPaint);
        }
        AnimationState animationState = this.q;
        if (animationState != AnimationState.SPINNING && animationState != AnimationState.END_SPINNING) {
            if (animationState == AnimationState.END_SPINNING_START_ANIMATING) {
                drawSpinner(canvas);
                if (this.o) {
                    drawBar(canvas, f);
                    drawTextWithUnit(canvas);
                } else if (this.mShowTextWhileSpinning) {
                    drawTextWithUnit(canvas);
                }
            } else {
                drawBar(canvas, f);
                drawTextWithUnit(canvas);
            }
        } else {
            drawSpinner(canvas);
            if (this.mShowTextWhileSpinning) {
                drawTextWithUnit(canvas);
            }
        }
        Bitmap bitmap = this.mClippingBitmap;
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.mMaskPaint);
        }
        if (this.mBarStartEndLineWidth <= 0 || this.mBarStartFinishLiner == BarStartFinishLiner.NONE) {
            return;
        }
        drawStartEndLine(canvas, f);
    }

    @Override
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int paddingLeft = (measuredWidth - getPaddingLeft()) - getPaddingRight();
        int paddingTop = (measuredHeight - getPaddingTop()) - getPaddingBottom();
        if (paddingLeft > paddingTop) {
            paddingLeft = paddingTop;
        }
        setMeasuredDimension(getPaddingLeft() + paddingLeft + getPaddingRight(), paddingLeft + getPaddingTop() + getPaddingBottom());
    }

    @Override
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mLayoutWidth = i;
        this.mLayoutHeight = i2;
        setupBounds();
        setupBarPaint();
        Bitmap bitmap = this.mClippingBitmap;
        if (bitmap != null) {
            this.mClippingBitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), false);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        if (!this.mSeekModeEnabled) {
            return super.onTouchEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0 || actionMasked == 1) {
            this.mTouchEventCount = 0;
            setValueAnimated((this.e / 360.0f) * getRotationAngleForPointFromStart(new PointF(motionEvent.getX(), motionEvent.getY())), 800L);
            return true;
        } else if (actionMasked != 2) {
            if (actionMasked != 3) {
                return super.onTouchEvent(motionEvent);
            }
            this.mTouchEventCount = 0;
            return false;
        } else {
            int i = this.mTouchEventCount + 1;
            this.mTouchEventCount = i;
            if (i > 5) {
                setValue((this.e / 360.0f) * getRotationAngleForPointFromStart(new PointF(motionEvent.getX(), motionEvent.getY())));
                return true;
            }
            return false;
        }
    }

    public void setAutoTextSize(boolean z) {
        this.mIsAutoTextSize = z;
    }

    public void setBarColor(@ColorInt int... iArr) {
        this.mBarColors = iArr;
        setupBarPaint();
    }

    public void setBarStartEndLine(int i, BarStartFinishLiner barStartFinishLiner, @ColorInt int i2, float f) {
        this.mBarStartEndLineWidth = i;
        this.mBarStartFinishLiner = barStartFinishLiner;
        this.mBarStartEndLineColor = i2;
        this.mBarStartEndLineSweep = f;
    }

    public void setBarStrokeCap(Paint.Cap cap) {
        this.mBarStrokeCap = cap;
        this.mBarPaint.setStrokeCap(cap);
        if (this.mBarStrokeCap != Paint.Cap.BUTT) {
            Paint paint = new Paint(this.mBarPaint);
            this.mShaderlessBarPaint = paint;
            paint.setShader(null);
            this.mShaderlessBarPaint.setColor(this.mBarColors[0]);
        }
    }

    public void setBarWidth(@IntRange(from = 0) int i) {
        this.mBarWidth = i;
        float f = i;
        this.mBarPaint.setStrokeWidth(f);
        this.mBarSpinnerPaint.setStrokeWidth(f);
    }

    public void setBlockCount(int i) {
        if (i > 1) {
            this.mShowBlock = true;
            this.mBlockCount = i;
            float f = 360.0f / i;
            this.mBlockDegree = f;
            this.mBlockScaleDegree = f * this.mBlockScale;
            return;
        }
        this.mShowBlock = false;
    }

    public void setBlockScale(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        if (f < 0.0f || f > 1.0f) {
            return;
        }
        this.mBlockScale = f;
        this.mBlockScaleDegree = this.mBlockDegree * f;
    }


    public void setDirection(Direction direction) {
        this.f4769a = direction;
    }

    public void setFillCircleColor(@ColorInt int i) {
        this.mBackgroundCircleColor = i;
        this.mBackgroundCirclePaint.setColor(i);
    }

    public void setInnerContourColor(@ColorInt int i) {
        this.mInnerContourColor = i;
        this.mInnerContourPaint.setColor(i);
    }

    public void setInnerContourSize(@FloatRange(from = 0.0d) float f) {
        this.mInnerContourSize = f;
        this.mInnerContourPaint.setStrokeWidth(f);
    }


    public void setMaxValue(@FloatRange(from = 0.0d) float f) {
        this.e = f;
    }

    public void setMaxValueAllowed(@FloatRange(from = 0.0d) float f) {
        this.g = f;
    }

    public void setMinValueAllowed(@FloatRange(from = 0.0d) float f) {
        this.f = f;
    }


    public void setOuterContourColor(@ColorInt int i) {
        this.mOuterContourColor = i;
        this.mOuterContourPaint.setColor(i);
    }

    public void setOuterContourSize(@FloatRange(from = 0.0d) float f) {
        this.mOuterContourSize = f;
        this.mOuterContourPaint.setStrokeWidth(f);
    }

    public void setRimColor(@ColorInt int i) {
        this.mRimColor = i;
        this.mRimPaint.setColor(i);
    }

    public void setRimWidth(@IntRange(from = 0) int i) {
        this.mRimWidth = i;
        this.mRimPaint.setStrokeWidth(i);
    }

    public void setRoundToBlock(boolean z) {
        this.mRoundToBlock = z;
    }

    public void setRoundToWholeNumber(boolean z) {
        this.mRoundToWholeNumber = z;
    }

    public void setSeekModeEnabled(boolean z) {
        this.mSeekModeEnabled = z;
    }


    public void setShowTextWhileSpinning(boolean z) {
        this.mShowTextWhileSpinning = z;
    }

    public void setSpinBarColor(@ColorInt int i) {
        this.mSpinnerColor = i;
        this.mBarSpinnerPaint.setColor(i);
    }

    public void setSpinSpeed(float f) {
        this.k = f;
    }

    public void setSpinnerStrokeCap(Paint.Cap cap) {
        this.mSpinnerStrokeCap = cap;
        this.mBarSpinnerPaint.setStrokeCap(cap);
    }

    public void setSpinningBarLength(@FloatRange(from = 0.0d) float f) {
        this.i = f;
        this.h = f;
    }

    public void setStartAngle(@IntRange(from = 0, to = 360) int i) {
        this.mStartAngle = (int) normalizeAngle(i);
    }

    public void setText(String str) {
        if (str == null) {
            str = "";
        }
        this.mText = str;
        invalidate();
    }

    public void setTextColor(@ColorInt int i) {
        this.mTextColor = i;
        this.mTextPaint.setColor(i);
    }

    public void setTextColorAuto(boolean z) {
        this.mIsAutoColorEnabled = z;
    }

    public void setTextMode(TextMode textMode) {
        this.mTextMode = textMode;
    }

    public void setTextScale(@FloatRange(from = 0.0d) float f) {
        this.mTextScale = f;
    }

    public void setTextSize(@IntRange(from = 0) int i) {
        this.mTextPaint.setTextSize(i);
        this.mTextSize = i;
        this.mIsAutoTextSize = false;
    }

    public void setUnit(String str) {
        if (str == null) {
            this.mUnit = "";
        } else {
            this.mUnit = str;
        }
        invalidate();
    }

    public void setUnitColor(@ColorInt int i) {
        this.mUnitColor = i;
        this.mUnitTextPaint.setColor(i);
        this.mIsAutoColorEnabled = false;
    }

    public void setUnitPosition(UnitPosition unitPosition) {
        this.mUnitPosition = unitPosition;
        triggerReCalcTextSizesAndPositions();
    }

    public void setUnitScale(@FloatRange(from = 0.0d) float f) {
        this.mUnitScale = f;
    }

    public void setUnitSize(@IntRange(from = 0) int i) {
        this.mUnitTextSize = i;
        this.mUnitTextPaint.setTextSize(i);
    }


    public void setUnitToTextScale(@FloatRange(from = 0.0d) float f) {
        this.mRelativeUniteSize = f;
        triggerReCalcTextSizesAndPositions();
    }

    public void setUnitVisible(boolean z) {
        if (z != this.mShowUnit) {
            this.mShowUnit = z;
            triggerReCalcTextSizesAndPositions();
        }
    }

    public void setValue(float f) {
        float f2=0;
        if (this.mShowBlock && this.mRoundToBlock) {
            f = Math.round(f / f2) * (this.e / this.mBlockCount);
        } else if (this.mRoundToWholeNumber) {
            f = Math.round(f);
        }
        float max = Math.max(this.f, f);
        float f3 = this.g;
        if (f3 >= 0.0f) {
            max = Math.min(f3, max);
        }
        Message message = new Message();
        message.what = AnimationText.SET_VALUE.ordinal();
        message.obj = new float[]{max, max};
        this.p.sendMessage(message);
        triggerOnProgressChanged(max);
    }

    public void setupPaints() {
        setupBarPaint();
        setupBarSpinnerPaint();
        setupOuterContourPaint();
        setupInnerContourPaint();
        setupUnitTextPaint();
        setupTextPaint();
        setupBackgroundCirclePaint();
        setupRimPaint();
        setupBarStartEndLinePaint();
    }

    public void spin() {
        setSpin(true);
        this.p.sendEmptyMessage(AnimationText.START_SPINNING.ordinal());
    }

    public void stopSpinning() {
        setSpin(false);
        this.p.sendEmptyMessage(AnimationText.STOP_SPINNING.ordinal());
    }

    private int calcTextColor(double d2) {
        int[] iArr = this.mBarColors;
        int i = 0;
        if (iArr.length <= 1) {
            return iArr.length == 1 ? iArr[0] : ViewCompat.MEASURED_STATE_MASK;
        }
        double maxValue = (1.0f / getMaxValue()) * d2;
        int floor = (int) Math.floor((this.mBarColors.length - 1) * maxValue);
        int i2 = floor + 1;
        if (floor < 0) {
            i2 = 1;
        } else {
            int[] iArr2 = this.mBarColors;
            if (i2 >= iArr2.length) {
                floor = iArr2.length - 2;
                i2 = iArr2.length - 1;
            }
            i = floor;
        }
        int[] iArr3 = this.mBarColors;
        return ColorUtils.getRGBGradient(iArr3[i], iArr3[i2], (float) (1.0d - (((iArr3.length - 1) * maxValue) % 1.0d)));
    }

    public void setValueAnimated(float f, long j) {
        setValueAnimated(this.b, f, j);
    }

    public void setValueAnimated(float f, float f2, long j) {
        float f3=0;
        if (this.mShowBlock && this.mRoundToBlock) {
            f2 = Math.round(f2 / f3) * (this.e / this.mBlockCount);
        } else if (this.mRoundToWholeNumber) {
            f2 = Math.round(f2);
        }
        float max = Math.max(this.f, f2);
        float f4 = this.g;
        if (f4 >= 0.0f) {
            max = Math.min(f4, max);
        }
        this.m = j;
        Message message = new Message();
        message.what = AnimationText.SET_VALUE_ANIMATED.ordinal();
        message.obj = new float[]{f, max};
        this.p.sendMessage(message);
        triggerOnProgressChanged(max);
    }
}
