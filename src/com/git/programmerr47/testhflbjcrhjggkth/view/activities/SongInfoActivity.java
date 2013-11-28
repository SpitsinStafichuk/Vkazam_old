package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SongInfoController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObserver;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.HistoryPageFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SongInfoActivity extends Activity implements IPlayerStateObserver {
	public static final String TAG = "SongInfoActivity";
	public static final String ARGUMENT_SONGLIST_POSITION = "SongDataPosition";

	private MicroScrobblerModel model;
	private SongInfoController controller;
	private DatabaseSongData data;
	private TextView artistTextView;

	ImageButton playPauseButton;
	ImageButton changeSong;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_information_layout);
		Log.i(TAG, "Creating song info activity");
		Intent intent = getIntent();
		int position = 0;
		if ((intent != null) && (intent.getExtras() != null)) {
			position = intent.getIntExtra(HistoryPageFragment.ARGUMENT_SONGLIST_POSITION, 0);
		}
		
		controller = new SongInfoController(this);
		model = RecognizeServiceConnection.getModel();
		model.getSongManager().addObserver(this);
		data = (DatabaseSongData) model.getHistoryItem(position);
		fillActivity(data);
		
		playPauseButton = (ImageButton) findViewById(R.id.songInfoPlayPauseButton);
		playPauseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				controller.playPauseSong(data);
			}
		});
		
		artistTextView = (TextView) findViewById(R.id.songInfoArtist);
		final int fposition = position;
		artistTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SongInfoActivity.this, ArtistInfoActivity.class);
				intent.putExtra(ARGUMENT_SONGLIST_POSITION, fposition);
				startActivity(intent);
			}
		});
		
		changeSong = (ImageButton) findViewById(R.id.refresh);
		changeSong.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SongInfoActivity.this, RefreshPagerActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void fillActivity(DatabaseSongData data) {
		if (data != null) {
			fillTextInformation(R.id.songInfoArtist, data.getArtist());
			fillTextInformation(R.id.songInfoTitle, data.getTitle());
			fillTextInformation(R.id.songInfoDate, data.getDate().toString());
			fillTextInformation(R.id.songInfoTrackId, data.getTrackId());
			
			if (data.getCoverArtUrl() != null) {
				DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.no_cover_art)
					.showImageOnFail(R.drawable.no_cover_art)
					.cacheOnDisc(true)
					.cacheInMemory(true)
					.build();
				model.getImageLoader().displayImage(data.getCoverArtUrl(), (ImageView) findViewById(R.id.songInfoCoverArt), options);
			}
		}
	}
	
	private void fillTextInformation(int resId, String text) {
		TextView textView = (TextView) findViewById(resId);
		if (textView != null) {
			textView.setText(text);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "Resuming song info activity");
		
		updatePlayerState();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "Pausing song info activity");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Closing song info activity");
	}

	@Override
	public void updatePlayerState() {
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.songInfoLoading);

		Log.v(TAG, "Current Data: " + data.getDate());
		Log.v(TAG, "SongManager Data: " + data.getDate());
		if (data.equals(model.getSongManager().getSongData())) {
			Log.v(TAG, "Data are equals");
			if (model.getSongManager().isLoading()) {
				progressBar.setVisibility(View.VISIBLE);
				playPauseButton.setVisibility(View.GONE);
				Log.v("SongPlayer", "Song" + model.getSongManager().getArtist() + " - " + model.getSongManager().getTitle() + "is loading");
			} else {
				progressBar.setVisibility(View.GONE);
				if (model.getSongManager().isPrepared()) {
					playPauseButton.setVisibility(View.VISIBLE);
				} else {
					if (playPauseButton.getVisibility() == View.GONE)
						progressBar.setVisibility(View.INVISIBLE);
				}
				Log.v("SongPlayer", "Song is not loading");
			}
			
			if (model.getSongManager().isPlaying()) {
				playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
				Log.v("SongPlayer", "Song" + model.getSongManager().getArtist() + " - " + model.getSongManager().getTitle() + "is playing");
			} else {
				playPauseButton.setImageResource(android.R.drawable.ic_media_play);
				Log.v("SongPlayer", "Song is on pause");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.common_options_menu, menu);
		return true;
	}
}
