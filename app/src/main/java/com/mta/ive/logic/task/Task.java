package com.mta.ive.logic.task;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mta.ive.logic.location.UserLocation;

import java.util.List;

public class Task {
    private String id;
    private String name = "";
    private String description = "";
    private int duration = 0;
    private int priority = 0;
    private List<UserLocation> locations;
    private String deadLineDate;
    private Status status = Status.ACTIVE;




    public enum Status { ACTIVE, ARCHIVED, DONE}

    public Task() {
    }

    public Task(String name, String description, int duration){
        this.name = name;
        this.description = description;
        this.duration = duration;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeLocation(UserLocation locationToDelete) {

        locations.removeIf(location -> location.getId().equals(locationToDelete.getId()));
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isActive(){
        return this.status == Status.ACTIVE;
    }

    public String getDeadLineDate() {
        return deadLineDate;
    }

    public void setDeadLineDate(String deadLineDate) {
        this.deadLineDate = deadLineDate;
    }

    public List<UserLocation> getLocations() {
        return locations;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean isRelevantForLocation(UserLocation specificLocation){
        return this.locations.stream().map(UserLocation::getName).anyMatch(name -> name.equals(specificLocation.getName()));
    }

    public void setLocations(List<UserLocation> locations) {
        this.locations = locations;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
