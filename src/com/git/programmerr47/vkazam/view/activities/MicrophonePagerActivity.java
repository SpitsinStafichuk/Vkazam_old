package com.git.programmerr47.vkazam.view.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.vkazam.model.RecognizeService;
import com.git.programmerr47.vkazam.model.RecognizeServiceConnection;
import com.git.programmerr47.vkazam.view.adapters.MicrophonePagerAdapter;
import com.git.programmerr47.vkazam.view.adapters.MyPagerAdapter;

public class MicrophonePagerActivity extends PagerActivity implements
		ServiceConnection {
	private static final String TAG = "MicrophonePagerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "Binding to service");
		startService(new Intent(MicrophonePagerActivity.this,
				RecognizeService.class));
		bindService(new Intent(MicrophonePagerActivity.this,
				RecognizeService.class), this, BIND_AUTO_CREATE);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected MyPagerAdapter getAdapter() {
		return new MicrophonePagerAdapter(getSupportFragmentManager(),
				getApplicationContext());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			Log.v("Settings", "Creating settings activity");
			Intent intent = new Intent(this, SettingsActivity.class);
			Intent parentIntent = new Intent(this,
					MicrophonePagerActivity.class);
			parentIntent.putExtra(PAGE_NUMBER, pager.getCurrentItem());
			intent.putExtra(SettingsActivity.PARENT_INTENT, parentIntent);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(this);
	}

	@Override
	public void onServiceConnected(ComponentName className, IBinder service) {
		Log.i(TAG, "onServiceConnected");
		RecognizeServiceConnection
				.setModel(((RecognizeService.RecognizeBinder) service)
						.getService().getModel());
		setupUi();
	}

	@Override
	public void onServiceDisconnected(ComponentName className) {
		Log.i(TAG, "onServiceDisconnectedFromService");
		RecognizeServiceConnection.setModel(null);
	}

}
