package com.mta.ive.logic;

import android.location.Location;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mta.ive.MainActivity;
import com.mta.ive.logic.device.DeviceManager;
import com.mta.ive.logic.filter.TasksFilterer;
import com.mta.ive.logic.location.LocationWithTasksWrapper;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;
import com.mta.ive.logic.users.User;
import com.mta.ive.logic.users.UserSettings;
import com.mta.ive.logic.users.UsersHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogicHandler {
//    private static DatabaseReference reference;
    private static String currentUserEmail;
    private static Map<UserLocation, List<Task>> locationToTasksMap;
    private static Map<String, UserLocation> idToUserLocationMap;
    private static Map<String, List<Task>> locationIdToTasksMap;

    private static boolean showingAllTasksInLocation = false;
//    private static Location deviceLocation;



//    private static Map<UserLocation, >

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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        reloadUserData();
        loadSwichableLocations();
//        task.setTaskID(taskId);
        ref.setValue(task);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        reloadUserData();
        loadSwichableLocations();

        ref.setValue(userLocation);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void updateExistingTask(Task task){
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(String.valueOf(email.hashCode()))
                .child("tasks")
                .child(task.getId())
                .setValue(task);

        getCurrentUser().getTasks().put(task.getId(), task);
        reloadUserData();
        loadSwichableLocations();
    }

    public static void updateExistingLocation(UserLocation location){
        String email = getCurrentUserEmail();

        //optional without thread
        Thread thread = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(String.valueOf(email.hashCode()))
                        .child("locations")
                        .child(location.getId())
                        .setValue(location);
                getCurrentUser().getLocations().put(location.getId(), location);
                reloadUserData();
                loadSwichableLocations();
            }
        };
        thread.start();

    }

    public static void updateExistingUserSettings(UserSettings userSettings){
        String email = getCurrentUserEmail();

        Thread thread = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(String.valueOf(email.hashCode()))
                        .child("settings")
                        .setValue(userSettings);

                getCurrentUser().setSettings(userSettings);
                reloadUserData();
                loadSwichableLocations();

            }
        };
        thread.start();
    }

    public static User getCurrentUser(){
        return UsersHandler.getInstance().getCurrentUser();
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public static ArrayList<UserLocation> getAllLocationsWithTasks(){
//        ArrayList<UserLocation> locations = LogicHandler.getCurrentUser().getArrayOfLocations();
//        return locations.stream().filter(location -> location.getId().equals(getCurrentLocation().getId())
//                || getTasksOfCurrentUserInLocation(location).size() > 0)
//                .collect(Collectors.toCollection(ArrayList::new));
//
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Map<UserLocation, List<Task>> getCurrentUserLocationToTasksMap() {

        return locationToTasksMap;
    }

    public static Map<String, List<Task>> getLocationIdToTasksMap(){

        return locationIdToTasksMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Map<String, UserLocation> getIdToUserLocationMap() {

        return idToUserLocationMap;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Task> getTasksOfCurrentUserInLocation(UserLocation currentLocation){
//        ArrayList<Task> allTasks = getCurrentUser().getArrayOfTasks();
//        ArrayList<Task> filteredByLocationTasks = (ArrayList<Task>) allTasks.stream()
//                .filter(task -> task.isRelevantForLocation(currentLocation))// -> task.getLocations().contains(currentLocation))
//                .collect(Collectors.toList());

        //return filteredByLocationTasks;
//        return UsersHandler.getInstance(
        return locationIdToTasksMap.get(currentLocation.getId());
//        return locationToTasksMap.get(currentLocation);
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void loadUserLocationsAndTasksMaps() {
        locationToTasksMap = UsersHandler.getInstance().getLocationToTasksMap();

        idToUserLocationMap = new HashMap<>();
        locationIdToTasksMap = new HashMap<>();
        locationToTasksMap.forEach((location, tasks) -> {
            idToUserLocationMap.put(location.getId(), location);
            locationIdToTasksMap.put(location.getId(), tasks);
        });
//        loadSwichableLocations();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void reloadUserData(){
        UsersHandler.getInstance().loadLocationsToTasksMap();
        loadUserLocationsAndTasksMaps();
        //loadSwichableLocations();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Task> getAllTasksUnderOnlyThisLocation(UserLocation location){
//        ArrayList<Task> allTasks = getCurrentUser().getArrayOfTasks();
//        ArrayList<Task> relevantOnlyForThisLocationTasks = (ArrayList<Task>) allTasks.stream()
//                .filter(task -> task.isRelevantForLocation(location))
//                .filter(task -> task.getLocations().size() == 1)
//                .collect(Collectors.toList());

        return locationIdToTasksMap.get(location.getId()).stream().filter(task -> task.getLocations().size() == 1).collect(Collectors.toList());
//        return locationToTasksMap.get(location).stream().filter(task -> task.getLocations().size() == 1).collect(Collectors.toList());

//        return relevantOnlyForThisLocationTasks;
    }

    public static void setCurrentUser(User user){
        UsersHandler.getInstance().setCurrentUser(user);
    }


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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void deleteTaskById(String taskId){

        Task taskToArchive = getTaskById(taskId);
        taskToArchive.setStatus(Task.Status.ARCHIVED);
        updateExistingTask(taskToArchive);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        reloadUserData();
        loadSwichableLocations();
        updateCurrentUserLocation(); //?????
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void deleteTasksAssociatedWithLocation(UserLocation locationToDelete) {
        ArrayList<Task> allTasks = getCurrentUser().getArrayOfTasks();

        ArrayList<Task> tasksInLocation = (ArrayList<Task>) allTasks.stream()
                .filter(task -> task.isRelevantForLocation(locationToDelete))
                .collect(Collectors.toList());

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void markDoneTask(String taskId) {
        Task taskToArchive = getTaskById(taskId);
        taskToArchive.setStatus(Task.Status.DONE);
        updateExistingTask(taskToArchive);
    }

    public static void createUserIfNotExist(String email, String userName, MainActivity context) {

        currentUserEmail = email;
        UsersHandler.getInstance().createUserIfNotExist(email, userName, context);
    }

//    public static boolean userValidationComplated(){
//        return UsersHandler.getInstance().isUserValidationCompleted();
//    }

    private static List<LocationWithTasksWrapper> swichableLocationsWithAll;
    private static List<LocationWithTasksWrapper> swichableLocationsWithRelevant;

    public static List<LocationWithTasksWrapper> getSwichableLocationsWithRelevant() {
        return swichableLocationsWithRelevant;
    }

    public static List<LocationWithTasksWrapper> getSwichableLocationsWithAll() {
        return swichableLocationsWithAll;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void loadSwichableLocations(){
        swichableLocationsWithAll = new ArrayList<>();
        swichableLocationsWithRelevant = new ArrayList<>();


        if(locationToTasksMap.size() > 0) {

            locationToTasksMap.entrySet()
                    .forEach(locationToTasksEntry -> {
                        UserLocation location = locationToTasksEntry.getKey();
                        List<Task> tasksInLocation = locationToTasksEntry.getValue();

                        if (tasksInLocation.size() > 0) {
                            swichableLocationsWithAll.add(new LocationWithTasksWrapper(location, tasksInLocation));
                        }
                    });

            UserLocation currentLocation = getCurrentLocation();
            boolean containsCurrentLocation = swichableLocationsWithAll.stream()
                    .map(LocationWithTasksWrapper::getLocation)
                    .map(UserLocation::getId)
                    .anyMatch(locationId -> locationId.equals(currentLocation.getId()));

            if (!containsCurrentLocation) {
                LocationWithTasksWrapper locationToAdd = new LocationWithTasksWrapper(currentLocation, new ArrayList<>());
                swichableLocationsWithAll.add(0, locationToAdd);
            }
            swichableLocationsWithAll.sort((a,b) -> a.getLocation().getName()
                    .compareToIgnoreCase(b.getLocation().getName()));

            swichableLocationsWithRelevant = generateSwitchableWithRelevantOnly(swichableLocationsWithAll);

//            swichableLocationsWithAll.sort((a,b) -> a.getLocation().getName()
//                    .compareToIgnoreCase(b.getLocation().getName()));
        }
//        loadLocationIdToAllTasksEverCreated();
//
//        return swichableLocations;
//
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Map<String, List<Task>> getLocationIdToAllTasksCreated(){
        return UsersHandler.getInstance().getIdToAllCreatedTasks();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static List<LocationWithTasksWrapper> generateSwitchableWithRelevantOnly(List<LocationWithTasksWrapper> swichableLocationsWithAll) {
        List<LocationWithTasksWrapper> relevant = new ArrayList<>();

        UserSettings userSettings = UsersHandler.getInstance().getCurrentUser().getSettings();
//        swichableLocationsWithAll.forEach(locationWithAllTasksWrapper ->
        for (LocationWithTasksWrapper locationWithAllTasksWrapper: swichableLocationsWithAll){
            UserLocation location = locationWithAllTasksWrapper.getLocation();
            List<Task> allTasksInLocation = locationWithAllTasksWrapper.getTasks();
            List<Task> onlyRelevantTasks = TasksFilterer.getOnlyRelevantTasks(allTasksInLocation, userSettings);

            relevant.add(new LocationWithTasksWrapper(location, onlyRelevantTasks));
        }

        return relevant;
    }


    public static boolean locationWasFound = false;

    public static Location getDeviceLocation(){

        return DeviceManager.getInstance().getDeviceLocation();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void setCurrentLocationOfDevice(Location location) {
        DeviceManager.getInstance().setDeviceLocation(location);
        //updateCurrentUserLocation();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void updateCurrentUserLocation(){
        //TODO: find closest Userlocation to this location and decide if he is in valid radius(15)

        ArrayList<UserLocation> allLocations = UsersHandler.getInstance().getCurrentUser().getArrayOfLocations();

        Location location = DeviceManager.getInstance().getDeviceLocation();
        if (location != null) {

            if (allLocations != null && allLocations.size() > 0) {
                allLocations.sort((a, b) -> {
                    int distanceFromA = distanceBetweenLocations(a, location);

                    int distanceFromB = distanceBetweenLocations(b, location);

                    return distanceFromA - distanceFromB;
                });

                UserLocation closestUserLocation = allLocations.get(0);
                currentUserLocationByDevice = closestUserLocation;

                boolean closeEnough = deviceIsCloseEnoughToLocation(closestUserLocation, location);

                if (closeEnough){
                    locationWasFound = true;
                }

            }
        }
        else {
            if (allLocations.size() > 0){
                currentUserLocationByDevice = allLocations.get(0);
            }
        }

        //loadSwichableLocations();
    }

    final static int DISTANCE_THRESHOLD = 30;

    private static boolean deviceIsCloseEnoughToLocation(UserLocation closestUserLocation, Location deviceLocation) {
        int distance = distanceBetweenLocations(closestUserLocation, deviceLocation);

        return distance < DISTANCE_THRESHOLD;
    }

    private static int distanceBetweenLocations(UserLocation userLocation, Location deviceLocation){
        float[] distanceResults = new float[3];
        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                deviceLocation.getLatitude(), deviceLocation.getLongitude(), distanceResults);

        int distance = (int) distanceResults[0];

        return distance;
    }

    private static boolean userNextToHisLocation = false;
    private static UserLocation currentUserLocationByDevice = null;

    public static boolean userIsInOfOfHisLocations(){
        return userNextToHisLocation;
    }

    public static UserLocation getCurrentLocation(){
        /*//TODO: add logic to find what is our location
        ArrayList<UserLocation> allLocations = UsersHandler.getInstance().getCurrentUser().getArrayOfLocations();
        UserLocation currentLocation = null;

        //temp implementation
        if (allLocations.size() > 0)
            currentLocation = allLocations.get(0);

        return currentLocation;*/

        return currentUserLocationByDevice;
    }

    private static int lastSelectedIndex = -1;

    public static void updateLastSelectedLocationIndex(int newIndex){
        lastSelectedIndex = newIndex;
    }


    public static int getLastSelectedLocationIndex(int defaultIndex) {

        if(lastSelectedIndex == -1){
            lastSelectedIndex = defaultIndex;
        }

        return lastSelectedIndex;
//
//        int indexToReturn;
//        if (lastSelectedIndex != -1){
//            indexToReturn = lastSelectedIndex;
//        } else{
//            indexToReturn = defaultIndex;
//        }

//        return indexToReturn;
    }

    public static int getLastSelectedIndex() {
        return lastSelectedIndex;
    }

    public static boolean isShowingAllLocations(){
        return showingAllTasksInLocation;
    }

    public static void setIsShowingAllLocations(boolean newValue){
        showingAllTasksInLocation = newValue;
    }

    public static String getCurrentSelectedLocationId() {
        return null;
    }

    private static GoogleSignInClient googleClient;
    public static void setGoogleClient(GoogleSignInClient googleSignInClient) {
        googleClient = googleSignInClient;
    }

    public static void signOutGoogleIfNeeded(){
        if (googleClient != null ){
            googleClient.signOut();
        }
    }
}
