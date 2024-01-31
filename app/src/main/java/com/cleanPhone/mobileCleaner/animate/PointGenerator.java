package com.cleanPhone.mobileCleaner.animate;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;

import java.util.Vector;

public interface PointGenerator {
    void configure(AttributeSet attributeSet, Context context);
    Vector<PointF> generatePoints(int i, int i2);
}
