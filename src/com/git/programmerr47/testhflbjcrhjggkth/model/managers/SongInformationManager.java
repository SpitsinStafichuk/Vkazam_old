package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongDAO;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISearchStatusObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISearchStatusObservable;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.gracenote.mmid.MobileSDK.GNOperations;
import com.gracenote.mmid.MobileSDK.GNSearchResponse;
import com.gracenote.mmid.MobileSDK.GNSearchResult;
import com.gracenote.mmid.MobileSDK.GNSearchResultReady;

public class SongInformationManager implements GNSearchResultReady, ISearchStatusObservable{
	private static final String TAG = "SongInformation";
	public static final String SEARCH_SUCCESS = "search_success";
	
	private Set<ISearchStatusObserver> searchStatusObservers;
	private Map<String, SongData> songsData;
	
	private GNConfig config;
	private String searchStatus;
	private SongDAO songDAO;
	
	public SongInformationManager(GNConfig config, SongDAO songDAO) {
		this.config = config;
		this.songDAO = songDAO;
		searchStatusObservers = new HashSet<ISearchStatusObserver>();
		songsData = new ConcurrentHashMap<String, SongData>();
	}
	
	public void searchCoverArtByTrackId(SongData songData) {
		String trackId = songData.getTrackId();
		Log.i(TAG, "search by track id: " + trackId);
		songsData.put(trackId, songData);
		GNOperations.fetchByTrackId(this, config, trackId);
	}
	
	public void searchCoverArtByTrackIdIfNotNull(SongData songData) {
		if (songData.getCoverArtURL() == null) {
			searchCoverArtByTrackId(songData);
		} else {
			searchStatus = SEARCH_SUCCESS;
			notifySearchStatusObservers(songData);
		}
	}

	@Override
	public void GNResultReady(GNSearchResult result) {
		Log.i(TAG, "GNResultReady");
		SongData songData = null;
		if (result.isFailure()) {
			int errCode = result.getErrCode();
			searchStatus = String.format("[%d] %s", errCode, result.getErrMessage());
		} else {
			GNSearchResponse bestResponse = result.getBestResponse();
			String trackId = bestResponse.getTrackId();

			if (bestResponse.getCoverArt() != null) {
				String coverArtURL = bestResponse.getCoverArt().getUrl();
				Log.i(TAG, "URL = " + coverArtURL);
				songData = songsData.get(trackId);
				songData.setCoverArtURL(coverArtURL);
				songDAO.update(songData);
			} 
			
			searchStatus = SEARCH_SUCCESS;
			Log.i(TAG, searchStatus);
		}
		
		notifySearchStatusObservers(songData);
	}
	
	public String getSearchStatus() {
		return searchStatus;
	}

	@Override
	public void addObserver(ISearchStatusObserver o) {
		searchStatusObservers.add(o);
	}

	@Override
	public void removeObserver(ISearchStatusObserver o) {
		searchStatusObservers.remove(o);
	}

	@Override
	public void notifySearchStatusObservers(SongData songData) {
		for(ISearchStatusObserver o : searchStatusObservers)
			o.updateSearchStatus(songData);
	}
}
