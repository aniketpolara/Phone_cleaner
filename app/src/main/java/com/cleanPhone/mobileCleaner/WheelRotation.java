package com.cleanPhone.mobileCleaner;

import android.os.CountDownTimer;

public class WheelRotation extends CountDownTimer {
    private static final float SLOW_FACTOR = 0.6666667f;
    private final float ROTATE_SCALE_FACTOR;
    private float angle;
    private long duration;
    private float maxAngle;
    private RotationListener rotationListener;
    private long thresholdSlow;

    public interface RotationListener {
        void onRotate(float f);

        void onStop();
    }

    public WheelRotation(long j, long j2) {
        super(j, j2);
        this.ROTATE_SCALE_FACTOR = 2.0f;
        this.angle = 1.0f;
        this.thresholdSlow = (long) (((float) j) * SLOW_FACTOR);
        this.duration = j;
    }

    public static WheelRotation init(long j, long j2) {
        return new WheelRotation(j, j2);
    }

    @Override
    public void onFinish() {
        RotationListener rotationListener = this.rotationListener;
        if (rotationListener == null) {
            return;
        }
        rotationListener.onStop();
    }

    @Override
    public void onTick(long j) {
        RotationListener rotationListener = this.rotationListener;
        if (rotationListener == null) {
            return;
        }
        if (j <= this.thresholdSlow) {
            float f = this.maxAngle * (((float) j) / ((float) this.duration));
            this.angle = f;
            rotationListener.onRotate(f);
            return;
        }
        float f2 = this.angle;
        if (f2 < this.maxAngle) {
            rotationListener.onRotate(f2);
            float f3 = this.angle * 2.0f;
            this.angle = f3;
            float f4 = this.maxAngle;
            if (f3 > f4) {
                this.angle = f4;
                return;
            }
            return;
        }
        rotationListener.onRotate(f2);
    }

    public WheelRotation setListener(RotationListener rotationListener) {
        this.rotationListener = rotationListener;
        return this;
    }

    public WheelRotation setMaxAngle(float f) {
        this.maxAngle = f;
        return this;
    }
}
