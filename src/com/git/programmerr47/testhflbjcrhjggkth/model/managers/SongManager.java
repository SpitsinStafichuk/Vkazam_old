package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongDAO;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.SongNotFoundException;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.IScrobbler;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.Api;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.Audio;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.KException;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

public class SongManager implements IPlayerStateObservable {

	private Set<IPlayerStateObserver> playerStateObservers;
	
	private MediaPlayer songPlayer;
	
	private SongData songData;
	private boolean wasPlayed = false;
	
	private IScrobbler scrobbler;
	
	private boolean isLoading;
	private boolean isPlaying;
	private boolean isPrepared;
	
	private SongDAO songDAO;
	private Handler handler;
	
	public SongManager(IScrobbler scrobbler, SongDAO songDAO, Handler handler) {
		songPlayer = new MediaPlayer();
		this.scrobbler = scrobbler;
		this.songDAO = songDAO;
		this.handler = handler;
		isPrepared = false;
		playerStateObservers = new HashSet<IPlayerStateObserver>();
	}
	
	public SongManager(SongDAO songDAO, Handler handler) {
		this(null, songDAO, handler);
	}
	
	public void set(SongData songData) {
		this.songData = songData;
		isPrepared = false;
	}
	
	private Audio findSongOnPleercom(String artist, String title) throws SongNotFoundException, MalformedURLException, IOException, JSONException, KException {
		String q = artist + " " + title;
		List<Audio> audioList = Api.searchAudio(q, 1, 1);
	
		if (audioList.isEmpty()) {
			isLoading = false;
			asyncNotifyPlayerStateObservers();
			throw new SongNotFoundException();
		}
		return audioList.get(0);
	}
	
	public void prepare() throws MalformedURLException, IOException, JSONException, SongNotFoundException, KException {
		isLoading = true;
		Log.v("SongPlayer", "Player is loading");
		asyncNotifyPlayerStateObservers();
		songPlayer = new MediaPlayer();
		boolean songDataNeedUpdate = false;
		Audio audio = null;
		if(songData.getPleercomURL() == null) {
			songDataNeedUpdate = true;
			audio = findSongOnPleercom(getArtist(), getTitle());
			songPlayer.setDataSource(audio.url);
		} else {
			try {
				songPlayer.setDataSource(songData.getPleercomURL());
			} catch(IOException e) {
				songDataNeedUpdate = true;
				audio = findSongOnPleercom(getArtist(), getTitle());
			} catch(IllegalArgumentException e) {
				songDataNeedUpdate = true;
				audio = findSongOnPleercom(getArtist(), getTitle());
			}
		}
		songPlayer.prepare();
		if(songDataNeedUpdate) {
			songData = new SongData(songData.getId(), songData.getArtist(), songData.getTitle(), songData.getTrackId(), songData.getDate(), audio.url, songData.getCoverArtURL());
			songDAO.update(songData);
		}
		wasPlayed = false;
		isPrepared = true;
	}
	
	public void play() {
		isLoading = false;
		
		songPlayer.start();
		isPlaying = true;
		Log.v("SongListController", "Song" + songData.getArtist() + "-" + songData.getTitle() + "was started");
		if (!wasPlayed && scrobbler != null)
			scrobbler.scrobble(songData.getArtist(), songData.getTitle());
		wasPlayed = true;
		asyncNotifyPlayerStateObservers();
	}
	
	public void pause() {
		songPlayer.pause();
		isPlaying = false;
		asyncNotifyPlayerStateObservers();
	}
	
	public void stop() {
		songPlayer.stop();
		isLoading = false;
		isPlaying = false;
		wasPlayed = false;
		asyncNotifyPlayerStateObservers();
	}
	
	public void setPosition(int position) {
		songPlayer.seekTo(position);
	}
	
	public int getPosition() {
		return songPlayer.getCurrentPosition();
	}
	
	public boolean isLove() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setLove(boolean love) {
		// TODO Auto-generated method stub

	}
	
	public boolean isVkAdded() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void addToVk() {
		// TODO Auto-generated method stub

	}
	
	public void download() {
		// TODO Auto-generated method stub

	}
	
	public String getArtist() {
		return songData.getArtist();
	}
	
	public String getTitle() {
		return songData.getTitle();
	}
	
	public int getDuration() {
		return songPlayer.getDuration();
	}
	
	public Drawable getSongLogo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}

	public void addObserver(IPlayerStateObserver o) {
		Log.v("SongManager", "Add new observer"); 
		playerStateObservers.add(o);
	}

	public void removeObserver(IPlayerStateObserver o) {
		playerStateObservers.remove(o);
	}
	
	private void asyncNotifyPlayerStateObservers() {
		handler.post(new Runnable() {
			public void run() {
				notifyPlayerStateObservers();
			}
		});
	}

	public void notifyPlayerStateObservers() {
		for (IPlayerStateObserver o : playerStateObservers)
			o.updatePlayerState();
	}

	public void setScrobbler(IScrobbler scrobbler) {
		this.scrobbler = scrobbler;
	}

	public SongData getSongData() {
		return songData;
	}

	public void release() {
		songPlayer.release();
		isPlaying = false;
		isLoading = false;
		isPrepared = true;
		Log.v("SongPlayer", "Player is released");
		asyncNotifyPlayerStateObservers();
	}

	public boolean isLoading() {
		return isLoading;
	}

	public boolean isPrepared() {
		return isPrepared;
	}

}
