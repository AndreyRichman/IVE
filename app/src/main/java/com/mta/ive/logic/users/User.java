package com.mta.ive.logic.users;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class User {

    String name;
    String email;

    Map<String, Task> tasks;
    Map<String, UserLocation> locations;


    List<Task> tasksList;
    private UserSettings settings;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public User() {
        tasks = new HashMap<>();
        locations = new HashMap<>();
        settings = new UserSettings();
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        tasks = new HashMap<>();
        locations = new HashMap<>();
        settings = new UserSettings();
    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        this.settings = settings;
    }

    public Map<String, UserLocation> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, UserLocation> locations) {
        this.locations = locations;
    }

    public List<Task> getTasksList() {
        return tasksList;
    }

    public void setTasksList(List<Task> tasksList) {
        this.tasksList = tasksList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, Task> tasks) {
        this.tasks = tasks;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Task> getArrayOfTasks()
    {
        ArrayList<Task> activeTasks = new ArrayList<>();
        if(this.tasks != null && this.tasks.size() > 0){
            activeTasks = new ArrayList<>(this.tasks.values());
        }

        return activeTasks.stream().filter(Task::isActive).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<UserLocation> getArrayOfLocations()
    {
        if(this.locations == null || this.locations.size() < 1)
        {
            return new ArrayList<>();
        }

        else
        {
            return new ArrayList<>(this.locations.values());
        }
    }

    public void addTask(Task taskToAdd){
        this.tasks.put(taskToAdd.getId(), taskToAdd);
    }

    public void addLocation(UserLocation userLocation){
        this.locations.put(userLocation.getId(), userLocation);
    }

    public List<Task> getArrayOfAllCreatedTasks(){
        List<Task> allTasks = new ArrayList<>();
        if(this.tasks != null && this.tasks.size() > 0){
            allTasks = new ArrayList<>(this.tasks.values());
        }

        return allTasks;
    }
}
