package com.example.albert.pestormix_apk.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.adapters.ItemsAdapter;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.controllers.CocktailController;
import com.example.albert.pestormix_apk.controllers.DrinkController;
import com.example.albert.pestormix_apk.enums.CreateCocktailType;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Drink;
import com.example.albert.pestormix_apk.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ManuallyActivity extends PestormixMasterActivity {

    private ItemsAdapter adapter;
    private String name = "";
    private String description = "";
    private List<Drink> drinksAdapter;
    private CreateCocktailType createCocktailType = CreateCocktailType.NEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeOrientationIfIsPhone();
        setContentView(R.layout.activity_manually);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(getString(R.string.title_create_cocktail));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configView();
    }

    private void configView() {
        ImageButton cocktail_detail = (ImageButton) findViewById(R.id.cocktail_detail);
        ImageButton add_item = (ImageButton) findViewById(R.id.add_item);
        ImageButton next = (ImageButton) findViewById(R.id.next);
        ListView itemsList = (ListView) findViewById(R.id.items_list);


        final List<Drink> drinks = DrinkController.getDrinks(getRealm());
        drinksAdapter = new ArrayList<>();
        checkIntent();
        removeEquals(drinks, drinksAdapter);
        adapter = new ItemsAdapter(getApplicationContext(), drinksAdapter, true);
        adapter.setRemoveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                drinks.add(adapter.getItem(position));
                adapter.removeDrink(position);
            }
        });
        itemsList.setAdapter(adapter);

        cocktail_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Cocktail Detail");
            }
        });

        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drinks.size() == 0) showToast(R.string.no_more_drinks_available);
                else {
                    final ItemsAdapter itemsAdapter = new ItemsAdapter(
                            getPestormixApplication(),
                            drinks, false);
                    new AlertDialog.Builder(ManuallyActivity.this)
                            .setAdapter(itemsAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    adapter.addDrink(drinks.get(which));
                                    drinks.remove(which);
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setTitle(R.string.select_drink)
                            .show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getCount() == 0) {
                    showToast(R.string.obligate_add_drink);
                } else {
                    Cocktail cocktail = new Cocktail();
                    for (int i = 0; i < adapter.getCount(); i++) {
                        CocktailController.addDrink(cocktail, adapter.getItem(i));
                    }
                    goGiveCocktailName(cocktail);
                }
            }
        });

    }

    private void removeEquals(List<Drink> drinks, List<Drink> drinksAdapter) {
        for (Drink drink : drinksAdapter) {
            drinks.remove(drink);
        }
    }

    private void checkIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString(Constants.EXTRA_COCKTAIL_NAME);
            description = bundle.getString(Constants.EXTRA_COCKTAIL_DESCRIPTION);
            String drinksString = bundle.getString(Constants.EXTRA_COCKTAIL_DRINKS);
            drinksAdapter = CocktailController.getDrinksFromString(getRealm(), drinksString);
            createCocktailType = CreateCocktailType.EDIT;
        }
    }

    private void goGiveCocktailName(Cocktail cocktail) {
        String drinks = CocktailController.getDrinksAsString(cocktail);
        Intent intent = new Intent(this, GiveCocktailNameActivity.class);
        intent.putExtra(Constants.EXTRA_COCKTAIL_NAME, name);
        intent.putExtra(Constants.EXTRA_COCKTAIL_DESCRIPTION, description);
        intent.putExtra(Constants.EXTRA_COCKTAIL_DRINKS, drinks);
        intent.putExtra(Constants.EXTRA_CREATE_COCKTAIL_TYPE, createCocktailType);
        startActivity(intent);
    }


}
