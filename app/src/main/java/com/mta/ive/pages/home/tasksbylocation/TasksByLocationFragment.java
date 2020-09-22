package com.mta.ive.pages.home.tasksbylocation;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;
import com.mta.ive.logic.users.User;
import com.mta.ive.vm.adapter.TasksAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TasksByLocationFragment extends Fragment {

    private TextView mainTitle, subTitle, bottomText;
    DatabaseReference reference;
    RecyclerView tasksRecList;
    ArrayList<Task> tasksList;
    TasksAdapter tasksAdapter;
    ViewGroup root;
    FloatingActionButton switchLocationButton;
    //Dialog locationsDialog;

    View view;
    String selected;

//    public void setTasksAdapter(TasksAdapter tasksAdapter) {
//        this.tasksAdapter = tasksAdapter;
//    }

    List<UserLocation> locations;
    List<String> locationsNames;
    UserLocation currentLocation;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        tasksList = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_tasks_by_location, container, false);
        tasksRecList = view.findViewById(R.id.tasksRecycleList);
        tasksRecList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        tasksRecList.setAdapter(new TasksAdapter(view.getContext(), tasksList));
        currentLocation = LogicHandler.getCurrentLocation();

        updateAllUserFields();

        switchLocationButton = view.findViewById(R.id.fab);

        switchLocationButton.setOnClickListener(click -> {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setTitle("Switch Location");

            locations = LogicHandler.getCurrentUser().getArrayOfLocations();
            int selectedIndex = locations.indexOf(currentLocation);
            List<String> names = locations.stream().map(UserLocation::getName).collect(Collectors.toCollection(ArrayList::new));
            String[] locationsNames = new String[names.size()];
            locationsNames = names.toArray(locationsNames);

            alertBuilder.setSingleChoiceItems(locationsNames, selectedIndex, (dialogInterface, i) -> currentLocation = locations.get(i));
            alertBuilder.setOnCancelListener(quit -> {
                updateAllUserFields();
            });
            alertBuilder.show();
        });

//        updateUserTitle(view);


//        reference = FirebaseDatabase.getInstance().getReference().child("task");

//        reference = LogicHandler.getAllTasksDBReference();
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                tasksList = new ArrayList<>();
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    Task task = dataSnapshot1.getValue(Task.class);
//                    tasksList.add(task);
//                }
//
//                tasksAdapter = new TasksAdapter(view.getContext(), tasksList); //TODO: originally: MainActivity.this
//                tasksRecList.setAdapter(tasksAdapter);
//                tasksAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(),"Error pulling data", Toast.LENGTH_SHORT).show();
//            }
//        });

        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateAllUserFields() {
        User user = LogicHandler.getCurrentUser();

        if (user != null) {
            updateAllUserFieldsByUser(user);
        }
        else {
            loadUserFromDBAndUpdateUI();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateAllUserFieldsByUser(User user) {
        ArrayList<Task> tasks = LogicHandler.getAllRelevantTasksOfCurrentUser(currentLocation);

        tasksAdapter = new TasksAdapter(view.getContext(), tasks); //TODO: originally: MainActivity.this
        tasksRecList.setAdapter(tasksAdapter);
        tasksAdapter.notifyDataSetChanged();

        updateUserTitle(user);
    }
    private void loadUserFromDBAndUpdateUI() {
        String userEmail = LogicHandler.getCurrentUserEmail();
        DatabaseReference userReference = LogicHandler.getCurrentUserDBReferenceById(userEmail);

        userReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);
                LogicHandler.setCurrentUser(currentUser);
                updateAllUserFieldsByUser(currentUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateUserTitle(User user) {
        String userName = user.getName();
        //UserLocation location = LogicHandler.getCurrentLocation();
        TextView title = view.findViewById(R.id.tasksListMainTitle);

        String userTitle = "Hello "+ userName + "!\n";
        boolean hasLocations = user.getArrayOfLocations().size() > 0;
        boolean foundLocation = currentLocation != null;



        String locationTitle = foundLocation? "You are at " + currentLocation.getName() : hasLocations?
                "Location not found" : "No locations defined";
        title.setText(userTitle + locationTitle);
//        title.setText("Hello "+ userName + "! \n You are at " + location.getName());
    }


}