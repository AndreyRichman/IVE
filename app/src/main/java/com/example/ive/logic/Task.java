package com.example.ive.logic;

import android.location.Location;

import java.util.Date;

public class Task {
    private String m_taskID;
    private String m_Title = "";
    private int m_urgency;
    private int m_estimatedDuration;
    private Location m_location;
    //optional//
    private Date m_dueDate;
    private String m_Description = "";

    public Task(String i_title, int i_urgency,int i_estimatedDuration) {
    }


}
