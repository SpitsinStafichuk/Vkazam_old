package com.github.spitsinstafichuk.vkazam.model.observers;

public interface IPlayerStateObservable {
	
	void addPlayerStateObserver(IPlayerStateObserver o);
	void removePlayerStateObserver(IPlayerStateObserver o);
	void notifyPlayerStateObservers();
}
