package com.mta.ive.wizard;

import com.mta.ive.Task.Task;
import com.mta.ive.deviceManager.DeviceManager;
import java.util.List;

public class Wizard {
    public Wizard (){};
    void setUserPreference(/*Preference*/){};
    void setDeviceManager(DeviceManager dm){};
    List<Task> getRelevantTasks(List<Task> tasks){return null;};
}
