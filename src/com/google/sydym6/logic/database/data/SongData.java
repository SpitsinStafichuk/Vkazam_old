package com.google.sydym6.logic.database.data;

public class SongData implements ISongData {

	private String artist;
	private String title;
	private String trackId;
	private String date;
	private String pleercomURL;
	private long id;
	
	public SongData(long id, String artist, String title, String trackId, String date, String pleercomURL) {
		this.id = id;
		this.artist = artist;
		this.title = title;
		this.trackId = trackId;
		this.date = date;
		this.pleercomURL = pleercomURL;
	}
	
	@Override
	public String getArtist() {
		return artist;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getTrackId() {
		return trackId;
	}

	@Override
	public String getDate() {
		return date;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof ISongData) {
				ISongData oData = (ISongData) o;
				return this.date == oData.getDate();
			}
		}
		return false;
	}

	@Override
	public String getPleercomURL() {
		return pleercomURL;
	}
}
