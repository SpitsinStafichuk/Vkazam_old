package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.Date;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
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
			if (bestResponse == null) {
				searchResultStatus = "Music is not found";
			} else {
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
				
				songData = new SongData(trackId, artist, album, title, null, coverArtURL, new Date(), contributorImageURL,
						artistBiographyURL, songPosition, albumReviewUrl, albumReleaseYear, albumArtist);
				
				searchResultStatus = SEARCH_SUCCESS;
			}
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
