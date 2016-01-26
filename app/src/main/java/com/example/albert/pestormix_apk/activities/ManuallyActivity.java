package com.example.albert.pestormix_apk.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.adapters.ItemsAdapter;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.builders.CocktailBuilder;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Drink;
import com.example.albert.pestormix_apk.utils.Constants;
import com.example.albert.pestormix_apk.utils.Drinks;

import java.util.ArrayList;
import java.util.List;

public class ManuallyActivity extends PestormixMasterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configView();
    }

    private void configView() {
        ImageButton cocktail_detail = (ImageButton) findViewById(R.id.cocktail_detail);
        ImageButton add_item = (ImageButton) findViewById(R.id.add_item);
        ImageButton next = (ImageButton) findViewById(R.id.next);
        ListView itemsList = (ListView) findViewById(R.id.items_list);


        final List<Drink> drinks = Drinks.getDrinks();
        final ItemsAdapter adapter = new ItemsAdapter(getApplicationContext(), new ArrayList<Drink>());
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
                    final ArrayAdapter<Drink> arrayAdapter = new ArrayAdapter<>(
                            getPestormixApplication(),
                            android.R.layout.select_dialog_singlechoice,
                            drinks);
                    new AlertDialog.Builder(ManuallyActivity.this)
                            .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
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
                    CocktailBuilder cocktailBuilder = new CocktailBuilder();
                    for (int i = 0; i < adapter.getCount(); i++) {
                        cocktailBuilder.addDrink(adapter.getItem(i));
                    }
                    goPutCocktailName(cocktailBuilder.build());
                }
            }
        });

    }

    private void goPutCocktailName(Cocktail cocktail) {
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_COCKTAIL, cocktail);
        //startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
