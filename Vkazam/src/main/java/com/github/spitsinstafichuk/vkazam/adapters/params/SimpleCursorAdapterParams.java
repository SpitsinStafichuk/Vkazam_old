package com.github.spitsinstafichuk.vkazam.adapters.params;

import com.github.spitsinstafichuk.vkazam.utils.Constants;

import android.database.Cursor;

/**
 * Container of parameters, that will be send to SimpleCursorAdapter.
 *
 * @author Michael Spitsin
 * @since 2014-06-14
 */
public class SimpleCursorAdapterParams {

    /**
     * Cursor that represents elements of list, that will be handled by simpleCursorAdapter or his inheritors.
     */
    private Cursor mCursor;

    /**
     * ResID of item layout.
     */
    private int mLayout;

    /**
     * Array of column names, that must be represented by given layout, or handled in other way.
     * All elements of this array must be in given cursor.
     */
    private String[] mFrom;

    /**
     * Array of IDs of item components (only TextViews and ImageViews), that must exist in given layout.
     * Each element of the array must be associated with appropriate element of mFrom array.
     */
    private int[] mTo;

    /**
     * Optional flags.
     */
    private int mFlags;

    protected SimpleCursorAdapterParams(Builder builder) {
        mCursor = builder.mCursor;
        mLayout = builder.mLayout;
        mFrom = builder.mFrom;
        mTo = builder.mTo;
        mFlags = builder.mFlags;
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public int getLayout() {
        return mLayout;
    }

    public String[] getFrom() {
        return mFrom;
    }

    public int[] getTo() {
        return mTo;
    }

    public int getFlags() {
        return mFlags;
    }

    public static class Builder {
        private Cursor mCursor;
        private int mLayout;
        private String[] mFrom;
        private int[] mTo;
        private int mFlags;

        /**
         * Standard constructor with only one argument, because it is necessary to have layout.
         *
         * @param layout - id of list item layout
         */
        public Builder(int layout) {
            mLayout = layout;
        }

        public Builder setCursor(Cursor cursor) {
            mCursor = cursor;
            return this;
        }

        public Builder setFrom(String[] from) {
            mFrom = from;
            return this;
        }

        public Builder setTo(int[] to) {
            mTo = to;
            return this;
        }

        public Builder setFlags(int flags) {
            mFlags = flags;
            return this;
        }

        public SimpleCursorAdapterParams build() {
            return new SimpleCursorAdapterParams(this);
        }
    }
}
