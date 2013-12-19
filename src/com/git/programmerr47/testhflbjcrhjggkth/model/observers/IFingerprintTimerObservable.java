package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

public interface IFingerprintTimerObservable {

    void addFingerprintTimerObserver(IFingerprintTimerObserver o);
    void removeFingerprintTimerObserver(IFingerprintTimerObserver o);
    void notifyFingerprintObservers();
}
