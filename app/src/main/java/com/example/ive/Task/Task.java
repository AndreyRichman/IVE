package com.example.ive.Task;

import android.location.Location;

import java.util.Date;

public class Task {
    private String m_taskID;
    private String m_Title = "";
    private int m_urgency;
    private int m_estimatedDurationMinutes;
    private Location m_location ;
    //optional//
    private Date m_dueDate;
    private String m_Description = "";

    public Task() {

    }


    public String getTitle() {
        return m_Title;
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
