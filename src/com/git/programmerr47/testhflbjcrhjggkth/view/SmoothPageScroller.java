package com.git.programmerr47.testhflbjcrhjggkth.view;

import android.content.Context;
import android.widget.Scroller;

public class SmoothPageScroller extends Scroller{

	private int mDuration = 1500;

    public SmoothPageScroller(Context context) {
        super(context);
    }
    
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

}
