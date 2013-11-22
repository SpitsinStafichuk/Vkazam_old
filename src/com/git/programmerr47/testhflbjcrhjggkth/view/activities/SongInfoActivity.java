package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObserver;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.HistoryPageFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class SongInfoActivity extends Activity implements IPlayerStateObserver {
	public static final String TAG = "SongInfoActivity";

	private MicroScrobblerModel model;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_information_layout);
		Log.i(TAG, "Creating song info activity");
		Intent intent = getIntent();
		int position = 0;
		if ((intent != null) && (intent.getExtras() != null)) {
			position = intent.getIntExtra(HistoryPageFragment.ARGUMENT_SONG_POSITION, 0);
		}
		
		model = MicroScrobblerModel.getInstance();
		SongData data = (SongData) model.getHistoryItem(position);
		
		if (data != null) {
			fillTextInformation(R.id.songInfoArtist, data.getArtist());
			fillTextInformation(R.id.songInfoTitle, data.getTitle());
			fillTextInformation(R.id.songInfoDate, data.getDate());
			fillTextInformation(R.id.songInfoTrackId, data.getTrackId());
			
			if (data.getCoverArtURL() != null) {
				DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.no_cover_art)
					.showImageOnFail(R.drawable.no_cover_art)
					.cacheOnDisc(true)
					.cacheInMemory(true)
					.build();
				model.getImageLoader().displayImage(data.getCoverArtURL(), (ImageView) findViewById(R.id.songInfoCoverArt), options);
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
		// TODO Auto-generated method stub
		
	}
}
