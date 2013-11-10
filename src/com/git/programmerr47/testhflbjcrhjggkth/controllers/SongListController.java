package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.json.JSONException;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.SongNotFoundException;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.ISongManager;
import com.google.sydym6.logic.database.data.ISongData;
import com.pleer.api.KException;

public class SongListController implements ISongListController {

	MicroScrobblerModel model;
	Fragment view;
	Thread preparingThread;
	
	public SongListController(Fragment view) {
		this.view = view;
		this.model = MicroScrobblerModel.getInstance();
	}
	
	@Override
	public List<ISongData> getList() {
		return model.getHistory();
	}

	@Override
	public void playPauseSong(final ISongData songData) {
		if(preparingThread != null) {
			ISongManager songManager = MicroScrobblerModel.getInstance().getSongManager();
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
	
	private void _playPauseSong(ISongData songData) {
		ISongManager songManager = MicroScrobblerModel.getInstance().getSongManager();
		if(songManager.getSongData() == songData) {
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
		view.getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast toast = Toast.makeText(view.getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}
	
	private void showToast(final String message, final int length) {
		view.getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast toast = Toast.makeText(view.getActivity().getApplicationContext(), message, length);
				toast.show();
			}
		});
	}

	@Override
	public boolean deleteSong(ISongData songData) {
		//return model.getRadioManager().deleteSongFromHistory(songData);
		return false;
	}
}
