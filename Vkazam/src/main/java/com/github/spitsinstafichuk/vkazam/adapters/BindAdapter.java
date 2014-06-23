package com.github.spitsinstafichuk.vkazam.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Abstract adapter that separates getting View on
 * creating new View and binding existing View.
 *
 * @author Michael Spitsin
 * @since 2014-06-23
 */
public abstract class BindAdapter extends BaseAdapter {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = newView(position, parent);
        } else {
            v = convertView;
        }

        bindView(position, v, parent);
        return v;
    }

    /**
     * This method calls when adapter item view appears in the first time.
     * It defines and creates new view.
     *
     * @param position - position of list view item
     * @param parent   - parent list view
     * @return created view
     */
    protected abstract View newView(int position, ViewGroup parent);

    /**
     * This method calls when adapter need to prepare view before showing.
     * It defines what in this view must be shown, sets listeners and e.t.c.
     *
     * @param position - position of list view item
     * @param view     - binding view
     * @param parent   - parent list view
     */
    protected abstract void bindView(int position, View view, ViewGroup parent);
}
