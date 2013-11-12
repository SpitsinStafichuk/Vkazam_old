package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.FingerprintDAO;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongDAO;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.IFingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.ISongData;
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
	public static final String RECOGNIZING_SUCCESS = "Success";
	private static final int GRACENOTE_ERROR_CODE_NO_INTERNET_CONNECTION = 5001;
	
	private Set<IRecognizeStatusObserver> recognizeStatusObservers;
	
	private GNConfig config;
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
	
	public RecognizeManager(GNConfig config, Context context, Scrobbler scrobbler) {
		this.config = config;
		songDAO = new SongDAO(context);
		fingerprintDAO = new FingerprintDAO(context);
        songList = songDAO.getHistory();
        this.scrobbler = scrobbler;
	}
	
	public List<ISongData> getHistory() {
		return songList;
	}
	
	public List<IFingerprintData> getFingerprints() {
		return fingerprintDAO.getFingerprints();
	}
	
	public synchronized void recognizeFingerprint(String fingerprint) {
		GNOperations.searchByFingerprint(this, config, fingerprint);
	}

	@Override
	public void GNResultReady(GNSearchResult result) {
		Log.i("Recognizing", "GNResultReady");
		if (result.isFailure()) {
			int errCode = result.getErrCode();
			recognizeStatus = String.format("[%d] %s", errCode,
					result.getErrMessage());
			if(errCode == GRACENOTE_ERROR_CODE_NO_INTERNET_CONNECTION) {
				FingerprintData fingerprintData = new FingerprintData(-1, fingerprint, (new Date()).toString());
				fingerprintDAO.insert(fingerprintData);
			}
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
