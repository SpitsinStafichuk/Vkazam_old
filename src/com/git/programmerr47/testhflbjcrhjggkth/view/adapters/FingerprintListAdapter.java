package com.git.programmerr47.testhflbjcrhjggkth.view.adapters;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.FingerprintListController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.Data;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseFingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.utils.ImageUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FingerprintListAdapter extends BaseAdapter{
	private static String TAG = "FingerprintListAdapter";
	private FingerprintListController controller;
	private int idItem;
	private Activity activity;
	private LayoutInflater inflater;
	private MicroScrobblerModel model;
    private boolean isScrolling = false;
    private int lastPosition;
    private int countInAnimation = 0;
	
	public FingerprintListAdapter(Activity activity, int idItem, FingerprintListController controller) {
		this.activity = activity;
		this.controller = controller;
		this.idItem = idItem;
		model = RecognizeServiceConnection.getModel();
		inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		Log.v(TAG, "List count = " + model.getFingerprintList().size());
		return model.getFingerprintList().size();
	}

	@Override
	public Object getItem(int position) {
		return model.getFingerprintList().get(position);
	}
	
	private DatabaseFingerprintData getFingerprintData(int position) {
		return (DatabaseFingerprintData) getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

    @Override
    public void notifyDataSetChanged() {
        countInAnimation--;
        if (countInAnimation <= 0) {
            isScrolling = false;
            super.notifyDataSetChanged();
        }
    }

    public void scrolling() {
        isScrolling = true;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(idItem, parent, false);
		}
		DatabaseFingerprintData data = getFingerprintData(position);
		String fingerXML = data.getFingerprint();
		String finger = fingerXML.substring(fingerXML.indexOf("<FP_BLOCK"), fingerXML.indexOf("</FP_BLOCK>") + 11);
		
		TextView fingerprintText = (TextView) view.findViewById(R.id.fingerprintText);
		fingerprintText.setText(finger.hashCode() + "");
		TextView fingerprintDate = (TextView) view.findViewById(R.id.fingerprintDate);
		fingerprintDate.setText(data.getDate().toString());
		
		Bitmap coverArtBMP = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888);
		int[] colors = ImageUtils.getColors(finger);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				coverArtBMP.setPixel(i, j, colors[i * 8 + j]);
			}
		}
        BitmapDrawable coverArt = new BitmapDrawable(activity.getResources(), coverArtBMP);
        coverArt.setAntiAlias(false);
        coverArt.setDither(false);
        coverArt.setFilterBitmap(false);

		ImageView fingerCoverArt = (ImageView) view.findViewById(R.id.fingerprintImage);
        fingerCoverArt.setImageDrawable(coverArt);

        if (isScrolling) {
            if (position > lastPosition) {
                view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.list_view_up_down));
            } else {
                view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.list_view_down_up));
            }
        }
        lastPosition = position;
		
		return view;
	}

    public void deleteFingerprint(final View view, final int position) {
        final Data data = model.getFingerprintList().get(position);
        final Animation addToDequeue = AnimationUtils.loadAnimation(activity, R.anim.add_to_recognize_queue);
        addToDequeue.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                countInAnimation++;
                model.getFingerprintsDeque().addLast(data);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //try to recognize
                countInAnimation--;
                final Animation deletionAnimation = AnimationUtils.loadAnimation(activity, R.anim.complete_recognize);
                countInAnimation++;
                view.startAnimation(deletionAnimation);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        model.getFingerprintList().remove(data);
                        notifyDataSetChanged();
                        deletionAnimation.cancel();
                    }
                }, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(addToDequeue);
    }

}
