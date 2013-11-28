package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeService;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.MicrophonePagerAdapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MicrophonePagerActivity extends PagerActivity implements ServiceConnection{

	private static final String TAG = "myLogs";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "Binding to service");
		bindService(new Intent(MicrophonePagerActivity.this, RecognizeService.class), this, Context.BIND_AUTO_CREATE);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void setupUi() {
		super.setupUi();
        pagerAdapter = new MicrophonePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(this);
		Log.i(TAG, "onServiceDisconnected");
		//RecognizeServiceConnection.setModel(null);
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
