package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongDAO;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISearchResultObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISearchResultObserver;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.gracenote.mmid.MobileSDK.GNDescriptor;
import com.gracenote.mmid.MobileSDK.GNOperations;
import com.gracenote.mmid.MobileSDK.GNSearchResponse;
import com.gracenote.mmid.MobileSDK.GNSearchResult;
import com.gracenote.mmid.MobileSDK.GNSearchResultReady;

public class SearchManager implements GNSearchResultReady, ISearchResultObservable{
	private static final String TAG = "SearchManager";
	public static final String SEARCH_SUCCESS = "search_success";
	
	private Set<ISearchResultObserver> searchStatusObservers;
	
	private GNConfig config;
	private SongDAO songDAO;
	
	public SearchManager(GNConfig config, SongDAO songDAO) {
		this.config = config;
		this.songDAO = songDAO;
		searchStatusObservers = new HashSet<ISearchResultObserver>();
	}
	
	public void search(String artist, String album, String title) {
		Log.i(TAG, "search by: " + artist + " - " + title + " from album " + album);
		GNOperations.searchByText(this, config, artist, album, title);
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
			String artist = bestResponse.getArtist();
			String title = bestResponse.getTrackTitle();
			String album = bestResponse.getAlbumTitle();
			String trackId = bestResponse.getTrackId();
			String coverArtURL = bestResponse.getCoverArt() != null ? bestResponse.getCoverArt().getUrl() : null;
			String contributorImageURL = bestResponse.getContributorImage() != null ? bestResponse.getContributorImage().getUrl() : null;
			String albumArtist = bestResponse.getAlbumArtist();
			String artistBiographyURL = bestResponse.getArtistBiographyUrl();
			String songPosition = bestResponse.getSongPosition();
			String albumReviewUrl = bestResponse.getAlbumReviewUrl();
			String albumReleaseYear = bestResponse.getAlbumReleaseYear();
			
			GNDescriptor artistType[] = bestResponse.getArtistType();
			GNDescriptor era[] = bestResponse.getEra();
			GNDescriptor mood[] = bestResponse.getMood();
			GNDescriptor origin[] = bestResponse.getOrigin();
			GNDescriptor tempo[] = bestResponse.getTempo();
			GNDescriptor trackGenre[] = bestResponse.getTrackGenre();
			GNDescriptor albumGenre[] = bestResponse.getAlbumGenre();
			
			Log.i(TAG, "artist: " + artist);
			Log.i(TAG, "title: " + title);
			Log.i(TAG, "album: " + album);
			Log.i(TAG, "coverArtURL: " + coverArtURL);
			Log.i(TAG, "contributorImageURL: " + contributorImageURL);
			Log.i(TAG, "artistBiographyURL: " + artistBiographyURL);
			Log.i(TAG, "songPosition: " + songPosition);
			Log.i(TAG, "albumReviewUrl: " + albumReviewUrl);
			Log.i(TAG, "albumReleaseYear: " + albumReleaseYear);
			Log.i(TAG, "albumArtist: " + albumArtist);
			
			Log.i(TAG, "Types:");
			for(GNDescriptor d : artistType) {
				Log.i(TAG, "typeId: " + d.getId() + " type: " + d.getData());
			}
			Log.i(TAG, "Era:");
			for(GNDescriptor d : era) {
				Log.i(TAG, "eraId: " + d.getId() + " era: " + d.getData());
			}
			Log.i(TAG, "Mood:");
			for(GNDescriptor d : mood) {
				Log.i(TAG, "moodId: " + d.getId() + " mood: " + d.getData());
			}
			Log.i(TAG, "Origin:");
			for(GNDescriptor d : origin) {
				Log.i(TAG, "originId: " + d.getId() + " origin: " + d.getData());
			}
			Log.i(TAG, "Tempo:");
			for(GNDescriptor d : tempo) {
				Log.i(TAG, "tempoId: " + d.getId() + " tempo: " + d.getData());
			}
			Log.i(TAG, "TrackGenre:");
			for(GNDescriptor d : trackGenre) {
				Log.i(TAG, "trackgenreId: " + d.getId() + " trackgenre: " + d.getData());
			}
			Log.i(TAG, "AlbumGenre:");
			for(GNDescriptor d : albumGenre) {
				Log.i(TAG, "albumgenreId: " + d.getId() + " albumgenre: " + d.getData());
			}
			
			songData = new SongData(-1, artist, title, trackId, (new Date()).toString(), null, coverArtURL);
			songDAO.insert(songData);
			
			searchResultStatus = SEARCH_SUCCESS;
			Log.i(TAG, searchResultStatus);
		}
		
		notifySearchStatusObservers(searchResultStatus, songData);
	}

	@Override
	public void addObserver(ISearchResultObserver o) {
		searchStatusObservers.add(o);
	}

	@Override
	public void removeObserver(ISearchResultObserver o) {
		searchStatusObservers.remove(o);
	}

	@Override
	public void notifySearchStatusObservers(String resultStatus, SongData songData) {
		for(ISearchResultObserver o : searchStatusObservers)
			o.updateSearchStatus(resultStatus, songData);
	}
}