package com.example.boyao.coupletones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final Button mapButton = (Button) findViewById(R.id.MapButton);

        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                startActivity(new Intent(MainMenuActivity.this, MapsActivity.class));
            }
        });

        final Button partnerButton = (Button) findViewById(R.id.partnerButton);

        partnerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                startActivity(new Intent(MainMenuActivity.this, FindPartnerActivity.class));
            }
        });

        final Button signOutButton = (Button) findViewById(R.id.signOutButton);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                /*GoogleApiClient mGoogleApiClient = GlobalAccess.getInstance(null).getClient();

                Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // [START_EXCLUDE]
                                //updateUI(false);
                                // [END_EXCLUDE]
                            }
                        });*/

                startActivity(new Intent(MainMenuActivity.this, GoogleSigninActivity.class));
            }
        });
    }

}
