package com.git.programmerr47.vkazam.controllers;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

import com.git.programmerr47.vkazam.model.FingerprintData;
import com.git.programmerr47.vkazam.model.MicroScrobblerModel;
import com.git.programmerr47.vkazam.model.RecognizeServiceConnection;
import com.git.programmerr47.vkazam.model.SongData;
import com.git.programmerr47.vkazam.model.managers.RecognizeManager;
import com.git.programmerr47.vkazam.model.observers.IRecognizeResultObserver;
import com.git.programmerr47.vkazam.model.observers.IRecognizeStatusObserver;
import com.git.programmerr47.vkazam.view.adapters.FingerprintListAdapter;
import com.git.programmerr47.vkazam.model.MicroScrobblerModel;
import com.git.programmerr47.vkazam.model.RecognizeServiceConnection;
import com.git.programmerr47.vkazam.model.SongData;
import com.git.programmerr47.vkazam.model.observers.IRecognizeResultObserver;
import com.git.programmerr47.vkazam.model.observers.IRecognizeStatusObserver;
import com.git.programmerr47.vkazam.view.adapters.FingerprintListAdapter;

public class FingerprintListController implements IRecognizeStatusObserver,
        IRecognizeResultObserver {
	
	MicroScrobblerModel model;
	FingerprintListAdapter adapter;
	Activity view;
	
	public FingerprintListController(Fragment fragment, FingerprintListAdapter adapter) {
		this.adapter = adapter;
		view = fragment.getActivity();
		model = RecognizeServiceConnection.getModel();
		model.getRecognizeListManager().addRecognizeStatusObserver(this);
		model.getRecognizeListManager().addRecognizeResultObserver(this);
	}
	
	public void finish() {
		model.getRecognizeListManager().removeRecognizeStatusObserver(this);
		model.getRecognizeListManager().removeRecognizeResultObserver(this);
	}

    @Override
	public void onRecognizeResult(int errorCode, SongData songData) {
		Log.v("Fingers", "onRecognizeResult " + songData);
		if (songData != null) {
			model.getScrobbler().sendLastFMTrack(songData.getArtist(), songData.getTitle(), songData.getAlbum());
		}
	}

	@Override
	public void onRecognizeStatusChanged(String status) {
		Log.v("Fingers", "onRecognizeStatusChanged " + status);
//        if (!((listView != null) &&
//            (model.getRecognizeListManager().getPositionOfCurrentFingerprint() >= listView.getFirstVisiblePosition()) &&
//            (model.getRecognizeListManager().getPositionOfCurrentFingerprint() <= listView.getLastVisiblePosition()))) {
//            Log.v("Fingers", "Adapter notify because list is invisible");
//            adapter.notifyDataSetChanged();
//        }
	}

}
