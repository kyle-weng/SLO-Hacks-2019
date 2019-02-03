package com.example.navtest1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GetLocation extends Activity {
        private FusedLocationProviderClient mFusedLocationClient;
        private final Context mContext;
        private LocationManager mLocationManager;
        private LocationListener mLocationListener;

        private boolean flag = false;
        private boolean isGPSEnabled = false;
        private boolean isNetworkEnabled = false;
        private boolean canGetLocation = false;

        private Location location;
        private double latitude;
        private double longitude;

        private static final long MIN_DISTANCE_CHANGE = 10; // 10 meters
        private static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 15; // 15 seconds

    public GetLocation(Context context) {
        this.mContext = context;
        this.location = getLocation();
    }

    /**
     * Determines if GPS/Network is available.
     * Sets Location to Network (if available) then to GPS (if available)
     * GPS has precedence.
     * Alerts the user if GPS and Network are unavailable.
     *
     * @return A Location variable (use .getLatitude and .getLongitude to get details of Location)
     */
    public Location getLocation() {
        try {
            mLocationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = mLocationManager.isProviderEnabled(mLocationManager.GPS_PROVIDER);
            isNetworkEnabled = mLocationManager.isProviderEnabled(mLocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this.mContext);
                dialog.setMessage("GPS Network Not Available!");
            } else {
                this.canGetLocation = true;

                // Find location via network
                if (isNetworkEnabled) {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_CHANGE, mLocationListener);
                    Log.d("Network", "Network");
                    if (mLocationManager != null) {
                        this.location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                // Find location via GPS
                if (isGPSEnabled) {
                    if (location == null) {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BETWEEN_UPDATES,
                                MIN_DISTANCE_CHANGE, mLocationListener);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (mLocationManager != null) {
                            this.location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (SecurityException e){
            e.printStackTrace();
        }

        return this.location;
    }

    public double getLatitude() {
        if (this.location != null) {
            this.latitude = this.location.getLatitude();
        }

        return this.latitude;
    }

    public double getLongitude() {
        if (this.location != null) {
            this.longitude = this.location.getLongitude();
        }

        return this.longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLocation gps = new GetLocation(GetLocation.this);
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                }
            }
        });
    }

    public void onClick(View v) {
        flag = isGPSEnabled();
    }


    public boolean isGPSEnabled() {
        return this.isGPSEnabled;
    }

    public boolean isNetworkEnabled() {
        return this.isNetworkEnabled;
    }

}
