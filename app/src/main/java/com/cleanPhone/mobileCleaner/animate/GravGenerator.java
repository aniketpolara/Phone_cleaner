package com.cleanPhone.mobileCleaner.animate;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

public interface GravGenerator {
    void configure(AttributeSet attributeSet, Context context);

    Grav generate(PointF pointF, Paint paint);
}
