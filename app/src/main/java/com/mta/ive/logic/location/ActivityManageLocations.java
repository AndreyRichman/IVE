package com.mta.ive.logic.location;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.mta.ive.R;
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
            Intent addLocationIntent = new Intent(this, ActivityAddNewLocation.class);
            startActivity(addLocationIntent);
        });
    }
}
