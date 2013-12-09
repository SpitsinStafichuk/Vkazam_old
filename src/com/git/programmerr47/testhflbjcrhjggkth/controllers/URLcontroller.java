package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerMediaPlayer;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SongManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObserver;

import java.io.IOException;
import java.net.MalformedURLException;

public class URLcontroller implements IPlayerStateObserver{

    private View currentElement;
    private String currentUrl;
    private MicroScrobblerMediaPlayer player;
    private MicroScrobblerModel model;
    private Thread preparingThread;

    public URLcontroller() {
        model = RecognizeServiceConnection.getModel();
        player = model.getPlayer();
        player.addObserver(this);
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        player.removeObserver(this);
    }

    public void setCurrentElement(View v) {
        if (currentElement != null) {
            ImageButton playPauseButton = (ImageButton) currentElement.findViewById(R.id.ppUrlListItemPlayPauseButton);
            ProgressBar progressBar = (ProgressBar) currentElement.findViewById(R.id.ppUrlListItemLoading);
            playPauseButton.setVisibility(View.VISIBLE);
            playPauseButton.setImageResource(android.R.drawable.ic_media_play);
            progressBar.setVisibility(View.GONE);
        }
        currentElement = v;
    }

    public View getCurrentElement() {
        return currentElement;
    }

    public synchronized void playPauseSong(final String url, final Activity activity) {
        if(preparingThread != null) {
            SongManager songManager = model.getSongManager();
            songManager.set(null, -1);
            preparingThread.interrupt();
        }
        preparingThread = new Thread(){
            @Override
            public void run() {
                _playPauseSong(url, activity);
                preparingThread = null;
            }
        };
        preparingThread.start();
    }

    private void _playPauseSong(String url, Activity activity) {
        if(url.equals(currentUrl)) {
            if(player.isPrepared())
                if(player.isPlaying()) {
                    player.pause();
                } else {
                    player.start();
                }
        } else {
            currentUrl = url;
            if(player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = model.getPlayer();
            try {
                player.setLoadingState();
                player.setDataSource(url);
            } catch (IOException e) {
                showToast(e.getLocalizedMessage(), activity);
            }
            Log.v("SongListController", "song was setted");
            try {
                player.prepare();
                Log.v("SongListController", "song was prepared ");
                player.start();
            } catch (MalformedURLException e) {
                showToast("Seems you haven't internet connection", activity);
                player.release();
            } catch (IOException e) {
                showToast("Seems you haven't internet connection", activity);
                player.release();
            }
        }
    }

    private void showToast(final String message, final Activity activity) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast toast = Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void updatePlayerState() {
        ImageButton playPauseButton = (ImageButton) currentElement.findViewById(R.id.ppUrlListItemPlayPauseButton);
        ProgressBar progressBar = (ProgressBar) currentElement.findViewById(R.id.ppUrlListItemLoading);

        if (player.isLoading()) {
            progressBar.setVisibility(View.VISIBLE);
            playPauseButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            if (player.isPrepared()) {
                playPauseButton.setVisibility(View.VISIBLE);
            } else {
                if (playPauseButton.getVisibility() == View.GONE)
                    progressBar.setVisibility(View.INVISIBLE);
            }
        }

        if (player.isPlaying()) {
            playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            playPauseButton.setImageResource(android.R.drawable.ic_media_play);
        }
    }
}
