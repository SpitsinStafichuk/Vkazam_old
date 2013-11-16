package com.git.programmerr47.testhflbjcrhjggkth.view.adapters;

import java.util.List;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.ISongListController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.ISongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.ISongManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObserver;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SongListAdapter extends BaseAdapter implements IPlayerStateObserver {

	private Activity activity;
	private LayoutInflater inflater;
	private int idItem;
	private ISongListController controller;
	private ISongManager songManager;
	private View currentListItemView;
	private MicroScrobblerModel model;
	
	public SongListAdapter(Activity activity, int idItem, ISongListController controller) {
		this.activity = activity;
		this.idItem = idItem;
		this.controller = controller;
		songManager = MicroScrobblerModel.getInstance().getSongManager();
		model = MicroScrobblerModel.getInstance();
		Log.v("Lists", "History adapter created");
		
		IPlayerStateObservable songManagerStateObservable = (IPlayerStateObservable) songManager;
		songManagerStateObservable.addObserver((IPlayerStateObserver)this);
		Log.v("SongPlayer", "IPlayerStateObserver was added");
		inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return model.getHistory().size();
	}

	@Override
	public Object getItem(int position) {
		return model.getHistory().get(position);
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
		Log.v("playPauseButton", "" + playPauseButton);
		playPauseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.v("playPauseButton", "songPlayPauseButton was clicked");
				
				if ((currentListItemView != null) && (currentListItemView != fView)) {
					ImageButton playPauseButton = (ImageButton) currentListItemView.findViewById(R.id.songPlayPauseButton);
					playPauseButton.setVisibility(View.VISIBLE);
					playPauseButton.setImageResource(android.R.drawable.ic_media_play);
					ProgressBar progressBar = (ProgressBar) currentListItemView.findViewById(R.id.songItemLoading);
					progressBar.setVisibility(View.GONE);
				    LinearLayout element = (LinearLayout) currentListItemView.findViewById(R.id.songHistoryItem);
				    element.setBackgroundResource(R.drawable.song_list_item_bg_default);
					Log.v("SongPlayer", "Song" + /*songManager.getArtist() + " - " + songManager.getTitle() +*/ "is closing");
				}
				
				currentListItemView = fView;
			    LinearLayout element = (LinearLayout) currentListItemView.findViewById(R.id.songHistoryItem);
			    element.setBackgroundResource(R.drawable.song_list_item_bg_pressed);
				controller.playPauseSong((ISongData) getItem(position));
			}
		});
		
		ISongData data = getSongData(position);
		((TextView) view.findViewById(R.id.songListItemArtist)).setText(data.getArtist());
		((TextView) view.findViewById(R.id.songListItemTitle)).setText(data.getTitle());
		((TextView) view.findViewById(R.id.songListItemDate)).setText(data.getDate());
		if ((songManager.getSongData() != null) && 
		    (songManager.getSongData().equals(data))) {
		    ((LinearLayout) view.findViewById(R.id.songHistoryItem)).setBackgroundResource(R.drawable.song_list_item_bg_pressed);
			updateListItem(view);
		} else {
		    ((LinearLayout) view.findViewById(R.id.songHistoryItem)).setBackgroundResource(R.drawable.song_list_item_bg_default);
		    ((ImageButton) view.findViewById(R.id.songPlayPauseButton)).setImageResource(android.R.drawable.ic_media_play);
		    ((ImageButton) view.findViewById(R.id.songPlayPauseButton)).setVisibility(View.VISIBLE);
		    ((ProgressBar) view.findViewById(R.id.songItemLoading)).setVisibility(View.GONE);
		}
		return view;
	}

	private ISongData getSongData(int position) {
		return ((ISongData) getItem(position));
	}

	@Override
	public void updatePlayerState() {
		//TODO
		//TODO
		//TODO ВОТ ЭТО ДЕЛАТЬ В UI ПОТОКЕ СРАЗУ
		//TODO
		//TODO
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Log.v("SongPlayer", "updatePlayerState");
				if (currentListItemView != null) {
					updateListItem(currentListItemView);
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

	private void updateListItem(View v) {
		ImageButton playPauseButton = (ImageButton) v.findViewById(R.id.songPlayPauseButton);
		ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.songItemLoading);
		
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
