package com.git.programmerr47.testhflbjcrhjggkth.model.database.data;

public class SongData {

	private String artist;
	private String title;
	private String trackId;
	private String date;
	private String pleercomURL;
	private String coverArtURL;
	private long id;
	
	public SongData(long id, String artist, String title, String trackId, String date, String pleercomURL, String coverArtURL) {
		this.id = id;
		this.artist = artist;
		this.title = title;
		this.trackId = trackId;
		this.date = date;
		this.pleercomURL = pleercomURL;
		this.coverArtURL = coverArtURL;
	}
	
	public String getArtist() {
		return artist;
	}

	public String getTitle() {
		return title;
	}

	public String getTrackId() {
		return trackId;
	}

	public String getDate() {
		return date;
	}

	public long getId() {
		return id;
	}

	public String getPleercomURL() {
		return pleercomURL;
	}
	
	public String getCoverArtURL() {
		return coverArtURL;
	}
	
	public void setCoverArtURL(String coverArtURL) {
		this.coverArtURL = coverArtURL;
	}
	
	public void setPleercomURL(String pleercomURL) {
		this.pleercomURL = pleercomURL;
	}

	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof SongData) {
				SongData oData = (SongData) o;
				return this.date == oData.getDate();
			}
		}
		return false;
	}
}
