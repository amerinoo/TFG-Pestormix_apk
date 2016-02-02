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
    private Drink drink;

    public Valve() {
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

    public Drink getDrink() {
        return drink;
    }

    public void setDrink(Drink drink) {
        this.drink = drink;
    }
}
