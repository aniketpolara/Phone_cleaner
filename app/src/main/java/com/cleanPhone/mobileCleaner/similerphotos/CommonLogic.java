package com.cleanPhone.mobileCleaner.similerphotos;

import android.graphics.Rect;
import android.view.Gravity;

import java.util.List;

public class CommonLogic {

    public static void applyGravityToLine(LineDefinition lineDefinition, ConfigDefinition configDefinition) {
        int round;
        List<ViewDefinition> views = lineDefinition.getViews();
        int size = views.size();
        if (size <= 0) {
            return;
        }
        float f = 0.0f;
        for (int i = 0; i < size; i++) {
            f += getWeight(views.get(i), configDefinition);
        }
        ViewDefinition viewDefinition = views.get(size - 1);
        int lineLength = lineDefinition.getLineLength() - ((viewDefinition.getLength() + viewDefinition.getSpacingLength()) + viewDefinition.getInlineStartLength());
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            ViewDefinition viewDefinition2 = views.get(i3);
            float weight = getWeight(viewDefinition2, configDefinition);
            int gravity = getGravity(viewDefinition2, configDefinition);
            if (f == 0.0f) {
                round = lineLength / size;
            } else {
                round = Math.round((lineLength * weight) / f);
            }
            int length = viewDefinition2.getLength() + viewDefinition2.getSpacingLength();
            int thickness = viewDefinition2.getThickness() + viewDefinition2.getSpacingThickness();
            Rect rect = new Rect();
            rect.top = 0;
            rect.left = i2;
            rect.right = length + round + i2;
            rect.bottom = lineDefinition.getLineThickness();
            Rect rect2 = new Rect();
            Gravity.apply(gravity, length, thickness, rect, rect2);
            i2 += round;
            viewDefinition2.setInlineStartLength(rect2.left + viewDefinition2.getInlineStartLength());
            viewDefinition2.setInlineStartThickness(rect2.top);
            viewDefinition2.setLength(rect2.width() - viewDefinition2.getSpacingLength());
            viewDefinition2.setThickness(rect2.height() - viewDefinition2.getSpacingThickness());
        }
    }

    public static void applyGravityToLines(List<LineDefinition> list, int i, int i2, ConfigDefinition configDefinition) {
        int size = list.size();
        if (size <= 0) {
            return;
        }
        LineDefinition lineDefinition = list.get(size - 1);
        int lineThickness = i2 - (lineDefinition.getLineThickness() + lineDefinition.getLineStartThickness());
        if (lineThickness < 0) {
            lineThickness = 0;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < size; i4++) {
            LineDefinition lineDefinition2 = list.get(i4);
            int gravity = getGravity(null, configDefinition);
            int round = Math.round((lineThickness * 1) / size);
            int lineLength = lineDefinition2.getLineLength();
            int lineThickness2 = lineDefinition2.getLineThickness();
            Rect rect = new Rect();
            rect.top = i3;
            rect.left = 0;
            rect.right = i;
            rect.bottom = lineThickness2 + round + i3;
            Rect rect2 = new Rect();
            Gravity.apply(gravity, lineLength, lineThickness2, rect, rect2);
            i3 += round;
            lineDefinition2.setLineStartLength(lineDefinition2.getLineStartLength() + rect2.left);
            lineDefinition2.setLineStartThickness(lineDefinition2.getLineStartThickness() + rect2.top);
            lineDefinition2.setLength(rect2.width());
            lineDefinition2.setThickness(rect2.height());
            applyGravityToLine(lineDefinition2, configDefinition);
        }
    }

    public static void calculateLinesAndChildPosition(List<LineDefinition> list) {
        int size = list.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            LineDefinition lineDefinition = list.get(i2);
            lineDefinition.setLineStartThickness(i);
            i += lineDefinition.getLineThickness();
            List<ViewDefinition> views = lineDefinition.getViews();
            int size2 = views.size();
            int i3 = 0;
            for (int i4 = 0; i4 < size2; i4++) {
                ViewDefinition viewDefinition = views.get(i4);
                viewDefinition.setInlineStartLength(i3);
                i3 += viewDefinition.getLength() + viewDefinition.getSpacingLength();
            }
        }
    }

    public static void fillLines(List<ViewDefinition> list, List<LineDefinition> list2, ConfigDefinition configDefinition) {
        LineDefinition lineDefinition = new LineDefinition(configDefinition);
        list2.add(lineDefinition);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ViewDefinition viewDefinition = list.get(i);
            if (viewDefinition.isNewLine() || (configDefinition.isCheckCanFit() && !lineDefinition.canFit(viewDefinition))) {
                lineDefinition = new LineDefinition(configDefinition);
                if (configDefinition.getOrientation() == 1 && configDefinition.getLayoutDirection() == 1) {
                    list2.add(0, lineDefinition);
                } else {
                    list2.add(lineDefinition);
                }
            }
            if (configDefinition.getOrientation() == 0 && configDefinition.getLayoutDirection() == 1) {
                lineDefinition.addView(0, viewDefinition);
            } else {
                lineDefinition.addView(viewDefinition);
            }
        }
    }

    public static int findSize(int i, int i2, int i3) {
        if (i != Integer.MIN_VALUE) {
            return i != 1073741824 ? i3 : i2;
        }
        return Math.min(i3, i2);
    }

    private static int getGravity(ViewDefinition viewDefinition, ConfigDefinition configDefinition) {
        int gravity = configDefinition.getGravity();
        int gravityFromRelative = getGravityFromRelative((viewDefinition == null || !viewDefinition.gravitySpecified()) ? gravity : viewDefinition.getGravity(), configDefinition);
        int gravityFromRelative2 = getGravityFromRelative(gravity, configDefinition);
        if ((gravityFromRelative & 7) == 0) {
            gravityFromRelative |= gravityFromRelative2 & 7;
        }
        if ((gravityFromRelative & 112) == 0) {
            gravityFromRelative |= gravityFromRelative2 & 112;
        }
        if ((gravityFromRelative & 7) == 0) {
            gravityFromRelative |= 3;
        }
        return (gravityFromRelative & 112) == 0 ? gravityFromRelative | 48 : gravityFromRelative;
    }

    public static int getGravityFromRelative(int i, ConfigDefinition configDefinition) {
        if (configDefinition.getOrientation() == 1 && (i & 8388608) == 0) {
            i = (((i & 112) >> 4) << 0) | (((i & 7) >> 0) << 4) | 0;
        }
        if (configDefinition.getLayoutDirection() != 1 || (i & 8388608) == 0) {
            return i;
        }
        return ((i & 3) == 3 ? 5 : 0) | 0 | ((i & 5) == 5 ? 3 : 0);
    }

    private static float getWeight(ViewDefinition viewDefinition, ConfigDefinition configDefinition) {
        return viewDefinition.weightSpecified() ? viewDefinition.getWeight() : configDefinition.getWeightDefault();
    }
}
