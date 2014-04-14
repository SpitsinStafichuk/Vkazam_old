package com.git.programmerr47.vkazam.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Service that connects to another single service when onCreate is called
 * and disconnects from related service when onDestroy is called
 *
 * @author Michael Spitsin
 * @since 2014-03-14
 */
public abstract class RelatingService extends StartBoundService{

    /**
     * Defines if service is bound
     */
    private boolean isRelativeServiceBound;

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to RecognizeFingerprintService, cast the IBinder and get RecognizeFingerprintService instance
            StartBoundService.ServiceBinder binder = (StartBoundService.ServiceBinder) service;
            isRelativeServiceBound = true;
            RelatingService.this.onServiceConnected(binder.getService());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isRelativeServiceBound = false;
        }
    };

    public void onCreate() {
        Intent intent = new Intent(this, getRelativeServiceClass());
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void onDestroy() {
        if (isRelativeServiceBound) {
            unbindService(connection);
            isRelativeServiceBound = false;
        }
    }

    /**
     * Calls when related service if connected to this service
     * Subclasses must implement this method and cast this class to their own
     *
     * @param service - connected service
     */
    protected abstract void onServiceConnected(Service service);

    /**
     * Defines which service will be relative to this service
     *
     * @return class Name of Relative Service
     */
    protected abstract Class<?> getRelativeServiceClass();
}
