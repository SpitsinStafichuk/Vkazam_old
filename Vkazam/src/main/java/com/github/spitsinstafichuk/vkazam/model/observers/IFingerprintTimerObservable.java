package com.github.spitsinstafichuk.vkazam.model.observers;

public interface IFingerprintTimerObservable {

    void addFingerprintTimerObserver(IFingerprintTimerObserver o);

    void removeFingerprintTimerObserver(IFingerprintTimerObserver o);

    void notifyFingerprintObservers();
}
