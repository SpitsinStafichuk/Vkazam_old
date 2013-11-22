package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.Date;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.DBConstants;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.utils.LogHelper;
import com.gracenote.mmid.MobileSDK.GNOperationStatusChanged;
import com.gracenote.mmid.MobileSDK.GNSearchResponse;
import com.gracenote.mmid.MobileSDK.GNSearchResult;
import com.gracenote.mmid.MobileSDK.GNSearchResultReady;
import com.gracenote.mmid.MobileSDK.GNStatus;

public class GNSearchResultReadyImplementation implements GNSearchResultReady, GNOperationStatusChanged {
	private static final String TAG = "GNSearchResultReadyImplementation";
	public static final String SEARCH_SUCCESS = "search_success";
	
	private SearchManager.SearchListener listener;

	public GNSearchResultReadyImplementation(SearchManager.SearchListener listener) {
		this.listener = listener;
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
			
			songData = new SongData.SongDataBuilder()
									.setArtist(artist)
									.setTitle(title)
									.setTrackId(trackId)
									.setDate((new Date()).toString())
									.setCoverArtURL(coverArtURL)
									.build();
			
			searchResultStatus = SEARCH_SUCCESS;
		}
		
		Log.i(TAG, searchResultStatus);
		listener.onSearchStatusChanged(searchResultStatus);
		listener.onSearchResult(songData);
	}

	@Override
	public void GNStatusChanged(GNStatus status) {
		String searchStatus = status.getMessage();
		listener.onSearchStatusChanged(searchStatus);
	}
}
