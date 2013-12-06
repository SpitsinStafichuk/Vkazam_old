package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerMediaPlayer;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObserver;

public class URLcontroller implements IPlayerStateObserver{

    View currentElement;
    MicroScrobblerMediaPlayer player;

    public URLcontroller() {
        player = RecognizeServiceConnection.getModel().getPlayer();
    }

    public void setCurrentElement(View v) {
        currentElement = v;
    }

    @Override
    public void updatePlayerState() {
        ImageButton playPauseButton = (ImageButton) currentElement.findViewById(R.id.songPlayPauseButton);
        ProgressBar progressBar = (ProgressBar) currentElement.findViewById(R.id.songItemLoading);

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
