package com.github.spitsinstafichuk.vkazam.adapters_old;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.github.spitsinstafichuk.vkazam.model.RecognizeServiceConnection;
import com.github.spitsinstafichuk.vkazam.view.fragments.SongReplacePPFragment;
import com.github.spitsinstafichuk.vkazam.view.fragments.SongReplaceVkFragment;

@Deprecated
public class SongReplacePagerAdapter extends MyPagerAdapter {
	public static final int VK_PAGE_NUMBER = 1;

	public SongReplacePagerAdapter(int position, FragmentManager fm,
			Context context) {
		super(fm, 1);
		fragments.add(SongReplacePPFragment.newInstance(position));
		if (RecognizeServiceConnection.getModel().getVkApi() != null) {
			setPageCount(2);
			fragments.add(SongReplaceVkFragment.newInstance(position));
		}
	}
}
