
package com.github.spitsinstafichuk.vkazam.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.spitsinstafichuk.vkazam.R;
import com.github.spitsinstafichuk.vkazam.model.MicroScrobblerModel;
import com.github.spitsinstafichuk.vkazam.model.RecognizeServiceConnection;
import com.github.spitsinstafichuk.vkazam.model.SongData;
import com.github.spitsinstafichuk.vkazam.model.database.FingerprintList;
import com.github.spitsinstafichuk.vkazam.model.observers.IFingerprintDAOObserver;
import com.github.spitsinstafichuk.vkazam.model.observers.IRecognizeResultObserver;
import com.github.spitsinstafichuk.vkazam.utils.AndroidUtils;
import com.github.spitsinstafichuk.vkazam.utils.NetworkUtils;
import com.github.spitsinstafichuk.vkazam.adapters.FingerprintListAdapter;

public class FingerprintPageFragment extends FragmentWithName implements
        IFingerprintDAOObserver, CompoundButton.OnCheckedChangeListener, IRecognizeResultObserver {

    private static final String TAG = "FingerprintPageFragment";

    private FingerprintListAdapter adapter;

    private MicroScrobblerModel model;

    private CheckBox autoRecognizeCheckBox;

    private ImageView tutorial1Link;

    private ImageView tutorial2Link;

    private ImageView tutorial3Link;

    private TextView tutorial1Recognize;

    private TextView tutorial2RecognizeResult;

    private TextView tutorial3AutoRecognize;

    private View tutorial3AutoRecognizeLayout;

    private View tutorial12FingerLayout;

    private LinearLayout tutorialPage;

    private LinearLayout fingerPage;

    private boolean firstTimeAppearing;

    private SharedPreferences preferences;

    public static FingerprintPageFragment newInstance(Context context) {
        FingerprintPageFragment pageFragment = new FingerprintPageFragment();
        Bundle arguments = new Bundle();
        pageFragment.setArguments(arguments);
        pageFragment.setFragmentName(context
                .getString(R.string.fingerprints_page_fragment_caption));
        pageFragment.setFragmentIcon(R.drawable.ic_action_fingerprint);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        adapter = new FingerprintListAdapter(getActivity(),
                R.layout.list_item_finger);
        model = RecognizeServiceConnection.getModel();
        model.getRecognizeListManager().addRecognizeResultObserver(this);
        FingerprintList fingerprintList = RecognizeServiceConnection.getModel()
                .getFingerprintList();
        fingerprintList.addObserver(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        firstTimeAppearing = preferences.getBoolean("FingersPageFragmentFirstTime",
                true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.v(TAG, "FingerprintPageFragment: onCreateView");
        View view = inflater.inflate(R.layout.fragment_fingerprints, null);

        ListView fingerprintHLV = (ListView) view.findViewById(R.id.listView);
        fingerprintHLV.setAdapter(adapter);
        adapter.setListView(fingerprintHLV);
        fingerprintHLV
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
                        if (NetworkUtils
                                .isNetworkAvailable(FingerprintPageFragment.this
                                        .getActivity())) {
                            Log.v(TAG, "Perform click: " + view + "; "
                                    + position);
                            adapter.recognizeFingerprint(view, position);
                        } else {
                            Toast.makeText(
                                    FingerprintPageFragment.this.getActivity(),
                                    getString(R.string.network_not_available),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
        fingerprintHLV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
                adapter.scrolling();
            }
        });

        LinearLayout autoRecognize = (LinearLayout) view
                .findViewById(R.id.settingsAutoRecognize);
        autoRecognizeCheckBox = (CheckBox) autoRecognize
                .findViewById(R.id.checkbox);
        autoRecognizeCheckBox.setOnCheckedChangeListener(this);
        autoRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoRecognizeCheckBox.setChecked(!autoRecognizeCheckBox
                        .isChecked());
                if (NetworkUtils
                        .isNetworkAvailable(FingerprintPageFragment.this
                                .getActivity())) {
                    if (autoRecognizeCheckBox.isChecked()) {
                        RecognizeServiceConnection.getModel()
                                .getRecognizeListManager()
                                .recognizeFingerprints();
                    } else {
                        RecognizeServiceConnection.getModel()
                                .getRecognizeListManager()
                                .cancelAutoRecognize();
                    }
                }
            }
        });

        fingerPage = (LinearLayout) view.findViewById(R.id.fingersPage);
        tutorialPage = (LinearLayout) view.findViewById(R.id.tutorialPage);
        if (firstTimeAppearing) {
            tutorialPage.setVisibility(View.VISIBLE);
            tutorialPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tutorial1Link.getVisibility() == View.VISIBLE) {
                        tutorial1Link.setVisibility(View.GONE);
                        tutorial1Recognize.setVisibility(View.GONE);

                        tutorial2Link.setVisibility(View.VISIBLE);
                        tutorial2RecognizeResult.setVisibility(View.VISIBLE);
                    } else if (tutorial2Link.getVisibility() == View.VISIBLE) {
                        tutorial2Link.setVisibility(View.GONE);
                        tutorial2RecognizeResult.setVisibility(View.GONE);
                        tutorial12FingerLayout.setVisibility(View.GONE);

                        tutorial3AutoRecognizeLayout
                                .setVisibility(View.VISIBLE);
                        tutorial3AutoRecognize.setVisibility(View.VISIBLE);
                        tutorial3Link.setVisibility(View.VISIBLE);
                    } else if (tutorial3Link.getVisibility() == View.VISIBLE) {
                        tutorialPage.setVisibility(View.GONE);
                        AndroidUtils.setViewEnabled(fingerPage, true);

                        firstTimeAppearing = false;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("FingersPageFragmentFirstTime", false);
                        editor.commit();
                    }
                }
            });
            AndroidUtils.setViewEnabled(fingerPage, false);

            tutorial12FingerLayout = view
                    .findViewById(R.id.tutorial12FingerLayout);
            AndroidUtils.setViewEnabled(tutorial12FingerLayout, false);
            AndroidUtils.setViewClickable(tutorial12FingerLayout, false);
            tutorial12FingerLayout.setVisibility(View.VISIBLE);
            tutorial3AutoRecognizeLayout = view
                    .findViewById(R.id.tutorial3AutoRecognizeLayout);
            AndroidUtils.setViewClickable(tutorial3AutoRecognizeLayout, false);
            tutorial3AutoRecognizeLayout.setVisibility(View.INVISIBLE);
            tutorial1Recognize = (TextView) view
                    .findViewById(R.id.tutorial1FingerRecognize);
            tutorial1Recognize.setVisibility(View.VISIBLE);
            tutorial2RecognizeResult = (TextView) view
                    .findViewById(R.id.tutorial2FingerRecognizeResult);
            tutorial2RecognizeResult.setVisibility(View.INVISIBLE);
            tutorial3AutoRecognize = (TextView) view
                    .findViewById(R.id.tutorial3FingerAutoRecognize);
            tutorial3AutoRecognize.setVisibility(View.INVISIBLE);
            tutorial1Link = (ImageView) view.findViewById(R.id.tutorial1Link);
            tutorial1Link.setVisibility(View.VISIBLE);
            tutorial2Link = (ImageView) view.findViewById(R.id.tutorial2Link);
            tutorial2Link.setVisibility(View.INVISIBLE);
            tutorial3Link = (ImageView) view.findViewById(R.id.tutorial3Link);
            tutorial3Link.setVisibility(View.INVISIBLE);

        } else {
            tutorialPage.setVisibility(View.GONE);
            AndroidUtils.setViewEnabled(fingerPage, true);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "FingerprintPageFragment: onDestroyView");
        adapter.setListView(null);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        autoRecognizeCheckBox.setChecked(prefs.getBoolean(
                "settingsAutoRecognize", false));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.getRecognizeListManager().removeRecognizeResultObserver(this);
        adapter.finish();
    }

    @Override
    public void onFingerprintListChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("settingsAutoRecognize", b);
        editor.commit();
    }

    @Override
    public void onRecognizeResult(int errorCode, SongData songData) {
        Log.v("Fingers", "onRecognizeResult " + songData);
        if (songData != null) {
            model.getScrobbler().sendLastFMTrack(songData.getArtist(), songData.getTitle(),
                    songData.getAlbum());
        }
    }
}
