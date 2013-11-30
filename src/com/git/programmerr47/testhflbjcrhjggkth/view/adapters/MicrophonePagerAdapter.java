package com.git.programmerr47.testhflbjcrhjggkth.view.adapters;

import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.HistoryPageFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.RecognizePageFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.TestPageFragment;

import android.content.Context;
import android.support.v4.app.FragmentManager;

public class MicrophonePagerAdapter extends PagerAdapter {
   
    public MicrophonePagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            fragments.add(RecognizePageFragment.newInstance(context));
            fragments.add(HistoryPageFragment.newInstance(context));
            fragments.add(TestPageFragment.newInstance(context));
    }
}
