package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

public interface IFingerprintStatusObservable {
	
	void addFingerprintStatusObserver(IFingerprintStatusObserver o);
	void removeFingerprintStatusObserver(IFingerprintStatusObserver o);
	void notifyFingerprintStatusObservers(String status);
}
