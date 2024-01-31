package com.cleanPhone.mobileCleaner.socialmedia;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class FillerView extends LinearLayout {
    private View mMeasureTarget;

    public FillerView(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(int i, int i2) {
        View view = this.mMeasureTarget;
        if (view != null) {
            i2 = MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), MeasureSpec.EXACTLY);
        }
        super.onMeasure(i, i2);
    }

    public void setMeasureTarget(View view) {
        this.mMeasureTarget = view;
    }

    public FillerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public FillerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
