package com.git.programmerr47.testhflbjcrhjggkth.view.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.SongReplacePPFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.SongReplaceVkFragment;

public class SongReplacePagerAdapter extends PagerAdapter{
   
    public SongReplacePagerAdapter(FragmentManager fm, Context context) {
            super(fm, 2);
            fragments.add(SongReplacePPFragment.newInstance(context));
            fragments.add(SongReplaceVkFragment.newInstance(context));
    }
}
