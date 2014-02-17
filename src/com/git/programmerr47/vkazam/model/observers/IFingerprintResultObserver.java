package com.git.programmerr47.vkazam.model.observers;

public interface IFingerprintResultObserver {

	void onFingerprintResult(int errorCode, String fingerprint);
}
