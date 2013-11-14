package com.git.programmerr47.testhflbjcrhjggkth.model;


import java.util.List;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.IFingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class RecognizeService extends Service implements IRecognizeStatusObserver {
	
	private static final String TAG = "RecognizeService";
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
		Toast toast = Toast.makeText(this, "RecognizeService onCreate", Toast.LENGTH_LONG);
		toast.show();
		if(!MicroScrobblerModel.hasContext()) {
			MicroScrobblerModel.setContext(this);
		}
		recognizeManager = MicroScrobblerModel.getInstance().getRecognizeManager();
	}
	
	@Override 
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "on fake startCommand just for test");
		if(!isStarted) {
			isStarted = true;
			Log.i(TAG, "onStartCommand");
			recognizeManager.addObserver(this);
			List<IFingerprintData> fingerprints = recognizeManager.getFingerprints();
			if(!fingerprints.isEmpty()) {
				IFingerprintData fingerprint = fingerprints.get(0);
				Log.i(TAG, "recognizing fingerprint date = " + fingerprint.getDate());
				recognizeManager.recognizeFingerprint(fingerprint, true);
			} else {
				stopSelf();
			}
		}
		return Service.START_REDELIVER_INTENT;
	}
	
	@Override 
    public void onDestroy() {
		isStarted = false;
	}

	@Override
	public void updateRecognizeStatus() {
		List<IFingerprintData> fingerprints = recognizeManager.getFingerprints();
		if(!fingerprints.isEmpty()) {
			IFingerprintData fingerprint = fingerprints.get(0);
			Log.i(TAG, "recognizing fingerprint date = " + fingerprint.getDate());
			recognizeManager.recognizeFingerprint(fingerprint, true);
		} else {
			stopSelf();
		}
	}

}
