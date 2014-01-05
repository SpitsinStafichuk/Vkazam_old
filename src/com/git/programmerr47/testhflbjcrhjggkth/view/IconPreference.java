package com.git.programmerr47.testhflbjcrhjggkth.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.git.programmerr47.testhflbjcrhjggkth.R;

public class IconPreference extends EditTextPreference {

    private Drawable mIcon;
    private View preference;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.v("IconPreference", "!!!Showdialog!!!");
            //show dialog
        }
    };
    private int iconVisibility;

    public IconPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.icon_preference_with_additional_info);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.IconPreference, defStyle, 0);
        mIcon = a.getDrawable(R.styleable.IconPreference_icon);
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
