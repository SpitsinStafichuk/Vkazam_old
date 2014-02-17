package com.git.programmerr47.vkazam.model.observers;

public interface ISongInfoObserverable {
    void addSongIngoObserver(ISongInfoObserver o);
    void removeSongIngoObserver(ISongInfoObserver o);
    void notifySongInfoObservers();
}
