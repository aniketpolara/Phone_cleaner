package com.cleanPhone.mobileCleaner.animate;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.Random;

public class RandomColorGenerator implements PaintGenerator {
    private Random random = new Random();

    @Override
    public void configure(AttributeSet attributeSet, Context context) {
    }

    @Override
    public Paint generate() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setARGB(255, this.random.nextInt(256), this.random.nextInt(256), this.random.nextInt(256));
        return paint;
    }
}
