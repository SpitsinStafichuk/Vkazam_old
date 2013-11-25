package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
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
		this.model = MicroScrobblerModel.getInstance();
	}

	public synchronized void playPauseSong(final DatabaseSongData songData) {
		if(preparingThread != null) {
			SongManager songManager = MicroScrobblerModel.getInstance().getSongManager();
			songManager.set(null);
			preparingThread.interrupt();
		}
		preparingThread = new Thread(){
			@Override
			public void run() {
				_playPauseSong(songData);
				preparingThread = null;
			}
		};
		preparingThread.start();	
	}
	
	private void _playPauseSong(DatabaseSongData songData) {
		SongManager songManager = MicroScrobblerModel.getInstance().getSongManager();
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
			songManager.set(songData);
			Log.v("SongListController", "song was setted");
			try {
				songManager.prepare();
				Log.v("SongListController", "song was prepared ");
				songManager.play();
			} catch (SongNotFoundException e) {
				showToast("Song is not found");
				songManager.release();
				songManager.set(null);
			} catch (MalformedURLException e) {
				showToast("Seems you haven't internet connection");
				songManager.release();
				songManager.set(null);
			} catch (IOException e) {
				showToast("Seems you haven't internet connection");
				songManager.release();
				songManager.set(null);
			} catch (JSONException e) {
				showToast(e.getLocalizedMessage());
				songManager.release();
				songManager.set(null);
			} catch (KException e) {
				showToast(e.getLocalizedMessage());
				songManager.release();
				songManager.set(null);
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
