package com.git.programmerr47.testhflbjcrhjggkth.model;

import java.util.Iterator;
import java.util.List;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.IFingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class RecognizeService extends Service implements IRecognizeStatusObserver {
	
	private static final String TAG = "RecognizeService";
	RecognizeManager recognizeManager;
	Iterator<IFingerprintData> fingerprintsIterator;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override 
    public void onCreate() { 
		Log.i(TAG, "onCreate");
		if(!MicroScrobblerModel.hasContext()) {
			MicroScrobblerModel.setContext(this);
		}
		recognizeManager = MicroScrobblerModel.getInstance().getRecognizeManager();
	}
	
	@Override 
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");
		recognizeManager.addObserver(this);
		List<IFingerprintData> fingerprints = recognizeManager.getFingerprints();
		fingerprintsIterator = fingerprints.iterator();
		if(fingerprintsIterator.hasNext()) {
			IFingerprintData fingerprint = fingerprintsIterator.next();
			Log.i(TAG, "recognizing fingerprint date = " + fingerprint.getDate());
			recognizeManager.recognizeFingerprint(fingerprint, true);
		} else {
			stopSelf();
		}
		return Service.START_REDELIVER_INTENT;
	}
	
	@Override 
    public void onDestroy() {
		
	}

	@Override
	public void updateRecognizeStatus() {
		if(fingerprintsIterator.hasNext()) {
			IFingerprintData fingerprint = fingerprintsIterator.next();
			Log.i(TAG, "recognizing fingerprint date = " + fingerprint.getDate());
			recognizeManager.recognizeFingerprint(fingerprint, true);
		} else {
			stopSelf();
		}
	}

}
