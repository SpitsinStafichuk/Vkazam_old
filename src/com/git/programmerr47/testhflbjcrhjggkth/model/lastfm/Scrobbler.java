package com.git.programmerr47.testhflbjcrhjggkth.model.lastfm;

import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.LastfmLoginException;

import android.util.Log;

import de.umass.lastfm.Authenticator;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Session;
import de.umass.lastfm.Track;

public class Scrobbler implements IScrobbler {
        private Session lastfmSession;
        private boolean enableScrobbler;
       
        public Scrobbler(String login, String password) throws LastfmLoginException {
                Caller.getInstance().setUserAgent(LASTFM_USERAGENT);
                Caller.getInstance().setCache(null);
               
                if (login != null && password != null) {
                        lastfmSession = Authenticator.getMobileSession(login, password, LASTFM_API_KEY, LASTFM_SECRET);

                        if (!Caller.getInstance().getLastResult().isSuccessful()) {
                                throw new LastfmLoginException(Caller.getInstance().getLastResult().getErrorMessage());
                        }
                }
               
                Log.v("Settings", "Last fm session: " + lastfmSession);
        }
       
        @Override
        public boolean hasLastFmAccount() {
                return lastfmSession != null;
        }
       
        @Override
        public void setEnableScrobbler(boolean enabled) {
                enableScrobbler = enabled;
        }
       
        @Override
        public boolean isScrobblerEnabled() {
                return enableScrobbler;
        }
       
        @Override
        public void scrobble(String artist, String title) {
                if (lastfmSession != null && enableScrobbler) {
                        int now = (int) (System.currentTimeMillis() / 1000);
                        Track.scrobble(artist, title, now, lastfmSession);
                }
        }

}

