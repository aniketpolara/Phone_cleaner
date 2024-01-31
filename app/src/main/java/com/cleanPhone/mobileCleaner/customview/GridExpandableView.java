package com.cleanPhone.mobileCleaner.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import androidx.core.view.ViewCompat;

public class GridExpandableView extends GridView {
    public boolean f4881a;

    public GridExpandableView(Context context) {
        super(context);
        this.f4881a = false;
    }

    public boolean isExpanded() {
        return this.f4881a;
    }

    @Override
    public void onMeasure(int i, int i2) {
        if (isExpanded()) {
            super.onMeasure(i, MeasureSpec.makeMeasureSpec(ViewCompat.MEASURED_SIZE_MASK, MeasureSpec.AT_MOST));
            getLayoutParams().height = getMeasuredHeight();
            return;
        }
        super.onMeasure(i, i2);
    }

    public void setExpanded(boolean z) {
        this.f4881a = z;
    }

    public GridExpandableView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f4881a = false;
    }

    public GridExpandableView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f4881a = false;
    }
}
