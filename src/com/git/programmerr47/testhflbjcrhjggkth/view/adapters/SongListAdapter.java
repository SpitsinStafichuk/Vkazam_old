package com.git.programmerr47.testhflbjcrhjggkth.view.adapters;

import java.util.List;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.ISongListController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.ISongManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObserver;
import com.google.sydym6.logic.database.data.ISongData;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SongListAdapter extends BaseAdapter implements IPlayerStateObserver {

	private Activity activity;
	private LayoutInflater inflater;
	private List<ISongData> songs;
	private int idItem;
	private ISongListController controller;
	private ISongManager songManager;
	private View currentListItemView;
	
	public SongListAdapter(Activity activity, int idItem, List<ISongData> songs, ISongListController controller) {
		this.activity = activity;
		this.idItem = idItem;
		this.songs = songs;
		this.controller = controller;
		songManager = MicroScrobblerModel.getInstance().getSongManager();
		
		Log.v("Lists", "History adapter created");
		
		IPlayerStateObservable songManagerStateObservable = (IPlayerStateObservable) songManager;
		songManagerStateObservable.addObserver((IPlayerStateObserver)this);
		Log.v("SongPlayer", "IPlayerStateObserver was added");
		inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return songs.size();
	}

	@Override
	public Object getItem(int position) {
		return songs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(idItem, parent, false);
		}
		
		final View fView = view;
		
		ImageButton playPauseButton = (ImageButton) view.findViewById(R.id.songPlayPauseButton);
		playPauseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.v("playPauseButton", "songPlayPauseButton was clicked");
				
				if (currentListItemView != null) {
					ImageButton playPauseButton = (ImageButton) currentListItemView.findViewById(R.id.songPlayPauseButton);
					playPauseButton.setVisibility(View.VISIBLE);
					playPauseButton.setImageResource(android.R.drawable.ic_media_play);
					ProgressBar progressBar = (ProgressBar) currentListItemView.findViewById(R.id.songItemLoading);
					progressBar.setVisibility(View.GONE);
					Log.v("SongPlayer", "Song" + /*songManager.getArtist() + " - " + songManager.getTitle() +*/ "is closing");
				}
				
				currentListItemView = fView;
				controller.playPauseSong((ISongData) getItem(position));
				//fView.setSelected(true);
			}
		});
		
		ImageButton deleteButton = (ImageButton) view.findViewById(R.id.songDeleteButton);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				controller.deleteSong((ISongData) getItem(position));
				//fView.setSelected(true);
			}
		});
		
		ISongData data = getSongData(position);
		((TextView) view.findViewById(R.id.songListItemArtistTitle)).setText(data.getArtist() + " - " + data.getTitle());
		((TextView) view.findViewById(R.id.songListItemDate)).setText(data.getDate());
		return view;
	}

	private ISongData getSongData(int position) {
		return ((ISongData) getItem(position));
	}

	@Override
	public void updatePlayerState() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (currentListItemView != null) {
					ImageButton playPauseButton = (ImageButton) currentListItemView.findViewById(R.id.songPlayPauseButton);
					ProgressBar progressBar = (ProgressBar) currentListItemView.findViewById(R.id.songItemLoading);;
					
					if (songManager.isLoading()) {
						progressBar.setVisibility(View.VISIBLE);
						playPauseButton.setVisibility(View.GONE);
						Log.v("SongPlayer", "Song" + songManager.getArtist() + " - " + songManager.getTitle() + "is loading");
					} else {
						progressBar.setVisibility(View.GONE);
						if (songManager.isPrepared()) {
							playPauseButton.setVisibility(View.VISIBLE);
						} else {
							if (playPauseButton.getVisibility() == View.GONE)
								progressBar.setVisibility(View.INVISIBLE);
						}
						Log.v("SongPlayer", "Song" + /*songManager.getArtist() + " - " + songManager.getTitle() +*/ "is not loading");
					}
					
					if (songManager.isPlaying()) {
						playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
						Log.v("SongPlayer", "Song" + songManager.getArtist() + " - " + songManager.getTitle() + "is playing");
					} else {
						playPauseButton.setImageResource(android.R.drawable.ic_media_play);
						Log.v("SongPlayer", "Song" + /*songManager.getArtist() + " - " + songManager.getTitle() +*/ "is on pause");
					}
				}
			}
			
		});
	}
	
	@Override
	protected void finalize() {
		Log.v("SongPlayer", "SongListAdapter was destoyed");
		release();
		try {
			super.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void release() {
		IPlayerStateObservable songManagerStateObservable = (IPlayerStateObservable) songManager;
		songManagerStateObservable.removeObserver((IPlayerStateObserver)this);
		Log.v("SongPlayer", "IPlayerStateObserver was removed");
	}

}
