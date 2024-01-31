package com.cleanPhone.mobileCleaner.animate;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

public interface PaintGenerator {
    void configure(AttributeSet attributeSet, Context context);
    Paint generate();
}
