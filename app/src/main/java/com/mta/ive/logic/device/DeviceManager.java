package com.mta.ive.logic.device;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mta.ive.MainActivity;

import java.util.Date;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;

public class DeviceManager {// extends android.app.Service {
    private FusedLocationProviderClient fusedLocationClient;
    private static DeviceManager instance;
    private Location currLocation;

    private LocationManager locationManager;

    private boolean hasLocationPermissions;

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







    public Location getLocationFromDevice(Activity activity) {

        //checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && //
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getLocationWithPermission(activity);
//            System.out.println("wd");
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(activity, "Location permission is need to show tasks related to your location", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }
        return null;
    }

    @SuppressLint("MissingPermission")
    private void getLocationWithPermission(Activity activity) {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        }

//        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        this.locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER,);
//        pullLocationManually(activity);
        //David's
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            pullLocationManually(activity);
                        }
                        else {
                            setCurrLocation(location);
                        }
                    }
                });
//                .addOnSuccessListener(activity, this::setCurrLocation);
    }

    private double latitude, longitude;
    private LocationListener locationListener;

    @SuppressLint("MissingPermission")
    private void pullLocationManually(Activity activity) {

        //Andrey's
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                updateUserLocationAndGoToHomePage(activity);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                boolean what = false;

            }

            @Override
            public void onProviderEnabled(String s) {

                boolean isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (isGPSEnabled){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location == null) {
                                        boolean please = true;
                                    }
                                    else {
                                        setCurrLocation(location);
                                    }
                                }
                            });
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
                }
                //updateUserLocationAndGoToHomePage(activity);
            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

//        if (isNetworkEnabled){
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//        }

        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGPSEnabled){
            showAlertMessageNoGps(activity);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        }
//        else {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
//
//        }

//        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);



//        final Location[] currentLocation = new Location[1];
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
//            @Override
//            public void onLocationChanged(@NonNull Location newLocation) {
//                currentLocation[0] = newLocation;
//            }
//        });
    }


    private void showAlertMessageNoGps(Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @SuppressLint("MissingPermission")
                    public void onClick(final DialogInterface dialog, final int id) {
                        Intent settings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(settings);
//                        dialog.cancel();

//                        boolean isGPSEnabled = locationManager
//                                .isProviderEnabled(LocationManager.GPS_PROVIDER);
//                        if (isGPSEnabled){
//                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
//                        }
//                        updateUserLocationAndGoToHomePage(activity);


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        updateUserLocationAndGoToHomePage(activity);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 1) {
//            switch (requestCode) {
//                case 1:
//                    break;
//            }
//        }
//    }

    private void updateUserLocationAndGoToHomePage(Activity activity){
        locationManager.removeUpdates(locationListener);
        locationListener = null;
        ((MainActivity)activity).goToHomePage();
    }

    public Date getDate() {
        return null;
    }

//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
}