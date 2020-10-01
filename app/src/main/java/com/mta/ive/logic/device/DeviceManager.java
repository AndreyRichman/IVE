package com.mta.ive.logic.device;


import android.location.Location;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

public class DeviceManager {
    private static DeviceManager instance;
    private Location deviceLocation;

    public static DeviceManager getInstance() {
        if (instance == null) {
            instance = new DeviceManager();
        }
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalTime getTime() {

        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentMinute = rightNow.get(Calendar.MINUTE);

        LocalTime time = LocalTime.MIN.plusHours(currentHourIn24Format).plusMinutes(currentMinute);
        return time;
    }

    public void setDeviceLocation(Location location) {
        deviceLocation = location;
    }

    public Location getDeviceLocation() {
        return deviceLocation;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getDate() {
        Calendar rightNow = Calendar.getInstance();
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH);
        int day = rightNow.get(Calendar.DAY_OF_MONTH);

        LocalDate todayDate = LocalDate.MIN.plusYears(year).plusMonths(month).minusDays(day);

        return todayDate;
    }

}