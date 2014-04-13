package com.git.programmerr47.vkazam.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service that manages history of fingers
 * and then send them to RecognizeFingerprintService
 *
 * @author Michael Spitsin
 * @since 2014-04-13
 */
public class RecognizeHistoryService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
