package com.mta.ive.logic.location;

import com.mta.ive.logic.task.Task;

import java.util.List;

public class LocationWithTasksWrapper {

    UserLocation location;
    List<Task> tasks;

    public LocationWithTasksWrapper() {
    }

    public LocationWithTasksWrapper(UserLocation location, List<Task> tasks) {
        this.location = location;
        this.tasks = tasks;
    }

    public UserLocation getLocation() {
        return location;
    }

    public void setLocation(UserLocation location) {
        this.location = location;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
