package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.media.MediaPlayer;
import android.view.MenuInflater;
import android.widget.*;

import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SongManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongProgressObserver;
import com.git.programmerr47.testhflbjcrhjggkth.utils.AndroidUtils;
import com.perm.kate.api.Audio;
import org.json.JSONException;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SongInfoController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.SongNotFoundException;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObserver;
import com.git.programmerr47.testhflbjcrhjggkth.utils.FileSystemUtils;
import com.git.programmerr47.testhflbjcrhjggkth.view.DynamicImageView;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.HistoryPageFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.MessageDialogFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.ProgressDialogFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.ProgressDialogFragment.Builder;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.ProgressDialogFragment.OnCancelListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.perm.kate.api.Api;
import com.perm.kate.api.KException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class SongInfoActivity extends FragmentActivity implements IPlayerStateObserver, ISongProgressObserver {
	public static final String TAG = "SongInfoActivity";
	public static final String ARGUMENT_SONGLIST_POSITION = "SongDataPosition";
    private static final String SHOW_DIALOG_TAG = "dialog";

	private MicroScrobblerModel model;
	private SongInfoController controller;
	private DatabaseSongData data;
	private DynamicImageView coverArt;
	private Api vkApi;

    private ImageButton ppPlayPauseButton;
    private ImageButton vkPlayPauseButton;
	private ImageButton shareButton;
	private ImageButton addVkButton;
	private ImageButton downloadPPButton;
    private ImageButton downloadVKButton;
    private ProgressDialogFragment.Builder appProgressDialogBuilder;
	private ImageButton deleteButton;
    private ImageButton settingsButton;
    private ImageButton lyricsButton;
    private ImageButton youtubeButton;
    private SeekBar ppSongProgress;
    private SeekBar vkSongProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_information_layout);
		Log.i(TAG, "Creating song info activity");

		coverArt = ((DynamicImageView) findViewById(R.id.songInfoCoverArt));
		coverArt.setMaxRatioKoef(1.0);
		
		controller = new SongInfoController(this);
		model = RecognizeServiceConnection.getModel();
		vkApi = model.getVkApi();
		model.getPlayer().addPlayerStateObserver(this);
        model.getSongManager().addSongProgressObserver(this);
        model.getSongManager().setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
                Log.v("MiniPlayer", "Song downloading is updated " + percent);
                SeekBar songProgress = vkSongProgress;
                if (model.getSongManager().getType() == SongManager.PP_SONG) {
                    songProgress = ppSongProgress;
                }

                if (data.equals(model.getSongManager().getSongData())) {
                    songProgress.setSecondaryProgress(percent);
                }
            }
        });
		data = model.getCurrentOpenSong();
		fillActivity(data);

        ppSongProgress = (SeekBar) findViewById(R.id.SongProgressForPP);
        ppSongProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ((fromUser) && (model.getSongManager().getType() == SongManager.PP_SONG)) {
                    controller.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        vkSongProgress = (SeekBar) findViewById(R.id.SongProgressForVk);
        vkSongProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ((fromUser) && (model.getSongManager().getType() == SongManager.VK_SONG)) {
                    controller.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        ppPlayPauseButton = (ImageButton) findViewById(R.id.songInfoPlayPauseButtonForPP);
        ppPlayPauseButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!data.equals(model.getSongManager().getSongData())) {
                    model.getSongManager().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            Log.v("MiniPlayer", "pp Song is completed");
                        }
                    });
                }
                controller.playPauseSong(data, model.getCurrentOpenSongPosition(), SongManager.PP_SONG);
            }
        });

        vkPlayPauseButton = (ImageButton) findViewById(R.id.songInfoPlayPauseButtonForVk);
        vkPlayPauseButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!data.equals(model.getSongManager().getSongData())) {
                    model.getSongManager().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            Log.v("MiniPlayer", "vk Song is completed");
                        }
                    });
                }
                controller.playPauseSong(data, model.getCurrentOpenSongPosition(), SongManager.VK_SONG);
            }
        });

        shareButton = (ImageButton) findViewById(R.id.shareButton);
		shareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.new_song_title));
                if (data.getAlbum() != null) {
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.new_song_message) + " " + data.getArtist() + " - " + data.getTitle() + " ("+ data.getAlbum() +")");
                } else {
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.new_song_message) + " " + data.getArtist() + " - " + data.getTitle());
                }
				startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_song)));
			}
		});
		
		addVkButton = (ImageButton) findViewById(R.id.addVkButton);
		addVkButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AddToVkTask(SongInfoActivity.this).execute(data);
			}
		});
		
		appProgressDialogBuilder = new ProgressDialogFragment.Builder();
		appProgressDialogBuilder.setIcon(R.drawable.ic_progress_dialog);
		appProgressDialogBuilder.setMessage(data.getFullTitle() + " " + getString(R.string.loading_message));
		appProgressDialogBuilder.setTitle(getString(R.string.loading_title));
		appProgressDialogBuilder.setProgressStyle(ProgressDialogFragment.Builder.STYLE_HORIZONTAL);
		
		downloadPPButton = (ImageButton) findViewById(R.id.downloadPPButton);
		downloadPPButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(FileSystemUtils.isExternalStorageWritable()) {
					final DownloadTask downloadTask = new DownloadTask(SongInfoActivity.this, DownloadTask.PP_TASK);
                    appProgressDialogBuilder.setOnCancelListener(new OnCancelListener() {

                        @Override
                        public void onCancel(ProgressDialogFragment fragment) {
                            downloadTask.cancel(true);
                        }
                    });
					downloadTask.execute(data);
				} else {
					Toast.makeText(SongInfoActivity.this, getString(R.string.ext_storage_not_available), Toast.LENGTH_SHORT).show();
				}
			}
		});

        downloadVKButton = (ImageButton) findViewById(R.id.downloadVKButton);
        downloadVKButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FileSystemUtils.isExternalStorageWritable()) {
                    final DownloadTask downloadTask = new DownloadTask(SongInfoActivity.this, DownloadTask.VK_TASK);
                    appProgressDialogBuilder.setOnCancelListener(new OnCancelListener() {

                        @Override
                        public void onCancel(ProgressDialogFragment fragment) {
                            downloadTask.cancel(true);
                        }
                    });
                    downloadTask.execute(data);
                } else {
                    Toast.makeText(SongInfoActivity.this, getString(R.string.ext_storage_not_available), Toast.LENGTH_SHORT).show();
                }
            }
        });
		
		deleteButton = (ImageButton) findViewById(R.id.deleteButton);
		deleteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				model.getSongList().remove(data);
				Intent intent = new Intent(SongInfoActivity.this, MicrophonePagerActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
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

        lyricsButton = (ImageButton) findViewById(R.id.lyricsButton);
        lyricsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.getLyrics(data);
            }
        });

        youtubeButton = (ImageButton) findViewById(R.id.youtubeButton);
        youtubeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.showYoutubePage(data);
            }
        });

        TextView ppSong = (TextView) findViewById(R.id.SongInfoRealArtistTitleForPP);
        ppSong.setSelected(true);

        TextView vkSong = (TextView) findViewById(R.id.SongInfoRealArtistTitleForVk);
        vkSong.setSelected(true);

        TextView artist = (TextView) findViewById(R.id.artistTitle);
        artist.setText(data.getArtist());

        TextView title = (TextView) findViewById(R.id.titleTitle);
        title.setText(data.getTitle());
	}

    private class DownloadTask extends AsyncTask<DatabaseSongData, Integer, String> {

        public static final int PP_TASK = 1;
        public static final int VK_TASK = 2;

	    private Context context;
        private int task;
        private ProgressDialogFragment dialogFragment;

	    public DownloadTask(Context context, int task) {
	        this.context = context;
            this.task = task;
	    }
	    
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
//	        downloadPPProgressDialog.show();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag(SHOW_DIALOG_TAG);
            if (prev != null) {
                fragmentTransaction.remove(prev);
            }

            dialogFragment = ProgressDialogFragment.newInstance(appProgressDialogBuilder);
            dialogFragment.show(fragmentTransaction, SHOW_DIALOG_TAG);
	    }

	    @Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	        // if we get here, length is known, now set indeterminate to false
	        dialogFragment.setProgress(progress[0]);
	    }
	    
	    @Override
	    protected void onPostExecute(String result) {
            Log.v("ProgressDialog", "onPostDownLoad: " + result);
	    	dialogFragment.dismiss();
	        if (result != null) {
	            Toast.makeText(context, context.getString(R.string.download_error) + " " + result, Toast.LENGTH_LONG).show();
	        } else {
	            Toast.makeText(context, context.getString(R.string.download_success), Toast.LENGTH_SHORT).show();
	        }
	    }

        @Override
        protected void onCancelled(String result) {
            Log.v("ProgressDialog", "onCancel: " + result);
            Toast.makeText(context, context.getString(R.string.download_cancel), Toast.LENGTH_SHORT).show();
        }

	    @Override
	    protected String doInBackground(DatabaseSongData... databaseSongData) {
	        // take CPU lock to prevent CPU from going off if the user 
	        // presses the power button during download
	        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
	             getClass().getName());
	        wl.acquire();

	        try {
	            InputStream input = null;
	            OutputStream output = null;
	            HttpURLConnection connection = null;
	            try {
                    URL url;
                    if (task == PP_TASK) {
                        String urlString = databaseSongData[0].getPleercomUrl();
                        if(urlString == null) {
                            databaseSongData[0].findPPAudio();
                            urlString = databaseSongData[0].getPleercomUrl();
                        }
                        url = new URL(urlString);
                    } else {
                        if (model.getVkApi() != null) {
                            List<Audio> audioList = model.getVkApi().getAudioById(databaseSongData[0].getVkAudioId(), null, null);
                            if (audioList.isEmpty()) {
                                throw new SongNotFoundException();
                            }
                            String vkUrl = audioList.get(0).url;
                            url = new URL(vkUrl);
                        } else {
                            throw new JSONException(context.getString(R.string.vk_not_available));
                        }
                    }
	                connection = (HttpURLConnection) url.openConnection();
	                connection.connect();

	                // expect HTTP 200 OK, so we don't mistakenly save error report 
	                // instead of the file
	                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
	                     return "Server returned HTTP " + connection.getResponseCode() 
	                         + " " + connection.getResponseMessage();

	                // this will be useful to display download percentage
	                // might be -1: server did not report the length
	                int fileLength = connection.getContentLength();

	                // download the file
	                input = connection.getInputStream();
	                File file = new File(Environment.getExternalStoragePublicDirectory(
	                		Environment.DIRECTORY_MUSIC), data.getFullTitle() + ".mp3");
	                output = new FileOutputStream(file);

	                byte data[] = new byte[4096];
	                long total = 0;
	                int count;
	                while ((count = input.read(data)) != -1) {
	                    // allow canceling with back button
	                    if (isCancelled()) {
                            Log.v("ProgressDialog", "Download is canceled");
                            return context.getString(R.string.download_cancel);
                        }
	                    total += count;
	                    // publishing the progress....
	                    if (fileLength > 0) // only if total length is known
	                        publishProgress((int) (total * 100 / fileLength));
	                    output.write(data, 0, count);
	                }
	            } catch (Exception e) {
	                return e.getLocalizedMessage();
	            } finally {
	                try {
	                    if (output != null)
	                        output.close();
	                    if (input != null)
	                        input.close();
	                } 
	                catch (IOException ignored) { }

	                if (connection != null)
	                    connection.disconnect();
	            }
	        } finally {
	            wl.release();
	        }
	        return null;
	    }
	}
	
	private class AddToVkTask extends AsyncTask<DatabaseSongData, Void, String> {

        private Context context;

        public AddToVkTask(Context context) {
            this.context = context;
        }

        protected String doInBackground(DatabaseSongData... databaseSongData) {
            String result = null;
            String audioId = databaseSongData[0].getVkAudioId();
            if(audioId == null) {
                try {
                    databaseSongData[0].findVkAudio(vkApi);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    result = e.getLocalizedMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    result = e.getLocalizedMessage();
                } catch (JSONException e) {
                    e.printStackTrace();
                    result = e.getLocalizedMessage();
                } catch (KException e) {
                    e.printStackTrace();
                    result = e.getLocalizedMessage();
                } catch (SongNotFoundException e) {
                    e.printStackTrace();
                    result = e.getLocalizedMessage();
                }
                audioId = databaseSongData[0].getVkAudioId();
            }
            if (audioId != null) {
                String[] ids = audioId.split("_");
                long oid = Long.parseLong(ids[0]);
                long aid = Long.parseLong(ids[1]);
                try {
                    vkApi.addAudio(aid, oid, null, null, null);
                    result = context.getString(R.string.added_in_vk_music);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    result = e.getLocalizedMessage();
                } catch (IOException e) {
                    result = e.getLocalizedMessage();
                    e.printStackTrace();
                } catch (JSONException e) {
                    result = e.getLocalizedMessage();
                    e.printStackTrace();
                } catch (KException e) {
                    result = e.getLocalizedMessage();
                    e.printStackTrace();
                }
            }
            return result;
        }

        protected void onPostExecute(String result) {
            Toast.makeText(SongInfoActivity.this, result, Toast.LENGTH_SHORT).show();
        }
	}
	
	private void fillActivity(SongData data) {
		if (data != null) {
			fillTextInformation(R.id.songInfoArtist, data.getArtist());
			fillTextInformation(R.id.songInfoTitle, data.getTitle());
			fillTextInformation(R.id.songInfoDate, data.getDate().toString());
			fillTextInformation(R.id.songInfoTrackId, data.getTrackId());
			
			if (data.getCoverArtUrl() != null) {
				String url = data.getCoverArtUrl();
				if (url.contains("size=small")) {
					url = url.replace("size=small", "size=large");
				}
				DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.no_cover_art)
					.showImageOnFail(R.drawable.no_cover_art)
					.cacheOnDisc(true)
					.cacheInMemory(true)
					.build();
				model.getImageLoader().displayImage(url, coverArt, options);
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

        LinearLayout vkPlayerLabel = (LinearLayout) findViewById(R.id.vkPlayerLabel);
        LinearLayout vkPlayer = (LinearLayout) findViewById(R.id.vkPlayer);
        if (model.getVkApi() == null) {
            vkPlayer.setVisibility(View.GONE);
            vkPlayerLabel.setVisibility(View.GONE);
        } else {
            vkPlayer.setVisibility(View.VISIBLE);
            vkPlayerLabel.setVisibility(View.VISIBLE);
        }

		updatePlayerState();
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
        model.getPlayer().removePlayerStateObserver(this);
        model.getSongManager().removeSongProgressObserver(this);
	}

	@Override
	public void updatePlayerState() {
		ProgressBar progressBar;
        ImageButton playPauseButton;
        if (model.getSongManager().getType() == SongManager.PP_SONG) {
            progressBar = (ProgressBar) findViewById(R.id.songInfoLoadingForPP);
            playPauseButton = ppPlayPauseButton;

            ProgressBar secPB = (ProgressBar) findViewById(R.id.songInfoLoadingForVk);
            secPB.setVisibility(View.GONE);
            vkPlayPauseButton.setImageResource(android.R.drawable.ic_media_play);
            vkPlayPauseButton.setVisibility(View.VISIBLE);
            vkSongProgress.setProgress(0);
            vkSongProgress.setSecondaryProgress(0);
        } else {
            progressBar = (ProgressBar) findViewById(R.id.songInfoLoadingForVk);
            playPauseButton = vkPlayPauseButton;

            ProgressBar secPB = (ProgressBar) findViewById(R.id.songInfoLoadingForPP);
            secPB.setVisibility(View.GONE);
            ppPlayPauseButton.setImageResource(android.R.drawable.ic_media_play);
            ppPlayPauseButton.setVisibility(View.VISIBLE);
            ppSongProgress.setProgress(0);
            ppSongProgress.setSecondaryProgress(0);
        }

		Log.v(TAG, "Current Data: " + data.getDate());
		Log.v(TAG, "SongManager Data: " + data.getDate());
		if (data.equals(model.getSongManager().getSongData())) {
			Log.v(TAG, "Data are equals");
			if (model.getSongManager().isLoading()) {
				progressBar.setVisibility(View.VISIBLE);
                playPauseButton.setVisibility(View.GONE);
				Log.v("SongPlayer", "Song" + model.getSongManager().getArtist() + " - " + model.getSongManager().getTitle() + "is loading");
			} else {
				progressBar.setVisibility(View.GONE);
				if (model.getSongManager().isPrepared()) {
                    playPauseButton.setVisibility(View.VISIBLE);
				} else {
					if (playPauseButton.getVisibility() == View.GONE)
						progressBar.setVisibility(View.INVISIBLE);
				}
				Log.v("SongPlayer", "Song is not loading");
			}
			
			if (model.getSongManager().isPlaying()) {
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
				Log.v("SongPlayer", "Song" + model.getSongManager().getArtist() + " - " + model.getSongManager().getTitle() + "is playing");
			} else {
                playPauseButton.setImageResource(android.R.drawable.ic_media_play);
				Log.v("SongPlayer", "Song is on pause");
			}
		}
	}

    @Override
    public void updateProgress(int progress, int duration) {
        SeekBar songProgress = vkSongProgress;
        if (model.getSongManager().getType() == SongManager.PP_SONG) {
            songProgress = ppSongProgress;
        }

        if (data.equals(model.getSongManager().getSongData())) {
            if (duration == -1) {
                songProgress.setProgress(0);
                songProgress.setSecondaryProgress(0);
            } else {
                songProgress.setProgress(progress * 100 / duration);
            }
        } else {
            songProgress.setProgress(0);
            songProgress.setSecondaryProgress(0);
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.song_info_options_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        	case R.id.settings :
        		Log.v("Settings", "Creating settings activity");
            	Intent intent = new Intent(SongInfoActivity.this, SettingsActivity.class);
            	startActivity(intent);
            	return true;
        	case R.id.refresh :
        		Log.v("Settings", "Creating refresh activity");
				intent = new Intent(SongInfoActivity.this, RefreshPagerActivity.class);
				startActivity(intent);
            	return true;
        	default :
        		return super.onOptionsItemSelected(item);
        }
	}
}
