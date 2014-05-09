
package com.git.programmerr47.vkazam.vos;

import java.util.Date;

public class Fingerprint {

    private long id;
    private String fingerprint;
    private Date date;
    private Date deletionDate;

    public Fingerprint(long id, String fingerprint, Date date, Date deletionDate) {
        super();
        this.id = id;
        this.fingerprint = fingerprint;
        this.date = date;
        this.deletionDate = deletionDate;
    }

    public Fingerprint(String fingerprint, Date date, Date deletionDate) {
        super();
        id = -1;
        this.fingerprint = fingerprint;
        this.date = date;
        this.deletionDate = deletionDate;
    }

    public long getId() {
        return id;
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

    public Date getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(Date deletionDate) {
        this.deletionDate = deletionDate;
    }

}
