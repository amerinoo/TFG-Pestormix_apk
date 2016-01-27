package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.models.Cocktail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Albert on 27/01/2016.
 */
public abstract class CocktailController {
    public static List<Cocktail> init() {
        List<Cocktail> cocktails = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Cocktail cocktail = new Cocktail();
            cocktail.setName("Cocktail " + i);
            cocktails.add(cocktail);
        }
        return cocktails;
    }
}
