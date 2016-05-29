package com.example.albert.pestormix_apk.cocktailEndpoint;

import android.util.Log;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.api.CocktailEndpoint;
import com.example.albert.pestormix_apk.application.PestormixApplication;
import com.example.albert.pestormix_apk.backend.cocktailApi.model.CocktailBean;
import com.example.albert.pestormix_apk.backend.messaging.Messaging;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.repositories.CocktailRepository;
import com.example.albert.pestormix_apk.utils.Constants;
import com.example.albert.pestormix_apk.utils.Utils;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Albert on 20/05/2016.
 */
public class EndpointsCocktailHerokuImpl implements CocktailBag {
    final CocktailEndpoint cocktailApi;
    final Messaging messaging;
    protected List<CocktailBean> cocktailBeans = new ArrayList<>();
    protected Date lastSync = null;
    private PestormixApplication pestormixApplication;

    public EndpointsCocktailHerokuImpl(PestormixApplication pestormixApplication) {
        this.pestormixApplication = pestormixApplication;
        cocktailApi = new CocktailEndpoint();

        Messaging.Builder builder1 = new Messaging.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null);
        messaging = builder1.build();
    }

    @Override
    public void reload() {
        List<Cocktail> cocktails = CocktailRepository.getCocktails(pestormixApplication.getRealm());
        cocktailBeans.clear();
        for (Cocktail cocktail : cocktails) {
            CocktailBean cocktailBean = new CocktailBean();
            cocktailBean.setName(cocktail.getName());
            cocktailBean.setDescription(cocktail.getDescription());
            cocktailBean.setAlcohol(cocktail.isAlcohol());
            cocktailBean.setDrinks(CocktailRepository.getDrinksAsString(cocktail,true));
            cocktailBeans.add(cocktailBean);
        }
    }

    @Override
    public void pushToRemote(String userId) {
        try {
            cocktailApi.deleteAllCocktails(userId);
            messaging.messagingEndpoint().sendMessage(String.format(Utils.getStringResource(
                    R.string.heroku_push_to_remote), pestormixApplication.getString(Constants.PREFERENCES_USER_NAME,
                    Utils.getStringResource(R.string.default_only)))).execute();
            for (CocktailBean cocktailBean : cocktailBeans) {
                cocktailApi.insertCocktail(userId, cocktailBean);
            }
            lastSync = new Date();
        } catch (IOException e) {
            Log.e(EndpointsCocktailHerokuImpl.class.getName(), "Error when storing cocktailBeans", e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CocktailBean> pullFromRemote(String userId) {
        List<CocktailBean> cocktails = null;
        try {
            messaging.messagingEndpoint().sendMessage(String.format(Utils.getStringResource(
                    R.string.heroku_pull_from_remote), pestormixApplication.getString(Constants.PREFERENCES_USER_NAME,
                    Utils.getStringResource(R.string.default_only)))).execute();
            cocktails = cocktailApi.getCocktails(userId);
        } catch (IOException e) {
            Log.e(EndpointsCocktailHerokuImpl.class.getSimpleName(), "Error when loading cocktailBeans", e);
        }
        return cocktails;
    }
}
