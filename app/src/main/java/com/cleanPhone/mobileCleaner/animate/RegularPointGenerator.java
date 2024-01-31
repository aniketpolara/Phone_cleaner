package com.cleanPhone.mobileCleaner.animate;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.cleanPhone.mobileCleaner.R;

import java.util.Random;
import java.util.Vector;

public class RegularPointGenerator implements PointGenerator {
    private final Random random = new Random();
    private int cellSize = 300;
    private int variance = 5;

    @Override
    public void configure(AttributeSet attributeSet, Context context) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, new int[]{R.style.RegularPointGenerator}, 0, 0);
        this.cellSize = obtainStyledAttributes.getInteger(0, this.cellSize);
        this.variance = obtainStyledAttributes.getInteger(1, this.variance);
        obtainStyledAttributes.recycle();
    }

    @Override
    public Vector<PointF> generatePoints(int i, int i2) {
        Vector<PointF> vector = new Vector<>();
        int i3 = 0;
        while (i3 < i2) {
            int i4 = 0;
            while (i4 < i) {
                vector.add(new PointF(this.random.nextInt(this.variance) + i4, this.random.nextInt(this.variance) + i3));
                i4 += this.cellSize;
            }
            i3 += this.cellSize;
        }
        return vector;
    }
}
