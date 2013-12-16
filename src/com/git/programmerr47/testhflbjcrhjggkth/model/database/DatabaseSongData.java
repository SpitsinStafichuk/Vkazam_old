package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import org.json.JSONException;

import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.SongNotFoundException;
import com.perm.kate.api.Api;
import com.perm.kate.api.KException;


public class DatabaseSongData extends SongData implements Data {
	
	private long id;
	private AbstractDAO dao;
	
	public DatabaseSongData(long id, AbstractDAO dao, String trackId, String artist, String title,
			Date date, String pleercomUrl, String coverArtUrl) {
		super(trackId, artist, title, date, pleercomUrl, coverArtUrl);
		this.id = id;
		this.dao = dao;
	}
	
	DatabaseSongData(long id, AbstractDAO dao, String trackId, String artist, String album, 
			String title, Date date, String pleercomUrl, String vkAudioId, String coverArtUrl, String contributorImageUrl, 
			String artistBiographyURL, String albumReviewUrl, String albumReleaseYear,
			String albumArtist) {
		super(trackId, artist, album, title, pleercomUrl, vkAudioId, coverArtUrl, date, contributorImageUrl, 
				artistBiographyURL, null, albumReviewUrl, albumReleaseYear, albumArtist);
		this.id = id;
		this.dao = dao;
	}
	
	public DatabaseSongData(long id, AbstractDAO dao, SongData data) {
		super(data);
		this.id = id;
		this.dao = dao;
	}
	
	//возможно вообще стоит убрать сет методы для единичных полей
	@Override
	public void setCoverArtUrl(String coverArtUrl) {
		super.setCoverArtUrl(coverArtUrl);
		//TODO возможно неэффективно обновлять все поля, когда нужно обновить только одно
		dao.update(this);
	}
	
	@Override
	public void setPleercomUrl(String pleercomUrl) {
		super.setPleercomUrl(pleercomUrl);
		dao.update(this);
	}
	
	@Override
	public void setVkAudioId(String vkAudioId) {
		super.setVkAudioId(vkAudioId);
		dao.update(this);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof DatabaseSongData) {
				DatabaseSongData oData = (DatabaseSongData) o;
				return this.id == oData.id;
			}
		}
		return false;
	}
	
	@Override
	public void setNullFields(SongData songData) {
		super.setNullFields(songData);
		dao.update(this);
	}
	
	@Override
	public String toString() {
		return "id: " + id + "\n" + super.toString();
	}

	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public void findVkAudio(Api vkApi) throws MalformedURLException, IOException, JSONException, KException, SongNotFoundException {
		super.findVkAudio(vkApi);
		dao.update(this);
	}
	
	@Override
	public void findPPAudio() throws MalformedURLException, IOException, JSONException, com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.KException, SongNotFoundException {
		super.findPPAudio();
		dao.update(this);
	}
}
