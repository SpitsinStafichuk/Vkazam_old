package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.FingerprintsDeque;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeResultObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.FingerprintListAdapter;

public class FingerprintListController implements FingerprintsDeque.OnDequeStateListener,
                                                  IRecognizeStatusObserver,
                                                  IRecognizeResultObserver {
	
	MicroScrobblerModel model;
	RecognizeManager storageRacognizeManager;
	FingerprintsDeque fingerprintsDeque;
	FingerprintData currentFinger;
	FingerprintListAdapter adapter;
	Activity view;
	
	public FingerprintListController(Fragment fragment, FingerprintListAdapter adapter) {
		this.adapter = adapter;
		view = fragment.getActivity();
		model = RecognizeServiceConnection.getModel();
		storageRacognizeManager = model.getStorageRecognizeManager();
		fingerprintsDeque = model.getFingerprintsDeque();
		fingerprintsDeque.setOnDequeStateListener(this);
		storageRacognizeManager.addRecognizeStatusObserver(this);
		storageRacognizeManager.addRecognizeResultObserver(this);
	}
	
	public void finish() {
		storageRacognizeManager.removeRecognizeStatusObserver(this);
		storageRacognizeManager.removeRecognizeResultObserver(this);
	}

	@Override
	public void onNonEmpty() {
		// TODO Auto-generated method stub
		Log.v("Fingers", "Now queue is not empty");
		Log.v("Fingers", "before fingerprintsDeque.size() = " + fingerprintsDeque.size());
		currentFinger = (FingerprintData)fingerprintsDeque.peekFirst();
		Log.v("Fingers", "after fingerprintsDeque.size() = " + fingerprintsDeque.size());
		storageRacognizeManager.recognizeFingerprint(currentFinger, false);
	}

	@Override
	public void onRecognizeResult(SongData songData) {
		Log.v("Fingers", "onRecognizeResult " + songData);
		if (songData != null) {
			model.getSongList().add(0, songData);
			model.getScrobbler().sendLastFMTrack(songData.getArtist(), songData.getTitle(), songData.getAlbum());
		}
		
		if (songData != null) {
			adapter.updateFingerStatus(currentFinger, songData.getArtist() + " - " + songData.getTitle());
		} else {
			adapter.updateFingerStatus(currentFinger, "Music not identified");
		}
        
		Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            	adapter.deletionFromList(currentFinger);
        		Log.v("Fingers", "before fingerprintsDeque.size() = " + fingerprintsDeque.size());
        		fingerprintsDeque.pollFirst();
        		Log.v("Fingers", "after fingerprintsDeque.size() = " + fingerprintsDeque.size());
                if (fingerprintsDeque.size() > 0) {
                	onNonEmpty();
                }
            }
        }, 1000);
	}

	@Override
	public void onRecognizeStatusChanged(String status) {
		// TODO Auto-generated method stub
		Log.v("Fingers", "onRecognizeStatusChanged " + status);
		adapter.updateFingerStatus(currentFinger, status);
	}

}
