package com.cleanPhone.mobileCleaner.animate;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import com.cleanPhone.mobileCleaner.R;

import java.util.Iterator;
import java.util.Vector;

public class GravView extends View {
    private Vector<GravAnimatorGenerator> gravAnimatorGenerators;
    private Vector<ValueAnimator> gravAnimators;
    private GravGenerator gravGenerator;
    private Vector<Grav> gravVector;
    private PaintGenerator paintGenerator;
    private PointGenerator pointGenerator;
    private ValueAnimator viewRefreshAnimator;

    public GravView(Context context) {
        super(context);
        initialize(null);
    }

    private Vector<Grav> generateBallFrom(Vector<PointF> vector) {
        Vector<Grav> vector2 = new Vector<>(vector.size());
        Iterator<PointF> it = vector.iterator();
        while (it.hasNext()) {
            Paint generate = this.paintGenerator.generate();
            vector2.add(this.gravGenerator.generate(it.next(), generate));
        }
        return vector2;
    }

    private Vector<ValueAnimator> generateGravAnimatorsFrom(Vector<Grav> vector) {
        Vector<ValueAnimator> vector2 = new Vector<>(vector.size());
        Iterator<Grav> it = vector.iterator();
        while (it.hasNext()) {
            Grav next = it.next();
            Iterator<GravAnimatorGenerator> it2 = this.gravAnimatorGenerators.iterator();
            while (it2.hasNext()) {
                vector2.add(it2.next().generateGravAnimator(next, getWidth(), getHeight()));
            }
        }
        return vector2;
    }

    @SuppressLint("ResourceType")
    private void initialize(AttributeSet attributeSet) {
        GeneratorFactory generatorFactory = new GeneratorFactory(getContext());
        initializeRefreshAnimator();
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(attributeSet, new int[]{R.style.GravView}, 0, 0);
            try {
                this.paintGenerator = generatorFactory.createPaint(obtainStyledAttributes.getString(2), attributeSet);
                this.pointGenerator = generatorFactory.createPoint(obtainStyledAttributes.getString(4), attributeSet);
                this.gravGenerator = generatorFactory.createGrav(obtainStyledAttributes.getString(3), attributeSet);
                this.gravAnimatorGenerators = obtainGravAnimators(attributeSet, obtainStyledAttributes, generatorFactory);
            } finally {
                obtainStyledAttributes.recycle();
            }
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                GravView.this.start();
                GravView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void initializeRefreshAnimator() {
        ValueAnimator ofInt = ValueAnimator.ofInt(0, 1);
        this.viewRefreshAnimator = ofInt;
        ofInt.setRepeatCount(-1);
        this.viewRefreshAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                GravView.this.invalidate();
            }
        });
    }

    private void obtainAnimatorFromSingleAttribute(AttributeSet attributeSet, TypedArray typedArray, GeneratorFactory generatorFactory, Vector<GravAnimatorGenerator> vector) {
        GravAnimatorGenerator createAnimator = generatorFactory.createAnimator(typedArray.getString(0), attributeSet);
        if (createAnimator != null) {
            vector.add(createAnimator);
        }
    }

    private void obtainAnimatorsFromArray(AttributeSet attributeSet, GeneratorFactory generatorFactory, Vector<GravAnimatorGenerator> vector, int i) {
        for (String str : getContext().getResources().getStringArray(i)) {
            GravAnimatorGenerator createAnimator = generatorFactory.createAnimator(str, attributeSet);
            if (createAnimator != null) {
                vector.add(createAnimator);
            }
        }
    }

    private Vector<GravAnimatorGenerator> obtainGravAnimators(AttributeSet attributeSet, TypedArray typedArray, GeneratorFactory generatorFactory) {
        Vector<GravAnimatorGenerator> vector = new Vector<>();
        @SuppressLint("ResourceType") int resourceId = typedArray.getResourceId(1, 0);
        if (resourceId != 0) {
            obtainAnimatorsFromArray(attributeSet, generatorFactory, vector, resourceId);
        } else {
            obtainAnimatorFromSingleAttribute(attributeSet, typedArray, generatorFactory, vector);
        }
        return vector;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Vector<Grav> vector = this.gravVector;
        if (vector != null) {
            Iterator<Grav> it = vector.iterator();
            while (it.hasNext()) {
                try {
                    it.next().draw(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        PointGenerator pointGenerator = this.pointGenerator;
        if (pointGenerator != null) {
            Vector<Grav> generateBallFrom = generateBallFrom(pointGenerator.generatePoints(i, i2));
            this.gravVector = generateBallFrom;
            this.gravAnimators = generateGravAnimatorsFrom(generateBallFrom);
        }
    }

    public void start() {
        this.viewRefreshAnimator.start();
        Vector<ValueAnimator> vector = this.gravAnimators;
        if (vector != null) {
            Iterator<ValueAnimator> it = vector.iterator();
            while (it.hasNext()) {
                it.next().start();
            }
        }
    }


    public GravView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(attributeSet);
    }

    public GravView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(attributeSet);
    }
}
