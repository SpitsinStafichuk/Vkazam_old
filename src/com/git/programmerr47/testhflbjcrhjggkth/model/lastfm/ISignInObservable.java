package com.git.programmerr47.testhflbjcrhjggkth.model.lastfm;

import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.Scrobbler.IOnSignInResultListener;

public interface ISignInObservable {
	
	String STATUS_SUCCESS = "Success";

	void setOnSignInResultListener(IOnSignInResultListener listener);
    
    void removeOnSignInResultListener(IOnSignInResultListener listener);
    
    void notifyOnSignInResultListener(String status);
    
}
