package com.github.spitsinstafichuk.vkazam.model.observers;

public interface IFingerprintResultObserver {

	void onFingerprintResult(int errorCode, String fingerprint);
}
