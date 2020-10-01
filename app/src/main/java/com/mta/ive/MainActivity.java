package com.mta.ive;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
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
import com.mta.ive.pages.home.HomeActivity;

public class MainActivity extends AppCompatActivity {


    Class<HomeActivity> homeAcivity;
    private FusedLocationProviderClient fusedLocationClient;
    private final int REQUEST_CHECK_SETTINGS = 79;
    private boolean locationWasFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

        String email = getIntent().getStringExtra("email");
        String userName = getIntent().getStringExtra("userName");

        homeAcivity = HomeActivity.class;
        LogicHandler.createUserIfNotExist(email, userName, this);
    }

    public void loadUserFromDB() {
        String userEmail = LogicHandler.getCurrentUserEmail();
        DatabaseReference userReference = LogicHandler.getCurrentUserDBReferenceById(userEmail);

        userReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);
                LogicHandler.setCurrentUser(currentUser);
                LogicHandler.loadUserLocationsAndTasksMaps();

                checkForLocationAndRequestIfNeeded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void goToHomePage(){
        LogicHandler.updateCurrentUserLocation();
        LogicHandler.reloadUserData();
        LogicHandler.loadSwichableLocations();

        Intent homePage = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(homePage);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }


    private void fetchLocationAndGoToHomePage(){
        boolean hasPermissionsToLocations = hasPermissionsToLocations();

        if (hasPermissionsToLocations){
            requestLocationWithGooglePermissions();
        }
        else {  //don't have permissions -> request them!
            requestForPermissions();
        }

    }


    private void requestLocationWithGooglePermissions() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                boolean locationUsable = locationSettingsResponse.getLocationSettingsStates().isLocationUsable();

                if (locationUsable){
                    retryGettingDeviceLocationAfterEnablingSettings();
                }
                else {
                    goHomePageWithoutLocation("Location is not available");
                }
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        requestLocationWithGooglePermissions();
                        break;
                    case Activity.RESULT_CANCELED:

                        goHomePageWithoutLocation("Location is not available");
                        break;
                }
                break;
        }
    }



    @SuppressLint("MissingPermission")
    private void retryGettingDeviceLocationAfterEnablingSettings() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {

                            generateFusedGPSLocationAndProceedToHomePage();
                        }
                        else {
                            goHomePageWithLocation(location);
                        }
                    }
                });
        task.addOnFailureListener(new OnFailureListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFailure(@NonNull Exception e) {
                goHomePageWithoutLocation("Location is not available");
            }
        });

    }

    private void checkForLocationAndRequestIfNeeded(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    fetchLocationAndGoToHomePage();
                }
                else {
                    goHomePageWithLocation(location);
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fetchLocationAndGoToHomePage();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void goHomePageWithoutLocation(String no_location_available) {
        //TODO: show TOAS of no location available
        Toast.makeText(this, no_location_available, Toast.LENGTH_SHORT).show();
        goToHomePage();
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {

                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        if (mLocationCallbackListener != null) {
                            locationClient.removeLocationUpdates(mLocationCallbackListener);
                        }
                        mLocationCallbackListener = null;
                        goHomePageWithLocation(location);
                        break;

                    }
                }
            }
        };
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationClient.requestLocationUpdates(mLocationRequest, mLocationCallbackListener, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void goHomePageWithLocation(Location location) {
        if (!locationWasFound) {
            locationWasFound = true;
            LogicHandler.setCurrentLocationOfDevice(location);
            Toast.makeText(this, "Location found", Toast.LENGTH_SHORT).show();
            goToHomePage();
        }
    }

    private boolean hasPermissionsToLocations(){

        return ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationWithGooglePermissions();
                }  else {
                    // Permissions denied -> go to come location without location feature
                    goHomePageWithoutLocation("No Permissions for location");
                }
                return;
        }
    }

}