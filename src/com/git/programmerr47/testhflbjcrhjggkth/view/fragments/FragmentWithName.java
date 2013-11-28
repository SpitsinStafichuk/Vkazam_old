package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.support.v4.app.Fragment;

public class FragmentWithName extends Fragment{
	protected static final String ARG_NAME = "name_of_fragment";

	protected String name;
	
	public String getFragmentName() {
		return name;
	}
	
	protected void setFragmentName(String name) {
		this.name = name;
	}
}
