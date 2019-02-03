package com.example.navtest1;

import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<PointOfInterest> locationList = new ArrayList<>();
    private FileHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*
         *Find a way to get locationList from each element in the cloud storage.
         */
        handler = new FileHandler();
        File test = handler.download("3d printing notes"); //THIS IS A STAND-IN FOR AN ACTUAL POI FILE

        // Deseralize the file.
        try {
            InputStream stream = handler.fileToInputStream(test);
            byte[] arr = handler.inputStreamToByteArray(stream);
            PointOfInterest poi = handler.deserializeBytes(arr); //throwing class not found exception? will be fixed

            locationList.add(poi);
        } catch (Exception e) {
            //Do nothing? Error fixing code tbd
        }

        //


        Intent intent = getIntent();
        Uri locationUri = intent.getData();
        // locationUri is the link for the firebase storage site?
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Map<String, PointOfInterest> bathroomLocations = new HashMap<>();
        /*
         * Take each value in the locationList and put them into the bathroomLocations map if their
         * ratings are above a certain threshold.
         */

        for (PointOfInterest location : locationList) {
            if (location.checkThreshold()) {
                bathroomLocations.put(location.getName(),
                        new PointOfInterest(location.getLoc().getLatitude(),
                                location.getLoc().getLongitude(),
                                location.getName(),
                                location.getDescription()));
            }


            /*
             * Take each value in bathroomLocations and create a new marker
             */
            for (Map.Entry<String, PointOfInterest> entry : bathroomLocations.entrySet()) {
                PointOfInterest currPOI = entry.getValue();
                Location currLocation = currPOI.getLoc();
                LatLng currLatLng = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currLatLng)
                        .title(currPOI.getName())
                        .snippet(currPOI.getDescription()));
            }

        }
    }
}