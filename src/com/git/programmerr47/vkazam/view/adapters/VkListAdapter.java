package com.git.programmerr47.vkazam.view.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.git.programmerr47.vkazam.model.SongData;
import com.git.programmerr47.vkazam.model.managers.SongManager;
import com.perm.kate.api.Api;
import com.perm.kate.api.Audio;
import com.perm.kate.api.KException;

public class VkListAdapter extends URLlistAdapter {
	private final List<Audio> audios;
	private final Api vkApi;

	public VkListAdapter(final FragmentActivity activity, int position,
			int resLayout, int endOfListResLayout) {
		super(activity, position, resLayout, endOfListResLayout);
		vkApi = model.getVkApi();
		audios = new ArrayList<Audio>();
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
	protected void addMoreUrls() throws KException, IOException, JSONException {
		audios.addAll(vkApi.searchAudio(currentSongData.getArtist() + " "
				+ currentSongData.getTitle(), "2", "0", 5l, (page - 1) * 5l,
				null, null));
	}

	@Override
	protected String getArtist(int position) {
		return audios.get(position).artist;
	}

	@Override
	protected String getTitle(int position) {
		return audios.get(position).title;
	}

	@Override
	protected long getDuration(int position) {
		return audios.get(position).duration;
	}

	@Override
	protected String getBitrate(int position) {
		return null;
	}

	@Override
	protected View.OnClickListener getOnViewClickListener(final int position) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Audio audio = audios.get(position);
				String audioId = audio.owner_id + "_" + audio.aid;
				long lyricsId = -1;
				if (audio.lyrics_id != null) {
					lyricsId = audio.lyrics_id;
				}
				if (currentSongData.getVkAudioId() == null
						|| !currentSongData.getVkAudioId().equals(audioId)) {
					currentSongData.setVkAudioId(audioId);
					currentSongData.setLyricsId(lyricsId);
					notifyDataSetChanged();
				}
			}
		};
	}

	@Override
	protected boolean isGetViewEqualsCurrentSong(int position, SongData data) {
		return (data.getVkAudioId() != null)
				&& (data.getVkAudioId().equals(audios.get(position).owner_id
						+ "_" + audios.get(position).aid));
	}

	@Override
	protected SongData generateSongDataByViewInfo(int position) {
		SongData tempInfo = new SongData(null, audios.get(position).artist,
				null, audios.get(position).title, null);
		tempInfo.setVkAudioId(audios.get(position).owner_id + "_"
				+ audios.get(position).aid);
		return tempInfo;
	}

	@Override
	protected int getSongType() {
		return SongManager.VK_SONG;
	}
}
