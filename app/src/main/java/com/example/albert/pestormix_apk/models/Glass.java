package com.example.albert.pestormix_apk.models;

import io.realm.RealmObject;

/**
 * Created by Albert on 07/02/2016.
 */
public class Glass extends RealmObject {
    private String name;

    public Glass() {
        name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
