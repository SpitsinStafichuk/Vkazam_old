package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

public interface ISongDAOObservable {

	void addObserver(ISongDAOObserver o);
	void removeObserver(ISongDAOObserver o);
	void notifySongDAOObservers();
}
