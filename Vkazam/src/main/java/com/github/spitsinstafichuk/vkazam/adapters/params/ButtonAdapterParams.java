package com.github.spitsinstafichuk.vkazam.adapters.params;

/**
 * Container of parameters, that will be send to ButtonAdapter.
 *
 * @author Michael Spitsin
 * @since 2014-06-14
 */
public class ButtonAdapterParams extends SelectionModeAdapterParams{

    /**
     * Ids of all buttons inside each listView item.
     */
    private int[] mButtonIds;

    protected ButtonAdapterParams(Builder builder) {
        super(builder);
        mButtonIds = builder.mButtonIds;
    }

    public int[] getButtonIds() {
        return mButtonIds;
    }

    public static class Builder extends SelectionModeAdapterParams.Builder {
        private int[] mButtonIds;

        /**
         * Standard constructor with only one argument, because it is necessary to have layout.
         *
         * @param layout - id of list item layout
         */
        public Builder(int layout) {
            super(layout);
        }

        public void setButtonIds(int[] buttonIds) {
            mButtonIds = buttonIds;
        }

        @Override
        public ButtonAdapterParams build() {
            return new ButtonAdapterParams(this);
        }
    }
}
