package com.mta.ive.logic.location;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    Button selectButton;
    LatLng latLng;
    String location;
    Context context;
    Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        context = this;
        searchView = findViewById(R.id.sv_location);
        selectButton = (Button) findViewById(R.id.select_map_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    location = searchView.getQuery().toString();
                    List<Address> addressList; //= new ArrayList<>();

                    if (location != null && !location.equals("")) {
                        Geocoder geocoder = new Geocoder(GoogleMapActivity.this);
                        try {
                            try {
                                addressList = geocoder.getFromLocationName(location, 1);
                            } catch (IOException io){
                                addressList = new ArrayList<>();
                            }
                            if (addressList.size() > 0) {
                                Address address = addressList.get(0);

                                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                                if (currentMarker != null)
                                    currentMarker.remove();
                                currentMarker = map.addMarker(new MarkerOptions().position(latLng).title(location));
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                selectButton.setVisibility(View.VISIBLE);
                            } else {
                                String message = "Unable to find location";
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                selectButton.setVisibility(View.INVISIBLE);
                                return false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            String message = "Unable to find location";
                            selectButton.setVisibility(View.INVISIBLE);
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                } catch (Exception ignore){
                    String message = "Unable to find location";
                    selectButton.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        selectButton.setOnClickListener(click -> {
            /*
            *  String latStr = data.getStringExtra("lat");
            String lngStr = data.getStringExtra("lng");
            String address = data.getStringExtra("address");
            * */
            Intent returnIntent = new Intent();
            returnIntent.putExtra("lat", this.latLng.latitude);
            returnIntent.putExtra("lng", this.latLng.longitude);
            returnIntent.putExtra("address", location);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String address = bundle.getString("Address");
            double lat = bundle.getDouble("Lat");
            double lng = bundle.getDouble("Lng");
            if(address != null && !address.equals("")){
                updateMapWIthExistingLocation(address, lat, lng);
            } else {
                updateMapWIthUserLocation();
            }
        }
        else { //new location
            updateMapWIthUserLocation();
        }
    }

    private void updateMapWIthUserLocation() {
        Location deviceLocation = LogicHandler.getDeviceLocation();
        if (deviceLocation != null) {


            try {
                String title;
                List<Address> addressList = null;
                Geocoder geocoder = new Geocoder(GoogleMapActivity.this, Locale.getDefault());
                addressList = geocoder.getFromLocation(deviceLocation.getLatitude(), deviceLocation.getLongitude(), 1);

                location = "";
                if(addressList.size() > 0){
                    location = addressList.get(0).getAddressLine(0);
                    searchView.setQuery(location, false);
                    latLng = new LatLng(deviceLocation.getLatitude(), deviceLocation.getLongitude());
                    if(currentMarker != null)
                        currentMarker.remove();
                    currentMarker = map.addMarker(new MarkerOptions().position(latLng).title(location));
                    selectButton.setVisibility(View.VISIBLE);
                }

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

            }
            catch (Exception ignore){}
        }
    }
    private void updateMapWIthExistingLocation(String address, double lat, double lng) {

        searchView.setQuery(address, false);
        latLng = new LatLng(lat, lng);
        currentMarker = map.addMarker(new MarkerOptions().position(latLng).title(address));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

    }
}