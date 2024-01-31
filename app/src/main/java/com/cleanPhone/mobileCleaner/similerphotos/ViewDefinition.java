package com.cleanPhone.mobileCleaner.similerphotos;

import android.view.View;

public class ViewDefinition {
    private int bottomMargin;
    private final ConfigDefinition config;
    private int gravity;
    private int height;
    private int inlineStartLength;
    private int inlineStartThickness;
    private int leftMargin;
    private boolean newLine;
    private int rightMargin;
    private int topMargin;
    private final View view;
    private float weight;
    private int width;

    public ViewDefinition(ConfigDefinition configDefinition, View view) {
        this.config = configDefinition;
        this.view = view;
    }

    public int getGravity() {
        return this.gravity;
    }

    public int getHeight() {
        return this.height;
    }

    public int getInlineStartLength() {
        return this.inlineStartLength;
    }

    public int getInlineStartThickness() {
        return this.inlineStartThickness;
    }

    public int getInlineX() {
        return this.config.getOrientation() == 0 ? this.inlineStartLength : this.inlineStartThickness;
    }

    public int getInlineY() {
        return this.config.getOrientation() == 0 ? this.inlineStartThickness : this.inlineStartLength;
    }

    public int getLength() {
        return this.config.getOrientation() == 0 ? this.width : this.height;
    }

    public int getSpacingLength() {
        int i;
        int i2;
        if (this.config.getOrientation() == 0) {
            i = this.leftMargin;
            i2 = this.rightMargin;
        } else {
            i = this.topMargin;
            i2 = this.bottomMargin;
        }
        return i + i2;
    }

    public int getSpacingThickness() {
        int i;
        int i2;
        if (this.config.getOrientation() == 0) {
            i = this.topMargin;
            i2 = this.bottomMargin;
        } else {
            i = this.leftMargin;
            i2 = this.rightMargin;
        }
        return i + i2;
    }

    public int getThickness() {
        return this.config.getOrientation() == 0 ? this.height : this.width;
    }

    public View getView() {
        return this.view;
    }

    public float getWeight() {
        return this.weight;
    }

    public int getWidth() {
        return this.width;
    }

    public boolean gravitySpecified() {
        return this.gravity != 0;
    }

    public boolean isNewLine() {
        return this.newLine;
    }

    public void setGravity(int i) {
        this.gravity = i;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public void setInlineStartLength(int i) {
        this.inlineStartLength = i;
    }

    public void setInlineStartThickness(int i) {
        this.inlineStartThickness = i;
    }

    public void setLength(int i) {
        if (this.config.getOrientation() == 0) {
            this.width = i;
        } else {
            this.height = i;
        }
    }

    public void setMargins(int i, int i2, int i3, int i4) {
        this.leftMargin = i;
        this.topMargin = i2;
        this.rightMargin = i3;
        this.bottomMargin = i4;
    }

    public void setNewLine(boolean z) {
        this.newLine = z;
    }

    public void setThickness(int i) {
        if (this.config.getOrientation() == 0) {
            this.height = i;
        } else {
            this.width = i;
        }
    }

    public void setWeight(float f) {
        this.weight = f;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public boolean weightSpecified() {
        return this.weight >= 0.0f;
    }
}
