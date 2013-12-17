package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

public class Scrobbler {
	private static final String TAG ="Scrobbler";
	
	private Context context;
	
	public Scrobbler(Context context) {
		this.context = context;
	}
	
	public void sendLastFMPlaybackCompleted(String artist, String title, String album, int duration) {
		if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("settingsLastFmConnection", false)) {
			Log.i(TAG, "sendLastFMPlaybackCompleted " + artist + " - " + title + " " + duration);
			Intent bCast = new Intent("net.jjc1138.android.scrobbler.action.MUSIC_STATUS");
			bCast.putExtra("playing", false);
			bCast.putExtra("artist", artist);
			bCast.putExtra("album", album);
			bCast.putExtra("track", title);
			bCast.putExtra("secs", duration / 1000);
			context.sendBroadcast(bCast);
		}
	}

	public void sendLastFMTrackPaused(String artist, String title, String album, int duration) {
		if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("settingsLastFmConnection", false)) {
			Log.i(TAG, "sendLastFMTrackPaused " + artist + " - " + title + " " + duration);
			Intent bCast = new Intent("net.jjc1138.android.scrobbler.action.MUSIC_STATUS");
			bCast.putExtra("playing", false);
			bCast.putExtra("artist", artist);
			bCast.putExtra("album", album);
			bCast.putExtra("track", title);
			bCast.putExtra("secs", duration / 1000);
			context.sendBroadcast(bCast);
		}
	}

	public void sendLastFMTrackStarted(String artist, String title, String album, int duration) {
		if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("settingsLastFmConnection", false)) {
			Log.i(TAG, "sendLastFMTrackStarted " + artist + " - " + title + " " + duration);
			Intent bCast = new Intent("net.jjc1138.android.scrobbler.action.MUSIC_STATUS");
			bCast.putExtra("playing", true);
			bCast.putExtra("artist", artist);
			bCast.putExtra("album", album);
			bCast.putExtra("track", title);
			bCast.putExtra("secs", duration / 1000);
			context.sendBroadcast(bCast);
		}
	}

	public void sendLastFMTrackUnpaused(String artist, String title, String album, int duration, int currentPosition) {
		if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("settingsLastFmConnection", false)) {
			Log.i(TAG, "sendLastFMTrackUnpaused " + artist + " - " + title + " " + duration);
			Intent bCast = new Intent("net.jjc1138.android.scrobbler.action.MUSIC_STATUS");
			bCast.putExtra("playing", true);
			bCast.putExtra("artist", artist);
			bCast.putExtra("album", album);
			bCast.putExtra("track", title);
			bCast.putExtra("secs", duration / 1000);
			context.sendBroadcast(bCast);
		}
	}
	
	public void sendLastFMTrack(String artist, String title, String album) {
		if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("settingsLastFmConnection", false)) {
			Log.i(TAG, "sendLastFMTrackUnpaused " + artist + " - " + title);
			Intent bCast = new Intent("net.jjc1138.android.scrobbler.action.MUSIC_STATUS");
			bCast.putExtra("playing", true);
			bCast.putExtra("artist", artist);
			bCast.putExtra("album", album);
			bCast.putExtra("track", title);
			bCast.putExtra("source", "U");
			context.sendBroadcast(bCast);
		}
	}
}
