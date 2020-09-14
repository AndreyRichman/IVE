package com.mta.ive.pages.home.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.location.GoogleMapActivity;
import com.mta.ive.logic.location.UserLocation;

public class AddLocationFragment extends AppCompatActivity {

    Button saveLocationButton, deleteButton;
    TextView locationName, locationAddress;
    LatLng locationLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_new_location);

        saveLocationButton = findViewById(R.id.save_location_button);
        deleteButton = findViewById(R.id.delete_location_button);

        locationName = findViewById(R.id.location_name);
        locationAddress = findViewById(R.id.location_address);


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
                    addNewLocation();
                    finish();
                    Toast.makeText(btn.getRootView().getContext(),"Location added", Toast.LENGTH_SHORT).show();

                }

            }
        });

        deleteButton.setOnClickListener( click -> {
            finish();
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

    private void setNavigationButtons() {
        ((BottomNavigationView)findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_home);

        findViewById(R.id.navigation_location).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "1");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
        findViewById(R.id.navigation_home).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "2");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
        findViewById(R.id.navigation_add).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "3");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

        findViewById(R.id.navigation_user).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "4");
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
