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
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.Api;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.Audio;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.KException;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

public class SongManager implements IPlayerStateObservable {

	private Set<IPlayerStateObserver> playerStateObservers;
	
	private MediaPlayer songPlayer;
	
	private SongData songData;
	
	private boolean isLoading;
	private boolean isPlaying;
	private boolean isPrepared;
	
	private SongDAO songDAO;
	private Handler handler;
	
	private Context context;
	
	public SongManager(SongDAO songDAO, Handler handler, Context context) {
		songPlayer = new MediaPlayer();
		this.songDAO = songDAO;
		this.handler = handler;
		this.context = context;
		isPrepared = false;
		playerStateObservers = new HashSet<IPlayerStateObserver>();
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
				songPlayer.setDataSource(audio.url);
			} catch(IllegalArgumentException e) {
				songDataNeedUpdate = true;
				audio = findSongOnPleercom(getArtist(), getTitle());
				songPlayer.setDataSource(audio.url);
			} 
		}
		songPlayer.prepare();
		if(songDataNeedUpdate) {
			songData.setPleercomURL(audio.url);
			songDAO.update(songData);
		}
		isPrepared = true;
	}
	
	private void sendLastFMPlaybackCompleted() {
		context.sendBroadcast(new Intent("fm.last.android.playbackcomplete"));
	}

	private void sendLastFMTrackPaused() {
		context.sendBroadcast(new Intent("fm.last.android.playbackpaused"));
	}

	private void sendLastFMTrackStarted() {
		Intent localIntent = new Intent("fm.last.android.metachanged");
		localIntent.putExtra("artist", getArtist());
		localIntent.putExtra("track", getTitle());
		//localIntent.putExtra("album", getAlbumName());
		localIntent.putExtra("duration", songPlayer.getDuration());
		context.sendBroadcast(localIntent);
	}

	private void sendLastFMTrackUnpaused() {
		context.sendBroadcast(new Intent("fm.last.android.metachanged").putExtra("position", songPlayer.getCurrentPosition()));
	}
	
	public void play() {
		isLoading = false;
		
		songPlayer.start();
		isPlaying = true;
		Log.v("SongListController", "Song" + songData.getArtist() + "-" + songData.getTitle() + "was started");
		sendLastFMTrackStarted();
		asyncNotifyPlayerStateObservers();
	}
	
	public void pause() {
		sendLastFMPlaybackCompleted();
		songPlayer.pause();
		isPlaying = false;
		asyncNotifyPlayerStateObservers();
	}
	
	public void stop() {
		songPlayer.stop();
		isLoading = false;
		isPlaying = false;
		asyncNotifyPlayerStateObservers();
	}
	
	public void setPosition(int position) {
		songPlayer.seekTo(position);
	}
	
	public int getPosition() {
		return songPlayer.getCurrentPosition();
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
