package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.models.Drink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Albert on 25/01/2016.
 */
public abstract class DrinkController {
    private static Map<String, Drink> drinks = null;

    public static List<Drink> getDrinks() {
        checkNull();
        List<Drink> list = new ArrayList<>();
        list.addAll(drinks.values());
        return list;
    }

    private static void checkNull() {
        if (drinks == null) {
            generateDrinks();
        }
    }

    public static Drink getDrinkById(String id){
        checkNull();
        return drinks.get(id);
    }

    private static void generateDrinks() {
        drinks = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            Drink drink = new Drink("Drink " + i, "");
            drinks.put(drink.getName(), drink);
        }
    }
}
