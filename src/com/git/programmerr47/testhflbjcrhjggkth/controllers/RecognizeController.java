package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import java.util.Date;

import android.content.Context;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.FingerprintManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintResultObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeResultObserver;
import com.git.programmerr47.testhflbjcrhjggkth.utils.NetworkUtils;

public class RecognizeController implements IFingerprintResultObserver, IRecognizeResultObserver {
	private static final String TAG = "RecognizeController";
	
	private MicroScrobblerModel model;
    private FingerprintManager fingerprintManager;
    private RecognizeManager recognizeManager;
    private Context context;

    public RecognizeController(Context context) {
            model = RecognizeServiceConnection.getModel();
            this.context = context;
            fingerprintManager = model.getFingerprintManager();
            fingerprintManager.addObserver(this);
            recognizeManager = model.getRecognizeManager();
            recognizeManager.addObserver(this);
    }
    
    public boolean fingerprintByTimerRecognizeCancel() {
    	if(fingerprintManager.isFingerprintingByTimer()) {
    		Log.v("Fingerprinting", "Cancel fingerprintByTimer");
    		fingerprintManager.fingerprintByTimerCancel();
    		return false;
    	} else {
    		Log.v("Fingerprinting", "fingerprintByTimer");
    		fingerprintManager.fingerprintByTimer();
    		return true;
    	}
    }
    
    public boolean fingerprintNowRecognizeCancel() {
    	if(fingerprintManager.isFingerprintingOneTime()) {
    		Log.v("Fingerprinting", "Cancel fingerprintNow");
    		fingerprintManager.fingerprintOneTimeCancel();
    		return false;
    	} else {
    		Log.v("Fingerprinting", "fingerprintNow");
    		fingerprintManager.fingerprintOneTime();
    		return true;
    	}
    }


	@Override
	public void onFingerprintResult(String fingerprint) {
		FingerprintData fingerprintData = new FingerprintData(fingerprint, new Date());
		Log.i(TAG, "fingerprint = " + fingerprint);
		if (NetworkUtils.isNetworkAvailable(context)) {
	        recognizeManager.recognizeFingerprint(fingerprintData, false);
		} else {
			Log.v("testik", "adding offline finger");
			model.getFingerprintList().add(fingerprintData);
		}
	}

	@Override
	public void onRecognizeResult(SongData builder) {
		if (builder != null) {
			model.getSongList().add(0, builder);
		}
	}
}
