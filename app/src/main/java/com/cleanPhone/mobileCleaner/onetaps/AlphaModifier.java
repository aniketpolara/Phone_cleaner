package com.cleanPhone.mobileCleaner.onetaps;

import android.view.animation.Interpolator;


public class AlphaModifier implements ParticleModifier {
    private float mDuration;
    private long mEndTime;
    private int mFinalValue;
    private int mInitialValue;
    private Interpolator mInterpolator;
    private long mStartTime;
    private float mValueIncrement;

    public AlphaModifier(int i, int i2, long j, long j2, Interpolator interpolator) {
        this.mInitialValue = i;
        this.mFinalValue = i2;
        this.mStartTime = j;
        this.mEndTime = j2;
        this.mDuration = (float) (j2 - j);
        this.mValueIncrement = i2 - i;
        this.mInterpolator = interpolator;
    }

    @Override
    public void apply(Particle particle, long j) {
        long j2 = this.mStartTime;
        if (j < j2) {
            particle.mAlpha = this.mInitialValue;
        } else if (j > this.mEndTime) {
            particle.mAlpha = this.mFinalValue;
        } else {
            particle.mAlpha = (int) (this.mInitialValue + (this.mValueIncrement * this.mInterpolator.getInterpolation((((float) (j - j2)) * 1.0f) / this.mDuration)));
        }
    }

}
