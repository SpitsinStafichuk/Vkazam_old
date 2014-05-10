package com.github.spitsinstafichuk.vkazam.model.observers;

public interface ISongDAOObservable {

	void addObserver(ISongDAOObserver o);
	void removeObserver(ISongDAOObserver o);
	void notifySongDAOObservers();
}
