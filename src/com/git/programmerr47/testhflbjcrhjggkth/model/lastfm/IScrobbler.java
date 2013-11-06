package com.git.programmerr47.testhflbjcrhjggkth.model.lastfm;

public interface IScrobbler {
	String LASTFM_API_KEY = "-1";
	String LASTFM_SECRET = "-1";
	String LASTFM_USERAGENT = "Microphone Scrobbler v0.1 Android en-US";

    boolean hasLastFmAccount();
    void setEnableScrobbler(boolean enabled);
    boolean isScrobblerEnabled();
    void scrobble(String artist, String title);
}
