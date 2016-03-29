package com.example.albert.pestormix_apk.repositories;

import android.content.Context;
import android.content.Intent;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.controllers.DataController;
import com.example.albert.pestormix_apk.exceptions.CocktailFormatException;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Drink;
import com.example.albert.pestormix_apk.utils.Constants;
import com.example.albert.pestormix_apk.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Albert on 27/01/2016.
 */
public abstract class CocktailRepository {

    public static List<Cocktail> init(Realm realm) {
        List<Cocktail> cocktails = new ArrayList<>();
        generateCocktail(realm, cocktails, Utils.getStringResource(R.string.cocktail_water),
                Utils.getStringResource(R.string.cocktail_water_description), Utils.getStringResource(R.string.cocktail_water));
        generateCocktail(realm, cocktails, Utils.getStringResource(R.string.cocktail_coca_cola),
                Utils.getStringResource(R.string.cocktail_coca_cola_description),
                Utils.getStringResource(R.string.drink_coca_cola));
        generateCocktail(realm, cocktails, Utils.getStringResource(R.string.cocktail_lemonade),
                Utils.getStringResource(R.string.cocktail_lemonade_description),
                Utils.getStringResource(R.string.drink_lemonade));
        generateCocktail(realm, cocktails, Utils.getStringResource(R.string.cocktail_orangeade),
                Utils.getStringResource(R.string.cocktail_orangeade_description),
                Utils.getStringResource(R.string.drink_orangeade));
        generateCocktail(realm, cocktails, Utils.getStringResource(R.string.cocktail_cuba_libre),
                Utils.getStringResource(R.string.cocktail_cuba_libre_description),
                Utils.getStringResource(R.string.drink_coca_cola) + "," + Utils.getStringResource(R.string.drink_ron));
        return cocktails;
    }

    private static void generateCocktail(Realm realm, List<Cocktail> cocktails, String name, String description, String drinks) {
        Cocktail cocktail = new Cocktail();
        cocktail.setName(name);
        cocktail.setDescription(description);
        List<Drink> drinksFromString = DrinkRepository.getDrinksFromString(realm, drinks);
        for (Drink drink : drinksFromString) {
            addDrink(cocktail, drink);
        }
        cocktails.add(cocktail);
    }

    public static void generateCocktails(Realm realm) {
        DataController.generateCocktails(realm);
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
                cocktail = CocktailRepository.getCocktailFromString(activity.getRealm(), data);
            } catch (CocktailFormatException e) {
                activity.showToast(activity.getString(R.string.cocktail_format_error));
                return null;
            }
            if (CocktailRepository.cocktailExist(activity.getRealm(), cocktail)) {
                activity.showToast(activity.getString(R.string.cocktail_name_already_exist));
            } else {
                return cocktail;
            }
        }
        return null;
    }

    public static Cocktail getCocktailFromString(Realm realm, String data) throws CocktailFormatException {
        String[] split = data.split(",", 5);
        if (split.length < 5 || !split[0].equals("Pestormix")) {
            throw new CocktailFormatException();
        }
        Cocktail cocktail = new Cocktail();
        cocktail.setName(split[1]);
        cocktail.setDescription(split[2]);
        cocktail.setAlcohol(Boolean.valueOf(split[3]));
        setDrinksFromString(realm, cocktail, split[4]);
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

    public static void setDrinksFromString(Realm realm, Cocktail cocktail, String drinksString) {
        List<Drink> drinks = DrinkRepository.getDrinksFromString(realm, drinksString);
        for (Drink drink : drinks) {
            addDrink(cocktail, drink);
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

        if (oldName != null) {
            DataController.updateCocktail(realm, cocktail, oldName);
            Utils.putBooleanPreference(Constants.PREFERENCES_NEED_TO_PUSH, true);
        } else {
            DataController.updateCocktail(realm, cocktail);
        }
    }

    public static void updateCocktails(Realm realm, List<Cocktail> cocktails) {
        for (Cocktail cocktail : cocktails) {
            updateCocktail(realm, cocktail, null);
        }
    }

    public static void removeCocktailByName(Realm realm, String cocktailName) {
        DataController.removeCocktailByName(realm, cocktailName);
        Utils.putBooleanPreference(Constants.PREFERENCES_NEED_TO_PUSH, true);

    }

    public static void removeAllCocktails(Realm realm) {
        DataController.removeAllCocktails(realm);
    }

    public static void updateCocktails(Context context) {
        context.sendBroadcast(new Intent(Constants.ACTION_START_SYNC_WITH_REMOTE));
    }
}
