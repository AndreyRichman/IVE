package com.mta.ive.logic.device;


import android.location.Location;

import java.util.Date;

public class DeviceManager {
    private static DeviceManager instance;
    private Location deviceLocation;

    public static DeviceManager getInstance() {
        if (instance == null) {
            instance = new DeviceManager();
        }
        return instance;
    }

    public Date getTime() {
        return null;
    }


    public void setDeviceLocation(Location location) {
        deviceLocation = location;
    }

    public Location getDeviceLocation() {
        return deviceLocation;
    }


    public Date getDate() {
        return null;
    }

}