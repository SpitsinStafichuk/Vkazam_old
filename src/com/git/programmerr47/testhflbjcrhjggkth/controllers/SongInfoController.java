package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.SongNotFoundException;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.VkLyricsActivity;
import com.musixmatch.lyrics.musiXmatchLyricsConnector;
import com.perm.kate.api.Api;
import com.perm.kate.api.KException;

public class SongInfoController extends SongController{
	
	public SongInfoController(Activity view) {
		super(view);
	}

    public void getLyrics(final SongData data) {
        final Api api = model.getVkApi();
    	new Thread(new Runnable() {
            final musiXmatchLyricsConnector lyricsPlugin = new musiXmatchLyricsConnector(view);
            public void run() {
            	if (api != null) {
            		try {
            			if (data.getVkAudioId() == null) {
            				try {
								data.findVkAudio(api);
							} catch (SongNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
            			}
						Log.v("Lyrics", "" + api.getLyrics(data.getLirycsId()));
						view.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Intent intent = new Intent(view, VkLyricsActivity.class);
								view.startActivity(intent);
							}
						});
					} catch (NumberFormatException e) {
						Log.v("Lyrics", "NumberFormatException");
						Log.v("Lyrics", "" + data.getVkAudioId());
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MalformedURLException e) {
						Log.v("Lyrics", "MalformedURLException");
						e.printStackTrace();
					} catch (IOException e) {
						Log.v("Lyrics", "IOException");
						e.printStackTrace();
					} catch (JSONException e) {
						Log.v("Lyrics", "JSONException");
						e.printStackTrace();
					} catch (KException e) {
						Log.v("Lyrics", "KException");
						e.printStackTrace();
					}
            	}
//                Looper.prepare();
//                long millis = System.currentTimeMillis();
//                long s;
//                //lyricsPlugin.doBindService();
//                do {
//                    lyricsPlugin.doBindService();
//                    s = System.currentTimeMillis();
//                } while (!lyricsPlugin.getIsBound() && !((s - millis) > 1000));
//                if (lyricsPlugin.getIsBound()) {
//                    final String artist = data.getArtist();
//                    final String title = data.getTitle();
//                    view.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                lyricsPlugin.startLyricsActivity(artist,title);
//                            } catch (RemoteException e) {
//                                Toast.makeText(view, "Can not connect to MusiXmatch, try again later", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                } else { //You need to install Musixmatch\'s app and launch it at least one time to see the lyrics context
//                    AlertDialog.Builder appDialogBuilder = new AlertDialog.Builder(view);
//                    appDialogBuilder.setIcon(R.drawable.ic_launcher);
//                    appDialogBuilder.setMessage("You need to install Musixmatch\\'s app and launch it at least one time to see lyrics");
//                    appDialogBuilder.setTitle("No musiXmatch application");
//                    appDialogBuilder.setPositiveButton("Get MusiXmatch", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.musixmatch.android.lyrify"));
//                            view.startActivity(intent);
//                        }
//                    });
//                    appDialogBuilder.setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    });
//
//                    appDialogBuilder.show();
//                }
//                Looper.loop();
            }
        }).start();
    }
}
