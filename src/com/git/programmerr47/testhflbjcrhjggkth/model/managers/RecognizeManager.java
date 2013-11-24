package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeResultObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeResultObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.gracenote.mmid.MobileSDK.GNOperationStatusChanged;
import com.gracenote.mmid.MobileSDK.GNOperations;
import com.gracenote.mmid.MobileSDK.GNSearchResponse;
import com.gracenote.mmid.MobileSDK.GNSearchResult;
import com.gracenote.mmid.MobileSDK.GNSearchResultReady;
import com.gracenote.mmid.MobileSDK.GNStatus;

public class RecognizeManager implements GNSearchResultReady, GNOperationStatusChanged, IRecognizeStatusObservable, IRecognizeResultObservable {
	public static final String RECOGNIZING_SUCCESS = "Recognizing success";
	private static final String TAG = "Recognizing";
	
	private Set<IRecognizeStatusObserver> recognizeStatusObservers;
	private Set<IRecognizeResultObserver> recognizeResultObservers;
	
	private GNConfig config;
	
	private FingerprintData currentFingerprintData;
	private boolean currentFingerprintIsSaved;
	
	
	public RecognizeManager(GNConfig config, Context context) {
		this.config = config;
        recognizeStatusObservers = new HashSet<IRecognizeStatusObserver>();
        recognizeResultObservers = new HashSet<IRecognizeResultObserver>();
	}
	
	//TODO synchronized в данном случае не работает, нужно разобраться с блокировками
	public synchronized void recognizeFingerprint(FingerprintData fingerprint, boolean isSaved) {
		Log.i(TAG, fingerprint.getFingerprint());
		currentFingerprintData = fingerprint;
		currentFingerprintIsSaved = isSaved;
		GNOperations.searchByFingerprint(this, config, fingerprint.getFingerprint());
	}

	public void recognizeFingerprintCancel() {
		//TODO возможно нужно сделать что-то ещё
		GNOperations.cancel(this);
	}
	
	@Override
	public void GNStatusChanged(GNStatus status) {
		String recognizeStatus = status.getMessage() + " " + status.getPercentDone() + " %";
		notifyRecognizeStatusObservers(recognizeStatus);
	}

	@Override
	public void GNResultReady(GNSearchResult result) {
		Log.i(TAG, "GNResultReady");
		String recognizeStatus = null;
		SongData songData = null;
		if (result.isFailure()) {
			int errCode = result.getErrCode();
			recognizeStatus = String.format("[%d] %s", errCode,
					result.getErrMessage());
	/*		if(result.isNetworkFailure() && !currentFingerprintIsSaved) {
				fingerprintDAO.insert(currentFingerprintData);
			}*/
		} else {
			if (result.isFingerprintSearchNoMatchStatus()) {
				recognizeStatus = "Music Not Identified";
			} else {
				GNSearchResponse bestResponse = result.getBestResponse();

				String artist = bestResponse.getArtist();
				String title = bestResponse.getTrackTitle();
				String coverArtURL = bestResponse.getCoverArt() != null ? bestResponse.getCoverArt().getUrl() : null;
				Log.i(TAG, "coverArtUrl = " + coverArtURL);
				
				songData = new SongData.SongDataBuilder()
														.setArtist(artist)
														.setTitle(title)
														.setTrackId(bestResponse.getTrackId())
														.setDate(currentFingerprintData.getDate())
														.setCoverArtURL(coverArtURL)
														.build();
				//songDAO.insert(songInfo);
				/*if(currentFingerprintIsSaved) {
					removeFingerprint(currentFingerprintData);
				}*/
		
				recognizeStatus = RECOGNIZING_SUCCESS;
			}
		}
		
		notifyRecognizeStatusObservers(recognizeStatus);
		notifyRecognizeResultObservers(songData);
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
	public void addObserver(IRecognizeResultObserver o) {
		recognizeResultObservers.add(o);
	}

	@Override
	public void removeObserver(IRecognizeResultObserver o) {
		recognizeResultObservers.remove(o);
	}

	@Override
	public void notifyRecognizeResultObservers(SongData songData) {
		for(IRecognizeResultObserver o : recognizeResultObservers)
			o.onRecognizeResult(songData);
	}

	@Override
	public void notifyRecognizeStatusObservers(String status) {
		for(IRecognizeStatusObserver o : recognizeStatusObservers)
			o.onRecognizeStatusChanged(status);
	}
}
