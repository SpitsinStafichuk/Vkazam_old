package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeService;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.MicrophonePagerAdapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MicrophonePagerActivity extends FragmentActivity implements ServiceConnection{

	private static final String TAG = "myLogs";

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//MicroScrobblerModel.setContext(getApplicationContext());
		//MicroScrobblerModel.getInstance();
		Log.v(TAG, "Binding to service");
		bindService(new Intent(MicrophonePagerActivity.this, RecognizeService.class), this, Context.BIND_AUTO_CREATE);
		super.onCreate(savedInstanceState);
	}
	
	private void setupUi() {
		setContentView(R.layout.microphone_pager_layout);
        pager = (ViewPager) findViewById(R.id.microphonePager);
        pagerAdapter = new MicrophonePagerAdapter(getSupportFragmentManager());
        pager.setPageTransformer(true, new DepthPageTransformer());
        pager.setAdapter(pagerAdapter);

        pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d("MicrophonePager", "onPageSelected, position = " + position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(this);
		Log.i(TAG, "onServiceDisconnected");
		//RecognizeServiceConnection.setModel(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.microphone_pager, menu);
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

	@Override
	public void onServiceConnected(ComponentName className, IBinder service) {
		Log.i(TAG, "onServiceConnected");
		RecognizeServiceConnection.setModel(((RecognizeService.RecognizeBinder) service).getService().getModel());
		setupUi();
	}

	@Override
	public void onServiceDisconnected(ComponentName className) {
		Log.i(TAG, "onServiceDisconnectedFromService");
		RecognizeServiceConnection.setModel(null);
	}
	
}
