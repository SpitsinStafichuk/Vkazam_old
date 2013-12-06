package com.git.programmerr47.testhflbjcrhjggkth.model;

import android.media.MediaPlayer;

import java.io.IOException;

//TODO I don't know what should be here
public class MicroScrobblerMediaPlayer extends MediaPlayer{
    private static MicroScrobblerMediaPlayer instance;

    private boolean isLoading;
    private boolean isPlaying;
    private boolean isPrepared;

    public static synchronized MicroScrobblerMediaPlayer getInstance() {
        if (instance == null) {
            instance = new MicroScrobblerMediaPlayer();
        }

        return instance;
    }

    private MicroScrobblerMediaPlayer() {

    }

    @Override
    public void prepare() throws IOException {
        super.prepare();

        isPlaying = false;
        isLoading = false;
        isPrepared = true;
    }

    @Override
    public void release() {
        super.release();
        instance = null;

        isPlaying = false;
        isLoading = false;
        isPrepared = false;
    }

    @Override
    public void start() {
        super.start();

        isPlaying = true;
        isPrepared = true;
        isLoading = false;
    }

    @Override
    public void pause() {
        super.pause();

        isPlaying = false;
        isPrepared = true;
        isLoading = false;
    }

    @Override
    public void stop() {
        super.stop();

        isPlaying = false;
        isPrepared = true;
        isLoading = false;
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setLoadingState() {
        isLoading = true;
    }
}
