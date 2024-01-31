package com.cleanPhone.mobileCleaner.animate;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;


public abstract class GravAnimatorGenerator<T extends Grav> {

    public class AnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private final UpdageGravListener UpdageGravListener;
        private final Grav grav;

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.UpdageGravListener.onUpdate(this.grav, valueAnimator);
        }

        private AnimatorUpdateListener(Grav grav, UpdageGravListener updageGravListener) {
            this.grav = grav;
            this.UpdageGravListener = updageGravListener;
        }
    }

    public interface UpdageGravListener<T> {
        void onUpdate(T t, ValueAnimator valueAnimator);
    }

    public abstract void configure(AttributeSet attributeSet, Context context);

    public abstract UpdageGravListener<T> createUpdateListener();

    public abstract ValueAnimator createValueAnimator(T t, int i, int i2);

    public ValueAnimator generateGravAnimator(T t, int i, int i2) {
        ValueAnimator createValueAnimator = createValueAnimator(t, i, i2);
        createValueAnimator.addUpdateListener(new AnimatorUpdateListener(t, createUpdateListener()));
        return createValueAnimator;
    }
}
