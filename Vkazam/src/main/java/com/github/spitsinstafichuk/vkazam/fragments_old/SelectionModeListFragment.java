package com.github.spitsinstafichuk.vkazam.fragments_old;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.astroplayerbeta.Common;
import com.astroplayerbeta.R;
import com.astroplayerbeta.darfm.AdapterItemClickListener;
import com.astroplayerbeta.darfm.Animations;
import com.astroplayerbeta.gui.components.adapters.CheckingCursorAdapter;
import com.astroplayerbeta.gui.mediabrowser.actionmode.AbstractContextActionFactory;
import com.astroplayerbeta.gui.mediabrowser.actionmode.ContextAction;
import com.astroplayerbeta.util.StringUtil;
import com.github.spitsinstafichuk.vkazam.fragments.CursorListFragment;
import com.mobeta.android.dslv.DragSortItemView;

/**
 * Fragment for working with selection mode.
 *
 * @author Michael Spitsin
 * @since 2014-06-23
 */
public abstract class SelectionModeListFragment extends CursorListFragment implements ActionMode.Callback, AdapterView.OnItemLongClickListener, AdapterItemClickListener, ContextAction.OnActionCompleteListener {

    protected static final String COVER_ART = "cover_art";
    protected static final String COVER_ART_ABBREVIATION = "cover_art_abbreviation";
    protected static final String COVER_ART_DELIMITER = ":";

    private ActionMode mMode;

    public SelectionModeListFragment() {
        super(CursorListAdapterCreator.CursorType.Checking);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setSelector(R.drawable.list_selector_kitkat);
        getListView().setOnItemLongClickListener(this);
        getAdapter().setItemClickListener(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        super.onLoadFinished(cursorLoader, cursor);

        CheckingCursorAdapter adapter = getAdapter();
        if (adapter.getCheckedItemCount() > 0) {
            startOrUpdateActionMode((ActionBarActivity) getActivity(), adapter);
        }
    }

    @Override
    protected AstroCursorLoader.DB_TYPE getDBType() {
        return AstroCursorLoader.DB_TYPE.ASTRO_DB;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Create the menu from the xml file
        if (getIdOfContextMenu() != Common.NO_ID) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(getIdOfContextMenu(), menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        AbstractContextActionFactory factory = getContextActionFactory();
        if (factory != null) {
            ContextAction action = factory.getAction(item.getItemId());
            if (action != null) {
                action.setSelectedPositions(getAdapter().getCheckedItemPositions());
                action.setOnActionCompleteListener(this);
                action.start();
            }
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        CheckingCursorAdapter checkingCursorAdapter = getAdapter();
        for (int i = 0; i < getListView().getLastVisiblePosition() - getListView().getFirstVisiblePosition() + 1; i++) {
            View view = getListView().getChildAt(i);
            int position = getListView().getFirstVisiblePosition() + i - getListView().getHeaderViewsCount();
            if (view != null && checkingCursorAdapter.isItemChecked(position)) {
                if (view instanceof DragSortItemView) {
                    view = ((DragSortItemView) view).getChildAt(0);
                }

                if (view != null) {
                    CheckingCursorAdapter.ViewHolder holder = (CheckingCursorAdapter.ViewHolder) view.getTag();
                    if (holder != null) {
                        setViewChecked(holder, false, position);
                    }
                }
                checkingCursorAdapter.setItemChecked(position, !checkingCursorAdapter.isItemChecked(position));
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    notifyDataSetChanged();
                }
            }
        }, Animations.getSetCheckedDuration());

        mMode = null;

        checkingCursorAdapter.clearCheckMode();
    }

    @Override
    public void onDetach () {
        super.onDetach();
        mMode = null;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        position -= getListView().getHeaderViewsCount();
        if (position > Common.NO_ID) {
            ActionBarActivity activity = (ActionBarActivity) getActivity();

            if (view instanceof DragSortItemView) {
                view = ((DragSortItemView) view).getChildAt(0);
            }

            if (view != null) {
                CheckingCursorAdapter.ViewHolder holder = (CheckingCursorAdapter.ViewHolder) view.getTag();
                if (holder != null) {
                    setViewChecked(holder, !getAdapter().isItemChecked(position), position);
                }
            }
            getAdapter().setItemChecked(position, !getAdapter().isItemChecked(position));

            if (getAdapter().getCheckedItemCount() > 0) {
                startOrUpdateActionMode(activity, getAdapter());
            } else {
                clearActionMode();
            }

            return true;
        }
        return false;
    }

    private void setViewChecked(CheckingCursorAdapter.ViewHolder holder, boolean isChecked, int position) {
        if (getAdapter().getCheckedViewBackgroundColor() != Common.NO_ID) {
            holder.checkedView.setBackgroundColor(getAdapter().getCheckedViewBackgroundColor());
        }

        if (getAdapter().getUncheckedViewBackgroundColor() != Common.NO_ID) {
            holder.unCheckedView.setBackgroundColor(getAdapter().getUncheckedViewBackgroundColor());
        }

        Animations.setChecked(holder.unCheckedView, holder.checkedView, isChecked);

        Cursor cursor = (Cursor) getAdapter().getItem(position);
        if (cursor.getColumnIndex(COVER_ART_ABBREVIATION) != Common.NO_ID) {
            String abbreviation = cursor.getString(cursor.getColumnIndex(COVER_ART_ABBREVIATION));
            if ((abbreviation != null) && (abbreviation.contains(COVER_ART_DELIMITER))) {
                String coverArt = abbreviation.substring(0, abbreviation.indexOf(COVER_ART_DELIMITER));
                if (!StringUtil.isEmpty(coverArt)) {
                    Animations.setChecked(holder.abbreviationView, null, isChecked, View.INVISIBLE);
                } else {
                    Animations.setChecked(holder.abbreviationView, null, isChecked, View.VISIBLE);
                }
            } else {
                Animations.setChecked(holder.abbreviationView, null, isChecked, View.INVISIBLE);
            }
        }
    }

    @Override
    public final void onListItemClick(ListView l, View v, int position, long id) {
        position -= getListView().getHeaderViewsCount();

        if (position > Common.NO_ID) {
            if (mMode != null) {
                position += getListView().getHeaderViewsCount();
                onItemLongClick(null, v, position, id);
            } else {
                performListItemClick(l, v, position, id);
            }
        }
    }

    @Override
    public final void onItemClick(View view, int position) {
        onItemLongClick(null, view, position + getListView().getHeaderViewsCount(), getAdapter().getItemId(position));
    }

    @Override
    public void onItemOverflowClick(View view, int position) {
    }

    @Override
    public void onActionComplete(long actionId) {
        clearActionMode();
    }

    public CheckingCursorAdapter getAdapter() {
        return (CheckingCursorAdapter) super.getAdapter();
    }

    /**
     * Refresh actionMode if it is already created (Then Action mode calls onPrepareActioMode)
     * or creates actionMode if it is undefined.
     *
     * @param activity - activity that starts new action mode
     * @param checkingCursorAdapter - adapter of current displaying ListView
     */
    protected void startOrUpdateActionMode(ActionBarActivity activity, CheckingCursorAdapter checkingCursorAdapter) {
        if (mMode == null) {
            mMode = activity.startSupportActionMode(this);
        }
        mMode.setTitle(checkingCursorAdapter.getCheckedItemCount() + " selected");
        mMode.invalidate();
    }

    /**
     * Finish action mode if it still exists.
     */
    public void clearActionMode() {
        if (mMode != null) {
            mMode.finish();
        }
    }

    /**
     * @return id of context action menu, that placed in folder res/menu
     */
    public abstract int getIdOfContextMenu();

    /**
     * Different implementations of this fragment has different logic for same items.
     * For example. Almost all fragments have delete item,
     * but each fragment has it's own delete action for this item.
     *
     * @return concrete factory of actions
     */
    public abstract AbstractContextActionFactory getContextActionFactory();

    /**
     * Performs click on ListItem
     *
     * @param l - ListView on which item was clicked
     * @param v - View of ListView item
     * @param position - position of ListView item
     * @param id - id of ListView item
     */
    protected abstract void performListItemClick(ListView l, View v, int position, long id);
}
