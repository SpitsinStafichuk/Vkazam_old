package com.git.programmerr47.vkazam.view.activities;

import android.os.Bundle;

import com.git.programmerr47.vkazam.view.adapters.SongReplacePagerAdapter;

public class RefreshPagerActivity extends PagerActivity {

	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getIntent().getExtras().getInt("position");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setupUi();
	}

	@Override
	protected void setupUi() {
		super.setupUi();
		pagerAdapter = new SongReplacePagerAdapter(position,
				getSupportFragmentManager(), getApplicationContext());
		pager.setAdapter(pagerAdapter);
		pager.setCurrentItem(initialPage);
	}
}
