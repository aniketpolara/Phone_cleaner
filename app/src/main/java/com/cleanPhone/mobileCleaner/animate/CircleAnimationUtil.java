package com.cleanPhone.mobileCleaner.animate;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.cleanPhone.mobileCleaner.utility.GlobalData;

import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class CircleAnimationUtil {
    private static final int DEFAULT_DURATION = 800;
    private float destX;
    private float destY;
    private Animator.AnimatorListener mAnimationListener;
    private Bitmap mBitmap;
    private WeakReference<Activity> mContextReference;
    private View mDest;
    private CircleImageView mImageView;
    private View mTarget;
    private float originX;
    private float originY;
    private int mCircleDuration = DEFAULT_DURATION;
    private int mMoveDuration = DEFAULT_DURATION;
    private int mBorderWidth = 0;
    private int mBorderColor = 0;
    private float scaleFactor = 0.0f;

    private Bitmap drawViewToBitmap(View view, int i, int i2) throws Exception {
        BitmapDrawable bitmapDrawable = new BitmapDrawable();
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        bitmapDrawable.setBounds(new Rect(0, 0, i, i2));
        bitmapDrawable.draw(canvas);
        view.draw(canvas);
        return createBitmap;
    }

    private AnimatorSet getAvatarRevealAnimator() {

        final float max = Math.max(this.destX, this.destY) / 0.6f;
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mImageView, "drawableRadius", Math.max(this.originX, this.originY), 0.05f * max, 1.9f * max, max);
        ofFloat.setInterpolator(new OvershootInterpolator(-10.0f));
        this.scaleFactor = Math.max((this.destY * 1.0f) / this.originY, (this.destX * 1.0f) / this.originX);
        if (GlobalData.dontAnimate) {
            this.scaleFactor = 0.0f;
        }
        CircleImageView circleImageView = this.mImageView;
        Property property = View.SCALE_Y;
        float f = this.scaleFactor;
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(circleImageView, property, 1.0f, 1.0f, f, f);
        CircleImageView circleImageView2 = this.mImageView;
        Property property2 = View.SCALE_X;
        float f2 = this.scaleFactor;
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(circleImageView2, property2, 1.0f, 1.0f, f2, f2);
        AnimatorSet animatorSet = new AnimatorSet();
        if (GlobalData.dontAnimate) {
            animatorSet.setDuration(0L);
        } else {
            animatorSet.setDuration(this.mCircleDuration);
        }
        animatorSet.playTogether(ofFloat3, ofFloat2, ofFloat);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                int[] iArr = new int[2];
                int[] iArr2 = new int[2];
                CircleAnimationUtil.this.mImageView.getLocationOnScreen(iArr);
                CircleAnimationUtil.this.mDest.getLocationOnScreen(iArr2);
                float y = CircleAnimationUtil.this.mImageView.getY();
                float x = CircleAnimationUtil.this.mImageView.getX();
                ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(CircleAnimationUtil.this.mImageView, View.X, x, ((x + iArr2[0]) - (iArr[0] + (((CircleAnimationUtil.this.originX * CircleAnimationUtil.this.scaleFactor) - ((max * 2.0f) * CircleAnimationUtil.this.scaleFactor)) / 2.0f))) + ((CircleAnimationUtil.this.destX * 0.6f) - (CircleAnimationUtil.this.scaleFactor * max)));
                ofFloat4.setInterpolator(new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float f3) {
                        return (float) ((-Math.pow(f3 - 1.0f, 2.0d)) + 1.0d);
                    }
                });
                ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(CircleAnimationUtil.this.mImageView, View.Y, y, ((y + iArr2[1]) - (iArr[1] + (((CircleAnimationUtil.this.originY * CircleAnimationUtil.this.scaleFactor) - ((max * 2.0f) * CircleAnimationUtil.this.scaleFactor)) / 2.0f))) + ((CircleAnimationUtil.this.destY * 0.3f) - (CircleAnimationUtil.this.scaleFactor * max)));
                ofFloat5.setInterpolator(new AnticipateInterpolator(3.0f));
                AnimatorSet animatorSet2 = new AnimatorSet();
                animatorSet2.playTogether(ofFloat4, ofFloat5);
                animatorSet2.setDuration(CircleAnimationUtil.this.mMoveDuration);
                AnimatorSet animatorSet3 = new AnimatorSet();
                ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(CircleAnimationUtil.this.mImageView, View.SCALE_Y, CircleAnimationUtil.this.scaleFactor, 0.0f);
                ofFloat6.setInterpolator(new AnticipateInterpolator(2.0f));
                ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat(CircleAnimationUtil.this.mImageView, View.SCALE_X, CircleAnimationUtil.this.scaleFactor, 0.0f);
                ofFloat7.setInterpolator(new AnticipateInterpolator(2.0f));
                if (GlobalData.dontAnimate) {
                    animatorSet3.setDuration(10L);
                } else {
                    animatorSet3.setDuration(1000L);
                }
                animatorSet3.playTogether(ofFloat7, ofFloat6);
                AnimatorSet animatorSet4 = new AnimatorSet();
                animatorSet4.playTogether(animatorSet2, animatorSet3);
                animatorSet4.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationCancel(Animator animator2) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator2) {
                        if (CircleAnimationUtil.this.mAnimationListener != null) {
                            CircleAnimationUtil.this.mAnimationListener.onAnimationEnd(animator2);
                        }
                        CircleAnimationUtil.this.reset();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator2) {
                    }

                    @Override
                    public void onAnimationStart(Animator animator2) {
                    }
                });
                animatorSet4.start();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }

            @Override
            public void onAnimationStart(Animator animator) {
                if (CircleAnimationUtil.this.mAnimationListener != null) {
                    CircleAnimationUtil.this.mAnimationListener.onAnimationStart(animator);
                }
            }
        });
        return animatorSet;
    }

    private boolean prepare() {
        if (this.mContextReference.get() != null) {
            ViewGroup viewGroup = (ViewGroup) this.mContextReference.get().getWindow().getDecorView();
            try {
                View view = this.mTarget;
                this.mBitmap = drawViewToBitmap(view, view.getWidth(), this.mTarget.getHeight());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.mImageView == null) {
                this.mImageView = new CircleImageView(this.mContextReference.get());
            }
            this.mImageView.setImageBitmap(this.mBitmap);
            this.mImageView.setBorderWidth(this.mBorderWidth);
            this.mImageView.setBorderColor(this.mBorderColor);
            int[] iArr = new int[2];
            this.mTarget.getLocationOnScreen(iArr);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(this.mTarget.getWidth(), this.mTarget.getHeight());
            layoutParams.setMargins(iArr[0], iArr[1], 0, 0);
            if (this.mImageView.getParent() == null) {
                viewGroup.addView(this.mImageView, layoutParams);
            }
        }
        return true;
    }


    public void reset() {
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            bitmap.recycle();
        }
        if (this.mImageView.getParent() != null) {
            ((ViewGroup) this.mImageView.getParent()).removeView(this.mImageView);
        }
        this.mBitmap = null;
        this.mImageView = null;
    }

    private CircleAnimationUtil setDestRect(float f, float f2) {
        this.destX = f;
        this.destY = f2;
        return this;
    }

    private CircleAnimationUtil setOriginRect(float f, float f2) {
        this.originX = f;
        this.originY = f2;
        return this;
    }

    public CircleAnimationUtil attachActivity(Activity activity) {
        this.mContextReference = new WeakReference<>(activity);
        return this;
    }

    public CircleAnimationUtil setDestView(View view) {
        this.mDest = view;
        setDestRect(view.getWidth(), this.mDest.getWidth());
        return this;
    }


    public CircleAnimationUtil setTargetView(View view) {
        this.mTarget = view;
        setOriginRect(view.getWidth(), this.mTarget.getHeight());
        return this;
    }

    public void startAnimation() {
        if (prepare()) {
            getAvatarRevealAnimator().start();
        }
    }
}
