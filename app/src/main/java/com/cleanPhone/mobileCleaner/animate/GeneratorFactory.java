package com.cleanPhone.mobileCleaner.animate;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class GeneratorFactory {
    private Context context;

    public GeneratorFactory(Context context) {
        this.context = context;
    }

    @Nullable
    public GravAnimatorGenerator createAnimator(String str, AttributeSet attributeSet) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        GravAnimatorGenerator gravAnimatorGenerator = (GravAnimatorGenerator) ClassUtil.getClassByName(str, GravAnimatorGenerator.class);
        if (gravAnimatorGenerator != null) {
            gravAnimatorGenerator.configure(attributeSet, this.context);
        }
        return gravAnimatorGenerator;
    }

    public GravGenerator createGrav(String str, AttributeSet attributeSet) {
        if (str != null && !str.isEmpty()) {
            GravGenerator gravGenerator = (GravGenerator) ClassUtil.getClassByName(str, GravGenerator.class);
            if (gravGenerator != null) {
                gravGenerator.configure(attributeSet, this.context);
            }
            return gravGenerator;
        }
        return new BallCreator();
    }

    public PaintGenerator createPaint(String str, AttributeSet attributeSet) {
        if (str != null && !str.isEmpty()) {
            PaintGenerator paintGenerator = (PaintGenerator) ClassUtil.getClassByName(str, PaintGenerator.class);
            if (paintGenerator != null) {
                paintGenerator.configure(attributeSet, this.context);
            }
            return paintGenerator;
        }
        return new RandomColorGenerator();
    }

    public PointGenerator createPoint(String str, AttributeSet attributeSet) {
        if (str != null && !str.isEmpty()) {
            PointGenerator pointGenerator = (PointGenerator) ClassUtil.getClassByName(str, PointGenerator.class);
            if (pointGenerator != null) {
                pointGenerator.configure(attributeSet, this.context);
            }
            return pointGenerator;
        }
        return new RegularPointGenerator();
    }
}
