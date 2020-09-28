package com.mta.ive.pages.home.tasksbylocation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class TasksByLocationFragment extends Fragment {

    private TextView mainTitle, subTitle, bottomDurationText;
    DatabaseReference reference;
    RecyclerView tasksRecList;
    ArrayList<Task> tasksList;
    TasksAdapter tasksAdapter;
    ViewGroup root;
    FloatingActionButton switchLocationButton, exposeOptionsButton, showAllTasksButton;
    TextView switchLocationText, showAllTasksText;
    //Dialog locationsDialog;

    View view;
    String selected;

//    Map<UserLocation, List<Task>> locationToTasksMap;
    Map<String, List<Task>> locationIdToTasksMap;
    Map<String, UserLocation> locationIdToUserLocationMap;
    List<LocationWithTasksWrapper> swichableLocationsWithRelevant;
    List<LocationWithTasksWrapper> swichableLocationsWithAll;
    int indexOfCurrentlySelectedLocation;
    int indexOfUserCurrentLocation;
    UserLocation currentLocation;
    List<Task> tasksToShowInList;

    Boolean fabIsOpen = false;
    private Animation fab_open, fab_close;

    private boolean showingAllTasksInLocation = false;
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
//        tasksRecList.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        tasksRecList.setAdapter(new TasksAdapter(view.getContext(), tasksList));
        switchLocationButton = view.findViewById(R.id.fab_option_location);
        showAllTasksButton = view.findViewById(R.id.fab_option_all_tasks);
        exposeOptionsButton = view.findViewById(R.id.fab);
        switchLocationText = view.findViewById(R.id.textview_location);
        showAllTasksText = view.findViewById(R.id.textview_all_tasks);


        bottomDurationText = view.findViewById(R.id.tasksListBottomText);

        currentLocation = LogicHandler.getCurrentLocation();
        locationIdToUserLocationMap = LogicHandler.getIdToUserLocationMap();
//        locationToTasksMap = LogicHandler.getCurrentUserLocationToTasksMap();
        locationIdToTasksMap = LogicHandler.getLocationIdToTasksMap();
        swichableLocationsWithRelevant = LogicHandler.getSwichableLocationsWithRelevant();
        swichableLocationsWithAll = LogicHandler.getSwichableLocationsWithAll();
        indexOfUserCurrentLocation = getIndeOfCurrentUserLocationInList(swichableLocationsWithRelevant);
        indexOfCurrentlySelectedLocation = indexOfUserCurrentLocation;

        tasksToShowInList = new ArrayList<>();
        tasksAdapter = new TasksAdapter(view.getContext(), tasksToShowInList); //TODO: originally: MainActivity.this
        tasksRecList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        tasksRecList.setAdapter(tasksAdapter);

        updateAllUserFields();

        setFabButtons();


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

        List<UserLocation> locationsInList =  new ArrayList<>();

        swichableLocations.forEach(locationWithTasksWrapper -> {
            locationsInList.add(locationWithTasksWrapper.getLocation());
        });
                //.map(LocationWithTasksWrapper::getLocation).collect(Collectors.toList());

        //locationsInList.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
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
                            List<Task> tasksToShow = swichableLocationsWithRelevant.get(indexOfCurrentlySelectedLocation).getTasks();
                            updateUserTasksList(tasksToShow);
                            updateUserTitle(LogicHandler.getCurrentUser(), true);

//                            updateAllUserFields();
//                            boolean hasTasks = LogicHandler.getTasksOfCurrentUserInLocation(currentLocation).size() > 0;
//                            String msg = hasTasks? "Showing tasks for selected location": "No tasks found in selected location";
                            String msg = "Showing tasks for selected location";
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            hideFloating();
                            break;
                    }
                }
            };


            if (swichableLocationsWithRelevant.size() > 1){
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setTitle("Switch Location");
                alertBuilder.setPositiveButton ("Switch", dialogClickListener);
                alertBuilder.setNegativeButton("Cancel", dialogClickListener);

                List<String> locationsNames = new ArrayList<>();
                swichableLocationsWithRelevant.forEach(locationWithTasks -> {
                    String name = locationWithTasks.getLocation().getName();
                    String locationId = locationWithTasks.getLocation().getId();

                    if (locationId.equals(LogicHandler.getCurrentLocation().getId())){
                        name += "(Current)";
                    }
                    locationsNames.add(name);
                });

                locationsNames.sort(String::compareToIgnoreCase);
//                .stream()
//                        .map(LocationWithTasksWrapper::getLocation)
//                        .map(UserLocation::getName)
//                        .collect(Collectors.toList());

//                locationsNames.set(indexOfUserCurrentLocation, locationsNames.get(indexOfUserCurrentLocation) + "(Current)");
                String[] namesToShowInWindow = new String[locationsNames.size()];
                locationsNames.toArray(namesToShowInWindow);

//                namesToShowInWindow[indexOfUserCurrentLocation] = locationsNames.get(indexOfUserCurrentLocation) + "(Current)";

                alertBuilder.setSingleChoiceItems(namesToShowInWindow, indexOfCurrentlySelectedLocation, (dialogInterface, i) -> {
//                    currentLocation = swichableLocations.get(i).getLocation();
                    indexOfCurrentlySelectedLocation = i;
                });
                alertBuilder.show();
            } else {
                boolean moreLocationsExist = locationIdToTasksMap.keySet().size() > 1;
                String alertMessage;

                if (moreLocationsExist){
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
//        List<Task> tasks = this.locationToTasksMap.get(currentLocation); //LogicHandler.getTasksOfCurrentUserInLocation(currentLocation);
        List<Task> tasks = this.locationIdToTasksMap.get(currentLocation.getId());
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
//        tasksAdapter.setAllTasks(tasksToShow);

        tasksRecList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        tasksRecList.setAdapter(tasksAdapter);
//        tasksToShowInList = tasksToShow;
//        tasksRecList.setAdapter(tasksAdapter);
        tasksAdapter.notifyDataSetChanged();

        String durationMessage;
        int minutes = tasksToShow.stream().mapToInt(Task::getDuration).sum();
        if (minutes >= 60){
            int hours = minutes / 60;
            minutes = minutes % 60;
            durationMessage = "Total Task time: "+ hours + " hours, " + minutes + " minutes";
        } else {
            durationMessage = "Total Task time: "+ minutes + " minutes";
        }
        bottomDurationText.setText(durationMessage);
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
            String locationName = swichableLocationsWithRelevant.get(indexOfCurrentlySelectedLocation)
                    .getLocation().getName();
            locationTitle = "Showing tasks at " + locationName;
        } else {
            locationTitle = foundLocation? "You are at " + currentLocation.getName() : hasLocations?
                    "Location not found" : "No locations defined";
        }

        title.setText(userTitle + locationTitle);
//        title.setText("Hello "+ userName + "! \n You are at " + location.getName());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setFabButtons() {
        fab_close = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_open);

        exposeOptionsButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                if (fabIsOpen) {
                    hideFloating();
                } else {
                    showFloating();
                }

            }
        });

        showAllTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Task> tasksToShow;
                String msg;

                if (showingAllTasksInLocation){
                    tasksToShow = swichableLocationsWithRelevant.get(indexOfCurrentlySelectedLocation).getTasks();
                    msg = "Showing relevant tasks";

                } else {
                    tasksToShow = swichableLocationsWithAll.get(indexOfCurrentlySelectedLocation).getTasks();
                    msg = "Showing all tasks";
                }

                showingAllTasksInLocation = !showingAllTasksInLocation;

                updateUserTasksList(tasksToShow);
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                hideFloating();

            }
        });

        setSwitchLocationFunctionality();


    }

    private void hideFloating(){
        switchLocationText.setVisibility(View.INVISIBLE);
        showAllTasksText.setVisibility(View.INVISIBLE);
        switchLocationButton.startAnimation(fab_close);
        showAllTasksButton.startAnimation(fab_close);
//                    fab_main.startAnimation(fab_anticlock);
        switchLocationButton.setClickable(false);
        showAllTasksButton.setClickable(false);
        fabIsOpen = false;
    }

    private void showFloating(){
        if(showingAllTasksInLocation){
            showAllTasksText.setText("Show relevant tasks in location");
        } else {
            showAllTasksText.setText("Show all tasks in location");
        }

        switchLocationText.setVisibility(View.VISIBLE);
        showAllTasksText.setVisibility(View.VISIBLE);
        switchLocationButton.startAnimation(fab_open);
        showAllTasksButton.startAnimation(fab_open);
//                    fab_main.startAnimation(fab_clock);
        switchLocationButton.setClickable(true);
        showAllTasksButton.setClickable(true);
        fabIsOpen = true;
    }


}