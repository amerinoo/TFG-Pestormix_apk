package com.example.albert.pestormix_apk.api;

import android.util.Log;

import com.example.albert.pestormix_apk.backend.cocktailApi.model.CocktailBean;
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
 * Created by Albert on 20/05/2016.
 */
public class CocktailEndpoint {
    private String main_url = "http://pestormix.herokuapp.com/";

    public CocktailEndpoint() {

    }

    public List<CocktailBean> getCocktails(String userId) {
        String url = main_url + "cocktails?userId=" + userId;
        String result = GET(url);
        List<CocktailBean> cocktailBean = new ArrayList<>();
        if (result != null) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    cocktailBean.add(jsonToCocktailBean(jsonObject));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cocktailBean;
    }

    public void deleteAllCocktails(String userId) {
        String url = main_url + "cocktails?userId=" + userId;
        DELETE(url);
    }

    public void insertCocktail(String userId, CocktailBean cocktailBean) throws JSONException {
        String url = main_url + "cocktails?userId=" + userId;
        JSONObject jsonObject = cocktailToJson(userId, cocktailBean);
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

    private CocktailBean jsonToCocktailBean(JSONObject jsonObject) throws JSONException {
        CocktailBean bean = new CocktailBean();
        bean.setName(jsonObject.getString(Constants.COCKTAIL_NAME));
        bean.setDescription(jsonObject.getString(Constants.COCKTAIL_DESCRIPTION));
        bean.setAlcohol(jsonObject.getBoolean(Constants.COCKTAIL_ALCOHOL));
        bean.setDrinks(jsonObject.getString(Constants.COCKTAIL_DRINKS));
        return bean;
    }


    private JSONObject cocktailToJson(String userId, CocktailBean cocktail) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.COCKTAIL_NAME, cocktail.getName());
        jsonObject.put(Constants.COCKTAIL_USER_ID, userId);
        jsonObject.put(Constants.COCKTAIL_DESCRIPTION, cocktail.getDescription());
        jsonObject.put(Constants.COCKTAIL_ALCOHOL, cocktail.getAlcohol());
        jsonObject.put(Constants.COCKTAIL_DRINKS, cocktail.getDrinks());
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