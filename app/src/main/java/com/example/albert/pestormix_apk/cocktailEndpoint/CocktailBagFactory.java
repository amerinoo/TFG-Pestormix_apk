package com.example.albert.pestormix_apk.cocktailEndpoint;

import android.preference.PreferenceManager;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixApplication;
import com.example.albert.pestormixlibrary.Constants;

/**
 * Created by Albert on 29/03/2016.
 */
public class CocktailBagFactory {
    public static CocktailBag getCocktailBag(PestormixApplication pestormixApplication) {
        String backend_type = PreferenceManager.getDefaultSharedPreferences(pestormixApplication)
                .getString(pestormixApplication.getString(R.string.PREFERENCE_HEROKU), Constants.BACKEND_GOOGLE);
        if (backend_type.equals(Constants.BACKEND_HEROKU))
            return new EndpointsCocktailHerokuImpl(pestormixApplication);
        else
            return new EndpointsCocktailBagImpl(pestormixApplication);
    }
}
