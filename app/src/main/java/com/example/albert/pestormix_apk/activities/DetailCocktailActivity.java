package com.example.albert.pestormix_apk.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.fragments.DetailCocktailFragment;
import com.example.albert.pestormix_apk.utils.Constants;

public class DetailCocktailActivity extends PestormixMasterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeOrientationIfIsPhone();
        setContentView(R.layout.activity_cocktail_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(getString(R.string.title_create_cocktail));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String name = getIntent().getStringExtra(Constants.EXTRA_COCKTAIL_NAME);
        String drinks = getIntent().getStringExtra(Constants.EXTRA_COCKTAIL_DRINKS);
        Fragment fragment = DetailCocktailFragment.getInstance(name, drinks);
        loadFragment(R.id.detail_content, fragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAnimation();
    }
}
