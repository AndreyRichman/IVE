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
import com.mta.ive.pages.login.LoginActivity;
import com.mta.ive.pages.login.SignUpInActivity;


public class UserFragment extends Fragment {

//    private UserViewModel notificationsViewModel;
    Button appSettingBtn, manageLocationBtn, personalSettingBtn, logoutBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        appSettingBtn = (Button)view.findViewById(R.id.app_settings);
        manageLocationBtn = (Button)view.findViewById(R.id.manage_locations);
        personalSettingBtn = (Button)view.findViewById(R.id.personal_settings);
        logoutBtn = (Button)view.findViewById(R.id.logout_button);

        logoutBtn.setOnClickListener( click -> {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), SignUpInActivity.class));
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
}