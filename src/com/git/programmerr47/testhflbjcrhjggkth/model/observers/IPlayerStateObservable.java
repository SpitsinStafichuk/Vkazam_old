package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

public interface IPlayerStateObservable {
	
	void addObserver(IPlayerStateObserver o);
	void removeObserver(IPlayerStateObserver o);
	void notifyPlayerStateObservers();
}
