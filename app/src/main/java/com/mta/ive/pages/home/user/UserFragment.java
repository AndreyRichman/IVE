package com.mta.ive.pages.home.user;

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


public class UserFragment extends Fragment {

//    private UserViewModel notificationsViewModel;
    Button appSettingBtn, manageLocationBtn, logoutBtn; //personalSettingBtn
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);


        appSettingBtn = (Button)view.findViewById(R.id.app_settings);
        manageLocationBtn = (Button)view.findViewById(R.id.manage_locations);
//        personalSettingBtn = (Button)view.findViewById(R.id.personal_settings);
        logoutBtn = (Button)view.findViewById(R.id.logout_button);

        appSettingBtn.setOnClickListener(click -> {
            Bundle bundle = new Bundle();
            ((HomeActivity)view.getContext()).openManageAppSettings(bundle);
        });

//        personalSettingBtn.setOnClickListener(click -> {
//            Bundle bundle = new Bundle();
//            ((HomeActivity)view.getContext()).openManagePersonalSettings(bundle);
//        });


        logoutBtn.setOnClickListener( click -> {
            FirebaseAuth.getInstance().signOut();
            LogicHandler.signOutGoogleIfNeeded();

            startActivity(new Intent(getActivity(), SignUpInActivity.class));
        });

        manageLocationBtn.setOnClickListener( click -> {
//            startActivity(new Intent(getActivity(), ActivityManageLocations.class));
            Bundle bundle = new Bundle();
            ((HomeActivity)view.getContext()).openManageLocationsPage(bundle);
//            int LAUNCH_SECOND_ACTIVITY = 1;
//            Intent addLocationPage = new Intent(getContext(), ActivityManageLocations.class);
//            startActivityForResult(addLocationPage, LAUNCH_SECOND_ACTIVITY);
        });

        return view;



//        notificationsViewModel =
//                ViewModelProviders.of(this).get(UserViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_user, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            String selection = data.getStringExtra("selection");
//
//            Integer option = Integer.parseInt(selection);
//            switch (option) {
//                case 1:
//                    view.findViewById(R.id.navigation_location).callOnClick();
//                    break;
//                case 2:
//                    view.findViewById(R.id.navigation_home).callOnClick();
//                    break;
//                case 3:
//                    view.findViewById(R.id.navigation_add).callOnClick();
//                    break;
//                case 4:
//                    view.findViewById(R.id.navigation_user).callOnClick();
//                    break;
//            }
////            switchToTabAccordingToSelection(Integer.parseInt(selection));
//        }
//    }
    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            String selection = data.getStringExtra("selection");
//
//            Integer option = Integer.parseInt(selection);
//            switch (option) {
//                case 1:
//                    view.findViewById(R.id.navigation_location).callOnClick();
//                    break;
//                case 2:
//                    view.findViewById(R.id.navigation_home).callOnClick();
//                    break;
//                case 3:
//                    view.findViewById(R.id.navigation_add).callOnClick();
//                    break;
//                case 4:
//                    view.findViewById(R.id.navigation_user).callOnClick();
//                    break;
//            }
////            switchToTabAccordingToSelection(Integer.parseInt(selection));
//        }
//    }
}