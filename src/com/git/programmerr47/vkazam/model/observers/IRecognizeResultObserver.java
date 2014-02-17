package com.git.programmerr47.vkazam.model.observers;

import com.git.programmerr47.vkazam.model.SongData;

public interface IRecognizeResultObserver {

	void onRecognizeResult(int errorCode, SongData songData);
}
