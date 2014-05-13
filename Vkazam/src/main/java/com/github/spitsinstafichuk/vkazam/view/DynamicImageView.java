package com.github.spitsinstafichuk.vkazam.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DynamicImageView extends ImageView {

    private double koef = 3.0 / 4;

    public DynamicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final Drawable d = this.getDrawable();

        if (d != null) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) Math
                    .ceil(width * (float) d.getIntrinsicHeight() / d.getIntrinsicWidth());
            if (height > width * koef) {
                height = (int) (width * koef);
            }
            this.setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setMaxRatioKoef(double koef) {
        this.koef = koef;
    }
}
