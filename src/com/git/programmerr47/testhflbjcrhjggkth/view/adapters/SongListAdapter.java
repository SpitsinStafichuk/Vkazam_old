package com.git.programmerr47.testhflbjcrhjggkth.view.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SongListController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.Data;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SearchManager.SearchListener;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SongManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IPlayerStateObserver;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SongListAdapter extends BaseAdapter implements IPlayerStateObserver {
	private static final String TAG = "SongListAdapter";
	
	private Activity activity;
	private LayoutInflater inflater;
	private MicroScrobblerModel model;
    private boolean isScrolling = false;
	private int lastPosition;
	private int idItem;
	private SongListController controller;
	private SongManager songManager;
	private View currentListItemView;
    private int currentListItemPosition = -1;
	private Map<String, ImageView> coverArts;
	
	public SongListAdapter(Activity activity, int idItem, SongListController controller) {
		this.activity = activity;
		this.idItem = idItem;
		this.controller = controller;
		model = RecognizeServiceConnection.getModel();
		songManager = model.getSongManager();
		coverArts = new HashMap<String, ImageView>();
		Log.v("Lists", "History adapter created");
		
		//IPlayerStateObservable songManagerStateObservable = (IPlayerStateObservable) songManager;
		//songManagerStateObservable.addObserver((IPlayerStateObserver)this);
        model.getPlayer().addPlayerStateObserver(this);
		Log.v("SongPlayer", "IPlayerStateObserver was added");
		inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	//TODO переписать!!! данная реализация неэффективна
	public List<DatabaseSongData> getSongDataFromHistoryById(String trackId) {
		List<DatabaseSongData> result = new ArrayList<DatabaseSongData>();
		List<Data> history = model.getSongList();
		for(Data i : history) {
			if(((DatabaseSongData)i).getTrackId().equals(trackId)) {
				result.add((DatabaseSongData) i);
			}
		}
		return result;
	}
	
	@Override
	public int getCount() {
		return model.getSongList().size();
	}

	@Override
	public Object getItem(int position) {
		return model.getSongList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

    @Override
    public void notifyDataSetChanged() {
        isScrolling = false;
        super.notifyDataSetChanged();
    }

    public void scrolling() {
        isScrolling = true;
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
				    LinearLayout element = (LinearLayout) currentListItemView.findViewById(R.id.songHistoryItemInfo);
				    element.setBackgroundResource(R.drawable.list_item_bg_default);
					Log.v("SongPlayer", "Song" + /*songManager.getArtist() + " - " + songManager.getTitle() +*/ "is closing");
				}
				
				currentListItemView = fView;
                currentListItemPosition = position;
			    LinearLayout element = (LinearLayout) currentListItemView.findViewById(R.id.songHistoryItemInfo);
			    element.setBackgroundResource(R.drawable.list_item_bg_pressed);
				controller.playPauseSong((DatabaseSongData) getItem(position), position);
			}
		});
		
		ViewHelper.setAlpha(view.findViewById(R.id.songListItemPlayPauseLayout), 0.75f);
		DatabaseSongData songData = getSongData(position);
		Log.v("SongInformation", "in trackId: " + songData.getTrackId());
		coverArts.put(songData.getTrackId(), (ImageView) view.findViewById(R.id.songListItemCoverArt));
		if(songData.getCoverArtUrl() == null) {
			Log.i(TAG, "songData before search: " + songData.toString());
			model.getSearchManager().search(songData.getTrackId(), new SearchListener() {
				
				@Override
				public void onSearchStatusChanged(String status) {
				}
				
				@Override
				public void onSearchResult(SongData songData) {
					if (songData != null) {
						List<DatabaseSongData> historySongData = getSongDataFromHistoryById(songData.getTrackId());
						if(historySongData != null) {
							for (DatabaseSongData i : historySongData) {
								if (i != null) i.setNullFields(songData);
								displayCoverArt(i);
							}
						} else {
							Log.i(TAG, "historySongData: null");
						}
					}
				}
			});
		} else {
			displayCoverArt(songData);
		}
		((TextView) view.findViewById(R.id.songListItemArtist)).setText(songData.getArtist());
		((TextView) view.findViewById(R.id.songListItemTitle)).setText(songData.getTitle());
		((TextView) view.findViewById(R.id.songListItemDate)).setText(songData.getDate().toString());
		if ((songManager.getSongData() != null) && 
		    (songManager.getSongData().equals(songData))) {
		    ((LinearLayout) view.findViewById(R.id.songHistoryItemInfo)).setBackgroundResource(R.drawable.list_item_bg_pressed);
			updateListItem(view);
		} else {
		    ((LinearLayout) view.findViewById(R.id.songHistoryItemInfo)).setBackgroundResource(R.drawable.list_item_bg_default);
		    ((ImageButton) view.findViewById(R.id.songPlayPauseButton)).setImageResource(android.R.drawable.ic_media_play);
		    ((ImageButton) view.findViewById(R.id.songPlayPauseButton)).setVisibility(View.VISIBLE);
		    ((ProgressBar) view.findViewById(R.id.songItemLoading)).setVisibility(View.GONE);
		}

        if (isScrolling) {
            if (position > lastPosition) {
                view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.list_view_up_down));
            } else {
                view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.list_view_down_up));
            }
        }
		lastPosition = position;
		
		return view;
	}

	private DatabaseSongData getSongData(int position) {
		return ((DatabaseSongData) getItem(position));
	}

	@Override
	public void updatePlayerState() {
		Log.v("SongPlayer", "updatePlayerState");
		if (currentListItemView != null) {
			updateListItem(currentListItemView);
		}
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

    public void resetCurrentElement() {
        currentListItemView = null;
    }
	
	public void release() {
        model.getPlayer().removePlayerStateObserver(this);
		Log.v("SongPlayer", "IPlayerStateObserver was removed");
	}

	private void updateListItem(View v) {
        if (songManager.getPositionInList() != -1) {
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
	
	private void displayCoverArt(DatabaseSongData songData) {
		String coverArtUrl = songData.getCoverArtUrl();
		Log.v(TAG, "CoverArtUrl: " + coverArtUrl);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.no_cover_art)
			.showImageOnFail(R.drawable.no_cover_art)
			.cacheOnDisc(true)
			.cacheInMemory(true)
			.build();
		model.getImageLoader().displayImage(coverArtUrl, coverArts.get(songData.getTrackId()), options);
	}

    public int getCurrentListPosition() {
        return currentListItemPosition;
    }
}
