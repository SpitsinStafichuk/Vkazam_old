package com.git.programmerr47.testhflbjcrhjggkth.model;

import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RecognizeService extends Service {
	
	RecognizeManager recognizeManager;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override 
    public void onCreate() { 
		if(!MicroScrobblerModel.hasContext()) {
			MicroScrobblerModel.setContext(this);
		}
		recognizeManager = MicroScrobblerModel.getInstance().getRecognizeManager();
	}
	
	@Override 
    public int onStartCommand(Intent intent, int flags, int startId) {
		
		return Service.START_NOT_STICKY;
	}
	
	@Override 
    public void onDestroy() {
		
	}

}
