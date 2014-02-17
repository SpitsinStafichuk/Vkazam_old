package com.git.programmerr47.vkazam.model.observers;

public interface IFingerprintDAOObservable {

	void addObserver(IFingerprintDAOObserver o);
	void removeObserver(IFingerprintDAOObserver o);
	void notifyFigerprintDAOObservers();
}
