package com.git.programmerr47.testhflbjcrhjggkth.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.IScrobbler;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.ISignInObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.Scrobbler;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.Scrobbler.IOnSignInResultListener;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.gracenote.mmid.MobileSDK.GNOperations;
import com.gracenote.mmid.MobileSDK.GNSearchResponse;
import com.gracenote.mmid.MobileSDK.GNSearchResult;
import com.gracenote.mmid.MobileSDK.GNSearchResultReady;

public class MicroScrobblerModel implements IMicroScrobblerModel, ISignInObservable, IOnSignInResultListener, GNSearchResultReady, IRecognizeStatusObservable {
	private static final String SAVE_LASTFM_INFO_PREF = "save lastfm info pref";
	private static final String LASTFM_USERNAME = "lastfm username";
	private static final String LASTFM_PASSWORD = "lastfm password";
	private static final int MODE = Activity.MODE_PRIVATE;
	public static final String RECOGNIZING_SUCCESS = "Success";
	private static final int DEFAULT_RECOGNIZE_TIMER_PERIOD = 10 * 1000;
	
	private static MicroScrobblerModel instance;
	private static Context context;
	
	private Scrobbler scrobbler;
	private SharedPreferences sharedPreferences;
	
	private Set<IOnSignInResultListener> listeners;
	
	private GNConfig config;
	private String recognizeStatus;
	private Set<IRecognizeStatusObserver> recognizeStatusObservers;
	private volatile boolean isRecognizing = false;
	private int recognizeTimerPeriod;
	private ScheduledThreadPoolExecutor recognizeTimer;
	
	private String artist;
	private String title;
	
	public static void setContext(Context con) {
		context = con;
	}
	
	public static synchronized MicroScrobblerModel getInstance() {
		if (instance == null) {
			instance = new MicroScrobblerModel();
		}
		
		return instance;
	}
	
	private MicroScrobblerModel() {
		listeners = new HashSet<IOnSignInResultListener>();
		scrobbler = new Scrobbler();
		config = GNConfig.init("5435392-85C21DCCC8BBE15A8B5EE2BDC8A9ACDC", context);
		recognizeStatusObservers = new HashSet<IRecognizeStatusObserver>();
		recognizeTimerPeriod = DEFAULT_RECOGNIZE_TIMER_PERIOD;
		isRecognizing = false;
		sharedPreferences = context.getSharedPreferences(SAVE_LASTFM_INFO_PREF, MODE);
        final String login = sharedPreferences.getString(LASTFM_USERNAME, null);
        final String password = sharedPreferences.getString(LASTFM_PASSWORD, null);
        
        setLastfmAccount(login, password);
	}
	
	@Override
	public void setLastfmAccount(String username, String password) {
        scrobbler.signIn(username, password);
        scrobbler.setOnSignInResultListener(this);
	}

	@Override
	public IScrobbler getScrobbler() {
		return scrobbler;
	}
	
	public void recognizeByTimer() {
		recognizeTimer = new ScheduledThreadPoolExecutor(1);
		recognizeTimer.scheduleWithFixedDelay(new Thread() {

			@Override
			public void run() {
				recognize();
			}
			
		}, 0, recognizeTimerPeriod, TimeUnit.MILLISECONDS);
	}
	
	public void recognize() {
		if (!isRecognizing) {
			synchronized (this) {
				if (!isRecognizing) {
					isRecognizing = true;
					GNOperations.recognizeMIDStreamFromMic(this, config);
				}
			}
		}
	}
	
	public void recognizeCancel() {
		isRecognizing = false;
		GNOperations.cancel(this);
	}
	
	public void recognizeByTimerCancel() {
		isRecognizing = false;
		recognizeTimer.shutdownNow();
		recognizeCancel();
	}
	
	public boolean isRecognizing() {
		return isRecognizing;
	}

	@Override
	public List getHistory() {
		// TODO Auto-generated method stub
		// test
		return null;
	}

	@Override
    public void setOnSignInResultListener(IOnSignInResultListener listener) {
    	listeners.add(listener);
    }
    
    @Override
    public void removeOnSignInResultListener(IOnSignInResultListener listener) {
    	listeners.remove(listener);
    }
    
    @Override
    public void notifyOnSignInResultListener(String status) {
    	for(IOnSignInResultListener listener : listeners) {
    		listener.onResult(status);
    	}
    }

	@Override
	public void onResult(String status) {
		isRecognizing = false;
		if(status.equals(STATUS_SUCCESS)) {
			SharedPreferences.Editor editor = sharedPreferences.edit();
	        editor.putString(LASTFM_USERNAME, scrobbler.getCorrectUsername());
	        editor.putString(LASTFM_PASSWORD, scrobbler.getCorrectPassword());
	        editor.commit();
		}
		notifyOnSignInResultListener(status);
	}

	@Override
	public void GNResultReady(GNSearchResult result) {
		if (result.isFailure()) {
			recognizeStatus = String.format("[%d] %s", result.getErrCode(),
					result.getErrMessage());
		} else {
			if (result.isFingerprintSearchNoMatchStatus()) {
				recognizeStatus = "Music Not Identified";
			} else {
				GNSearchResponse bestResponse = result.getBestResponse();

				artist = bestResponse.getArtist();
				title = bestResponse.getTrackTitle();
		
				recognizeStatus = RECOGNIZING_SUCCESS;
				Log.i("Recognizing", recognizeStatus);
				Log.i("Recognizing", artist + " " + title);
			}
		}
		
		notifyRecognizeStatusObservers();
		
	}
	
	public String getRecognizeStatus() {
		return recognizeStatus;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getTitle() {
		return title;
	}
	
	@Override
	public void addObserver(IRecognizeStatusObserver o) {
		recognizeStatusObservers.add(o);
	}

	@Override
	public void removeObserver(IRecognizeStatusObserver o) {
		recognizeStatusObservers.remove(o);
	}

	@Override
	public void notifyRecognizeStatusObservers() {
		for(IRecognizeStatusObserver o : recognizeStatusObservers)
			o.updateRecognizeStatus();
	}
}
