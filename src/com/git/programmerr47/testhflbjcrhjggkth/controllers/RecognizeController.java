package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.FingerprintManager;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.RecognizePageFragment;

public class RecognizeController implements IRecognizeController {
	private MicroScrobblerModel model;
    private RecognizePageFragment view;
    private FingerprintManager fingerprintManager;

    public RecognizeController(RecognizePageFragment view) {
            this.view = view;
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
