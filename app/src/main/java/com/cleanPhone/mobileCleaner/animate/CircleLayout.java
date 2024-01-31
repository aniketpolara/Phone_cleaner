package com.cleanPhone.mobileCleaner.animate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


public class CircleLayout extends FrameLayout {
    private boolean isClipOutlines;
    private float mCenterX;
    private float mCenterY;
    private Path mCirclelPath;
    private float mRadius;

    public CircleLayout(Context context) {
        this(context, null);
    }

    @Override
    public boolean drawChild(Canvas canvas, View view, long j) {
        if (!this.isClipOutlines) {
            return super.drawChild(canvas, view, j);
        }
        int save = canvas.save();
        this.mCirclelPath.reset();
        this.mCirclelPath.addCircle(this.mCenterX, this.mCenterY, this.mRadius, Path.Direction.CW);
        canvas.clipPath(this.mCirclelPath);
        boolean drawChild = super.drawChild(canvas, view, j);
        canvas.restoreToCount(save);
        return drawChild;
    }


    public CircleLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircleLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCirclelPath = new Path();
    }
}
