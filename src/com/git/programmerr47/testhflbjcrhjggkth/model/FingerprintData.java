package com.git.programmerr47.testhflbjcrhjggkth.model;

import java.util.Date;

public class FingerprintData {
	
	protected String fingerprint;
	protected Date date;
	
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
}
