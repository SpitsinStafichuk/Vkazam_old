package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Log;

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
	private Map<String, String> coverArtUrls;
	
	private GNConfig config;
	private String searchStatus;
	
	public SongInformationManager(GNConfig config) {
		this.config = config;
		searchStatusObservers = new HashSet<ISearchStatusObserver>();
		coverArtUrls = new ConcurrentHashMap<String, String>();
	}
	
	public void searchByTrackId(String trackId) {
		Log.i(TAG, "search by track id: " + trackId);
		if (!coverArtUrls.containsKey(trackId)) {
			GNOperations.fetchByTrackId(this, config, trackId);
		} else {
			searchStatus = SEARCH_SUCCESS;
			notifySearchStatusObservers(trackId);
		}
	}

	@Override
	public void GNResultReady(GNSearchResult result) {
		Log.i(TAG, "GNResultReady");
		String trackId = null;
		if (result.isFailure()) {
			int errCode = result.getErrCode();
			searchStatus = String.format("[%d] %s", errCode, result.getErrMessage());
		} else {
			GNSearchResponse bestResponse = result.getBestResponse();
			trackId = bestResponse.getTrackId();

			if (bestResponse.getCoverArt() != null) {
				Log.i(TAG, "URL = " + bestResponse.getCoverArt().getUrl());
				coverArtUrls.put(bestResponse.getTrackId(), bestResponse.getCoverArt().getUrl());
			} else {
				//coverArtUrl = null;
				coverArtUrls.put(bestResponse.getTrackId(), null);
			}
	
			searchStatus = SEARCH_SUCCESS;
			Log.i(TAG, searchStatus);
		}
		
		notifySearchStatusObservers(trackId);
	}
	
	public String getCoverArtUrl(String trackId) {
		if (coverArtUrls.containsKey(trackId)) {
			return coverArtUrls.get(trackId);
		} else {
			return null;
		}
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
	public void notifySearchStatusObservers(String trackId) {
		for(ISearchStatusObserver o : searchStatusObservers)
			o.updateSearchStatus(trackId);
	}
}
