package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.Date;

import com.git.programmerr47.testhflbjcrhjggkth.model.FingerprintData;


public class DatabaseFingerprintData extends FingerprintData implements Data {
	
	private long id;
	private AbstractDAO dao;
	
	public DatabaseFingerprintData(long id, AbstractDAO dao, String fingerprint, Date date) {
		super(fingerprint, date);
		this.id = id;
		this.dao = dao;
	}

	@Override
	public long getId() {
		return id;
	}
}
