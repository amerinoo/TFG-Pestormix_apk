package com.example.albert.pestormix_apk.repositories;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.controllers.DataController;
import com.example.albert.pestormix_apk.models.Drink;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Albert on 25/01/2016.
 */
public abstract class DrinkRepository {

    public static List<Drink> init() {
        List<Drink> drinks = new ArrayList<>();
        drinks.add(getDrink(getStringResource(R.string.drink_water),
                getStringResource(R.string.drink_water_description), false, R.drawable.agua));
        drinks.add(getDrink(getStringResource(R.string.drink_coca_cola),
                getStringResource(R.string.drink_coca_cola_description), false, R.drawable.cocacola));
        drinks.add(getDrink(getStringResource(R.string.drink_lemonade),
                getStringResource(R.string.drink_lemonade_description), false, R.drawable.limonada));
        drinks.add(getDrink(getStringResource(R.string.drink_orangeade),
                getStringResource(R.string.drink_orangeade_description), false, R.drawable.naranjada));
        drinks.add(getDrink(getStringResource(R.string.drink_ron),
                getStringResource(R.string.drink_ron_description), true, R.drawable.ron_barcelo));
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

    public static List<Drink> getDrinksFromString(Realm realm, String drinksString) {
        List<Drink> drinks = new ArrayList<>();
        for (String name : drinksString.split(",")) {
            drinks.add(getDrinkByName(realm, name));
        }
        return drinks;
    }
}
