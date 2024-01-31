package com.cleanPhone.mobileCleaner.animate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class CricleProgressBarCustom extends View {
    private int bounceDotRadius;
    private int circleRadius;
    private int dotAmount;
    private int dotPosition;
    private int dotRadius;


    public class BounceAnimation extends Animation {
        private BounceAnimation() {
        }

        @Override
        public void applyTransformation(float f, Transformation transformation) {
            super.applyTransformation(f, transformation);
            CricleProgressBarCustom.this.invalidate();
        }
    }

    public CricleProgressBarCustom(Context context) {
        super(context);
        this.dotRadius = 10;
        this.bounceDotRadius = 13;
        this.dotPosition = 1;
        this.dotAmount = 10;
        this.circleRadius = 50;
    }

    public static  int c(CricleProgressBarCustom cricleProgressBarCustom) {
        int i = cricleProgressBarCustom.dotPosition;
        cricleProgressBarCustom.dotPosition = i + 1;
        return i;
    }

    private void createDotInCircle(Canvas canvas, Paint paint) {
        for (int i = 1; i <= this.dotAmount; i++) {
            if (i == this.dotPosition) {
                double d2 = 36 * i * 0.017453292519943295d;
                canvas.drawCircle((float) (this.circleRadius * Math.cos(d2)), (float) (this.circleRadius * Math.sin(d2)), this.bounceDotRadius, paint);
            } else {
                double d3 = 36 * i * 0.017453292519943295d;
                canvas.drawCircle((float) (this.circleRadius * Math.cos(d3)), (float) (this.circleRadius * Math.sin(d3)), this.dotRadius, paint);
            }
        }
    }

    private void startAnimation() {
        BounceAnimation bounceAnimation = new BounceAnimation();
        bounceAnimation.setDuration(150L);
        bounceAnimation.setRepeatCount(-1);
        bounceAnimation.setInterpolator(new LinearInterpolator());
        bounceAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                CricleProgressBarCustom.c(CricleProgressBarCustom.this);
                if (CricleProgressBarCustom.this.dotPosition > CricleProgressBarCustom.this.dotAmount) {
                    CricleProgressBarCustom.this.dotPosition = 1;
                }
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
        startAnimation(bounceAnimation);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ff014e"));
        createDotInCircle(canvas, paint);
    }

    @Override
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int i3 = this.dotRadius;
        setMeasuredDimension((i3 * 3) + 100, (i3 * 3) + 100);
    }

    public CricleProgressBarCustom(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.dotRadius = 10;
        this.bounceDotRadius = 13;
        this.dotPosition = 1;
        this.dotAmount = 10;
        this.circleRadius = 50;
    }

    public CricleProgressBarCustom(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.dotRadius = 10;
        this.bounceDotRadius = 13;
        this.dotPosition = 1;
        this.dotAmount = 10;
        this.circleRadius = 50;
    }
}
