package com.example.albert.pestormix_apk.cocktailEndpoint;

import android.util.Log;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixApplication;
import com.example.albert.pestormix_apk.backend.cocktailApi.CocktailApi;
import com.example.albert.pestormix_apk.backend.cocktailApi.model.CocktailBean;
import com.example.albert.pestormix_apk.backend.messaging.Messaging;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.repositories.CocktailRepository;
import com.example.albert.pestormix_apk.utils.Constants;
import com.example.albert.pestormix_apk.utils.Utils;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Albert on 29/03/2016.
 */
public class EndpointsCocktailBagImpl implements CocktailBag {
    final CocktailApi cocktailApi;
    final Messaging messaging;
    protected List<CocktailBean> cocktailBeans = new ArrayList<>();
    protected Date lastSync = null;
    private PestormixApplication pestormixApplication;

    public EndpointsCocktailBagImpl(PestormixApplication pestormixApplication) {
        this.pestormixApplication = pestormixApplication;
        CocktailApi.Builder builder = new CocktailApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null);
        cocktailApi = builder.build();

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
            cocktailBean.setDrinks(CocktailRepository.getDrinksAsString(cocktail));
            cocktailBeans.add(cocktailBean);
        }
    }

    @Override
    public void pushToRemote(String userId) {
        try {
            cocktailApi.deleteAllCocktails(userId).execute();
            messaging.messagingEndpoint().sendMessage(String.format(
                    Utils.getStringResource(R.string.push_to_remote),
                    pestormixApplication.getString(Constants.PREFERENCES_USER_NAME,
                            Utils.getStringResource(R.string.default_only)))).execute();
            for (CocktailBean cocktailBean : cocktailBeans) {
                cocktailApi.insertCocktail(userId, cocktailBean).execute();
            }
            lastSync = new Date();
        } catch (IOException e) {
            Log.e(EndpointsCocktailBagImpl.class.getName(), "Error when storing cocktailBeans", e);
        }
    }

    @Override
    public List<CocktailBean> pullFromRemote(String userId) {
        List<CocktailBean> cocktails = null;
        try {
            messaging.messagingEndpoint().sendMessage(String.format(
                    Utils.getStringResource(R.string.pull_from_remote),
                    pestormixApplication.getString(Constants.PREFERENCES_USER_NAME,
                            Utils.getStringResource(R.string.default_only)))).execute();
            cocktails = cocktailApi.getCocktails(userId).execute().getItems();
        } catch (IOException e) {
            Log.e(EndpointsCocktailBagImpl.class.getSimpleName(), "Error when loading cocktailBeans", e);
        }
        return cocktails;
    }
}
