package com.github.spitsinstafichuk.vkazam.adapters;

import com.github.spitsinstafichuk.vkazam.adapters.params.ButtonAdapterParams;
import com.github.spitsinstafichuk.vkazam.adapters.params.SelectionModeAdapterParams;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

/**
 * Adapter that handles keeping buttons by ListView items and handles clicks on them.
 *
 * @author Michael Spitsin
 * @since 2014-06-14
 */
public class ButtonAdapter extends SelectionModeAdapter {

    private ButtonAdapterParams mParams;

    /**
     * Listener for clicks on any button.
     */
    private OnItemButtonClickListener mListener;

    public ButtonAdapter(Context context, ButtonAdapterParams params) {
        super(context, params);
        mParams = params;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View result = super.newView(context, cursor, viewGroup);

        if (result != null) {
            int[] ids = mParams.getButtonIds();

            for(int id : ids) {
                View button = result.findViewById(id);
                result.setTag(id, button);
            }
        }

        return result;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View result = super.getView(position, convertView, parent);

        if (result == null) {
            return null;
        }

        int[] ids = mParams.getButtonIds();

        for(int id : ids) {
            final View button = (View) result.getTag(id);

            if ((button != null) && (mListener != null)) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onButtonClick(result, button, position, getItemId(position));
                    }
                });
            }
        }

        return result;
    }

    public void setOnItemButtonClickListener(OnItemButtonClickListener listener) {
        this.mListener = listener;
    }

    /**
     * Listener for clicks on item button.
     */
    public static interface OnItemButtonClickListener {

        /**
         * Calls when any button was clicked.
         *
         * @param itemView - item, in that button was clicked
         * @param button - button that was clicked
         * @param position - position of this item
         * @param id - item id (id of row, that was represented by this item)
         */
        void onButtonClick(View itemView, View button, int position, long id);
    }
}
