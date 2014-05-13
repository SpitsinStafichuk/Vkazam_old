package com.github.spitsinstafichuk.vkazam.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Base service class for all vkazam recognizing services It is bound and start
 * service at same time
 *
 * @author Michael Spitsin
 * @since 2014-04-14
 */
public abstract class StartBoundService extends Service {

    // Binder given to clients
    private final IBinder serviceBinder = new ServiceBinder();

    /**
     * A flag that indicates whether the service is working
     */
    private boolean isWorking;

    /**
     * Count of bound objects to this service
     */
    private int binderCount;

    @Override
    public IBinder onBind(Intent intent) {
        binderCount++;
        return serviceBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        binderCount++;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        binderCount--;
        if (!isWorking && (binderCount == 0)) {
            stopSelf();
        }
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags,
            int startId) {
        return START_STICKY;
    }

    /**
     * Made protected method to observe binders from subclasses
     *
     * @return amount of bound objects to this service
     */
    @SuppressWarnings("unused")
    protected int getBinderAmount() {
        return binderCount;
    }

    /**
     * Stops working service and if no one have bound to it stops service
     * Subclasses must use this method to tell StartBoundService when it is
     * working They must call this method in the end of working function
     */
    protected void stopWorking() {
        isWorking = false;

        if (binderCount == 0) {
            stopSelf();
        }
    }

    /**
     * Start working service. Subclasses must use this method to tell
     * StartBoundService when it is working They must call this method in the
     * beginning of function
     */
    protected void startServiceWorking() {
        isWorking = true;
    }

    /**
     * Checks if service is working
     *
     * @return true if working, false otherwise
     */
    public boolean isWorking() {
        return isWorking;
    }

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class ServiceBinder extends Binder {

        /**
         * @return instance of StartBoundService so clients can call public
         * methods
         */
        public StartBoundService getService() {
            return StartBoundService.this;
        }
    }
}
