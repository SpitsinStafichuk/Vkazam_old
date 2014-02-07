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
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SongManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.Api;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.Audio;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.KException;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class PleerListAdapter extends URLlistAdapter {
    private List<Audio> urls;

    public PleerListAdapter(final FragmentActivity activity, int resLayout, int endOfListResLayout) {
        super(activity, resLayout, endOfListResLayout);
        urls = new ArrayList<Audio>();
    }

    @Override
    public int getCount() {
        return urls.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position < urls.size())
            return urls.get(position);
        else
            return null;
    }

    @Override
    protected void addMoreUrls() throws KException, IOException, JSONException {
        urls.addAll(Api.searchAudio(currentSongData.getArtist() + " " + currentSongData.getTitle(), 5, page));
    }

    @Override
    protected String getArtist(int position) {
        return urls.get(position).artist;
    }

    @Override
    protected String getTitle(int position) {
        return urls.get(position).title;
    }

    @Override
    protected long getDuration(int position) {
        return urls.get(position).duration;
    }

    @Override
    protected String getBitrate(int position) {
        return urls.get(position).bitrate;
    }

    @Override
    protected View.OnClickListener getOnViewClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((currentSongData.getPleercomUrl() == null) || (!currentSongData.getPleercomUrl().equals(urls.get(position).url))) {
                    currentSongData.setPleercomUrl(urls.get(position).url);
                    currentSongData.setPpArtist(urls.get(position).artist);
                    currentSongData.setPpTitle(urls.get(position).title);
                    PleerListAdapter.this.notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    protected boolean isGetViewEqualsCurrentSong(int position, SongData data) {
        return (data.getPleercomUrl() != null) &&
                (data.getPleercomUrl().equals(urls.get(position).url));
    }

    @Override
    protected SongData generateSongDataByViewInfo(int position) {
        SongData tempInfo = new SongData(null, urls.get(position).artist, null, urls.get(position).title, null);
        tempInfo.setPleercomUrl(urls.get(position).url);
        return tempInfo;
    }
}
