package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Drink;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Albert on 26/01/2016.
 */
public abstract class DataController {


    /******************
     * Start General
     ******************/
    public static void init(Realm realm) {
        generateCocktails(realm);
        generateDrinks(realm);
    }

    private static void addItem(Realm realm, RealmObject object) {
        realm.beginTransaction();
        realm.copyToRealm(object);
        realm.commitTransaction();
    }

    private static void removeAllInstances(Realm realm) {
        removeAllCocktails(realm);
        removeAllDrinks(realm);
    }
    /******************* End General     ******************/

    /******************
     * Start Cocktail
     ******************/
    public static void addCocktail(Realm realm, Cocktail cocktail) {
        addItem(realm, cocktail);
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

    public static Cocktail getCocktailByName(Realm realm, String name) {
        return realm.where(Cocktail.class).equalTo("name", name).findFirst();
    }

    public static boolean cocktailExist(Realm realm, String name) {
        return realm.where(Cocktail.class).equalTo("name", name).findFirst() != null;
    }

    private static void generateCocktails(Realm realm) {
        List<Cocktail> cocktails = CocktailController.init();
        realm.beginTransaction();
        for (Cocktail cocktail: cocktails) {
            realm.copyToRealm(cocktail);
        }
        realm.commitTransaction();
    }
    /****************** End Cocktail ******************/

    /******************
     * Start Drink
     ******************/
    public static void addDrink(Realm realm, Drink drink) {
        addItem(realm, drink);
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

    private static void generateDrinks(Realm realm) {
        List<Drink> drinks = DrinkController.init();
        realm.beginTransaction();
        for (Drink drink: drinks) {
            realm.copyToRealm(drink);
        }
        realm.commitTransaction();
    }
    /****************** End Drink ******************/
}
