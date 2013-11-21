package com.git.programmerr47.testhflbjcrhjggkth.model.database.data;

public class Data {

	protected long id;
	protected String date;
	
	public String getDate() {
		return date;
	}

	public long getId() {
		return id;
	}

	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof Data) {
				Data oData = (Data) o;
				return this.date == oData.getDate();
			}
		}
		return false;
	}
}
