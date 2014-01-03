package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.perm.kate.api.KException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class VkLyricsActivity extends Activity{

	TextView lyrics;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vk_lyrics_layout);
		
		lyrics = (TextView) findViewById(R.id.vkLyrics);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String lyrics2 = "recs";
				
				try {
					lyrics2 = RecognizeServiceConnection.getModel().getVkApi().getLyrics(
							RecognizeServiceConnection.getModel().getCurrentOpenSong().getLirycsId());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (KException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final String lyrics3 = lyrics2;
				Log.v("Lyrics", "++++++++++ " + lyrics3);
				
				VkLyricsActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						lyrics.setText(lyrics3);
					}
				});
			}
		}).start();
	}
}
