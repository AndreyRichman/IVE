package com.mta.ive.logic.task;

import com.mta.ive.logic.location.UserLocation;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Task {
    private String id;
    private String name = "";
    private String description = "";
    private int duration = 0;
    private int priority = 0;
    private List<UserLocation> locations;
//    private LocalDate deadLineDate;
    private String deadLineDate;




//    private int urgency;
//    private int estimatedDurationMinutes;
//    private Location location ;
    //optional//
//    private Date dueDate;

    public Task() {
    }

    public Task(String name, String description, int duration){
        this.name = name;
        this.description = description;
        this.duration = duration;
    }

//    public LocalDate getDeadLineDate() {
//        return deadLineDate;
//    }
//
//    public void setDeadLineDate(LocalDate deadLineDate) {
//        this.deadLineDate = deadLineDate;
//    }


    public String getDeadLineDate() {
        return deadLineDate;
    }

    public void setDeadLineDate(String deadLineDate) {
        this.deadLineDate = deadLineDate;
    }

    public List<UserLocation> getLocations() {
        return locations;
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

//    ListView tasksList;
//    populateDummyTasksMap();
//        tasksList = findViewById(R.id.allTasksListView);
//                List<String> tasksTitles = tasks.values().stream().map(Task::getTitle).collect(Collectors.toCollection(ArrayList::new));

//    public Map<String, Task> tasks = new HashMap();
//
//    private void populateDummyTasksMap() {
//        tasks.put("1", new Task("contact asos about delivery", 3, 5, new Location("")));
//        tasks.put("2", new Task("buy groceries from the supermarket", 2, 45, new Location("")));
//        tasks.put("3", new Task("do homework in calculus", 1, 150, new Location("")));
//        tasks.put("4", new Task("send CV to companies", 3, 60, new Location("")));
//    }
