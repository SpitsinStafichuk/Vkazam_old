package com.git.programmerr47.testhflbjcrhjggkth.model;

import java.util.Date;

public class SongData {

	protected String artist;
	protected String album;
	protected String title;
	protected String trackId;
	protected String pleercomUrl;
	protected String coverArtUrl;
	protected Date date;
	protected String contributorImageUrl;
	protected String artistBiographyURL;
	protected String songPosition;
	protected String albumReviewUrl;
	protected String albumReleaseYear;
	protected String albumArtist;
	
	public SongData(SongData songData) {
		this(songData.trackId, songData.artist, songData.album, songData.title, songData.pleercomUrl,
				songData.coverArtUrl, songData.date, songData.contributorImageUrl, songData.artistBiographyURL,
				songData.songPosition, songData.albumReviewUrl, songData.albumReleaseYear, songData.albumArtist);
	}
	
	public SongData(String trackId, String artist, String title, Date date) {
		this(trackId, artist, null, title, null, null, date, null, null, null, null, null, null);
	}
	
	public SongData(String trackId, String artist, String title, Date date, 
			String pleercomUrl, String coverArtUrl) {
		this(trackId, artist, null, title, pleercomUrl, coverArtUrl, date, null, null, null, null, null, null);
	} 
	
	public SongData(String trackId, String artist, String title, Date date, String coverArtUrl) {
		this(trackId, artist, null, title, null, coverArtUrl, date, null, null, null, null, null, null);
	} 
	
	public SongData(String trackId, String artist, String album, String title, Date date) {
		this(trackId, artist, album, title, null, null, date, null, null, null, null, null, null);
	}
	
	public SongData(String trackId, String artist, String album, 
			String title, String pleercomUrl, String coverArtUrl, Date date, String contributorImageUrl, 
			String artistBiographyURL, String songPosition, String albumReviewUrl, String albumReleaseYear,
			String albumArtist) {
		this.artist = artist;
		this.setAlbum(album);
		this.title = title;
		this.trackId = trackId;
		this.pleercomUrl = pleercomUrl;
		this.coverArtUrl = coverArtUrl;
		this.date = date;
		this.setContributorImageUrl(contributorImageUrl);
		this.setArtistBiographyURL(artistBiographyURL);
		this.setSongPosition(songPosition);
		this.setAlbumReviewUrl(albumReviewUrl);
		this.setAlbumReleaseYear(albumReleaseYear);
		this.setAlbumArtist(albumArtist);
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public void setTitle(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public String getTrackId() {
		return trackId;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public String getPleercomUrl() {
		return pleercomUrl;
	}
	
	public String getCoverArtUrl() {
		return coverArtUrl;
	}
	
	public void setCoverArtUrl(String coverArtURL) {
		this.coverArtUrl = coverArtURL;
	}
	
	public void setPleercomUrl(String pleercomURL) {
		this.pleercomUrl = pleercomURL;
	}
	
	public void setNullFields(SongData songData) {
		if(trackId.equals(songData.trackId)) {
			if(pleercomUrl == null) pleercomUrl = songData.pleercomUrl;
			if(coverArtUrl == null) coverArtUrl = songData.coverArtUrl;
			if(contributorImageUrl == null) contributorImageUrl = songData.contributorImageUrl;
			if(artistBiographyURL == null) artistBiographyURL = songData.artistBiographyURL;
			if(songPosition == null) songPosition = songData.songPosition;
			if(albumReviewUrl == null) albumReviewUrl = songData.albumReviewUrl;
			if(albumArtist == null) albumArtist = songData.albumArtist;
			if(albumReleaseYear == null) albumReleaseYear = songData.albumReleaseYear;
		} else {
			throw new IllegalArgumentException("TrackIds must be equals");
		}
	}
	
	public String getContributorImageUrl() {
		return contributorImageUrl;
	}

	public void setContributorImageUrl(String contributorImageUrl) {
		this.contributorImageUrl = contributorImageUrl;
	}

	public String getArtistBiographyURL() {
		return artistBiographyURL;
	}

	public void setArtistBiographyURL(String artistBiographyURL) {
		this.artistBiographyURL = artistBiographyURL;
	}

	public String getSongPosition() {
		return songPosition;
	}

	public void setSongPosition(String songPosition) {
		this.songPosition = songPosition;
	}

	public String getAlbumReviewUrl() {
		return albumReviewUrl;
	}

	public void setAlbumReviewUrl(String albumReviewUrl) {
		this.albumReviewUrl = albumReviewUrl;
	}

	public String getAlbumReleaseYear() {
		return albumReleaseYear;
	}

	public void setAlbumReleaseYear(String albumReleaseYear) {
		this.albumReleaseYear = albumReleaseYear;
	}

	public String getAlbumArtist() {
		return albumArtist;
	}

	public void setAlbumArtist(String albumArtist) {
		this.albumArtist = albumArtist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}
}
