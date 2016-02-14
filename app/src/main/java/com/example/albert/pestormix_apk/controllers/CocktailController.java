package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.exceptions.CocktailFormatException;
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

    public static void generateCocktails(Realm realm) {
        DataController.generateCocktails(realm);
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
        return getDrinksAsString(cocktail, ",");
    }

    public static Cocktail processData(PestormixMasterActivity activity, String data) {
        if (data != null && !data.equals("")) {
            Cocktail cocktail;
            try {
                cocktail = CocktailController.getCocktailFromString(activity.getRealm(), data);
            } catch (CocktailFormatException e) {
                activity.showToast(activity.getString(R.string.cocktail_format_error));
                return null;
            }
            if (CocktailController.cocktailExist(activity.getRealm(), cocktail)) {
                activity.showToast(activity.getString(R.string.cocktail_name_already_exist));
            } else {
                return cocktail;
            }
        }
        return null;
    }

    public static Cocktail getCocktailFromString(Realm realm, String data) throws CocktailFormatException {
        String[] split = data.split(",", 4);
        if (split.length < 4 || !split[0].equals("Pestormix")) {
            throw new CocktailFormatException();
        }
        Cocktail cocktail = new Cocktail();
        cocktail.setName(split[1]);
        cocktail.setDescription(split[2]);
        setDrinksFromString(realm, cocktail, split[3]);
        return cocktail;
    }

    public static String getDrinksAsString(Cocktail cocktail, String separator) {
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

    public static List<Drink> getDrinksFromString(Realm realm, String drinksString) {
        List<Drink> drinks = new ArrayList<>();
        for (String name : drinksString.split(",")) {
            drinks.add(DrinkController.getDrinkByName(realm, name));
        }
        return drinks;
    }

    public static void setDrinksFromString(Realm realm, Cocktail cocktail, String drinksString) {
        for (String name : drinksString.split(",")) {
            addDrink(cocktail, DrinkController.getDrinkByName(realm, name));
        }
    }

    public static void addCocktailToDB(Realm realm, Cocktail cocktail) {
        DataController.addCocktail(realm, cocktail);
    }

    public static Cocktail getCocktailByName(Realm realm, String cocktailName) {
        return DataController.getCocktailByName(realm, cocktailName);
    }

    public static List<String> getCocktailsNames(Realm realm) {
        return DataController.getCocktailsNames(realm);
    }

    public static List<Cocktail> getCocktails(Realm realm) {
        return DataController.getCocktails(realm);
    }

    public static boolean cocktailExist(Realm realm, Cocktail cocktail) {
        return DataController.cocktailExist(realm, cocktail);
    }

    public static void updateCocktail(Realm realm, Cocktail cocktail, String oldName) {
        DataController.updateCocktail(realm, cocktail, oldName);
    }

    public static void removeCocktailByName(Realm realm, String cocktailName) {
        DataController.removeCocktailByName(realm, cocktailName);
    }

    public static void removeAllCocktails(Realm realm) {
        DataController.removeAllCocktails(realm);
    }
}
