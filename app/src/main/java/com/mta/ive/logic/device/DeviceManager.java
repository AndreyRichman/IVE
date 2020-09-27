package com.mta.ive.logic.device;


import android.location.Location;
import android.location.LocationManager;



import java.util.Date;

public class DeviceManager {
    private static DeviceManager instance;
    private Location currLocation;

    private LocationManager locationManager;

    public static DeviceManager getInstance() {
        if (instance == null) {
            instance = new DeviceManager();
        }
        return instance;
    }

    public Date getTime() {
        return null;
    }


    public void setCurrLocation(Location location) {
        currLocation = location;
    }

    public Location getLocation() {
        return currLocation;
    }


    public Date getDate() {
        return null;
    }

}