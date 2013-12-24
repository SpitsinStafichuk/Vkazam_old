package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerMediaPlayer;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.SongNotFoundException;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongInfoObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongInfoObserverable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongProgressObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongProgressObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.KException;

public class SongManager implements ISongInfoObserverable, ISongProgressObservable {
	private final static String TAG = "SongManager";

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
    
    private com.perm.kate.api.Api vkApi;
	
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
	
	public void set(DatabaseSongData songData, int positionInList, com.perm.kate.api.Api vkApi) {
		this.vkApi = vkApi;
		this.songData = songData;
        this.positionInList = positionInList;
        wasPlayed = false;
        //isPrepared = false;
        asyncNotifySongInfoObservers();
	}
	
	private boolean findPPAudio() {
		try {
			if(songData.getPleercomUrl() == null) {
				songData.findPPAudio();
	            Log.i(TAG, "new Pleercomurl: " + songData.getPleercomUrl());
	            try {
	        		songPlayer.setDataSource(songData.getPleercomUrl());
	        	} catch (IllegalArgumentException e) {
	        		songData.findPPAudio();
	        	} catch (IOException e) {
	        		songData.findPPAudio();
	        	}	
			} else {
				try {
					Log.i(TAG, "Pleercomurl: " + songData.getPleercomUrl());
					songPlayer.setDataSource(songData.getPleercomUrl());
				} catch(IOException e) {
					songData.findPPAudio();
				} catch(IllegalArgumentException e) {
					songData.findPPAudio();
				} 
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private boolean findVkAudio() {
		try {
			if(songData.getVkAudioId() == null) {
				String vkUrl;
				vkUrl = songData.findVkAudio(vkApi);
				Log.i(TAG, "vk audio id: " + songData.getVkAudioId());
				Log.i(TAG, "vk audio url: " + vkUrl);
				try {
	        		songPlayer.setDataSource(vkUrl);
	        	} catch (IllegalArgumentException e) {
	        		songData.findPPAudio();
	        	} catch (IOException e) {
	        		songData.findPPAudio();
	        	}	
			} else {
				try {
					List<com.perm.kate.api.Audio> audioList;
					try {
						audioList = vkApi.getAudioById(songData.getVkAudioId(), null, null);
					} catch (Exception e) {
						return false;
					} 
					if (audioList.isEmpty()) {
						return false;
					}
					String vkUrl = audioList.get(0).url;
					Log.i(TAG, "vk audio id: " + songData.getVkAudioId());
					Log.i(TAG, "vk audio url: " + vkUrl);
					songPlayer.setDataSource(vkUrl);
				} catch(IOException e) {
					songData.findVkAudio(vkApi);
				} catch(IllegalArgumentException e) {
					songData.findVkAudio(vkApi);
				} 
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	//есть сомнения по поводу корректности проверки рабочий ли url для песни перед попыткой его обновить: возможно, помимо setDataSource, стоит также вызывать prepare
    public void prepare(boolean force) throws IOException, JSONException, SongNotFoundException, KException, com.perm.kate.api.KException {
		Log.v(TAG, "Player is loading");
        songPlayer = MicroScrobblerMediaPlayer.getInstance();
        songPlayer.setLoadingState();
        songPlayer.setOnCompletionListener(onCompletionListener);
        songPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
		Log.v(TAG, "Player is reconstructed");
		boolean found = false;
		if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("settingsVkConnection", false) ||
           !PreferenceManager.getDefaultSharedPreferences(context).getBoolean("settingsVkUrls", false)) {
			found = findPPAudio();
			if(!found && !force) {
				if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("settingsVkConnection", false)) {
					found = findVkAudio();
				}
			}
		} else {
			found = findVkAudio();
			if(!found && !force) {
				found = findPPAudio();
			}
		}
		Log.v(TAG, "found: " + found);
		if(!found) {
			throw new SongNotFoundException();
		}
		songPlayer.prepare();
	}
	
	public void play() {
		Log.v("SongListController", "Song" + songData.getArtist() + "-" + songData.getTitle() + "was started");
		if(!wasPlayed) {
			scrobbler.sendLastFMTrackStarted(getArtist(), getTitle(), songData.getAlbum(), songPlayer.getDuration());
			if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("settingsVkAudioBroadcast", false)) {
				new Thread() {
					@Override
					public void run() {
						try {
							vkApi.setStatus(null, songData.getVkAudioId());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
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
		Log.v(TAG, "Player is released");
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
