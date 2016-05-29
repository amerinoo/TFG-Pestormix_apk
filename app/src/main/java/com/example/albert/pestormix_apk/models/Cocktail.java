package com.example.albert.pestormix_apk.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Albert on 25/01/2016.
 */
public class Cocktail extends RealmObject {
    @PrimaryKey
    private String name;
    private String description;
    private boolean alcohol;
    private RealmList<Drink> drinks;


    public Cocktail() {
        name = "";
        description = "";
        alcohol = false;
        drinks = new RealmList<>();
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
        if (description == null) description = "";
        this.description = description;
    }

    public RealmList<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(RealmList<Drink> drinks) {
        this.drinks = drinks;
    }

    public boolean isAlcohol() {
        return alcohol;
    }

    public void setAlcohol(boolean alcohol) {
        this.alcohol = alcohol;
    }
}
