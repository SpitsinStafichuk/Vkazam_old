package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.FingerprintDAO;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongDAO;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.ISongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.Scrobbler;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.gracenote.mmid.MobileSDK.GNFingerprintResult;
import com.gracenote.mmid.MobileSDK.GNFingerprintResultReady;
import com.gracenote.mmid.MobileSDK.GNOperationStatusChanged;
import com.gracenote.mmid.MobileSDK.GNOperations;
import com.gracenote.mmid.MobileSDK.GNSearchResponse;
import com.gracenote.mmid.MobileSDK.GNSearchResult;
import com.gracenote.mmid.MobileSDK.GNSearchResultReady;
import com.gracenote.mmid.MobileSDK.GNStatus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class FingerprintManager implements IRecognizeStatusObservable, GNOperationStatusChanged, GNFingerprintResultReady {
	private static final int DEFAULT_RECOGNIZE_TIMER_PERIOD = 10 * 1000;
	public static final String RECOGNIZING_SUCCESS = "Success";
	private static final int GRACENOTE_ERROR_CODE_NO_INTERNET_CONNECTION = 5001;
	
	private GNConfig config;
	private Set<IRecognizeStatusObserver> recognizeStatusObservers;
	private int recognizeTimerPeriod;
	private ScheduledThreadPoolExecutor recognizeTimer;
	private volatile boolean isRecognizing;
	private boolean isRecognizingByTimer;
	private boolean isRecognizingOneTime;
	private String recognizeStatus;
	
	private String artist;
	private String title;
	private Bitmap coverArt;
	
	private Scrobbler scrobbler;
	
	private String previousArtist;
	private String previousTitle;
	
	private String fingerprint;
	
	private SongDAO songDAO;
	private FingerprintDAO fingerprintDAO;
	private List<ISongData> songList;
	
	public FingerprintManager(GNConfig config, Context context, Scrobbler scrobbler) {
		recognizeStatusObservers = new HashSet<IRecognizeStatusObserver>();
		recognizeTimerPeriod = DEFAULT_RECOGNIZE_TIMER_PERIOD;
		isRecognizing = false;
		isRecognizingByTimer = false;
		isRecognizingOneTime = false;
		this.config = config;
		this.scrobbler = scrobbler;
		//возможны проблемы с одновременным обращением к базе данных
		songDAO = new SongDAO(context);
		fingerprintDAO = new FingerprintDAO(context);
        songList = songDAO.getHistory();
        if (songList != null && songList.size() > 0) {
			previousArtist = songList.get(songList.size() - 1).getArtist();
			previousTitle = songList.get(songList.size() - 1).getTitle();
		} else {
			previousArtist = null;
			previousTitle = null;
		}
	}
	
	public List<ISongData> getHistory() {
		return songList;
	}
	
	public String getRecognizeStatus() {
		return recognizeStatus;
	}
	
	public void recognizeByTimer() {
		isRecognizingByTimer = true;
		recognizeTimer = new ScheduledThreadPoolExecutor(1);
		recognizeTimer.scheduleWithFixedDelay(new Thread() {

			@Override
			public void run() {
				recognize();
			}
			
		}, 0, recognizeTimerPeriod, TimeUnit.MILLISECONDS);
	}
	
	public void recognizeByTimerCancel() {
		recognizeTimer.shutdownNow();
		recognizeCancel();
		isRecognizingByTimer = false;
	}
	
	public void recognizeOneTime() {
		isRecognizingOneTime = true;
		recognize();
	}
	
	public void recognizeOneTimeCancel() {
		recognizeCancel();
		isRecognizingOneTime = false;
	}
	
	private void recognize() {
		if (!isRecognizing) {
			synchronized (this) {
				if (!isRecognizing) {
					isRecognizing = true;
					Log.v("Recognizing", "New recognize by Timer");
					GNOperations.fingerprintMIDStreamFromMic(this, config);
				}
			}
		}
	}
	
	
	private void recognizeCancel() {
		GNOperations.cancel((GNSearchResultReady)this);
		GNOperations.cancel((GNFingerprintResultReady)this);
		isRecognizing = false;
	}
	
	public boolean isRecognizingOneTime() {
		return isRecognizingOneTime;
	}
	
	public boolean isRecognizingByTimer() {
		return isRecognizingByTimer;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Bitmap getCoverArt() {
		return coverArt;
	}
	
	public void setRecognizeStatus(String status) {
		recognizeStatus = status;
		notifyRecognizeStatusObservers();
	}
	
	@Override
	public void GNStatusChanged(GNStatus status) {
		recognizeStatus = status.getMessage() + " " + status.getPercentDone() + " %";
		notifyRecognizeStatusObservers();
	}
	
	@Override
	public void GNResultReady(GNFingerprintResult result) {
		fingerprint = result.getFingerprintData();
		Log.v("Recognizing", "fingerprint = " + fingerprint);
		//GNOperations.searchByFingerprint(this, config, fingerprint);
		MicroScrobblerModel.getInstance().getRecognizeManager().recognizeFingerprint(fingerprint);
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
