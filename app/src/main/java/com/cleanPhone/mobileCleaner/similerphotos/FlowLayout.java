package com.cleanPhone.mobileCleaner.similerphotos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

import com.cleanPhone.mobileCleaner.R;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    public List<LineDefinition> f5074a;
    public List<ViewDefinition> b;
    private final ConfigDefinition config;

    public FlowLayout(Context context) {
        super(context);
        this.f5074a = new ArrayList();
        this.b = new ArrayList();
        this.config = new ConfigDefinition();
        readStyleParameters(context, null);
    }

    private void applyPositionsToViews(LineDefinition lineDefinition) {
        List<ViewDefinition> views = lineDefinition.getViews();
        int size = views.size();
        for (int i = 0; i < size; i++) {
            ViewDefinition viewDefinition = views.get(i);
            viewDefinition.getView().measure(MeasureSpec.makeMeasureSpec(viewDefinition.getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(viewDefinition.getHeight(), MeasureSpec.EXACTLY));
        }
    }

    @SuppressLint("ResourceType")
    private void readStyleParameters(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{R.style.FlowLayout});
        try {
            this.config.setOrientation(obtainStyledAttributes.getInteger(1, 0));
            this.config.setDebugDraw(obtainStyledAttributes.getBoolean(2, false));
            this.config.setWeightDefault(obtainStyledAttributes.getFloat(6, 0.0f));
            this.config.setGravity(obtainStyledAttributes.getInteger(0, 0));
            this.config.setLayoutDirection(obtainStyledAttributes.getInteger(4, 0));
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    @Override
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override
    public boolean drawChild(Canvas canvas, View view, long j) {
        boolean drawChild = super.drawChild(canvas, view, j);
        return drawChild;
    }

    public int getGravity() {
        return this.config.getGravity();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int getLayoutDirection() {
        ConfigDefinition configDefinition = this.config;
        if (configDefinition == null) {
            return View.LAYOUT_DIRECTION_LTR;
        }
        return configDefinition.getLayoutDirection();
    }

    public int getOrientation() {
        return this.config.getOrientation();
    }


    @Override
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int size = this.f5074a.size();
        for (int i5 = 0; i5 < size; i5++) {
            LineDefinition lineDefinition = this.f5074a.get(i5);
            int size2 = lineDefinition.getViews().size();
            for (int i6 = 0; i6 < size2; i6++) {
                ViewDefinition viewDefinition = lineDefinition.getViews().get(i6);
                View view = viewDefinition.getView();
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                view.layout(getPaddingLeft() + lineDefinition.getX() + viewDefinition.getInlineX() + ((MarginLayoutParams) layoutParams).leftMargin, getPaddingTop() + lineDefinition.getY() + viewDefinition.getInlineY() + ((MarginLayoutParams) layoutParams).topMargin, getPaddingLeft() + lineDefinition.getX() + viewDefinition.getInlineX() + ((MarginLayoutParams) layoutParams).leftMargin + viewDefinition.getWidth(), getPaddingTop() + lineDefinition.getY() + viewDefinition.getInlineY() + ((MarginLayoutParams) layoutParams).topMargin + viewDefinition.getHeight());
            }
        }
    }

    @Override
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int childCount = getChildCount();
        this.b.clear();
        this.f5074a.clear();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != View.GONE) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                childAt.measure(ViewGroup.getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight(), ((MarginLayoutParams) layoutParams).width), ViewGroup.getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom(), ((MarginLayoutParams) layoutParams).height));
                ViewDefinition viewDefinition = new ViewDefinition(this.config, childAt);
                viewDefinition.setWidth(childAt.getMeasuredWidth());
                viewDefinition.setHeight(childAt.getMeasuredHeight());
                viewDefinition.setNewLine(layoutParams.isNewLine());
                viewDefinition.setGravity(layoutParams.getGravity());
                viewDefinition.setWeight(layoutParams.getWeight());
                viewDefinition.setMargins(((MarginLayoutParams) layoutParams).leftMargin, ((MarginLayoutParams) layoutParams).topMargin, ((MarginLayoutParams) layoutParams).rightMargin, ((MarginLayoutParams) layoutParams).bottomMargin);
                this.b.add(viewDefinition);
            }
        }
        this.config.setMaxWidth((MeasureSpec.getSize(i) - getPaddingRight()) - getPaddingLeft());
        this.config.setMaxHeight((MeasureSpec.getSize(i2) - getPaddingTop()) - getPaddingBottom());
        this.config.setWidthMode(MeasureSpec.getMode(i));
        this.config.setHeightMode(MeasureSpec.getMode(i2));
        ConfigDefinition configDefinition = this.config;
        configDefinition.setCheckCanFit(configDefinition.getLengthMode() != 0);
        CommonLogic.fillLines(this.b, this.f5074a, this.config);
        CommonLogic.calculateLinesAndChildPosition(this.f5074a);
        int size = this.f5074a.size();
        int i6 = 0;
        for (int i7 = 0; i7 < size; i7++) {
            i6 = Math.max(i6, this.f5074a.get(i7).getLineLength());
        }
        List<LineDefinition> list = this.f5074a;
        LineDefinition lineDefinition = list.get(list.size() - 1);
        int lineStartThickness = lineDefinition.getLineStartThickness() + lineDefinition.getLineThickness();
        CommonLogic.applyGravityToLines(this.f5074a, CommonLogic.findSize(this.config.getLengthMode(), this.config.getMaxLength(), i6), CommonLogic.findSize(this.config.getThicknessMode(), this.config.getMaxThickness(), lineStartThickness), this.config);
        for (int i8 = 0; i8 < size; i8++) {
            applyPositionsToViews(this.f5074a.get(i8));
        }
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        int paddingBottom = getPaddingBottom() + getPaddingTop();
        if (this.config.getOrientation() == 0) {
            i3 = paddingLeft + i6;
            i4 = paddingBottom + lineStartThickness;
        } else {
            i3 = paddingLeft + lineStartThickness;
            i4 = paddingBottom + i6;
        }
        setMeasuredDimension(ViewGroup.resolveSize(i3, i), ViewGroup.resolveSize(i4, i2));
    }



    public void setGravity(int i) {
        this.config.setGravity(i);
        requestLayout();
    }

    @Override
    public void setLayoutDirection(int i) {
        this.config.setLayoutDirection(i);
        requestLayout();
    }

    public void setOrientation(int i) {
        this.config.setOrientation(i);
        requestLayout();
    }

    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public static class LayoutParams extends MarginLayoutParams {
        private int gravity;
        @ViewDebug.ExportedProperty(mapping = {@ViewDebug.IntToString(from = 0, to = "NONE"), @ViewDebug.IntToString(from = 48, to = "TOP"), @ViewDebug.IntToString(from = 80, to = "BOTTOM"), @ViewDebug.IntToString(from = 3, to = "LEFT"), @ViewDebug.IntToString(from = 5, to = "RIGHT"), @ViewDebug.IntToString(from = 16, to = "CENTER_VERTICAL"), @ViewDebug.IntToString(from = 112, to = "FILL_VERTICAL"), @ViewDebug.IntToString(from = 1, to = "CENTER_HORIZONTAL"), @ViewDebug.IntToString(from = 7, to = "FILL_HORIZONTAL"), @ViewDebug.IntToString(from = 17, to = "CENTER"), @ViewDebug.IntToString(from = 119, to = "FILL")})
        private boolean newLine;
        private float weight;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.newLine = false;
            this.gravity = 0;
            this.weight = -1.0f;
            readStyleParameters(context, attributeSet);
        }

        @SuppressLint("ResourceType")
        private void readStyleParameters(Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{R.style.FlowLayout_LayoutParams});
            try {
                this.newLine = obtainStyledAttributes.getBoolean(1, false);
                this.gravity = obtainStyledAttributes.getInt(0, 0);
                this.weight = obtainStyledAttributes.getFloat(2, -1.0f);
            } finally {
                obtainStyledAttributes.recycle();
            }
        }

        public int getGravity() {
            return this.gravity;
        }

        public float getWeight() {
            return this.weight;
        }

        public boolean isNewLine() {
            return this.newLine;
        }

        public void setGravity(int i) {
            this.gravity = i;
        }

        public void setNewLine(boolean z) {
            this.newLine = z;
        }

        public void setWeight(float f) {
            this.weight = f;
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.newLine = false;
            this.gravity = 0;
            this.weight = -1.0f;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.newLine = false;
            this.gravity = 0;
            this.weight = -1.0f;
        }
    }

    public FlowLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f5074a = new ArrayList();
        this.b = new ArrayList();
        this.config = new ConfigDefinition();
        readStyleParameters(context, attributeSet);
    }

    public FlowLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f5074a = new ArrayList();
        this.b = new ArrayList();
        this.config = new ConfigDefinition();
        readStyleParameters(context, attributeSet);
    }
}
