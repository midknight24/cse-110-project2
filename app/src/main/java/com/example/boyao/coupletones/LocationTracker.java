package com.example.boyao.coupletones;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;

/**
 * Works with user's locations and Location Manager to detect if user is at favorite location,
 * sending a notification via LocationMatchReceiver if a positive detection occurs
 */
public class LocationTracker extends Service implements LocationListener {

    public ArrayList<Location> favoriteLocations;

    private Location location;
    //private boolean locationAvailable;
    private double latitude;
    private double longitude;

    private static final long UPDATE_DISTANCE = 30; // 30 meters
    private static final long UPDATE_REFRESH = 1000 * 30; // 30 seconds
    private static final float DETECTION_RADIUS = 161; // 161 meters ~= 1/10 mile
    private static final long EXPIRATION_TIME = -1; //

    private final Context mContext;
    protected LocationManager locationManager;

    private ArrayList<PendingIntent> favLocationListenerIntents = null;

    /**
     * Standard LocationTracker constructor with Context param
     */
    public LocationTracker(Context ctext) {
        mContext = ctext;

        //locationAvailable = false;
        initializeTracking();
        initializeFavLocationListeners();
    }

    /**
     * Retrieves user's favorite location data from server, populating the favoriteLocations
     * member variable
     */
    public void updateFavoriteLocations(ArrayList<Location> newFavLocations) {
        favoriteLocations = newFavLocations;
        updateFavLocationListeners();
    }

    @Override
    public void onLocationChanged(Location newLocation) {
        latitude = newLocation.getLatitude();
        longitude = newLocation.getLongitude();
        location = newLocation;
    }

    @Override
    public void onStatusChanged(String a, int b, Bundle c) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    /**
     * latitude getter
     */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    /**
     * longitude getter
     */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    /**
     * favorite locations getter
     */
    public ArrayList<Location> getFavoriteLocations() {
        return favoriteLocations;
    }

    /**
     * Sets up provider to gets current device location - returning null if no providers are
     * enabled or if location permissions are not granted
     */
    public Location initializeTracking() {

        //Permissions handling - return if not granted
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( mContext, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        try   {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            longitude = 0.0;
            latitude = 0.0;

            // Check GPS/network location providers
            boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // No providers
            if (!networkEnabled && !GPSEnabled) {
                return null;
            //Use network provider
            } else if (networkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            UPDATE_REFRESH, UPDATE_DISTANCE, this);

                //locationAvailable = true;

                if (locationManager != null)   {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
            //Use GPS provider
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            UPDATE_REFRESH, UPDATE_DISTANCE, this);

                //locationAvailable = true;

                if (locationManager != null)  {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        } catch (Exception e)  {
            //PANIC!
        }
        return location;
    }

    /**
     * Creates proximity alerts for all of user's favorite locations via the Location Manager,
     * returning without initialization if permissions not granted
     */
    private void initializeFavLocationListeners() {

        //Permissions handling - return if not granted
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( mContext, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //iterate through favorite locations, creating proximity listeners
        for (Location l : favoriteLocations) {
            Intent i = new Intent(Intent.ACTION_SEND);
            PendingIntent newIntent = PendingIntent.getBroadcast(mContext,-1, i, 0);
            favLocationListenerIntents.add(newIntent);
            locationManager.addProximityAlert(l.getLatitude(), l.getLongitude(),
                                                 DETECTION_RADIUS, EXPIRATION_TIME, newIntent);
        }

    }

    /**
     * Called when favorite locations change to update notification areas
     * Clears all old listeners, then initializes new ones
     */
    private void updateFavLocationListeners() {

        //Permissions handling - return if not granted
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( mContext, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        for (PendingIntent i : favLocationListenerIntents)
            locationManager.removeProximityAlert(i);

        initializeFavLocationListeners();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
