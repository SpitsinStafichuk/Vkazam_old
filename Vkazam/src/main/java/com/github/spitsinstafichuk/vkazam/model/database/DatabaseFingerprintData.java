package com.github.spitsinstafichuk.vkazam.model.database;

import java.util.Date;

import com.github.spitsinstafichuk.vkazam.model.FingerprintData;


public class DatabaseFingerprintData extends FingerprintData implements Data {

    private long id;

    private AbstractDAO dao;

    public DatabaseFingerprintData(long id, AbstractDAO dao, String fingerprint, Date date) {
        super(fingerprint, date);
        this.id = id;
        this.dao = dao;
    }

    public DatabaseFingerprintData(long id, AbstractDAO dao, FingerprintData data) {
        this(id, dao, data.getFingerprint(), data.getDate());
    }

    @Override
    public long getId() {
        return id;
    }
}
