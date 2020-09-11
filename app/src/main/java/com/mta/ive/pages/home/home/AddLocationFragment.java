package com.mta.ive.pages.home.home;

import android.hardware.usb.UsbRequest;
import android.os.Bundle;


import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;

public class AddLocationFragment extends AppCompatActivity {

    Button saveLocationButton;
    TextView locationName, locationAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_new_location);

        saveLocationButton = findViewById(R.id.save_location_button);
        locationName = findViewById(R.id.location_name);
        locationAddress = findViewById(R.id.location_address);


        saveLocationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View btn){

                addNewLocation();
                finish();
                Toast.makeText(btn.getRootView().getContext(),"Location added", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void addNewLocation(){
        UserLocation userLocation = new UserLocation();

        userLocation.setName(locationName.getText().toString());
        userLocation.setAddress(locationAddress.getText().toString());

        LogicHandler.saveLocation(userLocation);
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
