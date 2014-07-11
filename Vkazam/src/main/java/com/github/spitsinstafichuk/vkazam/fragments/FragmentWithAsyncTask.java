package com.github.spitsinstafichuk.vkazam.fragments;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Fragment with replacement of existing Loaders and LoaderManager.
 * Uses Async task instead of Loader and LoaderManager,
 * because this is buggy stuff (see stackoverflow.com)
 *
 * @author Michael Spitsin
 * @since 2014-06-23
 */
public abstract class FragmentWithAsyncTask<D> extends ListFragment implements LoaderTask.Callbacks<D> {

    private LoaderTask mLoaderTask;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    //todo write necessary methods
}
