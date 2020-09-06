package com.mta.ive.logic.task;

import android.location.Location;

import java.util.Date;

public class Task {
    private int taskID;
    private String name = "";
    private String description = "";
    private String duration = "";

    public static int uniqueTaskId = 777;

    private int urgency;
    private int estimatedDurationMinutes;
    private Location location ;
    //optional//
    private Date dueDate;

    public Task() {
        taskID = uniqueTaskId++;
    }

    public Task(String name, String description, String duration){
        this.name = name;
        this.description = description;
        this.duration = duration;
    }


    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
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
