package com.cleanPhone.mobileCleaner.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

public class AutoScrollListView extends ListView {
    private static final float PREFERRED_SELECTION_OFFSET_FROM_TOP = 0.33f;
    private int mRequestedScrollPosition;
    private boolean mSmoothScrollRequested;

    public AutoScrollListView(Context context) {
        super(context);
        this.mRequestedScrollPosition = -1;
    }

    @Override
    @TargetApi(11)
    public void layoutChildren() {
        super.layoutChildren();
        int i = this.mRequestedScrollPosition;
        if (i == -1) {
            return;
        }
        this.mRequestedScrollPosition = -1;
        int firstVisiblePosition = getFirstVisiblePosition() + 1;
        int lastVisiblePosition = getLastVisiblePosition();
        if (i < firstVisiblePosition || i > lastVisiblePosition) {
            int height = (int) (getHeight() * PREFERRED_SELECTION_OFFSET_FROM_TOP);
            if (!this.mSmoothScrollRequested) {
                setSelectionFromTop(i, height);
                super.layoutChildren();
                return;
            }
            int i2 = (lastVisiblePosition - firstVisiblePosition) * 2;
            if (i < firstVisiblePosition) {
                int i3 = i2 + i;
                if (i3 >= getCount()) {
                    i3 = getCount() - 1;
                }
                if (i3 < firstVisiblePosition) {
                    setSelection(i3);
                    super.layoutChildren();
                }
            } else {
                int i4 = i - i2;
                if (i4 < 0) {
                    i4 = 0;
                }
                if (i4 > lastVisiblePosition) {
                    setSelection(i4);
                    super.layoutChildren();
                }
            }
            if (Build.VERSION.SDK_INT >= 11) {
                smoothScrollToPositionFromTop(i, height);
            } else {
                smoothScrollToPosition(i);
            }
        }
    }

    public void requestPositionToScreen(int i, boolean z) {
        this.mRequestedScrollPosition = i;
        this.mSmoothScrollRequested = z;
        requestLayout();
    }

    public AutoScrollListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRequestedScrollPosition = -1;
    }

    public AutoScrollListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRequestedScrollPosition = -1;
    }
}
