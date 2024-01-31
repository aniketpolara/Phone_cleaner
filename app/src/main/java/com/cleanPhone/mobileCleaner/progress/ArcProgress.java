package com.cleanPhone.mobileCleaner.progress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.Util;


public class ArcProgress extends View {
    private static final String INSTANCE_ARC_ANGLE = "arc_angle";
    private static final String INSTANCE_BOTTOM_TEXT = "bottom_text";
    private static final String INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size";
    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_STROKE_WIDTH = "stroke_width";
    private static final String INSTANCE_SUFFIX = "suffix";
    private static final String INSTANCE_SUFFIX_TEXT_PADDING = "suffix_text_padding";
    private static final String INSTANCE_SUFFIX_TEXT_SIZE = "suffix_text_size";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
    private float arcAngle;
    private float arcBottomHeight;
    private String bottomText;
    private float bottomTextSize;
    private Activity context;
    private final float default_arc_angle;
    private final float default_bottom_text_size;
    private final int default_finished_color;
    private final int default_max;
    private final float default_stroke_width;
    private final float default_suffix_padding;
    private final String default_suffix_text;
    private final float default_suffix_text_size;
    private final int default_text_color;
    private float default_text_size;
    private final int default_unfinished_color;
    private int finishedStrokeColor;
    private boolean isdashed;
    private int max;
    private final int min_size;
    private Paint paint;
    private Paint paint2;
    private int progress;
    private RectF rectF;
    private float strokeWidth;
    private String suffixText;
    private float suffixTextPadding;
    private float suffixTextSize;
    private float tempProgress;
    private int textColor;
    public Paint textPaint;
    public Paint textPaintCenter;
    private float textSize;
    private int unfinishedStrokeColor;
    private String uppertext;
    private float uppertextSize;

    public ArcProgress(Context context) {
        this(context, null);
    }

    public float getArcAngle() {
        return this.arcAngle;
    }

    public String getBottomText() {
        return this.bottomText;
    }

    public float getBottomTextSize() {
        return this.bottomTextSize;
    }

    public int getFinishedStrokeColor() {
        return this.finishedStrokeColor;
    }

    public int getMax() {
        return this.max;
    }

    public int getProgress() {
        return this.progress;
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public String getSuffixText() {
        return this.suffixText;
    }

    public float getSuffixTextPadding() {
        return this.suffixTextPadding;
    }

    public float getSuffixTextSize() {
        return this.suffixTextSize;
    }

    @Override
    public int getSuggestedMinimumHeight() {
        return this.min_size;
    }

    @Override
    public int getSuggestedMinimumWidth() {
        return this.min_size;
    }

    public float getTempProgress() {
        return this.tempProgress;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public int getUnfinishedStrokeColor() {
        return this.unfinishedStrokeColor;
    }

    @SuppressLint("ResourceType")
    public void initByAttributes(TypedArray typedArray) {
        this.finishedStrokeColor = typedArray.getColor(3, -1);
        this.unfinishedStrokeColor = typedArray.getColor(12, this.default_unfinished_color);
        this.textColor = typedArray.getColor(10, -1);
        this.textSize = typedArray.getInt(11, 5);
        this.arcAngle = typedArray.getFloat(0, 270.0f);
        setMax(typedArray.getInt(4, 100));
        setProgress(typedArray.getInt(5, 0));
        this.strokeWidth = typedArray.getFloat(6, 1.0f);
        this.isdashed = typedArray.getBoolean(13, false);
        this.strokeWidth = (ParentActivity.displaymetrics.widthPixels * (this.strokeWidth / 100.0f)) / 100.0f;
        this.suffixTextSize = typedArray.getInt(9, 3);
        this.suffixText = TextUtils.isEmpty(typedArray.getString(7)) ? this.default_suffix_text : typedArray.getString(7);
        this.suffixTextPadding = typedArray.getDimension(8, this.default_suffix_padding);
        this.bottomTextSize = typedArray.getInt(2, 5);
        this.bottomText = typedArray.getString(1);
        this.uppertext = typedArray.getString(14);
        this.uppertextSize = typedArray.getInt(15, 2);
    }

    public void initPainters() {
        TextPaint textPaint = new TextPaint();
        this.textPaint = textPaint;
        textPaint.setColor(this.textColor);
        this.textPaint.setTextSize(14.0f);
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        TextPaint textPaint2 = new TextPaint();
        this.textPaintCenter = textPaint2;
        textPaint2.setColor(this.textColor);
        this.textPaintCenter.setTextSize(14.0f);
        this.textPaintCenter.setAntiAlias(true);
        this.textPaintCenter.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        Paint paint = new Paint();
        this.paint = paint;
        paint.setColor(this.default_unfinished_color);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(this.strokeWidth);
        this.paint.setStyle(Paint.Style.STROKE);
        if (Util.isHome) {
            this.paint.setStrokeCap(Paint.Cap.ROUND);
        } else {
            this.paint.setStrokeCap(Paint.Cap.BUTT);
        }
        Paint paint2 = new Paint();
        this.paint2 = paint2;
        paint2.setColor(this.default_unfinished_color);
        this.paint2.setAntiAlias(true);
        this.paint2.setStrokeWidth(this.strokeWidth);
        this.paint2.setStyle(Paint.Style.STROKE);
        this.paint2.setStrokeCap(Paint.Cap.BUTT);
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float f = 270.0f - (this.arcAngle / 2.0f);
        float max = (this.progress / getMax()) * this.arcAngle;
        float f2 = this.progress == 0 ? 0.01f : f;
        this.paint.setColor(this.unfinishedStrokeColor);
        if (!this.isdashed) {
            canvas.drawArc(this.rectF, f, this.arcAngle, false, this.paint);
        } else {
            this.paint.setPathEffect(new DashPathEffect(new float[]{1.0f, 16.0f}, 4.0f));
            canvas.drawArc(this.rectF, f, this.arcAngle, false, this.paint);
            this.paint.setPathEffect(new DashPathEffect(new float[]{0.0f, 0.0f}, 0.0f));
        }
        this.paint.setColor(this.finishedStrokeColor);
        canvas.drawArc(this.rectF, f2, max, false, this.paint);
        String valueOf = String.valueOf(getProgress());
        if (!TextUtils.isEmpty(valueOf)) {
            this.textPaint.setColor(this.textColor);
            this.textPaint.setTextSize((ParentActivity.displaymetrics.heightPixels * this.textSize) / 100.0f);
            float descent = this.textPaint.descent() + this.textPaint.ascent();
            float height = (getHeight() - descent) / 2.0f;
            canvas.drawText(valueOf, (getWidth() - this.textPaint.measureText(valueOf)) / 2.0f, height, this.textPaint);
            this.textPaint.setTextSize((ParentActivity.displaymetrics.heightPixels * this.suffixTextSize) / 100.0f);
            canvas.drawText(this.suffixText, (getWidth() / 2.0f) + this.textPaint.measureText(valueOf) + this.suffixTextPadding, (height + descent) - (this.textPaint.descent() + this.textPaint.ascent()), this.textPaint);
            this.textPaintCenter.setTextSize((ParentActivity.displaymetrics.heightPixels * this.uppertextSize) / 100.0f);
            float descent2 = this.textPaintCenter.descent() + this.textPaintCenter.ascent();
            try {
                canvas.drawText("" + this.uppertext, (getWidth() - this.textPaintCenter.measureText("" + this.uppertext)) / 2.0f, ((getHeight() / 2.0f) - descent2) / 2.0f, this.textPaintCenter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.arcBottomHeight == 0.0f) {
            this.arcBottomHeight = (getWidth() / 2.0f) * ((float) (1.0d - Math.cos((((360.0f - this.arcAngle) / 2.0f) / 180.0f) * 3.141592653589793d)));
        }
        if (TextUtils.isEmpty(getBottomText())) {
            return;
        }
        this.textPaint.setTextSize((ParentActivity.displaymetrics.heightPixels * this.bottomTextSize) / 100.0f);
        float height2 = (getHeight() - this.arcBottomHeight) - ((this.textPaint.descent() + this.textPaint.ascent()) / 2.0f);
        this.textPaint.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        canvas.drawText(getBottomText(), (getWidth() - this.textPaint.measureText(getBottomText())) / 2.0f, height2, this.textPaint);
    }

    @Override
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(i, i2);
        int size = MeasureSpec.getSize(i);
        RectF rectF = this.rectF;
        float f = this.strokeWidth;
        float f2 = size;
        rectF.set(f / 2.0f, f / 2.0f, f2 - (f / 2.0f), MeasureSpec.getSize(i2) - (this.strokeWidth / 2.0f));
        this.arcBottomHeight = (f2 / 2.0f) * ((float) (1.0d - Math.cos((((360.0f - this.arcAngle) / 2.0f) / 180.0f) * 3.141592653589793d)));
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.strokeWidth = bundle.getFloat(INSTANCE_STROKE_WIDTH);
            this.suffixTextSize = bundle.getFloat(INSTANCE_SUFFIX_TEXT_SIZE);
            this.suffixTextPadding = bundle.getFloat(INSTANCE_SUFFIX_TEXT_PADDING);
            this.bottomTextSize = bundle.getFloat(INSTANCE_BOTTOM_TEXT_SIZE);
            this.bottomText = bundle.getString(INSTANCE_BOTTOM_TEXT);
            this.textSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            this.textColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt("progress"));
            this.finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
            this.unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
            this.suffixText = bundle.getString(INSTANCE_SUFFIX);
            initPainters();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_STROKE_WIDTH, getStrokeWidth());
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_SIZE, getSuffixTextSize());
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_PADDING, getSuffixTextPadding());
        bundle.putFloat(INSTANCE_BOTTOM_TEXT_SIZE, getBottomTextSize());
        bundle.putString(INSTANCE_BOTTOM_TEXT, getBottomText());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putInt("progress", getProgress());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor());
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor());
        bundle.putFloat(INSTANCE_ARC_ANGLE, getArcAngle());
        bundle.putString(INSTANCE_SUFFIX, getSuffixText());
        return bundle;
    }


    public void setMax(int i) {
        if (i > 0) {
            this.max = i;
            invalidate();
        }
    }

    public void setProgress(int i) {
        this.progress = i;
        if (i > getMax()) {
            this.progress %= getMax();
        }
        invalidate();
    }


    public void setTextColor(int i) {
        this.textColor = i;
        invalidate();
    }

    public void setTextSize(float f) {
        this.textSize = f;
        invalidate();
    }


    public ArcProgress(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ArcProgress(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.rectF = new RectF();
        this.progress = 0;
        this.suffixText = "%";
        this.default_finished_color = -1;
        this.default_unfinished_color = Color.rgb(28, 53, 115);
        this.default_text_color = -1;
        this.default_max = 100;
        this.default_arc_angle = 270.0f;
        this.isdashed = false;
        this.tempProgress = 0.0f;
        this.default_text_size = Utils.sp2px(getResources(), 26.0f);
        this.min_size = (int) Utils.dp2px(getResources(), 100.0f);
        this.default_text_size = Utils.sp2px(getResources(), 40.0f);
        this.default_suffix_text_size = Utils.sp2px(getResources(), 15.0f);
        this.default_suffix_padding = Utils.dp2px(getResources(), 1.0f);
        this.default_suffix_text = "%";
        this.default_bottom_text_size = Utils.sp2px(getResources(), 10.0f);
        this.default_stroke_width = Utils.dp2px(getResources(), 4.0f);
        if (ParentActivity.displaymetrics == null) {
            ParentActivity.displaymetrics = new DisplayMetrics();
            try {
                ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(ParentActivity.displaymetrics);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, new int[]{R.style.ArcProgress}, i, 0);
        initByAttributes(obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        initPainters();
    }
}
