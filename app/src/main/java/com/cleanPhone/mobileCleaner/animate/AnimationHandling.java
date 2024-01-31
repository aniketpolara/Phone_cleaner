package com.cleanPhone.mobileCleaner.animate;

import android.animation.TimeInterpolator;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.lang.ref.WeakReference;

public class AnimationHandling extends Handler {
    private long mAnimationStartTime;
    private final WeakReference<CircleProgressView> mCircleViewWeakReference;
    private long mFrameStartTime;
    private TimeInterpolator mInterpolator;
    private double mLengthChangeAnimationDuration;
    private long mLengthChangeAnimationStartTime;
    private TimeInterpolator mLengthChangeInterpolator;
    private float mSpinningBarLengthStart;


    public static class AnonymousClass1 {

        public static final int[] f4766a;
        public static final int[] b;

        static {
            int[] iArr = new int[AnimationState.values().length];
            b = iArr;
            try {
                iArr[AnimationState.IDLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                b[AnimationState.SPINNING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                b[AnimationState.END_SPINNING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                b[AnimationState.END_SPINNING_START_ANIMATING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                b[AnimationState.ANIMATING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            int[] iArr2 = new int[AnimationText.values().length];
            f4766a = iArr2;
            try {
                iArr2[AnimationText.START_SPINNING.ordinal()] = 1;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f4766a[AnimationText.STOP_SPINNING.ordinal()] = 2;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f4766a[AnimationText.SET_VALUE.ordinal()] = 3;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f4766a[AnimationText.SET_VALUE_ANIMATED.ordinal()] = 4;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f4766a[AnimationText.TICK.ordinal()] = 5;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    public AnimationHandling(CircleProgressView circleProgressView) {
        super(circleProgressView.getContext().getMainLooper());
        this.mLengthChangeInterpolator = new DecelerateInterpolator();
        this.mInterpolator = new AccelerateDecelerateInterpolator();
        this.mFrameStartTime = 0L;
        this.mCircleViewWeakReference = new WeakReference<>(circleProgressView);
    }

    private boolean calcNextAnimationValue(CircleProgressView circleProgressView) {
        float currentTimeMillis = (float) ((System.currentTimeMillis() - this.mAnimationStartTime) / circleProgressView.m);
        if (currentTimeMillis > 1.0f) {
            currentTimeMillis = 1.0f;
        }
        float interpolation = this.mInterpolator.getInterpolation(currentTimeMillis);
        float f = circleProgressView.f4771d;
        circleProgressView.b = f + ((circleProgressView.f4770c - f) * interpolation);
        return currentTimeMillis >= 1.0f;
    }

    private void enterEndSpinning(CircleProgressView circleProgressView) {
        circleProgressView.q = AnimationState.END_SPINNING;
        initReduceAnimation(circleProgressView);
        AnimationStateChangedListener animationStateChangedListener = circleProgressView.r;
        if (animationStateChangedListener != null) {
            animationStateChangedListener.onAnimationStateChanged(circleProgressView.q);
        }
        sendEmptyMessageDelayed(AnimationText.TICK.ordinal(), circleProgressView.n - (SystemClock.uptimeMillis() - this.mFrameStartTime));
    }

    private void enterEndSpinningStartAnimating(CircleProgressView circleProgressView, Message message) {
        AnimationState animationState = AnimationState.END_SPINNING_START_ANIMATING;
        circleProgressView.q = animationState;
        AnimationStateChangedListener animationStateChangedListener = circleProgressView.r;
        if (animationStateChangedListener != null) {
            animationStateChangedListener.onAnimationStateChanged(animationState);
        }
        circleProgressView.f4771d = 0.0f;
        circleProgressView.f4770c = ((float[]) message.obj)[1];
        this.mLengthChangeAnimationStartTime = System.currentTimeMillis();
        this.mSpinningBarLengthStart = circleProgressView.h;
        sendEmptyMessageDelayed(AnimationText.TICK.ordinal(), circleProgressView.n - (SystemClock.uptimeMillis() - this.mFrameStartTime));
    }

    private void enterSetValueAnimated(Message message, CircleProgressView circleProgressView) {
        Object obj = message.obj;
        circleProgressView.f4771d = ((float[]) obj)[0];
        circleProgressView.f4770c = ((float[]) obj)[1];
        this.mAnimationStartTime = System.currentTimeMillis();
        AnimationState animationState = AnimationState.ANIMATING;
        circleProgressView.q = animationState;
        AnimationStateChangedListener animationStateChangedListener = circleProgressView.r;
        if (animationStateChangedListener != null) {
            animationStateChangedListener.onAnimationStateChanged(animationState);
        }
        sendEmptyMessageDelayed(AnimationText.TICK.ordinal(), circleProgressView.n - (SystemClock.uptimeMillis() - this.mFrameStartTime));
    }

    private void enterSpinning(CircleProgressView circleProgressView) {
        AnimationState animationState = AnimationState.SPINNING;
        circleProgressView.q = animationState;
        AnimationStateChangedListener animationStateChangedListener = circleProgressView.r;
        if (animationStateChangedListener != null) {
            animationStateChangedListener.onAnimationStateChanged(animationState);
        }
        float f = circleProgressView.e;
        float f2 = circleProgressView.b;
        circleProgressView.h = (360.0f / f) * f2;
        circleProgressView.j = (360.0f / f) * f2;
        this.mLengthChangeAnimationStartTime = System.currentTimeMillis();
        this.mSpinningBarLengthStart = circleProgressView.h;
        this.mLengthChangeAnimationDuration = (circleProgressView.i / circleProgressView.k) * circleProgressView.n * 2.0f;
        sendEmptyMessageDelayed(AnimationText.TICK.ordinal(), circleProgressView.n - (SystemClock.uptimeMillis() - this.mFrameStartTime));
    }

    private void initReduceAnimation(CircleProgressView circleProgressView) {
        this.mLengthChangeAnimationDuration = (circleProgressView.h / circleProgressView.k) * circleProgressView.n * 2.0f;
        this.mLengthChangeAnimationStartTime = System.currentTimeMillis();
        this.mSpinningBarLengthStart = circleProgressView.h;
    }

    private void setValue(Message message, CircleProgressView circleProgressView) {
        circleProgressView.f4771d = circleProgressView.f4770c;
        float f = ((float[]) message.obj)[0];
        circleProgressView.f4770c = f;
        circleProgressView.b = f;
        AnimationState animationState = AnimationState.IDLE;
        circleProgressView.q = animationState;
        AnimationStateChangedListener animationStateChangedListener = circleProgressView.r;
        if (animationStateChangedListener != null) {
            animationStateChangedListener.onAnimationStateChanged(animationState);
        }
        circleProgressView.invalidate();
    }

    @Override
    public void handleMessage(Message message) {
        CircleProgressView circleProgressView = this.mCircleViewWeakReference.get();
        if (circleProgressView == null) {
            return;
        }
        AnimationText animationText = AnimationText.values()[message.what];
        AnimationText animationText2 = AnimationText.TICK;
        if (animationText == animationText2) {
            removeMessages(animationText2.ordinal());
        }
        this.mFrameStartTime = SystemClock.uptimeMillis();
        int i = AnonymousClass1.b[circleProgressView.q.ordinal()];
        if (i == 1) {
            int i2 = AnonymousClass1.f4766a[animationText.ordinal()];
            if (i2 == 1) {
                enterSpinning(circleProgressView);
            } else if (i2 == 3) {
                setValue(message, circleProgressView);
            } else if (i2 == 4) {
                enterSetValueAnimated(message, circleProgressView);
            } else if (i2 != 5) {
            } else {
                removeMessages(animationText2.ordinal());
            }
        } else if (i == 2) {
            int i3 = AnonymousClass1.f4766a[animationText.ordinal()];
            if (i3 == 2) {
                enterEndSpinning(circleProgressView);
            } else if (i3 == 3) {
                setValue(message, circleProgressView);
            } else if (i3 == 4) {
                enterEndSpinningStartAnimating(circleProgressView, message);
            } else if (i3 != 5) {
            } else {
                float f = circleProgressView.h - circleProgressView.i;
                float currentTimeMillis = (float) ((System.currentTimeMillis() - this.mLengthChangeAnimationStartTime) / this.mLengthChangeAnimationDuration);
                if (currentTimeMillis > 1.0f) {
                    currentTimeMillis = 1.0f;
                }
                float interpolation = this.mLengthChangeInterpolator.getInterpolation(currentTimeMillis);
                if (Math.abs(f) < 1.0f) {
                    circleProgressView.h = circleProgressView.i;
                } else {
                    float f2 = circleProgressView.h;
                    float f3 = circleProgressView.i;
                    if (f2 < f3) {
                        float f4 = this.mSpinningBarLengthStart;
                        circleProgressView.h = f4 + ((f3 - f4) * interpolation);
                    } else {
                        float f5 = this.mSpinningBarLengthStart;
                        circleProgressView.h = f5 - ((f5 - f3) * interpolation);
                    }
                }
                float f6 = circleProgressView.j + circleProgressView.k;
                circleProgressView.j = f6;
                if (f6 > 360.0f) {
                    circleProgressView.j = 0.0f;
                }
                sendEmptyMessageDelayed(animationText2.ordinal(), circleProgressView.n - (SystemClock.uptimeMillis() - this.mFrameStartTime));
                circleProgressView.invalidate();
            }
        } else if (i == 3) {
            int i4 = AnonymousClass1.f4766a[animationText.ordinal()];
            if (i4 == 1) {
                AnimationState animationState = AnimationState.SPINNING;
                circleProgressView.q = animationState;
                AnimationStateChangedListener animationStateChangedListener = circleProgressView.r;
                if (animationStateChangedListener != null) {
                    animationStateChangedListener.onAnimationStateChanged(animationState);
                }
                sendEmptyMessageDelayed(animationText2.ordinal(), circleProgressView.n - (SystemClock.uptimeMillis() - this.mFrameStartTime));
            } else if (i4 == 3) {
                setValue(message, circleProgressView);
            } else if (i4 == 4) {
                enterEndSpinningStartAnimating(circleProgressView, message);
            } else if (i4 != 5) {
            } else {
                float currentTimeMillis2 = (float) ((System.currentTimeMillis() - this.mLengthChangeAnimationStartTime) / this.mLengthChangeAnimationDuration);
                if (currentTimeMillis2 > 1.0f) {
                    currentTimeMillis2 = 1.0f;
                }
                float interpolation2 = this.mSpinningBarLengthStart * (1.0f - this.mLengthChangeInterpolator.getInterpolation(currentTimeMillis2));
                circleProgressView.h = interpolation2;
                circleProgressView.j += circleProgressView.k;
                if (interpolation2 < 0.01f) {
                    AnimationState animationState2 = AnimationState.IDLE;
                    circleProgressView.q = animationState2;
                    AnimationStateChangedListener animationStateChangedListener2 = circleProgressView.r;
                    if (animationStateChangedListener2 != null) {
                        animationStateChangedListener2.onAnimationStateChanged(animationState2);
                    }
                }
                sendEmptyMessageDelayed(animationText2.ordinal(), circleProgressView.n - (SystemClock.uptimeMillis() - this.mFrameStartTime));
                circleProgressView.invalidate();
            }
        } else if (i != 4) {
            if (i != 5) {
                return;
            }
            int i5 = AnonymousClass1.f4766a[animationText.ordinal()];
            if (i5 == 1) {
                enterSpinning(circleProgressView);
            } else if (i5 == 3) {
                setValue(message, circleProgressView);
            } else if (i5 == 4) {
                this.mAnimationStartTime = System.currentTimeMillis();
                circleProgressView.f4771d = circleProgressView.b;
                circleProgressView.f4770c = ((float[]) message.obj)[1];
            } else if (i5 != 5) {
            } else {
                if (calcNextAnimationValue(circleProgressView)) {
                    AnimationState animationState3 = AnimationState.IDLE;
                    circleProgressView.q = animationState3;
                    AnimationStateChangedListener animationStateChangedListener3 = circleProgressView.r;
                    if (animationStateChangedListener3 != null) {
                        animationStateChangedListener3.onAnimationStateChanged(animationState3);
                    }
                    circleProgressView.b = circleProgressView.f4770c;
                }
                sendEmptyMessageDelayed(animationText2.ordinal(), circleProgressView.n - (SystemClock.uptimeMillis() - this.mFrameStartTime));
                circleProgressView.invalidate();
            }
        } else {
            int i6 = AnonymousClass1.f4766a[animationText.ordinal()];
            if (i6 == 1) {
                circleProgressView.o = false;
                enterSpinning(circleProgressView);
            } else if (i6 == 3) {
                circleProgressView.o = false;
                setValue(message, circleProgressView);
            } else if (i6 == 4) {
                circleProgressView.f4771d = 0.0f;
                circleProgressView.f4770c = ((float[]) message.obj)[1];
                sendEmptyMessageDelayed(animationText2.ordinal(), circleProgressView.n - (SystemClock.uptimeMillis() - this.mFrameStartTime));
            } else if (i6 != 5) {
            } else {
                if (circleProgressView.h > circleProgressView.i && !circleProgressView.o) {
                    float currentTimeMillis3 = (float) ((System.currentTimeMillis() - this.mLengthChangeAnimationStartTime) / this.mLengthChangeAnimationDuration);
                    if (currentTimeMillis3 > 1.0f) {
                        currentTimeMillis3 = 1.0f;
                    }
                    circleProgressView.h = this.mSpinningBarLengthStart * (1.0f - this.mLengthChangeInterpolator.getInterpolation(currentTimeMillis3));
                }
                float f7 = circleProgressView.j + circleProgressView.k;
                circleProgressView.j = f7;
                if (f7 > 360.0f && !circleProgressView.o) {
                    this.mAnimationStartTime = System.currentTimeMillis();
                    circleProgressView.o = true;
                    initReduceAnimation(circleProgressView);
                    AnimationStateChangedListener animationStateChangedListener4 = circleProgressView.r;
                    if (animationStateChangedListener4 != null) {
                        animationStateChangedListener4.onAnimationStateChanged(AnimationState.START_ANIMATING_AFTER_SPINNING);
                    }
                }
                if (circleProgressView.o) {
                    circleProgressView.j = 360.0f;
                    circleProgressView.h -= circleProgressView.k;
                    calcNextAnimationValue(circleProgressView);
                    float currentTimeMillis4 = (float) ((System.currentTimeMillis() - this.mLengthChangeAnimationStartTime) / this.mLengthChangeAnimationDuration);
                    if (currentTimeMillis4 > 1.0f) {
                        currentTimeMillis4 = 1.0f;
                    }
                    circleProgressView.h = this.mSpinningBarLengthStart * (1.0f - this.mLengthChangeInterpolator.getInterpolation(currentTimeMillis4));
                }
                if (circleProgressView.h < 0.1d) {
                    AnimationState animationState4 = AnimationState.ANIMATING;
                    circleProgressView.q = animationState4;
                    AnimationStateChangedListener animationStateChangedListener5 = circleProgressView.r;
                    if (animationStateChangedListener5 != null) {
                        animationStateChangedListener5.onAnimationStateChanged(animationState4);
                    }
                    circleProgressView.invalidate();
                    circleProgressView.o = false;
                    circleProgressView.h = circleProgressView.i;
                } else {
                    circleProgressView.invalidate();
                }
                sendEmptyMessageDelayed(animationText2.ordinal(), circleProgressView.n - (SystemClock.uptimeMillis() - this.mFrameStartTime));
            }
        }
    }

}
