package com.mta.ive.vm.adapter.multiselect;


import com.mta.ive.logic.location.UserLocation;

public class Item {
    private String name;
    private String value;
    private UserLocation location;

    public Item() {
    }

    public Item(String name, String value, UserLocation location) {
        this.name = name;
        this.value = value;
        this.location = location;
    }

    public static Item builder() {
        return new Item();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLocation(UserLocation location) {
        this.location = location;
    }

    public UserLocation getLocation() {
        return location;
    }
}
