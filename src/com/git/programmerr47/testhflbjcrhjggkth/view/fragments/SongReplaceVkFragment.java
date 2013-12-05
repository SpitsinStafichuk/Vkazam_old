package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import com.git.programmerr47.testhflbjcrhjggkth.R;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SongReplaceVkFragment extends FragmentWithName{
	
	public static SongReplaceVkFragment newInstance(Context context) {
		SongReplaceVkFragment pageFragment = new SongReplaceVkFragment();
        Bundle arguments = new Bundle();
        pageFragment.setArguments(arguments);
        pageFragment.setContext(context);
        pageFragment.setFragmentName("vk");
        pageFragment.setFragmentIcon(R.drawable.ic_action_vk);
        return pageFragment;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_fragment, null);
        return view;
    }
}
