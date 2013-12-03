package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.SongNotFoundException;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SongManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.KException;

public class SongController {
	
	MicroScrobblerModel model;
	Activity view;
	Thread preparingThread;
	
	public SongController(Activity view) {
		this.view = view;
		this.model = RecognizeServiceConnection.getModel();
	}

	public synchronized void playPauseSong(final DatabaseSongData songData, final int positionInList) {
		if(preparingThread != null) {
			SongManager songManager = model.getSongManager();
			songManager.set(null, -1);
			preparingThread.interrupt();
		}
		preparingThread = new Thread(){
			@Override
			public void run() {
				_playPauseSong(songData, positionInList);
				preparingThread = null;
			}
		};
		preparingThread.start();	
	}

    public synchronized void playPauseSong(int positionInList) {
        if (positionInList >= model.getSongList().size()) {
            positionInList = model.getSongList().size() - 1;
        } else if (positionInList < 0) {
            positionInList = 0;
        }
        this.playPauseSong(((DatabaseSongData)model.getSongList().get(positionInList)), positionInList);
    }

    public void seekTo(int percent) {
        model.getSongManager().seekTo(percent);
    }
	
	private void _playPauseSong(DatabaseSongData songData, int positionInList) {
		SongManager songManager = model.getSongManager();
		if(songData.equals(songManager.getSongData())) {
			Log.v("SongListController", "songManager.getSongData() == songData == " + songData);
			if(songManager.isPrepared())
				if(songManager.isPlaying()) {
					songManager.pause();
				} else {
					//TODO �������� ��� ���������� java.lang.IllegalStateException 
					songManager.play();
				}
		} else {
			if(songManager.isPlaying()) {
				songManager.stop();
			}
			songManager.release();
			songManager.set(songData, positionInList);
			Log.v("SongListController", "song was setted");
			try {
				songManager.prepare();
				Log.v("SongListController", "song was prepared ");
				songManager.play();
			} catch (SongNotFoundException e) {
				showToast("Song is not found");
				songManager.release();
				songManager.set(null, -1);
			} catch (MalformedURLException e) {
				showToast("Seems you haven't internet connection");
				songManager.release();
				songManager.set(null, -1);
			} catch (IOException e) {
				showToast("Seems you haven't internet connection");
				songManager.release();
				songManager.set(null, -1);
			} catch (JSONException e) {
				showToast(e.getLocalizedMessage());
				songManager.release();
				songManager.set(null, -1);
			} catch (KException e) {
				showToast(e.getLocalizedMessage());
				songManager.release();
				songManager.set(null, -1);
			}
		}
	}
	
	private void showToast(final String message) {
		view.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast toast = Toast.makeText(view.getApplicationContext(), message, Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}
}
