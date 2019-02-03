package com.example.navtest1;

import android.location.Location;
import java.io.Serializable;

public class PointOfInterest implements Serializable {

        private Location loc;
        private String name;
        private String description;
        private int rating;

    public PointOfInterest(String name_in, String desc_in){
            loc = new Location("");
            name = name_in;
            description = desc_in;
            rating = 0;
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
        rating++;
    }

    public void rateDown() {
        rating--;
        checkThreshold();
    }

    public int getRating() {
        return rating;
    }

    /*
     * If the rating's too low, this POI dies.
     */
    public void checkThreshold() {
        if (rating < -10) {
            name = null;
            description = null;
        }
    }
}
