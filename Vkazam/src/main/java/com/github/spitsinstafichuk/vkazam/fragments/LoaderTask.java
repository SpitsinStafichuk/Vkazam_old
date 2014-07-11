package com.github.spitsinstafichuk.vkazam.fragments;

import android.os.AsyncTask;

/**
 * Abstract class that computes some long evaluations
 * and than calls some functions of it listener.
 *
 * @author Michael Spitsin
 * @since 2014-06-23
 */
public abstract class LoaderTask<P, R> extends AsyncTask<P, Void, R> {

    Callbacks mCallbacks;

    public LoaderTask(Callbacks<R> callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    protected void onCancelled() {
        if (mCallbacks != null) {
            mCallbacks.onLoadCanceled();
        }
    }

    @Override
    protected void onPostExecute(R result) {
        if (mCallbacks != null) {
            mCallbacks.onLoadFinished(result);
        }
    }

    /**
     * Interface that determines listener, which calls
     * methods, when some loading is finished or canceled.
     *
     * @param <Data> - Type of data, which class work with
     */
    public interface Callbacks<Data> {

        /**
         * Calls when load is finished.
         *
         * @param data - returned data in the end of loading
         */
        void onLoadFinished(Data data);

        /**
         * Calls when load is canceled.
         */
        void onLoadCanceled();
    }
}
