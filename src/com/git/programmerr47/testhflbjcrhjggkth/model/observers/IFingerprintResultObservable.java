package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

public interface IFingerprintResultObservable {
	
	void addFingerprintResultObserver(IFingerprintResultObserver o);
	void removeFingerprintResultObserver(IFingerprintResultObserver o);
	void notifyFingerprintResultObservers(int errorCode, String fingerprint);
}
