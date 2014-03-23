package com.git.programmerr47.vkazam.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.vkazam.model.RecognizeServiceConnection;
import com.git.programmerr47.vkazam.view.fragments.SongInfoFragment;

public class SongInfoActivity extends ActionBarActivity {
	public static final String TAG = "SongInfoActivity";
	public static final String ARGUMENT_SONGLIST_POSITION = "SongDataPosition";

	private int position;
	private MyAdapter adapter;
	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		position = getIntent().getIntExtra("position", 0);
		Log.i(TAG, "position: " + position);
		adapter = new MyAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		pager.setCurrentItem(position);
	}

	public static class MyAdapter extends FragmentStatePagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return RecognizeServiceConnection.getModel().getSongList().size();
		}

		@Override
		public Fragment getItem(int position) {
			return SongInfoFragment.newInstance(position);
		}
	}

	@Override
	public Intent getSupportParentActivityIntent() {
		Intent intent = new Intent(this, MicrophonePagerActivity.class);
		Log.i(TAG, "getSupportParentActivityIntent");
		intent.putExtra(MicrophonePagerActivity.PAGE_NUMBER, 1);
		return intent;
	}

}
