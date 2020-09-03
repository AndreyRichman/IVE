package com.mta.ive.pages.home.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mta.ive.R;

public class LocationFragment extends Fragment {

//    private LocationViewModel locationViewModel;

    private TextView mainTitle, subTitle, bottomText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
//        locationViewModel =
//                ViewModelProviders.of(this).get(LocationViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_add, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        locationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

//
//        mainTitle = root.findViewById(R.id.tasksListMainTitle);
//        subTitle = root.findViewById(R.id.tasksListSubTitle);
//        bottomText = root.findViewById(R.id.tasksListBottomText);
//
//
//        return root;
    }
}