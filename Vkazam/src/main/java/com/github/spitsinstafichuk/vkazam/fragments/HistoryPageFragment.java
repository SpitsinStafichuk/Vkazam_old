package com.github.spitsinstafichuk.vkazam.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.spitsinstafichuk.vkazam.R;
import com.github.spitsinstafichuk.vkazam.model.RecognizeServiceConnection;
import com.github.spitsinstafichuk.vkazam.model.database.SongList;
import com.github.spitsinstafichuk.vkazam.model.observers.ISongDAOObserver;
import com.github.spitsinstafichuk.vkazam.utils.AndroidUtils;
import com.github.spitsinstafichuk.vkazam.activities.SongInfoActivity;
import com.github.spitsinstafichuk.vkazam.adapters.SongListAdapter;

public class HistoryPageFragment extends FragmentWithName implements
        ISongDAOObserver {

    public static final String ARGUMENT_SONGLIST_POSITION = "SongDataPosition";

    private SongListAdapter adapter;

    //private SongListController controller;
    private Activity parentActivity;

    private ListView songHLV;

    private SongList songList;

    private SharedPreferences prefs;

    private boolean firstTimeApearing;

    private LinearLayout tutorialPage;

    private LinearLayout historyPage;

    private View historyItem;

    private View miniPlayer;

    private TextView historyItemAbout;

    private TextView historyItemPlayAbout;

    private TextView miniPlayerAbout;

    private ImageView historyItemLink;

    private ImageView historyItemPlayLink;

    private ImageView miniPlayerLink;

    public static HistoryPageFragment newInstance(Context context) {
        HistoryPageFragment pageFragment = new HistoryPageFragment();
        Bundle arguments = new Bundle();
        pageFragment.setArguments(arguments);
        pageFragment.setFragmentName(context
                .getString(R.string.history_page_fragment_caption));
        pageFragment.setFragmentIcon(R.drawable.ic_action_view_as_list);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
//		controller = new SongListController(this);
        adapter = new SongListAdapter(this.getActivity(),
                R.layout.list_item_song, null);
        songList = RecognizeServiceConnection.getModel().getSongList();
        songList.addObserver(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this
                .getActivity());
        firstTimeApearing = prefs.getBoolean("HistoryPageFragmentFirstTime",
                true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, null);

        songHLV = (ListView) view.findViewById(R.id.listView);
        songHLV.setAdapter(adapter);
        songHLV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent intent = new Intent(HistoryPageFragment.this
                        .getActivity(), SongInfoActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        songHLV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
                adapter.scrolling();
            }
        });

        historyPage = (LinearLayout) view.findViewById(R.id.historyPage);
        tutorialPage = (LinearLayout) view
                .findViewById(R.id.tutorialPageHistory);
        if (firstTimeApearing) {
            tutorialPage.setVisibility(View.VISIBLE);
            tutorialPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (historyItemLink.getVisibility() == View.VISIBLE) {
                        historyItemAbout.setVisibility(View.GONE);
                        historyItemLink.setVisibility(View.GONE);

                        historyItemPlayAbout.setVisibility(View.VISIBLE);
                        historyItemPlayLink.setVisibility(View.VISIBLE);
                    } else if (historyItemPlayLink.getVisibility() == View.VISIBLE) {
                        historyItemPlayAbout.setVisibility(View.GONE);
                        historyItemPlayLink.setVisibility(View.GONE);
                        historyItem.setVisibility(View.GONE);

                        miniPlayer.setVisibility(View.VISIBLE);
                        miniPlayerAbout.setVisibility(View.VISIBLE);
                        miniPlayerLink.setVisibility(View.VISIBLE);
                    } else if (miniPlayerLink.getVisibility() == View.VISIBLE) {
                        tutorialPage.setVisibility(View.GONE);
                        AndroidUtils.setViewClickable(historyPage, true);

                        firstTimeApearing = false;
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("HistoryPageFragmentFirstTime", false);
                        editor.commit();
                    }
                }
            });
            AndroidUtils.setViewClickable(historyPage, false);

            historyItem = view.findViewById(R.id.tutorial12SongInfoLayout);
            AndroidUtils.setViewEnabled(historyItem, false);
            View tut3ResExInfo = historyItem
                    .findViewById(R.id.songHistoryItemInfo);
            if (tut3ResExInfo != null) {
                tut3ResExInfo
                        .setBackgroundResource(R.drawable.list_item_bg_default);
            }
            historyItem.setVisibility(View.VISIBLE);
            historyItemAbout = (TextView) view
                    .findViewById(R.id.tutorial1InfoAboutSong);
            historyItemAbout.setVisibility(View.VISIBLE);
            historyItemLink = (ImageView) view.findViewById(R.id.tutorial1Link);
            historyItemLink.setVisibility(View.VISIBLE);
            historyItemPlayAbout = (TextView) view
                    .findViewById(R.id.tutorial2InfoAboutPlaying);
            historyItemPlayAbout.setVisibility(View.GONE);
            historyItemPlayLink = (ImageView) view
                    .findViewById(R.id.tutorial2Link);
            historyItemPlayLink.setVisibility(View.GONE);
            miniPlayer = view.findViewById(R.id.tutorial3MiniPlayer);
            AndroidUtils.setViewEnabled(miniPlayer, false);
            miniPlayer.setVisibility(View.GONE);
            miniPlayerAbout = (TextView) view
                    .findViewById(R.id.tutorial3InfoAboutMiniPlayer);
            miniPlayerAbout.setVisibility(View.GONE);
            miniPlayerLink = (ImageView) view.findViewById(R.id.tutorial3Link);
            miniPlayerLink.setVisibility(View.GONE);

        } else {
            tutorialPage.setVisibility(View.GONE);
            AndroidUtils.setViewClickable(historyPage, true);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Fragment miniplayer = MiniPlayerFragment.newInstance(adapter);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.miniplayer, miniplayer).commit();

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        songList.removeObserver(this);
        Log.v("SongPlayer", "HistoryPageFragment onDestroy()");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
    }

    @Override
    public void onHistoryListChanged() {
        adapter.notifyDataSetChanged();
    }

}
