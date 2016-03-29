package com.example.albert.pestormix_apk.cocktailEndpoint;

import com.example.albert.pestormix_apk.application.PestormixApplication;

/**
 * Created by Albert on 29/03/2016.
 */
public class CocktailBagFactory {
    public static CocktailBag getCocktailBag(PestormixApplication pestormixApplication) {
        return new EndpointsCocktailBagImpl(pestormixApplication);
    }
}
