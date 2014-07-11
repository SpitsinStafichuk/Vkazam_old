package com.github.spitsinstafichuk.vkazam.adapters;

import java.io.IOException;

import org.json.JSONException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.spitsinstafichuk.vkazam.R;
import com.github.spitsinstafichuk.vkazam.controllers.UrlController;
import com.github.spitsinstafichuk.vkazam.model.SongData;
import com.github.spitsinstafichuk.vkazam.model.database.DatabaseSongData;
import com.github.spitsinstafichuk.vkazam.model.pleer.api.KException;
import com.github.spitsinstafichuk.vkazam.vos.SongInfo;

/**
 * Common pattern adapter for every url-page
 * 
 * @author Michael Spitsin
 * @since 2014-02-07
 */
public abstract class URLListAdapter extends BindAdapter {

    protected SongInfo mSongInfo;

	protected LayoutInflater inflater;
	protected Context context;

	protected int itemLayout = R.layout.song_url_list_item;
	protected int moreItemsLayout = R.layout.more_url_list_item;
	protected int page = 1;

	protected ProgressBar newSongLoadingBar;

	protected boolean isFullList = false;
	protected boolean isAllSongWithOriginArtistShown = false;
	protected boolean isFirstClick = true;

	protected UrlController controller;
    protected Handler uiHandler;
    private OnPlayPauseButtonClickListener mListener;

	public URLListAdapter(final Context context, SongInfo songInfo) {
		this.context = context;
		controller = new UrlController(context);
        mSongInfo = songInfo;
		updateSongsList();
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        uiHandler = new Handler();
	}

	/**
	 * Send request to service and wait for answer, then add to list new songs.
	 * 
	 * @throws com.github.spitsinstafichuk.vkazam.model.pleer.api.KException
	 * @throws java.io.IOException
	 * @throws org.json.JSONException
	 * @throws com.perm.kate.api.KException
	 */
	protected abstract void addMoreUrls() throws KException, IOException,
			JSONException, com.perm.kate.api.KException;

	/**
	 * Get Artist by position of Audio in list
	 * 
	 * @param position
	 *            - position of song in list
	 * @return artist label
	 */
	protected abstract String getArtist(int position);

	/**
	 * Get Title by position of Audio in list
	 * 
	 * @param position
	 *            - position of song in list
	 * @return title label
	 */
	protected abstract String getTitle(int position);

	/**
	 * Get Duration by position of Audio in list
	 * 
	 * @param position
	 *            - position of song in list
	 * @return duration label
	 */
	protected abstract long getDuration(int position);

	/**
	 * Get Bitrate by position of Audio in list
	 * 
	 * @param position
	 *            - position of song in list
	 * @return bitrate label
	 */
	protected abstract String getBitrate(int position);

	/**
	 * Get specific click listener that determines by certain child
	 * 
	 * @param position
	 *            - position of view in list
	 * @return listener for view clicking
	 */
	protected abstract View.OnClickListener getOnViewClickListener(
			final int position);

	/**
	 * Check selected this song in list or not
	 * 
	 * @param position
	 *            - position of view in list
	 * @param data
	 *            - view information
	 * @return true if this song is selected, false - otherwise
	 */
	protected abstract boolean isGetViewEqualsCurrentSong(int position,
			SongData data);

	/**
	 * Generate songData card to send it in model and manage it state in player
	 * 
	 * @param position
	 *            - position of view in list
	 * @return SongData instance
	 */
	protected abstract SongData generateSongDataByViewInfo(int position);

	/**
	 * Get type of song
	 * 
	 * @return type of song (Vk or PP now)
	 */
	protected abstract int getSongType();

    public void setOnPlayPauseButtonClickListener(OnPlayPauseButtonClickListener listener) {
        mListener = listener;
    }

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (position < (getCount() - 1)) {
			ImageButton playPause = (ImageButton) view
					.findViewById(R.id.ppUrlListItemPlayPauseButton);
			final View viewFinal = view;
			playPause.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (isFirstClick) {
                        //TODO Release Player Service
						isFirstClick = false;
					}

					controller.setCurrentElement(viewFinal);
					controller.playPauseSong(new DatabaseSongData(position,
							null, generateSongDataByViewInfo(position)), -1,
							getSongType());
				}
			});

//			if (model.getSongManager().getSongData() != null) {
//				SongData data = model.getSongManager().getSongData();
//				if (data.getTrackId() == null) {
//					if (isGetViewEqualsCurrentSong(position, data)
//							&& (model.getSongManager().isPlaying())) {
//						playPause.setImageResource(R.drawable.ic_media_pause);
//					} else {
//						playPause.setImageResource(R.drawable.ic_media_play);
//					}
//				} else {
//					playPause.setImageResource(R.drawable.ic_media_play);
//				}
//			} else {
//				playPause.setImageResource(R.drawable.ic_media_play);
//			}
		}

		return view;
	}

    @Override
    protected View newView(int position, ViewGroup parent) {
        View resultView;

        if (position < (getCount() - 1)) {
            resultView = inflater.inflate(itemLayout, parent, false);

            if (resultView != null) {
                TextView artistView = (TextView) resultView.findViewById(R.id.urlArtist);
                TextView titleView = (TextView) resultView.findViewById(R.id.urlTitle);
                TextView durationView = (TextView) resultView.findViewById(R.id.urlDuration);
                TextView bitRateView = (TextView) resultView.findViewById(R.id.urlBitRate);
                RadioButton checkerView = (RadioButton) resultView.findViewById(R.id.urlChecker);
                ImageButton playPauseButton = (ImageButton) resultView.findViewById(R.id.urlPlayPauseButton);

                resultView.setTag(R.id.urlArtist, artistView);
                resultView.setTag(R.id.urlTitle, titleView);
                resultView.setTag(R.id.urlDuration, durationView);
                resultView.setTag(R.id.urlBitRate, bitRateView);
                resultView.setTag(R.id.urlChecker, checkerView);
                resultView.setTag(R.id.urlPlayPauseButton, playPauseButton);
            }
        } else {
            resultView = inflater.inflate(moreItemsLayout, parent, false);

            newSongLoadingBar = (ProgressBar) resultView
                    .findViewById(R.id.moreListItemLoading);
            if (isFullList) {
                resultView.setVisibility(View.GONE);
            }

            resultView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    page++;
                    updateSongsList();
                }
            });
        }

        return resultView;
    }

    @Override
    protected void bindView(final int position, View view, ViewGroup parent) {
        if (position < (getCount() - 1)) {
            TextView artistView = (TextView) view.getTag(R.id.urlArtist);
            if ((getArtist(position) != null) && (artistView != null)) {
                artistView.setText(getArtist(position));
            }

            TextView titleView = (TextView) view.getTag(R.id.urlTitle);
            if ((getTitle(position) != null) && (titleView != null)) {
                titleView.setText(getTitle(position));
            }

            TextView durationView = (TextView) view.getTag(R.id.urlDuration);
            if ((durationView != null)) {
                durationView.setText(getDuration(position) + "");
            }

            TextView bitRateView = (TextView) view.getTag(R.id.urlBitRate);
            if ((getBitrate(position) != null) && (bitRateView != null)) {
                bitRateView.setText(getArtist(position));
            }

            RadioButton checkerView = (RadioButton) view.getTag(R.id.urlChecker);
            if (checkerView != null) {
                checkerView.setOnClickListener(getOnViewClickListener(position));
            }

//			if (isGetViewEqualsCurrentSong(position, currentSongData)) {
//				radioButton.setChecked(true);
//			} else {
//				radioButton.setChecked(false);
//			}
            final ImageButton playPauseButton = (ImageButton) view.getTag(R.id.urlPlayPauseButton);
            if ((playPauseButton != null)) {
                playPauseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onPlayPauseButtonClick(playPauseButton, position);
                        }
                    }
                });
            }
//            final View viewFinal = view;
//            playPause.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isFirstClick) {
//                        //TODO Release Player Service
//                        isFirstClick = false;
//                    }
//
//                    controller.setCurrentElement(viewFinal);
//                    controller.playPauseSong(new DatabaseSongData(position,
//                                    null, generateSongDataByViewInfo(position)), -1,
//                            getSongType());
//                }
//            });
        } else {
//            resultView = inflater.inflate(moreItemsLayout, parent, false);
//
//            newSongLoadingBar = (ProgressBar) resultView
//                    .findViewById(R.id.moreListItemLoading);
//            if (isFullList) {
//                resultView.setVisibility(View.GONE);
//            }
//
//            resultView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    page++;
//                    updateSongsList();
//                }
//            });
        }
    }

    protected synchronized void updateSongsList() {
		new Thread(new Runnable() {
			@Override
			public void run() {

                uiHandler.post(new Runnable() {
					@Override
					public void run() {
						newSongLoadingBar.setVisibility(View.VISIBLE);
					}
				});
				try {
					int listUpdate = getCount() - 1;
					if (!isAllSongWithOriginArtistShown) {
						addMoreUrls();
					} else {
						if ((!mSongInfo.getGracenoteSongInfo().getArtist().equals(
                                mSongInfo.getGracenoteSongInfo().getAlbumArtist()))
								&& (mSongInfo.getGracenoteSongInfo().getAlbumArtist() != null)) {
							addMoreUrls();
						}
					}
					Log.v("PleerListAdapter", "ANSWER FROM INTERNET");
					final int listUpdateFinal = getCount() - 1 - listUpdate;
                    uiHandler.post(new Runnable() {
						@Override
						public void run() {
							Log.v("PleerListAdapter", "" + listUpdateFinal);
							if (listUpdateFinal <= 0) {
								if (!isAllSongWithOriginArtistShown) {
									isAllSongWithOriginArtistShown = true;
								} else {
									isFullList = true;
									Toast.makeText(
											context,
											context.getString(R.string.no_more_songs),
											Toast.LENGTH_SHORT).show();
									Log.v("PleerListAdapter", "No more songs");
								}
							}
							notifyDataSetChanged();
						}
					});
				} catch (IOException e) {
					e.printStackTrace(); // To change body of catch statement
											// use File | Settings | File
											// Templates.
				} catch (JSONException e) {
					e.printStackTrace(); // To change body of catch statement
											// use File | Settings | File
											// Templates.
				} catch (KException e) {
					e.printStackTrace(); // To change body of catch statement
											// use File | Settings | File
											// Templates.
				} catch (com.perm.kate.api.KException e) {
					e.printStackTrace(); // To change body of catch statement
											// use File | Settings | File
											// Templates.
				} finally {
					uiHandler.post(new Runnable() {
						@Override
						public void run() {
							newSongLoadingBar.setVisibility(View.GONE);
						}
					});
				}
			}
		}).start();
	}

	protected String getStringTime(long time) {
		String result = "" + time / 60 + ":";
		long seconds = time % 60;
		if (seconds < 10) {
			result += "0";
		}
		return result + seconds;
	}

	public void finish() {
		if (!isFirstClick) {
            //TODO Release PlayerService
		}
	}

    /**
     * Listener on clicking on playPause button on each item.
     */
    public static interface OnPlayPauseButtonClickListener {

        /**
         * Calls when play/pause button on any item is clicked.
         *
         * @param button - view of clicked button
         * @param position - position of list item, the button of which is clicked
         */
        void onPlayPauseButtonClick(ImageButton button, int position);
    }
}
