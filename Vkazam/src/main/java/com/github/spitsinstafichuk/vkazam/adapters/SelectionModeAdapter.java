package com.github.spitsinstafichuk.vkazam.adapters;

import com.github.spitsinstafichuk.vkazam.adapters.params.SelectionModeAdapterParams;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Adapter that handles selection mode.
 *
 * @author Michael Spitsin
 * @since 2014-06-14
 */
public class SelectionModeAdapter extends SimpleCursorAdapter {

    /**
     * IDs of checked items.
     */
    private Set<Long> mCheckedIds;

    private SelectionModeAdapterParams mParams;

    /**
     * Listener for clicks on checkbox or simulated checkbox.
     */
    private OnItemCheckerClickListener mListener;


    public SelectionModeAdapter(Context context, SelectionModeAdapterParams params) {
        super(context, params.getLayout(), params.getCursor(), params.getFrom(), params.getTo(), params.getFlags());
        mParams = params;
        mCheckedIds = new HashSet<Long>();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View result = super.newView(context, cursor, viewGroup);

        if (result != null) {
            CheckBox checkBox = (CheckBox) result.findViewById(mParams.getCheckboxId());
            View simulatedCheckBox = result.findViewById(mParams.getSimulatedCheckboxId());
            View checkedView = result.findViewById(mParams.getCheckedViewId());
            View uncheckedView = result.findViewById(mParams.getUncheckedViewId());

            result.setTag(mParams.getCheckboxId(), checkBox);
            result.setTag(mParams.getSimulatedCheckboxId(), simulatedCheckBox);
            result.setTag(mParams.getCheckedViewId(), checkedView);
            result.setTag(mParams.getUncheckedViewId(), uncheckedView);
        }

        return result;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View result = super.getView(position, convertView, parent);

        if (result == null) {
            return null;
        }

        CheckBox checkBox = (CheckBox) result.getTag(mParams.getCheckboxId());
        View simulatedCheckBox = (View) result.getTag(mParams.getSimulatedCheckboxId());
        View checkedView = (View) result.getTag(mParams.getCheckedViewId());
        View uncheckedView = (View) result.getTag(mParams.getUncheckedViewId());

        if ((checkBox != null) && (mListener != null)) {
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onCheckboxClick(parent, result, position, getItemId(position));
                }
            });
        }

        if ((simulatedCheckBox != null) && (mListener != null)) {
            simulatedCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onCheckboxClick(parent, result, position, getItemId(position));
                }
            });
        }

        if (isItemChecked(position)) {
            if (checkedView != null) {
                checkedView.setVisibility(View.VISIBLE);
            }

            if (uncheckedView != null) {
                uncheckedView.setVisibility(View.INVISIBLE);
            }
        } else {
            if (checkedView != null) {
                checkedView.setVisibility(View.INVISIBLE);
            }

            if (uncheckedView != null) {
                uncheckedView.setVisibility(View.VISIBLE);
            }
        }

        return result;
    }

    public void setOnItemCheckerClickListener(OnItemCheckerClickListener listener) {
        this.mListener = listener;
    }

    public void setItemChecked(int position, boolean isChecked) {
        if ((position < 0) || (position >= getCount())) {
            return;
        }

        long itemId = getItemId(position);

        if (isChecked) {
            mCheckedIds.add(itemId);
        } else {
            if (mCheckedIds.contains(itemId)) {
                mCheckedIds.remove(itemId);
            }
        }
    }

    public boolean isItemChecked(int position) {
        return mCheckedIds.contains(getItemId(position));
    }

    public int getCheckedItemsCount() {
        return mCheckedIds.size();
    }

    public long[] getCheckedIds() {
        long[] result = new long[mCheckedIds.size()];
        int index = 0;

        for (long checkedId : mCheckedIds) {
            result[index] = checkedId;
            index++;
        }

        return result;
    }

    /**
     * Listener for clicks on checkbox or simulated checkbox.
     */
    public static interface OnItemCheckerClickListener {

        /**
         * Calls when any checkbox was clicked.
         *
         * @param parent - parent holder of item
         * @param view - item in that checkbox was clicked
         * @param position - position of this item
         * @param id - item id (id of row, that was represented by this item)
         */
        void onCheckboxClick(ViewGroup parent, View view, int position, long id);
    }
}
