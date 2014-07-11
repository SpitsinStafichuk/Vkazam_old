package com.github.spitsinstafichuk.vkazam.fragments_old;

import android.util.Log;

import com.github.spitsinstafichuk.vkazam.R;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressDialogFragment extends MessageDialogFragment {

    private int positiveButtonVisibility;

    private int negativeButtonVisibility;

    private int style;

    private OnCancelListener cancelListener;

    private int progress = 0;

    private ProgressBar progressBarDefaultView;

    private ProgressBar progressBarHorizontalView;

    private TextView progressView;

    private LinearLayout horizontalStyleView;

    private LinearLayout buttonsView;

    public static ProgressDialogFragment newInstance(Builder builder) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        fragment.icon = builder.icon;
        fragment.title = builder.title;
        fragment.message = builder.message;
        fragment.positiveButtonTitle = builder.positiveButtonTitle;
        fragment.negativeButtonTitle = builder.negativeButtonTitle;
        fragment.positiveListener = builder.positiveListener;
        fragment.negativeListener = builder.negativeListener;
        fragment.positiveButtonVisibility = builder.positiveButtonVisibility;
        fragment.negativeButtonVisibility = builder.negativeButtonVisibility;
        fragment.style = builder.style;
        fragment.cancelListener = builder.cancelListener;
        Log.v("ProgressDialog", "Cancel listener = " + builder.cancelListener);
        if (builder.cancelListener != null) {
            fragment.setCancelable(true);
        } else {
            fragment.setCancelable(false);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_progress, container, false);

        iconView = (ImageView) view.findViewById(R.id.icon);
        if (icon != -1) {
            iconView.setImageResource(icon);
        }

        titleView = (TextView) view.findViewById(R.id.title);
        if (title != null) {
            titleView.setText(title);
        }

        messageView = (TextView) view.findViewById(R.id.message);
        if (message != null) {
            messageView.setText(message);
        }

        positiveButtonTitleView = (TextView) view.findViewById(R.id.positiveButtonTitle);
        if (positiveButtonTitle != null) {
            positiveButtonTitleView.setText(positiveButtonTitle);
        }

        negativeButtonTitleView = (TextView) view.findViewById(R.id.negativeButtonTitle);
        if (negativeButtonTitle != null) {
            negativeButtonTitleView.setText(negativeButtonTitle);
        }

        positiveButton = view.findViewById(R.id.positiveButton);
        positiveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (positiveListener != null) {
                    positiveListener.onDialogClick(ProgressDialogFragment.this, v);
                }
            }
        });
        positiveButton.setVisibility(positiveButtonVisibility);

        negativeButton = view.findViewById(R.id.negativeButton);
        negativeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (negativeListener != null) {
                    negativeListener.onDialogClick(ProgressDialogFragment.this, v);
                }
            }
        });
        negativeButton.setVisibility(negativeButtonVisibility);

        buttonsView = (LinearLayout) view.findViewById(R.id.buttons);
        if ((positiveButtonVisibility == View.GONE) &&
                (negativeButtonVisibility == View.GONE)) {
            buttonsView.setVisibility(View.GONE);
        }

        progressBarDefaultView = (ProgressBar) view.findViewById(R.id.progressBarDefault);
        progressBarDefaultView.setVisibility(View.GONE);

        progressBarHorizontalView = (ProgressBar) view.findViewById(R.id.progressBarHorizontal);
        progressBarHorizontalView.setProgress(0);

        progressView = (TextView) view.findViewById(R.id.progress);

        horizontalStyleView = (LinearLayout) view.findViewById(R.id.horizontalStyle);
        horizontalStyleView.setVisibility(View.GONE);

        if (style == Builder.STYLE_HORIZONTAL) {
            horizontalStyleView.setVisibility(View.VISIBLE);
        } else if (style == Builder.STYLE_SPINNER) {
            progressBarDefaultView.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        progressView.setText(progress + "%");
        progressBarHorizontalView.setProgress(progress);
    }

    @Override
    public void onResume() {
        super.onResume();
        progressView.setText(progress + "%");
        progressBarHorizontalView.setProgress(progress);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.v("ProgressDialog", "onCancel: listener = " + cancelListener);
        if (cancelListener != null) {
            cancelListener.onCancel(this);
        }
    }

    public interface OnCancelListener {

        void onCancel(ProgressDialogFragment fragment);
    }

    public static class Builder extends MessageDialogFragment.Builder {

        public static int STYLE_HORIZONTAL = 1;

        public static int STYLE_SPINNER = 2;

        private int positiveButtonVisibility = View.GONE;

        private int negativeButtonVisibility = View.GONE;

        private int style = STYLE_SPINNER;

        private OnCancelListener cancelListener;

        public Builder setPositiveButton(String title, onDialogClickListener listener) {
            super.setPositiveButton(title, listener);
            positiveButtonVisibility = View.VISIBLE;
            return this;
        }

        public Builder setNegativeButton(String title, onDialogClickListener listener) {
            super.setNegativeButton(title, listener);
            negativeButtonVisibility = View.VISIBLE;
            return this;
        }

        public Builder setProgressStyle(int style) {
            this.style = style;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener listener) {
            Log.v("ProgressDialog", "Cancel listener = " + cancelListener);
            this.cancelListener = listener;
            Log.v("ProgressDialog", "Cancel listener = " + cancelListener);
            return this;
        }
    }
}
