package com.git.programmerr47.vkazam.view.activities;

import com.git.programmerr47.vkazam.view.adapters.SongReplacePagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import com.git.programmerr47.vkazam.view.adapters.SongReplacePagerAdapter;

public class RefreshPagerActivity extends PagerActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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