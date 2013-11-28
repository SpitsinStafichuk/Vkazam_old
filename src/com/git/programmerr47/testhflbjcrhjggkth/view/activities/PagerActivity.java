package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import java.lang.reflect.Field;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.view.DepthPageTransformer;
import com.git.programmerr47.testhflbjcrhjggkth.view.SmoothPageScroller;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class PagerActivity extends FragmentActivity{

    protected ViewPager pager;
    protected PagerAdapter pagerAdapter;
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.common_options_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        	case R.id.settings :
        		Log.v("Settings", "Creating settings activity");
            	Intent intent = new Intent(this, SettingsActivity.class);
            	startActivity(intent);
            	return true;
        	default :
        		return super.onOptionsItemSelected(item);
        }
	}

	
	protected void setupUi() {
		setContentView(R.layout.pager_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setPageTransformer(true, new DepthPageTransformer());
        
        try {
            Field mScroller;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			SmoothPageScroller scroller = new SmoothPageScroller(pager.getContext());
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
