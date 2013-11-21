package com.git.programmerr47.testhflbjcrhjggkth.model.database.data;

public class FingerprintData extends Data{
	
	private String fingerprint;
	
	public FingerprintData(FingerprintDataBuilder builder) {
		this.id = builder.id;
		this.fingerprint = builder.fingerprint;
		this.date = builder.date;
	}

	public String getFingerprint() {
		return fingerprint;
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
