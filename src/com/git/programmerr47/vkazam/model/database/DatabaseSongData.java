package com.git.programmerr47.vkazam.model.database;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import org.json.JSONException;

import com.git.programmerr47.vkazam.model.SongData;
import com.git.programmerr47.vkazam.model.exceptions.SongNotFoundException;
import com.perm.kate.api.Api;
import com.perm.kate.api.KException;


public class DatabaseSongData extends SongData implements Data {
	
	private long id;
	private AbstractDAO dao;
	
	DatabaseSongData(long id, String ppArtist, String ppTitle, AbstractDAO dao, String trackId, String artist, String album,
			String title, Date date, String pleercomUrl, String vkAudioId, String coverArtUrl, String contributorImageUrl, 
			String artistBiographyURL, String albumReviewUrl, String albumReleaseYear,
			String albumArtist) {
		super(ppArtist, ppTitle, trackId, artist, album, title, pleercomUrl, vkAudioId, coverArtUrl, date, contributorImageUrl,
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

		if (dao != null) {
            dao.update(this);
        }
	}

    @Override
    public void setPleercomUrl(String pleercomUrl) {
        super.setPleercomUrl(pleercomUrl);

        if (dao != null) {
            dao.update(this);
        }
    }

    @Override
    public void setPpArtist(String artist) {
        super.setPpArtist(artist);

        if (dao != null) {
            dao.update(this);
        }
    }

    @Override
    public void setPpTitle(String title) {
        super.setPpTitle(title);

        if (dao != null) {
            dao.update(this);
        }
    }
	
	@Override
	public void setVkAudioId(String vkAudioId) {
		super.setVkAudioId(vkAudioId);

        if (dao != null) {
            dao.update(this);
        }
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

        if (dao != null) {
            dao.update(this);
        }
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
	public String findVkAudio(Api vkApi) throws MalformedURLException, IOException, JSONException, KException, SongNotFoundException {
		String result = super.findVkAudio(vkApi);

        if (dao != null) {
            dao.update(this);
        }

		return result;
	}
	
	@Override
	public void findPPAudio() throws MalformedURLException, IOException, JSONException, com.git.programmerr47.vkazam.model.pleer.api.KException, SongNotFoundException {
		super.findPPAudio();

        if (dao != null) {
            dao.update(this);
        }
	}
}
