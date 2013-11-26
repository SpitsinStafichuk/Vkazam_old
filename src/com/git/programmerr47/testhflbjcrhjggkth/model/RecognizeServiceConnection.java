package com.git.programmerr47.testhflbjcrhjggkth.model;

public class RecognizeServiceConnection {
	
	private static MicroScrobblerModel model = null;
	
	public static MicroScrobblerModel getModel() {
		if(model == null) {
			throw new IllegalStateException("You must bind to RecognizeService before calling this");
		} else {
			return model;
		}
	}
	
	public static void setModel(MicroScrobblerModel model) {
		RecognizeServiceConnection.model = model;
	}

}
