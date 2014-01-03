package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

import android.R.bool;
import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Toast;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.SongNotFoundException;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.RefreshPagerActivity;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.SongInfoActivity;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.VkLyricsActivity;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.SongReplacePagerAdapter;
import com.musixmatch.lyrics.musiXmatchLyricsConnector;
import com.perm.kate.api.Api;
import com.perm.kate.api.KException;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.utils.YoutubeUtils;
import org.json.JSONException;

import java.io.IOException;

public class SongInfoController extends SongController{
	
	public SongInfoController(Activity view) {
		super(view);
	}
	
	private void getVkLyrics(final SongData data) {
        final ProgressDialog dialog = new ProgressDialog(view);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Waiting for answer");
        dialog.setMessage("Please wait for answer from vk");
        dialog.show();
        
		new Thread(new Runnable() {
			Api api = model.getVkApi();
			
	        public void run() {
                Looper.prepare();
	        	if (api != null) {
	        		try {
						String lyrics = data.getLyrics(api);
						if (lyrics != null) {
							Intent intent = new Intent(view, VkLyricsActivity.class);
							intent.putExtra(VkLyricsActivity.LYRICS_KEY, lyrics);
							view.startActivity(intent);
						} else {
							
			        		AlertDialog.Builder appDialogBuilder = new AlertDialog.Builder(view);
			        		appDialogBuilder.setIcon(R.drawable.ic_alert_dialog);
			        		appDialogBuilder.setMessage("Vk lyrics is not available for this song. You can choose another url or put lyrics from musicXmatch");
			        		appDialogBuilder.setTitle("No lyrics for song");
			        		appDialogBuilder.setPositiveButton("Choose url", new DialogInterface.OnClickListener() {
			        			@Override
			        			public void onClick(DialogInterface dialogInterface, int i) {
			        				Intent intent = new Intent(view, RefreshPagerActivity.class);
			        				intent.putExtra(RefreshPagerActivity.VK_KEY, SongReplacePagerAdapter.VK_PAGE_NUMBER);
			        				view.startActivity(intent);
			        				dialogInterface.dismiss();
			        			}
			        		});
			        		appDialogBuilder.setNegativeButton("Get MM lyrics", new DialogInterface.OnClickListener() {
			        			@Override
			        			public void onClick(DialogInterface dialogInterface, int i) {
			        				getMMLyrics(data);
			        				dialogInterface.dismiss();
			        			}
			        		});

			        		appDialogBuilder.show();
						}
					} catch (MalformedURLException e) {
						showToast("Seems you haven't internet connection");
						e.printStackTrace();
					} catch (IOException e) {
						showToast("Seems you haven't internet connection");
						e.printStackTrace();
					} catch (JSONException e) {
						showToast(e.getLocalizedMessage());
						e.printStackTrace();
					} catch (KException e) {
						showToast(e.getLocalizedMessage());
						e.printStackTrace();
					} catch (SongNotFoundException e) {
						showToast("Song is not found");
						e.printStackTrace();
					}
	        		
	        	} else {
	        		showToast("Vk is not available. Did you connected to vkontakte?");
	        	}
	        	dialog.dismiss();
                Looper.loop();
	        }
		}).start();
	}
	
	public void getMMLyrics(final SongData data) {
        
    	new Thread(new Runnable() {
            final musiXmatchLyricsConnector lyricsPlugin = new musiXmatchLyricsConnector(view);
            public void run() {
                Looper.prepare();
                long millis = System.currentTimeMillis();
                long s;
                do {
                    lyricsPlugin.doBindService();
                    s = System.currentTimeMillis();
                } while (!lyricsPlugin.getIsBound() && !((s - millis) > 1000));
                if (lyricsPlugin.getIsBound()) {
                    final String artist = data.getArtist();
                    final String title = data.getTitle();
                    view.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                lyricsPlugin.startLyricsActivity(artist,title);
                            } catch (RemoteException e) {
                                Toast.makeText(view, "Can not connect to MusiXmatch, try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else { 
                    AlertDialog.Builder appDialogBuilder = new AlertDialog.Builder(view);
	        		appDialogBuilder.setIcon(R.drawable.ic_alert_dialog);
                    appDialogBuilder.setMessage("You need to install Musixmatch\\'s app and launch it at least one time to see lyrics");
                    appDialogBuilder.setTitle("No musiXmatch application");
                    appDialogBuilder.setPositiveButton("Get MusiXmatch", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.musixmatch.android.lyrify"));
                            view.startActivity(intent);
                            dialogInterface.dismiss();
                        }
                    });
                    appDialogBuilder.setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    appDialogBuilder.show();
                }
                Looper.loop();
            }
        }).start();
	}

    public void getLyrics(final SongData data) {
    	boolean vkConnection = PreferenceManager.getDefaultSharedPreferences(view).getBoolean("settingsVkConnection", false);
    	boolean vkLyrics = PreferenceManager.getDefaultSharedPreferences(view).getBoolean("settingsVkLyrics", false);
    	
    	if (vkConnection && vkLyrics) {
    		getVkLyrics(data);
    	} else {
    		getMMLyrics(data);
    	}
    }

    public void showYoutubePage(final SongData data) {
        final ProgressDialog dialog = new ProgressDialog(view);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Waiting for answer");
        dialog.setMessage("Please wait for answer from youtube app");
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String youtubeUrl;
                try {
                    youtubeUrl = YoutubeUtils.sendRequest(data.getArtist() + " " + data.getTitle());
                    if (youtubeUrl == null) {
                        Toast.makeText(view, "Can not find video", Toast.LENGTH_SHORT).show();
                    } else {
                        view.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)));
                    }
                    dialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  \
                }
            }
        }).start();
    }
}
