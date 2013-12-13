package com.git.programmerr47.testhflbjcrhjggkth.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.git.programmerr47.testhflbjcrhjggkth.R;

public class IconCheckBoxPreference extends CheckBoxPreference {

    private Drawable mIcon;
    private View preference;
    private View.OnClickListener listener;
    private int iconVisibility;

    public IconCheckBoxPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconCheckBoxPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.checkbox_preference);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.IconCheckBoxPreference, defStyle, 0);
        mIcon = a.getDrawable(R.styleable.IconCheckBoxPreference_icon);
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        preference = view.findViewById(R.id.preference);
        if (listener != null) {
            preference.setOnClickListener(listener);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        if (imageView != null && mIcon != null) {
            imageView.setImageDrawable(mIcon);
            imageView.setVisibility(iconVisibility);
        }
    }

    /**
     * Sets the icon for this Preference with a Drawable.
     *
     * @param icon The icon for this Preference
     */
    public void setIcon(Drawable icon, int visibility) {
        if ((icon == null && mIcon != null) || (icon != null && !icon.equals(mIcon))) {
            mIcon = icon;
            iconVisibility = visibility;
            notifyChanged();
        }
    }

    public void setIcon(Drawable icon) {
        setIcon(icon, View.VISIBLE);
    }

    /**
     * Returns the icon of this Preference.
     *
     * @return The icon.
     * @see #setIcon(Drawable)
     */
    public Drawable getIcon() {
        return mIcon;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
        notifyChanged();
    }
}
