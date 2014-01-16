package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
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

public class URLcontroller extends SongController implements IPlayerStateObserver{
	private static final String TAG = "URLcontroller";
	
    private View currentElement;
    private String currentUrl;
    private SongManager songManager;

    public URLcontroller(FragmentActivity view) {
        super(view);
        model = RecognizeServiceConnection.getModel();
        songManager = model.getSongManager();
        model.getPlayer().addPlayerStateObserver(this);
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        model.getPlayer().removePlayerStateObserver(this);
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
        Log.v(TAG, "currentElement = " + currentElement);
    }

    public View getCurrentElement() {
        return currentElement;
    }

    @Override
    public void updatePlayerState() {
    	if (currentElement != null) {
            Log.v(TAG, "before updatePlayerState: currentElement = " + currentElement);
            ImageButton playPauseButton = (ImageButton) currentElement.findViewById(R.id.ppUrlListItemPlayPauseButton);
            ProgressBar progressBar = (ProgressBar) currentElement.findViewById(R.id.ppUrlListItemLoading);

            if (songManager.isLoading()) {
                progressBar.setVisibility(View.VISIBLE);
                playPauseButton.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                if (songManager.isPrepared()) {
                    playPauseButton.setVisibility(View.VISIBLE);
                } else {
                    if (playPauseButton.getVisibility() == View.GONE)
                        progressBar.setVisibility(View.INVISIBLE);
                }
            }

            if (songManager.isPlaying()) {
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
            } else {
                playPauseButton.setImageResource(android.R.drawable.ic_media_play);
            }
    	}
    }
}
