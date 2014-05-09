package com.git.programmerr47.vkazam.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Service that connects to another single service when onCreate is called and
 * disconnects from related service when onDestroy is called
 * 
 * @author Michael Spitsin
 * @since 2014-03-14
 */
public abstract class RelatingService extends StartBoundService {

	/**
	 * Defines if service is bound
	 */
	protected boolean isRelativeServiceBound;

	/**
	 * Defines callbacks for service binding, passed to bindService()
	 */
	private final ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to RecognizeFingerprintService, cast the IBinder and
			// get StartBoundService instance
			StartBoundService.ServiceBinder binder = (StartBoundService.ServiceBinder) service;
			isRelativeServiceBound = true;
			RelatingService.this.onServiceConnected(binder.getService());
			Log.v("Services", "onServiceConnected: "
					+ binder.getService().getClass().getName());
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			isRelativeServiceBound = false;
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Intent intent = new Intent(this, getRelativeServiceClass());
		startService(intent);
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isRelativeServiceBound) {
			cleanUpAllDependencies();
			unbindService(connection);
			isRelativeServiceBound = false;
		}
	}

	/**
	 * Calls when service is destroyed. Subclasses must remove all observers and
	 * dependencies of related service
	 */
	protected void cleanUpAllDependencies() {
		// Subclasses must override this method and
		// clean up all dependencies between two services: this and related
	}

	/**
	 * Calls when related service if connected to this service Subclasses must
	 * implement this method and cast this class to their own
	 * 
	 * @param service
	 *            - connected service
	 */
	protected abstract void onServiceConnected(Service service);

	/**
	 * Defines which service will be relative to this service
	 * 
	 * @return class Name of Relative Service
	 */
	protected abstract Class<?> getRelativeServiceClass();
}
