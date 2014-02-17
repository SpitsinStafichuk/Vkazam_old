package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.FingerprintList;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongList;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.*;
import com.gracenote.mmid.MobileSDK.GNConfig;

public class RecognizeListManager implements IRecognizeStatusObservable, IRecognizeResultObservable, 
											 IRecognizeResultObserver, IRecognizeStatusObserver {
	public static final String RECOGNIZING_SUCCESS = "Recognizing success";
	public static final String ALL_RECOGNIZED = "All recognized";
	private static final String TAG = "RecognizeListManager";
	
	private Set<IRecognizeStatusObserver> recognizeStatusObservers;
	private Set<IRecognizeResultObserver> recognizeResultObservers;
    private Set<IFingerQueueListener> fingerQueueListeners;
	
    private RecognizeManager recognizeManager;
    private boolean isRecognizing = false;
    private FingerprintList fingerprints;
    private List<FingerprintData> fingerprintsQueue;
    private SongList songs;
    private FingerprintData currentFingerprint;
    private boolean autorecognizing = false;
	
	public RecognizeListManager(GNConfig config, FingerprintList fingerprintList, SongList songList) {
		this.fingerprints = fingerprintList;
		fingerprintsQueue = new LinkedList<FingerprintData>();
		this.songs = songList;
		recognizeManager = new RecognizeManager(config);
		recognizeManager.addRecognizeResultObserver(this);
		recognizeManager.addRecognizeStatusObserver(this);
        recognizeStatusObservers = new HashSet<IRecognizeStatusObserver>();
        recognizeResultObservers = new HashSet<IRecognizeResultObserver>();
        fingerQueueListeners = new HashSet<IFingerQueueListener>();
	}

	public void recognizeFingerprints() {
		isRecognizing = true;
		autorecognizing = true;
		if(fingerprintsQueue.isEmpty()) {
			if(fingerprints.isEmpty()) {
				autorecognizing = false;
				isRecognizing = false;
				onRecognizeStatusChanged(ALL_RECOGNIZED);
				return;
			} else {
				fingerprintsQueue.add((FingerprintData) fingerprints.get(0));
                ((FingerprintData)fingerprints.get(0)).setInQueueForRecognizing(true);

                for (IFingerQueueListener listener : fingerQueueListeners) {
                    listener.addElementToQueue((FingerprintData) fingerprints.get(0));
                }
			}
		}
		currentFingerprint = fingerprintsQueue.get(0);
		recognizeManager.recognizeFingerprint(currentFingerprint);
	}
	
	public void addFingerprintToQueue(FingerprintData fingerprint) {
		if(fingerprints.contains(fingerprint)) {
			fingerprintsQueue.add(fingerprint);

            for (IFingerQueueListener listener : fingerQueueListeners) {
                listener.addElementToQueue(fingerprint);
            }

			if(!isRecognizing) {
				isRecognizing = true;
				currentFingerprint = fingerprintsQueue.get(0);
				recognizeManager.recognizeFingerprint(currentFingerprint);
			}
		} else {
			throw new IllegalArgumentException("You can only add elements from fingerprints list, which have been passed to the constructor");
		}
	}
	
	public void removeFingerprintFromQueue(FingerprintData fingerprint) {
		fingerprintsQueue.remove(fingerprint);

        for (IFingerQueueListener listener : fingerQueueListeners) {
            listener.removeElementFromQueue((FingerprintData) fingerprints.get(0));
        }

		if(fingerprint == currentFingerprint) {
			recognizeManager.recognizeFingerprintCancel();
			if(isRecognizing) {
				if(fingerprintsQueue.isEmpty()) {
					if(fingerprints.isEmpty() || !autorecognizing) {
						autorecognizing = false;
						isRecognizing = false;
						onRecognizeStatusChanged(ALL_RECOGNIZED);
						return;
					} else {
						fingerprintsQueue.add((FingerprintData) fingerprints.get(0));
                        ((FingerprintData)fingerprints.get(0)).setInQueueForRecognizing(true);

                        for (IFingerQueueListener listener : fingerQueueListeners) {
                            listener.addElementToQueue((FingerprintData) fingerprints.get(0));
                        }
					}
				}
				currentFingerprint = fingerprintsQueue.get(0);
				recognizeManager.recognizeFingerprint(currentFingerprint);
			}
		}
	}

    public void addQueueListener(IFingerQueueListener listener) {
        fingerQueueListeners.add(listener);
    }

    public void removeQueueListener(IFingerQueueListener listener) {
        fingerQueueListeners.remove(listener);
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
        currentFingerprint.setRecognizeStatus(status);
		notifyRecognizeStatusObservers(status);
	}

    public int getPositionOfCurrentFingerprint() {
        return fingerprints.indexOf(currentFingerprint);
    }

	@Override
	public void onRecognizeResult(int errorCode, SongData songData) {
		notifyRecognizeResultObservers(errorCode, songData);
		if (songData != null) {
			currentFingerprint.setRecognizeStatus(songData.getArtist() + " - " + songData.getTitle());
		} else {
			currentFingerprint.setRecognizeStatus("Music not identified");
		}
		if(songData != null) {
			songs.add(songData);
		}
		if(errorCode != 5001) {
			fingerprintsQueue.remove(0);
			fingerprints.remove(currentFingerprint);
		} else {
			autorecognizing = false;
			isRecognizing = false;
			onRecognizeStatusChanged(ALL_RECOGNIZED);
			return;
		}
		if(isRecognizing) {
			if(fingerprintsQueue.isEmpty()) {
				if(fingerprints.isEmpty() || !autorecognizing) {
					autorecognizing = false;
					isRecognizing = false;
					onRecognizeStatusChanged(ALL_RECOGNIZED);
					return;
				} else {
					fingerprintsQueue.add((FingerprintData) fingerprints.get(0));
                    ((FingerprintData)fingerprints.get(0)).setInQueueForRecognizing(true);
				}
			}
			currentFingerprint = fingerprintsQueue.get(0);
			recognizeManager.recognizeFingerprint(currentFingerprint);
		}
	}

    public void cancelAutoRecognize() {
        autorecognizing = false;
    }
}
