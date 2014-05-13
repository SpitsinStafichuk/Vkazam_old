package com.github.spitsinstafichuk.vkazam.model.observers;

public interface IRecognizeStatusObservable {

    void addRecognizeStatusObserver(IRecognizeStatusObserver o);

    void removeRecognizeStatusObserver(IRecognizeStatusObserver o);

    void notifyRecognizeStatusObservers(String status);
}
