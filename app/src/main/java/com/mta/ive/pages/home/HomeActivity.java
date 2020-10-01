package com.mta.ive.pages.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mta.ive.R;
import com.mta.ive.pages.location.ActivityManageLocations;
import com.mta.ive.pages.settings.AppSettingsPage;
import com.mta.ive.pages.task.EditExistingTaskActivity;
import com.mta.ive.pages.tasksbylocation.StatisticsActivity;


public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void openEditTaskPage(Bundle bundle){

        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent editTaskPage = new Intent(this, EditExistingTaskActivity.class);
        editTaskPage.putExtras(bundle);
        startActivityForResult(editTaskPage, LAUNCH_SECOND_ACTIVITY);
    }

    public void openManageAppSettings(Bundle bundle){
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent modifyAppSettings = new Intent(this, AppSettingsPage.class);
        modifyAppSettings.putExtras(bundle);
        startActivityForResult(modifyAppSettings, LAUNCH_SECOND_ACTIVITY);
    }

    public void openManageLocationsPage(Bundle bundle){
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent addLocationPage = new Intent(this, ActivityManageLocations.class);
        addLocationPage.putExtras(bundle);
        startActivityForResult(addLocationPage, LAUNCH_SECOND_ACTIVITY);
    }

    public void openStatisticsPage(Bundle bundle){
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent statisticsPage = new Intent(this, StatisticsActivity.class);
        statisticsPage.putExtras(bundle);
        startActivityForResult(statisticsPage, LAUNCH_SECOND_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            String selection = data.getStringExtra("selection");

            Integer option = Integer.parseInt(selection);
            switch (option) {
                case 1:
                    findViewById(R.id.navigation_location).callOnClick();
                    break;
                case 2:
                    findViewById(R.id.navigation_add).callOnClick();
                    break;
                case 3:
                    findViewById(R.id.navigation_user).callOnClick();
                    break;
            }
        }

    }

    @Override
    public void onBackPressed() {
    }
}