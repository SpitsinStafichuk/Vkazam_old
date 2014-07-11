package com.github.spitsinstafichuk.vkazam.fragments;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.spitsinstafichuk.vkazam.adapters.params.SimpleCursorAdapterParams;
import com.github.spitsinstafichuk.vkazam.utils.CursorListAdapterCreator;

/**
 * Base class for any tab that uses cursor as main resource of adapter
 *
 * @author Spitsin Michael
 * @since 2014-06-23
 */
public abstract class CursorListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INTERNAL_LOADER_ID = 0;

    private CursorAdapter mAdapter;
    private CursorListAdapterCreator.CursorType mCursorType;

    public CursorListFragment (CursorListAdapterCreator.CursorType cursorType) {
        mCursorType = cursorType;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = CursorListAdapterCreator.createCursorListAdapter(mCursorType, this, getAdapterParams());

        // Show menu in action bar
        setHasOptionsMenu(true);

        setListAdapter(mAdapter);

        setListShown(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getLoaderManager() != null) {
            Loader l = getLoaderManager().getLoader(INTERNAL_LOADER_ID);

            if ((l != null) && (l.isStarted())) {
                l.stopLoading();
            }

            getLoaderManager().destroyLoader(INTERNAL_LOADER_ID);
            getLoaderManager().initLoader(INTERNAL_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new AstroCursorLoader(this.getActivity(), getQuery(), getDBType());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        // Swap the new cursor in, the framework will take care of closing the old cursor once we return
        mAdapter.swapCursor(cursor);

        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // The list should now be shown
                    if (isResumed()) {
                        setListShown(true);
                    } else {
                        setListShownNoAnimation(true);
                    }
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // The last cursor provided to onLoadFinished() is about to be closed, make sure that it is no longer in use
        mAdapter.swapCursor(null);
    }

    public void notifyDataSetChanged() {
        if (isAdded() && (getLoaderManager() != null)) {
            getLoaderManager().restartLoader(0, null, CursorListFragment.this);
        }
    }

    /**
     * Returns parameters to initialise list view cursor adapter.
     *
     * @return adapter parameters.
     */
    protected abstract SimpleCursorAdapterParams getAdapterParams();

    /**
     * @return query for initializing adapter
     */
    protected abstract String getQuery();

    /**
     * @return type of db
     */
    protected abstract AstroCursorLoader.DB_TYPE getDBType();

    /**
     * Hook method that called before set text to list item
     *
     * @param v    view of element
     * @param text text got from cursor
     * @return text that will be set to view or null if no text should be set
     */
    public abstract String beforeSetViewText(TextView v, String text);

    /**
     * Hook method that called before set image to list item
     *
     * @param v    view of element
     * @param value uri of image
     * @return text that will be set to view or null if no text should be set
     */
    public String beforeSetViewImage(ImageView v, String value) {
        return value;
    }

    /**
     * Returns list view cursor adapter.
     *
     * @return cursor adapter.
     */
    protected CursorAdapter getAdapter() {
        return mAdapter;
    }


    /**
     * Custom implementation of CursorLoader for user database
     *
     * @author Michael Spitsin
     * @since 2014-02-10
     */
    protected static class AstroCursorLoader extends CursorLoader {
        public static enum DB_TYPE {ASTRO_DB, PODCAST_DB}

        private String query;
        private DB_TYPE dbType;

        /**
         * Constructor
         *
         * @param context - context for default parent class constructor
         * @param query   - custom db query for loading cursor in background
         */
        public AstroCursorLoader(Context context, String query, DB_TYPE dbType) {
            super(context);
            this.query = query;
            this.dbType = dbType;
        }

        @Override
        public Cursor loadInBackground() {
            switch (dbType) {
                case ASTRO_DB:
                    if (DBHelper.getInstance() == null) {
                        DBHelper.init(getContext());
                    }
                    return (Cursor) DBHelper.getInstance().runOnDB(getDbRunnable(), true);
                case PODCAST_DB:
                    if (PodcastDbHelper.getInstance() == null) {
                        PodcastDbHelper.init(getContext());
                    }
                    return (Cursor) PodcastDbHelper.getInstance().runOnDB(getDbRunnable(), true);
                default:
                    throw new IllegalArgumentException();
            }
        }

        private DBRunnable getDbRunnable() {
            return new DBRunnable() {
                @Override
                public Object run(SQLiteDatabase db) {
                    if (query == null) {
                        return null;
                    } else {
                        return db.rawQuery(query, null);
                    }
                }
            };
        }
    }
}
