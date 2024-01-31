package com.cleanPhone.mobileCleaner.animate;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;


public abstract class Grav {
    public PointF drawPoint;
    public final Paint paint;
    public final PointF startPoint;

    public Grav(PointF pointF, Paint paint) {
        this.startPoint = pointF;
        this.paint = paint;
        this.drawPoint = new PointF(pointF.x, pointF.y);
    }

    public void draw(Canvas canvas) {
        draw(canvas, this.drawPoint);
    }

    public abstract void draw(Canvas canvas, PointF pointF);


    public Paint getPaint() {
        return this.paint;
    }

    public PointF getStartPoint() {
        return this.startPoint;
    }

    public float getX() {
        return this.drawPoint.x;
    }

    public float getY() {
        return this.drawPoint.y;
    }


    public void setX(float f) {
        this.drawPoint.x = f;
    }

    public void setY(float f) {
        this.drawPoint.y = f;
    }
}
