package com.git.programmerr47.testhflbjcrhjggkth.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.SongNotFoundException;
import com.perm.kate.api.Api;
import com.perm.kate.api.Audio;
import com.perm.kate.api.KException;

import android.util.Log;

public class SongData {

	protected String artist;
	protected String album;
	protected String title;
	protected String trackId;
	protected String pleercomUrl;
	protected String coverArtUrl;
	protected long lyricsId = -1;
	protected Date date;
	protected String contributorImageUrl;
	protected String artistBiographyURL;
	protected String songPosition;
	protected String albumReviewUrl;
	protected String albumReleaseYear;
	protected String albumArtist;
	protected String vkAudioId;
	
	@Override
	public String toString() {
		return "artist: " + artist +
			   "\nalbum: " + album +
			   "\ntitle: " + title +
			   "\ntrackId" + trackId +
			   "\ndate: " + date +
			   "\npleercomUrl: " + pleercomUrl +
			   "\nvkAudioId: " + vkAudioId +
			   "\ncoverArtUrl: " + coverArtUrl +
			   "\ncontributorImageUrl" + contributorImageUrl +
			   "\nartistBiographyURL: " + artistBiographyURL +
			   "\nsongPosition: " + songPosition +
			   "\nalbumReviewUrl: " + albumReviewUrl +
			   "\nalbumReleaseYear" + albumReleaseYear +
			   "\nalbumArtist: " + albumArtist;
	}
	
	public String getFullTitle() {
		return artist + " - " + title;
	}
	
	public SongData(SongData songData) {
		this(songData.trackId, songData.artist, songData.album, songData.title, songData.pleercomUrl, songData.vkAudioId,
				songData.coverArtUrl, songData.date, songData.contributorImageUrl, songData.artistBiographyURL,
				songData.songPosition, songData.albumReviewUrl, songData.albumReleaseYear, songData.albumArtist);
	}
	
	public SongData(String trackId, String artist, String title, Date date) {
		this(trackId, artist, null, title, null, null, null, date, null, null, null, null, null, null);
	}
	
	public SongData(String trackId, String artist, String title, Date date, 
			String pleercomUrl, String coverArtUrl) {
		this(trackId, artist, null, title, pleercomUrl, null, coverArtUrl, date, null, null, null, null, null, null);
	} 
	
	public SongData(String trackId, String artist, String title, Date date, String coverArtUrl) {
		this(trackId, artist, null, title, null, null, coverArtUrl, date, null, null, null, null, null, null);
		Log.v("SongData", "Creating data with no pleercomurl is success");
		Log.v("SongData", "pleercomurl = " + pleercomUrl);
		Log.v("SongData", "coverarturl = " + this.coverArtUrl);
	} 
	
	public SongData(String trackId, String artist, String album, String title, Date date) {
		this(trackId, artist, album, title, null, null, null, date, null, null, null, null, null, null);
	}
	
	public SongData(String trackId, String artist, String album, 
			String title, String pleercomUrl, String vkAudioId, String coverArtUrl, Date date, String contributorImageUrl, 
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
		Log.v("SongData", "Creating: pleercomUrl is " + this.pleercomUrl);
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public void setTitle(String title) {
		this.title = title;
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
	
	public void setVkAudioId(String vkAudioId) {
		this.vkAudioId = vkAudioId;
	}
	
	public long getLirycsId() {
		return lyricsId;
	}
	
	public String getVkAudioId() {
		return vkAudioId;
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
	
	public String findVkAudio(Api vkApi) throws MalformedURLException, IOException, JSONException, KException, SongNotFoundException {
        if (vkApi != null) {
            List<Audio> audioList = vkApi.searchAudio(getArtist() + " " + getTitle(), "2", "0", 1l, 0l, null, null);
            if (audioList.isEmpty()) {
                throw new SongNotFoundException();
            }
            Audio audio = audioList.get(0);
            vkAudioId = audio.owner_id + "_" + audio.aid;
            Log.v("Lyrics", "" + audio + " --- " + audio.lyrics_id);
            if (audio.lyrics_id != null) {
                lyricsId = audio.lyrics_id;
            } else {
            	lyricsId = -1;
            }
            return audio.url;
        } else {
            return null;
        }
	}
	
	public String getLyrics(Api vkApi) throws MalformedURLException, IOException, JSONException, KException, SongNotFoundException {
		if (lyricsId != -1) {
			return vkApi.getLyrics(lyricsId);
		} else {
			if (vkAudioId == null) {
				findVkAudio(vkApi);
				if (lyricsId != -1) {
					return vkApi.getLyrics(lyricsId);
				}
			}
		}
		return null;
	}
	
	public void setLyricsId(long lyricsId) {
		this.lyricsId = lyricsId;
	}
	
	public void findPPAudio() throws MalformedURLException, IOException, JSONException, com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.KException, SongNotFoundException {
		List<com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.Audio> audioList = com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.Api.searchAudio(getArtist() + " " + getTitle(), 1, 1);
		if (audioList.isEmpty()) {
			throw new SongNotFoundException();
		}
		pleercomUrl = audioList.get(0).url;
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
