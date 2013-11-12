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
    
    public boolean recognizeByTimerRecognizeCancel() {
    	if(fingerprintManager.isRecognizingByTimer()) {
    		Log.v("Recognizing", "Cancel recognizeByTimer");
    		fingerprintManager.recognizeByTimerCancel();
    		return false;
    	} else {
    		Log.v("Recognizing", "recognizeByTimer");
    		fingerprintManager.recognizeByTimer();
    		return true;
    	}
    }
    
    public boolean recognizeNowRecognizeCancel() {
    	if(fingerprintManager.isRecognizingOneTime()) {
    		Log.v("Recognizing", "Cancel recognizeNow");
    		fingerprintManager.recognizeOneTimeCancel();
    		return false;
    	} else {
    		Log.v("Recognizing", "recognizeNow");
    		fingerprintManager.recognizeOneTime();
    		return true;
    	}
    }
}
