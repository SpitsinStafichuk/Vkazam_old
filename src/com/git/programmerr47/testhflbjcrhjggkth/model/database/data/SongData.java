package com.git.programmerr47.testhflbjcrhjggkth.model.database.data;

public class SongData extends Data{

	private String artist;
	private String title;
	private String trackId;
	private String pleercomURL;
	private String coverArtURL;
	
	private SongData(SongDataBuilder builder) {
		this.id = builder.id;
		this.artist = builder.artist;
		this.title = builder.title;
		this.trackId = builder.trackId;
		this.date = builder.date;
		this.pleercomURL = builder.pleercomURL;
		this.coverArtURL = builder.coverArtURL;
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
	
	public static class SongDataBuilder implements Builder<SongData> {

		private String artist;
		private String title;
		private String trackId;
		private String date;
		private String pleercomURL;
		private String coverArtURL;
		private long id;
		
		@Override
		public SongData build() {
			return new SongData(this);
		}
		
		public SongDataBuilder setId(long id) {
			this.id = id;
			return this;
		}
		
		public SongDataBuilder setArtist(String artist) {
			this.artist = artist;
			return this;
		}
		
		public SongDataBuilder setTitle(String title) {
			this.title = title;
			return this;
		}
		
		public SongDataBuilder setTrackId(String trackId) {
			this.trackId = trackId;
			return this;
		}
		
		public SongDataBuilder setDate(String date) {
			this.date = date;
			return this;
		}
		
		public SongDataBuilder setPleercomURL(String pleercomURL) {
			this.pleercomURL = pleercomURL;
			return this;
		}
		
		public SongDataBuilder setCoverArtURL(String coverArtURL) {
			this.coverArtURL = coverArtURL;
			return this;
		}
	}
}
