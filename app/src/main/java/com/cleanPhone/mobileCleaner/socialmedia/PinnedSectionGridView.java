package com.cleanPhone.mobileCleaner.socialmedia;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;
import android.widget.WrapperListAdapter;

import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;

public class PinnedSectionGridView extends AutoScrollGridView {
    public OnScrollListener f5119a;
    public PinnedSection b;
    public PinnedSection f5120c;
    public int f5121d;
    private int mAvailableWidth;
    private int mColumnWidth;
    private final DataSetObserver mDataSetObserver;
    private MotionEvent mDownEvent;
    private int mHorizontalSpacing;
    private int mNumColumns;
    private final OnScrollListener mOnScrollListener;
    private int mSectionsDistanceY;
    private GradientDrawable mShadowDrawable;
    private int mShadowHeight;
    private final PointF mTouchPoint;
    private final Rect mTouchRect;
    private int mTouchSlop;
    private View mTouchTarget;

    public static class PinnedSection {
        public long id;
        public int position;
        public View view;
    }

    public interface PinnedSectionGridAdapter extends ListAdapter {
        int getHeaderLayoutResId();

        boolean isItemViewTypePinned(int i);
    }

    public PinnedSectionGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTouchRect = new Rect();
        this.mTouchPoint = new PointF();
        this.mOnScrollListener = new OnScrollListener() {
            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                OnScrollListener onScrollListener = PinnedSectionGridView.this.f5119a;
                if (onScrollListener != null) {
                    onScrollListener.onScroll(absListView, i, i2, i3);
                }
                PinnedSectionGridAdapter pinnedAdapter = PinnedSectionGridView.this.getPinnedAdapter();
                if (pinnedAdapter == null || i2 == 0) {
                    return;
                }
                if (PinnedSectionGridView.isItemViewTypePinned(pinnedAdapter, i)) {
                    if (PinnedSectionGridView.this.getChildAt(0).getTop() == PinnedSectionGridView.this.getPaddingTop()) {
                        PinnedSectionGridView.this.c();
                        return;
                    } else {
                        PinnedSectionGridView.this.d(i, i, i2);
                        return;
                    }
                }
                int e = PinnedSectionGridView.this.e(i);
                if (e > -1) {
                    PinnedSectionGridView.this.d(e, i, i2);
                } else {
                    PinnedSectionGridView.this.c();
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                OnScrollListener onScrollListener = PinnedSectionGridView.this.f5119a;
                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(absListView, i);
                }
            }
        };
        this.mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                PinnedSectionGridView.this.g();
            }

            @Override
            public void onInvalidated() {
                PinnedSectionGridView.this.g();
            }
        };
        initView();
    }

    private void clearTouchTarget() {
        this.mTouchTarget = null;
        MotionEvent motionEvent = this.mDownEvent;
        if (motionEvent != null) {
            motionEvent.recycle();
            this.mDownEvent = null;
        }
    }


    public PinnedSectionGridAdapter getPinnedAdapter() {
        if (getAdapter() instanceof WrapperListAdapter) {
            return (PinnedSectionGridAdapter) ((WrapperListAdapter) getAdapter()).getWrappedAdapter();
        }
        return (PinnedSectionGridAdapter) getAdapter();
    }

    private void initView() {
        setOnScrollListener(this.mOnScrollListener);
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        initShadow(true);
    }

    public static boolean isItemViewTypePinned(ListAdapter listAdapter, int i) {
        if (listAdapter instanceof HeaderViewListAdapter) {
            listAdapter = ((HeaderViewListAdapter) listAdapter).getWrappedAdapter();
        }
        return ((PinnedSectionGridAdapter) listAdapter).isItemViewTypePinned(i);
    }

    private boolean isPinnedViewTouched(View view, float f, float f2) {
        view.getHitRect(this.mTouchRect);
        Rect rect = this.mTouchRect;
        int i = rect.top;
        int i2 = this.f5121d;
        rect.top = i + i2;
        rect.bottom += i2 + getPaddingTop();
        this.mTouchRect.left += getPaddingLeft();
        this.mTouchRect.right -= getPaddingRight();
        return this.mTouchRect.contains((int) f, (int) f2);
    }

    private boolean performPinnedItemClick() {
        OnItemClickListener onItemClickListener;
        if (this.f5120c == null || (onItemClickListener = getOnItemClickListener()) == null) {
            return false;
        }
        View view = this.f5120c.view;
        playSoundEffect(SoundEffectConstants.CLICK);
        if (view != null) {
            view.sendAccessibilityEvent(1);
        }
        PinnedSection pinnedSection = this.f5120c;
        onItemClickListener.onItemClick(this, view, pinnedSection.position, pinnedSection.id);
        return true;
    }

    @SuppressLint("WrongConstant")
    public void b(int i) {
        PinnedSection pinnedSection = this.b;
        this.b = null;
        if (pinnedSection == null) {
            pinnedSection = new PinnedSection();
        }
        View view = getAdapter().getView(i, pinnedSection.view, this);
        ((HeaderLayout) view.findViewById(getPinnedAdapter().getHeaderLayoutResId())).setHeaderWidth(1);
        view.setBackgroundColor(-1);
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-1, -2);
        }
        int mode = MeasureSpec.getMode(layoutParams.height);
        int size = MeasureSpec.getSize(layoutParams.height);
        if (mode == 0) {
            mode = BasicMeasure.EXACTLY;
        }
        int height = (getHeight() - getListPaddingTop()) - getListPaddingBottom();
        if (size > height) {
            size = height;
        }
        view.measure(MeasureSpec.makeMeasureSpec((getWidth() - getListPaddingLeft()) - getListPaddingRight(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(size, mode));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        this.f5121d = 0;
        pinnedSection.view = view;
        pinnedSection.position = i;
        pinnedSection.id = getAdapter().getItemId(i);
        this.f5120c = pinnedSection;
    }

    public void c() {
        PinnedSection pinnedSection = this.f5120c;
        if (pinnedSection != null) {
            this.b = pinnedSection;
            this.f5120c = null;
        }
    }

    public void d(int i, int i2, int i3) {
        if (i3 < 2) {
            c();
            return;
        }
        PinnedSection pinnedSection = this.f5120c;
        if (pinnedSection != null && pinnedSection.position != i) {
            c();
        }
        if (this.f5120c == null) {
            b(i);
        }
        int numColumns = i + getNumColumns();
        if (numColumns < getCount()) {
            int f = f(numColumns, i3 - (numColumns - i2));
            if (f > -1) {
                int top = getChildAt(f - i2).getTop() - (this.f5120c.view.getBottom() + getPaddingTop());
                this.mSectionsDistanceY = top;
                if (top < 0) {
                    this.f5121d = top;
                    return;
                } else {
                    this.f5121d = 0;
                    return;
                }
            }
            this.f5121d = 0;
            this.mSectionsDistanceY = Integer.MAX_VALUE;
        }
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.f5120c != null) {
            int listPaddingLeft = getListPaddingLeft();
            int listPaddingTop = getListPaddingTop();
            View view = this.f5120c.view;
            canvas.save();
            canvas.clipRect(listPaddingLeft, listPaddingTop, view.getWidth() + listPaddingLeft, view.getHeight() + (this.mShadowDrawable == null ? 0 : Math.min(this.mShadowHeight, this.mSectionsDistanceY)) + listPaddingTop);
            canvas.translate(listPaddingLeft, listPaddingTop + this.f5121d);
            drawChild(canvas, this.f5120c.view, getDrawingTime());
            GradientDrawable gradientDrawable = this.mShadowDrawable;
            if (gradientDrawable != null && this.mSectionsDistanceY > 0) {
                gradientDrawable.setBounds(this.f5120c.view.getLeft(), this.f5120c.view.getBottom(), this.f5120c.view.getRight(), this.f5120c.view.getBottom() + this.mShadowHeight);
                this.mShadowDrawable.draw(canvas);
            }
            canvas.restore();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        PinnedSection pinnedSection;
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0 && this.mTouchTarget == null && (pinnedSection = this.f5120c) != null && isPinnedViewTouched(pinnedSection.view, x, y)) {
            this.mTouchTarget = this.f5120c.view;
            PointF pointF = this.mTouchPoint;
            pointF.x = x;
            pointF.y = y;
            this.mDownEvent = MotionEvent.obtain(motionEvent);
        }
        View view = this.mTouchTarget;
        if (view != null) {
            if (isPinnedViewTouched(view, x, y)) {
                this.mTouchTarget.dispatchTouchEvent(motionEvent);
            }
            if (action == 1) {
                super.dispatchTouchEvent(motionEvent);
                performPinnedItemClick();
                clearTouchTarget();
            } else if (action == 3) {
                clearTouchTarget();
            } else if (action == 2 && Math.abs(y - this.mTouchPoint.y) > this.mTouchSlop) {
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                obtain.setAction(3);
                this.mTouchTarget.dispatchTouchEvent(obtain);
                obtain.recycle();
                super.dispatchTouchEvent(this.mDownEvent);
                super.dispatchTouchEvent(motionEvent);
                clearTouchTarget();
            }
            return true;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public int e(int i) {
        PinnedSectionGridAdapter pinnedAdapter = getPinnedAdapter();
        if (pinnedAdapter instanceof SectionIndexer) {
            SectionIndexer sectionIndexer = (SectionIndexer) pinnedAdapter;
            int positionForSection = sectionIndexer.getPositionForSection(sectionIndexer.getSectionForPosition(i));
            if (isItemViewTypePinned(pinnedAdapter, positionForSection)) {
                return positionForSection;
            }
        }
        while (i >= 0) {
            if (isItemViewTypePinned(pinnedAdapter, i)) {
                return i;
            }
            i--;
        }
        return -1;
    }

    public int f(int i, int i2) {
        PinnedSectionGridAdapter pinnedAdapter = getPinnedAdapter();
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = i + i3;
            if (isItemViewTypePinned(pinnedAdapter, i4)) {
                return i4;
            }
        }
        return -1;
    }

    public void g() {
        int firstVisiblePosition;
        int e;
        c();
        ListAdapter adapter = getAdapter();
        if (adapter == null || adapter.getCount() <= 0 || (e = e((firstVisiblePosition = getFirstVisiblePosition()))) == -1) {
            return;
        }
        d(e, firstVisiblePosition, getLastVisiblePosition() - firstVisiblePosition);
    }

    public int getAvailableWidth() {
        int i = this.mAvailableWidth;
        return i != 0 ? i : getWidth();
    }

    @Override
    public int getColumnWidth() {
        return this.mColumnWidth;
    }

    @Override
    public int getHorizontalSpacing() {
        return this.mHorizontalSpacing;
    }

    @Override
    public int getNumColumns() {
        return this.mNumColumns;
    }

    public void initShadow(boolean z) {
        if (z) {
            if (this.mShadowDrawable == null) {
                this.mShadowDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#ffa0a0a0"), Color.parseColor("#50a0a0a0"), Color.parseColor("#00a0a0a0")});
                this.mShadowHeight = (int) (getResources().getDisplayMetrics().density * 8.0f);
            }
        } else if (this.mShadowDrawable != null) {
            this.mShadowDrawable = null;
            this.mShadowHeight = 0;
        }
    }

    @Override
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.f5120c == null || ((i3 - i) - getPaddingLeft()) - getPaddingRight() == this.f5120c.view.getWidth()) {
            return;
        }
        g();
    }

    @Override
    public void onMeasure(int i, int i2) {
        if (this.mNumColumns == -1) {
            this.mAvailableWidth = MeasureSpec.getSize(i);
            if (this.mColumnWidth > 0) {
                int size = (MeasureSpec.getSize(i) - getPaddingLeft()) - getPaddingRight();
                int i3 = this.mHorizontalSpacing;
                this.mNumColumns = (size + i3) / (this.mColumnWidth + i3);
            } else {
                this.mNumColumns = 2;
            }
            if (getAdapter() != null && (getAdapter() instanceof SimpleSectionedGridAdapter)) {
                ((SimpleSectionedGridAdapter) getAdapter()).setSections();
            }
        }
        super.onMeasure(i, i2);
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        super.onRestoreInstanceState(parcelable);
        post(new Runnable() {
            @Override
            public void run() {
                PinnedSectionGridView.this.g();
            }
        });
    }

    @Override
    public void setColumnWidth(int i) {
        this.mColumnWidth = i;
        super.setColumnWidth(i);
    }

    @Override
    public void setHorizontalSpacing(int i) {
        this.mHorizontalSpacing = i;
        super.setHorizontalSpacing(i);
    }

    @Override
    public void setNumColumns(int i) {
        this.mNumColumns = i;
        super.setNumColumns(i);
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        if (onScrollListener == this.mOnScrollListener) {
            super.setOnScrollListener(onScrollListener);
        } else {
            this.f5119a = onScrollListener;
        }
    }


    @Override
    public void setAdapter(ListAdapter listAdapter) {
        ListAdapter adapter = getAdapter();
        if (adapter != null) {
            adapter.unregisterDataSetObserver(this.mDataSetObserver);
        }
        if (listAdapter != null) {
            listAdapter.registerDataSetObserver(this.mDataSetObserver);
        }
        if (adapter != listAdapter) {
            c();
        }
        super.setAdapter(listAdapter);
    }

    public PinnedSectionGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTouchRect = new Rect();
        this.mTouchPoint = new PointF();
        this.mOnScrollListener = new OnScrollListener() {
            @Override
            public void onScroll(AbsListView absListView, int i2, int i22, int i3) {
                OnScrollListener onScrollListener = PinnedSectionGridView.this.f5119a;
                if (onScrollListener != null) {
                    onScrollListener.onScroll(absListView, i2, i22, i3);
                }
                PinnedSectionGridAdapter pinnedAdapter = PinnedSectionGridView.this.getPinnedAdapter();
                if (pinnedAdapter == null || i22 == 0) {
                    return;
                }
                if (PinnedSectionGridView.isItemViewTypePinned(pinnedAdapter, i2)) {
                    if (PinnedSectionGridView.this.getChildAt(0).getTop() == PinnedSectionGridView.this.getPaddingTop()) {
                        PinnedSectionGridView.this.c();
                        return;
                    } else {
                        PinnedSectionGridView.this.d(i2, i2, i22);
                        return;
                    }
                }
                int e = PinnedSectionGridView.this.e(i2);
                if (e > -1) {
                    PinnedSectionGridView.this.d(e, i2, i22);
                } else {
                    PinnedSectionGridView.this.c();
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i2) {
                OnScrollListener onScrollListener = PinnedSectionGridView.this.f5119a;
                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(absListView, i2);
                }
            }
        };
        this.mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                PinnedSectionGridView.this.g();
            }

            @Override
            public void onInvalidated() {
                PinnedSectionGridView.this.g();
            }
        };
        initView();
    }
}
