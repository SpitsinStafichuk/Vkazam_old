package com.git.programmerr47.vkazam.model.observers;

import com.git.programmerr47.vkazam.model.FingerprintData;
import com.git.programmerr47.vkazam.model.FingerprintData;

/**
 * Listener for queue of fingerprints, that recieve notifications
 * when new element is added to queue or some finger is removed from it.
 *
 * @author Spitsin Michael
 * @since 2014-02-13
 */
public interface IFingerQueueListener {
    void addElementToQueue(FingerprintData finger);
    void removeElementFromQueue(FingerprintData finger);
    void removeRecognizedElement(FingerprintData finger);
}
