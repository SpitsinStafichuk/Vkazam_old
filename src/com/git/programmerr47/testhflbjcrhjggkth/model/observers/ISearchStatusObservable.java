package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

public interface ISearchStatusObservable {
	
	void addObserver(ISearchStatusObserver o);
	void removeObserver(ISearchStatusObserver o);
	void notifySearchStatusObservers(String trackId);
}
