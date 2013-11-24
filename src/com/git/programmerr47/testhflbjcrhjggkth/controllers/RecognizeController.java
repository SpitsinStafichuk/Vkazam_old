package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import java.util.Date;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DBConstants;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongData.SongDataBuilder;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.FingerprintManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintResultObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintStatusObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeResultObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;

public class RecognizeController implements IFingerprintResultObserver, IRecognizeResultObserver {
	private static final String TAG = "RecognizeController";
	
	private MicroScrobblerModel model;
    private FingerprintManager fingerprintManager;
    RecognizeManager recognizeManager;

    public RecognizeController() {
            model = MicroScrobblerModel.getInstance();
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
		FingerprintData fingerprintData = new FingerprintData.FingerprintDataBuilder()
															.setDate((new Date()).getTime())
															.setFingerprint(fingerprint)
															.build();
		Log.i(TAG, "fingerprint = " + fingerprint);
        recognizeManager.recognizeFingerprint(fingerprintData, false);
	}

	@Override
	public void onRecognizeResult(SongDataBuilder builder) {
		if (builder != null) {
			model.getSongList().add(builder);
		}
	}
}
