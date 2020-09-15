package com.mta.ive.logic.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mta.ive.R;
import com.mta.ive.pages.home.home.AddLocationFragment;
import com.mta.ive.pages.login.SignUpInActivity;


public class ActivityManageLocations extends AppCompatActivity
{
    Button addLocationBtn;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_manage_locations);
        addLocationBtn = (Button)findViewById(R.id.add_location_button);
        addLocationBtn.setOnClickListener(v ->
        {
            Intent addLocationIntent = new Intent(this, AddLocationFragment.class);
            startActivity(addLocationIntent);
        });
        setNavigationButtons();

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


}
