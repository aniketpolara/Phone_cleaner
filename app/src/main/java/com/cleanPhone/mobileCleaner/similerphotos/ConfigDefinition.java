package com.cleanPhone.mobileCleaner.similerphotos;


public class ConfigDefinition {
    private boolean checkCanFit;
    private int heightMode;
    private int maxHeight;
    private int maxWidth;
    private int widthMode;
    private int orientation = 0;
    private boolean debugDraw = false;
    private float weightDefault = 0.0f;
    private int gravity = 51;
    private int layoutDirection = 0;

    public ConfigDefinition() {
        setOrientation(0);
        setDebugDraw(false);
        setWeightDefault(0.0f);
        setGravity(0);
        setLayoutDirection(0);
        setCheckCanFit(true);
    }

    public int getGravity() {
        return this.gravity;
    }

    public int getLayoutDirection() {
        return this.layoutDirection;
    }

    public int getLengthMode() {
        return this.orientation == 0 ? this.widthMode : this.heightMode;
    }

    public int getMaxLength() {
        return this.orientation == 0 ? this.maxWidth : this.maxHeight;
    }

    public int getMaxThickness() {
        return this.orientation == 0 ? this.maxHeight : this.maxWidth;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public int getThicknessMode() {
        return this.orientation == 0 ? this.heightMode : this.widthMode;
    }

    public float getWeightDefault() {
        return this.weightDefault;
    }

    public boolean isCheckCanFit() {
        return this.checkCanFit;
    }

    public boolean isDebugDraw() {
        return this.debugDraw;
    }

    public void setCheckCanFit(boolean z) {
        this.checkCanFit = z;
    }

    public void setDebugDraw(boolean z) {
        this.debugDraw = z;
    }

    public void setGravity(int i) {
        this.gravity = i;
    }

    public void setHeightMode(int i) {
        this.heightMode = i;
    }

    public void setLayoutDirection(int i) {
        if (i == 1) {
            this.layoutDirection = i;
        } else {
            this.layoutDirection = 0;
        }
    }

    public void setMaxHeight(int i) {
        this.maxHeight = i;
    }

    public void setMaxWidth(int i) {
        this.maxWidth = i;
    }

    public void setOrientation(int i) {
        if (i == 1) {
            this.orientation = i;
        } else {
            this.orientation = 0;
        }
    }

    public void setWeightDefault(float f) {
        this.weightDefault = Math.max(0.0f, f);
    }

    public void setWidthMode(int i) {
        this.widthMode = i;
    }
}
