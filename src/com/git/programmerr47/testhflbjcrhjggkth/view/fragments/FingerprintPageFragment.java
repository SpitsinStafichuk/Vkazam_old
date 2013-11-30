package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.content.Context;
import android.os.Bundle;

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
}
