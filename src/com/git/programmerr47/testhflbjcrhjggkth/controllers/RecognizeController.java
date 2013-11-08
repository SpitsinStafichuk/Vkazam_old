package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.RecognizePageFragment;

public class RecognizeController {
	private MicroScrobblerModel model;
    private RecognizePageFragment view;

    public RecognizeController(RecognizePageFragment view) {
            this.view = view;
            this.model = MicroScrobblerModel.getInstance();
    }
    
    public boolean recognizeRecognizeCancel() {
    	if(model.isRecognizing()) {
    		model.recognizeByTimerCancel();
    		return false;
    	} else {
    		Log.v("Recognizing", "recognizeByTimer");
    		model.recognizeByTimer();
    		return true;
    	}
    }
}
