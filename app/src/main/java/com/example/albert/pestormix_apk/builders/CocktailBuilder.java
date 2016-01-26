package com.example.albert.pestormix_apk.builders;

import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Drink;

/**
 * Created by Albert on 26/01/2016.
 */
public class CocktailBuilder {

    private Cocktail cocktail;

    public CocktailBuilder() {
        cocktail = new Cocktail();
    }

    public CocktailBuilder addDrink(Drink drink) {
        cocktail.addDrink(drink);
        return this;
    }

    public Cocktail build() {
        return cocktail;
    }

}
