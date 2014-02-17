package com.git.programmerr47.vkazam.model.observers;

public interface IFingerprintStatusObservable {
	
	void addFingerprintStatusObserver(IFingerprintStatusObserver o);
	void removeFingerprintStatusObserver(IFingerprintStatusObserver o);
	void notifyFingerprintStatusObservers(String status);
}
