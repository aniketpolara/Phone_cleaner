package com.cleanPhone.mobileCleaner.similerphotos;

import java.util.ArrayList;
import java.util.List;


public class LineDefinition {
    private final ConfigDefinition config;
    private int lineLength;
    private int lineThickness;
    private final List<ViewDefinition> views = new ArrayList();
    private int lineStartThickness = 0;
    private int lineStartLength = 0;

    public LineDefinition(ConfigDefinition configDefinition) {
        this.config = configDefinition;
    }

    public void addView(ViewDefinition viewDefinition) {
        addView(this.views.size(), viewDefinition);
    }

    public boolean canFit(ViewDefinition viewDefinition) {
        return (this.lineLength + viewDefinition.getLength()) + viewDefinition.getSpacingLength() <= this.config.getMaxLength();
    }

    public int getLineLength() {
        return this.lineLength;
    }

    public int getLineStartLength() {
        return this.lineStartLength;
    }

    public int getLineStartThickness() {
        return this.lineStartThickness;
    }

    public int getLineThickness() {
        return this.lineThickness;
    }

    public List<ViewDefinition> getViews() {
        return this.views;
    }

    public int getX() {
        return this.config.getOrientation() == 0 ? this.lineStartLength : this.lineStartThickness;
    }

    public int getY() {
        return this.config.getOrientation() == 0 ? this.lineStartThickness : this.lineStartLength;
    }

    public void setLength(int i) {
        this.lineLength = i;
    }

    public void setLineStartLength(int i) {
        this.lineStartLength = i;
    }

    public void setLineStartThickness(int i) {
        this.lineStartThickness = i;
    }

    public void setThickness(int i) {
        this.lineThickness = i;
    }

    public void addView(int i, ViewDefinition viewDefinition) {
        this.views.add(i, viewDefinition);
        this.lineLength = this.lineLength + viewDefinition.getLength() + viewDefinition.getSpacingLength();
        this.lineThickness = Math.max(this.lineThickness, viewDefinition.getThickness() + viewDefinition.getSpacingThickness());
    }
}
