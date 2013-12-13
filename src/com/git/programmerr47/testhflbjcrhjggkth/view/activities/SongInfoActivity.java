package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SongInfoController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SongInfoActivity extends Activity implements IPlayerStateObserver {
	public static final String TAG = "SongInfoActivity";
	public static final String ARGUMENT_SONGLIST_POSITION = "SongDataPosition";

	private MicroScrobblerModel model;
	private SongInfoController controller;
	private DatabaseSongData data;
	private DynamicImageView coverArt;
	private Api vkApi;

	private ImageButton playPauseButton;
	private ImageButton shareButton;
	private ImageButton addVkButton;
	private ImageButton downloadPPButton;
	private ProgressDialog downloadPPProgressDialog;
	private ImageButton deleteButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_information_layout);
		Log.i(TAG, "Creating song info activity");
		Intent intent = getIntent();
		int position = 0;
		if ((intent != null) && (intent.getExtras() != null)) {
			position = intent.getIntExtra(HistoryPageFragment.ARGUMENT_SONGLIST_POSITION, 0);
		}
        final int positionInList = position;
		
		Log.v("testik", "findViewById(R.id.songInfoCoverArt): " + findViewById(R.id.songInfoCoverArt));
		coverArt = ((DynamicImageView) findViewById(R.id.songInfoCoverArt));
		coverArt.setMaxRatioKoef(1.0);
		
		controller = new SongInfoController(this);
		model = RecognizeServiceConnection.getModel();
		vkApi = model.getVkApi();
		model.getPlayer().addPlayerStateObserver(this);
		data = model.getCurrentOpenSong();
		fillActivity(data);
		
		
		playPauseButton = (ImageButton) findViewById(R.id.songInfoPlayPauseButtonForPP);
		playPauseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				controller.playPauseSong(data, positionInList);
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
				String audioId = data.getVkAudioId();
				if(audioId != null ) {
					String[] ids = audioId.split("_");
					long oid = Long.parseLong(ids[0]);
					long aid = Long.parseLong(ids[1]);
					new AddToVkTask().execute(oid, aid);
				} else {
					Toast.makeText(SongInfoActivity.this, "Choose song from vk before adding", Toast.LENGTH_SHORT).show();
				}
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
				String url = data.getPleercomUrl();
				if(url != null ) {
					if(FileSystemUtils.isExternalStorageWritable()) {
						final DownloadTask downloadTask = new DownloadTask(SongInfoActivity.this);
						downloadTask.execute(url);
						downloadPPProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
						    @Override
						    public void onCancel(DialogInterface dialog) {
						        downloadTask.cancel(true);
						    }
						});
					} else {
						Toast.makeText(SongInfoActivity.this, "External storage is not available", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(SongInfoActivity.this, "Choose song from pleer.com before adding", Toast.LENGTH_SHORT).show();
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
	}
	
	private class DownloadTask extends AsyncTask<String, Integer, String> {

	    private Context context;

	    public DownloadTask(Context context) {
	        this.context = context;
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
	    protected String doInBackground(String... sUrl) {
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
	                URL url = new URL(sUrl[0]);
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
	
	private class AddToVkTask extends AsyncTask<Long, Void, String> {
		
	     protected String doInBackground(Long... ids) {
	    	 String result = null;
	         try {
	        	 result = "Song was added with id " + 
	        			 vkApi.addAudio(ids[1], ids[0], null, null, null);
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
				//TODO Может нужно перенести в другое место
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
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.songInfoLoadingForPP);

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
