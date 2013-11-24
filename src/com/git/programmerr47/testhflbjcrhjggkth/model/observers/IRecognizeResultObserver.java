package com.git.programmerr47.testhflbjcrhjggkth.model.observers;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongData.SongDataBuilder;

public interface IRecognizeResultObserver {

	void onRecognizeResult(SongDataBuilder songData);
}
