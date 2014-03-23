package com.git.programmerr47.vkazam.view.activities;

import android.content.Intent;
import android.os.Bundle;

import com.git.programmerr47.vkazam.view.adapters.SongReplacePagerAdapter;

public class RefreshPagerActivity extends PagerActivity {

	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getIntent().getIntExtra("position", 0);
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

	@Override
	public Intent getSupportParentActivityIntent() {
		Intent intent = new Intent(this, SongInfoActivity.class);
		intent.putExtra("position", position);
		return intent;
	}
}
