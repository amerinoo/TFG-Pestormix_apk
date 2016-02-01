package com.example.albert.pestormix_apk.models;

import com.example.albert.pestormix_apk.R;

import io.realm.RealmObject;

/**
 * Created by Albert on 25/01/2016.
 */
public class Drink extends RealmObject {
    private String name;
    private String description;
    private boolean alcohol;
    private int image;

    public Drink() {
        this.name = "";
        this.description = "";
        this.alcohol = false;
        this.image = R.drawable.agua;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
