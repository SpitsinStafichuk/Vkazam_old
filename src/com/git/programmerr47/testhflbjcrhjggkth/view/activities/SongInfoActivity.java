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

import android.widget.*;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class SongInfoActivity extends Activity implements IPlayerStateObserver {
	public static final String TAG = "SongInfoActivity";
	public static final String ARGUMENT_SONGLIST_POSITION = "SongDataPosition";

    public static final int ANY_SONG = 0;
    public static final int VK_SONG = 1;
    public static final int PP_SONG = 2;

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
	private ProgressDialog downloadPPProgressDialog;
	private ImageButton deleteButton;
	private AnimationDrawable frameAnimation;

    private int songType = PP_SONG;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_information_layout);
		Log.i(TAG, "Creating song info activity");
		
		Log.v("testik", "findViewById(R.id.songInfoCoverArt): " + findViewById(R.id.songInfoCoverArt));
		coverArt = ((DynamicImageView) findViewById(R.id.songInfoCoverArt));
		coverArt.setMaxRatioKoef(1.0);
		
		controller = new SongInfoController(this);
		model = RecognizeServiceConnection.getModel();
		vkApi = model.getVkApi();
		model.getPlayer().addPlayerStateObserver(this);
		data = model.getCurrentOpenSong();
		fillActivity(data);


        ppPlayPauseButton = (ImageButton) findViewById(R.id.songInfoPlayPauseButtonForPP);
        ppPlayPauseButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                songType = PP_SONG;
                controller.playPauseSong(data, -1);
            }
        });

        vkPlayPauseButton = (ImageButton) findViewById(R.id.songInfoPlayPauseButtonForVk);
        vkPlayPauseButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                songType = VK_SONG;
                controller.playPauseSong(data, -1);
            }
        });

        shareButton = (ImageButton) findViewById(R.id.shareButton);
		shareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Like song");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I just listened a nice song on MicroScrobbler");
				startActivity(Intent.createChooser(sharingIntent, "Share song"));
			}
		});
		
		addVkButton = (ImageButton) findViewById(R.id.addVkButton);
		addVkButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AddToVkTask().execute(data);
			}
		});
		
		downloadPPProgressDialog = new ProgressDialog(this);
		downloadPPProgressDialog.setMessage(data.getFullTitle() + " is downloading");
		downloadPPProgressDialog.setIndeterminate(true);
		downloadPPProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		downloadPPProgressDialog.setCancelable(true);
		
		downloadPPButton = (ImageButton) findViewById(R.id.downloadPPButton);
		downloadPPButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(FileSystemUtils.isExternalStorageWritable()) {
					final DownloadTask downloadTask = new DownloadTask(SongInfoActivity.this, DownloadTask.PP_TASK);
					downloadTask.execute(data);
					downloadPPProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					    @Override
					    public void onCancel(DialogInterface dialog) {
					        downloadTask.cancel(true);
					    }
					});
				} else {
					Toast.makeText(SongInfoActivity.this, "External storage is not available", Toast.LENGTH_SHORT).show();
				}
			}
		});

        downloadVKButton = (ImageButton) findViewById(R.id.downloadVKButton);
        downloadVKButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FileSystemUtils.isExternalStorageWritable()) {
                    final DownloadTask downloadTask = new DownloadTask(SongInfoActivity.this, DownloadTask.VK_TASK);
                    downloadTask.execute(data);
                    downloadPPProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            downloadTask.cancel(true);
                        }
                    });
                } else {
                    Toast.makeText(SongInfoActivity.this, "External storage is not available", Toast.LENGTH_SHORT).show();
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

        TextView ppSong = (TextView) findViewById(R.id.SongInfoRealArtistTitleForPP);
        ppSong.setSelected(true);

        TextView vkSong = (TextView) findViewById(R.id.SongInfoRealArtistTitleForVk);
        vkSong.setSelected(true);
	}
	
	private class DownloadTask extends AsyncTask<DatabaseSongData, Integer, String> {

        public static final int PP_TASK = 1;
        public static final int VK_TASK = 2;

	    private Context context;
        private int task;

	    public DownloadTask(Context context, int task) {
	        this.context = context;
            this.task = task;
	    }
	    
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        downloadPPProgressDialog.show();
	    }

	    @Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	        // if we get here, length is known, now set indeterminate to false
	        downloadPPProgressDialog.setIndeterminate(false);
	        downloadPPProgressDialog.setMax(100);
	        downloadPPProgressDialog.setProgress(progress[0]);
	    }
	    
	    @Override
	    protected void onPostExecute(String result) {
	    	downloadPPProgressDialog.dismiss();
	        if (result != null) {
	            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
	        } else {
	            Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
	        }
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
                            throw new JSONException("No vk api. Try to set it in settings");
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
	                    if (isCancelled())
	                        return null;
	                    total += count;
	                    // publishing the progress....
	                    if (fileLength > 0) // only if total length is known
	                        publishProgress((int) (total * 100 / fileLength));
	                    output.write(data, 0, count);
	                }
	            } catch (Exception e) {
	                return e.toString();
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
		
	     protected String doInBackground(DatabaseSongData... databaseSongDatas) {
	    	 String result = null;
	    	 String audioId = databaseSongDatas[0].getVkAudioId();
	    	 if(audioId == null) {
	    		 try {
					databaseSongDatas[0].findVkAudio(vkApi);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					result = e.getMessage();
				} catch (IOException e) {
					e.printStackTrace();
					result = e.getMessage();
				} catch (JSONException e) {
					e.printStackTrace();
					result = e.getMessage();
				} catch (KException e) {
					e.printStackTrace();
					result = e.getMessage();
				} catch (SongNotFoundException e) {
					e.printStackTrace();
					result = e.getMessage();
				}
	    		 audioId = databaseSongDatas[0].getVkAudioId();
	    	 }
             if (audioId != null) {
                 String[] ids = audioId.split("_");
                 long oid = Long.parseLong(ids[0]);
                 long aid = Long.parseLong(ids[1]);
                 try {
                     result = "Song was added with id " +
                             vkApi.addAudio(aid, oid, null, null, null);
                 } catch (MalformedURLException e) {
                     e.printStackTrace();
                     result = e.getMessage();
                 } catch (IOException e) {
                     result = e.getMessage();
                     e.printStackTrace();
                 } catch (JSONException e) {
                     result = e.getMessage();
                     e.printStackTrace();
                 } catch (KException e) {
                     result = e.getMessage();
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
	}

	@Override
	public void updatePlayerState() {
		ProgressBar progressBar;
        ImageButton playPauseButton;
        if (songType == PP_SONG) {
            progressBar = (ProgressBar) findViewById(R.id.songInfoLoadingForPP);
            playPauseButton = ppPlayPauseButton;

            ProgressBar secPB = (ProgressBar) findViewById(R.id.songInfoLoadingForVk);
            secPB.setVisibility(View.GONE);
            vkPlayPauseButton.setImageResource(android.R.drawable.ic_media_play);
        } else {
            progressBar = (ProgressBar) findViewById(R.id.songInfoLoadingForVk);
            playPauseButton = vkPlayPauseButton;

            ProgressBar secPB = (ProgressBar) findViewById(R.id.songInfoLoadingForPP);
            secPB.setVisibility(View.GONE);
            ppPlayPauseButton.setImageResource(android.R.drawable.ic_media_play);
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
