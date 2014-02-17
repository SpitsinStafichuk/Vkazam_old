package com.git.programmerr47.vkazam.model.observers;

public interface ISongProgressObservable {
    void addSongProgressObserver(ISongProgressObserver o);
    void removeSongProgressObserver(ISongProgressObserver o);
    void notifySongProgressObservers(boolean isPrepared);
}
