package com.git.programmerr47.vkazam.controllers;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.git.programmerr47.vkazam.model.FingerprintData;
import com.git.programmerr47.vkazam.model.MicroScrobblerModel;
import com.git.programmerr47.vkazam.model.RecognizeServiceConnection;
import com.git.programmerr47.vkazam.model.SongData;
import com.git.programmerr47.vkazam.model.managers.FingerprintManager;
import com.git.programmerr47.vkazam.model.managers.RecognizeManager;
import com.git.programmerr47.vkazam.model.observers.IFingerprintResultObserver;
import com.git.programmerr47.vkazam.model.observers.IRecognizeResultObserver;
import com.git.programmerr47.vkazam.utils.NetworkUtils;
import com.git.programmerr47.vkazam.view.ProgressWheel;

public class RecognizeController implements IFingerprintResultObserver, IRecognizeResultObserver {
	private static final String TAG = "RecognizeController";
	
	private MicroScrobblerModel model;
    private FingerprintManager fingerprintManager;
    private RecognizeManager recognizeManager;
    private Context context;
    private Timer timerDelay;
    private ProgressWheel progressWheel;

    public RecognizeController(Context context) {
        model = RecognizeServiceConnection.getModel();
        this.context = context;
        fingerprintManager = model.getFingerprintManager();
        fingerprintManager.addFingerprintResultObserver(this);
        recognizeManager = model.getMainRecognizeManager();
        recognizeManager.addRecognizeResultObserver(this);
        timerDelay = new Timer();
    }

    public void setProgressWheel(ProgressWheel progressWheel) {
        this.progressWheel = progressWheel;
    }

    public void finish() {
        fingerprintManager.removeFingerprintResultObserver(this);
        recognizeManager.removeRecognizeResultObserver(this);
    }
    
    public boolean fingerprintByTimerRecognizeCancel() {
    	if(fingerprintManager.isFingerprintingByTimer()) {
    		Log.v("Fingerprinting", "Cancel fingerprintByTimer");
            timerDelay.cancel();
            timerDelay = new Timer();
    		fingerprintManager.fingerprintCancel();
    		return false;
    	} else {
    		Log.v("Fingerprinting", "fingerprintByTimer");
            fingerprint();
    		return true;
    	}
    }
    
    public boolean fingerprintNowRecognizeCancel() {
        timerDelay.cancel();
    	if(fingerprintManager.isFingerprintingOneTime()) {
    		Log.v("Fingerprinting", "Cancel fingerprintNow");
    		fingerprintManager.fingerprintCancel();
    		return false;
    	} else {
    		Log.v("Fingerprinting", "fingerprintNow");
    		fingerprintManager.fingerprintOneTime();
    		return true;
    	}
    }

    public void fingerprint() {
        timerDelay.cancel();
        if (progressWheel != null) {
            progressWheel.resetCount();
        }
        fingerprintManager.fingerprintByTimer();
    }

	@Override
	public void onFingerprintResult(int errorCode, String fingerprint) {
		FingerprintData fingerprintData = new FingerprintData(fingerprint, new Date());
		Log.i(TAG, "fingerprint = " + fingerprint);
		if(errorCode == 0) {
			if (NetworkUtils.isNetworkAvailable(context)) {
				recognizeManager.recognizeFingerprint(fingerprintData);
			} else {
				Log.v("RecognizeController", "adding offline finger");
				model.getFingerprintList().add(fingerprintData);
				runTimerDelay();
			}
		}
	}

	@Override
	public void onRecognizeResult(int errorCode, SongData songData) {
		if (songData != null) {
			model.getSongList().add(0, songData);
			model.getScrobbler().sendLastFMTrack(songData.getArtist(), songData.getTitle(), songData.getAlbum());
		}
        runTimerDelay();
	}

    private void runTimerDelay() {
        if (fingerprintManager.isFingerprintingByTimer()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            int period = prefs.getInt("settingsTimerDelay", 5) * 1000 / 360;
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (progressWheel != null) {
                        progressWheel.incrementProgress();
                    }
                }
            };
            timerDelay = new Timer();
            timerDelay.scheduleAtFixedRate(task, 0, period);
        }
    }
}