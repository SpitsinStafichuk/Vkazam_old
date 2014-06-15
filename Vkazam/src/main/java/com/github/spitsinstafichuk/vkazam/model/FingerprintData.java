package com.github.spitsinstafichuk.vkazam.model;

import java.util.Date;

public class FingerprintData {

    protected String fingerprint;

    protected Date date;

    private boolean isInQueueForRecognizing;

    private boolean isDeleting;

    private String recognizeStatus;

    public FingerprintData(String fingerprint, Date date) {
        this.fingerprint = fingerprint;
        this.date = date;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isInQueueForRecognizing() {
        return isInQueueForRecognizing;
    }

    public void setInQueueForRecognizing(boolean b) {
        isInQueueForRecognizing = b;
    }

    public boolean isDeleting() {
        return isDeleting;
    }

    public void setDeleting(boolean isDeleting) {
        this.isDeleting = isDeleting;
    }

    public String getRecognizeStatus() {
        return recognizeStatus;
    }

    public void setRecognizeStatus(String status) {
        recognizeStatus = status;
    }
}
