package com.git.programmerr47.testhflbjcrhjggkth.model.lastfm;

import java.util.HashSet;
import java.util.Set;

import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.LastfmLoginException;

import android.util.Log;

import de.umass.lastfm.Authenticator;
import de.umass.lastfm.CallException;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Session;
import de.umass.lastfm.Track;

public class Scrobbler implements IScrobbler, ISignInObservable {
	
	public interface IOnSignInResultListener {
		public void onResult(String status);
	}
	
	private Session lastfmSession;
    private boolean enableScrobbler;
    private Thread signInThread;
    private Set<IOnSignInResultListener> listeners;
    private String correctUsername;
    private String correctPassword;
   
    public Scrobbler() {
            Caller.getInstance().setUserAgent(LASTFM_USERAGENT);
            Caller.getInstance().setCache(null);
            listeners = new HashSet<IOnSignInResultListener>();
    }
    
    @Override
    public void signIn(final String username, final String password) {
    	signInThread = new Thread() {
    		@Override
    		public void run() {
    			_signIn(username, password);
    			signInThread = null;
    		}
    	};
    	signInThread.start();
    }
    
    private synchronized void _signIn(String username, String password) {
    	String status = "username or password is null";
    	if (username != null && password != null) {
    		try {
    			lastfmSession = Authenticator.getMobileSession(username, password, LASTFM_API_KEY, LASTFM_SECRET);
    		} catch(Exception e) {
    			if(e.getMessage() == null) {
    				status = "You probably have no internet connection";
    			} else {
    				status = e.getMessage();
    			}
    			notifyOnSignInResultListener(status);
    		}
    		//Log.v("Scrobbler", "Caller.getInstance() = " + Caller.getInstance().toString());
    		//Log.v("Scrobbler", "Caller.getInstance().getLastResult() = " + Caller.getInstance().getLastResult().toString());
    		try {
    			if (!Caller.getInstance().getLastResult().isSuccessful()) {
    				status = Caller.getInstance().getLastResult().getErrorMessage();
    			} else {
    				status = STATUS_SUCCESS;
    				correctUsername = username;
    				correctPassword = password;
    			}
    		} catch(Exception e) {
    			if(e.getMessage() == null) {
    				status = "You probably have no internet connection";
    			} else {
    				status = e.getMessage();
    			}
    			notifyOnSignInResultListener(status);
    		}
    	}
    	notifyOnSignInResultListener(status);
    	Log.v("Settings", "Last fm session: " + lastfmSession);
    }
    
    @Override
    public void signInCancel() {
    	//не факт, что это работает
    	if(signInThread != null)
    		signInThread.interrupt();
    }
    
    @Override
    public void setOnSignInResultListener(IOnSignInResultListener listener) {
    	listeners.add(listener);
    }
    
    @Override
    public void removeOnSignInResultListener(IOnSignInResultListener listener) {
    	listeners.remove(listener);
    }
    
    @Override
    public void notifyOnSignInResultListener(String status) {
    	for(IOnSignInResultListener listener : listeners) {
    		listener.onResult(status);
    	}
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

	public String getCorrectPassword() {
		return correctPassword;
	}

	public String getCorrectUsername() {
		return correctUsername;
	}
}

