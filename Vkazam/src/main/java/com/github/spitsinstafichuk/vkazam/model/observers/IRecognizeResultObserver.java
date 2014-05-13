package com.github.spitsinstafichuk.vkazam.model.observers;

import com.github.spitsinstafichuk.vkazam.model.SongData;

public interface IRecognizeResultObserver {

    void onRecognizeResult(int errorCode, SongData songData);
}
