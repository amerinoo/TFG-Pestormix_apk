package com.example.albert.pestormix_apk.builders;

import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Drink;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by Albert on 26/01/2016.
 */
public class CocktailBuilder {

    private Cocktail cocktail;

    public CocktailBuilder() {
        cocktail = new Cocktail();
    }

    public CocktailBuilder addDrink(Drink drink) {
        cocktail.getDrinks().add(drink);
        return this;
    }

    public List<Drink> getDrinks() {
        RealmList<Drink> results = cocktail.getDrinks();
        List<Drink> drinks = new ArrayList<>();
        for (Drink drink : results) drinks.add(drink);
        return drinks;
    }

    public Cocktail build() {
        return cocktail;
    }

}
