package com.cleanPhone.mobileCleaner.progress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.cleanPhone.mobileCleaner.R;

public class DonutProgress extends View {
    private static final String INSTANCE_BACKGROUND_COLOR = "inner_background_color";
    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
    private static final String INSTANCE_FINISHED_STROKE_WIDTH = "finished_stroke_width";
    private static final String INSTANCE_INNER_BOTTOM_TEXT = "inner_bottom_text";
    private static final String INSTANCE_INNER_BOTTOM_TEXT_COLOR = "inner_bottom_text_color";
    private static final String INSTANCE_INNER_BOTTOM_TEXT_SIZE = "inner_bottom_text_size";
    private static final String INSTANCE_INNER_DRAWABLE = "inner_drawable";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PREFIX = "prefix";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_STARTING_DEGREE = "starting_degree";
    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_SUFFIX = "suffix";
    private static final String INSTANCE_TEXT = "text";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
    private static final String INSTANCE_UNFINISHED_STROKE_WIDTH = "unfinished_stroke_width";
    private int attributeResourceId;
    private final int default_finished_color;
    private final int default_inner_background_color;
    private final int default_inner_bottom_text_color;
    private final float default_inner_bottom_text_size;
    private final int default_max;
    private final int default_startingDegree;
    private final float default_stroke_width;
    private final int default_text_color;
    private final float default_text_size;
    private final int default_unfinished_color;
    private RectF finishedOuterRect;
    private Paint finishedPaint;
    private int finishedStrokeColor;
    private float finishedStrokeWidth;
    private int innerBackgroundColor;
    private String innerBottomText;
    private int innerBottomTextColor;
    private float innerBottomTextHeight;
    public Paint innerBottomTextPaint;
    private float innerBottomTextSize;
    private Paint innerCirclePaint;
    private int max;
    private final int min_size;
    private String prefixText;
    private float progress;
    private boolean showText;
    private int startingDegree;
    private String suffixText;
    private String text;
    private int textColor;
    public Paint textPaint;
    private float textSize;
    private RectF unfinishedOuterRect;
    private Paint unfinishedPaint;
    private int unfinishedStrokeColor;
    private float unfinishedStrokeWidth;

    public DonutProgress(Context context) {
        this(context, null);
    }

    private float getProgressAngle() {
        return (getProgress() / this.max) * 360.0f;
    }

    private int measure(int i) {
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        if (mode == 1073741824) {
            return size;
        }
        int i2 = this.min_size;
        return mode == Integer.MIN_VALUE ? Math.min(i2, size) : i2;
    }

    public int getAttributeResourceId() {
        return this.attributeResourceId;
    }

    public int getFinishedStrokeColor() {
        return this.finishedStrokeColor;
    }

    public float getFinishedStrokeWidth() {
        return this.finishedStrokeWidth;
    }

    public int getInnerBackgroundColor() {
        return this.innerBackgroundColor;
    }

    public String getInnerBottomText() {
        return this.innerBottomText;
    }

    public int getInnerBottomTextColor() {
        return this.innerBottomTextColor;
    }

    public float getInnerBottomTextSize() {
        return this.innerBottomTextSize;
    }

    public int getMax() {
        return this.max;
    }

    public String getPrefixText() {
        return this.prefixText;
    }

    public float getProgress() {
        return this.progress;
    }

    public int getStartingDegree() {
        return this.startingDegree;
    }

    public String getSuffixText() {
        return this.suffixText;
    }

    public String getText() {
        return this.text;
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

    public float getUnfinishedStrokeWidth() {
        return this.unfinishedStrokeWidth;
    }

    @SuppressLint("ResourceType")
    public void initByAttributes(TypedArray typedArray) {
        this.finishedStrokeColor = typedArray.getColor(2, this.default_finished_color);
        this.unfinishedStrokeColor = typedArray.getColor(16, this.default_unfinished_color);
        this.showText = typedArray.getBoolean(11, true);
        this.attributeResourceId = typedArray.getResourceId(7, 0);
        setMax(typedArray.getInt(8, 100));
        setProgress(typedArray.getFloat(10, 0.0f));
        this.finishedStrokeWidth = typedArray.getDimension(3, this.default_stroke_width);
        this.unfinishedStrokeWidth = typedArray.getDimension(17, this.default_stroke_width);
        if (this.showText) {
            if (typedArray.getString(9) != null) {
                this.prefixText = typedArray.getString(9);
            }
            if (typedArray.getString(12) != null) {
                this.suffixText = typedArray.getString(12);
            }
            if (typedArray.getString(13) != null) {
                this.text = typedArray.getString(13);
            }
            this.textColor = typedArray.getColor(14, this.default_text_color);
            this.textSize = typedArray.getDimension(15, this.default_text_size);
            this.innerBottomTextSize = typedArray.getDimension(6, this.default_inner_bottom_text_size);
            this.innerBottomTextColor = typedArray.getColor(5, this.default_inner_bottom_text_color);
            this.innerBottomText = typedArray.getString(4);
        }
        this.innerBottomTextSize = typedArray.getDimension(6, this.default_inner_bottom_text_size);
        this.innerBottomTextColor = typedArray.getColor(5, this.default_inner_bottom_text_color);
        this.innerBottomText = typedArray.getString(4);
        this.startingDegree = typedArray.getInt(1, 0);
        this.innerBackgroundColor = typedArray.getColor(0, 0);
    }

    public void initPainters() {
        if (this.showText) {
            TextPaint textPaint = new TextPaint();
            this.textPaint = textPaint;
            textPaint.setColor(this.textColor);
            this.textPaint.setTextSize(this.textSize);
            this.textPaint.setAntiAlias(true);
            TextPaint textPaint2 = new TextPaint();
            this.innerBottomTextPaint = textPaint2;
            textPaint2.setColor(this.innerBottomTextColor);
            this.innerBottomTextPaint.setTextSize(this.innerBottomTextSize);
            this.innerBottomTextPaint.setAntiAlias(true);
        }
        Paint paint = new Paint();
        this.finishedPaint = paint;
        paint.setColor(this.finishedStrokeColor);
        this.finishedPaint.setStyle(Paint.Style.STROKE);
        this.finishedPaint.setAntiAlias(true);
        this.finishedPaint.setStrokeWidth(this.finishedStrokeWidth);
        Paint paint2 = new Paint();
        this.unfinishedPaint = paint2;
        paint2.setColor(this.unfinishedStrokeColor);
        this.unfinishedPaint.setStyle(Paint.Style.STROKE);
        this.unfinishedPaint.setAntiAlias(true);
        this.unfinishedPaint.setStrokeWidth(this.unfinishedStrokeWidth);
        Paint paint3 = new Paint();
        this.innerCirclePaint = paint3;
        paint3.setColor(this.innerBackgroundColor);
        this.innerCirclePaint.setAntiAlias(true);
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }


    @Override
    public void onDraw(Canvas canvas) {
        Bitmap decodeResource = null;
        super.onDraw(canvas);
        float max = Math.max(this.finishedStrokeWidth, this.unfinishedStrokeWidth);
        this.finishedOuterRect.set(max, max, getWidth() - max, getHeight() - max);
        this.unfinishedOuterRect.set(max, max, getWidth() - max, getHeight() - max);
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, ((getWidth() - Math.min(this.finishedStrokeWidth, this.unfinishedStrokeWidth)) + Math.abs(this.finishedStrokeWidth - this.unfinishedStrokeWidth)) / 2.0f, this.innerCirclePaint);
        canvas.drawArc(this.finishedOuterRect, getStartingDegree(), getProgressAngle(), false, this.finishedPaint);
        canvas.drawArc(this.unfinishedOuterRect, getProgressAngle() + getStartingDegree(), 360.0f - getProgressAngle(), false, this.unfinishedPaint);
        if (this.showText) {
            String str = this.text;
            if (str == null) {
                str = this.prefixText + this.progress + this.suffixText;
            }
            if (!TextUtils.isEmpty(str)) {
                canvas.drawText(str, (getWidth() - this.textPaint.measureText(str)) / 2.0f, (getWidth() - (this.textPaint.descent() + this.textPaint.ascent())) / 2.0f, this.textPaint);
            }
            if (!TextUtils.isEmpty(getInnerBottomText())) {
                this.innerBottomTextPaint.setTextSize(this.innerBottomTextSize);
                canvas.drawText(getInnerBottomText(), (getWidth() - this.innerBottomTextPaint.measureText(getInnerBottomText())) / 2.0f, (getHeight() - this.innerBottomTextHeight) - ((this.textPaint.descent() + this.textPaint.ascent()) / 2.0f), this.innerBottomTextPaint);
            }
        }
        if (this.attributeResourceId != 0) {
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), this.attributeResourceId), (getWidth() - decodeResource.getWidth()) / 2.0f, (getHeight() - decodeResource.getHeight()) / 2.0f, (Paint) null);
        }
    }

    @Override
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(measure(i), measure(i2));
        this.innerBottomTextHeight = getHeight() - ((getHeight() * 3) / 4);
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.textColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            this.textSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            this.innerBottomTextSize = bundle.getFloat(INSTANCE_INNER_BOTTOM_TEXT_SIZE);
            this.innerBottomText = bundle.getString(INSTANCE_INNER_BOTTOM_TEXT);
            this.innerBottomTextColor = bundle.getInt(INSTANCE_INNER_BOTTOM_TEXT_COLOR);
            this.finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
            this.unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
            this.finishedStrokeWidth = bundle.getFloat(INSTANCE_FINISHED_STROKE_WIDTH);
            this.unfinishedStrokeWidth = bundle.getFloat(INSTANCE_UNFINISHED_STROKE_WIDTH);
            this.innerBackgroundColor = bundle.getInt(INSTANCE_BACKGROUND_COLOR);
            this.attributeResourceId = bundle.getInt(INSTANCE_INNER_DRAWABLE);
            initPainters();
            setMax(bundle.getInt(INSTANCE_MAX));
            setStartingDegree(bundle.getInt(INSTANCE_STARTING_DEGREE));
            setProgress(bundle.getFloat("progress"));
            this.prefixText = bundle.getString(INSTANCE_PREFIX);
            this.suffixText = bundle.getString(INSTANCE_SUFFIX);
            this.text = bundle.getString("text");
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize());
        bundle.putFloat(INSTANCE_INNER_BOTTOM_TEXT_SIZE, getInnerBottomTextSize());
        bundle.putFloat(INSTANCE_INNER_BOTTOM_TEXT_COLOR, getInnerBottomTextColor());
        bundle.putString(INSTANCE_INNER_BOTTOM_TEXT, getInnerBottomText());
        bundle.putInt(INSTANCE_INNER_BOTTOM_TEXT_COLOR, getInnerBottomTextColor());
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor());
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_STARTING_DEGREE, getStartingDegree());
        bundle.putFloat("progress", getProgress());
        bundle.putString(INSTANCE_SUFFIX, getSuffixText());
        bundle.putString(INSTANCE_PREFIX, getPrefixText());
        bundle.putString("text", getText());
        bundle.putFloat(INSTANCE_FINISHED_STROKE_WIDTH, getFinishedStrokeWidth());
        bundle.putFloat(INSTANCE_UNFINISHED_STROKE_WIDTH, getUnfinishedStrokeWidth());
        bundle.putInt(INSTANCE_BACKGROUND_COLOR, getInnerBackgroundColor());
        bundle.putInt(INSTANCE_INNER_DRAWABLE, getAttributeResourceId());
        return bundle;
    }

    public void setMax(int i) {
        if (i > 0) {
            this.max = i;
            invalidate();
        }
    }


    public void setProgress(float f) {
        this.progress = f;
        if (f > getMax()) {
            this.progress %= getMax();
        }
        invalidate();
    }


    public void setStartingDegree(int i) {
        this.startingDegree = i;
        invalidate();
    }

    public void setText(String str) {
        this.text = str;
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


    public DonutProgress(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DonutProgress(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.finishedOuterRect = new RectF();
        this.unfinishedOuterRect = new RectF();
        this.attributeResourceId = 0;
        this.progress = 0.0f;
        this.prefixText = "";
        this.suffixText = "%";
        this.text = null;
        this.default_finished_color = Color.rgb(66, 145, 241);
        this.default_unfinished_color = Color.rgb(204, 204, 204);
        this.default_text_color = Color.rgb(66, 145, 241);
        this.default_inner_bottom_text_color = Color.rgb(66, 145, 241);
        this.default_inner_background_color = 0;
        this.default_max = 100;
        this.default_startingDegree = 0;
        this.default_text_size = Utils.sp2px(getResources(), 18.0f);
        this.min_size = (int) Utils.dp2px(getResources(), 100.0f);
        this.default_stroke_width = Utils.dp2px(getResources(), 10.0f);
        this.default_inner_bottom_text_size = Utils.sp2px(getResources(), 18.0f);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, new int[]{R.style.DonutProgress}, i, 0);
        initByAttributes(obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        initPainters();
    }
}
