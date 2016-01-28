package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Drink;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by Albert on 26/01/2016.
 */
public abstract class DataController {


    /******************
     * Start General
     ******************/
    public static void init(Realm realm) {
        generateDrinks(realm);
        generateCocktails(realm);
    }

    private static boolean addItem(Realm realm, RealmObject object) {
        boolean correct = true;
        realm.beginTransaction();
        try {
            realm.copyToRealm(object);
        } catch (RealmPrimaryKeyConstraintException e) {
            correct = false;
        }
        realm.commitTransaction();
        return correct;
    }

    private static void removeAllInstances(Realm realm) {
        removeAllCocktails(realm);
        removeAllDrinks(realm);
    }
    /******************* End General     ******************/

    /******************
     * Start Cocktail
     ******************/
    public static boolean addCocktail(Realm realm, Cocktail cocktail) {
        return addItem(realm, cocktail);
    }

    public static void removeCocktailByName(Realm realm, String name) {
        realm.beginTransaction();
        realm.where(Cocktail.class).equalTo("name", name).findAll().clear();
        realm.commitTransaction();
    }

    public static List<Cocktail> getCocktails(Realm realm) {
        RealmResults<Cocktail> results = realm.where(Cocktail.class).findAll();
        List<Cocktail> cocktails = new ArrayList<>();
        for (Cocktail cocktail : results) {
            cocktails.add(cocktail);
        }
        return cocktails;
    }

    public static void removeAllCocktails(Realm realm) {
        RealmResults<Cocktail> results = realm.where(Cocktail.class).findAll();
        realm.beginTransaction();
        results.clear();
        realm.commitTransaction();
    }

    public static void updateCocktail(Realm realm, Cocktail cocktail, String oldName) {
        removeCocktailByName(realm,oldName);
        addItem(realm,cocktail);
    }

    public static Cocktail getCocktailByName(Realm realm, String name) {
        return realm.where(Cocktail.class).equalTo("name", name).findFirst();
    }

    public static boolean cocktailExist(Realm realm, Cocktail cocktail) {
        return !addItem(realm, cocktail);
    }

    public static void generateCocktails(Realm realm) {
        List<Cocktail> cocktails = CocktailController.init(realm);

        realm.beginTransaction();
        for (Cocktail cocktail : cocktails) {
            try {
                realm.copyToRealm(cocktail);
            } catch (RealmPrimaryKeyConstraintException e) {
            }
        }
        realm.commitTransaction();

    }
    /****************** End Cocktail ******************/

    /******************
     * Start Drink
     ******************/
    public static boolean addDrink(Realm realm, Drink drink) {
        return addItem(realm, drink);
    }


    public static void removeDrinkByName(Realm realm, String name) {
        realm.beginTransaction();
        realm.where(Drink.class).equalTo("name", name).findAll().clear();
        realm.commitTransaction();
    }

    public static List<Drink> getDrinks(Realm realm) {
        RealmResults<Drink> results = realm.where(Drink.class).findAll();
        List<Drink> drinks = new ArrayList<>();
        for (Drink drink : results) {
            drinks.add(drink);
        }
        return drinks;
    }

    public static void removeAllDrinks(Realm realm) {
        RealmResults<Drink> results = realm.where(Drink.class).findAll();
        realm.beginTransaction();
        results.clear();
        realm.commitTransaction();
    }

    public static Drink getDrinkByName(Realm realm, String name) {
        return realm.where(Drink.class).equalTo("name", name).findFirst();
    }

    public static void generateDrinks(Realm realm) {
        List<Drink> drinks = DrinkController.init();
        realm.beginTransaction();
        for (Drink drink : drinks) {
            try {
                realm.copyToRealm(drink);
            } catch (RealmPrimaryKeyConstraintException e) {
            }
        }
        realm.commitTransaction();
    }
/******************
 * End Drink
 ******************/
}
