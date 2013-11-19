package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import android.view.View;
import com.nineoldandroids.view.ViewHelper;

import android.support.v4.view.ViewPager;

public class DepthPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0;

    public void transformPage(View view, float position) {
        float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
        
        if ((position >= -1) && (position <= 1)) {
            ViewHelper.setAlpha(view, 1 - Math.abs(position));
            ViewHelper.setTranslationX(view, 0);
        	ViewHelper.setScaleX(view, scaleFactor);
        	ViewHelper.setScaleY(view, scaleFactor);
        } else {
        	ViewHelper.setAlpha(view, MIN_ALPHA);
        }
    }
}
