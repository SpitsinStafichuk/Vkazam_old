package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Scrobbler {
	private static final String TAG ="Scrobbler";
	private static final int START = 0;
	private static final int RESUME = 1;
	private static final int PAUSE = 2;
	private static final int COMPLETE = 3;
	
	private Context context;
	
	public Scrobbler(Context context) {
		this.context = context;
	}
	
/*	public void sendLastFMPlaybackCompleted(String artist, String title, String album, int duration) {
		Log.i(TAG, "sendLastFMPlaybackCompleted");
		Intent localIntent = new Intent("fm.last.android.playbackcomplete");
		localIntent.putExtra("artist", artist);
		localIntent.putExtra("track", title);
		localIntent.putExtra("album", album);
		localIntent.putExtra("duration", duration);
		context.sendBroadcast(localIntent);
	}

	public void sendLastFMTrackPaused(String artist, String title, String album, int duration) {
		Log.i(TAG, "sendLastFMTrackPaused");
		Intent localIntent = new Intent("fm.last.android.playbackpaused");
		localIntent.putExtra("artist", artist);
		localIntent.putExtra("track", title);
		localIntent.putExtra("album", album);
		localIntent.putExtra("duration", duration);
		context.sendBroadcast(localIntent);
	}

	public void sendLastFMTrackStarted(String artist, String title, String album, int duration) {
		Log.i(TAG, "sendLastFMTrackStarted " + artist + " - " + title + " " + duration);
		Intent localIntent = new Intent("fm.last.android.playstatechanged");
		localIntent.putExtra("artist", artist);
		localIntent.putExtra("track", title);
		localIntent.putExtra("album", album);
		localIntent.putExtra("duration", duration);
		context.sendBroadcast(localIntent);
	}

	public void sendLastFMTrackUnpaused(String artist, String title, String album, int duration, int currentPosition) {
		Log.i(TAG, "sendLastFMTrackUnpaused");
		Intent localIntent = new Intent("fm.last.android.metachanged");
		localIntent.putExtra("artist", artist);
		localIntent.putExtra("track", title);
		localIntent.putExtra("album", album);
		localIntent.putExtra("duration", duration);
		localIntent.putExtra("position", currentPosition);
		context.sendBroadcast(localIntent);
	}
	*/
	
	public void sendLastFMPlaybackCompleted(String artist, String title, String album, int duration) {
		Log.i(TAG, "sendLastFMPlaybackCompleted " + artist + " - " + title + " " + duration);
		Intent bCast = new Intent("com.adam.aslfms.notify.playstatechanged");
		bCast.putExtra("state", COMPLETE);
		bCast.putExtra("app-name", context.getResources().getString(R.string.app_name));
		bCast.putExtra("app-package", "com.git.programmerr47.testhflbjcrhjggkth");
		bCast.putExtra("artist", artist);
		bCast.putExtra("album", album);
		bCast.putExtra("track", title);
		bCast.putExtra("duration", duration / 1000);
		context.sendBroadcast(bCast);
	}

	public void sendLastFMTrackPaused(String artist, String title, String album, int duration) {
		Log.i(TAG, "sendLastFMTrackPaused " + artist + " - " + title + " " + duration);
		Intent bCast = new Intent("com.adam.aslfms.notify.playstatechanged");
		bCast.putExtra("state", PAUSE);
		bCast.putExtra("app-name", context.getResources().getString(R.string.app_name));
		bCast.putExtra("app-package", "com.git.programmerr47.testhflbjcrhjggkth");
		bCast.putExtra("artist", artist);
		bCast.putExtra("album", album);
		bCast.putExtra("track", title);
		bCast.putExtra("duration", duration / 1000);
		context.sendBroadcast(bCast);
	}

	public void sendLastFMTrackStarted(String artist, String title, String album, int duration) {
		Log.i(TAG, "sendLastFMTrackStarted " + artist + " - " + title + " " + duration);
		Intent bCast = new Intent("com.adam.aslfms.notify.playstatechanged");
		bCast.putExtra("state", START);
		bCast.putExtra("app-name", context.getResources().getString(R.string.app_name));
		bCast.putExtra("app-package", "com.git.programmerr47.testhflbjcrhjggkth");
		bCast.putExtra("artist", artist);
		bCast.putExtra("album", album);
		bCast.putExtra("track", title);
		bCast.putExtra("duration", duration / 1000);
		context.sendBroadcast(bCast);
	}

	public void sendLastFMTrackUnpaused(String artist, String title, String album, int duration, int currentPosition) {
		Log.i(TAG, "sendLastFMTrackUnpaused " + artist + " - " + title + " " + duration);
		Intent bCast = new Intent("com.adam.aslfms.notify.playstatechanged");
		bCast.putExtra("state", RESUME);
		bCast.putExtra("app-name", context.getResources().getString(R.string.app_name));
		bCast.putExtra("app-package", "com.git.programmerr47.testhflbjcrhjggkth");
		bCast.putExtra("artist", artist);
		bCast.putExtra("album", album);
		bCast.putExtra("track", title);
		bCast.putExtra("duration", duration / 1000);
		bCast.putExtra("position", currentPosition / 1000);
		context.sendBroadcast(bCast);
	}
}
