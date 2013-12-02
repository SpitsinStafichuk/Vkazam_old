package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.git.programmerr47.testhflbjcrhjggkth.R;

public class MiniPlayerFragment extends Fragment {

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mini_player_fragment, null);

        TextView songInfo = (TextView) view.findViewById(R.id.miniplayerSongInfo);
        songInfo.setSelected(true);

        return view;
    }
}
