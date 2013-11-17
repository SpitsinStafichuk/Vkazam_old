package com.git.programmerr47.testhflbjcrhjggkth.model.database.data;

public class FingerprintData {
	
	private long id;
	private String fingerprint;
	private String date;
	
	public FingerprintData(long id, String fingerprint, String date) {
		this.id = id;
		this.fingerprint = fingerprint;
		this.date = date;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public String getDate() {
		return date;
	}

	public long getId() {
		return id;
	}
	
	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof FingerprintData) {
				FingerprintData oData = (FingerprintData) o;
				return this.date == oData.getDate();
			}
		}
		return false;
	}

}
