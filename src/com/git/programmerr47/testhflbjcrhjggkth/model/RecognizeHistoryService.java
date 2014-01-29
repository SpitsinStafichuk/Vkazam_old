package com.git.programmerr47.testhflbjcrhjggkth.model;

import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeListManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class RecognizeHistoryService extends Service implements IRecognizeStatusObserver, ServiceConnection {
	
	private static final String TAG = "RecognizeHistoryService";
	private volatile boolean isStarted = false;
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override 
    public void onCreate() { 
		Log.i(TAG, "onCreate");
		Toast toast = Toast.makeText(this, "RecognizeHistoryService onCreate", Toast.LENGTH_LONG);
		toast.show();
		if(!MicroScrobblerModel.hasContext()) {
			MicroScrobblerModel.setContext(this);
		}
		startService(new Intent(RecognizeHistoryService.this, RecognizeService.class));
		bindService(new Intent(RecognizeHistoryService.this, RecognizeService.class), this, Context.BIND_AUTO_CREATE);
	}
	
	@Override 
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");
		if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("settingsAutoRecognize", false) && !isStarted) {
			isStarted = true;
			Log.i(TAG, "recognizing");
			RecognizeServiceConnection.getModel().getRecognizeListManager().addRecognizeStatusObserver(this);
			RecognizeServiceConnection.getModel().getRecognizeListManager().recognizeFingerprints();
		}
		return Service.START_REDELIVER_INTENT;
	}
	
	@Override 
    public void onDestroy() {
		Log.i(TAG, "onDestroy");
		RecognizeServiceConnection.getModel().getRecognizeListManager().removeRecognizeStatusObserver(this);
		unbindService(this);
        Toast.makeText(this, "RecognizeHistoryService onDestroy", Toast.LENGTH_LONG).show();
		isStarted = false;
	}

	@Override
	public void onRecognizeStatusChanged(String status) {
		Log.i(TAG, "status");
		if(RecognizeListManager.ALL_RECOGNIZED.equals(status)) {
			Log.i(TAG, "stopSelf()");
			stopSelf();
		}
	}

	@Override
	public void onServiceConnected(ComponentName className, IBinder service) {
		Log.i(TAG, "onServiceConnected");
		RecognizeServiceConnection.setModel(((RecognizeService.RecognizeBinder) service).getService().getModel());
	}

	@Override
	public void onServiceDisconnected(ComponentName className) {
		Log.i(TAG, "onServiceDisconnectedFromService");
		RecognizeServiceConnection.setModel(null);
	}

}
