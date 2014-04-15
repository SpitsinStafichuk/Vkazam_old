package com.git.programmerr47.vkazam.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Base service class for all vkazam recognizing services
 * It is bound and start service at same time
 *
 * @author Michael Spitsin
 * @since 2014-04-14
 */
public class StartBoundService extends Service {

    // Binder given to clients
    private final IBinder serviceBinder = new ServiceBinder();

    /**
     * Count of bound objects to this service
     */
    private int binderCount;

    @Override
    public IBinder onBind(Intent intent) {
        binderCount++;
        Log.v("Services", this.getClass().getName() + ": Recieved bindIntent (" + intent.getPackage() + ")");
        return serviceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        binderCount--;
        return super.onUnbind(intent);
    }

    public int onStartCommand(android.content.Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * Made protected method to observe binders from subclasses
     *
     * @return count of bound objects to this service
     */
    protected int getBinderCount() {
        return binderCount;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class ServiceBinder extends Binder {

        /**
         * @return instance of RecognizeFingerprintService so clients can call public methods
         */
        public StartBoundService getService() {
            return StartBoundService.this;
        }
    }
}
