package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

public interface IPlayerStateObservable {
	
	void addPlayerStateObserver(IPlayerStateObserver o);
	void removePlayerStateObserver(IPlayerStateObserver o);
	void notifyPlayerStateObservers();
}
