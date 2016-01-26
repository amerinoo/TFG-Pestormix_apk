package com.example.albert.pestormix_apk.models;

import java.util.List;

/**
 * Created by Albert on 25/01/2016.
 */
public class Drink {
    String name;
    String description;
    boolean alcohol;

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

    public String getDescription() {
        return description;
    }

    public boolean isAlcohol() {
        return alcohol;
    }

    @Override
    public String toString() {
        return name;
    }
}
