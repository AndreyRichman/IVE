package com.mta.ive.logic.task;

import android.location.Location;

import java.util.Date;

public class Task {
    private String taskID;
    private String title = "";
    private int urgency;
    private int estimatedDurationMinutes;
    private Location location ;
    //optional//
    private Date dueDate;
    private String description = "";

    public Task() {

    }


    public String getTitle() {

        return title;
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
