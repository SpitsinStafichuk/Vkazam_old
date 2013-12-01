package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.git.programmerr47.testhflbjcrhjggkth.R;

public class FingerprintPageFragment extends FragmentWithName {

	public static FingerprintPageFragment newInstance(Context context) {
		FingerprintPageFragment pageFragment = new FingerprintPageFragment();
        Bundle arguments = new Bundle();
        pageFragment.setArguments(arguments);
        pageFragment.setFragmentName("Fingers");
        pageFragment.setFragmentIcon(R.drawable.ic_action_fingerprint);
        pageFragment.setContext(context);
        return pageFragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fingerprints_fragment, null);
            return view;
	}
}
