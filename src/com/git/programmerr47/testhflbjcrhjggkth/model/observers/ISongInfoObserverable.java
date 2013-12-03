package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

public interface ISongInfoObserverable {
    void addSongIngoObserver(ISongInfoObserver o);
    void removeSongIngoObserver(ISongInfoObserver o);
    void notifySongInfoObservers();
}
