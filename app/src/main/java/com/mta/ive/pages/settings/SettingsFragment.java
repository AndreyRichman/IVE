package com.mta.ive.pages.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.pages.home.HomeActivity;
import com.mta.ive.pages.login.SignUpInActivity;


public class SettingsFragment extends Fragment {

    Button appSettingBtn, manageLocationBtn, logoutBtn;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);


        appSettingBtn = (Button)view.findViewById(R.id.app_settings);
        manageLocationBtn = (Button)view.findViewById(R.id.manage_locations);
        logoutBtn = (Button)view.findViewById(R.id.logout_button);

        appSettingBtn.setOnClickListener(click -> {
            Bundle bundle = new Bundle();
            ((HomeActivity)view.getContext()).openManageAppSettings(bundle);
        });

        logoutBtn.setOnClickListener( click -> {
            FirebaseAuth.getInstance().signOut();
            LogicHandler.signOutGoogleIfNeeded();

            startActivity(new Intent(getActivity(), SignUpInActivity.class));
        });

        manageLocationBtn.setOnClickListener( click -> {
            Bundle bundle = new Bundle();
            ((HomeActivity)view.getContext()).openManageLocationsPage(bundle);
        });

        return view;
    }
}