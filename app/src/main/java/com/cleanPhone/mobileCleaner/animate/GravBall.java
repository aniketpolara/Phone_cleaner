package com.cleanPhone.mobileCleaner.animate;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class GravBall extends Grav {
    private int radius;

    public GravBall(PointF pointF, Paint paint, int i) {
        super(pointF, paint);
        this.radius = i;
    }

    @Override
    public void draw(Canvas canvas, PointF pointF) {
        canvas.drawCircle(pointF.x, pointF.y, this.radius, this.paint);
    }

    public void setRadius(int i) {
        this.radius = i;
    }
}
