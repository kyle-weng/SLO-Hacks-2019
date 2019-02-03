package com.example.navtest1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GetLocation extends Activity {
        private FusedLocationProviderClient mFusedLocationClient;
        private LocationManager mLocationManager;
        private LocationListener mLocationListener;

        private boolean flag = false;
        private boolean isGPSEnabled = false;
        private boolean isNetworkEnabled = false;

        private Location location;
        private double latitude;
        private double longitude;
        private static final String TAG = "Debug";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // What will we do with this location
                        // Use the location to find the public facilities that are near this location
                    }
                }
            });
        } catch (Exception e) {};
    }

    public void onClick(View v) {
        flag = displayGpsStatus();
    }

    private boolean isLocationEnabled(Context context) {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = false;
        boolean network = false;

        try {
            gps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if (!gps && !network) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage("GPS Network Not Available!");
            dialog.setPositiveButton("Open location settings?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
        return true;
    }
    private boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext().getContentResolver();

        // Checks if GPS status is a go
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver,
                LocationManager.GPS_PROVIDER);


        if (gpsStatus) {
            return true;
        } else {
            return false;
        }
    }
}
