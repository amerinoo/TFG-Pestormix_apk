package com.example.albert.pestormix_apk.controllers;

import android.os.AsyncTask;

import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Valve;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Albert on 07/02/2016.
 */
public abstract class NetworkController {
    public static void send(Realm realm, String cocktailName, String glassName) {
        Cocktail cocktail = CocktailController.getCocktailByName(realm, cocktailName);
        String cocktailDrinks = CocktailController.getDrinksAsString(cocktail);
        String jsonMessage = getJsonAsString(realm, cocktailDrinks, glassName);
        System.out.println(jsonMessage);
        new SendMessageTask().execute(jsonMessage);
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
                JSONObject valveObject = new JSONObject();
                valveObject.put("use", cocktailDrinks.contains(valve.getDrink().getName()));
                valveObject.put("name", valve.getDrink().getName());
                valveObject.put("alcohol", valve.getDrink().isAlcohol());
                jsonObject.put("v" + valve.getId(), valveObject);
            } catch (JSONException e) {
            }
        }
        return jsonObject.toString();
    }

    static class SendMessageTask extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... jsonMessage) {
            final String ip = "192.168.1.8";
            final int port = 1110;
            try {
                Socket socket = new Socket(ip, port);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(jsonMessage[0]);
                out.flush();
                out.close();
                socket.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            return null;
        }
    }
}
