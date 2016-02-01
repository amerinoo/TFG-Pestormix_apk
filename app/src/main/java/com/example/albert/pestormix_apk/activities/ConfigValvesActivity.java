package com.example.albert.pestormix_apk.activities;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.adapters.ScreenSlidePagerAdapter;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.controllers.DrinkController;
import com.example.albert.pestormix_apk.models.Drink;

import java.util.List;

/**
 * Created by Albert on 01/02/2016.
 */
public class ConfigValvesActivity extends PestormixMasterActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_pager);
        mPager = (ViewPager) findViewById(R.id.pager);
        List<Drink> drinks = DrinkController.getDrinks(getRealm());
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), drinks);
        mPager.setAdapter(mPagerAdapter);
    }
}
