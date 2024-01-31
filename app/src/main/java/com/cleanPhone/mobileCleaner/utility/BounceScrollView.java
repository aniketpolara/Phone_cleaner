package com.cleanPhone.mobileCleaner.utility;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

public class BounceScrollView extends ScrollView {
    private View inner;
    private boolean isCount;
    private Rect normal;
    private float y;

    public BounceScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.normal = new Rect();
        this.isCount = false;
    }

    public void animation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, this.inner.getTop(), this.normal.top);
        translateAnimation.setDuration(400L);
        this.inner.startAnimation(translateAnimation);
        View view = this.inner;
        Rect rect = this.normal;
        view.layout(rect.left, rect.top, rect.right, rect.bottom);
        this.normal.setEmpty();
    }

    public void commOnTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 1) {
            if (isNeedAnimation()) {
                animation();
                this.isCount = false;
            }
        } else if (action != 2) {
        } else {
            float f = this.y;
            float y = motionEvent.getY();
            int i = this.isCount ? (int) (f - y) : 0;
            this.y = y;
            if (isNeedMove()) {
                if (this.normal.isEmpty()) {
                    this.normal.set(this.inner.getLeft(), this.inner.getTop(), this.inner.getRight(), this.inner.getBottom());
                }
                View view = this.inner;
                int i2 = i / 2;
                view.layout(view.getLeft(), this.inner.getTop() - i2, this.inner.getRight(), this.inner.getBottom() - i2);
            }
            this.isCount = true;
        }
    }

    public boolean isNeedAnimation() {
        return !this.normal.isEmpty();
    }

    public boolean isNeedMove() {
        int measuredHeight = this.inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        return scrollY == 0 || scrollY == measuredHeight;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            this.inner = getChildAt(0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.inner != null) {
            commOnTouchEvent(motionEvent);
        }
        return super.onTouchEvent(motionEvent);
    }
}
