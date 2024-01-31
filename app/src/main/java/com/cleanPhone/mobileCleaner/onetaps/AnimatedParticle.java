package com.cleanPhone.mobileCleaner.onetaps;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;


public class AnimatedParticle extends Particle {
    private AnimationDrawable mAnimationDrawable;
    private int mTotalTime;

    public AnimatedParticle(AnimationDrawable animationDrawable) {
        this.mAnimationDrawable = animationDrawable;
        this.mImage = ((BitmapDrawable) animationDrawable.getFrame(0)).getBitmap();
        this.mTotalTime = 0;
        for (int i = 0; i < this.mAnimationDrawable.getNumberOfFrames(); i++) {
            this.mTotalTime += this.mAnimationDrawable.getDuration(i);
        }
    }

    @Override
    public boolean update(long j) {
        boolean update = super.update(j);
        if (update) {
            long j2 = 0;
            long j3 = j - this.mStartingMilisecond;
            int i = 0;
            if (j3 > this.mTotalTime) {
                if (this.mAnimationDrawable.isOneShot()) {
                    return false;
                }
                j3 %= this.mTotalTime;
            }
            while (true) {
                if (i >= this.mAnimationDrawable.getNumberOfFrames()) {
                    break;
                }
                j2 += this.mAnimationDrawable.getDuration(i);
                if (j2 > j3) {
                    this.mImage = ((BitmapDrawable) this.mAnimationDrawable.getFrame(i)).getBitmap();
                    break;
                }
                i++;
            }
        }
        return update;
    }
}
