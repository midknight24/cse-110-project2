package com.example.boyao.coupletones;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by kevin on 5/3/2016.
 */
public class GlobalAccess extends Application {
    private static GoogleApiClient mGoogleApiClient;
    private static GlobalAccess mInstance;

    protected GlobalAccess() {}

    public static GlobalAccess getInstance(GoogleApiClient aGoogleApiClient) {
        if(mInstance == null) {
            mInstance = new GlobalAccess();
            if(mGoogleApiClient == null)
                mGoogleApiClient = aGoogleApiClient;
        }
        return mInstance;
    }

    public GoogleApiClient getClient(){
        return mGoogleApiClient;
    }
}
