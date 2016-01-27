package com.example.albert.pestormix_apk.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.controllers.DataController;
import com.example.albert.pestormix_apk.controllers.DrinkController;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Drink;
import com.example.albert.pestormix_apk.utils.Constants;

import io.realm.RealmList;

public class GiveCocktailNameActivity extends PestormixMasterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_cocktail_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configView();
    }

    private void configView() {
        final EditText name = (EditText) findViewById(R.id.name);
        Button save = (Button) findViewById(R.id.save);
        Button cancel = (Button) findViewById(R.id.cancel);

        final Cocktail cocktail = new Cocktail();
        cocktail.setDrinks(getDrinks());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cocktail.setName(name.getText().toString());
                saveCocktail(cocktail);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(GiveCocktailNameActivity.this)
                        .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goMain();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setTitle(R.string.are_you_sure)
                        .setMessage(R.string.the_cocktail_will_be_removed)
                        .show();
            }
        });
    }

    private void goMain() {
        Intent intent = new Intent(GiveCocktailNameActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clean back stack
        startActivity(intent);
        finish();
    }

    private RealmList<Drink> getDrinks() {
        RealmList<Drink> drinks = new RealmList<>();
        String drinksString = getIntent().getStringExtra(Constants.EXTRA_COCKTAIL_DRINKS);
        for (String id : drinksString.split(",")) {
            drinks.add(DrinkController.getDrinkById(id));
        }
        return drinks;
    }

    private void saveCocktail(Cocktail cocktail) {
        if (cocktail.getName().equals("")) {
            showToast(R.string.give_name_mandatory);
        } else if (DataController.getCocktailByName(getRealm(), cocktail.getName()) != null) {
            showToast(getString(R.string.cocktail_name_already_exist));
        } else {
            DataController.addCocktail(getRealm(), cocktail);
            goMain();
        }
    }

}
