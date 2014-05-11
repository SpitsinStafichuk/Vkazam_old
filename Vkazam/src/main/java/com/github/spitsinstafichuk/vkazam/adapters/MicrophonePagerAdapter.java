package com.github.spitsinstafichuk.vkazam.adapters;


import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.github.spitsinstafichuk.vkazam.view.fragments.RecognizePageFragment;

public class MicrophonePagerAdapter extends MyPagerAdapter {
    public static final int HISTORY_PAGE_NUMBER = 1;
   
    public MicrophonePagerAdapter(FragmentManager fm, Context context) {
            super(fm, 1);
            fragments.add(RecognizePageFragment.newInstance(context));
            //fragments.add(HistoryPageFragment.newInstance(context));
            //fragments.add(FingerprintPageFragment.newInstance(context));
            //fragments.add(TestPageFragment.newInstance(context));
    }
}
