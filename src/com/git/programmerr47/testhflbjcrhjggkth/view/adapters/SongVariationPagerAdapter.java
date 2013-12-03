package com.git.programmerr47.testhflbjcrhjggkth.view.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.SongVariationFragment;

public class SongVariationPagerAdapter extends PagerAdapter{
   
    public SongVariationPagerAdapter(FragmentManager fm, Context context) {
            super(fm, 2);
            fragments.add(SongVariationFragment.newInstance("pleer", context, R.drawable.ic_action_prostopleer));
            fragments.add(SongVariationFragment.newInstance("vk", context, R.drawable.ic_action_vk));
    }
}
