package com.example.Albert.pestormix_apk.backend;

/**
 * Created by Albert on 24/03/2016.
 */
public class CocktailBean {
    private String name;
    private String description;
    private boolean alcohol;
    private String drinks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAlcohol() {
        return alcohol;
    }

    public void setAlcohol(boolean alcohol) {
        this.alcohol = alcohol;
    }

    public String getDrinks() {
        return drinks;
    }

    public void setDrinks(String drinks) {
        this.drinks = drinks;
    }
}
