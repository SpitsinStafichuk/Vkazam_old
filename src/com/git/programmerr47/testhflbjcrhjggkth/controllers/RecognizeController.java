package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.FingerprintManager;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.RecognizePageFragment;

public class RecognizeController implements IRecognizeController {
	private MicroScrobblerModel model;
    private RecognizePageFragment view;
    private FingerprintManager recognizeManager;

    public RecognizeController(RecognizePageFragment view) {
            this.view = view;
            this.model = MicroScrobblerModel.getInstance();
            recognizeManager = model.getRecognizeManager();
    }
    
    public boolean recognizeByTimerRecognizeCancel() {
    	if(recognizeManager.isRecognizingByTimer()) {
    		Log.v("Recognizing", "Cancel recognizeByTimer");
    		recognizeManager.recognizeByTimerCancel();
    		return false;
    	} else {
    		Log.v("Recognizing", "recognizeByTimer");
    		recognizeManager.recognizeByTimer();
    		return true;
    	}
    }
    
    public boolean recognizeNowRecognizeCancel() {
    	if(recognizeManager.isRecognizingOneTime()) {
    		Log.v("Recognizing", "Cancel recognizeNow");
    		recognizeManager.recognizeOneTimeCancel();
    		return false;
    	} else {
    		Log.v("Recognizing", "recognizeNow");
    		recognizeManager.recognizeOneTime();
    		return true;
    	}
    }
}
