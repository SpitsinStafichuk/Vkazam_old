package com.git.programmerr47.testhflbjcrhjggkth.model.database.data;

public class FingerprintData {
	
	private long id;
	private String fingerprint;
	private String date;
	
	public FingerprintData(FingerprintDataBuilder builder) {
		this.id = builder.id;
		this.fingerprint = builder.fingerprint;
		this.date = builder.date;
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

	public static class FingerprintDataBuilder implements Builder<FingerprintData> {

		private long id;
		private String fingerprint;
		private String date;
		
		
		
		@Override
		public FingerprintData build() {
			return new FingerprintData(this);
		}
		
		public FingerprintDataBuilder setId(long id) {
			this.id = id;
			return this;
		}
		
		public FingerprintDataBuilder setFingerprint(String fingerprint) {
			this.fingerprint = fingerprint;
			return this;
		}
		
		public FingerprintDataBuilder setDate(String date) {
			this.date = date;
			return this;
		}
	}
}
