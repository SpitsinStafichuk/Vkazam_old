package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;

public interface ISearchResultObservable {
	
	void addObserver(ISearchResultObserver o);
	void removeObserver(ISearchResultObserver o);
	void notifySearchResultObservers(SongData songData);
}
