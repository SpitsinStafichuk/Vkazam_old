package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

import android.widget.Toast;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.model.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.FingerprintsDeque;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeResultObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;
import com.git.programmerr47.testhflbjcrhjggkth.utils.NetworkUtils;
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
		Log.v("Fingers", "Now queue is not empty: size = " + fingerprintsDeque.size());
		currentFinger = (FingerprintData)fingerprintsDeque.getFirst();
		storageRacognizeManager.recognizeFingerprint(currentFinger, false);
	}

    @Override
    public void onEmpty() {
        Log.v("Fingers", "Now queue is empty");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view);
        if (prefs.getBoolean("settingsAutoRecognize", false)) {
            if (NetworkUtils.isNetworkAvailable(view)) {
                if (model.getFingerprintList().size() != 0) {
                    if (listView != null) {
                        listView.performItemClick(adapter.getView(0, null, null), 0, adapter.getItemId(0));
                    } else {
                        FingerprintData data = (FingerprintData) model.getFingerprintList().get(0);
                        data.setInQueueForRecognizing(true);
                        model.getFingerprintsDeque().addLast(data);
                    }
                } else {
                    Toast.makeText(view, view.getString(R.string.empty_fingerlist), Toast.LENGTH_SHORT);
                }
            } else {
                Toast.makeText(view, view.getString(R.string.network_not_available), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void startRecognizingIfDequeIsEmpty() {
        if (fingerprintsDeque.isEmpty()) {
            onEmpty();
        }
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
            int beforeSize = model.getFingerprintList().size();
			model.getFingerprintList().remove(currentFinger);
            Log.v("Fingers", "Listsize(controller) after deletion is " + model.getFingerprintList().size());
            int afterSize = model.getFingerprintList().size();
            if (beforeSize > afterSize) {
                fingerprintsDeque.pollFirst();
                Log.v("Fingers", "(controller) after deletion deque.size() = " + fingerprintsDeque.size());
            }
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
