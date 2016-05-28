package com.example.albert.pestormix_apk.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Valve;
import com.example.albert.pestormix_apk.repositories.CocktailRepository;
import com.example.albert.pestormix_apk.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Albert on 07/02/2016.
 */
public abstract class NetworkController {

    public static Boolean send(List<Valve> valves, Cocktail cocktail, int glassName) {
        String cocktailDrinks = CocktailRepository.getDrinksAsString(cocktail);
        String jsonMessage = getJsonAsString(valves, cocktailDrinks, glassName);
        System.out.println(jsonMessage);
        AsyncTask<String, Void, Boolean> execute = new SendMessageTask().execute(jsonMessage);
        try {
            return execute.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getJsonAsString(List<Valve> valves, String cocktailDrinks, int glassName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("glass", glassName);
        } catch (JSONException ignored) {
        }
        for (Valve valve : valves) {
            try {
                JSONObject valveObject = new JSONObject();
                valveObject.put("use", cocktailDrinks.contains(valve.getDrink().getName()));
                valveObject.put("alcohol", valve.getDrink().isAlcohol());
                jsonObject.put("v" + valve.getId(), valveObject);
            } catch (JSONException ignored) {
            }
        }
        return jsonObject.toString();
    }

    static class SendMessageTask extends AsyncTask<String, Void, Boolean> {

        protected Boolean doInBackground(String... jsonMessage) {
            final String ip = Constants.NETWORK_RASPBERRY_IP;
            final int port = Integer.valueOf(Constants.NETWORK_RASPBERRY_PORT);
            Socket socket = null;
            Boolean sended = true;
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), 1000);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(jsonMessage[0]);
                out.close();
            } catch (IOException e) {
                Log.e("Socket", e.toString());
                sended = false;
            } finally {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sended;
        }
    }
}
