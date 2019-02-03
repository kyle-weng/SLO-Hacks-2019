package com.example.navtest1;

import android.location.Location;
import java.io.Serializable;

public class PointOfInterest implements Serializable {

        private Location loc;
        private String name;
        private String description;
        private double rating;
        private int numRatings;
        private static final double THRESHOLD = -0.25;

    public PointOfInterest(double latitude, double longitude, String name_in, String desc_in){
            this.loc = new Location("");
            this.loc.setLatitude(latitude);
            this.loc.setLongitude(longitude);
            this.name = name_in;
            this.description = desc_in;
            this.rating = 0.0;
            this.numRatings = 0;
    }

    public Location getLoc() {
        return this.loc;
    }

    public void setName(String in) {
        name = in;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String in) {
        description = in;
    }

    public String getDescription() {
        return description;
    }

    public void rateUp() {
        this.rating = (this.rating * this.numRatings + 1) / (this.numRatings + 1);
        this.numRatings++;
    }

    public void rateDown() {
        this.rating = (this.rating * this.numRatings - 1) / (this.numRatings + 1);
        this.numRatings++;
        checkThreshold();
    }

    public double getRating() {
        return this.rating;
    }

    /*
     * If the rating's too low, this POI dies.
     */

    public boolean checkThreshold() {
        if (this.rating < THRESHOLD) {
            return false;
        }
        return true;
    }
    /*
    public void checkThreshold() {
        if (rating < -0.3) {
            this.loc.reset();
            this.name = null;
            this.description = null;
            this.rating = 0.0;
            this.numRatings = 0;
        }
    }
    */
}
