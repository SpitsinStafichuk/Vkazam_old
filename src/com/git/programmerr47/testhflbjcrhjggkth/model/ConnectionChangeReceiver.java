package com.git.programmerr47.testhflbjcrhjggkth.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionChangeReceiver extends BroadcastReceiver {

	  @Override
	  public void onReceive(Context context, Intent intent ) {
		    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
		    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		    if (activeNetInfo != null ) {
		    	MicroScrobblerModel.getInstance().getFingerprintManager().setRecognizeStatus("Active Network Type : " + activeNetInfo.getTypeName());
			    Toast.makeText( context, "Active Network Type : " + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
			    context.startService(new Intent(context, RecognizeService.class));
		    } else {
		    	MicroScrobblerModel.getInstance().getFingerprintManager().setRecognizeStatus("Active Network Type : none");
			    Toast.makeText( context, "Active Network Type : none", Toast.LENGTH_SHORT ).show();
		    }
	  }
}
