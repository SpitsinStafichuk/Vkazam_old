package com.git.programmerr47.vkazam.view.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.vkazam.controllers.SongInfoController;
import com.git.programmerr47.vkazam.model.MicroScrobblerModel;
import com.git.programmerr47.vkazam.model.RecognizeServiceConnection;
import com.git.programmerr47.vkazam.model.SongData;
import com.git.programmerr47.vkazam.model.database.DatabaseSongData;
import com.git.programmerr47.vkazam.model.exceptions.SongNotFoundException;
import com.git.programmerr47.vkazam.model.managers.SongManager;
import com.git.programmerr47.vkazam.model.observers.IPlayerStateObserver;
import com.git.programmerr47.vkazam.model.observers.ISongProgressObserver;
import com.git.programmerr47.vkazam.utils.FileSystemUtils;
import com.git.programmerr47.vkazam.utils.NetworkUtils;
import com.git.programmerr47.vkazam.view.DynamicImageView;
import com.git.programmerr47.vkazam.view.activities.MicrophonePagerActivity;
import com.git.programmerr47.vkazam.view.activities.PagerActivity;
import com.git.programmerr47.vkazam.view.activities.RefreshPagerActivity;
import com.git.programmerr47.vkazam.view.activities.SettingsActivity;
import com.git.programmerr47.vkazam.view.adapters.MicrophonePagerAdapter;
import com.git.programmerr47.vkazam.view.fragments.ProgressDialogFragment.OnCancelListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.perm.kate.api.Api;
import com.perm.kate.api.Audio;
import com.perm.kate.api.KException;

public class SongInfoFragment extends Fragment implements IPlayerStateObserver,
		ISongProgressObserver {
	public static final String TAG = "SongInfoFragment";
	public static final String ARGUMENT_SONGLIST_POSITION = "SongDataPosition";
	private static final String SHOW_DIALOG_TAG = "dialog";

	private MicroScrobblerModel model;
	private SongInfoController controller;
	private int position;
	private DatabaseSongData data;
	private DynamicImageView coverArt;
	private Api vkApi;

	private ImageButton ppPlayPauseButton;
	private ImageButton vkPlayPauseButton;
	private ImageButton addVkButton;
	private ImageButton downloadPPButton;
	private ImageButton downloadVKButton;
	private ProgressDialogFragment.Builder appProgressDialogBuilder;
	private ImageButton lyricsButton;
	private ImageButton youtubeButton;
	private SeekBar ppSongProgress;
	private SeekBar vkSongProgress;
	private TextView ppArtistTitleView;
	private TextView vkArtistTitleView;

	private ProgressBar progressBar;
	private ProgressBar secPB;

	private ShareActionProvider shareActionProvider;

	public static SongInfoFragment newInstance(int position) {
		SongInfoFragment fragment = new SongInfoFragment();

		Bundle args = new Bundle();
		args.putInt("position", position);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		position = getArguments().getInt("position");
	}

	/*
	 * @Override public void onStart() { super.onStart(); ((ActionBarActivity)
	 * getActivity()) .setTitle(((DatabaseSongData) RecognizeServiceConnection
	 * .getModel().getSongList().get(position)).getFullTitle()); }
	 */

	@Override
	public void onDestroy() {
		super.onDestroy();
		model.getPlayer().removePlayerStateObserver(this);
		model.getSongManager().removeSongProgressObserver(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_song_info, container,
				false);
		coverArt = ((DynamicImageView) view.findViewById(R.id.songInfoCoverArt));
		coverArt.setMaxRatioKoef(1.0);

		controller = new SongInfoController(getActivity());
		model = RecognizeServiceConnection.getModel();
		vkApi = model.getVkApi();
		model.getPlayer().addPlayerStateObserver(this);
		model.getSongManager().addSongProgressObserver(this);
		model.getSongManager().setOnBufferingUpdateListener(
				new MediaPlayer.OnBufferingUpdateListener() {
					@Override
					public void onBufferingUpdate(MediaPlayer mediaPlayer,
							int percent) {
						Log.v("MiniPlayer", "Song downloading is updated "
								+ percent);
						SeekBar songProgress = vkSongProgress;
						if (model.getSongManager().getType() == SongManager.PP_SONG) {
							songProgress = ppSongProgress;
						}

						if (data.equals(model.getSongManager().getSongData())) {
							songProgress.setSecondaryProgress(percent);
						}
					}
				});
		data = (DatabaseSongData) model.getSongList().get(position);
		fillFragment(data, view);

		ppArtistTitleView = (TextView) view
				.findViewById(R.id.SongInfoRealArtistTitleForPP);
		ppArtistTitleView.setText("");

		vkArtistTitleView = (TextView) view
				.findViewById(R.id.SongInfoRealArtistTitleForVk);
		vkArtistTitleView.setText("");

		ppSongProgress = (SeekBar) view.findViewById(R.id.SongProgressForPP);
		ppSongProgress
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						if ((fromUser)
								&& (model.getSongManager().getType() == SongManager.PP_SONG)) {
							controller.seekTo(progress);
						}
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// To change body of implemented methods use File |
						// Settings | File Templates.
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// To change body of implemented methods use File |
						// Settings | File Templates.
					}
				});

		vkSongProgress = (SeekBar) view.findViewById(R.id.SongProgressForVk);
		vkSongProgress
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						if ((fromUser)
								&& (model.getSongManager().getType() == SongManager.VK_SONG)) {
							controller.seekTo(progress);
						}
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// To change body of implemented methods use File |
						// Settings | File Templates.
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// To change body of implemented methods use File |
						// Settings | File Templates.
					}
				});

		ppPlayPauseButton = (ImageButton) view
				.findViewById(R.id.songInfoPlayPauseButtonForPP);
		ppPlayPauseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!data.equals(model.getSongManager().getSongData())) {
					model.getSongManager().setOnCompletionListener(
							new MediaPlayer.OnCompletionListener() {
								@Override
								public void onCompletion(MediaPlayer mediaPlayer) {
									Log.v("MiniPlayer", "pp Song is completed");
								}
							});
				}
				controller.playPauseSong(data, position, SongManager.PP_SONG);
			}
		});

		vkPlayPauseButton = (ImageButton) view
				.findViewById(R.id.songInfoPlayPauseButtonForVk);
		vkPlayPauseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!data.equals(model.getSongManager().getSongData())) {
					model.getSongManager().setOnCompletionListener(
							new MediaPlayer.OnCompletionListener() {
								@Override
								public void onCompletion(MediaPlayer mediaPlayer) {
									Log.v("MiniPlayer", "vk Song is completed");
								}
							});
				}
				controller.playPauseSong(data, position, SongManager.VK_SONG);
			}
		});

		addVkButton = (ImageButton) view.findViewById(R.id.addVkButton);
		addVkButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (NetworkUtils.isNetworkAvailable(SongInfoFragment.this
						.getActivity())) {
					new AddToVkTask(SongInfoFragment.this.getActivity())
							.execute(data);
				} else {
					Toast.makeText(SongInfoFragment.this.getActivity(),
							getString(R.string.network_not_available),
							Toast.LENGTH_LONG).show();
				}
			}
		});

		appProgressDialogBuilder = new ProgressDialogFragment.Builder();
		appProgressDialogBuilder.setIcon(R.drawable.ic_progress_dialog);
		appProgressDialogBuilder.setMessage(data.getFullTitle() + " "
				+ getString(R.string.loading_message));
		appProgressDialogBuilder.setTitle(getString(R.string.loading_title));
		appProgressDialogBuilder
				.setProgressStyle(ProgressDialogFragment.Builder.STYLE_HORIZONTAL);

		downloadPPButton = (ImageButton) view
				.findViewById(R.id.downloadPPButton);
		downloadPPButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (NetworkUtils.isNetworkAvailable(SongInfoFragment.this
						.getActivity())) {
					if (FileSystemUtils.isExternalStorageWritable()) {
						final DownloadTask downloadTask = new DownloadTask(
								SongInfoFragment.this.getActivity(),
								DownloadTask.PP_TASK);
						appProgressDialogBuilder
								.setOnCancelListener(new OnCancelListener() {

									@Override
									public void onCancel(
											ProgressDialogFragment fragment) {
										downloadTask.cancel(true);
									}
								});
						downloadTask.execute(data);
					} else {
						Toast.makeText(SongInfoFragment.this.getActivity(),
								getString(R.string.ext_storage_not_available),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(SongInfoFragment.this.getActivity(),
							getString(R.string.network_not_available),
							Toast.LENGTH_LONG).show();
				}
			}
		});

		downloadVKButton = (ImageButton) view
				.findViewById(R.id.downloadVKButton);
		downloadVKButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (NetworkUtils.isNetworkAvailable(SongInfoFragment.this
						.getActivity())) {
					if (FileSystemUtils.isExternalStorageWritable()) {
						final DownloadTask downloadTask = new DownloadTask(
								SongInfoFragment.this.getActivity(),
								DownloadTask.VK_TASK);
						appProgressDialogBuilder
								.setOnCancelListener(new OnCancelListener() {

									@Override
									public void onCancel(
											ProgressDialogFragment fragment) {
										downloadTask.cancel(true);
									}
								});
						downloadTask.execute(data);
					} else {
						Toast.makeText(SongInfoFragment.this.getActivity(),
								getString(R.string.ext_storage_not_available),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(SongInfoFragment.this.getActivity(),
							getString(R.string.network_not_available),
							Toast.LENGTH_LONG).show();
				}
			}
		});

		lyricsButton = (ImageButton) view.findViewById(R.id.lyricsButton);
		lyricsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				controller.getLyrics(data, position);
			}
		});

		youtubeButton = (ImageButton) view.findViewById(R.id.youtubeButton);
		youtubeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (NetworkUtils.isNetworkAvailable(SongInfoFragment.this
						.getActivity())) {
					controller.showYoutubePage(data);
				} else {
					Toast.makeText(SongInfoFragment.this.getActivity(),
							R.string.internet_connection_not_available,
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		ppArtistTitleView.setSelected(true);
		vkArtistTitleView.setSelected(true);

		if (model.getSongManager().getType() == SongManager.PP_SONG) {
			progressBar = (ProgressBar) view
					.findViewById(R.id.songInfoLoadingForPP);
			secPB = (ProgressBar) view.findViewById(R.id.songInfoLoadingForVk);
		} else {
			progressBar = (ProgressBar) view
					.findViewById(R.id.songInfoLoadingForVk);
			ProgressBar secPB = (ProgressBar) view
					.findViewById(R.id.songInfoLoadingForPP);
		}

		return view;
	}

	private void fillFragment(SongData data, View view) {
		if (data != null) {
			fillTextInformation(R.id.songInfoArtist, data.getArtist(), view);
			fillTextInformation(R.id.songInfoTitle, data.getTitle(), view);
			fillTextInformation(R.id.songInfoAlbum, data.getAlbum(), view);
			fillTextInformation(R.id.songInfoDate, data.getDate().toString(),
					view);
			fillTextInformation(R.id.songInfoTrackId, data.getTrackId(), view);

			if (data.getCoverArtUrl() != null) {
				String url = data.getCoverArtUrl();
				if (url.contains("size=small")) {
					url = url.replace("size=small", "size=large");
				}
				DisplayImageOptions options = new DisplayImageOptions.Builder()
						.showImageForEmptyUri(R.drawable.no_cover_art)
						.showImageOnFail(R.drawable.no_cover_art)
						.cacheOnDisc(true).cacheInMemory(true).build();
				model.getImageLoader().displayImage(url, coverArt, options);
			}
		}
	}

	private void fillTextInformation(int resId, String text, View view) {
		TextView textView = (TextView) view.findViewById(resId);
		if (textView != null) {
			textView.setText(text);
		}
	}

	private class DownloadTask extends
			AsyncTask<DatabaseSongData, Integer, String> {

		public static final int PP_TASK = 1;
		public static final int VK_TASK = 2;

		private final Context context;
		private final int task;
		private ProgressDialogFragment dialogFragment;

		public DownloadTask(Context context, int task) {
			this.context = context;
			this.task = task;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			FragmentTransaction fragmentTransaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			Fragment prev = getActivity().getSupportFragmentManager()
					.findFragmentByTag(SHOW_DIALOG_TAG);
			if (prev != null) {
				fragmentTransaction.remove(prev);
			}

			dialogFragment = ProgressDialogFragment
					.newInstance(appProgressDialogBuilder);
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
			setRealArtistTitleForPlayers();
			dialogFragment.dismiss();
			if (result != null) {
				Toast.makeText(
						context,
						context.getString(R.string.download_error) + " "
								+ result, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context,
						context.getString(R.string.download_success),
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onCancelled() {
			Log.v("ProgressDialog", "onCancel");
			Toast.makeText(context,
					context.getString(R.string.download_cancel),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		protected String doInBackground(DatabaseSongData... databaseSongData) {
			// take CPU lock to prevent CPU from going off if the user
			// presses the power button during download
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			PowerManager.WakeLock wl = pm.newWakeLock(
					PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
			wl.acquire();

			try {
				InputStream input = null;
				OutputStream output = null;
				HttpURLConnection connection = null;
				try {
					URL url;
					if (task == PP_TASK) {
						String urlString = databaseSongData[0].getPleercomUrl();
						Log.v(TAG, "1 " + urlString);
						if (urlString == null) {
							try {
								databaseSongData[0].findPPAudio();
								urlString = databaseSongData[0]
										.getPleercomUrl();
							} catch (SongNotFoundException e) {
								// return
								// context.getString(R.string.song_not_found);
							}
						}
						url = new URL(urlString);
					} else {
						if (model.getVkApi() != null) {
							List<Audio> audioList = model.getVkApi()
									.getAudioById(
											databaseSongData[0].getVkAudioId(),
											null, null);
							if (audioList.isEmpty()) {
								throw new SongNotFoundException();
							}
							String vkUrl = audioList.get(0).url;
							url = new URL(vkUrl);
						} else {
							throw new JSONException(
									context.getString(R.string.vk_not_available));
						}
					}
					connection = (HttpURLConnection) url.openConnection();
					connection.connect();

					// expect HTTP 200 OK, so we don't mistakenly save error
					// report
					// instead of the file
					if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
						return "Server returned HTTP "
								+ connection.getResponseCode() + " "
								+ connection.getResponseMessage();

					// this will be useful to display download percentage
					// might be -1: server did not report the length
					int fileLength = connection.getContentLength();

					// download the file
					input = connection.getInputStream();
					File file = new File(
							Environment
									.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
							data.getFullTitle() + ".mp3");
					File directoryFile = Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
					if (!directoryFile.exists()) {
						directoryFile.mkdirs();
					}
					output = new FileOutputStream(file);

					byte data[] = new byte[4096];
					long total = 0;
					int count;
					while ((count = input.read(data)) != -1) {
						// allow canceling with back button
						if (isCancelled()) {
							Log.v("ProgressDialog", "Download is canceled");
							if (file.exists()) {
								file.delete();
							}
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
					} catch (IOException ignored) {
					}

					if (connection != null)
						connection.disconnect();
				}
			} finally {
				wl.release();
			}
			return null;
		}
	}

	public void setRealArtistTitleForPlayers() {
		setText(ppArtistTitleView, data.getPpArtist(), data.getPpTitle());
		setText(vkArtistTitleView, data.getVkArtist(), data.getVkTitle());
	}

	private void setText(TextView textView, String artist, String title) {
		if ((artist == null) || (title == null)) {
			textView.setText(data.getArtist() + " - " + data.getTitle());
		} else {
			textView.setText(artist + " - " + title);
		}
	}

	private class AddToVkTask extends AsyncTask<DatabaseSongData, Void, String> {

		private final Context context;

		public AddToVkTask(Context context) {
			this.context = context;
		}

		@Override
		protected String doInBackground(DatabaseSongData... databaseSongData) {
			String result = null;
			String audioId = databaseSongData[0].getVkAudioId();
			if (audioId == null) {
				try {
					databaseSongData[0].findVkAudio(vkApi);
				} catch (IOException e) {
					e.printStackTrace();
					result = e.getLocalizedMessage();
				} catch (JSONException e) {
					e.printStackTrace();
					result = e.getLocalizedMessage();
				} catch (KException e) {
					e.printStackTrace();
					result = context.getString(R.string.vk_not_available);
				} catch (SongNotFoundException e) {
					e.printStackTrace();
					result = context.getString(R.string.song_not_found);
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

		@Override
		protected void onPostExecute(String result) {
			setRealArtistTitleForPlayers();
			Toast.makeText(SongInfoFragment.this.getActivity(), result,
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_song_info_menu, menu);

		MenuItem shareItem = menu.findItem(R.id.action_share);
		shareActionProvider = (ShareActionProvider) MenuItemCompat
				.getActionProvider(shareItem);
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				getString(R.string.new_song_title));
		if (data.getAlbum() != null) {
			sharingIntent.putExtra(
					android.content.Intent.EXTRA_TEXT,
					getString(R.string.new_song_message) + " "
							+ data.getArtist() + " - " + data.getTitle() + " ("
							+ data.getAlbum() + ")");
		} else {
			sharingIntent.putExtra(
					android.content.Intent.EXTRA_TEXT,
					getString(R.string.new_song_message) + " "
							+ data.getArtist() + " - " + data.getTitle());
		}
		shareActionProvider.setShareIntent(sharingIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_delete:
			MessageDialogFragment.Builder builder = new MessageDialogFragment.Builder();
			builder.setIcon(R.drawable.ic_alert_dialog)
					.setTitle(getString(R.string.deletion_title))
					.setMessage(getString(R.string.deletion_message))
					.setPositiveButton(getString(R.string.yes),
							new MessageDialogFragment.onDialogClickListener() {
								@Override
								public void onDialogClick(
										DialogFragment fragment, View view) {
									fragment.dismiss();
									model.getSongList().remove(data);
									Intent intent = new Intent(
											SongInfoFragment.this.getActivity(),
											MicrophonePagerActivity.class);
									intent.putExtra(
											PagerActivity.PAGE_NUMBER,
											MicrophonePagerAdapter.HISTORY_PAGE_NUMBER);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);
								}
							})
					.setNegativeButton(getString(R.string.no),
							new MessageDialogFragment.onDialogClickListener() {
								@Override
								public void onDialogClick(
										DialogFragment fragment, View view) {
									fragment.dismiss();
								}
							});

			FragmentTransaction fragmentTransaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			Fragment prev = getActivity().getSupportFragmentManager()
					.findFragmentByTag(SHOW_DIALOG_TAG);
			if (prev != null) {
				fragmentTransaction.remove(prev);
			}

			MessageDialogFragment dialogFragment = ProgressDialogFragment
					.newInstance(builder);
			dialogFragment.show(fragmentTransaction, SHOW_DIALOG_TAG);
			return true;
		case R.id.settings:
			Log.v("Settings", "Creating settings activity");
			Intent intent = new Intent(SongInfoFragment.this.getActivity(),
					SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.refresh:
			Log.v("Settings", "Creating refresh activity");
			intent = new Intent(SongInfoFragment.this.getActivity(),
					RefreshPagerActivity.class);
			intent.putExtra("position", position);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void updatePlayerState() {
		ImageButton playPauseButton;
		if (model.getSongManager().getType() == SongManager.PP_SONG) {
			playPauseButton = ppPlayPauseButton;

			secPB.setVisibility(View.GONE);
			vkPlayPauseButton.setImageResource(R.drawable.ic_media_play);
			vkPlayPauseButton.setVisibility(View.VISIBLE);
			vkSongProgress.setProgress(0);
			vkSongProgress.setSecondaryProgress(0);
		} else {
			playPauseButton = vkPlayPauseButton;

			secPB.setVisibility(View.GONE);
			ppPlayPauseButton.setImageResource(R.drawable.ic_media_play);
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
				Log.v(TAG, "Song " + model.getSongManager().getArtist() + " - "
						+ model.getSongManager().getTitle() + " is loading");
			} else {
				progressBar.setVisibility(View.GONE);
				if (model.getSongManager().isPrepared()) {
					playPauseButton.setVisibility(View.VISIBLE);
					setRealArtistTitleForPlayers();
				} else {
					if (playPauseButton.getVisibility() == View.GONE)
						progressBar.setVisibility(View.INVISIBLE);
				}
				Log.v(TAG, "Song is not loading");
			}

			if (model.getSongManager().isPlaying()) {
				playPauseButton.setImageResource(R.drawable.ic_media_pause);
				Log.v(TAG, "Song " + model.getSongManager().getArtist() + " - "
						+ model.getSongManager().getTitle() + " is playing");
			} else {
				playPauseButton.setImageResource(R.drawable.ic_media_play);
				Log.v(TAG, "Song is on pause");
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
}
