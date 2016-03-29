package com.example.albert.pestormix_apk.cocktailEndpoint;

import android.util.Log;

import com.example.albert.pestormix_apk.application.PestormixApplication;
import com.example.albert.pestormix_apk.backend.cocktailApi.CocktailApi;
import com.example.albert.pestormix_apk.backend.cocktailApi.model.CocktailBean;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.repositories.CocktailRepository;
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
    protected List<CocktailBean> cocktailBeans = new ArrayList<>();
    protected Date lastSync = null;
    private PestormixApplication pestormixApplication;

    public EndpointsCocktailBagImpl(PestormixApplication pestormixApplication) {
        this.pestormixApplication = pestormixApplication;
        CocktailApi.Builder builder = new CocktailApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null);
        cocktailApi = builder.build();
    }

    @Override
    public void reload() {
        List<Cocktail> cocktails = CocktailRepository.getCocktails(pestormixApplication.getRealm());
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
    public void pushToRemote(Long userId) {
        try {
            cocktailApi.clearCocktails(userId).execute();

            for (CocktailBean cocktailBean : cocktailBeans) {
                cocktailApi.insertCocktail(userId, cocktailBean).execute();
            }
            lastSync = new Date();
        } catch (IOException e) {
            Log.e(EndpointsCocktailBagImpl.class.getName(), "Error when storing cocktailBeans", e);
        }
    }

    @Override
    public List<CocktailBean> pullFromRemote(Long userId) {
        List<CocktailBean> cocktails = null;
        try {
            cocktails = cocktailApi.getCocktails(userId).execute().getItems();
        } catch (IOException e) {
            Log.e(EndpointsCocktailBagImpl.class.getSimpleName(), "Error when loading cocktailBeans", e);
        }
        return cocktails;
    }
}
