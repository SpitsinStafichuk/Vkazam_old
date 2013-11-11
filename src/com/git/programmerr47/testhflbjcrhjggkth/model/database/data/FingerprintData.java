package com.git.programmerr47.testhflbjcrhjggkth.model.database.data;

public class FingerprintData implements IFingerprintData {
	
	private long id;
	private String fingerprint;
	private String date;
	
	public FingerprintData(long id, String fingerprint, String date) {
		this.id = id;
		this.fingerprint = fingerprint;
		this.date = date;
	}

	@Override
	public String getFingerprint() {
		return fingerprint;
	}

	@Override
	public String getDate() {
		return date;
	}

	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof IFingerprintData) {
				IFingerprintData oData = (IFingerprintData) o;
				return this.date == oData.getDate();
			}
		}
		return false;
	}

}
