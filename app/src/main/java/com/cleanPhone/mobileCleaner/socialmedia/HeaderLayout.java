package com.cleanPhone.mobileCleaner.socialmedia;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class HeaderLayout extends FrameLayout {
    private int mHeaderWidth;

    public HeaderLayout(Context context) {
        super(context);
        this.mHeaderWidth = 1;
    }

    @Override
    public void onMeasure(int i, int i2) {
        int i3 = this.mHeaderWidth;
        if (i3 != 1) {
            i = MeasureSpec.makeMeasureSpec(i3, MeasureSpec.getMode(i));
        }
        super.onMeasure(i, i2);
    }

    public void setHeaderWidth(int i) {
        this.mHeaderWidth = i;
    }

    public HeaderLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHeaderWidth = 1;
    }

    public HeaderLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHeaderWidth = 1;
    }
}
