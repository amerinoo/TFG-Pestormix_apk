package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.R;
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
        drinks.add(getDrink("Water", "", false, R.drawable.agua));
        drinks.add(getDrink("Coca Cola", "", false, R.drawable.cocacola));
        drinks.add(getDrink("Lemonade", "", false, R.drawable.limonada));
        drinks.add(getDrink("Orangeade", "", false, R.drawable.naranjada));
        drinks.add(getDrink("Ron Barcelo", "", true, R.drawable.ron_barcelo));
        return drinks;
    }

    private static Drink getDrink(String name, String description, boolean alcohol, int image) {
        Drink drink = new Drink();
        drink.setName(name);
        drink.setDescription(description);
        drink.setAlcohol(alcohol);
        drink.setImage(image);
        return drink;
    }

    public static List<Drink> getDrinks(Realm realm) {
        return DataController.getDrinks(realm);
    }


    public static Drink getDrinkByName(Realm realm, String name) {
        return DataController.getDrinkByName(realm, name);
    }
}
