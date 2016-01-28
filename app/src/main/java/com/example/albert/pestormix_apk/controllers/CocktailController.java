package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Drink;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Albert on 27/01/2016.
 */
public abstract class CocktailController {
    public static List<Cocktail> init(Realm realm) {
        List<Drink> drinks = DrinkController.getDrinks(realm);
        List<Cocktail> cocktails = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Cocktail cocktail = new Cocktail();
            cocktail.setName("Cocktail " + i);
            addDrink(cocktail, drinks.get(i % drinks.size()));
            cocktails.add(cocktail);
        }
        return cocktails;
    }

    public static void setName(Cocktail cocktail, String name) {
        cocktail.setName(name);
    }

    public static void setDescription(Cocktail cocktail, String description) {
        cocktail.setDescription(description);
    }

    public static void addDrink(Cocktail cocktail, Drink drink) {
        cocktail.getDrinks().add(drink);
    }

    public static String getDrinksAsString(Cocktail cocktail) {
        return getDrinksAsString(cocktail,",");
    }
    public static String getDrinksAsString(Cocktail cocktail,String separator) {
        String drinks = "";
        for (Drink drink : cocktail.getDrinks()) drinks += drink.getName() + separator;
        drinks = drinks.substring(0, drinks.length() - separator.length()); //Delete the last ","
        return drinks;
    }

    public static List<Drink> getDrinks(Cocktail cocktail) {
        RealmList<Drink> results = cocktail.getDrinks();
        List<Drink> drinks = new ArrayList<>();
        for (Drink drink : results) drinks.add(drink);
        return drinks;
    }

    public static void setDrinksFromString(Realm realm, Cocktail cocktail, String drinksString) {
        for (String name : drinksString.split(",")) {
            addDrink(cocktail, DrinkController.getDrinkByName(realm, name));
        }
    }

    public static List<Drink> getDrinksFromString(Realm realm, String drinksString) {
        List<Drink> drinks = new ArrayList<>();
        for (String name : drinksString.split(",")) {
            drinks.add(DrinkController.getDrinkByName(realm, name));
        }
        return drinks;
    }
}
