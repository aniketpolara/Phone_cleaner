package com.cleanPhone.mobileCleaner.onetaps;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class ParticleField extends View {
    private ArrayList<Particle> mParticles;

    public ParticleField(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (this.mParticles) {
            for (int i = 0; i < this.mParticles.size(); i++) {
                this.mParticles.get(i).draw(canvas);
            }
        }
    }

    public void setParticles(ArrayList<Particle> arrayList) {
        this.mParticles = arrayList;
    }

    public ParticleField(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ParticleField(Context context) {
        super(context);
    }
}
