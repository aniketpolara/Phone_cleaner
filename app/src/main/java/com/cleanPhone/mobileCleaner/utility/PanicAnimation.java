package com.cleanPhone.mobileCleaner.utility;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.cleanPhone.mobileCleaner.R;

import java.util.ArrayList;
import java.util.Iterator;


public class PanicAnimation extends RelativeLayout {
    private static final int DEFAULT_DURATION_TIME = 2500;
    private static final float DEFAULT_SCALE = 10.0f;
    private boolean animationRunning;
    private AnimatorSet animatorSet;
    private Paint paint;
    private float rippleStrokeWidth;
    private int rippleType;
    private ArrayList<RippleView> rippleViewList;

    public class RippleView extends View {
        public RippleView(Context context) {
            super(context);
            setVisibility(View.INVISIBLE);
        }

        @Override
        public void onDraw(Canvas canvas) {
            float min = Math.min(getWidth(), getHeight()) / 2;
            canvas.drawCircle(min, min, min - PanicAnimation.this.rippleStrokeWidth, PanicAnimation.this.paint);
        }
    }

    public PanicAnimation(Context context) {
        super(context);
        this.animationRunning = false;
        this.rippleViewList = new ArrayList<>();
    }

    @SuppressLint("ResourceType")
    private void init(Context context, AttributeSet attributeSet) {
        if (isInEditMode()) {
            return;
        }
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{R.style.RippleBackground});
            int color = obtainStyledAttributes.getColor(0, getResources().getColor(R.color.rippelColor));
            this.rippleStrokeWidth = obtainStyledAttributes.getDimension(5, getResources().getDimension(R.dimen.rippleStrokeWidth));
            float dimension = obtainStyledAttributes.getDimension(2, getResources().getDimension(R.dimen.rippleRadius));
            @SuppressLint("ResourceType") int i = obtainStyledAttributes.getInt(1, DEFAULT_DURATION_TIME);
            @SuppressLint("ResourceType") int i2 = obtainStyledAttributes.getInt(3, 10);
            float f = obtainStyledAttributes.getFloat(4, DEFAULT_SCALE);
            obtainStyledAttributes.recycle();
            int i3 = i / i2;
            Paint paint = new Paint();
            this.paint = paint;
            paint.setAntiAlias(true);
            if (this.rippleType == 0) {
                this.rippleStrokeWidth = 0.0f;
                this.paint.setStyle(Paint.Style.FILL);
            } else {
                this.paint.setStyle(Paint.Style.STROKE);
            }
            this.paint.setColor(color);
            float f2 = this.rippleStrokeWidth;
            LayoutParams layoutParams = new LayoutParams((int) ((dimension + f2) * 2.0f), (int) ((dimension + f2) * 2.0f));
            layoutParams.addRule(13, -1);
            AnimatorSet animatorSet = new AnimatorSet();
            this.animatorSet = animatorSet;
            animatorSet.setDuration(i);
            this.animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            ArrayList arrayList = new ArrayList();
            for (int i4 = 0; i4 < i2; i4++) {
                RippleView rippleView = new RippleView(getContext());
                addView(rippleView, layoutParams);
                this.rippleViewList.add(rippleView);
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(rippleView, "ScaleX", 1.0f, f);
                ofFloat.setRepeatCount(-1);
                ofFloat.setRepeatMode(1);
                long j = i4 * i3;
                ofFloat.setStartDelay(j);
                arrayList.add(ofFloat);
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(rippleView, "ScaleY", 1.0f, f);
                ofFloat2.setRepeatCount(-1);
                ofFloat2.setRepeatMode(1);
                ofFloat2.setStartDelay(j);
                arrayList.add(ofFloat2);
                ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(rippleView, "Alpha", 1.0f, 0.0f);
                ofFloat3.setRepeatCount(-1);
                ofFloat3.setRepeatMode(1);
                ofFloat3.setStartDelay(j);
                arrayList.add(ofFloat3);
            }
            this.animatorSet.playTogether(arrayList);
            return;
        }
        throw new IllegalArgumentException("Attributes should be provided to this view,");
    }

    public boolean isRippleAnimationRunning() {
        return this.animationRunning;
    }

    public void startRippleAnimation() {
        if (isRippleAnimationRunning()) {
            return;
        }
        Iterator<RippleView> it = this.rippleViewList.iterator();
        while (it.hasNext()) {
            it.next().setVisibility(View.VISIBLE);
        }
        this.animatorSet.start();
        this.animationRunning = true;
    }



    public PanicAnimation(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.animationRunning = false;
        this.rippleViewList = new ArrayList<>();
        init(context, attributeSet);
    }

    public PanicAnimation(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.animationRunning = false;
        this.rippleViewList = new ArrayList<>();
        init(context, attributeSet);
    }
}
