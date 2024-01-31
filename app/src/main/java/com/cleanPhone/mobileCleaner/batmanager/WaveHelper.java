package com.cleanPhone.mobileCleaner.batmanager;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.cleanPhone.mobileCleaner.waveview.WaveView;

import java.util.ArrayList;

public class WaveHelper {
    private String TAG = WaveHelper.class.getSimpleName();
    private Context context;
    private AnimatorSet mAnimatorSet;
    private WaveView mWaveView;

    public WaveHelper(WaveView waveView, Context context) {
        this.context = context;
        this.mWaveView = waveView;
        initAnimation();
    }

    private void initAnimation() {
        ArrayList arrayList = new ArrayList();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mWaveView, "waveShiftRatio", 0.0f, 1.0f);
        ofFloat.setRepeatCount(-1);
        ofFloat.setDuration(1200L);
        ofFloat.setInterpolator(new LinearInterpolator());
        arrayList.add(ofFloat);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mWaveView, "waterLevelRatio", 0.0f, 1.0f);
        ofFloat2.setDuration(5000L);
        ofFloat2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animator) {
                Log.w(WaveHelper.this.TAG, "onAnimationCancel called");
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.w(WaveHelper.this.TAG, "onAnimationEnd called");
                if (WaveHelper.this.context instanceof BatterySaverActivity) {
                    ((BatterySaverActivity) WaveHelper.this.context).finishBatteryAnimation();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                Log.w(WaveHelper.this.TAG, "onAnimationRepeat called");
            }

            @Override
            public void onAnimationStart(Animator animator) {
                Log.w(WaveHelper.this.TAG, "onAnimationStart called");
            }
        });
        ofFloat2.setInterpolator(new DecelerateInterpolator());
        arrayList.add(ofFloat2);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.mWaveView, "amplitudeRatio", 1.0E-4f, 0.06f);
        ofFloat3.setRepeatCount(-1);
        ofFloat3.setRepeatMode(2);
        ofFloat3.setDuration(1000L);
        ofFloat3.setInterpolator(new LinearInterpolator());
        arrayList.add(ofFloat3);
        AnimatorSet animatorSet = new AnimatorSet();
        this.mAnimatorSet = animatorSet;
        animatorSet.playTogether(arrayList);
    }

    public void cancel() {
        AnimatorSet animatorSet = this.mAnimatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.mAnimatorSet.end();
            this.mAnimatorSet.removeAllListeners();
        }
    }

    public void start() {
        this.mWaveView.setShowWave(true);
        AnimatorSet animatorSet = this.mAnimatorSet;
        if (animatorSet != null) {
            animatorSet.start();
        }
    }
}
