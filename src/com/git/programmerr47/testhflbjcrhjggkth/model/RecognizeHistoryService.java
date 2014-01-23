package com.git.programmerr47.testhflbjcrhjggkth.model;

import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.MicrophonePagerActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class RecognizeHistoryService extends Service implements IRecognizeStatusObserver, ServiceConnection {
	
	private static final String TAG = "RecognizeHistoryService";
	private RecognizeManager recognizeManager;
	private volatile boolean isStarted = false;
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
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
		Log.i(TAG, "on fake startCommand just for test");
		stopSelf();
/*		if(!isStarted) {
			isStarted = true;
			Log.i(TAG, "onStartCommand");
			recognizeManager.addObserver(this);
			List<Data> fingerprints = recognizeManager.getFingerprints();
			if(!fingerprints.isEmpty()) {
				FingerprintData fingerprint = (FingerprintData) fingerprints.get(0);
				Log.i(TAG, "recognizing fingerprint date = " + fingerprint.getDate());
				recognizeManager.recognizeFingerprint(fingerprint, true);
			} else {
				stopSelf();
			}
		}*/
		return Service.START_REDELIVER_INTENT;
	}
	
	@Override 
    public void onDestroy() {
		unbindService(this);
        Toast.makeText(this, "RecognizeHistoryService onDestroy", Toast.LENGTH_LONG).show();
		isStarted = false;
	}

/*	@Override
	public void updateRecognizeStatus() {
		List<Data> fingerprints = recognizeManager.getFingerprints();
		if(!fingerprints.isEmpty()) {
			FingerprintData fingerprint = (FingerprintData) fingerprints.get(0);
			Log.i(TAG, "recognizing fingerprint date = " + fingerprint.getDate());
			recognizeManager.recognizeFingerprint(fingerprint, true);
		} else {
			stopSelf();
		}
	}*/

	@Override
	public void onRecognizeStatusChanged(String status) {
		// TODO Auto-generated method stub
		
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
