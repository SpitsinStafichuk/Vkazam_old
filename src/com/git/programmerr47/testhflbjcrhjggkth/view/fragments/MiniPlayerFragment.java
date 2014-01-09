package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SongController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SongManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongInfoObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongProgressObserver;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.SongListAdapter;

public class MiniPlayerFragment extends Fragment implements IPlayerStateObserver, ISongInfoObserver, ISongProgressObserver{

    private TextView songInfo;
    private ImageButton playButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private ProgressBar progressBar;
    private SeekBar songProgress;

    private int currentPosition = 0;

    private MicroScrobblerModel model;
    private SongController controller;
    private SongListAdapter adapter;

    public MiniPlayerFragment(SongListAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        model = RecognizeServiceConnection.getModel();
        controller = new SongController(this.getActivity());

        model.getPlayer().addPlayerStateObserver(this);
        model.getSongManager().addSongIngoObserver(this);
        model.getSongManager().addSongProgressObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        currentPosition = adapter.getCurrentListPosition();

        model.getSongManager().setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
                Log.v("MiniPlayer", "Song downloading is updated " + percent);
                songProgress.setSecondaryProgress(percent);
            }
        });
        model.getSongManager().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.v("MiniPlayer", "Song is completed in miniplayer");
                controller.playPauseSong(currentPosition + 1);
            }
        });

        updatePlayerState();
        updateSongInfo();
    }

    @Override
    public void onStop() {
        super.onStop();
        model.getPlayer().removePlayerStateObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mini_player_fragment, null);

        songInfo = (TextView) view.findViewById(R.id.miniplayerSongInfo);
        songInfo.setText("");
        songInfo.setSelected(true);

        playButton = (ImageButton) view.findViewById(R.id.miniplayerPlayButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.playPauseSong(currentPosition);
            }
        });

        nextButton = (ImageButton) view.findViewById(R.id.miniplayerNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.playPauseSong(currentPosition + 1);
            }
        });

        prevButton = (ImageButton) view.findViewById(R.id.miniplayerPrevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.playPauseSong(currentPosition - 1);
            }
        });

        progressBar = (ProgressBar) view.findViewById(R.id.miniplayerSongLoading);
        songProgress = (SeekBar) view.findViewById(R.id.miniplayerSongProgress);
        songProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    controller.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        return view;
    }

    @Override
    public void updatePlayerState() {
        SongManager songManager = model.getSongManager();
        currentPosition = songManager.getPositionInList();
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateSongInfo() {
        SongData data = model.getSongManager().getSongData();
        if (data != null) {
            int type = model.getSongManager().getType();
            Log.v("SongPlayerInfo", "type is " + type);
            if (type == SongManager.ANY_SONG) {
                songInfo.setText(data.getArtist() + " - " + data.getTitle());
            } else if (type == SongManager.PP_SONG) {
                songInfo.setText(data.getPpArtist() + " - " + data.getPpTitle());
            } else if (type == SongManager.VK_SONG) {
                songInfo.setText(data.getVkArtist() + " - " + data.getVkTitle());
            } else if (type == SongManager.NO_SONG) {
                songInfo.setText("");
            }
        } else {
            songInfo.setText("");
        }
    }

    @Override
    public void updateProgress(int progress, int duration) {
        if (duration == -1) {
            songProgress.setProgress(0);
            songProgress.setSecondaryProgress(0);
        } else {
            songProgress.setProgress(progress * 100 / duration);
        }
    }
}
