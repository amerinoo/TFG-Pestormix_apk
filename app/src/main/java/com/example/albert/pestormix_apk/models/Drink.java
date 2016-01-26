package com.example.albert.pestormix_apk.models;

import io.realm.RealmObject;

/**
 * Created by Albert on 25/01/2016.
 */
public class Drink extends RealmObject {
    private String name;
    private String description;
    private boolean alcohol;

    public Drink() {
    }

    public Drink(String name, String description) {
        this(name, description, false);
    }

    public Drink(String name, String description, boolean alcohol) {
        this.name = name;
        this.description = description;
        this.alcohol = alcohol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAlcohol() {
        return alcohol;
    }

    public void setAlcohol(boolean alcohol) {
        this.alcohol = alcohol;
    }
}
