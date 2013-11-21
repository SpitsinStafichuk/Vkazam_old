package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISearchResultObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISearchResultObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISearchStatusObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISearchStatusObserver;
import com.git.programmerr47.testhflbjcrhjggkth.utils.LogHelper;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.gracenote.mmid.MobileSDK.GNOperationStatusChanged;
import com.gracenote.mmid.MobileSDK.GNOperations;
import com.gracenote.mmid.MobileSDK.GNSearchResponse;
import com.gracenote.mmid.MobileSDK.GNSearchResult;
import com.gracenote.mmid.MobileSDK.GNSearchResultReady;
import com.gracenote.mmid.MobileSDK.GNStatus;

public class SearchManager implements GNSearchResultReady, GNOperationStatusChanged, ISearchResultObservable, ISearchStatusObservable {
	private static final String TAG = "SearchManager";
	public static final String SEARCH_SUCCESS = "search_success";
	
	private Set<ISearchResultObserver> searchResultObservers;
	private Set<ISearchStatusObserver> searchStatusObservers;
	
	private GNConfig config;
	
	public SearchManager(GNConfig config) {
		this.config = config;
		searchStatusObservers = new HashSet<ISearchStatusObserver>();
		searchResultObservers = new HashSet<ISearchResultObserver>();
	}
	
	public void search(String artist, String album, String title) {
		Log.i(TAG, "search by: " + artist + " - " + title + " from album " + album);
		GNOperations.searchByText(this, config, artist, album, title);
	}
	
	public void search(String trackId) {
		Log.i(TAG, "search by trackId: " + trackId);
		GNOperations.fetchByTrackId(this, config, trackId);
	}

	@Override
	public void GNResultReady(GNSearchResult result) {
		Log.i(TAG, "GNResultReady");
		SongData songData = null;
		String searchResultStatus = null;
		if (result.isFailure()) {
			int errCode = result.getErrCode();
			searchResultStatus = String.format("[%d] %s", errCode, result.getErrMessage());
		} else {
			GNSearchResponse bestResponse = result.getBestResponse();
			LogHelper.print(TAG, bestResponse);
			String artist = bestResponse.getArtist();
			String title = bestResponse.getTrackTitle();
			String trackId = bestResponse.getTrackId();
			String coverArtURL = bestResponse.getCoverArt() != null ? bestResponse.getCoverArt().getUrl() : null;
			
			songData = new SongData(-1, artist, title, trackId, (new Date()).toString(), null, coverArtURL);
			
			searchResultStatus = SEARCH_SUCCESS;
		}
		
		Log.i(TAG, searchResultStatus);
		notifySearchStatusObservers(searchResultStatus);
		notifySearchResultObservers(songData);
	}

	@Override
	public void addObserver(ISearchResultObserver o) {
		searchResultObservers.add(o);
	}

	@Override
	public void removeObserver(ISearchResultObserver o) {
		searchResultObservers.remove(o);
	}

	@Override
	public void notifySearchResultObservers(SongData songData) {
		for(ISearchResultObserver o : searchResultObservers)
			o.onSearchResult(songData);
	}

	@Override
	public void GNStatusChanged(GNStatus status) {
		String searchStatus = status.getMessage() + " " + status.getPercentDone() + " %";
		notifySearchStatusObservers(searchStatus);
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
	public void notifySearchStatusObservers(String status) {
		for(ISearchStatusObserver o : searchStatusObservers)
			o.onSearchStatusChanged(status);
	}
}