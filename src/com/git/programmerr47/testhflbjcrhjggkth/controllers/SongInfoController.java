package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import java.io.IOException;
import java.net.MalformedURLException;

import com.git.programmerr47.testhflbjcrhjggkth.view.activities.PagerActivity;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.SongInfoActivity;
import org.json.JSONException;

import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.SongNotFoundException;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.RefreshPagerActivity;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.VkLyricsActivity;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.SongReplacePagerAdapter;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.MessageDialogFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.ProgressDialogFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.MessageDialogFragment.onDialogClickListener;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.ProgressDialogFragment.OnCancelListener;
import com.musixmatch.lyrics.musiXmatchLyricsConnector;
import com.perm.kate.api.Api;
import com.perm.kate.api.KException;
import com.git.programmerr47.testhflbjcrhjggkth.utils.NetworkUtils;
import com.git.programmerr47.testhflbjcrhjggkth.utils.YoutubeUtils;

public class SongInfoController extends SongController{
    private static final String SHOW_DIALOG_TAG = "dialog";
    private SongInfoActivity songInfoActivity;
	
	public SongInfoController(SongInfoActivity view) {
		super(view);
        songInfoActivity = view;
	}
	
	public void getVkLyrics(final SongData data) {

		ProgressDialogFragment.Builder appProgressDialogBuilder = new ProgressDialogFragment.Builder();
		appProgressDialogBuilder.setIcon(R.drawable.ic_progress_dialog);
		appProgressDialogBuilder.setMessage(view.getString(R.string.awaiting_vk_lyrics_message));
		appProgressDialogBuilder.setTitle(view.getString(R.string.awaiting_title));
		appProgressDialogBuilder.setProgressStyle(ProgressDialogFragment.Builder.STYLE_SPINNER);

        FragmentTransaction fragmentTransaction = view.getSupportFragmentManager().beginTransaction();
        Fragment prev = view.getSupportFragmentManager().findFragmentByTag(SHOW_DIALOG_TAG);
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }

        final DialogFragment dialogFragment = ProgressDialogFragment.newInstance(appProgressDialogBuilder);
        dialogFragment.show(fragmentTransaction, SHOW_DIALOG_TAG);
        
		new Thread(new Runnable() {
			Api api = model.getVkApi();
			
	        public void run() {
                Looper.prepare();
	        	if (api != null) {
	        		try {
						String lyrics = data.getLyrics(api);
                        songInfoActivity.setRealArtistTitleForPlayers();
						if (lyrics != null) {
							Intent intent = new Intent(view, VkLyricsActivity.class);
							intent.putExtra(VkLyricsActivity.LYRICS_KEY, lyrics);
							view.startActivity(intent);
						} else {
			                FragmentTransaction fragmentTransaction = view.getSupportFragmentManager().beginTransaction();
			                Fragment prev = view.getSupportFragmentManager().findFragmentByTag(SHOW_DIALOG_TAG);
			                if (prev != null) {
			                    fragmentTransaction.remove(prev);
			                }
			                
			                MessageDialogFragment.Builder appDialogBuilder = new MessageDialogFragment.Builder();
			        		appDialogBuilder.setIcon(R.drawable.ic_alert_dialog);
			        		appDialogBuilder.setMessage(view.getString(R.string.no_vk_lyrics_message));
			        		appDialogBuilder.setTitle(view.getString(R.string.no_vk_lyrics_title));
			        		appDialogBuilder.setPositiveButton(view.getString(R.string.action_refresh), new onDialogClickListener() {
								
								@Override
								public void onDialogClick(DialogFragment fragment, View v) {
			        				Intent intent = new Intent(view, RefreshPagerActivity.class);
			        				intent.putExtra(PagerActivity.PAGE_NUMBER, SongReplacePagerAdapter.VK_PAGE_NUMBER);
			        				view.startActivity(intent);
			        				fragment.dismiss();
								}
							});
			        		appDialogBuilder.setNegativeButton(view.getString(R.string.get_musixmatch_lyrics), new onDialogClickListener() {
								
								@Override
								public void onDialogClick(DialogFragment fragment, View v) {
			        				getMMLyrics(data);
			        				fragment.dismiss();
								}
							});

			                DialogFragment dialogFragment = MessageDialogFragment.newInstance(appDialogBuilder);
			                dialogFragment.show(fragmentTransaction, SHOW_DIALOG_TAG);
						}
					} catch (MalformedURLException e) {
						showToast(view.getString(R.string.internet_connection_not_available));
						e.printStackTrace();
					} catch (IOException e) {
                        showToast(view.getString(R.string.internet_connection_not_available));
						e.printStackTrace();
					} catch (JSONException e) {
						showToast(e.getLocalizedMessage());
						e.printStackTrace();
					} catch (KException e) {
						showToast(e.getLocalizedMessage());
						e.printStackTrace();
					} catch (SongNotFoundException e) {
						showToast(view.getString(R.string.song_not_found));
						e.printStackTrace();
					}
	        		
	        	} else {
	        		showToast(view.getString(R.string.vk_not_available));
	        	}
	        	dialogFragment.dismiss();
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
                } while (!lyricsPlugin.getIsBound() && !((s - millis) > 2000));
                if (lyricsPlugin.getIsBound()) {
                    final String artist = data.getArtist();
                    final String title = data.getTitle();
                    view.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                lyricsPlugin.startLyricsActivity(artist,title);
                            } catch (RemoteException e) {
                                Toast.makeText(view, view.getString(R.string.musixmatch_not_available), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else { 
                    FragmentTransaction fragmentTransaction = view.getSupportFragmentManager().beginTransaction();
                    Fragment prev = view.getSupportFragmentManager().findFragmentByTag(SHOW_DIALOG_TAG);
                    if (prev != null) {
                        fragmentTransaction.remove(prev);
                    }
                    
                    MessageDialogFragment.Builder appDialogBuilder = new MessageDialogFragment.Builder();
                    
	        		appDialogBuilder.setIcon(R.drawable.ic_alert_dialog);
                    appDialogBuilder.setMessage(view.getString(R.string.no_musixmatch_app_message));
                    appDialogBuilder.setTitle(view.getString(R.string.no_musixmatch_app_title));
                    appDialogBuilder.setPositiveButton(view.getString(R.string.get_musixmatch), new onDialogClickListener() {
						
						@Override
						public void onDialogClick(DialogFragment fragment, View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.musixmatch.android.lyrify"));
                            view.startActivity(intent);
                            fragment.dismiss();
						}
					});
                    appDialogBuilder.setNegativeButton(view.getString(R.string.no_thanks), new onDialogClickListener() {
						
						@Override
						public void onDialogClick(DialogFragment fragment, View v) {
							fragment.dismiss();
						}
					});

                    DialogFragment dialogFragment = MessageDialogFragment.newInstance(appDialogBuilder);
                    dialogFragment.show(fragmentTransaction, SHOW_DIALOG_TAG);
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
    
		ProgressDialogFragment.Builder appProgressDialogBuilder = new ProgressDialogFragment.Builder();
		appProgressDialogBuilder.setIcon(R.drawable.ic_progress_dialog);
		appProgressDialogBuilder.setMessage(view.getString(R.string.awaiting_youtube_message));
		appProgressDialogBuilder.setTitle(view.getString(R.string.awaiting_title));
		appProgressDialogBuilder.setProgressStyle(ProgressDialogFragment.Builder.STYLE_SPINNER);

        FragmentTransaction fragmentTransaction = view.getSupportFragmentManager().beginTransaction();
        Fragment prev = view.getSupportFragmentManager().findFragmentByTag(SHOW_DIALOG_TAG);
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        
        Thread youtubeThread = null;
        
        appProgressDialogBuilder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(ProgressDialogFragment fragment) {
            	//youtubeThread.interrupt();
            }
        });

        final DialogFragment dialogFragment = ProgressDialogFragment.newInstance(appProgressDialogBuilder);
        dialogFragment.show(fragmentTransaction, SHOW_DIALOG_TAG);

        youtubeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String youtubeUrl;
                try {
                    youtubeUrl = YoutubeUtils.sendRequest(data.getArtist() + " " + data.getTitle());
                    if (youtubeUrl == null) {
                        Toast.makeText(view, view.getString(R.string.video_not_found), Toast.LENGTH_SHORT).show();
                    } else {
                        view.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)));
                    }
                    dialogFragment.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  \
                }
            }
        });
        youtubeThread.start();
    }
}
