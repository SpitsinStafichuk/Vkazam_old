package com.github.spitsinstafichuk.vkazam.model.observers;

public interface ISongProgressObserver {

    void updateProgress(int progress, int duration);
}
