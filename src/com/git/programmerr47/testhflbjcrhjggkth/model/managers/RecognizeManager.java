package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.FingerprintsDeque;
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
                                         IRecognizeStatusObservable, IRecognizeResultObservable,
                                         FingerprintsDeque.OnDequeStateListener {
	public static final String RECOGNIZING_SUCCESS = "Recognizing success";
	private static final String TAG = "Recognizing";
	
	private Set<IRecognizeStatusObserver> recognizeStatusObservers;
	private Set<IRecognizeResultObserver> recognizeResultObservers;
	
	private GNConfig config;
    private FingerprintsDeque fingerprintsDeque;
	private FingerprintData currentFingerprintData;
	
	
	public RecognizeManager(GNConfig config, FingerprintsDeque fingerprintsDeque) {
        this.fingerprintsDeque = fingerprintsDeque;
        fingerprintsDeque.setOnDequeStateListener(this);
		this.config = config;
        recognizeStatusObservers = new HashSet<IRecognizeStatusObserver>();
        recognizeResultObservers = new HashSet<IRecognizeResultObserver>();
	}

	public void recognizeFingerprint(FingerprintData fingerprint, boolean isSaved) {
		Log.i(TAG, fingerprint.getFingerprint());
		currentFingerprintData = fingerprint;
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
				
				songData = new SongData(trackId, artist, album, title, null, null, coverArtURL, currentFingerprintData.getDate(), contributorImageURL,
						artistBiographyURL, songPosition, albumReviewUrl, albumReleaseYear, albumArtist);
		
				recognizeStatus = RECOGNIZING_SUCCESS;
			}
		}
		
		notifyRecognizeStatusObservers(recognizeStatus);
		notifyRecognizeResultObservers(songData);

        if (fingerprintsDeque.size() > 0) {
            recognizeFingerprint((FingerprintData)fingerprintsDeque.pollFirst(), false);
        }
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
	}

	@Override
	public void removeRecognizeResultObserver(IRecognizeResultObserver o) {
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

    @Override
    public void onNonEmpty() {
        //ToDo change body of implemented methods use File | Settings | File Templates.
        recognizeFingerprint((FingerprintData)fingerprintsDeque.pollFirst(), false);
    }
}
