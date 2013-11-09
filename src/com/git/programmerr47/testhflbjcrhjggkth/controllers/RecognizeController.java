package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.RecognizePageFragment;

public class RecognizeController implements IRecognizeController {
	private MicroScrobblerModel model;
    private RecognizePageFragment view;

    public RecognizeController(RecognizePageFragment view) {
            this.view = view;
            this.model = MicroScrobblerModel.getInstance();
    }
    
    public boolean recognizeByTimerRecognizeCancel() {
    	if(model.isRecognizing()) {
    		Log.v("Recognizing", "Cancel recognizeByTimer");
    		model.recognizeByTimerCancel();
    		return false;
    	} else {
    		Log.v("Recognizing", "recognizeByTimer");
    		model.recognizeByTimer();
    		return true;
    	}
    }
    
    public boolean recognizeNowRecognizeCancel() {
    	if(model.isRecognizing()) {
    		Log.v("Recognizing", "Cancel recognizeNow");
    		model.recognize();
    		return false;
    	} else {
    		Log.v("Recognizing", "recognizeNow");
    		model.recognizeCancel();
    		return true;
    	}
    }
}
