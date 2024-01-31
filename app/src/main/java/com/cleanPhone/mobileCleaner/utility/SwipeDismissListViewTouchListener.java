package com.cleanPhone.mobileCleaner.utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SwipeDismissListViewTouchListener implements View.OnTouchListener {
    private long mAnimationTime;
    private DismissCallbacks mCallbacks;
    private int mDownPosition;
    private View mDownView;
    private float mDownX;
    private float mDownY;
    private ListView mListView;
    private int mMaxFlingVelocity;
    private int mMinFlingVelocity;
    private boolean mPaused;
    private int mSlop;
    private boolean mSwiping;
    private int mSwipingSlop;
    private VelocityTracker mVelocityTracker;
    private int mViewWidth = 1;
    private List<PendingDismissData> mPendingDismisses = new ArrayList();
    private int mDismissAnimationRefCount = 0;

    public interface DismissCallbacks {
        boolean canDismiss(int i);

        void onDismiss(ListView listView, int[] iArr);

        boolean onclick(int i);
    }

    public class PendingDismissData implements Comparable<PendingDismissData> {
        public int position;
        public View view;

        public PendingDismissData(SwipeDismissListViewTouchListener swipeDismissListViewTouchListener, int i, View view) {
            this.position = i;
            this.view = view;
        }

        @Override
        public int compareTo(PendingDismissData pendingDismissData) {
            return pendingDismissData.position - this.position;
        }
    }

    @SuppressLint("ResourceType")
    public SwipeDismissListViewTouchListener(ListView listView, DismissCallbacks dismissCallbacks) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(listView.getContext());
        this.mSlop = viewConfiguration.getScaledTouchSlop();
        this.mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity() * 16;
        this.mMaxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mAnimationTime = listView.getContext().getResources().getInteger(17694720);
        this.mListView = listView;
        this.mCallbacks = dismissCallbacks;
    }

    public static int c(SwipeDismissListViewTouchListener swipeDismissListViewTouchListener) {
        int i = swipeDismissListViewTouchListener.mDismissAnimationRefCount - 1;
        swipeDismissListViewTouchListener.mDismissAnimationRefCount = i;
        return i;
    }


    public void performDismiss(final View view, int i) {
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        final int height = view.getHeight();
        ValueAnimator duration = ValueAnimator.ofInt(height, 1).setDuration(this.mAnimationTime);
        duration.addListener(new AnimatorListenerAdapter() { // from class: com.mobiclean.phoneclean.utility.SwipeDismissListViewTouchListener.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SwipeDismissListViewTouchListener.c(SwipeDismissListViewTouchListener.this);
                if (SwipeDismissListViewTouchListener.this.mDismissAnimationRefCount == 0) {
                    try {
                        Collections.sort(SwipeDismissListViewTouchListener.this.mPendingDismisses);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int[] iArr = new int[SwipeDismissListViewTouchListener.this.mPendingDismisses.size()];
                    for (int size = SwipeDismissListViewTouchListener.this.mPendingDismisses.size() - 1; size >= 0; size--) {
                        iArr[size] = ((PendingDismissData) SwipeDismissListViewTouchListener.this.mPendingDismisses.get(size)).position;
                    }
                    SwipeDismissListViewTouchListener.this.mCallbacks.onDismiss(SwipeDismissListViewTouchListener.this.mListView, iArr);
                    SwipeDismissListViewTouchListener.this.mDownPosition = -1;
                    for (PendingDismissData pendingDismissData : SwipeDismissListViewTouchListener.this.mPendingDismisses) {
                        pendingDismissData.view.setAlpha(1.0f);
                        pendingDismissData.view.setTranslationX(0.0f);
                        ViewGroup.LayoutParams layoutParams2 = pendingDismissData.view.getLayoutParams();
                        layoutParams2.height = height;
                        pendingDismissData.view.setLayoutParams(layoutParams2);
                    }
                    long uptimeMillis = SystemClock.uptimeMillis();
                    SwipeDismissListViewTouchListener.this.mListView.dispatchTouchEvent(MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0));
                    SwipeDismissListViewTouchListener.this.mPendingDismisses.clear();
                }
            }
        });
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.height = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                view.setLayoutParams(layoutParams);
            }
        });
        this.mPendingDismisses.add(new PendingDismissData(this, i, view));
        duration.start();
    }

    public AbsListView.OnScrollListener makeScrollListener() {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                SwipeDismissListViewTouchListener.this.setEnabled(i != 1);
            }
        };
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean z;
        boolean z2;
        if (this.mViewWidth < 2) {
            this.mViewWidth = this.mListView.getWidth();
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            if (this.mPaused) {
                return false;
            }
            Rect rect = new Rect();
            int childCount = this.mListView.getChildCount();
            int[] iArr = new int[2];
            this.mListView.getLocationOnScreen(iArr);
            int rawX = ((int) motionEvent.getRawX()) - iArr[0];
            int rawY = ((int) motionEvent.getRawY()) - iArr[1];
            int i = 0;
            while (true) {
                if (i >= childCount) {
                    break;
                }
                View childAt = this.mListView.getChildAt(i);
                childAt.getHitRect(rect);
                if (rect.contains(rawX, rawY)) {
                    this.mDownView = childAt;
                    break;
                }
                i++;
            }
            if (this.mDownView != null) {
                this.mDownX = motionEvent.getRawX();
                this.mDownY = motionEvent.getRawY();
                int positionForView = this.mListView.getPositionForView(this.mDownView);
                this.mDownPosition = positionForView;
                if (this.mCallbacks.canDismiss(positionForView)) {
                    VelocityTracker obtain = VelocityTracker.obtain();
                    this.mVelocityTracker = obtain;
                    obtain.addMovement(motionEvent);
                } else {
                    this.mDownView = null;
                }
            }
            return false;
        }
        if (actionMasked == 1) {
            Log.d("TOUCCC", "11111");
            if (this.mVelocityTracker != null) {
                Log.d("TOUCCC", "2222");
                float rawX2 = motionEvent.getRawX() - this.mDownX;
                this.mVelocityTracker.addMovement(motionEvent);
                this.mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = this.mVelocityTracker.getXVelocity();
                float abs = Math.abs(xVelocity);
                float abs2 = Math.abs(this.mVelocityTracker.getYVelocity());
                if (Math.abs(rawX2) > this.mViewWidth / 2 && this.mSwiping) {
                    Log.d("TOUCCC", "3333");
                    z2 = rawX2 > 0.0f;
                    z = true;
                } else {
                    if (this.mMinFlingVelocity > abs || abs > this.mMaxFlingVelocity || abs2 >= abs || !this.mSwiping) {
                        z = false;
                    } else {
                        Log.d("TOUCCC", "4444");
                        z = ((xVelocity > 0.0f ? 1 : (xVelocity == 0.0f ? 0 : -1)) < 0) == ((rawX2 > 0.0f ? 1 : (rawX2 == 0.0f ? 0 : -1)) < 0);
                        if (this.mVelocityTracker.getXVelocity() > 0.0f) {
                            z2 = true;
                        }
                    }
                    z2 = false;
                }
                if (z && this.mDownPosition != -1) {
                    Log.d("TOUCCC", "5555");
                    final View view2 = this.mDownView;
                    final int i2 = this.mDownPosition;
                    this.mDismissAnimationRefCount++;
                    view2.animate().translationX(z2 ? this.mViewWidth : -this.mViewWidth).alpha(0.0f).setDuration(this.mAnimationTime).setListener(new AnimatorListenerAdapter() { // from class: com.mobiclean.phoneclean.utility.SwipeDismissListViewTouchListener.2
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            SwipeDismissListViewTouchListener.this.performDismiss(view2, i2);
                        }
                    });
                } else {
                    this.mCallbacks.onclick(this.mDownPosition);
                    this.mDownView.animate().translationX(0.0f).alpha(1.0f).setDuration(this.mAnimationTime).setListener(null);
                }
                Log.d("TOUCCC", "7777");
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                this.mDownX = 0.0f;
                this.mDownY = 0.0f;
                this.mDownView = null;
                this.mDownPosition = -1;
                this.mSwiping = false;
            }
        } else if (actionMasked != 2) {
            if (actionMasked == 3 && this.mVelocityTracker != null) {
                View view3 = this.mDownView;
                if (view3 != null && this.mSwiping) {
                    view3.animate().translationX(0.0f).alpha(1.0f).setDuration(this.mAnimationTime).setListener(null);
                }
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                this.mDownX = 0.0f;
                this.mDownY = 0.0f;
                this.mDownView = null;
                this.mDownPosition = -1;
                this.mSwiping = false;
            }
        } else {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null && !this.mPaused) {
                velocityTracker.addMovement(motionEvent);
                float rawX3 = motionEvent.getRawX() - this.mDownX;
                float rawY2 = motionEvent.getRawY() - this.mDownY;
                if (Math.abs(rawX3) > this.mSlop && Math.abs(rawY2) < Math.abs(rawX3) / 2.0f) {
                    this.mSwiping = true;
                    this.mSwipingSlop = rawX3 > 0.0f ? this.mSlop : -this.mSlop;
                    this.mListView.requestDisallowInterceptTouchEvent(true);
                    MotionEvent obtain2 = MotionEvent.obtain(motionEvent);
                    obtain2.setAction((motionEvent.getActionIndex() << 8) | 3);
                    this.mListView.onTouchEvent(obtain2);
                    obtain2.recycle();
                }
                if (this.mSwiping) {
                    this.mDownView.setTranslationX(rawX3 - this.mSwipingSlop);
                    this.mDownView.setAlpha(Math.max(0.0f, Math.min(1.0f, 1.0f - ((Math.abs(rawX3) * 2.0f) / this.mViewWidth))));
                    return true;
                }
            } else {
                Log.d("TOUCCC", "101010");
            }
        }
        return false;
    }

    public void setEnabled(boolean z) {
        this.mPaused = !z;
    }
}
