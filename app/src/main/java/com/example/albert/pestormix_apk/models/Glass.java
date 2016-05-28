package com.example.albert.pestormix_apk.models;

import io.realm.RealmObject;

/**
 * Created by Albert on 07/02/2016.
 */
public class Glass extends RealmObject {
    private String name;
    private int capacity;

    public Glass() {
        name = "";
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
