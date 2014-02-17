package com.git.programmerr47.vkazam.view.activities;

import com.git.programmerr47.vkazam.model.RecognizeService;
import com.git.programmerr47.vkazam.model.RecognizeServiceConnection;
import com.git.programmerr47.vkazam.view.adapters.MicrophonePagerAdapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.git.programmerr47.vkazam.model.RecognizeServiceConnection;
import com.git.programmerr47.vkazam.view.adapters.MicrophonePagerAdapter;

public class MicrophonePagerActivity extends PagerActivity implements ServiceConnection{
	private static final String TAG = "myLogs";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "Binding to service");
		startService(new Intent(MicrophonePagerActivity.this, RecognizeService.class));
		bindService(new Intent(MicrophonePagerActivity.this, RecognizeService.class), this, BIND_AUTO_CREATE);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void setupUi() {
		super.setupUi();
        pagerAdapter = new MicrophonePagerAdapter(getSupportFragmentManager(), getApplicationContext());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(initialPage);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(this);
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
