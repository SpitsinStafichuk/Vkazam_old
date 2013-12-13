package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;


public interface IRecognizeResultObservable {
	
	void addRecognizeResultObserver(IRecognizeResultObserver o);
	void removeRecognizeResultObserver(IRecognizeResultObserver o);
	void notifyRecognizeResultObservers(SongData songData);
}
