package com.example.albert.pestormix_apk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.adapters.ScreenSlidePagerAdapter;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.controllers.DrinkController;
import com.example.albert.pestormix_apk.models.Drink;
import com.example.albert.pestormix_apk.utils.Constants;

import java.util.List;

/**
 * Created by Albert on 01/02/2016.
 */
public class ConfigValvesActivity extends PestormixMasterActivity implements View.OnClickListener {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private TextView v1;
    private TextView v2;
    private TextView v3;
    private TextView v4;

    private View lastSelected;

    private boolean isTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_valves);
        isTutorial = getIntent().getBooleanExtra(Constants.EXTRA_IS_TUTORIAL, false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (!isTutorial)
            ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(getString(R.string.configure_valves));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(!isTutorial);
        configView();
    }

    private void configView() {
        mPager = (ViewPager) findViewById(R.id.pager);
        v1 = (TextView) findViewById(R.id.v1);
        v2 = (TextView) findViewById(R.id.v2);
        v3 = (TextView) findViewById(R.id.v3);
        v4 = (TextView) findViewById(R.id.v4);

        v1.setTag(-1);
        v2.setTag(-1);
        v3.setTag(-1);
        v4.setTag(-1);

        changeSelected(v1);

        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
        v3.setOnClickListener(this);
        v4.setOnClickListener(this);

        List<Drink> drinks = DrinkController.getDrinks(getRealm());
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), drinks);
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v1:
            case R.id.v2:
            case R.id.v3:
            case R.id.v4:
                changeSelected(v);
                break;
        }
    }

    private void goNext() {
        Intent intent = new Intent(this, AllReadyActivity.class);
        startActivity(intent);
        finish();
    }

    private void changeSelected(View v) {
        if (lastSelected != null) {
            lastSelected.setSelected(false);
            lastSelected.setTag(mPager.getCurrentItem());
        }
        v.setSelected(true);
        int currentItem = (int) v.getTag();
        if (currentItem == -1) {
            mPager.setCurrentItem(0);
        } else {
            mPager.setCurrentItem(currentItem);

        }
        lastSelected = v;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_valves, menu);
        menu.setGroupVisible(R.id.tutorial_group, isTutorial);
        menu.setGroupVisible(R.id.config_valves_group, !isTutorial);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.continue_button:
                goNext();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
