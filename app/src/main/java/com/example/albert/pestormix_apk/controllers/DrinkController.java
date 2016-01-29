package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.models.Drink;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Albert on 25/01/2016.
 */
public abstract class DrinkController {

    public static List<Drink> init() {
        List<Drink> drinks = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Drink drink = new Drink();
            drink.setName("Drink " + i);
            drinks.add(drink);
        }
        return drinks;
    }

    public static List<Drink> getDrinks(Realm realm) {
        return DataController.getDrinks(realm);
    }


    public static Drink getDrinkByName(Realm realm, String name) {
        return DataController.getDrinkByName(realm, name);
    }
}
