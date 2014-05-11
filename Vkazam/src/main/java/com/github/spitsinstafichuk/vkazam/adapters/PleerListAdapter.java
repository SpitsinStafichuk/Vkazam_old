package com.github.spitsinstafichuk.vkazam.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.github.spitsinstafichuk.vkazam.model.SongData;
import com.github.spitsinstafichuk.vkazam.model.managers.SongManager;
import com.github.spitsinstafichuk.vkazam.model.pleer.api.Api;
import com.github.spitsinstafichuk.vkazam.model.pleer.api.Audio;
import com.github.spitsinstafichuk.vkazam.model.pleer.api.KException;

public class PleerListAdapter extends URLlistAdapter {

    private final List<Audio> urls;

    public PleerListAdapter(final FragmentActivity activity, int position,
            int resLayout, int endOfListResLayout) {
        super(activity, position, resLayout, endOfListResLayout);
        urls = new ArrayList<Audio>();
    }

    @Override
    public int getCount() {
        return urls.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position < urls.size()) {
            return urls.get(position);
        } else {
            return null;
        }
    }

    @Override
    protected void addMoreUrls() throws KException, IOException, JSONException {
        urls.addAll(Api.searchAudio(currentSongData.getArtist() + " "
                + currentSongData.getTitle(), 5, page));
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
                if ((currentSongData.getPleercomUrl() == null)
                        || (!currentSongData.getPleercomUrl().equals(
                        urls.get(position).url))) {
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
        return (data.getPleercomUrl() != null)
                && (data.getPleercomUrl().equals(urls.get(position).url));
    }

    @Override
    protected SongData generateSongDataByViewInfo(int position) {
        SongData tempInfo = new SongData(null, urls.get(position).artist, null,
                urls.get(position).title, null);
        tempInfo.setPleercomUrl(urls.get(position).url);
        return tempInfo;
    }

    @Override
    protected int getSongType() {
        return SongManager.PP_SONG;
    }
}
