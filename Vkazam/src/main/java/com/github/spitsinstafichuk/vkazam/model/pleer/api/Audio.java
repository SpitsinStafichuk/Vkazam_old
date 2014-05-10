package com.github.spitsinstafichuk.vkazam.model.pleer.api;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class Audio implements Serializable {
    private static final long serialVersionUID = 1L;
    public String id;
    public String artist;
    public String title;
    public long duration;
    public String url;
    public long size;
    public String bitrate;

    public static Audio parse(JSONObject o) throws NumberFormatException, JSONException{
        Audio audio = new Audio();
        audio.id = Api.unescape(o.getString("id"));
        audio.artist = Api.unescape(o.optString("artist"));
        audio.title = Api.unescape(o.optString("track"));
        audio.duration = Long.parseLong(o.getString("length"));
        audio.url = o.optString("file", null);
        audio.size = Long.parseLong(o.getString("size"));
        audio.bitrate = o.optString("bitrate", null);
        return audio;
    }
}