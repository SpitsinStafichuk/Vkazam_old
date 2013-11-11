package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongDAO;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.ISongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.Scrobbler;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;
import com.gracenote.mmid.MobileSDK.GNConfig;
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

public class RecognizeManager implements IRecognizeStatusObservable, GNSearchResultReady, GNOperationStatusChanged {
	private static final int DEFAULT_RECOGNIZE_TIMER_PERIOD = 10 * 1000;
	private static final String GRACENOTE_APPLICATION_ID = "5435392-85C21DCCC8BBE15A8B5EE2BDC8A9ACDC";
	public static final String RECOGNIZING_SUCCESS = "Success";
	
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
	
	private SongDAO songDAO;
	private List<ISongData> songList;
	
	public RecognizeManager(Context context, Scrobbler scrobbler) {
		config = GNConfig.init(GRACENOTE_APPLICATION_ID, context);
		recognizeStatusObservers = new HashSet<IRecognizeStatusObserver>();
		recognizeTimerPeriod = DEFAULT_RECOGNIZE_TIMER_PERIOD;
		isRecognizing = false;
		isRecognizingByTimer = false;
		isRecognizingOneTime = false;
		this.scrobbler = scrobbler;
		songDAO = new SongDAO(context);
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
					GNOperations.recognizeMIDStreamFromMic(this, config);
				}
			}
		}
	}
	
	
	private void recognizeCancel() {
		GNOperations.cancel(this);
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
	
	@Override
	public void GNStatusChanged(GNStatus status) {
		recognizeStatus = status.getMessage() + " " + status.getPercentDone() + " %";
		notifyRecognizeStatusObservers();
	}
	
	@Override
	public void GNResultReady(GNSearchResult result) {
		Log.i("Recognizing", "GNResultReady");
		isRecognizing = false;
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
				if (bestResponse.getContributorImage() != null) {
					byte[] coverArtArray = bestResponse.getContributorImage().getData();
					coverArt = BitmapFactory.decodeByteArray(coverArtArray , 0, coverArtArray.length);
				} else {
					coverArt = null;
				}
				
				if(!artist.equals(previousArtist) || !title.equals(previousTitle)) {
					if(scrobbler != null) {
						scrobbler.scrobble(artist, title);
					} else {
						Log.w("Scrobbling", "scrobbler == null");
					}
					
					ISongData songInfo = new SongData(-1, artist, title, bestResponse.getTrackId(), Calendar.getInstance().getTime().toString(), null);
					songList.add(songInfo);
					songDAO.insert(songInfo);
					
					previousArtist = artist;
					previousTitle = title;
				}
		
				recognizeStatus = RECOGNIZING_SUCCESS;
				Log.i("Recognizing", recognizeStatus);
				Log.i("Recognizing", artist + " " + title);
			}
		}
		
		notifyRecognizeStatusObservers();
		
		
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
