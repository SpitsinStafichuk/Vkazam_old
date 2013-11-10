package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.SongNotFoundException;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.IScrobbler;
import com.google.sydym6.logic.database.data.ISongData;
import com.pleer.api.KException;

import android.graphics.drawable.Drawable;

/**
 * Define interface to work with song
 * @author Mikl
 *
 */
public interface ISongManager {

	void set(ISongData songData);
	void prepare() throws MalformedURLException, IOException, JSONException, SongNotFoundException, KException;
	void play();
	void pause();
	void stop();
	void setPosition(int position);
	int getPosition();
	
	boolean isPlaying();
	boolean isLoading();
	boolean isPrepared();
	
	boolean isLove();
	void setLove(boolean love);
	
	boolean isVkAdded();
	void addToVk();
	
//TODO void share();
	void download();
	
	String getArtist();
	String getTitle();
	int getDuration();
	Drawable getSongLogo();
	ISongData getSongData();
	void release();
	
	/*void setVkApi(Api vkApi);
	Api getVkApi();*/
	void setScrobbler(IScrobbler scrobbler);
}
