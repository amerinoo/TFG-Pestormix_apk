package com.example.albert.pestormix_apk.adapters;

/**
 * Created by Albert on 01/02/2016.
 */


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.example.albert.pestormix_apk.fragments.ScreenSlidePageFragment;
import com.example.albert.pestormix_apk.models.Drink;

import java.util.List;

/**
 * Created by Albert on 01/02/2016.
 */
public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {


    private final List<Drink> drinks;

    public ScreenSlidePagerAdapter(FragmentManager fm, List<Drink> drinks) {
        super(fm);
        this.drinks = drinks;
    }

    @Override
    public Fragment getItem(int position) {
        return ScreenSlidePageFragment.newInstance(drinks.get(position));
    }


    @Override
    public int getCount() {
        return drinks.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return drinks.get(position).getName();
    }
}
