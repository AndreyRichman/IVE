package com.mta.ive.logic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;
import com.mta.ive.logic.users.User;
import com.mta.ive.logic.users.UserSettings;
import com.mta.ive.logic.users.UsersHandler;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LogicHandler {
    private static DatabaseReference reference;
    private static String currentUserEmail;

//    public static void saveUser(User user){
//
//    }

//    public static void createUserIfNotExist(String email, String userName){
//
//    }
//    public static User getUserByEmail(String email, String userName){
//
//        return null;
//    }

    public static void saveTask(Task task){
//        int id = task.getId();
//        reference = FirebaseDatabase.getInstance().getReference()
//                .child("task").child(String.valueOf(id));
//
//        reference.setValue(task);

        //add task to user
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//        FirebaseDatabase.getInstance().getReference()
//                .child("users")
//                .child(String.valueOf(email.hashCode()))
//                .child("tasks")
//                .push().setValue(task);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(String.valueOf(email.hashCode()))
                .child("tasks")
                .push();

//                .child(task.getId())
//                .setValue(task);
        String taskId = ref.getKey();
        task.setId(taskId);
        getCurrentUser().addTask(task);
//        task.setTaskID(taskId);
        ref.setValue(task);
    }

    public static void saveLocation(UserLocation userLocation){
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(String.valueOf(email.hashCode()))
                .child("locations")
                .push();

        String locationId = ref.getKey();
        userLocation.setId(locationId);
        getCurrentUser().addLocation(userLocation);

        ref.setValue(userLocation);
    }

    public static void updateExistingTask(Task task){
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(String.valueOf(email.hashCode()))
                .child("tasks")
                .child(task.getId())
                .setValue(task);

        getCurrentUser().getTasks().put(task.getId(), task);
    }

    public static void updateExistingLocation(UserLocation location){
        String email = getCurrentUserEmail();

        //optional without thread
        Thread thread = new Thread() {
            @Override
            public void run() {

                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(String.valueOf(email.hashCode()))
                        .child("locations")
                        .child(location.getId())
                        .setValue(location);
                getCurrentUser().getLocations().put(location.getId(), location);
            }
        };
        thread.start();

    }

    public static void updateExistingUserSettings(UserSettings userSettings){
        String email = getCurrentUserEmail();

        Thread thread = new Thread() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(String.valueOf(email.hashCode()))
                        .child("settings")
                        .setValue(userSettings);

                getCurrentUser().setSettings(userSettings);
            }
        };
        thread.start();
    }

    public static User getCurrentUser(){
        return UsersHandler.getInstance().getCurrentUser();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<UserLocation> getAllLocationsWithTasks(){
        ArrayList<UserLocation> locations = LogicHandler.getCurrentUser().getArrayOfLocations();
        return locations.stream().filter(location -> location.getId().equals(getCurrentLocation().getId())
                || getTasksOfCurrentUserInLocation(location).size() > 0)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Task> getTasksOfCurrentUserInLocation(UserLocation currentLocation){
        ArrayList<Task> allTasks = getCurrentUser().getArrayOfTasks();
//        UserLocation currentLocation = getCurrentLocation();
        ArrayList<Task> filteredByLocationTasks = (ArrayList<Task>) allTasks.stream()
                .filter(task -> task.isRelevantForLocation(currentLocation))// -> task.getLocations().contains(currentLocation))
                .collect(Collectors.toList());

        return filteredByLocationTasks;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Task> getAllTasksUnderOnlyThisLocation(UserLocation location){
        ArrayList<Task> allTasks = getCurrentUser().getArrayOfTasks();
        ArrayList<Task> relevantOnlyForThisLocationTasks = (ArrayList<Task>) allTasks.stream()
                .filter(task -> task.isRelevantForLocation(location))
                .filter(task -> task.getLocations().size() == 1)
                .collect(Collectors.toList());

        return relevantOnlyForThisLocationTasks;
    }

    public static void setCurrentUser(User user){
        UsersHandler.getInstance().setCurrentUser(user);
    }

    public static UserLocation getCurrentLocation(){
        ArrayList<UserLocation> allLocations = UsersHandler.getInstance().getCurrentUser().getArrayOfLocations();
        UserLocation currentLocation = null;

        //temp implementation
        if (allLocations.size() > 0)
            currentLocation = allLocations.get(0);

        return currentLocation;
    }

//    public static DatabaseReference getAllTasksDBReference(){
//        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//
//        return FirebaseDatabase.getInstance().getReference()
//                .child("users")
//                .child(String.valueOf(email.hashCode()))
//                .child("tasks");
//
//    }

//    public static DatabaseReference getAllLocationsDBReference(){
//        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//
//        return FirebaseDatabase.getInstance().getReference()
//                .child("users")
//                .child(String.valueOf(email.hashCode()))
//                .child("locations");
//    }

//    public static DatabaseReference getTaskDBReferenceById(String taskId){
//        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//
//        return FirebaseDatabase.getInstance().getReference()
//                .child("users")
//                .child(String.valueOf(email.hashCode()))
//                .child("tasks")
//                .child(String.valueOf(taskId));
//
//    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public static DatabaseReference getCurrentUserDBReferenceById(String userEmail){
//        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        return FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(String.valueOf(userEmail.hashCode()));
    }

    public static Task getTaskById(String id){
        return getCurrentUser().getTasks().get(id);
    }

    public static UserLocation getLocationById(String id){
        return getCurrentUser().getLocations().get(id);
    }

    public static void deleteTaskById(String taskId){

        Task taskToArchive = getTaskById(taskId);
        taskToArchive.setStatus(Task.Status.ARCHIVED);
        updateExistingTask(taskToArchive);
//
//        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//
//        FirebaseDatabase.getInstance().getReference()
//                .child("users")
//                .child(String.valueOf(email.hashCode()))
//                .child("tasks")
//                .child(String.valueOf(taskId)).removeValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void deleteLocationById(String locationId){
        UserLocation locationToDelete = getLocationById(locationId);

        String email = getCurrentUserEmail();

        FirebaseDatabase.getInstance().getReference()
            .child("users")
            .child(String.valueOf(email.hashCode()))
            .child("locations")
            .child(String.valueOf(locationId)).removeValue();


        deleteTasksAssociatedWithLocation(locationToDelete);
        getCurrentUser().getLocations().remove(locationId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static void deleteTasksAssociatedWithLocation(UserLocation locationToDelete) {
        ArrayList<Task> allTasks = getCurrentUser().getArrayOfTasks();

        ArrayList<Task> tasksInLocation = (ArrayList<Task>) allTasks.stream()
                .filter(task -> task.isRelevantForLocation(locationToDelete))
                .collect(Collectors.toList());

//        List<Task> tasksToDelete = filteredByLocationTasks.stream()
//                .filter(task -> task.getLocations().size() == 1).collect(Collectors.toList());

//        List<Task> tasksToUpdate =
        tasksInLocation.forEach(task -> {
            if (task.getLocations().size() == 1 ){
                task.setStatus(Task.Status.ARCHIVED);
            }
            else {
                task.removeLocation(locationToDelete);
            }
            updateExistingTask(task);
        });

    }

    public static void markDoneTask(String taskId) {
        Task taskToArchive = getTaskById(taskId);
        taskToArchive.setStatus(Task.Status.DONE);
        updateExistingTask(taskToArchive);
    }

    public static void createUserIfNotExist(String email, String userName) {

        currentUserEmail = email;
        UsersHandler.getInstance().createUserIfNotExist(email, userName);
    }

}
