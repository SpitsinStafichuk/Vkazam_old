package com.github.spitsinstafichuk.vkazam.fragments_old;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.spitsinstafichuk.vkazam.R;
import com.github.spitsinstafichuk.vkazam.model.SongData;
import com.github.spitsinstafichuk.vkazam.services.MicrophoneRecordingNowService;
import com.github.spitsinstafichuk.vkazam.services.MicrophoneRecordingTimerService;
import com.github.spitsinstafichuk.vkazam.services.OnStatusChangedListener;
import com.github.spitsinstafichuk.vkazam.services.StartBoundService;
import com.github.spitsinstafichuk.vkazam.utils.AndroidUtils;
import com.github.spitsinstafichuk.vkazam.view.ProgressWheel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class RecognizePageFragment extends FragmentWithName
        implements MicrophoneRecordingTimerService.OnTimerUpdateListener {

    private MicrophoneRecordingNowService mMicrophoneRecordingNowService;

    private boolean isMicrophoneRecordingNowServiceBound;

    private MicrophoneRecordingTimerService mMicrophoneRecordingTimerService;

    private boolean isMicrophoneRecordingTimerServiceBound;

    private Activity parentActivity;

    private LinearLayout song;

    private TextView songArtist;

    private TextView songTitle;

    private TextView songDate;

    private ImageView songCoverArt;

    private LinearLayout prevSong;

    private TextView prevSongArtist;

    private TextView prevSongTitle;

    private TextView prevSongDate;

    private ImageView prevSongCoverArt;

    private TextView status;

    private TextView progress;

    private ProgressBar statusProgress;

    private LinearLayout tutorialPage;

    private LinearLayout recognizePage;

    private TextView tut1RecTimer;

    private TextView tut1RecNow;

    private ProgressWheel tut2TimerDelay;

    private TextView tut2TimerDelayInfo;

    private TextView tut3ResInfo;

    private View tut3ResEx;

    private ImageView tut3ResLink;

    private SongData currentApearingSong;

    private boolean pageOnCreating;

    private boolean firstTimeApearing;

    private SharedPreferences prefs;

    private ProgressWheel fingerprintTimer;

    private OnStatusChangedListener mMicrophoneRecordingNowServiceListener
            = new OnStatusChangedListener() {

        @Override
        public void onStatusChanged(String status) {
            RecognizePageFragment.this.onStatusChanged(status);
        }

        @Override
        public void onResultStatus(SongData data) {
            RecognizePageFragment.this.onResultStatus(data);
            if (data != null) {
                //TODO go to SongListFragment
            }
            if (mMicrophoneRecordingTimerService.isWorking()) {
                mMicrophoneRecordingTimerService.startTimer();
            }
        }
    };

    private OnStatusChangedListener mMicrophoneRecordingTimerServiceListener
            = new OnStatusChangedListener() {

        @Override
        public void onStatusChanged(String status) {
            RecognizePageFragment.this.onStatusChanged(status);
        }

        @Override
        public void onResultStatus(SongData data) {
            RecognizePageFragment.this.onResultStatus(data);
        }
    };

    private void onStatusChanged(String status) {
        Log.v("Status_text", status);
        updateProgress(status);
    }

    private void onResultStatus(SongData data) {
        if (data != null) {
//            model.getSongList().add(0, songData);
//            model.getScrobbler().sendLastFMTrack(songData.getArtist(), songData.getTitle(), songData.getAlbum());
            displaySongInformationElement(data, true);
        } else {
            updateProgress("No music found");
        }
    }

    private ServiceConnection microphoneServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            StartBoundService.ServiceBinder binder = (StartBoundService.ServiceBinder) service;
            isMicrophoneRecordingNowServiceBound = true;
            mMicrophoneRecordingNowService = (MicrophoneRecordingNowService) binder.getService();
            mMicrophoneRecordingNowService
                    .addOnStatusChangedListener(mMicrophoneRecordingNowServiceListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isMicrophoneRecordingNowServiceBound = false;
        }
    };

    private ServiceConnection timerServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            StartBoundService.ServiceBinder binder = (StartBoundService.ServiceBinder) service;
            isMicrophoneRecordingTimerServiceBound = true;
            mMicrophoneRecordingTimerService = (MicrophoneRecordingTimerService) binder
                    .getService();
            mMicrophoneRecordingTimerService
                    .addOnStatusChangedListener(mMicrophoneRecordingTimerServiceListener);
            mMicrophoneRecordingTimerService.addOnTimerUpdateListener(RecognizePageFragment.this);

            if (fingerprintTimer != null) {
                fingerprintTimer.setProgress(mMicrophoneRecordingTimerService.getTimerProgress());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isMicrophoneRecordingTimerServiceBound = false;
        }
    };

    //TODO refactor
    public static RecognizePageFragment newInstance(Context context) {
        RecognizePageFragment pageFragment = new RecognizePageFragment();
        Bundle arguments = new Bundle();
        pageFragment.setArguments(arguments);
        pageFragment.setFragmentName(context
                .getString(R.string.tagging_page_fragment_caption));
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        pageOnCreating = true;

        prefs = PreferenceManager.getDefaultSharedPreferences(this
                .getActivity());
        firstTimeApearing = prefs.getBoolean("RecognizePageFragmentFirstTime",
                true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = new Intent(getActivity(), MicrophoneRecordingNowService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, microphoneServiceConnection, Context.BIND_AUTO_CREATE);

        intent = new Intent(getActivity(), MicrophoneRecordingTimerService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, timerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (isMicrophoneRecordingNowServiceBound) {
            mMicrophoneRecordingNowService
                    .removeOnStatusChangedListener(mMicrophoneRecordingNowServiceListener);
            getActivity().unbindService(microphoneServiceConnection);
            isMicrophoneRecordingNowServiceBound = false;
        }

        if (isMicrophoneRecordingTimerServiceBound) {
            mMicrophoneRecordingTimerService
                    .removeOnStatusChangedListener(mMicrophoneRecordingTimerServiceListener);
            mMicrophoneRecordingTimerService.removeOnTimerUpdateListener(this);
            getActivity().unbindService(timerServiceConnection);
            isMicrophoneRecordingTimerServiceBound = false;
        }
    }

    //TODO refactor
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recognize, null);

        fingerprintTimer = (ProgressWheel) view
                .findViewById(R.id.fingerprintTimer);
        if (isMicrophoneRecordingTimerServiceBound) {
            fingerprintTimer.setProgress(mMicrophoneRecordingTimerService.getTimerProgress());
        }

        song = (LinearLayout) view.findViewById(R.id.currentSong);
        song.setVisibility(View.GONE);
        songArtist = (TextView) song.findViewById(R.id.songListItemArtist);
        songTitle = (TextView) song.findViewById(R.id.songListItemTitle);
        songDate = (TextView) song.findViewById(R.id.songListItemDate);
        songCoverArt = (ImageView) song.findViewById(R.id.songListItemCoverArt);
        LinearLayout songPlayPauseButton = (LinearLayout) song
                .findViewById(R.id.songListItemPlayPauseLayout);
        songPlayPauseButton.setVisibility(View.GONE);

        prevSong = (LinearLayout) view.findViewById(R.id.prevSong);
        prevSong.setVisibility(View.INVISIBLE);
        prevSongArtist = (TextView) prevSong
                .findViewById(R.id.songListItemArtist);
        prevSongTitle = (TextView) prevSong
                .findViewById(R.id.songListItemTitle);
        prevSongDate = (TextView) prevSong.findViewById(R.id.songListItemDate);
        prevSongCoverArt = (ImageView) prevSong
                .findViewById(R.id.songListItemCoverArt);
        LinearLayout prevSongPlayPauseButton = (LinearLayout) prevSong
                .findViewById(R.id.songListItemPlayPauseLayout);
        prevSongPlayPauseButton.setVisibility(View.GONE);

        status = (TextView) view.findViewById(R.id.status);
        progress = (TextView) view.findViewById(R.id.progressPercent);
        statusProgress = (ProgressBar) view.findViewById(R.id.statusProgress);

        ImageButton microTimerListenButton = (ImageButton) view
                .findViewById(R.id.microTimerListenButton);
        microTimerListenButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isMicrophoneRecordingTimerServiceBound) {
                    if (!isMicrophoneRecordingNowServiceBound || !mMicrophoneRecordingNowService
                            .isWorking()) {
                        if (mMicrophoneRecordingTimerService.isWorking()) {
                            mMicrophoneRecordingTimerService.cancelRecording();
                        } else {
                            mMicrophoneRecordingTimerService.recordFingerprint();
                        }
                    }
                }
            }
        });
        microTimerListenButton.setClickable(true);

        Button microNowListenButton = (Button) view
                .findViewById(R.id.microNowListenButton);
        microNowListenButton.setText(getString(R.string.recognize_button));
        microNowListenButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isMicrophoneRecordingNowServiceBound) {

                    if (!isMicrophoneRecordingTimerServiceBound ||
                            !mMicrophoneRecordingTimerService.isWorking() ||
                            (!mMicrophoneRecordingTimerService.isRecording() &&
                                    !mMicrophoneRecordingTimerService.isRecognizing())) {
                        if (mMicrophoneRecordingNowService.isWorking()) {
                            mMicrophoneRecordingNowService.cancelRecording();
                        } else {
                            mMicrophoneRecordingNowService.recordFingerprint();
                        }
                        mMicrophoneRecordingTimerService.resetProgress();
                    }
                }
            }
        });
        microNowListenButton.setClickable(true);

        recognizePage = (LinearLayout) view.findViewById(R.id.recognizePage);
        tutorialPage = (LinearLayout) view.findViewById(R.id.tutorialPage);

        if (firstTimeApearing) {
            tutorialPage.setVisibility(View.VISIBLE);
            tutorialPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tut1RecNow.getVisibility() == View.VISIBLE) {
                        tut1RecNow.setVisibility(View.INVISIBLE);
                        tut1RecTimer.setVisibility(View.INVISIBLE);

                        tut2TimerDelay.setVisibility(View.VISIBLE);
                        tut2TimerDelayInfo.setVisibility(View.VISIBLE);
                    } else if (tut2TimerDelay.getVisibility() == View.VISIBLE) {
                        tut2TimerDelay.setVisibility(View.INVISIBLE);
                        tut2TimerDelayInfo.setVisibility(View.INVISIBLE);

                        tut3ResEx.setVisibility(View.VISIBLE);
                        tut3ResInfo.setVisibility(View.VISIBLE);
                        tut3ResLink.setVisibility(View.VISIBLE);
                    } else if (tut3ResInfo.getVisibility() == View.VISIBLE) {
                        tutorialPage.setVisibility(View.GONE);
                        AndroidUtils.setViewEnabled(recognizePage, true);

                        firstTimeApearing = false;
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("RecognizePageFragmentFirstTime",
                                false);
                        editor.commit();
                    }
                }
            });
            AndroidUtils.setViewEnabled(recognizePage, false);

            tut1RecNow = (TextView) tutorialPage
                    .findViewById(R.id.tutorial1RecNow);
            tut1RecNow.setVisibility(View.VISIBLE);
            tut1RecTimer = (TextView) tutorialPage
                    .findViewById(R.id.tutorial1RecTimer);
            tut1RecTimer.setVisibility(View.VISIBLE);
            tut2TimerDelay = (ProgressWheel) tutorialPage
                    .findViewById(R.id.tutorial2TimerDelay);
            tut2TimerDelay.setRimColor(0XFFFFFFFF);
            tut2TimerDelay.setVisibility(View.INVISIBLE);
            tut2TimerDelayInfo = (TextView) tutorialPage
                    .findViewById(R.id.tutorial2TimerDelayInfo);
            tut2TimerDelayInfo.setVisibility(View.INVISIBLE);
            tut3ResInfo = (TextView) tutorialPage
                    .findViewById(R.id.tutorial3ResultInfo);
            tut3ResInfo.setVisibility(View.INVISIBLE);
            tut3ResEx = tutorialPage.findViewById(R.id.tutorial3Result);
            View tut3ResExInfo = tut3ResEx
                    .findViewById(R.id.songHistoryItemInfo);
            if (tut3ResExInfo != null) {
                tut3ResExInfo
                        .setBackgroundResource(R.drawable.list_item_bg_default);
            }
            tut3ResEx.setVisibility(View.INVISIBLE);
            LinearLayout tutPlayButton = (LinearLayout) tut3ResEx
                    .findViewById(R.id.songListItemPlayPauseLayout);
            tutPlayButton.setVisibility(View.GONE);
            AndroidUtils.setViewEnabled(tut3ResEx, false);
            tut3ResLink = (ImageView) tutorialPage
                    .findViewById(R.id.tutorial3ResultLink);
            tut3ResLink.setVisibility(View.INVISIBLE);
        } else {
            tutorialPage.setVisibility(View.GONE);
            AndroidUtils.setViewEnabled(recognizePage, true);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        displaySongInformationElement(currentApearingSong, true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
    }

    //TODO refactor
    public void displaySongInformationElement(final SongData songData,
            boolean apearing) {
        if (songData != null) {
            updateItem(song, songArtist, songTitle, songDate, songCoverArt,
                    songData);
            if (apearing) {
                if (!pageOnCreating) {
                    Animation disappear = AnimationUtils.loadAnimation(
                            this.parentActivity, R.anim.disappear);
                    disappear.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            updateItem(prevSong, prevSongArtist, prevSongTitle,
                                    prevSongDate, prevSongCoverArt, songData);
                        }
                    });
                    prevSong.setAnimation(disappear);
                    prevSong.setVisibility(View.VISIBLE);
                } else {
                    updateItem(prevSong, prevSongArtist, prevSongTitle,
                            prevSongDate, prevSongCoverArt, songData);
                    pageOnCreating = false;
                }
                song.setAnimation(AnimationUtils.loadAnimation(
                        this.parentActivity, R.anim.appear));
            }
            song.setVisibility(View.VISIBLE);
            currentApearingSong = songData;
        }
    }

    //TODO refactor
    private void updateItem(LinearLayout song, TextView artist, TextView title,
            TextView date, ImageView coverArt, SongData songData) {
        song.setVisibility(View.INVISIBLE);
        String coverArtUrl = songData.getCoverArtUrl();
        artist.setText(songData.getArtist());
        title.setText(songData.getTitle());
        date.setText(songData.getDate().toString());
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.no_cover_art)
                .showImageOnFail(R.drawable.no_cover_art)
                .showStubImage(R.drawable.cover_art_loading).build();
//		model.getImageLoader().displayImage(coverArtUrl, coverArt, options);
    }

    //TODO refactor
    private void updateProgress(String status) {
        int listenStep = 6;
        int otherStep = 10;
        String patternListening = "Listening";
        String patternRecognizing = "Recognizing";
        int progress;

        if (status.contains(patternListening)) {
            progress = listenStep
                    * Integer
                    .parseInt(status.substring(
                            patternListening.length() + 1,
                            status.length() - 2)) / 10;
        } else {
            progress = statusProgress.getProgress() + otherStep;
        }
        statusProgress.setProgress(progress);

        if (status.contains(patternListening)) {
            this.status.setText(getString(R.string.recognize_status_listening));
        } else if (status.contains(patternRecognizing)) {
            this.status
                    .setText(getString(R.string.recognize_status_recognizing));
        } else {
            this.status.setText(status);
        }

        this.progress.setText(progress + " %");
    }

    @Override
    public void onUpdate(int progress) {
        fingerprintTimer.setProgress(progress);
    }
}
