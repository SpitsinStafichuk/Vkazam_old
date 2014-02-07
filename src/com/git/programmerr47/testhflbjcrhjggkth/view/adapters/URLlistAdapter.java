package com.git.programmerr47.testhflbjcrhjggkth.view.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.URLcontroller;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.KException;
import org.json.JSONException;

import java.io.IOException;

/**
 * Common pattern adapter for every url-page
 * @author Michael Spitsin
 * @since 2014-02-07
 */
public abstract class URLlistAdapter extends BaseAdapter {

    protected MicroScrobblerModel model;
    protected DatabaseSongData currentSongData;

    protected LayoutInflater inflater;
    protected FragmentActivity activity;

    protected int resLayout;
    protected int endOfListResLayout;
    protected int page = 1;

    protected ProgressBar newSongLoadingBar;

    protected boolean isFullList = false;
    protected boolean isAllSongWithOriginArtistShown = false;
    protected boolean isFirstClick = true;

    protected URLcontroller controller;

    public URLlistAdapter(final FragmentActivity activity, int resLayout, int endOfListResLayout) {
        this.activity = activity;
        this.resLayout = resLayout;
        this.endOfListResLayout = endOfListResLayout;
        controller = new URLcontroller(activity);
        model = RecognizeServiceConnection.getModel();
        currentSongData = model.getCurrentOpenSong();
        updateSongsList();
        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Send request to service and wait for answer, then add to list new songs.
     *
     * @throws KException
     * @throws IOException
     * @throws JSONException
     * @throws com.perm.kate.api.KException
     */
    protected abstract void addMoreUrls() throws KException, IOException, JSONException, com.perm.kate.api.KException;

    /**
     * Get Artist by position of Audio in list
     *
     * @param position - position of song in list
     * @return artist label
     */
    protected abstract String getArtist(int position);

    /**
     * Get Title by position of Audio in list
     *
     * @param position - position of song in list
     * @return title label
     */
    protected abstract String getTitle(int position);

    /**
     * Get Duration by position of Audio in list
     *
     * @param position - position of song in list
     * @return duration label
     */
    protected abstract long getDuration(int position);

    /**
     * Get Bitrate by position of Audio in list
     *
     * @param position - position of song in list
     * @return bitrate label
     */
    protected abstract String getBitrate(int position);

    /**
     * Get specific click listener that determines by certain child
     *
     * @param position - position of view in list
     * @return listener for view clicking
     */
    protected abstract View.OnClickListener getOnViewClickListener(final int position);

    /**
     * Check selected this song in list or not
     *
     * @param position - position of view in list
     * @param data - view information
     * @return true if this song is selected, false - otherwise
     */
    protected abstract boolean isGetViewEqualsCurrentSong(int position, SongData data);

    /**
     * Generate songData card to send it in model and manage it state in player
     *
     * @param position - position of view in list
     * @return SongData instance
     */
    protected abstract SongData generateSongDataByViewInfo(int position);

    /**
     * Get type of song
     *
     * @return type of song (Vk or PP now)
     */
    protected abstract int getSongType();

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (position < (getCount() - 1)) {
            view = inflater.inflate(resLayout, parent, false);

            TextView textView;

            textView = (TextView) view.findViewById(R.id.ppUrlListItemArtist);
            textView.setText(getArtist(position));

            textView = (TextView) view.findViewById(R.id.ppUrlListItemTitle);
            textView.setText(getTitle(position));

            textView = (TextView) view.findViewById(R.id.ppUrlListItemDuration);
            textView.setText(getStringTime(getDuration(position)));

            if (getBitrate(position) != null) {
                textView = (TextView) view.findViewById(R.id.ppUrlListItemBitRate);
                textView.setText(getBitrate(position));
            }

            LinearLayout info = (LinearLayout) view.findViewById(R.id.ppUrlListItemInfo);
            LinearLayout numbers = (LinearLayout) view.findViewById(R.id.ppUrlListItemNumbers);
            RadioButton radioButton = (RadioButton) view.findViewById(R.id.ppUrlListItemCheckbutton);

            View.OnClickListener listener = getOnViewClickListener(position);
            info.setOnClickListener(listener);
            numbers.setOnClickListener(listener);
            radioButton.setOnClickListener(listener);

            if (isGetViewEqualsCurrentSong(position, currentSongData)) {
                radioButton.setChecked(true);
            } else {
                radioButton.setChecked(false);
            }
        } else {
            view = inflater.inflate(endOfListResLayout, parent, false);

            newSongLoadingBar = (ProgressBar) view.findViewById(R.id.moreListItemLoading);
            if (isFullList) {
                view.setVisibility(View.GONE);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    page++;
                    updateSongsList();
                }
            });
        }

        if (position < (getCount() - 1)) {
            ImageButton playPause = (ImageButton) view.findViewById(R.id.ppUrlListItemPlayPauseButton);
            final View viewFinal = view;
            playPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFirstClick) {
                        model.getSongManager().release();
                        isFirstClick = false;
                    }

                    controller.setCurrentElement(viewFinal);
                    controller.playPauseSong(new DatabaseSongData(position, null, generateSongDataByViewInfo(position)), -1, getSongType());
                }
            });

            if (model.getSongManager().getSongData() != null) {
                SongData data = model.getSongManager().getSongData();
                if (data.getTrackId() == null) {
                    if (isGetViewEqualsCurrentSong(position, data) &&
                            (model.getSongManager().isPlaying())) {
                        playPause.setImageResource(R.drawable.ic_media_pause);
                    } else {
                        playPause.setImageResource(R.drawable.ic_media_play);
                    }
                } else {
                    playPause.setImageResource(R.drawable.ic_media_play);
                }
            } else {
                playPause.setImageResource(R.drawable.ic_media_play);
            }
        }

        return view;
    }

    protected synchronized void updateSongsList() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                activity.runOnUiThread(new Runnable() {
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
                        if ((!currentSongData.getArtist().equals(currentSongData.getAlbumArtist())) &&
                                (currentSongData.getAlbumArtist() != null)) {
                            addMoreUrls();
                        }
                    }
                    Log.v("PleerListAdapter", "ANSWER FROM INTERNET");
                    final int listUpdateFinal = getCount() - 1 - listUpdate;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("PleerListAdapter", "" + listUpdateFinal);
                            if (listUpdateFinal <= 0) {
                                if (!isAllSongWithOriginArtistShown) {
                                    isAllSongWithOriginArtistShown = true;
                                } else {
                                    isFullList = true;
                                    Toast.makeText(activity, activity.getString(R.string.no_more_songs), Toast.LENGTH_SHORT).show();
                                    Log.v("PleerListAdapter", "No more songs");
                                }
                            }
                            notifyDataSetChanged();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (JSONException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (KException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (com.perm.kate.api.KException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } finally {
                    activity.runOnUiThread(new Runnable() {
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
            model.getSongManager().release();
            model.getSongManager().set(null, -1, model.getVkApi());
        }
    }
}
