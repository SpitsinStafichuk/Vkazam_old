package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.RecognizeController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.FingerprintManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintStatusObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintTimerObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeResultObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;
import com.git.programmerr47.testhflbjcrhjggkth.view.ProgressWheel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RecognizePageFragment
       extends
             FragmentWithName
       implements
             IRecognizeStatusObserver,
             IRecognizeResultObserver,
             IFingerprintStatusObserver,
             IFingerprintTimerObserver {

	static final String ARGUMENT_RADIO_ID = "arg_rad_id";
    
    private RecognizeController controller;
    private MicroScrobblerModel model;
    private RecognizeManager recognizeManager;
    private FingerprintManager fingerprintManager;
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
    private ProgressBar statusProgress;
    
    private SongData currentApearingSong;
    private boolean firstTimeApearing;

    private ProgressWheel fingerprintTimer;
    
    public static RecognizePageFragment newInstance(Context context) {
        RecognizePageFragment pageFragment = new RecognizePageFragment();
        Bundle arguments = new Bundle();
        pageFragment.setArguments(arguments);
        pageFragment.setFragmentName("Tagging");
        pageFragment.setContext(context);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new RecognizeController(this.getActivity().getApplicationContext());
        model = RecognizeServiceConnection.getModel();
        fingerprintManager = model.getFingerprintManager();
        fingerprintManager.addFingerprintStatusObserver(this);
        fingerprintManager.addFingerprintTimerObserver(this);
        recognizeManager = model.getRecognizeManager();
        recognizeManager.addRecognizeStatusObserver(this);
        recognizeManager.addRecognizeResultObserver(this);
        firstTimeApearing = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recognize_fragment, null);
        
        song = (LinearLayout) view.findViewById(R.id.currentSong);
		song.setVisibility(View.GONE);
		songArtist = (TextView) song.findViewById(R.id.songListItemArtist);
		songTitle = (TextView) song.findViewById(R.id.songListItemTitle);
		songDate = (TextView) song.findViewById(R.id.songListItemDate);
		songCoverArt = (ImageView) song.findViewById(R.id.songListItemCoverArt);
		
		prevSong = (LinearLayout) view.findViewById(R.id.prevSong);
		prevSong.setVisibility(View.GONE);
		prevSongArtist = (TextView) prevSong.findViewById(R.id.songListItemArtist);
		prevSongTitle = (TextView) prevSong.findViewById(R.id.songListItemTitle);
		prevSongDate = (TextView) prevSong.findViewById(R.id.songListItemDate);
		prevSongCoverArt = (ImageView) prevSong.findViewById(R.id.songListItemCoverArt);
		
		status = (TextView) view.findViewById(R.id.status);
		statusProgress = (ProgressBar) view.findViewById(R.id.statusProgress);
        
        ImageButton microTimerListenButton = (ImageButton) view.findViewById(R.id.microTimerListenButton);
        microTimerListenButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v("Recognizing", "Recognize by timer: onLongClick");
                controller.fingerprintByTimerRecognizeCancel();
            }
        });
        
        ImageButton microNowListenButton = (ImageButton) view.findViewById(R.id.microNowListenButton);
        microNowListenButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v("Recognizing", "Recognize now: onLongClick");
                controller.fingerprintNowRecognizeCancel();
            }
        });

        fingerprintTimer = (ProgressWheel) view.findViewById(R.id.fingerprintTimer);
        
        return view;
    }
   
    @Override
    public void onDestroy() {
        super.onDestroy();
        fingerprintManager.removeFingerprintStatusObserver(this);
        fingerprintManager.removeFingerprintTimerObserver(this);
        recognizeManager.removeRecognizeStatusObserver(this);
        recognizeManager.removeRecognizeResultObserver(this);
        controller.finish();
        Log.v("SongPlayer", "HistoryPageFragment onDestroy()");
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	displaySongInformationElement(currentApearingSong, false);
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
    }

	@Override
	public void onFingerprintStatusChanged(String status) {
		Log.v("Status_text", status);
		updateProgress(status);
	}

	@Override
	public void onRecognizeStatusChanged(String status) {
		Log.v("Status_text", status);
		updateProgress(status);
	}

	@Override
	public void onRecognizeResult(SongData songData) {
    	displaySongInformationElement(songData, true);
	}
	
	public void displaySongInformationElement(final SongData songData, boolean apearing) {
		Log.v("RecognizeFragment", "Displaying info element and apearing = " + apearing);
		Log.v("RecognizeFragment", "songData = " + songData);
    	if(songData != null) {
			updateItem(song, songArtist, songTitle, songDate, songCoverArt, songData);
			if (apearing) {
				if (!firstTimeApearing) {
					Animation disappear = AnimationUtils.loadAnimation(this.parentActivity, R.anim.disappear);
					disappear.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							updateItem(prevSong, prevSongArtist, prevSongTitle, prevSongDate, prevSongCoverArt, songData);
						}
					});
					prevSong.setAnimation(disappear);
					prevSong.setVisibility(View.VISIBLE);
				} else {
					updateItem(prevSong, prevSongArtist, prevSongTitle, prevSongDate, prevSongCoverArt, songData);
					firstTimeApearing = false;
				}
				song.setAnimation(AnimationUtils.loadAnimation(this.parentActivity, R.anim.appear));
			}
			song.setVisibility(View.VISIBLE);
			Log.v("RecognizeFragment", "song info visibility is " + song.getVisibility());
			currentApearingSong = songData;
		}
    }
    
    private void updateItem(LinearLayout song, TextView artist, TextView title, TextView date, ImageView coverArt, SongData songData) {
    	song.setVisibility(View.INVISIBLE);
		String coverArtUrl = songData.getCoverArtUrl();
		artist.setText(songData.getArtist());
		title.setText(songData.getTitle());
		date.setText(songData.getDate().toString());
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.no_cover_art)
			.showImageOnFail(R.drawable.no_cover_art)
			.build();
		model.getImageLoader().displayImage(coverArtUrl, coverArt, options);
    }
	
	private void updateProgress(String status) {
		int listenStep = 6;
		int otherStep = 10;
		String patternListening = "Listening";
		
		if (status.contains(patternListening)) {
			int progress = listenStep * Integer.parseInt(status.substring(patternListening.length() + 1, status.length() - 2)) / 10;
			statusProgress.setProgress(progress);
		} else {
			statusProgress.setProgress(statusProgress.getProgress() + otherStep);
		}

		this.status.setText(status);
	}

    @Override
    public void onFingerprintTimerUpdated() {
        fingerprintTimer.incrementProgress();
    }
}
