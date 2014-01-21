package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeResultObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeResultObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;
import com.gracenote.mmid.MobileSDK.GNConfig;

public class RecognizeListManager implements IRecognizeStatusObservable, IRecognizeResultObservable, 
											 IRecognizeResultObserver, IRecognizeStatusObserver {
	public static final String RECOGNIZING_SUCCESS = "Recognizing success";
	private static final String TAG = "Recognizing";
	
	private Set<IRecognizeStatusObserver> recognizeStatusObservers;
	private Set<IRecognizeResultObserver> recognizeResultObservers;
	
	private GNConfig config;
    private RecognizeManager recognizeManager;
    private boolean isRecognizing = false;
    private List<FingerprintData> fingerprints;
	
	public RecognizeListManager(GNConfig config) {
		this.config = config;
		recognizeManager = new RecognizeManager(config);
		recognizeManager.addRecognizeResultObserver(this);
		recognizeManager.addRecognizeStatusObserver(this);
        recognizeStatusObservers = new HashSet<IRecognizeStatusObserver>();
        recognizeResultObservers = new HashSet<IRecognizeResultObserver>();
	}

	public void recognizeFingerprints(List<FingerprintData> fingerprints) {
		if(!fingerprints.isEmpty()) {
			isRecognizing = true;
			this.fingerprints = fingerprints;
			recognizeManager.recognizeFingerprint(fingerprints.get(0));
		}
	}

	public void recognizeFingerprintCancel() {
		isRecognizing = false;
		recognizeManager.recognizeFingerprintCancel();
	}

	@Override
	public void addRecognizeStatusObserver(IRecognizeStatusObserver o) {
		recognizeStatusObservers.add(o);
	}

	@Override
	public void removeRecognizeStatusObserver(IRecognizeStatusObserver o) {
		recognizeStatusObservers.remove(o);
	}

	@Override
	public void addRecognizeResultObserver(IRecognizeResultObserver o) {
		recognizeResultObservers.add(o);
        Log.v("RecognizeManager", "after adding, observers = " + recognizeResultObservers.size());
	}

	@Override
	public void removeRecognizeResultObserver(IRecognizeResultObserver o) {
		recognizeResultObservers.remove(o);
        Log.v("RecognizeManager", "after removing, observers = " + recognizeResultObservers.size());
	}

	@Override
	public void notifyRecognizeResultObservers(int errorCode, SongData songData) {
		for(IRecognizeResultObserver o : recognizeResultObservers)
			o.onRecognizeResult(errorCode, songData);
	}

	@Override
	public void notifyRecognizeStatusObservers(String status) {
		for(IRecognizeStatusObserver o : recognizeStatusObservers)
			o.onRecognizeStatusChanged(status);
	}

	@Override
	public void onRecognizeStatusChanged(String status) {
		notifyRecognizeStatusObservers(status);
	}

	@Override
	public void onRecognizeResult(int errorCode, SongData songData) {
		notifyRecognizeResultObservers(errorCode, songData);
		if(isRecognizing) {
			if(errorCode != 5001) {
				if(!fingerprints.isEmpty()) {
					fingerprints.remove(0);
					recognizeManager.recognizeFingerprint(fingerprints.get(0));
				}
			} else {
				isRecognizing = false;
			}
		}
	}
}
