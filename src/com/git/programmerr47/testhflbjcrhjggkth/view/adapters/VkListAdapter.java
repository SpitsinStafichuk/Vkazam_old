package com.git.programmerr47.testhflbjcrhjggkth.view.adapters;

import android.app.Activity;
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
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.perm.kate.api.Api;
import com.perm.kate.api.Audio;
import com.perm.kate.api.KException;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VkListAdapter extends BaseAdapter {
	private static String TAG = "VkListAdapter";

    private MicroScrobblerModel model;
    private DatabaseSongData currentSongData;
    private View currentElement;
    private List<Audio> audios;
    private LayoutInflater inflater;
    private FragmentActivity activity;
    private int resLayout;
    private int endOfListResLayout;
    private int page = 1;
    private ProgressBar newSongLoadingBar;
    private boolean isFullList = false;
    private URLcontroller controller;
    private Api vkApi;

    public VkListAdapter(final FragmentActivity activity, int resLayout, int endOfListResLayout) {
        this.activity = activity;
        this.resLayout = resLayout;
        this.endOfListResLayout = endOfListResLayout;
        controller = new URLcontroller(activity);
        model = RecognizeServiceConnection.getModel();
        vkApi = model.getVkApi();
        currentSongData = model.getCurrentOpenSong();
        //Log.v(TAG, "Current Song url = " + currentSongData.getPleercomUrl());
        audios = new ArrayList<Audio>();
        updateSongsList();
        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return audios.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position < audios.size())
            return audios.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        Log.v(TAG, position + " - " + audios.size());
        if (position < audios.size()) {
            view = inflater.inflate(resLayout, parent, false);

            TextView textView;

            textView = (TextView) view.findViewById(R.id.ppUrlListItemArtist);
            textView.setText(audios.get(position).artist);

            textView = (TextView) view.findViewById(R.id.ppUrlListItemTitle);
            textView.setText(audios.get(position).title);

            textView = (TextView) view.findViewById(R.id.ppUrlListItemDuration);
            textView.setText(getStringTime(audios.get(position).duration));

            textView = (TextView) view.findViewById(R.id.ppUrlListItemBitRate);

            LinearLayout info = (LinearLayout) view.findViewById(R.id.ppUrlListItemInfo);
            LinearLayout numbers = (LinearLayout) view.findViewById(R.id.ppUrlListItemNumbers);
            RadioButton radioButton = (RadioButton) view.findViewById(R.id.ppUrlListItemCheckbutton);
            
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                	Audio audio = audios.get(position);
                	String audioId = audio.owner_id + "_" + audio.aid;
                	long lyricsId = -1;
                	if (audio.lyrics_id != null) {
                		lyricsId = audio.lyrics_id;
                	}
                    if (currentSongData.getVkAudioId() == null || !currentSongData.getVkAudioId().equals(audioId)) {
                        currentSongData.setVkAudioId(audioId);
                        currentSongData.setLyricsId(lyricsId);
                        VkListAdapter.this.notifyDataSetChanged();
                    }
                }
            };
            info.setOnClickListener(listener);
            numbers.setOnClickListener(listener);
            radioButton.setOnClickListener(listener);
            
            String audioId = audios.get(position).owner_id + "_" + audios.get(position).aid;
            if ((currentSongData.getVkAudioId() != null) && (currentSongData.getVkAudioId().equals(audioId))) {
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

        if (position < audios.size()) {
            ImageButton playPause = (ImageButton) view.findViewById(R.id.ppUrlListItemPlayPauseButton);
            final View viewFinal = view;
            playPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	Log.v(TAG, "View = " + v);
                    controller.setCurrentElement(viewFinal);
                    //TODO change
                    //controller.playPauseSong(audios.get(position).url, activity);
                }
            });
        }

        return view;
    }

    private synchronized void updateSongsList() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        VkListAdapter.this.newSongLoadingBar.setVisibility(View.VISIBLE);
                    }
                });
                try {
                    int listUpdate = audios.size();
                    audios.addAll(vkApi.searchAudio(currentSongData.getArtist() + " " + currentSongData.getTitle(), "2", "0", 5l, (page - 1) * 5l, null, null));
                    Log.v(TAG, "ANSWER FROM INTERNET");
                    final int listUpdateFinal = audios.size() - listUpdate;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v(TAG, "" + listUpdateFinal);
                            if (listUpdateFinal <= 0) {
                                isFullList = true;
                                Toast.makeText(activity, activity.getString(R.string.no_more_songs), Toast.LENGTH_SHORT).show();
                                Log.v(TAG, "No more songs");
                            }
                            VkListAdapter.this.notifyDataSetChanged();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (JSONException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (KException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } finally {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            VkListAdapter.this.newSongLoadingBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }).start();
    }

    private String getStringTime(long time) {
        String result = "" + time / 60 + ":";
        long seconds = time % 60;
        if (seconds < 10) {
            result += "0";
        }
        return result + seconds;
    }

    //TODO finalize
}
