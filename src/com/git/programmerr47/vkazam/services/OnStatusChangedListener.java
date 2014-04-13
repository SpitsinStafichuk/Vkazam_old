package com.git.programmerr47.vkazam.services;
import com.git.programmerr47.vkazam.model.SongData;

/**
 * Callbacks for status of recognizing
 */
public interface OnStatusChangedListener {
    /**
     * Calls when status of recognizing has changed
     * @param status - current active status
     */
    void onStatusChanged(String status);

    /**
     * Calls when result of recognizing has received
     * @param data - result of recognizing or null if result is negative
     */
    void onResultStatus(SongData data);
}
