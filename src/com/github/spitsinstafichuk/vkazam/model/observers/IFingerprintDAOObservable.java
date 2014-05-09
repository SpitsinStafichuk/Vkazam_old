package com.github.spitsinstafichuk.vkazam.model.observers;

public interface IFingerprintDAOObservable {

	void addObserver(IFingerprintDAOObserver o);
	void removeObserver(IFingerprintDAOObserver o);
	void notifyFigerprintDAOObservers();
}
