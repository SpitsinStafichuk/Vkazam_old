package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.SongVariationPagerAdapter;

import android.os.Bundle;

public class RefreshPagerActivity extends PagerActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupUi();
	}
	
	@Override
	protected void setupUi() {
		super.setupUi();
        pagerAdapter = new SongVariationPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
	}
}
