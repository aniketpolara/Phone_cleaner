package com.cleanPhone.mobileCleaner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("AppCompatCustomView")
public class SpinImgView extends ImageView implements WheelRotation.RotationListener {
    private static final float ANGLE = 360.0f;
    private float angle;
    @ColorInt
    private int[] colors;
    private Paint itemPaint;
    private List items;
    private boolean onRotation;
    private OnRotationListener onRotationListener;
    private boolean onRotationListenerTicket;
    private Point[] points;
    private WheelRotation wheelRotation;

    public interface OnRotationListener<T> {
        void onRotation();
        void onStopRotation(String str);
    }

    public SpinImgView(Context context) {
        super(context);
        this.angle = 0.0f;
    }

    private void initPoints() {
        List list = this.items;
        if (list == null || list.isEmpty()) {
            return;
        }
        this.points = new Point[this.items.size()];
    }

    public List getItems() {
        return this.items;
    }

    public OnRotationListener getOnRotationListener() {
        return this.onRotationListener;
    }

    @Override
    public void onRotate(float f) {
        rotate(f);
    }

    @Override
    public void onStop() {
        this.onRotation = false;
    }

    public void rotate(float f) {
        OnRotationListener onRotationListener;
        float f2 = this.angle + f;
        this.angle = f2;
        this.angle = f2 % ANGLE;
        invalidate();
        if (!this.onRotationListenerTicket || f == 0.0f || (onRotationListener = this.onRotationListener) == null) {
            return;
        }
        onRotationListener.onRotation();
        this.onRotationListenerTicket = false;
    }

    public void setItems(List list) {
        this.items = list;
        initPoints();
        invalidate();
    }

    public void setOnRotationListener(OnRotationListener onRotationListener) {
        this.onRotationListener = onRotationListener;
    }

    public SpinImgView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.angle = 0.0f;
    }

    public void setItems(@ArrayRes int i) {
        if (i == 0) {
            return;
        }
        String[] stringArray = getResources().getStringArray(i);
        ArrayList arrayList = new ArrayList();
        for (String str : stringArray) {
            arrayList.add(str);
        }
        setItems(arrayList);
    }

    public SpinImgView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.angle = 0.0f;
    }

    public void rotate(float f, long j, long j2) {
        if (f == 0.0f) {
            return;
        }
        this.onRotationListenerTicket = true;
        this.onRotation = true;
        WheelRotation wheelRotation = this.wheelRotation;
        if (wheelRotation != null) {
            wheelRotation.cancel();
        }
        WheelRotation listener = WheelRotation.init(j, j2).setMaxAngle(f).setListener(this);
        this.wheelRotation = listener;
        listener.start();
    }
}
