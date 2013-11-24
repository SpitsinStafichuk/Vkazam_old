package com.git.programmerr47.testhflbjcrhjggkth.model.database;


public class Data {

	protected long id = -1;
	protected long date = -1;
	protected AbstractDAO dao;
	
	public long getDate() {
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
