package com.mta.ive.logic.users;

import com.mta.ive.logic.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    String name;
    String email;

//    List<Task> tasks;
    Map<String, Task> tasks;
    List<Task> tasksList;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        tasks = new HashMap<>();
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
}
