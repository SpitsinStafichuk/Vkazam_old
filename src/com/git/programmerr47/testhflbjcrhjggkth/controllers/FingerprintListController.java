package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

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
	ListView listView;
	Activity view;
	
	public FingerprintListController(Fragment fragment, FingerprintListAdapter adapter, ListView listView) {
		this.adapter = adapter;
		this.listView = listView;
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
		Log.v("Fingers", "Now queue is not empty: size = " + fingerprintsDeque.size());
		currentFinger = (FingerprintData)fingerprintsDeque.peekFirst();
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
			currentFinger.setRecognizeStatus(songData.getArtist() + " - " + songData.getTitle());
		} else {
			currentFinger.setRecognizeStatus("Music not identified");
		}
		
		int listPosition = model.getFingerprintList().indexOf(currentFinger);
		if ((listView != null) && (listView.getFirstVisiblePosition() <= listPosition) && (listView.getLastVisiblePosition() >= listPosition)) {
			currentFinger.setDeleting(true);
		} else {
			fingerprintsDeque.pollFirst();
    		Log.v("Fingers", "after deletion deque.size() = " + fingerprintsDeque.size());
			model.getFingerprintList().remove(currentFinger);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onRecognizeStatusChanged(String status) {
		// TODO Auto-generated method stub
		Log.v("Fingers", "onRecognizeStatusChanged " + status);
		currentFinger.setRecognizeStatus(status);
		adapter.notifyDataSetChanged();
	}
	
	public void setListView(ListView listView) {
		this.listView = listView;
	}

}
