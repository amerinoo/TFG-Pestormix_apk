package com.example.albert.pestormix_apk.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.enums.CreateCocktailType;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.repositories.CocktailRepository;
import com.example.albert.pestormix_apk.utils.Constants;

public class GiveCocktailNameActivity extends PestormixMasterActivity {

    private CreateCocktailType createCocktailType;
    private String extraName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeOrientationIfIsPhone();
        setContentView(R.layout.activity_give_cocktail_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(getString(R.string.title_create_cocktail));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configView();
    }

    private void configView() {
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText description = (EditText) findViewById(R.id.description);
        Button save = (Button) findViewById(R.id.save);
        Button cancel = (Button) findViewById(R.id.cancel);
        createCocktailType = (CreateCocktailType) getIntent()
                .getSerializableExtra(Constants.EXTRA_CREATE_COCKTAIL_TYPE);
        final Cocktail cocktail = new Cocktail();
        extraName = getIntent().getStringExtra(Constants.EXTRA_COCKTAIL_NAME);
        name.setText(extraName);
        description.setText(getIntent().getStringExtra(Constants.EXTRA_COCKTAIL_DESCRIPTION));
        String drinksString = getIntent().getStringExtra(Constants.EXTRA_COCKTAIL_DRINKS);
        CocktailRepository.setDrinksFromString(getRealm(), cocktail, drinksString);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cocktail.setName(name.getText().toString().trim());
                cocktail.setDescription(description.getText().toString().trim());
                if (createCocktailType == CreateCocktailType.NEW) saveCocktail(cocktail);
                else if (createCocktailType == CreateCocktailType.EDIT) updateCocktail(cocktail);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int message;
                if (createCocktailType == CreateCocktailType.NEW)
                    message = R.string.cocktail_removed_restart_creation;
                else
                    message = R.string.cocktail_not_updated_restart_edition;

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
                        .setMessage(message)
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

    private void saveCocktail(Cocktail cocktail) {
        if (!checkFields(cocktail)) {
            showToast(R.string.give_name_mandatory);
        } else {
            if (CocktailRepository.cocktailExist(getRealm(), cocktail)) {
                showToast(R.string.cocktail_name_already_exist);
            } else {
                CocktailRepository.addCocktailToDB(getRealm(), cocktail);
                goMain();
            }
        }
    }

    private void updateCocktail(Cocktail cocktail) {
        if (!checkFields(cocktail)) {
            showToast(R.string.give_name_mandatory);
        } else {
            if (!extraName.equals(cocktail.getName())
                    && CocktailRepository.cocktailExist(getRealm(), cocktail)) {
                showToast(R.string.cocktail_name_already_exist);
            } else {
                CocktailRepository.updateCocktail(getRealm(), cocktail, extraName);
                goMain();
            }
        }
    }

    private boolean checkFields(Cocktail cocktail) {
        return !cocktail.getName().equals("");
    }
}
