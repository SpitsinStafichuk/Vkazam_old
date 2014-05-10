package com.github.spitsinstafichuk.vkazam.services;

import com.github.spitsinstafichuk.vkazam.VkazamApplication;
import com.github.spitsinstafichuk.vkazam.model.SongData;
import com.gracenote.mmid.MobileSDK.*;

import java.util.*;

/**
 * Base logic for working with recognizing.
 * Communicates with GraceNote service and receives answers from it.
 *
 * Only this service recognize exact fingerprints.
 * All others recognize services (like MicrophoneRecordingNowService)
 * must translate resulting fingerprint to this service.
 *
 * @author Michael Spitsin
 * @since 2014-04-12
 */
public class RecognizeFingerprintService extends StartBoundService implements GNSearchResultReady, GNOperationStatusChanged {

    public static final String STATUS_NO_CONNECTION = "STATUS_NO_CONNECTION";

    private final List<FingerprintWrapper> fingerprintQueue = new ArrayList<FingerprintWrapper>();

    private GNConfig config;
    private FingerprintWrapper currentRecognizingFingerprint = null;

    public void onCreate() {
        super.onCreate();

        config = ((VkazamApplication) getApplication()).getConfig();
    }

    /**
     * Perform recognizing of given fingerprint
     * if there is no finger that recognizing
     * or put it in queue for recognizing
     *
     * @param wrapper - wrapper that contains fingerprint that must be recognized
     */
    public void recognize(FingerprintWrapper wrapper) {
        startServiceWorking();
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

        recognizeNextFingerIfItExists();
    }

    @Override
    public void GNStatusChanged(GNStatus gnStatus) {
        currentRecognizingFingerprint.getFingerprintListener().onStatusChanged(gnStatus.getMessage());
    }

    @Override
    public void GNResultReady(GNSearchResult gnSearchResult) {
        SongData songData = null;
        if (gnSearchResult.isFailure()) {
            //TODO fix on real error code
            if (gnSearchResult.getErrCode() == 1) {
                currentRecognizingFingerprint.getFingerprintListener().onStatusChanged(STATUS_NO_CONNECTION);
            } else {
                currentRecognizingFingerprint
                        .getFingerprintListener()
                        .onStatusChanged(String.format("[%d] %s", gnSearchResult.getErrCode(), gnSearchResult.getErrMessage()));
            }
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

        recognizeNextFingerIfItExists();
    }

    /**
     * Calls recognizeNow if there is another finger is queue
     * or stops service working if there is no more fingers
     */
    private void recognizeNextFingerIfItExists() {
        if (fingerprintQueue.isEmpty()) {
            currentRecognizingFingerprint = null;
            stopWorking();
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
}
