package com.mta.ive;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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

    public void loadDeviceLocation(){
        LogicHandler.loadDeviceLocation(this);
    }

    public void goToHomePage(){
        Intent homePage = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(homePage);//, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }


    private void fetchLocationAndGoToHomePage(){
        boolean hasPermissionsToLocations = hasPermissionsToLocations();

        if (hasPermissionsToLocations){
            getDeviceLocationAndGoToHomePage();
        }
        else {  //don't have permissions -> request them!
            if (shouldRequestForPermissions()){
                //request for permissions
                requestForPermissions();
            }
            else {
                //proceed to app without location
                goHomePageWithoutLocation("No Location available");
            }
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
            ListenToLocationFromGPS();
            requestToEnableLocationAndGetLocation();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (!gpsIsOn()){
//                goHomePageWithoutLocation("Locations is not enabled");
            } else {

//                getDeviceLocationAndGoToHomePage();
            }
        }
    }

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

    @SuppressLint("MissingPermission")
    private void generateFusedGPSLocationAndProceedToHomePage() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        boolean finally_ = true;
                        //TODO: UI updates.
                    }
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void stopListeningToGps() {
        locationManager.removeUpdates(locationListener);
        locationListener = null;
    }

    private void goHomePageWithLocation(Location location) {
        LogicHandler.setCurrentDeviceLocation(location);
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

    //    public void openSignUpInPage(View btn) {
//        Intent loginPage = new Intent(this, SignUpInActivity.class);
//        startActivity(loginPage);
//    }
//
//    public void openLoginPage(View btn) {
//        Intent loginPage = new Intent(this, LoginActivity.class);
//        loginPage.putExtra("PAGE_NAME", "LOGIN PAGE");
//        startActivity(loginPage);
//    }
//
//    public void openSignUpPage(View btn) {
//        Intent signupPage = new Intent(this, SignUpActivity.class);
//        signupPage.putExtra("PAGE_NAME", "SIGNUP PAGE");
//        startActivity(signupPage);
//    }
//
//    public void openLobbyPage(View btn) {
//        Intent lobbyPage = new Intent(this, LobbyActivity.class);
//        lobbyPage.putExtra("PAGE_NAME", "LOBBY PAGE");
//        startActivity(lobbyPage);
//    }
//
//    public void openNewTask(View btn) {
//        Intent newTaskPage = new Intent(this, NewTaskActivity.class);
//        newTaskPage.putExtra("PAGE_NAME", "NEW TASK PAGE");
//        startActivity(newTaskPage);
//    }
//
//    public void openAllTasks(View btn) {
//        Intent allTasksPage = new Intent(this, AllTasksActivity.class);
//        allTasksPage.putExtra("PAGE_NAME", "ALL TASKS PAGE");
//        startActivity(allTasksPage);
//    }
//
//    public void goMainPage(View btn){
//        Intent homePage = new Intent(this, HomeActivity.class);
//        startActivity(homePage);
//    }

}