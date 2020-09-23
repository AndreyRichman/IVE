package com.mta.ive.logic.users;


public class UserSettings {
    private final static int DEFAULT_START_HOUR = 9;
    private final static int DEFAULT_END_HOUR = 22;
    private int dayStartHour = DEFAULT_START_HOUR;
    private int dayStartMinutes = 0;
    private int dayEndHour = DEFAULT_END_HOUR;
    private int dayEndMinutes = 0;
    private PRIORITY_TYPE priorityType = PRIORITY_TYPE.URGENCY;

    enum PRIORITY_TYPE { QUANTITY, URGENCY};

    public UserSettings() {
    }

    public int getDayStartHour() {
        return dayStartHour;
    }

    public void setDayStartHour(int dayStartHour) {
        this.dayStartHour = dayStartHour;
    }

    public int getDayStartMinutes() {
        return dayStartMinutes;
    }

    public void setDayStartMinutes(int dayStartMinutes) {
        this.dayStartMinutes = dayStartMinutes;
    }

    public int getDayEndHour() {
        return dayEndHour;
    }

    public void setDayEndHour(int dayEndHour) {
        this.dayEndHour = dayEndHour;
    }

    public int getDayEndMinutes() {
        return dayEndMinutes;
    }

    public void setDayEndMinutes(int dayEndMinutes) {
        this.dayEndMinutes = dayEndMinutes;
    }


    public PRIORITY_TYPE getPriorityType() {
        return priorityType;
    }

    public void setPriorityType(PRIORITY_TYPE priorityType) {
        this.priorityType = priorityType;
    }
}
