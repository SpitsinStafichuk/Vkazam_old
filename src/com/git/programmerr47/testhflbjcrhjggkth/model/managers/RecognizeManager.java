package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeResultObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeResultObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;
import com.git.programmerr47.testhflbjcrhjggkth.utils.LogHelper;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.gracenote.mmid.MobileSDK.GNOperationStatusChanged;
import com.gracenote.mmid.MobileSDK.GNOperations;
import com.gracenote.mmid.MobileSDK.GNSearchResponse;
import com.gracenote.mmid.MobileSDK.GNSearchResult;
import com.gracenote.mmid.MobileSDK.GNSearchResultReady;
import com.gracenote.mmid.MobileSDK.GNStatus;

public class RecognizeManager implements GNSearchResultReady, GNOperationStatusChanged,
                                         IRecognizeStatusObservable, IRecognizeResultObservable {
	public static final String RECOGNIZING_SUCCESS = "Recognizing success";
	private static final String TAG = "Recognizing";
	
	private Set<IRecognizeStatusObserver> recognizeStatusObservers;
	private Set<IRecognizeResultObserver> recognizeResultObservers;
	
	private GNConfig config;
	
	
	public RecognizeManager(GNConfig config) {
		this.config = config;
        recognizeStatusObservers = new HashSet<IRecognizeStatusObserver>();
        recognizeResultObservers = new HashSet<IRecognizeResultObserver>();
	}

	public void recognizeFingerprint(FingerprintData fingerprint) {
		Log.i(TAG, fingerprint.getFingerprint());
		GNOperations.searchByFingerprint(this, config, fingerprint.getFingerprint());
	}

	public void recognizeFingerprintCancel() {
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
		int errCode = result.getErrCode();
		if (result.isFailure()) {
			recognizeStatus = String.format("[%d] %s", errCode,
					result.getErrMessage());
		} else {
			if (result.isFingerprintSearchNoMatchStatus()) {
				recognizeStatus = "Music Not Identified";
			} else {
				GNSearchResponse bestResponse = result.getBestResponse();


				LogHelper.print(TAG, bestResponse);
				String artist = bestResponse.getArtist();
				String title = bestResponse.getTrackTitle();
				String album = bestResponse.getAlbumTitle();
				String trackId = bestResponse.getTrackId();
				String coverArtURL = bestResponse.getCoverArt() != null ? 
						bestResponse.getCoverArt().getUrl() : null;
				String contributorImageURL = bestResponse.getContributorImage() != null ?
						bestResponse.getContributorImage().getUrl() : null;
				String albumArtist = bestResponse.getAlbumArtist();
				String artistBiographyURL = bestResponse.getArtistBiographyUrl();
				String songPosition = bestResponse.getSongPosition();
				String albumReviewUrl = bestResponse.getAlbumReviewUrl();
				String albumReleaseYear = bestResponse.getAlbumReleaseYear();
				
				songData = new SongData(null, null, trackId, artist, album, title, null, null, coverArtURL, new Date(), contributorImageURL,
						artistBiographyURL, songPosition, albumReviewUrl, albumReleaseYear, albumArtist);
		
				recognizeStatus = RECOGNIZING_SUCCESS;
			}
		}
		
		notifyRecognizeStatusObservers(recognizeStatus);
		notifyRecognizeResultObservers(errCode, songData);
	}

	@Override
	public void addRecognizeStatusObserver(IRecognizeStatusObserver o) {
		recognizeStatusObservers.add(o);
	}

	@Override
	public void removeRecognizeStatusObserver(IRecognizeStatusObserver o) {
		recognizeStatusObservers.remove(o);
	}

	@Override
	public void addRecognizeResultObserver(IRecognizeResultObserver o) {
		recognizeResultObservers.add(o);
        Log.v("RecognizeManager", "after adding, observers = " + recognizeResultObservers.size());
	}

	@Override
	public void removeRecognizeResultObserver(IRecognizeResultObserver o) {
		recognizeResultObservers.remove(o);
        Log.v("RecognizeManager", "after removing, observers = " + recognizeResultObservers.size());
	}

	@Override
	public void notifyRecognizeResultObservers(int errorCode, SongData songData) {
		for(IRecognizeResultObserver o : recognizeResultObservers)
			o.onRecognizeResult(errorCode, songData);
	}

	@Override
	public void notifyRecognizeStatusObservers(String status) {
		for(IRecognizeStatusObserver o : recognizeStatusObservers)
			o.onRecognizeStatusChanged(status);
	}
}
