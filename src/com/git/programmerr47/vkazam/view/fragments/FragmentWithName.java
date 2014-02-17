package com.git.programmerr47.vkazam.view.fragments;


import com.git.programmerr47.testhflbjcrhjggkth.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Log;

public class FragmentWithName extends Fragment{
	protected static final String ARG_NAME = "name_of_fragment";

	protected Context context;
	private String name;
	protected int icon = R.drawable.ic_action_mic;
	
	public String getFragmentName() {
		return name;
	}
	
	public Drawable getIcon() {
		return context.getResources().getDrawable(icon);
	}
	
	protected void setFragmentName(String name) {
		this.name = name.toUpperCase();
	}
	
	protected void setFragmentIcon(int icon) {
		this.icon = icon;
	}
	
	protected void setContext(Context context) {
		this.context = context;
	}
}
