package com.git.programmerr47.testhflbjcrhjggkth.model.database;


public class FingerprintData extends Data{
	
	private String fingerprint;
	
	public FingerprintData(FingerprintDataBuilder builder) {
		this.id = builder.id;
		this.fingerprint = builder.fingerprint;
		this.date = builder.date;
		this.dao = builder.dao;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public static class FingerprintDataBuilder implements Builder<FingerprintData> {

		private long id;
		private String fingerprint;
		private long date;
		private FingerprintDAO dao;
		
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
		
		public FingerprintDataBuilder setDate(long date) {
			this.date = date;
			return this;
		}
		
		FingerprintDataBuilder setFingerprintDAO(FingerprintDAO dao) {
			this.dao = dao;
			return this;
		}
	}
}
