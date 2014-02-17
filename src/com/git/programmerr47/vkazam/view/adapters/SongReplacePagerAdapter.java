package com.git.programmerr47.vkazam.view.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.git.programmerr47.vkazam.model.RecognizeServiceConnection;
import com.git.programmerr47.vkazam.view.fragments.SongReplacePPFragment;
import com.git.programmerr47.vkazam.view.fragments.SongReplaceVkFragment;

public class SongReplacePagerAdapter extends PagerAdapter{
	public static final int VK_PAGE_NUMBER = 1;
   
    public SongReplacePagerAdapter(FragmentManager fm, Context context) {
        super(fm, 1);
        fragments.add(SongReplacePPFragment.newInstance(context));
        if (RecognizeServiceConnection.getModel().getVkApi() != null) {
            setPageCount(2);
            fragments.add(SongReplaceVkFragment.newInstance(context));
        }
    }
}
