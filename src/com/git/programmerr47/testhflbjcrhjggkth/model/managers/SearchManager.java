package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.gracenote.mmid.MobileSDK.GNOperations;
public class SearchManager {
        
        public interface SearchListener {
                void onSearchStatusChanged(String status);
                void onSearchResult(SongData songData);
        }
        
        private static final String TAG = "SearchManager";
        
        private GNConfig config;
        
        public SearchManager(GNConfig config) {
                this.config = config;
        }
        
        public void search(String artist, String album, String title, SearchListener listener) {
                Log.i(TAG, "search by: " + artist + " - " + title + " from album " + album);
                GNOperations.searchByText(new GNSearchResultReadyImplementation(listener), config, artist, album, title);
        }
        
        public void search(String trackId, SearchListener listener) {
                Log.i(TAG, "search by trackId: " + trackId);
                GNOperations.fetchByTrackId(new GNSearchResultReadyImplementation(listener), config, trackId);
        }
}