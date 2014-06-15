package com.github.spitsinstafichuk.vkazam.model.observers;

import com.github.spitsinstafichuk.vkazam.model.SongData;


public interface IRecognizeResultObservable {

    void addRecognizeResultObserver(IRecognizeResultObserver o);

    void removeRecognizeResultObserver(IRecognizeResultObserver o);

    void notifyRecognizeResultObservers(int errorCode, SongData songData);
}
