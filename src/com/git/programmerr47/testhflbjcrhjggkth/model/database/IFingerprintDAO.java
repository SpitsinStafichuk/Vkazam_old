package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.List;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.FingerprintData;

public interface IFingerprintDAO {
	List<FingerprintData> getFingerprints();
	long insert(FingerprintData songData);
	int delete(FingerprintData songData);
}
