package com.example.boyao.coupletones;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

/**
 * Class to receive proximity detections by LocationTracker's location manager and deploy
 * a message when that detection occurs
 */
public class LocationMatchReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Key for determining whether user is leaving or entering
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        boolean entering = intent.getBooleanExtra(key, false);

        /*
        if (entering) {
            //send notification
            //put location hit notification here
        }
        */

    }
}
