package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerMediaPlayer;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.*;
import org.json.JSONException;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.SongNotFoundException;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.Api;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.Audio;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.KException;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

public class SongManager implements ISongInfoObserverable, ISongProgressObservable {

    private Set<ISongInfoObserver> songInfoObservers;
    private Set<ISongProgressObserver> songProgressObservers;
	
	private MicroScrobblerMediaPlayer songPlayer;
    private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener;
    private MediaPlayer.OnCompletionListener onCompletionListener;
	
	private DatabaseSongData songData;
    private int positionInList;
	
	private Handler handler;
	
	private Context context;
	private Scrobbler scrobbler;
    private ScheduledThreadPoolExecutor songProgressTimer;
    
    private boolean wasPlayed = false;
	
	public SongManager(Handler handler, Context context, Scrobbler scrobbler) {
		songPlayer = MicroScrobblerMediaPlayer.getInstance();
		this.handler = handler;
		this.context = context;
		this.scrobbler = scrobbler;
        songInfoObservers = new HashSet<ISongInfoObserver>();
        songProgressObservers = new HashSet<ISongProgressObserver>();

        songProgressTimer = new ScheduledThreadPoolExecutor(1);
        songProgressTimer.scheduleWithFixedDelay(new Thread() {

            @Override
            public void run() {
                if ((songData != null)) {
                    asyncNotifySongProgressObservers();
                }
            }

        }, 0, 1000, TimeUnit.MILLISECONDS);
	}
	
	public void set(DatabaseSongData songData, int positionInList) {
		this.songData = songData;
        this.positionInList = positionInList;
        wasPlayed = false;
        //isPrepared = false;
        asyncNotifySongInfoObservers();
	}
	
	private Audio findSongOnPleercom(String artist, String title) throws SongNotFoundException, MalformedURLException, IOException, JSONException, KException {
		String q = artist + " " + title;
		Log.v("SongPlayer", "Searching audio from given API");
		List<Audio> audioList = Api.searchAudio(q, 1, 1);
		Log.v("SongPlayer", "Searching audio is complete");
	
		if (audioList.isEmpty()) {
			songPlayer.release();
			throw new SongNotFoundException();
		}
		return audioList.get(0);
	}
	
	public void prepare() throws IOException, JSONException, SongNotFoundException, KException {
		Log.v("SongPlayer", "Player is loading");
        songPlayer = MicroScrobblerMediaPlayer.getInstance();
        songPlayer.setLoadingState();
        songPlayer.setOnCompletionListener(onCompletionListener);
        songPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
		Log.v("SongPlayer", "Player is reconstructed");
		boolean songDataNeedUpdate = false;
		Audio audio = null;
		if(songData.getPleercomUrl() == null) {
			songDataNeedUpdate = true;
			audio = findSongOnPleercom(getArtist(), getTitle());
            Log.v("SongPlayer", "new Pleercomurl is " + audio.url);
			songPlayer.setDataSource(audio.url);
		} else {
			try {
				Log.v("SongPlayer", "Pleercomurl is " + songData.getPleercomUrl());
				songPlayer.setDataSource(songData.getPleercomUrl());
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
			songData.setPleercomUrl(audio.url);
		}
	}
	
	public void play() {
		Log.v("SongListController", "Song" + songData.getArtist() + "-" + songData.getTitle() + "was started");
		if(!wasPlayed) {
			scrobbler.sendLastFMTrackStarted(getArtist(), getTitle(), songData.getAlbum(), songPlayer.getDuration());
			wasPlayed = true;
		} else {
			scrobbler.sendLastFMTrackUnpaused(getArtist(), getTitle(), songData.getAlbum(), songPlayer.getDuration() , songPlayer.getCurrentPosition());
		}
		songPlayer.start();
	}
	
	public void pause() {
		scrobbler.sendLastFMTrackPaused(getArtist(), getTitle(), songData.getAlbum(), songPlayer.getDuration());
		songPlayer.pause();
	}
	
	public void stop() {
		songPlayer.stop();
	}
	
	public String getArtist() {
		return songData.getArtist();
	}
	
	public String getTitle() {
		return songData.getTitle();
	}
	
	public boolean isPlaying() {
		return songPlayer.isPlaying();
	}

	public DatabaseSongData getSongData() {
		return songData;
	}

    public int getPositionInList() {
        return positionInList;
    }

	public void release() {
		if(songData != null)
			scrobbler.sendLastFMPlaybackCompleted(getArtist(), getTitle(), songData.getAlbum(), songPlayer.getDuration());
		songPlayer.release();
		Log.v("SongPlayer", "Player is released");
	}

	public boolean isLoading() {
		return songPlayer.isLoading();
	}

	public boolean isPrepared() {
		return songPlayer.isPrepared();
	}

    public synchronized void seekTo(int percent) {
        if (songPlayer.getDuration() != -1) {
            songPlayer.seekTo(songPlayer.getDuration() * percent / 100);
        }
    }

    public synchronized void setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener listener) {
        onBufferingUpdateListener = listener;
    }

    public synchronized void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        onCompletionListener = listener;
    }

    @Override
    public void addSongIngoObserver(ISongInfoObserver o) {
        songInfoObservers.add(o);
    }

    @Override
    public void removeSongIngoObserver(ISongInfoObserver o) {
        songInfoObservers.remove(o);
    }

    @Override
    public void notifySongInfoObservers() {
        for (ISongInfoObserver o : songInfoObservers) {
            o.updateSongInfo();
        }
    }

    private void asyncNotifySongInfoObservers() {
        handler.post(new Runnable() {
            public void run() {
                notifySongInfoObservers();
            }
        });
    }

    @Override
    public void addSongProgressObserver(ISongProgressObserver o) {
        songProgressObservers.add(o);
    }

    @Override
    public void removeSongProgressObserver(ISongProgressObserver o) {
        songProgressObservers.remove(o);
    }

    @Override
    public void notifySongProgressObservers(boolean isPrepared) {
        for (ISongProgressObserver o : songProgressObservers) {
            if (isPrepared) {
                o.updateProgress(songPlayer.getCurrentPosition(), songPlayer.getDuration());
            } else {
                o.updateProgress(0, -1);
            }
        }
    }

    private void asyncNotifySongProgressObservers() {
        handler.post(new Runnable() {
            public void run() {
                notifySongProgressObservers(isPrepared());
            }
        });
    }
}
