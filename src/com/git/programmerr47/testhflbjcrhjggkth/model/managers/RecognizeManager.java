package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.FingerprintDAO;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongDAO;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.Scrobbler;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.gracenote.mmid.MobileSDK.GNOperations;
import com.gracenote.mmid.MobileSDK.GNSearchResponse;
import com.gracenote.mmid.MobileSDK.GNSearchResult;
import com.gracenote.mmid.MobileSDK.GNSearchResultReady;

public class RecognizeManager implements GNSearchResultReady, IRecognizeStatusObservable {
	public static final String RECOGNIZING_SUCCESS = "Recognizing success";
	private static final String TAG = "Recognizing";
	
	private Set<IRecognizeStatusObserver> recognizeStatusObservers;
	
	private GNConfig config;
	private String recognizeStatus;
	
	private String artist;
	private String title;
	private String coverArtUrl;
	
	private Scrobbler scrobbler;
	
	private String previousArtist;
	private String previousTitle;
	
	private SongDAO songDAO;
	private FingerprintDAO fingerprintDAO;
	private Handler handler;
	
	private FingerprintData currentFingerprintData;
	private boolean currentFingerprintIsSaved;
	
	
	public RecognizeManager(GNConfig config, Context context, Scrobbler scrobbler, SongDAO songDAO, Handler handler) {
		this.config = config;
		this.songDAO = songDAO;
		this.handler = handler;
		fingerprintDAO = new FingerprintDAO(context);
        this.scrobbler = scrobbler;
        recognizeStatusObservers = new HashSet<IRecognizeStatusObserver>();
	}
	
	public String getRecognizeStatus() {
		return recognizeStatus;
	}
	
	public List<FingerprintData> getFingerprints() {
		return fingerprintDAO.getFingerprints();
	}
	
	public int removeFingerprint(FingerprintData fingerprint) {
		return fingerprintDAO.delete(fingerprint);
	}
	//TODO synchronized в данном случае не работает, нужно разобраться с блокировками
	public synchronized void recognizeFingerprint(FingerprintData fingerprint, boolean isSaved) {
			currentFingerprintData = fingerprint;
			currentFingerprintIsSaved = isSaved;
			GNOperations.searchByFingerprint(this, config, fingerprint.getFingerprint());
	}

	public void recognizeFingerprintCancel() {
		//TODO возможно нужно сделать что-то ещё
		GNOperations.cancel(this);
	}

	@Override
	public void GNResultReady(GNSearchResult result) {
		Log.i(TAG, "GNResultReady");
		if (result.isFailure()) {
			int errCode = result.getErrCode();
			recognizeStatus = String.format("[%d] %s", errCode,
					result.getErrMessage());
			if(result.isNetworkFailure() && !currentFingerprintIsSaved) {
				fingerprintDAO.insert(currentFingerprintData);
			}
		} else {
			if (result.isFingerprintSearchNoMatchStatus()) {
				recognizeStatus = "Music Not Identified";
			} else {
				GNSearchResponse bestResponse = result.getBestResponse();

				artist = bestResponse.getArtist();
				title = bestResponse.getTrackTitle();
				if (bestResponse.getCoverArt() != null) {
					Log.i(TAG, "URL = " + bestResponse.getCoverArt().getUrl());
					coverArtUrl = bestResponse.getCoverArt().getUrl();
				} else {
					coverArtUrl = null;
				}
				
				if(!artist.equals(previousArtist) || !title.equals(previousTitle)) {
					if(scrobbler != null) {
						scrobbler.scrobble(artist, title);
					} else {
						Log.w("Scrobbling", "scrobbler == null");
					}
					
					SongData songInfo = new SongData(-1, artist, title, bestResponse.getTrackId(), currentFingerprintData.getDate(), null, coverArtUrl);
					songDAO.insert(songInfo);
					if(currentFingerprintIsSaved) {
						removeFingerprint(currentFingerprintData);
					}
					
					previousArtist = artist;
					previousTitle = title;
				}
		
				recognizeStatus = RECOGNIZING_SUCCESS;
				Log.i(TAG, recognizeStatus);
				Log.i(TAG, artist + " " + title);
			}
		}
		
		asyncNotifyRecognizeStatusObservers();
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getCoverArtUrl() {
		return coverArtUrl;
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
	
	private void asyncNotifyRecognizeStatusObservers() {
		handler.post(new Runnable() {
			public void run() {
				notifyRecognizeStatusObservers();
			}
		});
	}
}
