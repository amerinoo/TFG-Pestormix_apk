package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Drink;
import com.example.albert.pestormix_apk.models.Question;

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
        generateQuestions(realm);
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
        removeCocktailByName(realm, oldName);
        addItem(realm, cocktail);
    }

    public static Cocktail getCocktailByName(Realm realm, String name) {
        return realm.where(Cocktail.class).equalTo("name", name).findFirst();
    }

    public static boolean cocktailExist(Realm realm, Cocktail cocktail) {
        return !addItem(realm, cocktail);
    }


    public static List<String> getCocktailsNames(Realm realm) {
        RealmResults<Cocktail> results = realm.where(Cocktail.class).findAll();
        List<String> names = new ArrayList<>();
        for (Cocktail cocktail : results) {
            names.add(cocktail.getName());
        }
        return names;
    }

    /****************** End Cocktail ******************/

    /******************
     * Start Drink
     ******************/
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

    public static boolean addDrink(Realm realm, Drink drink) {
        return addItem(realm, drink);
    }

    public static void removeDrinkByName(Realm realm, String name) {
        realm.beginTransaction();
        realm.where(Drink.class).equalTo("name", name).findAll().clear();
        realm.commitTransaction();
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

    public static List<Drink> getDrinks(Realm realm) {
        RealmResults<Drink> results = realm.where(Drink.class).findAll();
        List<Drink> drinks = new ArrayList<>();
        for (Drink drink : results) {
            drinks.add(drink);
        }
        return drinks;
    }
/******************* End Drink ******************/

    /******************
     * Start Question
     ******************/
    private static void generateQuestions(Realm realm) {
        List<Question> questions = QuestionController.init();

        realm.beginTransaction();
        for (Question question : questions) {
            try {
                realm.copyToRealm(question);
            } catch (RealmPrimaryKeyConstraintException e) {
            }
        }
        realm.commitTransaction();
    }

    public static List<Question> getQuestions(Realm realm) {
        RealmResults<Question> results = realm.where(Question.class).findAllSorted("id");
        List<Question> questions = new ArrayList<>();
        for (Question question : results) {
            questions.add(question);
        }
        return questions;
    }

    public static List<String> getQuestionsStrings(Realm realm) {
        RealmResults<Question> results = realm.where(Question.class).findAll();
        List<String> list = new ArrayList<>();
        for (Question question : results) {
            list.add(question.getQuestion());
        }
        return list;
    }

    public static Question getQuestionById(Realm realm, int id) {
        return realm.where(Question.class).equalTo("id", id).findFirst();
    }
/******************* End Question ******************/

}
