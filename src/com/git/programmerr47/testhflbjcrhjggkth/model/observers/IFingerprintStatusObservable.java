package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

public interface IFingerprintStatusObservable {
	
	void addObserver(IFingerprintStatusObserver o);
	void removeObserver(IFingerprintStatusObserver o);
	void notifyFingerprintStatusObservers(String status);
}
