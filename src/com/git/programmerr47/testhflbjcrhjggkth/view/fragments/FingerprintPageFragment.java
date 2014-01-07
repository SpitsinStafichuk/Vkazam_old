package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.FingerprintListController;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.FingerprintList;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintDAOObserver;
import com.git.programmerr47.testhflbjcrhjggkth.utils.NetworkUtils;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.FingerprintListAdapter;

public class FingerprintPageFragment extends FragmentWithName implements IFingerprintDAOObserver, CompoundButton.OnCheckedChangeListener {
	private FingerprintListAdapter adapter;
    private FingerprintListController controller;

    private ListView fingerprintHLV;
    private FingerprintList fingerprintList;

    private LinearLayout autoRecognize;
    private CheckBox autoRecognizeCheckBox;

	public static FingerprintPageFragment newInstance(Context context) {
		FingerprintPageFragment pageFragment = new FingerprintPageFragment();
        Bundle arguments = new Bundle();
        pageFragment.setArguments(arguments);
        pageFragment.setFragmentName("Fingers");
        pageFragment.setFragmentIcon(R.drawable.ic_action_fingerprint);
        pageFragment.setContext(context);
        return pageFragment;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            
            adapter = new FingerprintListAdapter(this.getActivity(), R.layout.finger_list_item);
            controller = new FingerprintListController(this, adapter);
            fingerprintList = RecognizeServiceConnection.getModel().getFingerprintList();
            fingerprintList.addObserver(this);
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("Fragments", "FingerprintPageFragment: onCreateView");
        View view = inflater.inflate(R.layout.fingerprints_fragment, null);

        fingerprintHLV = (ListView) view.findViewById(R.id.listView);
        fingerprintHLV.setAdapter(adapter);
        controller.setListView(fingerprintHLV);
        fingerprintHLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (NetworkUtils.isNetworkAvailable(FingerprintPageFragment.this.getActivity())) {
                    Log.v("Figers", "Perform click: " + view + "; " + position);
                    adapter.recognizeFingerprint(view, position);
                }  else {
                    Toast.makeText(FingerprintPageFragment.this.getActivity(), "Network is not available at this moment", Toast.LENGTH_LONG).show();
                }
            }
        });
        fingerprintHLV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                adapter.scrolling();
            }
        });

        autoRecognize = (LinearLayout) view.findViewById(R.id.settingsAutoRecognize);
        autoRecognizeCheckBox = (CheckBox) autoRecognize.findViewById(R.id.checkbox);
        autoRecognizeCheckBox.setOnCheckedChangeListener(this);
        autoRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoRecognizeCheckBox.setChecked(!autoRecognizeCheckBox.isChecked());
                if (autoRecognizeCheckBox.isChecked()) {
                    controller.startRecognizingIfDequeIsEmpty();
                }
            }
        });

        return view;
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v("Fragments", "FingerprintPageFragment: onDestroyView");
        controller.setListView(null);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        autoRecognizeCheckBox.setChecked(prefs.getBoolean("settingsAutoRecognize", false));
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	controller.finish();
    	controller.setListView(null);
    }

	@Override
	public void onFingerprintListChanged() {
		adapter.notifyDataSetChanged();
	}

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        SharedPreferences.Editor editor=prefs.edit();
        editor.putBoolean("settingsAutoRecognize", b);
        editor.commit();
    }
}
