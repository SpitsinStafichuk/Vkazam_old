package com.github.spitsinstafichuk.vkazam.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class NetworkUtils {

	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("settingsOnlyWiFiConntection", false)) {
	    	return activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI && activeNetworkInfo.isConnected();
	    }
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
