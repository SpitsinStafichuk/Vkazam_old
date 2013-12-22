package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.TestController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class TestPageFragment extends FragmentWithName {
	static final String ARGUMENT_RADIO_ID = "arg_rad_id";
    
	private TestController controller;
	private MicroScrobblerModel model;
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
    
	private SongData currentApearingSong;
	private boolean firstTimeApearing;

    public static TestPageFragment newInstance(Context context) {
    		TestPageFragment pageFragment = new TestPageFragment();
            Bundle arguments = new Bundle();
            pageFragment.setArguments(arguments);
            pageFragment.setFragmentName("search");
            pageFragment.setFragmentIcon(R.drawable.ic_action_search);
            pageFragment.setContext(context);
            return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            controller = new TestController(this);
            model = RecognizeServiceConnection.getModel();
            firstTimeApearing = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment, null);
        
		song = (LinearLayout) view.findViewById(R.id.currentSong);
		song.setVisibility(View.GONE);
		songArtist = (TextView) song.findViewById(R.id.songListItemArtist);
		songTitle = (TextView) song.findViewById(R.id.songListItemTitle);
		songDate = (TextView) song.findViewById(R.id.songListItemDate);
		songCoverArt = (ImageView) song.findViewById(R.id.songListItemCoverArt);
		LinearLayout songPlayPauseButton = (LinearLayout) song.findViewById(R.id.songListItemPlayPauseLayout);
		songPlayPauseButton.setVisibility(View.GONE);
		
		prevSong = (LinearLayout) view.findViewById(R.id.prevSong);
		prevSong.setVisibility(View.GONE);
		prevSongArtist = (TextView) prevSong.findViewById(R.id.songListItemArtist);
		prevSongTitle = (TextView) prevSong.findViewById(R.id.songListItemTitle);
		prevSongDate = (TextView) prevSong.findViewById(R.id.songListItemDate);
		prevSongCoverArt = (ImageView) prevSong.findViewById(R.id.songListItemCoverArt);
		LinearLayout prevSongPlayPauseButton = (LinearLayout) prevSong.findViewById(R.id.songListItemPlayPauseLayout);
		prevSongPlayPauseButton.setVisibility(View.GONE);
		
		status = (TextView) view.findViewById(R.id.status);
		
		final EditText artistEditText = (EditText) view.findViewById(R.id.artist);
		final EditText titleEditText = (EditText) view.findViewById(R.id.title);
		final EditText albumEditText = (EditText) view.findViewById(R.id.album);
		
        Button searchButton = (Button) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String artist = artistEditText.getText().toString();
				String title = titleEditText.getText().toString();
				String album = albumEditText.getText().toString();
				controller.search(artist, album, title);
			}
		});
        
        return view;
    }
    
    public void displayStatus(String statusString) {
    	status.setText(statusString);
    }
    
    public void displaySongInformationElement(final SongData songData, boolean apearing) {
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
			.showStubImage(R.drawable.cover_art_loading)
			.build();
		model.getImageLoader().displayImage(coverArtUrl, coverArt, options);
    }
    
    public void displaySongInformationElement(SongData songData) {
    	this.displaySongInformationElement(songData, true);
    }
   
    @Override
    public void onDestroy() {
        super.onDestroy();
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
}
