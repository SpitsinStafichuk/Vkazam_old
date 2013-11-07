package com.git.programmerr47.testhflbjcrhjggkth.model.lastfm;

public interface IScrobbler {
    String LASTFM_API_KEY = "f9c993afaa93e68a3113518aba4f8a23";
    String LASTFM_SECRET = "718fcd80cfefec3d88a7d1666a2107c6";
    String LASTFM_USERAGENT = "Radio Scrobbler v0.1 Android en-US"; 

    boolean hasLastFmAccount();
    void setEnableScrobbler(boolean enabled);
    boolean isScrobblerEnabled();
    void scrobble(String artist, String title);
	void signIn(String username, String password);
	void signInCancel();
}
