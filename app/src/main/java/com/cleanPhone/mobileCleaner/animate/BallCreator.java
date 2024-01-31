package com.cleanPhone.mobileCleaner.animate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.cleanPhone.mobileCleaner.R;

import java.util.Random;

public class BallCreator implements GravGenerator {
    private float size = 20.0f;
    private float fromSize = 20.0f;
    private float toSize = 20.0f;
    private Random random = new Random();

    private float getSizeInRange(float f, float f2) {
        return (this.random.nextFloat() * (f - f2)) + f2;
    }

    @SuppressLint("ResourceType")
    @Override
    public void configure(AttributeSet attributeSet, Context context) {

        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, new int[]{R.style.BallGenerator}, 0, 0);
        @SuppressLint("ResourceType") float dimension = obtainStyledAttributes.getDimension(1, this.size);
        this.size = dimension;
        this.fromSize = obtainStyledAttributes.getDimension(0, dimension);
        this.toSize = obtainStyledAttributes.getDimension(2, this.size);
        obtainStyledAttributes.recycle();
    }

    @Override
    public Grav generate(PointF pointF, Paint paint) {
        return new GravBall(pointF, paint, (int) getSizeInRange(this.fromSize, this.toSize));
    }
}
