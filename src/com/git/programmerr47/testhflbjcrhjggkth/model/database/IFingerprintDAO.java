package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.List;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.IFingerprintData;

public interface IFingerprintDAO {
	List<IFingerprintData> getFingerprints();
	long insert(IFingerprintData songData);
	int delete(IFingerprintData songData);
}
