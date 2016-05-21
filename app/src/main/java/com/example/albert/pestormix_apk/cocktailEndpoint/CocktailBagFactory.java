package com.example.albert.pestormix_apk.cocktailEndpoint;

import android.preference.PreferenceManager;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixApplication;

/**
 * Created by Albert on 29/03/2016.
 */
public class CocktailBagFactory {
    public static CocktailBag getCocktailBag(PestormixApplication pestormixApplication) {

        String isHeroku = PreferenceManager.getDefaultSharedPreferences(pestormixApplication).getString(pestormixApplication.getString(R.string.PREFERENCE_HEROKU), "Google");
        if (isHeroku.equals("Heroku"))
            return new EndpointsCocktailHerokuImpl(pestormixApplication);
        else
            return new EndpointsCocktailBagImpl(pestormixApplication);
    }
}
