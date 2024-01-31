package com.cleanPhone.mobileCleaner.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;


public class MyGridView extends GridView {
    public MyGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, MeasureSpec.makeMeasureSpec(536870911, MeasureSpec.AT_MOST));
    }
}
