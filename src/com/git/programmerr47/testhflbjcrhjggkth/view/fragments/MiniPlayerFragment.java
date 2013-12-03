package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SongController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SongManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObserver;

public class MiniPlayerFragment extends Fragment implements IPlayerStateObserver{

    TextView songInfo;
    ImageButton playButton;
    ImageButton nextButton;
    ImageButton prevButton;
    ProgressBar progressBar;
    int currentPosition = 0;

    MicroScrobblerModel model;
    SongController controller;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        model = RecognizeServiceConnection.getModel();
        controller = new SongController(this.getActivity());

        model.getSongManager().addObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePlayerState();
    }

    @Override
    public void onStop() {
        super.onStop();
        model.getSongManager().removeObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mini_player_fragment, null);

        songInfo = (TextView) view.findViewById(R.id.miniplayerSongInfo);
        songInfo.setSelected(true);

        playButton = (ImageButton) view.findViewById(R.id.miniplayerPlayButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.playPauseSong(((DatabaseSongData)model.getSongList().get(currentPosition)), currentPosition);
            }
        });

        progressBar = (ProgressBar) view.findViewById(R.id.miniplayerSongLoading);

        return view;
    }

    @Override
    public void updatePlayerState() {
        SongManager songManager = model.getSongManager();
        if (songManager.isLoading()) {
            progressBar.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            if (songManager.isPrepared()) {
                playButton.setVisibility(View.VISIBLE);
            } else {
                if (playButton.getVisibility() == View.GONE)
                    progressBar.setVisibility(View.INVISIBLE);
            }
        }

        if (songManager.isPlaying()) {
            playButton.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            playButton.setImageResource(android.R.drawable.ic_media_play);
        }
    }
}
