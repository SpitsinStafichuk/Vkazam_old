package com.github.spitsinstafichuk.vkazam.adapters.params;

import com.github.spitsinstafichuk.vkazam.utils.Constants;

/**
 * Container of parameters, that will be send to SelectionModeAdapter.
 *
 * @author Michael Spitsin
 * @since 2014-06-14
 */
public class SelectionModeAdapterParams extends SimpleCursorAdapterParams{

    /**
     * Id of checkbox in given layout.
     */
    private int mCheckboxId;

    /**
     * Id of "Checkbox" in given layout. If standard checkbox is a bad decision,
     * then there is another way to represent checking. There is 3 additional views.
     * This id is id of container of two states: checked state and unchecked state.
     */
    private int mSimulatedCheckboxId;

    /**
     * Id of unchecked view in given layout. If standard checkbox is a bad decision,
     * then there is another way to represent checking. There is 3 additional views.
     * This id is id of view that shows unchecked state of item.
     */
    private int mUncheckedViewId;

    /**
     * Id of checked view in given layout. If standard checkbox is a bad decision,
     * then there is another way to represent checking. There is 3 additional views.
     * This id is id of view that shows checked state of item.
     */
    private int mCheckedViewId;

    protected SelectionModeAdapterParams(Builder builder) {
        super(builder);
        mCheckboxId = builder.mCheckboxId;
        mSimulatedCheckboxId = builder.mSimulatedCheckboxId;
        mUncheckedViewId = builder.mUncheckedViewId;
        mCheckboxId = builder.mCheckedViewId;
    }

    public int getCheckboxId() {
        return mCheckboxId;
    }

    public int getSimulatedCheckboxId() {
        return mSimulatedCheckboxId;
    }

    public int getCheckedViewId() {
        return mCheckedViewId;
    }

    public int getUncheckedViewId() {
        return mUncheckedViewId;
    }

    public static class Builder extends SimpleCursorAdapterParams.Builder {
        private int mCheckboxId = Constants.NO_ID;
        private int mSimulatedCheckboxId = Constants.NO_ID;
        private int mUncheckedViewId = Constants.NO_ID;
        private int mCheckedViewId = Constants.NO_ID;

        /**
         * Standard constructor with only one argument, because it is necessary to have layout.
         *
         * @param layout - id of list item layout
         */
        public Builder(int layout) {
            super(layout);
        }

        public Builder setCheckboxId(int checkboxId) {
            mCheckboxId = checkboxId;
            return this;
        }

        public Builder setSimulatedCheckboxId(int simulatedCheckboxId) {
            mSimulatedCheckboxId = simulatedCheckboxId;
            return this;
        }

        public Builder setUncheckedViewId(int uncheckedViewId) {
            mUncheckedViewId = uncheckedViewId;
            return this;
        }

        public Builder setCheckedViewId(int checkedViewId) {
            mCheckedViewId = checkedViewId;
            return this;
        }

        @Override
        public SelectionModeAdapterParams build() {
            return new SelectionModeAdapterParams(this);
        }
    }
}
