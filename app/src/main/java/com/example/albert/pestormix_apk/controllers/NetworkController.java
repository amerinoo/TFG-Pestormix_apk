package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Valve;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Albert on 07/02/2016.
 */
public class NetworkController {
    public static void send(Realm realm, String cocktailName, String glassName) {
        Cocktail cocktail = CocktailController.getCocktailByName(realm, cocktailName);
        String cocktailDrinks = CocktailController.getDrinksAsString(cocktail);
        String json = getJsonAsString(realm, cocktailDrinks, glassName);
        System.out.println(json);
    }

    private static String getJsonAsString(Realm realm, String cocktailDrinks, String glassName) {
        JSONObject jsonObject = new JSONObject();
        List<Valve> valves = ValveController.getValves(realm);
        try {
            jsonObject.put("glass", glassName);
        } catch (JSONException e) {
        }
        for (Valve valve : valves) {
            try {
                jsonObject.put("v" + valve.getId(), cocktailDrinks.contains(valve.getDrink().getName()));
            } catch (JSONException e) {
            }
        }
        return jsonObject.toString();
    }
}
