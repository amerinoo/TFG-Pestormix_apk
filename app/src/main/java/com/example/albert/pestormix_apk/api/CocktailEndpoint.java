package com.example.albert.pestormix_apk.api;

import android.util.Log;

import com.example.albert.pestormix_apk.backend.cocktailApi.model.CocktailBean;

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
        String url = main_url + "cocktails?userId=" + "7777";
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

    }

    public void insertCocktail(String userId, CocktailBean cocktailBean) throws JSONException {
        String url = main_url + "cocktails";
        JSONObject jsonObject = cocktailToJson(userId, cocktailBean);
        POST(url,jsonObject);
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

    private CocktailBean jsonToCocktailBean(JSONObject jsonObject) throws JSONException {
        CocktailBean bean = new CocktailBean();
        bean.setName(jsonObject.getString("name"));
        bean.setDescription(jsonObject.getString("description"));
        bean.setAlcohol(jsonObject.getBoolean("alcohol"));
        bean.setDrinks(jsonObject.getString("drinks"));
        return bean;
    }


    private JSONObject cocktailToJson(String userId, CocktailBean cocktail) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", cocktail.getName());
        jsonObject.put("userId", userId);
        jsonObject.put("description", cocktail.getDescription());
        jsonObject.put("alcohol", cocktail.getAlcohol());
        jsonObject.put("drinks", cocktail.getDrinks());
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



//    @ApiMethod(name = "insertCocktail")
//    public void insertCocktail(@Named("id") String id, CocktailBean cocktailBean) {
//        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
//        Transaction txn = datastoreService.beginTransaction();
//        try {
//            Key parent = new Entity("User", id).getKey();
//            Entity taskEntity = new Entity("Cocktail", cocktailBean.getName(), parent);
//            taskEntity.setProperty("name", cocktailBean.getName());
//            taskEntity.setProperty("description", cocktailBean.getDescription());
//            taskEntity.setProperty("alcohol", cocktailBean.isAlcohol());
//            taskEntity.setProperty("drinks", cocktailBean.getDrinks());
//            datastoreService.put(taskEntity);
//            txn.commit();
//        } finally {
//            if (txn.isActive()) {
//                txn.rollback();
//            }
//        }
//    }
//
//    @ApiMethod(name = "getCocktails")
//    public List<CocktailBean> getCocktails(@Named("id") String id) {
//        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
//        Key parent = new Entity("User", id).getKey();
//        List<CocktailBean> cocktails = new ArrayList<>();
//        Query query = new Query("Cocktail").setAncestor(parent);
//        List<Entity> results = datastoreService.prepare(query)
//                .asList(FetchOptions.Builder.withDefaults());
//        for (Entity entity : results) {
//            CocktailBean cocktail = new CocktailBean();
//            cocktail.setName((String) entity.getProperty("name"));
//            cocktail.setDescription((String) entity.getProperty("description"));
//            cocktail.setAlcohol((boolean) entity.getProperty("alcohol"));
//            cocktail.setDrinks((String) entity.getProperty("drinks"));
//            cocktails.add(cocktail);
//        }
//        return cocktails;
//    }
//
//    @ApiMethod(name = "deleteCocktailByName")
//    public void deleteCocktailByName(@Named("id") String id, @Named("name") String name) {
//        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
//        Transaction txn = datastoreService.beginTransaction();
//        try {
//            Key parent = new Entity("User", id).getKey();
//            Query query = new Query("Cocktail").setAncestor(parent).setFilter(new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, name));
//            Entity result = datastoreService.prepare(query)
//                    .asList(FetchOptions.Builder.withDefaults()).get(0);
//            datastoreService.delete(result.getKey());
//            txn.commit();
//        } finally {
//            if (txn.isActive()) {
//                txn.rollback();
//            }
//        }
//    }
//
//    @ApiMethod(name = "deleteAllCocktails")
//    public void deleteAllCocktails(@Named("id") String id) {
//        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
//        Transaction txn = datastoreService.beginTransaction();
//        try {
//            Key parent = new Entity("User", id).getKey();
//            Query query = new Query("Cocktail").setAncestor(parent);
//            List<Entity> results = datastoreService.prepare(query)
//                    .asList(FetchOptions.Builder.withDefaults());
//            for (Entity entity : results) {
//                datastoreService.delete(entity.getKey());
//            }
//            txn.commit();
//        } finally {
//            if (txn.isActive()) {
//                txn.rollback();
//            }
//        }
//    }
}
