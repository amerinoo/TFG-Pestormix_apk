package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.models.Cocktail;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Albert on 26/01/2016.
 */
public abstract class DataController {

    public static void addCocktail(Realm realm, Cocktail cocktail) {
        realm.beginTransaction();
        realm.copyToRealm(cocktail);
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

    public static void removeCocktailByName(Realm realm, String name) {
        realm.beginTransaction();
        realm.where(Cocktail.class).equalTo("name", name).findAll().clear();
        realm.commitTransaction();
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
}
