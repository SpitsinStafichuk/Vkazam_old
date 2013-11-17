package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;

public interface ISearchStatusObservable {
	
	void addObserver(ISearchStatusObserver o);
	void removeObserver(ISearchStatusObserver o);
	void notifySearchStatusObservers(SongData songData);
}
