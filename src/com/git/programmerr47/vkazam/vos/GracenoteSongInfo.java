package com.git.programmerr47.vkazam.vos;

import java.util.List;

public class GracenoteSongInfo {

	private final int id;
	private String trackId;
	private String artist;
	private String album;
	private String title;
	private String coverArtUrl;
	private String albumReleaseYear;
	private String albumArtist;
	private String lyricsId;
	private List<SongUrl> songUrls;

	public GracenoteSongInfo(int id, String trackId, String artist,
			String album, String title, String coverArtUrl,
			String albumReleaseYear, String albumArtist, String lyricsId,
			List<SongUrl> songUrls) {
		super();
		this.id = id;
		this.trackId = trackId;
		this.artist = artist;
		this.album = album;
		this.title = title;
		this.coverArtUrl = coverArtUrl;
		this.albumReleaseYear = albumReleaseYear;
		this.albumArtist = albumArtist;
		this.lyricsId = lyricsId;
		this.songUrls = songUrls;
	}

	public GracenoteSongInfo(String trackId, String artist, String album,
			String title, String coverArtUrl, String albumReleaseYear,
			String albumArtist, String lyricsId, List<SongUrl> songUrls) {
		super();
		id = -1;
		this.trackId = trackId;
		this.artist = artist;
		this.album = album;
		this.title = title;
		this.coverArtUrl = coverArtUrl;
		this.albumReleaseYear = albumReleaseYear;
		this.albumArtist = albumArtist;
		this.lyricsId = lyricsId;
		this.songUrls = songUrls;
	}

	public int getId() {
		return id;
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCoverArtUrl() {
		return coverArtUrl;
	}

	public void setCoverArtUrl(String coverArtUrl) {
		this.coverArtUrl = coverArtUrl;
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

	public String getLyricsId() {
		return lyricsId;
	}

	public void setLyricsId(String lyricsId) {
		this.lyricsId = lyricsId;
	}

	public List<SongUrl> getSongUrls() {
		return songUrls;
	}

	public void setSongUrls(List<SongUrl> songUrls) {
		this.songUrls = songUrls;
	}

}
