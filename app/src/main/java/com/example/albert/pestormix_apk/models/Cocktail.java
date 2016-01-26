package com.example.albert.pestormix_apk.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Albert on 25/01/2016.
 */
public class Cocktail implements Serializable {
    String name;
    String description;
    boolean alcohol;
    List<Drink> drinks;

    public Cocktail() {
        name = "";
        description = "";
        alcohol = false;
        drinks = new ArrayList<>();
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

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void addDrink(Drink drink) {
        drinks.add(drink);
    }

    public void removeDrink(Drink drink) {
        drinks.remove(drink);
    }

    public void setAlcohol(boolean alcohol) {
        this.alcohol = alcohol;
    }

    public boolean isAlcohol() {
        return alcohol;
    }


}
