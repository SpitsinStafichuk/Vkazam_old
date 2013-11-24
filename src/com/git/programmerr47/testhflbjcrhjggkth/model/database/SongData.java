package com.git.programmerr47.testhflbjcrhjggkth.model.database;


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
		this.dao = builder.dao;
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
		dao.update(this);
	}
	
	public void setPleercomURL(String pleercomURL) {
		this.pleercomURL = pleercomURL;
		dao.update(this);
	}
	
	public void setNullFields(SongData songData) {
		if(id == -1) id = songData.id;
		if(artist == null) artist = songData.artist;
		if(title == null) title = songData.title;
		if(trackId == null) trackId = songData.trackId;
		if(date == -1) date = songData.date;
		if(pleercomURL == null) pleercomURL = songData.pleercomURL;
		if(coverArtURL == null) coverArtURL = songData.coverArtURL;
	}
	
	public void setNullFields(SongDataBuilder builder) {
		if(id == -1) id = builder.id;
		if(artist == null) artist = builder.artist;
		if(title == null) title = builder.title;
		if(trackId == null) trackId = builder.trackId;
		if(date == -1) date = builder.date;
		if(pleercomURL == null) pleercomURL = builder.pleercomURL;
		if(coverArtURL == null) coverArtURL = builder.coverArtURL;
	}
	
	public static class SongDataBuilder implements Builder<SongData> {
		private String artist;
		private String title;
		private String trackId;
		private long date;
		private String pleercomURL;
		private String coverArtURL;
		private long id;
		private AbstractDAO dao;
		
		@Override
		public SongData build() {
			if (dao == null) {
				throw new IllegalStateException("Dao is null, you need to put builder into the list");
			} else {
				return new SongData(this);
			}
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
		
		public SongDataBuilder setDate(long date) {
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
		
		SongDataBuilder setSongDAO(AbstractDAO dao) {
			this.dao = dao;
			return this;
		}
		
		public long getId() {
			return id;
		}
		
		public String getArtist() {
			return artist;
		}

		public String getTitle() {
			return title;
		}
		
		public String getCoverArtURL() {
			return coverArtURL;
		}

		public String getTrackId() {
			return trackId;
		}
	}
	
	@Override
	public String toString() {
		return "id: " + id + 
				"\nartist: " + artist + 
				"\ntitle: " + title + 
				"\ntrackId: " + trackId + 
				"\ndate: " + date + 
				"\npleercomURL: " + pleercomURL + 
				"\ncoverArtURL: " + coverArtURL;
	}
}