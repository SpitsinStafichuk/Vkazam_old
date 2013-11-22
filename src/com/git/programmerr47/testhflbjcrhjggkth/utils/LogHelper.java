package com.git.programmerr47.testhflbjcrhjggkth.utils;

import android.util.Log;

import com.gracenote.mmid.MobileSDK.GNDescriptor;
import com.gracenote.mmid.MobileSDK.GNSearchResponse;

public class LogHelper {
	
	public static void print(String tag, GNSearchResponse response) {
		String artist = response.getArtist();
		String title = response.getTrackTitle();
		String album = response.getAlbumTitle();
		String trackId = response.getTrackId();
		String coverArtURL = response.getCoverArt() != null ? response.getCoverArt().getUrl() : null;
		String contributorImageURL = response.getContributorImage() != null ? response.getContributorImage().getUrl() : null;
		String albumArtist = response.getAlbumArtist();
		String artistBiographyURL = response.getArtistBiographyUrl();
		String songPosition = response.getSongPosition();
		String albumReviewUrl = response.getAlbumReviewUrl();
		String albumReleaseYear = response.getAlbumReleaseYear();
		
		GNDescriptor artistType[] = response.getArtistType();
		GNDescriptor era[] = response.getEra();
		GNDescriptor mood[] = response.getMood();
		GNDescriptor origin[] = response.getOrigin();
		GNDescriptor tempo[] = response.getTempo();
		GNDescriptor trackGenre[] = response.getTrackGenre();
		GNDescriptor albumGenre[] = response.getAlbumGenre();
		
		Log.i(tag, "artist: " + artist);
		Log.i(tag, "title: " + title);
		Log.i(tag, "album: " + album);
		Log.i(tag, "trackID: " + trackId);
		Log.i(tag, "coverArtURL: " + coverArtURL);
		Log.i(tag, "contributorImageURL: " + contributorImageURL);
		Log.i(tag, "artistBiographyURL: " + artistBiographyURL);
		Log.i(tag, "songPosition: " + songPosition);
		Log.i(tag, "albumReviewUrl: " + albumReviewUrl);
		Log.i(tag, "albumReleaseYear: " + albumReleaseYear);
		Log.i(tag, "albumArtist: " + albumArtist);
		
		Log.i(tag, "Types:");
		for(GNDescriptor d : artistType) {
			Log.i(tag, "typeId: " + d.getId() + " type: " + d.getData());
		}
		Log.i(tag, "Era:");
		for(GNDescriptor d : era) {
			Log.i(tag, "eraId: " + d.getId() + " era: " + d.getData());
		}
		Log.i(tag, "Mood:");
		for(GNDescriptor d : mood) {
			Log.i(tag, "moodId: " + d.getId() + " mood: " + d.getData());
		}
		Log.i(tag, "Origin:");
		for(GNDescriptor d : origin) {
			Log.i(tag, "originId: " + d.getId() + " origin: " + d.getData());
		}
		Log.i(tag, "Tempo:");
		for(GNDescriptor d : tempo) {
			Log.i(tag, "tempoId: " + d.getId() + " tempo: " + d.getData());
		}
		Log.i(tag, "TrackGenre:");
		for(GNDescriptor d : trackGenre) {
			Log.i(tag, "trackgenreId: " + d.getId() + " trackgenre: " + d.getData());
		}
		Log.i(tag, "AlbumGenre:");
		for(GNDescriptor d : albumGenre) {
			Log.i(tag, "albumgenreId: " + d.getId() + " albumgenre: " + d.getData());
		}
	}
}
