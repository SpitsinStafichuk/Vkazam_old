package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;

public interface ISearchResultObserver {

	void updateSearchStatus(String resultStatus, SongData songData);
}
