package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SongInfoController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.utils.AndroidUtils;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.SongReplacePagerAdapter;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.MessageDialogFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.TimerDelayDialogFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.MessageDialogFragment.onDialogClickListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class VkLyricsActivity extends FragmentActivity{
	public static final String LYRICS_KEY = "vkLyrics";
    private static final String SHOW_DIALOG_TAG = "dialog";
	
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

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag(SHOW_DIALOG_TAG);
                if (prev != null) {
                    fragmentTransaction.remove(prev);
                }
				
                MessageDialogFragment.Builder appDialogBuilder = new MessageDialogFragment.Builder();
        		appDialogBuilder.setIcon(R.drawable.ic_alert_dialog);
        		appDialogBuilder.setMessage("Don't like this lyrics. You can choose another url or get lyrics from musicXmatch");
        		appDialogBuilder.setTitle("Bad lyrics");
        		appDialogBuilder.setPositiveButton("Choose url", new onDialogClickListener() {
					
					@Override
					public void onDialogClick(DialogFragment fragment, View v) {
        				Intent intent = new Intent(VkLyricsActivity.this, RefreshPagerActivity.class);
        				intent.putExtra(RefreshPagerActivity.VK_KEY, SongReplacePagerAdapter.VK_PAGE_NUMBER);
        				startActivity(intent);
        				finish();
        				fragment.dismiss();
					}
				});
        		appDialogBuilder.setNegativeButton("Get MM lyrics", new onDialogClickListener() {
					
					@Override
					public void onDialogClick(DialogFragment fragment, View v) {
        				controller.getMMLyrics(data);
        				fragment.dismiss();
					}
				});

                DialogFragment dialogFragment = MessageDialogFragment.newInstance(appDialogBuilder);
                dialogFragment.show(fragmentTransaction, SHOW_DIALOG_TAG);
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