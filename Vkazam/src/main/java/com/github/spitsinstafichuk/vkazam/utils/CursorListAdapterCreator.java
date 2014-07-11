package com.github.spitsinstafichuk.vkazam.utils;

import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.spitsinstafichuk.vkazam.adapters.ButtonAdapter;
import com.github.spitsinstafichuk.vkazam.adapters.SelectionModeAdapter;
import com.github.spitsinstafichuk.vkazam.adapters.params.ButtonAdapterParams;
import com.github.spitsinstafichuk.vkazam.adapters.params.SelectionModeAdapterParams;
import com.github.spitsinstafichuk.vkazam.adapters.params.SimpleCursorAdapterParams;
import com.github.spitsinstafichuk.vkazam.fragments.CursorListFragment;

/**
 * Factory, that produces different cursor adapters.
 *
 * @author Michael Spitsin
 * @since 2014-06-23
 */
public class CursorListAdapterCreator {
    public enum CursorType {
        SelectionMode,
        Button
    }

    public static CursorAdapter createCursorListAdapter(CursorType type, CursorListFragment fragment, SimpleCursorAdapterParams params) {
        switch (type) {
            case SelectionMode:
                return createSelectionModeAdapter(fragment, (SelectionModeAdapterParams) params);
            case Button:
                return CreateButtonAdapter(fragment, (ButtonAdapterParams) params);
            default:
                return CreateButtonAdapter(fragment, (ButtonAdapterParams) params);
        }
    }

    static private CursorAdapter createSelectionModeAdapter(final CursorListFragment fragment, SelectionModeAdapterParams params) {
        return new SelectionModeAdapter(fragment.getActivity(), params) {
            @Override
            public void setViewText(TextView v, String text) {

                String s = fragment.beforeSetViewText(v, text);
                if (s != null) {
                    super.setViewText(v, s);
                }
            }

            @Override
            public void setViewImage(ImageView v, String value) {
                String s = fragment.beforeSetViewImage(v, value);
                if (s != null) {
                    super.setViewImage(v, s);
                }
            }
        };
    }

    static private CursorAdapter CreateButtonAdapter(final CursorListFragment fragment, ButtonAdapterParams params) {
        return new ButtonAdapter(fragment.getActivity(), params) {
            @Override
            public void setViewText(TextView v, String text) {

                String s = fragment.beforeSetViewText(v, text);
                if (s != null) {
                    super.setViewText(v, s);
                }
            }

            @Override
            public void setViewImage(ImageView v, String value) {
                String s = fragment.beforeSetViewImage(v, value);
                if (s != null) {
                    super.setViewImage(v, s);
                }
            }
        };
    }
}
