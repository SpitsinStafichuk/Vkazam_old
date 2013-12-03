package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

public interface IFingerprintDAOObservable {

	void addObserver(IFingerprintDAOObserver o);
	void removeObserver(IFingerprintDAOObserver o);
	void notifyFigerprintDAOObservers();
}
