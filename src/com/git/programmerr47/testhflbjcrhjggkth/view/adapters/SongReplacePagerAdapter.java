package com.git.programmerr47.testhflbjcrhjggkth.view.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.SongReplacePPFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.SongReplaceVkFragment;

public class SongReplacePagerAdapter extends PagerAdapter{
   
    public SongReplacePagerAdapter(FragmentManager fm, Context context) {
            super(fm, 2);
            fragments.add(SongReplacePPFragment.newInstance("pleer", context, R.drawable.ic_action_prostopleer));
            fragments.add(SongReplaceVkFragment.newInstance("vk", context, R.drawable.ic_action_vk));
    }
}
