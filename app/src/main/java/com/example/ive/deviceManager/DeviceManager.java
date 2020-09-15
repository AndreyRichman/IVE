package com.example.ive.deviceManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Date;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;

public class DeviceManager extends android.app.Service {
    private FusedLocationProviderClient fusedLocationClient;

    public Date getTime() {
        return null;
    }

    Location currLocation;

    public void setCurrLocation(Location location) {
        currLocation = location;
    }

    public Location getLocation() {
        return currLocation;
    }

    public Location getLocationFromDevice(Activity activity) {

        //checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && //
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Location currLocation = getLocationWithPermission(activity);
            System.out.println("wd");
        } else {
            if (shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Location permission is need to show tasks related to your location", Toast.LENGTH_SHORT).show();

            }
            requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        return null;
    }

    @SuppressLint("MissingPermission")
    private Location getLocationWithPermission(Activity activity) {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, this::setCurrLocation);
        return currLocation;
    }

    ;

    public Date getDate() {
        return null;
    }

    ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
