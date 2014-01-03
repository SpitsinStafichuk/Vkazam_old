package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SongInfoController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.utils.AndroidUtils;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.SongReplacePagerAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class VkLyricsActivity extends Activity{
	public static final String LYRICS_KEY = "vkLyrics";
	
	MicroScrobblerModel model;
	SongData data;
	SongInfoController controller;

	TextView lyrics;
	String lyricsText;
	ImageButton settingsButton;
	ImageButton shareButton;
	ImageButton badButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vk_lyrics_layout);
		
		Intent intent = getIntent();
		lyricsText = intent.getExtras().getString(LYRICS_KEY);
		
		model = RecognizeServiceConnection.getModel();
		data = model.getCurrentOpenSong();
		controller = new SongInfoController(this);
		
		lyrics = (TextView) findViewById(R.id.vkLyrics);
		lyrics.setText(lyricsText);

        shareButton = (ImageButton) findViewById(R.id.shareButton);
		shareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "VKAZAM - new song (lyrics)");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I just wrote lyrics from song " + data.getArtist() +  " - " + data.getTitle());
				startActivity(Intent.createChooser(sharingIntent, "Share song"));
			}
		});
		
		badButton = (ImageButton) findViewById(R.id.badButton);
		badButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
        		AlertDialog.Builder appDialogBuilder = new AlertDialog.Builder(VkLyricsActivity.this);
        		appDialogBuilder.setIcon(R.drawable.ic_alert_dialog);
        		appDialogBuilder.setMessage("Don't like this lyrics. You can choose another url or get lyrics from musicXmatch");
        		appDialogBuilder.setTitle("Bad lyrics");
        		appDialogBuilder.setPositiveButton("Choose url", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface dialogInterface, int i) {
        				Intent intent = new Intent(VkLyricsActivity.this, RefreshPagerActivity.class);
        				intent.putExtra(RefreshPagerActivity.VK_KEY, SongReplacePagerAdapter.VK_PAGE_NUMBER);
        				startActivity(intent);
        				finish();
        				dialogInterface.dismiss();
        			}
        		});
        		appDialogBuilder.setNegativeButton("Get MM lyrics", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface dialogInterface, int i) {
        				controller.getMMLyrics(data);
        				dialogInterface.dismiss();
        			}
        		});

        		appDialogBuilder.show();
			}
		});

        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        if (AndroidUtils.isThereASettingsButton(this)) {
            settingsButton.setVisibility(View.GONE);
        }
        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openOptionsMenu();
            }
        });

        TextView artist = (TextView) findViewById(R.id.artistTitle);
        artist.setText(data.getArtist());

        TextView title = (TextView) findViewById(R.id.titleTitle);
        title.setText(data.getTitle());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.common_options_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        	case R.id.settings :
        		Log.v("Settings", "Creating settings activity");
            	Intent intent = new Intent(VkLyricsActivity.this, SettingsActivity.class);
            	startActivity(intent);
            	return true;
        	default :
        		return super.onOptionsItemSelected(item);
        }
	}
}
