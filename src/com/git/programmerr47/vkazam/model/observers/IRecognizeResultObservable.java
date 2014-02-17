package com.git.programmerr47.vkazam.model.observers;

import com.git.programmerr47.vkazam.model.SongData;


public interface IRecognizeResultObservable {
	
	void addRecognizeResultObserver(IRecognizeResultObserver o);
	void removeRecognizeResultObserver(IRecognizeResultObserver o);
	void notifyRecognizeResultObservers(int errorCode, SongData songData);
}
