package com.example.ive.logic;

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

    public Task(String i_title, int i_urgency, int i_estimatedDurationMinutes, Location i_location) {
        m_Title = i_title;
        m_urgency = i_urgency;
        m_estimatedDurationMinutes = i_estimatedDurationMinutes;
        m_location = i_location;
    }


    public String getTitle() {
        return m_Title;
    }
}
