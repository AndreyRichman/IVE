package com.mta.ive.logic.users;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mta.ive.MainActivity;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersHandler {
    private static UsersHandler instance;
    private User currentUser;

    private Map<UserLocation, List<Task>> locationToTasksMap;
    private Map<String, UserLocation> idToLocationMap;

    public static UsersHandler getInstance(){
        if (instance == null){
            instance = new UsersHandler();
        }
        return instance;
    }

//    public boolean userExist(String email){
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("users");
//
//
//
//
//        return false;
//    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<UserLocation, List<Task>> getLocationToTasksMap() {
        if (locationToTasksMap == null){
            loadLocationsToTasksMap();
        }
        return locationToTasksMap;
    }

    Map<String, List<Task>> idToAllCreatedTasks;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadLocationsToTasksMap() {
        idToLocationMap = new HashMap<>();
        locationToTasksMap = new HashMap<>();
        idToAllCreatedTasks = new HashMap<>();

        getCurrentUser().getArrayOfLocations()
                .forEach(location -> {
                    idToLocationMap.put(location.getId(), location);
                    locationToTasksMap.put(location, new ArrayList<>());
                    idToAllCreatedTasks.put(location.getId(), new ArrayList<>());
                });

        getCurrentUser().getArrayOfTasks().forEach(task -> {
            for (UserLocation relevantLocation: task.getLocations()) {
                String locationId = relevantLocation.getId();
                UserLocation keyLocation = idToLocationMap.get(locationId);
                locationToTasksMap.get(keyLocation).add(task);
            }
        });

        getCurrentUser().getArrayOfAllCreatedTasks().forEach(task -> {
            for (UserLocation relevantLocation: task.getLocations()) {
                String locationId = relevantLocation.getId();
                UserLocation keyLocation = idToLocationMap.get(locationId);
                if (keyLocation != null) {
                    idToAllCreatedTasks.get(keyLocation.getId()).add(task);
                }
            }
        });
//
//        //getArrayOfAllCreatedTasks
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, List<Task>> getIdToAllCreatedTasks(){
        if (idToAllCreatedTasks == null){
            loadLocationsToTasksMap();
        }

        return idToAllCreatedTasks;
    }

//    public User getUser(String email){
//        return null;
//    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

//    boolean userValidationCompleted = false;
//
//    public boolean isUserValidationCompleted() {
//        return userValidationCompleted;
//    }

    public void createUserIfNotExist(String email, String userName, MainActivity context) {
        /*
        * reference = FirebaseDatabase.getInstance().getReference()
                .child("task").child(String.valueOf(id));
                * */
//        FirebaseDatabase.getInstance().getReference()
//                .child("users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
//
        FirebaseDatabase.getInstance().getReference()
                .child("users").child(String.valueOf(email.hashCode())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    createUser(email, userName);

                }
                context.loadUserFromDB();
//                userValidationCompleted = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createUser(String email, String userName){
        User userToAdd = new User(userName, email);


        FirebaseDatabase.getInstance().getReference().child("users").child(String.valueOf(email.hashCode())).setValue(userToAdd);
        currentUser = userToAdd;

    }
}
