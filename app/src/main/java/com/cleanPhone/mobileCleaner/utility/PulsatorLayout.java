package com.cleanPhone.mobileCleaner.utility;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PulsatorLayout extends RelativeLayout {
    private static final int DEFAULT_COLOR = Color.rgb(0, 116, 193);
    private static final int DEFAULT_COUNT = 4;
    private static final int DEFAULT_DURATION = 7000;
    private static final int DEFAULT_INTERPOLATOR = 0;
    private static final int DEFAULT_REPEAT = 0;
    private static final boolean DEFAULT_START_FROM_SCRATCH = true;
    public static final int INFINITE = 0;
    public static final int INTERP_ACCELERATE = 1;
    public static final int INTERP_ACCELERATE_DECELERATE = 3;
    public static final int INTERP_DECELERATE = 2;
    public static final int INTERP_LINEAR = 0;
    private final Animator.AnimatorListener mAnimatorListener;
    private AnimatorSet mAnimatorSet;
    private float mCenterX;
    private float mCenterY;
    private int mColor;
    private int mCount;
    private int mDuration;
    private int mInterpolator;
    private boolean mIsStarted;
    private Paint mPaint;
    private float mRadius;
    private int mRepeat;
    private boolean mStartFromScratch;
    private final List<View> mViews;

    public class PulseView extends View {
        public PulseView(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawCircle(PulsatorLayout.this.mCenterX, PulsatorLayout.this.mCenterY, PulsatorLayout.this.mRadius, PulsatorLayout.this.mPaint);
        }
    }

    public PulsatorLayout(Context context) {
        this(context, null, 0);
    }

    private void build() {
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        int i = this.mRepeat;
        int i2 = i != 0 ? i : -1;
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < this.mCount; i3++) {
            PulseView pulseView = new PulseView(getContext());
            pulseView.setScaleX(0.0f);
            pulseView.setScaleY(0.0f);
            pulseView.setAlpha(1.0f);
            addView(pulseView, i3, layoutParams);
            this.mViews.add(pulseView);
            long j = (this.mDuration * i3) / this.mCount;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(pulseView, "ScaleX", 0.0f, 1.0f);
            ofFloat.setRepeatCount(i2);
            ofFloat.setRepeatMode(ValueAnimator.RESTART);
            ofFloat.setStartDelay(j);
            arrayList.add(ofFloat);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(pulseView, "ScaleY", 0.0f, 1.0f);
            ofFloat2.setRepeatCount(i2);
            ofFloat2.setRepeatMode(ValueAnimator.RESTART);
            ofFloat2.setStartDelay(j);
            arrayList.add(ofFloat2);
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(pulseView, "Alpha", 1.0f, 0.0f);
            ofFloat3.setRepeatCount(i2);
            ofFloat3.setRepeatMode(ValueAnimator.RESTART);
            ofFloat3.setStartDelay(j);
            arrayList.add(ofFloat3);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        this.mAnimatorSet = animatorSet;
        animatorSet.playTogether(arrayList);
        this.mAnimatorSet.setInterpolator(createInterpolator(this.mInterpolator));
        this.mAnimatorSet.setDuration(this.mDuration);
        this.mAnimatorSet.addListener(this.mAnimatorListener);
    }

    private void clear() {
        stop();
        for (View view : this.mViews) {
            removeView(view);
        }
        this.mViews.clear();
    }

    private static Interpolator createInterpolator(int i) {
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    return new LinearInterpolator();
                }
                return new AccelerateDecelerateInterpolator();
            }
            return new DecelerateInterpolator();
        }
        return new AccelerateInterpolator();
    }

    private void reset() {
        boolean isStarted = isStarted();
        clear();
        build();
        if (isStarted) {
            start();
        }
    }

    public int getColor() {
        return this.mColor;
    }

    public int getCount() {
        return this.mCount;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public int getInterpolator() {
        return this.mInterpolator;
    }

    public synchronized boolean isStarted() {
        boolean z = false;
        if (this.mAnimatorSet != null) {
            z = this.mIsStarted ? DEFAULT_START_FROM_SCRATCH : false;
        }
        return z;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AnimatorSet animatorSet = this.mAnimatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.mAnimatorSet = null;
        }
    }

    @Override
    public void onMeasure(int i, int i2) {
        int size = (MeasureSpec.getSize(i) - getPaddingLeft()) - getPaddingRight();
        int size2 = (MeasureSpec.getSize(i2) - getPaddingTop()) - getPaddingBottom();
        this.mCenterX = size * 0.5f;
        this.mCenterY = size2 * 0.5f;
        this.mRadius = Math.min(size, size2) * 0.5f;
        super.onMeasure(i, i2);
    }

    public void setColor(int i) {
        if (i != this.mColor) {
            this.mColor = i;
            Paint paint = this.mPaint;
            if (paint != null) {
                paint.setColor(i);
            }
        }
    }

    public void setCount(int i) {
        if (i >= 0) {
            if (i != this.mCount) {
                this.mCount = i;
                reset();
                invalidate();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Count cannot be negative");
    }

    public void setDuration(int i) {
        if (i >= 0) {
            if (i != this.mDuration) {
                this.mDuration = i;
                reset();
                invalidate();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Duration cannot be negative");
    }

    public void setInterpolator(int i) {
        if (i != this.mInterpolator) {
            this.mInterpolator = i;
            reset();
            invalidate();
        }
    }

    public synchronized void start() {
        AnimatorSet animatorSet = this.mAnimatorSet;
        if (animatorSet != null && !this.mIsStarted) {
            animatorSet.start();
            if (!this.mStartFromScratch) {
                Iterator<Animator> it = this.mAnimatorSet.getChildAnimations().iterator();
                while (it.hasNext()) {
                    ObjectAnimator objectAnimator = (ObjectAnimator) it.next();
                    long startDelay = objectAnimator.getStartDelay();
                    objectAnimator.setStartDelay(0L);
                    objectAnimator.setCurrentPlayTime(this.mDuration - startDelay);
                }
            }
        }
    }

    public synchronized void stop() {
        AnimatorSet animatorSet = this.mAnimatorSet;
        if (animatorSet != null && this.mIsStarted) {
            animatorSet.end();
        }
    }

    public PulsatorLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @SuppressLint("ResourceType")
    public PulsatorLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mViews = new ArrayList();
        this.mAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animator) {
                PulsatorLayout.this.mIsStarted = false;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                PulsatorLayout.this.mIsStarted = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }

            @Override
            public void onAnimationStart(Animator animator) {
                PulsatorLayout.this.mIsStarted = PulsatorLayout.DEFAULT_START_FROM_SCRATCH;
            }
        };
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet,null, 0, 0);
        this.mCount = 4;
        this.mDuration = DEFAULT_DURATION;
        this.mRepeat = 0;
        this.mStartFromScratch = DEFAULT_START_FROM_SCRATCH;
        int i2 = DEFAULT_COLOR;
        this.mColor = i2;
        this.mInterpolator = 0;
        try {
            this.mCount = obtainStyledAttributes.getInteger(1, 4);
            this.mDuration = obtainStyledAttributes.getInteger(2, DEFAULT_DURATION);
            this.mRepeat = obtainStyledAttributes.getInteger(5, 0);
            this.mStartFromScratch = obtainStyledAttributes.getBoolean(6, DEFAULT_START_FROM_SCRATCH);
            this.mColor = obtainStyledAttributes.getColor(0, i2);
            this.mInterpolator = obtainStyledAttributes.getInteger(3, 0);
            obtainStyledAttributes.recycle();
            Paint paint = new Paint();
            this.mPaint = paint;
            paint.setAntiAlias(DEFAULT_START_FROM_SCRATCH);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setColor(this.mColor);
            build();
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }
}
