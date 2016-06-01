package com.example.albert.pestormix_apk.api;

import android.util.Log;

import com.example.albert.pestormix_apk.backend.valveApi.model.ValveBean;
import com.example.albert.pestormixlibrary.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Albert on 31/05/2016.
 */
public class ValveEndpoint {
    private String main_url = "http://pestormix.herokuapp.com/";

    public ValveEndpoint() {

    }

    public List<ValveBean> getValves(String userId) {
        String url = main_url + "valves?userId=" + userId;
        String result = GET(url);
        List<ValveBean> valveBeen = new ArrayList<>();
        if (result != null) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    valveBeen.add(jsonToValveBean(jsonObject));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return valveBeen;
    }

    public void deleteAllCocktails(String userId) {
        String url = main_url + "cocktails?userId=" + userId;
        DELETE(url);
    }

    public void insertValve(String userId, ValveBean valveBean) throws JSONException {
        String url = main_url + "cocktails?userId=" + userId;
        JSONObject jsonObject = valveToJson(userId, valveBean);
        POST(url, jsonObject);
    }

    private void POST(String urls, JSONObject jsonObject) {
        Log.d("POST_URL@@@@@@@@", urls);
        try {
            URL url = new URL(urls);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(jsonObject.toString().getBytes());
            wr.flush();
            wr.close();
            conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void DELETE(String urls) {
        Log.d("DELETE_URL@@@@@@@@", urls);
        try {
            URL url = new URL(urls);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ValveBean jsonToValveBean(JSONObject jsonObject) throws JSONException {
        ValveBean bean = new ValveBean();
        bean.setId(jsonObject.getString(Constants.VALVE_ID));
        bean.setDrinkName(jsonObject.getString(Constants.VALVE_DRINK_NAME));
        bean.setDrinkAlcohol(jsonObject.getBoolean(Constants.VALVE_DRINK_ALCOHOL));
        return bean;
    }


    private JSONObject valveToJson(String userId, ValveBean valveBean) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.VALVE_USER_ID, userId);
        jsonObject.put(Constants.VALVE_ID, valveBean.getId());
        jsonObject.put(Constants.VALVE_DRINK_NAME, valveBean.getDrinkName());
        jsonObject.put(Constants.VALVE_DRINK_ALCOHOL, valveBean.getDrinkAlcohol());
        return jsonObject;
    }

    private String GET(String urls) {
        Log.d("GET_URL@@@@@@@@", urls);
        try {
            URL url = new URL(urls);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream inputStream = conn.getInputStream();

            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder sBuilder = new StringBuilder();

            String line;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line).append("\n");
            }
            inputStream.close();
            return sBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}