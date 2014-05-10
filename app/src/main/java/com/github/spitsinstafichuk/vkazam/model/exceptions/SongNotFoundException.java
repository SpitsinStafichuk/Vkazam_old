package com.github.spitsinstafichuk.vkazam.model.exceptions;

public class SongNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SongNotFoundException(String message) {
		super(message);
	}
	
	public SongNotFoundException() {
		super();
	}
}
