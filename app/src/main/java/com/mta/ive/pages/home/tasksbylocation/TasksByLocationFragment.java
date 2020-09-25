package com.mta.ive.pages.home.tasksbylocation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.location.LocationWithTasksWrapper;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;
import com.mta.ive.logic.users.User;
import com.mta.ive.vm.adapter.TasksAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    Map<UserLocation, List<Task>> locationToTasksMap;
    List<LocationWithTasksWrapper> swichableLocations;
    int indexOfCurrentlySelectedLocation;
    int indexOfUserCurrentLocation;
    UserLocation currentLocation;
//    UserLocation selectedLocation = null;

//    int lastSelectedLocationIndex = -1;
//    int userLocationByGPSIndex;

//    public void setTasksAdapter(TasksAdapter tasksAdapter) {
//        this.tasksAdapter = tasksAdapter;
//    }

//    List<UserLocation> locationsWithTasksPlusCurrent;
//    List<String> locationsNames;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        tasksList = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_tasks_by_location, container, false);
        tasksRecList = view.findViewById(R.id.tasksRecycleList);
        tasksRecList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        tasksRecList.setAdapter(new TasksAdapter(view.getContext(), tasksList));
        switchLocationButton = view.findViewById(R.id.fab);

        currentLocation = LogicHandler.getCurrentLocation();
        locationToTasksMap = LogicHandler.getCurrentUserLocationToTasksMap();
        swichableLocations = LogicHandler.getSwichableLocations();
        indexOfUserCurrentLocation = getIndeOfCurrentUserLocationInList(swichableLocations);
        indexOfCurrentlySelectedLocation = indexOfUserCurrentLocation;

        updateAllUserFields();

        setSwitchLocationFunctionality();


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
    private int getIndeOfCurrentUserLocationInList(List<LocationWithTasksWrapper> swichableLocations) {

        List<UserLocation> locationsInList = swichableLocations.stream()
                .map(LocationWithTasksWrapper::getLocation).collect(Collectors.toList());

        return locationsInList.indexOf(this.currentLocation);
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private Map<UserLocation, List<Task>> getSwichableLocations(Map<UserLocation, List<Task>> locationToTasksMap) {
//        Map<UserLocation, List<Task>> swichableLocations = new HashMap<>();
//
//        locationToTasksMap.entrySet()
//                .forEach(locationToTasksEntry -> {
//                    UserLocation location = locationToTasksEntry.getKey();
//                    List<Task> tasksInLocation = locationToTasksEntry.getValue();
//
//                    if (tasksInLocation.size() > 0){
//                        swichableLocations.put(location, tasksInLocation);
//                    }
//                });
//
//        if (!swichableLocations.containsKey(currentLocation)){
//
//        }
//
//        return swichableLocations;
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setSwitchLocationFunctionality() {
        switchLocationButton.setOnClickListener(click -> {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            List<Task> tasksToShow = swichableLocations.get(indexOfCurrentlySelectedLocation).getTasks();
                            updateUserTasksList(tasksToShow);
                            updateUserTitle(LogicHandler.getCurrentUser(), true);

//                            updateAllUserFields();
//                            boolean hasTasks = LogicHandler.getTasksOfCurrentUserInLocation(currentLocation).size() > 0;
//                            String msg = hasTasks? "Showing tasks for selected location": "No tasks found in selected location";
                            String msg = "Showing tasks for selected location";
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };


            if (swichableLocations.size() > 1){
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setTitle("Switch Location");
                alertBuilder.setPositiveButton ("Switch", dialogClickListener);
                alertBuilder.setNegativeButton("Cancel", dialogClickListener);

                List<String> locationsNames = swichableLocations.stream()
                        .map(LocationWithTasksWrapper::getLocation)
                        .map(UserLocation::getName)
                        .collect(Collectors.toList());

//                locationsNames.set(indexOfUserCurrentLocation, locationsNames.get(indexOfUserCurrentLocation) + "(Current)");
                String[] namesToShowInWindow = new String[locationsNames.size()];
                locationsNames.toArray(namesToShowInWindow);

                namesToShowInWindow[indexOfUserCurrentLocation] = locationsNames.get(indexOfUserCurrentLocation) + "(Current)";

                alertBuilder.setSingleChoiceItems(namesToShowInWindow, indexOfCurrentlySelectedLocation, (dialogInterface, i) -> {
//                    currentLocation = swichableLocations.get(i).getLocation();
                    indexOfCurrentlySelectedLocation = i;
                });
                alertBuilder.show();
            } else {
                boolean moreTasksExist = locationToTasksMap.keySet().size() > 1;
                String alertMessage;

                if (moreTasksExist){
                    alertMessage = "No tasks found in other locations";
                } else {
                    alertMessage = "No more locations defined..";
                }
//                String msg = "No tasks found in other locations";
                Toast.makeText(getContext(), alertMessage, Toast.LENGTH_SHORT).show();
            }

//            locationsWithTasksPlusCurrent = LogicHandler.getAllLocationsWithTasks();
//            if (locationsWithTasksPlusCurrent.size() > 1) {
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
//                alertBuilder.setTitle("Switch Location");
//                alertBuilder.setPositiveButton ("Switch", dialogClickListener);
//                alertBuilder.setNegativeButton("Cancel", dialogClickListener);
//
//                if (lastSelectedLocationIndex == -1) {
//                    lastSelectedLocationIndex = getIndexOfCurrentLocationInList(locationsWithTasksPlusCurrent);
//                    userLocationByGPSIndex = lastSelectedLocationIndex;
//                }
////                = locations.indexOf(LogicHandler.getCurrentLocation().getName());
//                List<String> names = locationsWithTasksPlusCurrent.stream().map(UserLocation::getName).collect(Collectors.toCollection(ArrayList::new));
//                String currentLocationName = names.get(userLocationByGPSIndex).concat("(Current Location)");
//                names.set(userLocationByGPSIndex, currentLocationName);
//                String[] locationsNames = new String[names.size()];
//                locationsNames = names.toArray(locationsNames);
//
//                alertBuilder.setSingleChoiceItems(locationsNames, lastSelectedLocationIndex, (dialogInterface, i) -> {
//                    currentLocation = locationsWithTasksPlusCurrent.get(i);
//                    lastSelectedLocationIndex = i;
//                });
////            alertBuilder.setOnCancelListener(quit -> {
////                updateAllUserFields();
////            });
//                alertBuilder.show();
//            }
//            else {
//                String msg = "No tasks found in other locations";
//                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//            }
        });
    }

    private int getIndexOfCurrentLocationInList(List<UserLocation> locationsWithTasksPlusCurrent) {
        int index = 0;
        int indexOfCurrent = 0;
        for (UserLocation location: locationsWithTasksPlusCurrent){
            if(location.getId().equals(LogicHandler.getCurrentLocation().getId())){
                indexOfCurrent = index;
            }
            index++;
        }
        return indexOfCurrent;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateAllUserFields() {
        User user = LogicHandler.getCurrentUser();
        List<Task> tasks = this.locationToTasksMap.get(currentLocation); //LogicHandler.getTasksOfCurrentUserInLocation(currentLocation);

        if (user != null) {
            updateUserTasksList(tasks);
            updateUserTitle(user, false);
        }
//        else {
//            LogicHandler.
//            loadUserFromDBAndUpdateUI();
//        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateUserTasksList(List<Task> tasksToShow) {
//        List<Task> tasks = this.locationToTasksMap.get(currentLocation); //LogicHandler.getTasksOfCurrentUserInLocation(currentLocation);

        tasksAdapter = new TasksAdapter(view.getContext(), tasksToShow); //TODO: originally: MainActivity.this
        tasksRecList.setAdapter(tasksAdapter);
        tasksAdapter.notifyDataSetChanged();
    }
//    private void loadUserFromDBAndUpdateUI() {
//        String userEmail = LogicHandler.getCurrentUserEmail();
//        DatabaseReference userReference = LogicHandler.getCurrentUserDBReferenceById(userEmail);
//
//        userReference.addValueEventListener(new ValueEventListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User currentUser = snapshot.getValue(User.class);
//                LogicHandler.setCurrentUser(currentUser);
//                updateAllUserFieldsByUser(currentUser);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

    private void updateUserTitle(User user, boolean chosenLocation) {
        String userName = user.getName();
        //UserLocation location = LogicHandler.getCurrentLocation();
        TextView title = view.findViewById(R.id.tasksListMainTitle);

        String userTitle = "Hello "+ userName + "!\n";
        boolean hasLocations = user.getArrayOfLocations().size() > 0;
        boolean foundLocation = currentLocation != null;

        String locationTitle;

        if (chosenLocation){
            String locationName = swichableLocations.get(indexOfCurrentlySelectedLocation)
                    .getLocation().getName();
            locationTitle = "Showing tasks at " + locationName;
        } else {
            locationTitle = foundLocation? "You are at " + currentLocation.getName() : hasLocations?
                    "Location not found" : "No locations defined";
        }

        title.setText(userTitle + locationTitle);
//        title.setText("Hello "+ userName + "! \n You are at " + location.getName());
    }


}