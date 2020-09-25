package com.mta.ive.pages.home.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.location.GoogleMapActivity;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;

import java.util.ArrayList;
import java.util.List;

public class AddLocationFragment extends AppCompatActivity {

    Button saveLocationButton, deleteButton;
    TextView locationName, locationAddress, title;

    LatLng locationLatLng;

    boolean existingLocation = false;
    UserLocation currentLocation = null;
//    ArrayList<Task> tasksUnderLocation = null;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_new_location);

        saveLocationButton = findViewById(R.id.save_location_button);
        deleteButton = findViewById(R.id.delete_location_button);

        locationName = findViewById(R.id.location_name);
        locationAddress = findViewById(R.id.location_address);
        title = findViewById(R.id.add_location_header);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title.setText("Edit Location");
            deleteButton.setEnabled(true);
            //show/enable Delete Button
            //move save button
            existingLocation = true;
            String locationId = bundle.getString("locationId");
            updateWindowWithLocation(locationId);
        }

        saveLocationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View btn){
                if(locationName.getText().toString().matches("")) {
                    Toast.makeText(btn.getRootView().getContext(),"Name is missing", Toast.LENGTH_SHORT).show();
                }
                else if (locationAddress.getText().toString().matches("")){
                    Toast.makeText(btn.getRootView().getContext(),"Address is missing", Toast.LENGTH_SHORT).show();
                }
                else {


                    finish();
                    if(existingLocation){
                        updateExistingLocationIfNeeded();
                        Toast.makeText(btn.getContext(), "Location updated", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        addNewLocation();
                        Toast.makeText(btn.getContext(), "Location added", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        deleteButton.setOnClickListener( click -> {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Toast.makeText(AddLocationFragment.this, "Location deleted", Toast.LENGTH_SHORT).show();

                            deleteLocationAndAllItsTasks(currentLocation);
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };


            List<Task> tasksUnderLocation = LogicHandler.getAllTasksUnderOnlyThisLocation(currentLocation);
            int numberOfTasks = tasksUnderLocation.size();
            String deleteTitle = "Are you sure?";
            String yesOptionText = "Yes";
            String noOptionText = "Cancel";

            if (numberOfTasks > 0) {
                String taskText = numberOfTasks > 1? "tasks": "task";
                deleteTitle = numberOfTasks + " active " + taskText + " in this location will be deleted, are you sure?";
                String itThem = numberOfTasks > 1?  "them": "it";
                yesOptionText = "Yes delete " + itThem;
            }
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setMessage(deleteTitle)
                .setPositiveButton(yesOptionText, dialogClickListener)
                .setNegativeButton(noOptionText, dialogClickListener)
                .show();


//            finish();
        });


        setNavigationButtons();

        locationAddress.setOnClickListener(click -> {
            // Intent addLocationPage = new Intent(this, AddLocationFragment.class);
//            startActivity(new Intent(this, GoogleMapActivity.class));
            int LAUNCH_SECOND_ACTIVITY = 1;
            Intent googleMapPage = new Intent(this, GoogleMapActivity.class);

            startActivityForResult(googleMapPage, LAUNCH_SECOND_ACTIVITY);

        });
    }

    private void updateWindowWithLocation(String locationId) {
        currentLocation = LogicHandler.getLocationById(locationId);

        locationName.setText(currentLocation.getName());
        locationAddress.setText(currentLocation.getAddress());
        locationLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deleteLocationAndAllItsTasks(UserLocation locationToDelete){
        LogicHandler.deleteLocationById(locationToDelete.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            double lat = data.getDoubleExtra("lat", 32);
            double lng = data.getDoubleExtra("lng", 35);
            String address = data.getStringExtra("address");


            this.locationLatLng = new LatLng(lat, lng);

            locationAddress.setText(address);
        }
    }

    private void addNewLocation(){
        UserLocation userLocation = new UserLocation();

        userLocation.setName(locationName.getText().toString());
        userLocation.setAddress(locationAddress.getText().toString());
        userLocation.setLatitude(this.locationLatLng.latitude);
        userLocation.setLongitude(this.locationLatLng.longitude);

        LogicHandler.saveLocation(userLocation);
    }

    private void updateExistingLocationIfNeeded(){
        boolean nameWasChanged = !locationName.getText().equals(currentLocation.getName());
        boolean locationWasChanged = !locationAddress.getText().equals(currentLocation.getAddress());

        if (nameWasChanged || locationWasChanged){
            currentLocation.setName(locationName.getText().toString());
            currentLocation.setAddress(locationAddress.getText().toString());
            currentLocation.setLatitude(this.locationLatLng.latitude);
            currentLocation.setLongitude(this.locationLatLng.longitude);
            LogicHandler.updateExistingLocation(currentLocation);
        }
    }

    private void setNavigationButtons() {
        ((BottomNavigationView)findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_user);

        findViewById(R.id.navigation_location).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "1");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
        findViewById(R.id.navigation_add).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "2");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

        findViewById(R.id.navigation_user).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "3");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }


//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_add_new_location, container, false);
//
//
//        return view;
//    }
}
