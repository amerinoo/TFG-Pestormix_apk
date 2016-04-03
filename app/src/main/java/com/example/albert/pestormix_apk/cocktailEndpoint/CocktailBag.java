package com.example.albert.pestormix_apk.cocktailEndpoint;

import com.example.albert.pestormix_apk.backend.cocktailApi.model.CocktailBean;

import java.util.List;

/**
 * Created by Albert on 29/03/2016.
 */
public interface CocktailBag {
    void reload();

    void pushToRemote(String userId);

    List<CocktailBean> pullFromRemote(String userId);
}
