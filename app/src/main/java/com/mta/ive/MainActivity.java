package com.mta.ive;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.users.User;
//import com.mta.ive.pages.delete.AllTasksActivity;
import com.mta.ive.pages.home.HomeActivity;


public class MainActivity extends AppCompatActivity {


    Class<HomeActivity> homeAcivity;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationListener locationListener;
    private LocationManager locationManager;


    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

        String email = getIntent().getStringExtra("email");
        String userName = getIntent().getStringExtra("userName");

        homeAcivity = HomeActivity.class;
        LogicHandler.createUserIfNotExist(email, userName);
        loadUserFromDB();
    }

    private void loadUserFromDB() {
        String userEmail = LogicHandler.getCurrentUserEmail();
        DatabaseReference userReference = LogicHandler.getCurrentUserDBReferenceById(userEmail);

        userReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);
                LogicHandler.setCurrentUser(currentUser);
                LogicHandler.loadUserLocationsAndTasksMaps();

//                loadDeviceLocation();
//                goToHomePage();
                fetchLocationAndGoToHomePage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

//    public void loadDeviceLocation(){
//        LogicHandler.loadDeviceLocation(this);
//    }

    public void goToHomePage(){
        Intent homePage = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(homePage);//, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }


    private void fetchLocationAndGoToHomePage(){
        boolean hasPermissionsToLocations = hasPermissionsToLocations();

        if (hasPermissionsToLocations){
//            getDeviceLocationAndGoToHomePage();
            requestLocationWithGooglePermissions();
        }
        else {  //don't have permissions -> request them!
            if (shouldRequestForPermissions()){
                //request for permissions
                requestForPermissions();
            }
             requestLocationWithGooglePermissions();
//            else if (true){
//                requestGooglePermissions();
//            }
//            else {
//                //proceed to app without location
//                goHomePageWithoutLocation("No Location available");
//            }
        }

    }

    private final int LOCATION_SETTINGS_REQUEST_CODE = 77;
    private final int REQUEST_CHECK_SETTINGS = 79;
    private void requestLocationWithGooglePermissions() {
//        System.out.println("Test running setting request" );
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                boolean usable = locationSettingsResponse.getLocationSettingsStates().isGpsUsable();
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
//                getDeviceLocation();
//                createLocationRequest();
                boolean locationUsable = locationSettingsResponse.getLocationSettingsStates().isLocationUsable();

                if (locationUsable){
//                    getDeviceLocationAndGoToHomePage();
                    retryGettingDeviceLocationAfterEnablingSettings();
//                    getLocation();
                }
                else {
                    goHomePageWithoutLocation("Location is not available");
//                    try {
//                        // Show the dialog by calling startResolutionForResult(),
//                        // and check the result in onActivityResult().
////                            status.startResolutionForResult(this, LOCATION_SETTINGS_REQUEST_CODE);
//                        startIntentSenderForResult( status.getResolution().getIntentSender(), LOCATION_SETTINGS_REQUEST_CODE, null, 0, 0, 0, null);
//                        System.out.println("Test setting not met starting dialog to prompt user" );
//                    } catch (IntentSender.SendIntentException e) {
//                        // Ignore the error.
//                    }
                }



            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

//        PendingResult<LocationSettingsResult> result = LocationServices.getS
//                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(LocationSettingsResult result) {
//                final Status status = result.getStatus();
//                final LocationSettingsStates state = result.getLocationSettingsStates();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        // All location settings are satisfied.
//                        System.out.println("Test setting all fine starting location request" );
//                        getLocation();
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        // Location settings are not satisfied. But could be fixed by showing the user
//                        // a dialog.
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
////                            status.startResolutionForResult(this, LOCATION_SETTINGS_REQUEST_CODE);
//                            startIntentSenderForResult(status.getResolution().getIntentSender(), LOCATION_SETTINGS_REQUEST_CODE, null, 0, 0, 0, null);
//                            System.out.println("Test setting not met starting dialog to prompt user" );
//                        } catch (IntentSender.SendIntentException e) {
//                            // Ignore the error.
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        // Location settings are not satisfied. However, we have no way to fix the
//                        // settings so we won't show the dialog.
//                        break;
//                }
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
//                        System.out.println("test user has turned the gps back on");
//                        getLocation();
                        requestLocationWithGooglePermissions();
//                        getDeviceLocationAndGoToHomePage();
                        break;
                    case Activity.RESULT_CANCELED:

                        goHomePageWithoutLocation("Location is not available");

                        System.out.println("test user has denied the gps to be turned on");
                        Toast.makeText(this, "Location is required to order stations", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocationAndGoToHomePage() {
//        if (fusedLocationClient == null) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            //no last location was found -> start listening to GPS
                            pullLocationManually();
                        }
                        else {
                            currentLocation = location;
                            goHomePageWithLocation(location);
                        }
                    }
                });
    }


    @SuppressLint("MissingPermission")
    private void retryGettingDeviceLocationAfterEnablingSettings() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {

//                            pullLocationManually();
                            generateFusedGPSLocationAndProceedToHomePage();
//                            initLocationListener();
//                            ListenToLocationFromGPS();
//                            goHomePageWithoutLocation("Error getting Location");
                        }
                        else {
                            currentLocation = location;
                            goHomePageWithLocation(location);
                        }
                    }
                });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                goHomePageWithoutLocation("Location is not available");
            }
        });

//        Location l = task.getResult();
    }

    private boolean gpsIsOn(){
        return locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void pullLocationManually() {
        initLocationListener();

        if (locationManager == null) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
        boolean gpsIsEnabled = gpsIsOn();

        if(gpsIsEnabled){
            ListenToLocationFromGPS();
        }
        else {
            goHomePageWithoutLocation("Error Pulling Location Data");
//            ListenToLocationFromGPS();
//            requestToEnableLocationAndGetLocation();
        }


    }

    private void requestToEnableLocationAndGetLocation() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, let's enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, final int id) {
//                        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

//                        startActivityForResult(settingsIntent, 1);

                        openSettingPage();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        goHomePageWithoutLocation("No location available");
                    }
                });
        final AlertDialog alert = builder.create();
//        pullLocationFromGPS();
        alert.show();
    }

    private void openSettingPage() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivityForResult(settingsIntent, 1);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1) {
//            if (!gpsIsOn()){
////                goHomePageWithoutLocation("Locations is not enabled");
//            } else {
//
////                getDeviceLocationAndGoToHomePage();
//            }
//        }
//    }

    private void goHomePageWithoutLocation(String no_location_available) {
        //TODO: show TOAS of no location available
        Toast.makeText(this, no_location_available, Toast.LENGTH_SHORT).show();
        goToHomePage();
    }

    boolean requestedToListen = false;
    @SuppressLint("MissingPermission")
    private void ListenToLocationFromGPS() {
        if (!requestedToListen) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            requestedToListen = true;
        }
    }

    private void initLocationListener() {
        if (locationListener == null) {
            locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    //stop listening
                    if (location != null) {
                        stopListeningToGps();
                        goHomePageWithLocation(location);
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                    boolean what = false;

                }

                @Override
                public void onProviderEnabled(String s) {
//                    pullLocationFromGPS();
//                    getDeviceLocationAndGoToHomePage();
                    generateFusedGPSLocationAndProceedToHomePage();
                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
        }
    }

    FusedLocationProviderClient locationClient;
    LocationCallback mLocationCallbackListener;

    @SuppressLint("MissingPermission")
    private void generateFusedGPSLocationAndProceedToHomePage() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallbackListener = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        locationClient.removeLocationUpdates(mLocationCallbackListener);
                        goHomePageWithLocation(location);
                        break;

                    }
                }
            }
        };
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationClient.requestLocationUpdates(mLocationRequest, mLocationCallbackListener, null);
    }

    private void stopListeningToGps() {
        locationManager.removeUpdates(locationListener);
        locationListener = null;
    }

    private void goHomePageWithLocation(Location location) {
        LogicHandler.setCurrentDeviceLocation(location);
        Toast.makeText(this, "Location found", Toast.LENGTH_SHORT).show();
        goToHomePage();

    }

    private boolean shouldRequestForPermissions(){
        return ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private boolean hasPermissionsToLocations(){

        return ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //GOT LOCATION PERMISSIONS -> let's ask for location
                    getDeviceLocationAndGoToHomePage();
                }  else {
                    // Permissions denied -> go to come location without location feature
                    goHomePageWithoutLocation("No Permissions for location");
                }
                return;
        }
    }

}