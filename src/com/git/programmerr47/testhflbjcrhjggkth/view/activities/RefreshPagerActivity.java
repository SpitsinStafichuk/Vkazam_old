package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.SongReplacePagerAdapter;

import android.content.Intent;
import android.os.Bundle;

public class RefreshPagerActivity extends PagerActivity{
	public static final String VK_KEY = "vk";
	private int initialPage = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent.hasExtra(VK_KEY)) {
			initialPage = intent.getExtras().getInt(VK_KEY, 0);
		}
		setupUi();
	}
	
	@Override
	protected void setupUi() {
		super.setupUi();
        pagerAdapter = new SongReplacePagerAdapter(getSupportFragmentManager(), getApplicationContext());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(initialPage);
	}
}
