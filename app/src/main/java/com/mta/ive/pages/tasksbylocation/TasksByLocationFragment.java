package com.mta.ive.pages.tasksbylocation;

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
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.location.LocationWithTasksWrapper;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;
import com.mta.ive.logic.users.UserSettings;
import com.mta.ive.pages.home.HomeActivity;
import com.mta.ive.vm.adapter.TasksAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TasksByLocationFragment extends Fragment {

    private TextView bottomDurationText;
    RecyclerView tasksRecList;
    ArrayList<Task> tasksList;
    TasksAdapter tasksAdapter;
    FloatingActionButton switchLocationButton, exposeOptionsButton, showAllTasksButton, showStats;
    TextView switchLocationText, showAllTasksText, tasksTitle, showStatsText;

    View view;

    Map<String, List<Task>> locationIdToTasksMap;
    Map<String, UserLocation> locationIdToUserLocationMap;
    List<LocationWithTasksWrapper> swichableLocationsWithRelevant;
    List<LocationWithTasksWrapper> swichableLocationsWithAll;
    int indexOfCurrentlySelectedLocation;
    int indexOfUserCurrentLocation;
    UserLocation currentLocation;
    List<Task> tasksToShowInList;

    Boolean fabIsOpen = false;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        tasksList = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_tasks_by_location, container, false);
        tasksRecList = view.findViewById(R.id.tasksRecycleList);
        switchLocationButton = view.findViewById(R.id.fab_option_location);
        showAllTasksButton = view.findViewById(R.id.fab_option_all_tasks);
        exposeOptionsButton = view.findViewById(R.id.fab);
        showStats = view.findViewById(R.id.fab_option_stats);
        showStatsText = view.findViewById(R.id.textview_stats);
        switchLocationText = view.findViewById(R.id.textview_location);
        showAllTasksText = view.findViewById(R.id.textview_all_tasks);
        tasksTitle = view.findViewById(R.id.showing_tasks_by);


        bottomDurationText = view.findViewById(R.id.tasksListBottomText);

        LogicHandler.updateCurrentUserLocation();

        currentLocation = LogicHandler.getCurrentLocation();
        locationIdToUserLocationMap = LogicHandler.getIdToUserLocationMap();
        locationIdToTasksMap = LogicHandler.getLocationIdToTasksMap();
        swichableLocationsWithRelevant = LogicHandler.getSwichableLocationsWithRelevant();
        swichableLocationsWithAll = LogicHandler.getSwichableLocationsWithAll();
        indexOfUserCurrentLocation = getIndeOfCurrentUserLocationInList(swichableLocationsWithRelevant);
        indexOfCurrentlySelectedLocation = LogicHandler.getLastSelectedLocationIndex(indexOfUserCurrentLocation);

        tasksToShowInList = new ArrayList<>();
        tasksAdapter = new TasksAdapter(view.getContext(), tasksToShowInList); //TODO: originally: MainActivity.this
        tasksRecList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        tasksRecList.setAdapter(tasksAdapter);


        updateAllUserFields();
        setFabButtons();

        return view;

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private int getIndeOfCurrentUserLocationInList(List<LocationWithTasksWrapper> swichableLocations) {

        List<UserLocation> locationsInList =  new ArrayList<>();

        swichableLocations.forEach(locationWithTasksWrapper -> {
            locationsInList.add(locationWithTasksWrapper.getLocation());
        });

        UserLocation currentLocation = LogicHandler.getCurrentLocation();
        if (currentLocation == null){
            LogicHandler.updateCurrentUserLocation();
        }

        int index = 0;
        int indexOfCurrent = 0;
        for (UserLocation location: locationsInList){
            if(location.getId().equals(LogicHandler.getCurrentLocation().getId())){
                indexOfCurrent = index;
            }
            index++;
        }
        return indexOfCurrent;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setSwitchLocationFunctionality() {
        switchLocationButton.setOnClickListener(click -> {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:

                            List<Task> tasksToShow;
                            if (LogicHandler.isShowingAllLocations()) {
                                tasksToShow = swichableLocationsWithAll.get(indexOfCurrentlySelectedLocation).getTasks();
                            }
                            else
                            {
                                tasksToShow = swichableLocationsWithRelevant.get(indexOfCurrentlySelectedLocation).getTasks();
                            }
                            updateUserTasksList(tasksToShow);
                            updateTitles();

                            String msg = "Location switched";
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
                        if (LogicHandler.locationWasFound) {
                            name += " (Current)";
                        } else {
                            name += " (Closest)";
                        }
                    }
                    locationsNames.add(name);
                });

                String[] namesToShowInWindow = new String[locationsNames.size()];
                locationsNames.toArray(namesToShowInWindow);


                alertBuilder.setSingleChoiceItems(namesToShowInWindow, indexOfCurrentlySelectedLocation, (dialogInterface, i) -> {
                    indexOfCurrentlySelectedLocation = i;
                    LogicHandler.updateLastSelectedLocationIndex(indexOfCurrentlySelectedLocation);
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
                Toast.makeText(getContext(), alertMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateAllUserFields() {

        boolean showAllLocations = LogicHandler.isShowingAllLocations();

        List<Task> tasks = new ArrayList<>();
        if (this.swichableLocationsWithAll.size() > 0) {

            if (showAllLocations) {
                tasks = this.swichableLocationsWithAll.get(indexOfCurrentlySelectedLocation).getTasks();
            } else {
                tasks = this.swichableLocationsWithRelevant.get(indexOfCurrentlySelectedLocation).getTasks();//this.locationIdToTasksMap.get(currentLocation.getId());
            }
        }

        updateUserTasksList(tasks);
        updateTitles();
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateUserTasksList(List<Task> tasksToShow) {

        tasksAdapter = new TasksAdapter(view.getContext(), tasksToShow);
        tasksRecList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        tasksRecList.setAdapter(tasksAdapter);
        tasksAdapter.notifyDataSetChanged();

        String durationMessage = "";
        if (tasksToShow.size() > 0) {
            int minutes = tasksToShow.stream().mapToInt(Task::getDuration).sum();
            if (minutes >= 60) {
                int hours = minutes / 60;
                minutes = minutes % 60;
                durationMessage = "Total Task time: " + hours + " hours, " + minutes + " minutes";
            } else {
                durationMessage = "Total Task time: " + minutes + " minutes";
            }
        }
        bottomDurationText.setText(durationMessage);
    }

    private void updateTitles(){
        updateUserTitle();
        updateTasksTitles();
    }
    private void updateUserTitle() {
        String userName = LogicHandler.getCurrentUser().getName();
        TextView title = view.findViewById(R.id.tasksListMainTitle);

        String userTitle = "Hello "+ userName + "!\n";

        boolean hasLocations = LogicHandler.getCurrentUser().getArrayOfLocations().size() > 0;
        boolean foundLocation = LogicHandler.userIsInOfOfHisLocations();

        String locationTitle;
        if (hasLocations){
            locationTitle = indexOfCurrentlySelectedLocation == indexOfUserCurrentLocation  && foundLocation?
                    "You are at ": "Showing tasks at ";
            locationTitle += swichableLocationsWithRelevant.get(indexOfCurrentlySelectedLocation).getLocation().getName();
        }
        else {
            locationTitle = "No Locations defined";
        }

        title.setText(userTitle + locationTitle);
        updateTasksTitles();
    }

    private void updateTasksTitles(){
        String tasksTitleText;
        if (LogicHandler.isShowingAllLocations()){
            tasksTitleText = "Showing all tasks in location";
        }
        else {
            String userPreference = LogicHandler.getCurrentUser().getSettings().getPriorityType() == UserSettings.PRIORITY_TYPE.URGENCY?
                    "urgency": "efficiency";
            tasksTitleText = "Showing tasks by " + userPreference;
        }
        tasksTitle.setText(tasksTitleText);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setFabButtons() {
        fab_close = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_rotate_anticlock);

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

        showStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                ((HomeActivity)view.getContext()).openStatisticsPage(bundle);
            }
        });

        showAllTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Task> tasksToShow;
                String msg;

                if(swichableLocationsWithAll.size() == 0){
                    Toast.makeText(getContext(), "No locations with tasks found..", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean showingAllTasksInLocation = LogicHandler.isShowingAllLocations();
                if (showingAllTasksInLocation){
                    tasksToShow = swichableLocationsWithRelevant.get(indexOfCurrentlySelectedLocation).getTasks();
                    msg = "Showing relevant tasks";

                } else {
                    tasksToShow = swichableLocationsWithAll.get(indexOfCurrentlySelectedLocation).getTasks();
                    msg = "Showing all tasks";
                }

                LogicHandler.setIsShowingAllLocations(!showingAllTasksInLocation);

                updateUserTasksList(tasksToShow);
                updateTitles();
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                hideFloating();

            }
        });
        setSwitchLocationFunctionality();
    }

    private void hideFloating(){
        switchLocationText.setVisibility(View.INVISIBLE);
        showAllTasksText.setVisibility(View.INVISIBLE);
        showStatsText.setVisibility(View.INVISIBLE);
        switchLocationButton.startAnimation(fab_close);
        showAllTasksButton.startAnimation(fab_close);
        showStats.startAnimation(fab_close);
        exposeOptionsButton.startAnimation(fab_anticlock);
        switchLocationButton.setClickable(false);
        showAllTasksButton.setClickable(false);
        showStats.setClickable(false);
        fabIsOpen = false;
    }

    private void showFloating(){
        boolean showingAllTasksInLocation = LogicHandler.isShowingAllLocations();

        if(showingAllTasksInLocation){
            showAllTasksText.setText("Show relevant tasks in location");
        } else {
            showAllTasksText.setText("Show all tasks in location");
        }

        switchLocationText.setVisibility(View.VISIBLE);
        showAllTasksText.setVisibility(View.VISIBLE);
        showStatsText.setVisibility(View.VISIBLE);
        switchLocationButton.startAnimation(fab_open);
        showAllTasksButton.startAnimation(fab_open);
        showStats.startAnimation(fab_open);
        exposeOptionsButton.startAnimation(fab_clock);
        switchLocationButton.setClickable(true);
        showAllTasksButton.setClickable(true);
        showStats.setClickable(true);
        fabIsOpen = true;
    }
}