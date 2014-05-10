package com.github.spitsinstafichuk.vkazam.services;

import com.github.spitsinstafichuk.vkazam.vos.Fingerprint;

/**
 * Wrapper that contains fingerprint and
 * client that want to recieve information
 * about it.
 *
 * @author Michael Spitsin
 * @since 2014-03-12
 */
public class FingerprintWrapper {

    public static final int RECOGNIZE_PRIORITY_HIGHEST = 3;

    public static final int RECOGNIZE_PRIORITY_HIGH = 2;

    public static final int RECOGNIZE_PRIORITY_STANDART = 1;

    public static final int RECOGNIZE_PRIORITY_LOW = 0;

    private Fingerprint fingerprint;

    private OnStatusChangedListener listener;

    //    priority of recognizing fingerprint. If there is queue on recognizing
//    low priority finger will be added to the end of queue
//    and hight priority finger will be added to the beginning of queue
    private int fingerprintPriority;

    public FingerprintWrapper(Fingerprint fingerprint, OnStatusChangedListener listener,
            int fingerprintPriority) {
        if (fingerprint == null) {
            throw new NullPointerException();
        }

        this.fingerprint = fingerprint;
        this.listener = listener;
        this.fingerprintPriority = fingerprintPriority;
    }

    public FingerprintWrapper(Fingerprint fingerprint, OnStatusChangedListener listener) {
        this(fingerprint, listener, RECOGNIZE_PRIORITY_STANDART);
    }

    public FingerprintWrapper(Fingerprint fingerprint) {
        this(fingerprint, null);
    }

    public OnStatusChangedListener getFingerprintListener() {
        return listener;
    }

    public Fingerprint getFingerprint() {
        return fingerprint;
    }

    public int getPriority() {
        return fingerprintPriority;
    }
}
