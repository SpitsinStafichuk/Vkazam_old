package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.FingerprintManager;

public class RecognizeController {
	private MicroScrobblerModel model;
    private FingerprintManager fingerprintManager;

    public RecognizeController() {
            this.model = MicroScrobblerModel.getInstance();
            fingerprintManager = model.getFingerprintManager();
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
}
