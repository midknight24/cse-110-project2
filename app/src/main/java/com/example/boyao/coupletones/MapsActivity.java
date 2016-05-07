package com.example.boyao.coupletones;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.location.Address;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,
        OnMyLocationButtonClickListener,
        GoogleMap.OnInfoWindowClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private GoogleApiClient mGoogleApiClient;

    private String TAG = "placeActivity";
    private MapsActivity thisActivity = this;
    private AlertDialog.Builder dialogBuilder;
    private LatLng latLng;
    private String locationName;

    private static final int MY_PERMISSIONS_REUQEST_CURRENT_LOCATION = 1;
    int PLACE_PICKER_REQUEST = 1;
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

    private String placeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady (GoogleMap googleMap){
        mMap = googleMap;

        mUiSettings = mMap.getUiSettings();

        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        enableMyLocation();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                final MarkerOptions markerOptions = new MarkerOptions();

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                //markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                String placeName;
                Geocoder location = new Geocoder(thisActivity, Locale.getDefault());
                String addressStr = "";
                try {
                    List<Address> myList = location.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    Address address = (Address) myList.get(0);
                    addressStr += address.getAddressLine(0) + ", ";
                    addressStr += address.getAddressLine(1) + ", ";
                    addressStr += address.getAddressLine(2);
                } catch( IOException e ) {

                }

                markerOptions.title(addressStr);
                markerOptions.position(latLng);
                thisActivity.latLng = latLng;
                markerOptions.snippet("Click here to add favorite location");
                Marker thisPlace = mMap.addMarker(markerOptions);
                thisPlace.showInfoWindow();
            }

        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != MY_PERMISSIONS_REUQEST_CURRENT_LOCATION) {
            return;
        }
        if (permissions.length == 1 &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
        } else {
                // Permission was denied. Display an error message.
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REUQEST_CURRENT_LOCATION);
        }
    }


    private void addFavoriteLocationDialog(final LatLng latLng) {
        dialogBuilder = new AlertDialog.Builder(this);
        final EditText nameInput = new EditText(this);

        dialogBuilder.setTitle("Give this location a name");
        dialogBuilder.setView(nameInput);

        dialogBuilder.setPositiveButton("Set the name", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send the name and the latlng to the server
                locationName = nameInput.getText().toString();
                new upLocationNameLatlng().execute();
                // show add is success
                Toast.makeText(getApplicationContext(), "Location name has been set.", Toast.LENGTH_SHORT).show();
                // get back to mapp
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialogLocationName = dialogBuilder.create();
        dialogLocationName.show();
    }


    @Override
    public boolean onMyLocationButtonClick() {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        addFavoriteLocationDialog(latLng);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void sendNameLatLng(String placeName, LatLng latlag) {

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://coupletone.zxq.io/apiv1/login.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            urlConnection.setRequestMethod("POST");

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(placeName);
            writer.flush();
            writer.write(Double.toString(latLng.latitude));
            writer.flush();
            writer.write(Double.toString(latLng.longitude));
            writer.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Error sending to backend.", e);
        }
    }

    private class upLocationNameLatlng extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            //display progress dialog.
        }

        protected Void doInBackground(Void... params) {
            sendNameLatLng(locationName, latLng);
            return null;
        }

        protected void onPostExecute(Void result) {
            // dismiss progress dialog and update ui
        }
    };
}
