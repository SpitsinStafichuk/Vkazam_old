package com.git.programmerr47.vkazam.view.activities;

import java.lang.reflect.Field;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.vkazam.view.DepthPageTransformer;
import com.git.programmerr47.vkazam.view.SmoothPageScroller;
import com.git.programmerr47.vkazam.view.adapters.PagerAdapter;

public class PagerActivity extends ActionBarActivity {
	public static final String PAGE_NUMBER = "page_number";

	protected ViewPager pager;
	protected PagerAdapter pagerAdapter;
	protected int initialPage = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager);
		Intent intent = getIntent();
		if (intent.hasExtra(PAGE_NUMBER)) {
			initialPage = intent.getExtras().getInt(PAGE_NUMBER, 0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.common_options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			Log.v("Settings", "Creating settings activity");
			Intent intent = new Intent(this, SettingsActivity.class);
			Intent parentIntent = getIntent();
			parentIntent.putExtra(PAGE_NUMBER, pager.getCurrentItem());
			intent.putExtra(SettingsActivity.PARENT_INTENT, parentIntent);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void setupUi() {
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setPageTransformer(true, new DepthPageTransformer());
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				pagerAdapter.setCurrentPage(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int status) {
				if ((status == ViewPager.SCROLL_STATE_SETTLING)
						|| (status == ViewPager.SCROLL_STATE_IDLE)) {
					pagerAdapter.setIsDragging(false);
					pagerAdapter.notifyDataSetChanged();
				} else if (status == ViewPager.SCROLL_STATE_DRAGGING) {
					if (!pagerAdapter.isDragging()) {
						pagerAdapter.setIsDragging(true);
						pagerAdapter.notifyDataSetChanged();
					}
				}
			}
		});

		try {
			Field mScroller;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			SmoothPageScroller scroller = new SmoothPageScroller(
					pager.getContext());
			mScroller.set(pager, scroller);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
