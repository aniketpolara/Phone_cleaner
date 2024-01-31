package com.cleanPhone.mobileCleaner.utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class SmallBang extends View {
    private long ANIMATE_DURATION;
    private float DOT_BIG_RADIUS;
    private int DOT_NUMBER;
    private float DOT_SMALL_RADIUS;
    private float MAX_CIRCLE_RADIUS;
    private float MAX_RADIUS;
    private float P1;
    private float P2;
    private float P3;
    private float RING_WIDTH;
    public int[] f5335a;
    public List<Dot> b;
    private int centerX;
    private int centerY;
    private Paint circlePaint;
    private int[] mExpandInset;
    private SmallBangListener mListener;
    private float progress;

    public class Dot {

        public int f5339a;
        public int b;

        public Dot(SmallBang smallBang) {
        }
    }

    public SmallBang(Context context) {
        super(context);
        this.f5335a = new int[]{-2145656, -3306504, -13918734, -5968204, -2058294, -3494714, -3824132, -672746, -860216, -1982834, -3618915};
        this.b = new ArrayList();
        this.ANIMATE_DURATION = 1000L;
        this.MAX_RADIUS = 150.0f;
        this.MAX_CIRCLE_RADIUS = 100.0f;
        this.RING_WIDTH = 10.0f;
        this.P1 = 0.15f;
        this.P2 = 0.28f;
        this.P3 = 0.3f;
        this.DOT_NUMBER = 16;
        this.DOT_BIG_RADIUS = 8.0f;
        this.DOT_SMALL_RADIUS = 5.0f;
        this.mExpandInset = new int[2];
        init(null, 0);
    }

    @SuppressLint("ResourceType")
    public static SmallBang attach2Window(Activity activity) {
        SmallBang smallBang = new SmallBang(activity);
        ((ViewGroup) activity.findViewById(16908290)).addView(smallBang, new ViewGroup.LayoutParams(-1, -1));
        return smallBang;
    }

    private int evaluateColor(int i, int i2, float f) {
        if (f <= 0.0f) {
            return i;
        }
        if (f >= 1.0f) {
            return i2;
        }
        int i3 = (i >> 24) & 255;
        int i4 = (i >> 16) & 255;
        int i5 = (i >> 8) & 255;
        int i6 = i & 255;
        return (i6 + ((int) (f * ((i2 & 255) - i6)))) | ((i3 + ((int) ((((i2 >> 24) & 255) - i3) * f))) << 24) | ((i4 + ((int) ((((i2 >> 16) & 255) - i4) * f))) << 16) | ((i5 + ((int) ((((i2 >> 8) & 255) - i5) * f))) << 8);
    }

    private void init(AttributeSet attributeSet, int i) {
        Paint paint = new Paint(1);
        this.circlePaint = paint;
        paint.setStyle(Paint.Style.FILL);
        this.circlePaint.setColor(ViewCompat.MEASURED_STATE_MASK);
    }

    private void initDots() {
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < this.DOT_NUMBER * 2; i++) {
            Dot dot = new Dot(this);
            int[] iArr = this.f5335a;
            int nextInt = random.nextInt(99999);
            int[] iArr2 = this.f5335a;
            dot.f5339a = iArr[nextInt % iArr2.length];
            dot.b = iArr2[random.nextInt(99999) % this.f5335a.length];
            this.b.add(dot);
        }
    }

    private void initRadius(float f) {
        this.MAX_CIRCLE_RADIUS = f;
        this.MAX_RADIUS = 1.1f * f;
        float f2 = f * 0.07f;
        this.DOT_BIG_RADIUS = f2;
        this.DOT_SMALL_RADIUS = f2 * 0.5f;
    }

    public void bang(View view, SmallBangListener smallBangListener) {
        bang(view, -1.0f, smallBangListener);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        float f = this.progress;
        if (f >= 0.0f) {
            float f2 = this.P1;
            if (f <= f2) {
                float f3 = (1.0f / f2) * f;
                float f4 = f3 <= 1.0f ? f3 : 1.0f;
                int[] iArr = this.f5335a;
                int i = iArr[0];
                int i2 = iArr[1];
                this.circlePaint.setStyle(Paint.Style.FILL);
                this.circlePaint.setColor(evaluateColor(i, i2, f4));
                canvas.drawCircle(this.centerX, this.centerY, this.MAX_CIRCLE_RADIUS * f4, this.circlePaint);
                return;
            }
        }
        float f5 = this.P1;
        if (f > f5) {
            if (f > f5) {
                float f6 = this.P3;
                if (f <= f6) {
                    float f7 = (f - f5) / (f6 - f5);
                    float f8 = f7 >= 0.0f ? f7 : 0.0f;
                    if (f8 > 1.0f) {
                        f8 = 1.0f;
                    }
                    this.circlePaint.setStyle(Paint.Style.STROKE);
                    float f9 = this.MAX_CIRCLE_RADIUS * (1.0f - f8);
                    this.circlePaint.setStrokeWidth(f9);
                    canvas.drawCircle(this.centerX, this.centerY, (this.MAX_CIRCLE_RADIUS * f8) + (f9 / 2.0f), this.circlePaint);
                }
            }
            if (this.progress >= this.P2) {
                this.circlePaint.setStyle(Paint.Style.FILL);
                float f10 = this.progress;
                float f11 = this.P2;
                float f12 = (f10 - f11) / (1.0f - f11);
                float f13 = this.MAX_CIRCLE_RADIUS;
                float f14 = f13 + ((this.MAX_RADIUS - f13) * f12);
                for (int i3 = 0; i3 < this.b.size(); i3 += 2) {
                    Dot dot = this.b.get(i3);
                    this.circlePaint.setColor(evaluateColor(dot.f5339a, dot.b, f12));
                    double d2 = f14;
                    double d3 = i3 * 2 * 3.141592653589793d;
                    float f15 = 1.0f - f12;
                    canvas.drawCircle(((float) (Math.cos(d3 / this.DOT_NUMBER) * d2)) + this.centerX, ((float) (Math.sin(d3 / this.DOT_NUMBER) * d2)) + this.centerY, this.DOT_BIG_RADIUS * f15, this.circlePaint);
                    Dot dot2 = this.b.get(i3 + 1);
                    this.circlePaint.setColor(evaluateColor(dot2.f5339a, dot2.b, f12));
                    canvas.drawCircle(((float) (Math.cos((d3 / this.DOT_NUMBER) + 0.2d) * d2)) + this.centerX, ((float) (d2 * Math.sin((d3 / this.DOT_NUMBER) + 0.2d))) + this.centerY, this.DOT_SMALL_RADIUS * f15, this.circlePaint);
                }
            }
        }
    }

    public void setColors(int[] iArr) {
        this.f5335a = Arrays.copyOf(iArr, iArr.length);
    }

    public void setDotNumber(int i) {
        this.DOT_NUMBER = i;
    }

    public void setmListener(SmallBangListener smallBangListener) {
        this.mListener = smallBangListener;
    }

    public void bang(final View view, float f, SmallBangListener smallBangListener) {
        if (smallBangListener != null) {
            setmListener(smallBangListener);
            this.mListener.onAnimationStart();
        }
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        rect.offset(-iArr[0], -iArr[1]);
        int[] iArr2 = this.mExpandInset;
        rect.inset(-iArr2[0], -iArr2[1]);
        this.centerX = rect.left + (rect.width() / 2);
        this.centerY = rect.top + (rect.height() / 2);
        if (f != -1.0f) {
            initRadius(f);
        } else {
            initRadius(Math.max(rect.width(), rect.height()));
        }
        view.setScaleX(0.1f);
        view.setScaleY(0.1f);
        ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration((long) (ANIMATE_DURATION * 0.5f));
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = (valueAnimator.getAnimatedFraction() * 0.9f) + 0.1f;
                view.setScaleX(animatedFraction);
                view.setScaleY(animatedFraction);
            }
        });
        duration.setInterpolator(new OvershootInterpolator(2.0f));
        duration.setStartDelay((long) (ANIMATE_DURATION * P3));
        duration.start();
        bang();
    }

    public SmallBang(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f5335a = new int[]{-2145656, -3306504, -13918734, -5968204, -2058294, -3494714, -3824132, -672746, -860216, -1982834, -3618915};
        this.b = new ArrayList();
        this.ANIMATE_DURATION = 1000L;
        this.MAX_RADIUS = 150.0f;
        this.MAX_CIRCLE_RADIUS = 100.0f;
        this.RING_WIDTH = 10.0f;
        this.P1 = 0.15f;
        this.P2 = 0.28f;
        this.P3 = 0.3f;
        this.DOT_NUMBER = 16;
        this.DOT_BIG_RADIUS = 8.0f;
        this.DOT_SMALL_RADIUS = 5.0f;
        this.mExpandInset = new int[2];
        init(attributeSet, 0);
    }

    public void bang(View view) {
        bang(view, null);
    }

    private void bang() {
        new ValueAnimator();
        ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(this.ANIMATE_DURATION);
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.mobiclean.phoneclean.utility.SmallBang.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                SmallBang.this.progress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                SmallBang.this.invalidate();
            }
        });
        duration.start();
        duration.addListener(new AnimatorListenerAdapter() { // from class: com.mobiclean.phoneclean.utility.SmallBang.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (SmallBang.this.mListener != null) {
                    SmallBang.this.mListener.onAnimationEnd();
                }
            }
        });
        initDots();
    }

    public SmallBang(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f5335a = new int[]{-2145656, -3306504, -13918734, -5968204, -2058294, -3494714, -3824132, -672746, -860216, -1982834, -3618915};
        this.b = new ArrayList();
        this.ANIMATE_DURATION = 1000L;
        this.MAX_RADIUS = 150.0f;
        this.MAX_CIRCLE_RADIUS = 100.0f;
        this.RING_WIDTH = 10.0f;
        this.P1 = 0.15f;
        this.P2 = 0.28f;
        this.P3 = 0.3f;
        this.DOT_NUMBER = 16;
        this.DOT_BIG_RADIUS = 8.0f;
        this.DOT_SMALL_RADIUS = 5.0f;
        this.mExpandInset = new int[2];
        init(attributeSet, i);
    }

    @TargetApi(21)
    public SmallBang(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.f5335a = new int[]{-2145656, -3306504, -13918734, -5968204, -2058294, -3494714, -3824132, -672746, -860216, -1982834, -3618915};
        this.b = new ArrayList();
        this.ANIMATE_DURATION = 1000L;
        this.MAX_RADIUS = 150.0f;
        this.MAX_CIRCLE_RADIUS = 100.0f;
        this.RING_WIDTH = 10.0f;
        this.P1 = 0.15f;
        this.P2 = 0.28f;
        this.P3 = 0.3f;
        this.DOT_NUMBER = 16;
        this.DOT_BIG_RADIUS = 8.0f;
        this.DOT_SMALL_RADIUS = 5.0f;
        this.mExpandInset = new int[2];
        init(attributeSet, i);
    }
}
