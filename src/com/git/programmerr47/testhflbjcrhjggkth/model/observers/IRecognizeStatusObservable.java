package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

public interface IRecognizeStatusObservable {
	
	void addObserver(IRecognizeStatusObserver o);
	void removeObserver(IRecognizeStatusObserver o);
	void notifyRecognizeStatusObservers(String status);
}
