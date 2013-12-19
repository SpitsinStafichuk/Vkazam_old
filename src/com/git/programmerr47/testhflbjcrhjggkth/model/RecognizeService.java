package com.git.programmerr47.testhflbjcrhjggkth.model;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.NotificationController;

public class RecognizeService extends Service {
	
	private static final String TAG = "RecognizeService";
	private MicroScrobblerModel model;
    private NotificationController notificationHandler;
	
	private final IBinder binder = new RecognizeBinder();
	
	public class RecognizeBinder extends Binder {
		public RecognizeService getService() {
            return RecognizeService.this;
        }
    }

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override 
    public void onCreate() { 
		Log.i(TAG, "onCreate");
		Toast toast = Toast.makeText(this, "RecognizeService onCreate", Toast.LENGTH_LONG);
		toast.show();
		MicroScrobblerModel.setContext(this);
		model = MicroScrobblerModel.getInstance();
        notificationHandler = new NotificationController(model, this);
	}
	
	public MicroScrobblerModel getModel() {
		return model;
	}
	
	@Override 
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY;
	}
	
	@Override 
    public void onDestroy() {
        notificationHandler.finish();
	}


}
