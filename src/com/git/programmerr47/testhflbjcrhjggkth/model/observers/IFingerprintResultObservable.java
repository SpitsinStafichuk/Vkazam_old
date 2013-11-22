package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

public interface IFingerprintResultObservable {
	
	void addObserver(IFingerprintResultObserver o);
	void removeObserver(IFingerprintResultObserver o);
	void notifyFingerprintResultObservers(String fingerprint);
}
