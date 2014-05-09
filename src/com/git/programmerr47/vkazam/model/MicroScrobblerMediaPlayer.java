package com.git.programmerr47.vkazam.model;

import android.media.MediaPlayer;
import android.os.Handler;
import com.git.programmerr47.vkazam.model.observers.IPlayerStateObservable;
import com.git.programmerr47.vkazam.model.observers.IPlayerStateObserver;
import com.git.programmerr47.vkazam.model.observers.IPlayerStateObservable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Deprecated
public class MicroScrobblerMediaPlayer extends MediaPlayer implements IPlayerStateObservable {
    private static MicroScrobblerMediaPlayer instance;
    private static Set<IPlayerStateObserver> playerStateObservers = new HashSet<IPlayerStateObserver>();
    private static Handler handler;

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

    public static void setHandler(Handler h) {
        handler = h;
    }

    @Override
    public void prepare() throws IOException {
        super.prepare();

        isPlaying = false;
        isLoading = false;
        isPrepared = true;
        asyncNotifyPlayerStateObservers();
    }

    @Override
    public void release() {
        super.release();
        instance = null;

        isPlaying = false;
        isLoading = false;
        isPrepared = false;
        asyncNotifyPlayerStateObservers();
    }

    @Override
    public void start() {
        super.start();

        isPlaying = true;
        isPrepared = true;
        isLoading = false;
        asyncNotifyPlayerStateObservers();
    }

    @Override
    public void pause() {
        super.pause();

        isPlaying = false;
        isPrepared = true;
        isLoading = false;
        asyncNotifyPlayerStateObservers();
    }

    @Override
    public void stop() {
        super.stop();

        isPlaying = false;
        isPrepared = true;
        isLoading = false;
        asyncNotifyPlayerStateObservers();
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
        asyncNotifyPlayerStateObservers();
    }

    @Override
    public void addPlayerStateObserver(IPlayerStateObserver o) {
        playerStateObservers.add(o);
    }

    @Override
    public void removePlayerStateObserver(IPlayerStateObserver o) {
        playerStateObservers.remove(o);
    }

    private void asyncNotifyPlayerStateObservers() {
        handler.post(new Runnable() {
            public void run() {
                notifyPlayerStateObservers();
            }
        });
    }

    @Override
    public void notifyPlayerStateObservers() {
        for (IPlayerStateObserver o : playerStateObservers)
            o.updatePlayerState();
    }
}
