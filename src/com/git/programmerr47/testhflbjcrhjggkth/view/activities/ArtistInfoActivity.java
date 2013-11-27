package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ArtistInfoActivity extends Activity {
	public static final String TAG = "ArtistInfoActivity";

	private MicroScrobblerModel model;
	private DatabaseSongData data;

	ImageButton playPauseButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.artist_information_layout);
		Log.i(TAG, "Creating artist info activity");
		Intent intent = getIntent();
		int position = 0;
		if ((intent != null) && (intent.getExtras() != null)) {
			position = intent.getIntExtra(SongInfoActivity.ARGUMENT_SONGLIST_POSITION, 0);
		}
		
		model = RecognizeServiceConnection.getModel();
		data = (DatabaseSongData) model.getHistoryItem(position);
		fillActivity(data);
	}
	
	private void fillActivity(DatabaseSongData data) {
		if (data != null) {
			fillTextInformation(R.id.artistInfoArtist, data.getArtist());
			TextView biographyTextView = (TextView) findViewById(R.id.artistInfoBiography);
			String biographyUrl = data.getArtistBiographyURL();
			Log.i(TAG, "biographyUrl: " + biographyUrl);
			String contributorImageUrl = data.getContributorImageUrl();
			Log.i(TAG, "contributorImageUrl: " + contributorImageUrl);
			DownloadTextTask downloadTextTask = new DownloadTextTask(biographyUrl, biographyTextView);
			downloadTextTask.execute();
			
			if (data.getContributorImageUrl() != null) {
				DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.no_cover_art)
					.showImageOnFail(R.drawable.no_cover_art)
					.cacheOnDisc(true)
					.cacheInMemory(true)
					.build();
				model.getImageLoader().displayImage(contributorImageUrl, (ImageView) findViewById(R.id.songInfoCoverArt), options);
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
	
	private class DownloadTextTask extends AsyncTask<Void, Void, String> {
		
		private String url;
		private TextView textView;
		
		public DownloadTextTask(String url, TextView textView) {
			super();
			this.url = url;
			this.textView = textView;
		}

		@Override
		protected String doInBackground(Void... arg0) {
			if(url != null) {
				Reader input = null;
	            
	            URL urlConn = null;
				try {
					urlConn = new URL(url);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				StringBuilder stringBuilder = new StringBuilder();
	            try {
	            	input = new InputStreamReader(urlConn.openStream());
	            	char[] buffer = new char[1024];
	                for(;;) {
	                	int rsz;
	    				
	    					rsz = input.read(buffer, 0, buffer.length);
	    				
	                	if(rsz < 0)
	                		break;
	                	stringBuilder.append(buffer, 0, rsz);
	                }
	            } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						input.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return stringBuilder.toString();
			}
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			textView.setText(result);
		}
		
	}
}
