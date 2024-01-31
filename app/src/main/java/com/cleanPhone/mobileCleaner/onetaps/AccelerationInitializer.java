package com.cleanPhone.mobileCleaner.onetaps;

import java.util.Random;


public class AccelerationInitializer implements ParticleInitializer {
    private int mMaxAngle;
    private float mMaxValue;
    private int mMinAngle;
    private float mMinValue;

    public AccelerationInitializer(float f, float f2, int i, int i2) {
        this.mMinValue = f;
        this.mMaxValue = f2;
        this.mMinAngle = i;
        this.mMaxAngle = i2;
    }

    @Override
    public void initParticle(Particle particle, Random random) {
        int i = this.mMinAngle;
        float f = i;
        int i2 = this.mMaxAngle;
        if (i2 != i) {
            f = random.nextInt(i2 - i) + this.mMinAngle;
        }
        float nextFloat = random.nextFloat();
        float f2 = this.mMaxValue;
        float f3 = this.mMinValue;
        double d2 = (nextFloat * (f2 - f3)) + f3;
        double d3 = (float) ((f * 3.141592653589793d) / 180.0d);
        particle.mAccelerationX = (float) (Math.cos(d3) * d2);
        particle.mAccelerationY = (float) (d2 * Math.sin(d3));
    }
}
