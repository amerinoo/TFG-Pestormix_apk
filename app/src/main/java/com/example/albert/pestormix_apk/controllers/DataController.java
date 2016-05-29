package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Drink;
import com.example.albert.pestormix_apk.models.Glass;
import com.example.albert.pestormix_apk.models.Question;
import com.example.albert.pestormix_apk.models.Valve;
import com.example.albert.pestormix_apk.repositories.CocktailRepository;
import com.example.albert.pestormix_apk.repositories.DrinkRepository;
import com.example.albert.pestormix_apk.repositories.GlassRepository;
import com.example.albert.pestormix_apk.repositories.QuestionRepository;
import com.example.albert.pestormix_apk.repositories.ValveRepository;
import com.example.albert.pestormix_apk.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by Albert on 26/01/2016.
 */
public abstract class DataController {


    /******************
     * Start General
     ******************/
    public static void init(Realm realm) {
        removeAllInstances(realm);
        generateDrinks(realm);
        generateCocktails(realm);
        generateQuestions(realm);
        generateValves(realm);
        generateGlasses(realm);
    }

    private static void addItem(Realm realm, RealmObject object) {
        realm.beginTransaction();
        realm.copyToRealm(object);
        realm.commitTransaction();
    }

    private static void removeAllInstances(Realm realm) {
        removeAllCocktails(realm);
        removeAllDrinks(realm);
        removeAllQuestions(realm);
        removeAllValves(realm);
        removeAllGlasses(realm);
    }
    /******************* End General     ******************/

    /******************
     * Start Cocktail
     ******************/

    public static void generateCocktails(Realm realm) {
        List<Cocktail> cocktails = CocktailRepository.init(realm);

        realm.beginTransaction();
        for (Cocktail cocktail : cocktails) {
            try {
                realm.copyToRealmOrUpdate(cocktail);
            } catch (RealmPrimaryKeyConstraintException ignored) {
            }
        }
        realm.commitTransaction();

    }

    public static void addCocktail(Realm realm, Cocktail cocktail) {
        addItem(realm, cocktail);
    }

    public static void removeCocktailByName(Realm realm, String name) {
        realm.beginTransaction();
        realm.where(Cocktail.class).equalTo(Constants.COCKTAIL_NAME, name).findAll().clear();
        realm.commitTransaction();
    }

    public static List<Cocktail> getCocktails(Realm realm) {
        RealmResults<Cocktail> results = realm.where(Cocktail.class).findAll();
        results.sort(Constants.COCKTAIL_NAME, Sort.ASCENDING);
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

    public static void updateCocktail(Realm realm, Cocktail cocktail) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(cocktail);
        realm.commitTransaction();
    }

    public static void updateCocktail(Realm realm, Cocktail cocktail, String oldName) {
        removeCocktailByName(realm, oldName);
        addItem(realm, cocktail);
    }

    public static Cocktail getCocktailByName(Realm realm, String name) {
        return realm.where(Cocktail.class).equalTo(Constants.COCKTAIL_NAME, name).findFirst();
    }

    public static boolean cocktailExist(Realm realm, Cocktail cocktail) {
        Cocktail cocktail1 = getCocktailByName(realm, cocktail.getName());
        return cocktail1 != null;
    }

    public static List<String> getCocktailsNames(Realm realm) {
        List<Cocktail> results = getCocktails(realm);
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
        List<Drink> drinks = DrinkRepository.init();
        realm.beginTransaction();
        for (Drink drink : drinks) {
            try {
                realm.copyToRealm(drink);
            } catch (RealmPrimaryKeyConstraintException ignored) {
            }
        }
        realm.commitTransaction();
    }

    public static void removeAllDrinks(Realm realm) {
        RealmResults<Drink> results = realm.where(Drink.class).findAll();
        realm.beginTransaction();
        results.clear();
        realm.commitTransaction();
    }

    public static Drink getDrinkById(Realm realm, String id) {
        return realm.where(Drink.class).equalTo(Constants.DRINK_ID, id).findFirst();
    }

    public static Drink getDrinkByName(Realm realm, String name) {
        return realm.where(Drink.class).equalTo(Constants.DRINK_NAME, name).findFirst();
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
        List<Question> questions = QuestionRepository.init();
        realm.beginTransaction();
        for (Question question : questions) {
            try {
                realm.copyToRealm(question);
            } catch (RealmPrimaryKeyConstraintException ignored) {
            }
        }
        realm.commitTransaction();
    }

    public static List<Question> getQuestions(Realm realm) {
        RealmResults<Question> results = realm.where(Question.class).findAllSorted(Constants.QUESTION_ID);
        List<Question> questions = new ArrayList<>();
        for (Question question : results) {
            questions.add(question);
        }
        return questions;
    }

    public static List<String> getQuestionsStrings(Realm realm) {
        List<Question> results = getQuestions(realm);
        List<String> list = new ArrayList<>();
        for (Question question : results) {
            list.add(question.getQuestion());
        }
        return list;
    }

    public static Question getQuestionById(Realm realm, int id) {
        return realm.where(Question.class).equalTo(Constants.QUESTION_ID, id).findFirst();
    }

    public static void removeAllQuestions(Realm realm) {
        RealmResults<Question> results = realm.where(Question.class).findAll();
        realm.beginTransaction();
        results.clear();
        realm.commitTransaction();
    }
    /******************* End Question ******************/

    /******************
     * Start Valve
     ******************/
    private static void generateValves(Realm realm) {
        List<Valve> valves = ValveRepository.init();

        realm.beginTransaction();
        for (Valve valve : valves) {
            try {
                realm.copyToRealm(valve);
            } catch (RealmPrimaryKeyConstraintException ignored) {
            }
        }
        realm.commitTransaction();
    }

    public static List<Valve> getValves(Realm realm) {
        RealmResults<Valve> results = realm.where(Valve.class).findAllSorted(Constants.VALVE_ID);
        List<Valve> valves = new ArrayList<>();
        for (Valve valve : results) {
            valves.add(valve);
        }
        return valves;
    }

    public static void removeAllValves(Realm realm) {
        RealmResults<Valve> results = realm.where(Valve.class).findAll();
        realm.beginTransaction();
        results.clear();
        realm.commitTransaction();
    }

    public static void updateValve(Realm realm, Valve valve, Drink drink, int position) {
        realm.beginTransaction();
        valve.setDrink(drink);
        valve.setDrinkPosition(position);
        realm.copyToRealmOrUpdate(valve);
        realm.commitTransaction();
    }

    /******************** End Valve ******************/

    /*******************
     * Start Glass
     ******************/
    public static void generateGlasses(Realm realm) {
        List<Glass> glasses = GlassRepository.init();
        realm.beginTransaction();
        for (Glass glass : glasses) {
            try {
                realm.copyToRealm(glass);
            } catch (RealmPrimaryKeyConstraintException ignored) {
            }
        }
        realm.commitTransaction();
    }

    public static List<Glass> getGlasses(Realm realm) {
        RealmResults<Glass> results = realm.where(Glass.class).findAll();
        List<Glass> glasses = new ArrayList<>();
        for (Glass glass : results) {
            glasses.add(glass);
        }
        return glasses;
    }

    public static Glass getGlassesByName(Realm realm, String name) {
        return realm.where(Glass.class).equalTo(Constants.GLASS_NAME, name).findFirst();
    }

    public static void removeAllGlasses(Realm realm) {
        RealmResults<Glass> results = realm.where(Glass.class).findAllSorted(Constants.GLASS_CAPACITY);
        realm.beginTransaction();
        results.clear();
        realm.commitTransaction();
    }
    /******************** End Glass ******************/
}
