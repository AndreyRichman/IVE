package com.mta.ive.pages.home.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;
import com.mta.ive.pages.home.HomeActivity;
import com.mta.ive.pages.home.addtask.EditExistingTaskActivity;
import com.mta.ive.vm.adapter.LocationsAdapter;
import com.mta.ive.vm.adapter.TasksAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView locationsRecList;
    DatabaseReference reference;
    LocationsAdapter tasksAdapter;
    Button addNewButton;
    List<UserLocation> userLocations;
//    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_locations, container, false);

        locationsRecList = view.findViewById(R.id.locationsRecycleList);
        locationsRecList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        userLocations = new ArrayList<>();
        locationsRecList.setAdapter(new LocationsAdapter(view.getContext(), userLocations));


        addNewButton = view.findViewById(R.id.add_location_button);

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addLocationPage = new Intent(getActivity(), AddLocationFragment.class);
                startActivity(addLocationPage);

//                ((HomeActivity)context).openEditTaskPage(bundle);

//                v.getRootView().findViewById(R.id.navigation_add_location).callOnClick();
//                FragmentTransaction fragmentTransaction = getActivity()
//                        .getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.nav_host_fragment, new AddLocationFragment());
//                fragmentTransaction.commit();
            }
        });

        reference = LogicHandler.getAllLocationsDBReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userLocations = new ArrayList<>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    UserLocation userLocation = dataSnapshot1.getValue(UserLocation.class);
                    userLocations.add(userLocation);
                }



                tasksAdapter = new LocationsAdapter(view.getContext(), userLocations); //TODO: originally: MainActivity.this
                locationsRecList.setAdapter(tasksAdapter);
                tasksAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error pulling data", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
    }
}