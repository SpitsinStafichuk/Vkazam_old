package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;

public interface ISearchCoverArtStatusObservable {
	
	void addObserver(ISearchCoverArtStatusObserver o);
	void removeObserver(ISearchCoverArtStatusObserver o);
	void notifySearchStatusObservers(SongData songData);
}
