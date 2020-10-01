package com.mta.ive.pages.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.vm.adapter.LocationsAdapter;

import java.util.ArrayList;
import java.util.List;


public class ActivityManageLocations extends AppCompatActivity
{

    RecyclerView locationsRecList;
    LocationsAdapter locationsAdapter;
    List<UserLocation> userLocations;
    Button addLocationBtn;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_manage_locations);


        locationsRecList = findViewById(R.id.locationsRecycleList);
        locationsRecList.setLayoutManager(new LinearLayoutManager(this));
        userLocations = new ArrayList<>();
        locationsRecList.setAdapter(new LocationsAdapter(this, userLocations));
        addLocationBtn = (Button)findViewById(R.id.add_location_button);

        addLocationBtn.setOnClickListener(click -> {

            int LAUNCH_SECOND_ACTIVITY = 1;
            Intent addLocationPage = new Intent(this, AddLocationFragment.class);
            startActivityForResult(addLocationPage, LAUNCH_SECOND_ACTIVITY);
        });

        updateLocationList();
        setNavigationButtons();
    }

    private void updateLocationList(){
        userLocations = LogicHandler.getCurrentUser().getArrayOfLocations();
        locationsAdapter = new LocationsAdapter(ActivityManageLocations.this, userLocations);
        locationsRecList.setAdapter(locationsAdapter);
        locationsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        updateLocationList();
        if(resultCode == Activity.RESULT_OK){
            String selection = data.getStringExtra("selection");

            Integer option = Integer.parseInt(selection);
            Intent returnIntent;
            switch (option) {
                case 1:
                    returnIntent = new Intent();
                    returnIntent.putExtra("selection", "1");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    break;
                case 2:
                    returnIntent = new Intent();
                    returnIntent.putExtra("selection", "2");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    break;
                case 3:
                    returnIntent = new Intent();
                    returnIntent.putExtra("selection", "3");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    break;
                default:
                    break;
            }
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
}
