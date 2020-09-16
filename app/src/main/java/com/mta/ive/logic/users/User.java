package com.mta.ive.logic.users;

import android.location.Location;
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

//    List<Task> tasks;
    Map<String, Task> tasks;
    Map<String, UserLocation> locations;


    List<Task> tasksList;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        tasks = new HashMap<>();
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
    public ArrayList<Task> getArrayOfTasks(){
        return new ArrayList<>(this.tasks.values());
    }

    public ArrayList<UserLocation> getArrayOfLocations(){
        return new ArrayList<>(this.locations.values());
    }
}
