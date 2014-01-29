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
import de.umass.util.StringUtilities;

public class MiniPlayerFragment extends Fragment implements IPlayerStateObserver, ISongInfoObserver, ISongProgressObserver{

    private TextView songInfoArtist;
    private TextView songInfoTitle;
    private TextView songProgressText;
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
                if (model.getSongManager().getPositionInList() != -1) {
                    Log.v("MiniPlayer", "Song downloading is updated " + percent);
                    songProgress.setSecondaryProgress(percent);
                }
            }
        });
        model.getSongManager().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (model.getSongManager().getPositionInList() != -1) {
                    Log.v("MiniPlayer", "Song is completed in miniplayer");
                    controller.playPauseSong(currentPosition + 1);
                }
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

        songInfoArtist = (TextView) view.findViewById(R.id.miniplayerSongInfoArtist);
        songInfoArtist.setText("");
        songInfoArtist.setSelected(true);

        songInfoTitle = (TextView) view.findViewById(R.id.miniplayerSongInfoTitle);
        songInfoTitle.setText("");
        songInfoTitle.setSelected(true);

        playButton = (ImageButton) view.findViewById(R.id.miniplayerPlayButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getSongManager().getPositionInList() != -1) {
                    controller.playPauseSong(currentPosition);
                }
            }
        });

        nextButton = (ImageButton) view.findViewById(R.id.miniplayerNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getSongManager().getPositionInList() != -1) {
                    controller.playPauseSong(currentPosition + 1);
                }
            }
        });

        prevButton = (ImageButton) view.findViewById(R.id.miniplayerPrevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getSongManager().getPositionInList() != -1) {
                    controller.playPauseSong(currentPosition - 1);
                }
            }
        });

        progressBar = (ProgressBar) view.findViewById(R.id.miniplayerSongLoading);
        songProgress = (SeekBar) view.findViewById(R.id.miniplayerSongProgress);
        songProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ((fromUser) && model.getSongManager().getPositionInList() != -1) {
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

        songProgressText = (TextView) view.findViewById(R.id.miniPlayerSongProgressText);

        return view;
    }

    @Override
    public void updatePlayerState() {
        SongManager songManager = model.getSongManager();
        if (songManager.getPositionInList() != -1) {
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
                playButton.setImageResource(R.drawable.ic_media_pause);
            } else {
                playButton.setImageResource(R.drawable.ic_media_play);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateSongInfo() {
        if (model.getSongManager().getPositionInList() != -1) {
            SongData data = model.getSongManager().getSongData();
            if (data != null) {
                int type = model.getSongManager().getType();
                Log.v("SongPlayerInfo", "type is " + type);
                if (type == SongManager.ANY_SONG) {
                    songInfoArtist.setText(data.getArtist());
                    songInfoArtist.setText(data.getTitle());
                } else if (type == SongManager.PP_SONG) {
                    songInfoArtist.setText(data.getPpArtist());
                    songInfoTitle.setText(data.getPpTitle());
                } else if (type == SongManager.VK_SONG) {
                    songInfoArtist.setText(data.getVkArtist());
                    songInfoTitle.setText(data.getVkTitle());
                } else if (type == SongManager.NO_SONG) {
                    songInfoArtist.setText("");
                    songInfoTitle.setText("");
                }
            } else {
                songInfoArtist.setText("");
                songInfoTitle.setText("");
            }
        }
    }

    @Override
    public void updateProgress(int progress, int duration) {
        if (model.getSongManager().getPositionInList() != -1) {
            if (duration == -1) {
                songProgress.setProgress(0);
                songProgress.setSecondaryProgress(0);
                songProgressText.setText("0:00");
            } else {
                songProgress.setProgress(progress * 100 / duration);

                progress /= 1000;
                duration /= 1000;

                int minutes = progress / 60;
                int seconds = progress % 60;

                int durationDim = (int)(Math.log10(duration / 60) + 1);
                int progressDim = (int)(Math.log10(progress / 60) + 1);
                String zerom = "";
                for (int i = 0; i < durationDim - progressDim; i++) {
                    zerom += "0";
                }
                String zeros = "";
                if (seconds < 10) {
                    zeros = "0";
                }

                songProgressText.setText(zerom + minutes + ":" + zeros + seconds);
            }
        }
    }
}
