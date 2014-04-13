package com.git.programmerr47.vkazam.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.git.programmerr47.vkazam.VkazamApplication;
import com.git.programmerr47.vkazam.model.SongData;
import com.gracenote.mmid.MobileSDK.*;

import java.util.*;

/**
 * Base logic for working with recognizing.
 * Communicates with GraceNote service and receives answers from it.
 *
 * Only this service recognize exact fingerprints.
 * All others recognize services (like MicrophoneRecordingService)
 * must translate resulting fingerprint to this service.
 *
 * @author Michael Spitsin
 * @since 2014-04-12
 */
public class RecognizeFingerprintService extends Service implements GNSearchResultReady, GNOperationStatusChanged {

    public static final String STATUS_NO_CONNECTION = "STATUS_NO_CONNECTION";

    // Binder given to clients
    private final IBinder recognizeFingerprintBinder = new RecognizeFingerprintBinder();
    private final List<FingerprintWrapper> fingerprintQueue = new ArrayList<FingerprintWrapper>();
    private final GNConfig config = ((VkazamApplication) getApplication()).getConfig();

    private FingerprintWrapper currentRecognizingFingerprint = null;

    @Override
    public IBinder onBind(Intent intent) {
        return recognizeFingerprintBinder;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Perform recognizing of given fingerprint
     * if there is no finger that recognizing
     * or put it in queue for recognizing
     *
     * @param wrapper - wrapper that contains fingerprint that must be recognized
     */
    public void recognize(FingerprintWrapper wrapper) {
        if (currentRecognizingFingerprint != null) {
            addFingerprintWrapperToQueue(wrapper);
        } else {
            if (fingerprintQueue.isEmpty() || fingerprintQueue.get(0).getPriority() < wrapper.getPriority()) {
                recognizeNow(wrapper);
            } else {
                addFingerprintWrapperToQueue(wrapper);
                recognizeNow(fingerprintQueue.get(0));
                fingerprintQueue.remove(fingerprintQueue.get(0));
            }
        }
    }

    /**
     * Cancel current recognizing if given fingerprint is send to GraceNote
     * or remove it from recognizing queue
     *
     * If this fingerprint not found nothing happens
     *
     * @param wrapper - wrapper that contains fingerprint that must be canceled
     */
    public void cancel(FingerprintWrapper wrapper) {
        if (wrapper == currentRecognizingFingerprint) {
            GNOperations.cancel(this);
        } else if (fingerprintQueue.contains(currentRecognizingFingerprint)) {
            fingerprintQueue.remove(currentRecognizingFingerprint);
        }
    }

    @Override
    public void GNStatusChanged(GNStatus gnStatus) {
        currentRecognizingFingerprint.getFingerprintListener().onStatusChanged(gnStatus.getMessage());
    }

    @Override
    public void GNResultReady(GNSearchResult gnSearchResult) {
        SongData songData = null;
        if (gnSearchResult.isFailure()) {
            //TODO dsfsgagas
            currentRecognizingFingerprint
                    .getFingerprintListener()
                    .onStatusChanged(String.format("[%d] %s", gnSearchResult.getErrCode(), gnSearchResult.getErrMessage()));
        } else {
            if (!gnSearchResult.isFingerprintSearchNoMatchStatus()) {
                GNSearchResponse bestResponse = gnSearchResult.getBestResponse();

                String artist = bestResponse.getArtist();
                String title = bestResponse.getTrackTitle();
                String album = bestResponse.getAlbumTitle();
                String trackId = bestResponse.getTrackId();
                String coverArtURL = bestResponse.getCoverArt() != null ?
                        bestResponse.getCoverArt().getUrl() : null;
                String contributorImageURL = bestResponse.getContributorImage() != null ?
                        bestResponse.getContributorImage().getUrl() : null;
                String albumArtist = bestResponse.getAlbumArtist();
                String artistBiographyURL = bestResponse.getArtistBiographyUrl();
                String songPosition = bestResponse.getSongPosition();
                String albumReviewUrl = bestResponse.getAlbumReviewUrl();
                String albumReleaseYear = bestResponse.getAlbumReleaseYear();

                songData = new SongData(null, null, trackId, artist, album, title, null, null, coverArtURL, new Date(), contributorImageURL,
                        artistBiographyURL, songPosition, albumReviewUrl, albumReleaseYear, albumArtist);
            }

            currentRecognizingFingerprint.getFingerprintListener().onResultStatus(songData);
        }

        if (fingerprintQueue.isEmpty()) {
            currentRecognizingFingerprint = null;
        } else {
            recognizeNow(fingerprintQueue.get(0));
            fingerprintQueue.remove(fingerprintQueue.get(0));
        }
    }

    private void addFingerprintWrapperToQueue(FingerprintWrapper wrapper) {
        int index = fingerprintQueue.size() - 1;
        while ((index > -1) && (fingerprintQueue.get(index).getPriority() < wrapper.getPriority())) {
            index--;
        }

        fingerprintQueue.add(index + 1, wrapper);
    }

    private void recognizeNow(FingerprintWrapper wrapper) {
        currentRecognizingFingerprint = wrapper;
        GNOperations.searchByFingerprint(this, config, wrapper.getFingerprintData().getFingerprint());
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class RecognizeFingerprintBinder extends Binder {

        /**
         * @return instance of RecognizeFingerprintService so clients can call public methods
         */
        RecognizeFingerprintService getService() {
            return RecognizeFingerprintService.this;
        }
    }
}
