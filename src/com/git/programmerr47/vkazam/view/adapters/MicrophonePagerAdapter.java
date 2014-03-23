package com.git.programmerr47.vkazam.view.adapters;

import com.git.programmerr47.vkazam.view.fragments.FingerprintPageFragment;
import com.git.programmerr47.vkazam.view.fragments.HistoryPageFragment;
import com.git.programmerr47.vkazam.view.fragments.RecognizePageFragment;
import com.git.programmerr47.vkazam.view.fragments.TestPageFragment;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import com.git.programmerr47.vkazam.view.fragments.RecognizePageFragment;

public class MicrophonePagerAdapter extends MyPagerAdapter {
    public static final int HISTORY_PAGE_NUMBER = 1;
   
    public MicrophonePagerAdapter(FragmentManager fm, Context context) {
            super(fm, 4);
            fragments.add(RecognizePageFragment.newInstance(context));
            fragments.add(HistoryPageFragment.newInstance(context));
            fragments.add(FingerprintPageFragment.newInstance(context));
            fragments.add(TestPageFragment.newInstance(context));
    }
}
