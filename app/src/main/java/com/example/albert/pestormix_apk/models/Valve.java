package com.example.albert.pestormix_apk.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Albert on 02/02/2016.
 */
public class Valve extends RealmObject {
    @PrimaryKey
    private int id;
    private int drinkPosition;
    private String drinkName;
    private boolean drinkAlcohol;

    public Valve() {
    }

    public boolean isDrinkAlcohol() {
        return drinkAlcohol;
    }

    public void setDrinkAlcohol(boolean drinkAlcohol) {
        this.drinkAlcohol = drinkAlcohol;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrinkPosition() {
        return drinkPosition;
    }

    public void setDrinkPosition(int drinkPosition) {
        this.drinkPosition = drinkPosition;
    }
}
