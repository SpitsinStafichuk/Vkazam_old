package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.interfaces.IConnectedDialogFragmentDissmised;

public class MessageDialogFragment extends DialogFragment{
    public static final String TAG = "MessageDialog";

	private int icon;
	private String title;
	private String message;
	private String positiveButtonTitle;
	private String negativeButtonTitle;
	private onDialogClickListener positiveListener;
	private onDialogClickListener negativeListener;

    private IConnectedDialogFragmentDissmised mListener;

    public static MessageDialogFragment newInstance(Builder builder) {
    	MessageDialogFragment fragment = new MessageDialogFragment();
    	fragment.icon = builder.icon;
    	fragment.title = builder.title;
    	fragment.message = builder.message;
    	fragment.positiveButtonTitle = builder.positiveButtonTitle;
    	fragment.negativeButtonTitle = builder.negativeButtonTitle;
    	fragment.positiveListener = builder.positiveListener;
    	fragment.negativeListener = builder.negativeListener;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (IConnectedDialogFragmentDissmised)activity;
        }
        catch (final ClassCastException e) {
        	this.mListener = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_dialog, container, false);
        
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        if (icon != -1) {
        	iconView.setImageResource(icon);
        }
        
        TextView titleView = (TextView) view.findViewById(R.id.title);
        if (title != null) {
        	titleView.setText(title);
        }
        
        TextView messageView = (TextView) view.findViewById(R.id.message);
        if (message != null) {
        	messageView.setText(message);
        }
        
        TextView positiveButtonTitleView = (TextView) view.findViewById(R.id.positiveButtonTitle);
        if (positiveButtonTitle != null) {
        	positiveButtonTitleView.setText(positiveButtonTitle);
        }
        
        TextView negativeButtonTitleView = (TextView) view.findViewById(R.id.negativeButtonTitle);
        if (negativeButtonTitle != null) {
        	negativeButtonTitleView.setText(negativeButtonTitle);
        }

        View negativeButton = view.findViewById(R.id.negativeButton);
        negativeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (negativeListener != null) {
					negativeListener.onDialogClick(MessageDialogFragment.this, v);
				}
			}
		});

        View positiveButton = view.findViewById(R.id.positiveButton);
        positiveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (positiveListener != null) {
					positiveListener.onDialogClick(MessageDialogFragment.this, v);
				}
			}
		});

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onComplete();
        }
    }
    
    public interface onDialogClickListener {
    	void onDialogClick(DialogFragment fragment, View view);
    }
    
    public static class Builder {
    	private int icon = -1;
    	private String title;
    	private String message;
    	private String positiveButtonTitle;
    	private String negativeButtonTitle;
    	private onDialogClickListener positiveListener;
    	private onDialogClickListener negativeListener;
    	
        public Builder() {
        }
        
        public Builder setIcon(int icon) {
            this.icon = icon;
            return this;
        }
        
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }
        
        public Builder setPositiveButton(String title, onDialogClickListener listener) {
            this.positiveButtonTitle = title;
            this.positiveListener = listener;
            return this;
        }
        
        public Builder setNegativeButton(String title, onDialogClickListener listener) {
            this.negativeButtonTitle = title;
            this.negativeListener = listener;
            return this;
        }

    }
}

