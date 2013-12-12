package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Scrobbler {
	private static final String TAG ="Scrobbler";
	
	private Context context;
	
	public Scrobbler(Context context) {
		this.context = context;
	}
	
	public void sendLastFMPlaybackCompleted(String artist, String title, String album) {
		Log.i(TAG, "sendLastFMPlaybackCompleted");
		Intent localIntent = new Intent("fm.last.android.playbackcomplete");
		localIntent.putExtra("artist", artist);
		localIntent.putExtra("track", title);
		localIntent.putExtra("album", album);
		context.sendBroadcast(localIntent);
	}

	public void sendLastFMTrackPaused() {
		Log.i(TAG, "sendLastFMTrackPaused");
		context.sendBroadcast(new Intent("fm.last.android.playbackpaused"));
	}

	public void sendLastFMTrackStarted(String artist, String title, String album, int duration) {
		Log.i(TAG, "sendLastFMTrackStarted " + artist + " - " + title/* + " " + duration*/);
		Intent localIntent = new Intent("fm.last.android.playstatechanged");
		localIntent.putExtra("artist", artist);
		localIntent.putExtra("track", title);
		localIntent.putExtra("album", album);
		localIntent.putExtra("duration", duration);
		context.sendBroadcast(localIntent);
	}

	public void sendLastFMTrackUnpaused(int currentPosition) {
		Log.i(TAG, "sendLastFMTrackUnpaused");
		context.sendBroadcast(new Intent("fm.last.android.metachanged").putExtra("position", currentPosition));
	}
	
}
