package com.example.albert.pestormix_apk.activities;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.fragments.AboutAuthorFragment;
import com.example.albert.pestormix_apk.fragments.CreateCocktailFragment;
import com.example.albert.pestormix_apk.fragments.HelpFragment;
import com.example.albert.pestormix_apk.fragments.HomeFragment;
import com.example.albert.pestormix_apk.fragments.SettingsFragment;

public class MainActivity extends PestormixMasterActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private NavigationView drawer;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private TextView toolbarTitle;
    private ImageButton searchIcon;
    private LinearLayout searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        searchIcon = (ImageButton) findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchIcon.setSelected(!searchIcon.isSelected());
                if (searchIcon.isSelected()) openSearchView();
                else closeSearchView();
            }
        });
        searchView = (LinearLayout) findViewById(R.id.search_view);
        drawer = (NavigationView) findViewById(R.id.main_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawer.setNavigationItemSelectedListener(this);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        int firtsScreen = R.id.navigation_cocktails;
        navigate(firtsScreen);
        drawer.getMenu().findItem(firtsScreen).setChecked(true);
    }

    private void navigate(int id) {

        Fragment fragment;
        int resId = R.id.main_content;
        hideSearchIcon();
        closeNavigationDrawer();
        closeSearchView();
        switch (id) {
            case R.id.navigation_cocktails:
                showSearchIcon();
                setToolbarTitleText(getText(R.string.title_cocktails));
                fragment = HomeFragment.getInstance();
                loadFragment(resId, fragment);
                break;
            case R.id.navigation_createCocktail:
                setToolbarTitleText(getText(R.string.title_create_cocktail));
                fragment = CreateCocktailFragment.getInstance();
                loadFragment(resId, fragment);
                break;
            case R.id.navigation_settings:
                setToolbarTitleText(getText(R.string.title_settings));
                fragment = SettingsFragment.getInstance();
                loadFragment(resId, fragment);
                break;
            case R.id.navigation_help:
                setToolbarTitleText(getText(R.string.title_help));
                fragment = HelpFragment.getInstance();
                loadFragment(resId, fragment);
                break;
            case R.id.navigation_aboutTheAuthor:
                setToolbarTitleText(getText(R.string.title_about_author));
                fragment = AboutAuthorFragment.getInstance();
                loadFragment(resId, fragment);
                break;
        }
    }


    private void closeNavigationDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void setToolbarTitleText(CharSequence text) {
        this.toolbarTitle.setText(text);
    }

    private boolean isSearchOpen() {
        return searchIcon.isSelected();
    }

    public void showSearchIcon() {
        searchIcon.setVisibility(View.VISIBLE);
    }

    public void hideSearchIcon() {
        searchIcon.setVisibility(View.GONE);
    }

    public void openSearchView() {
        searchView.setVisibility(View.VISIBLE);
    }

    public void closeSearchView() {
        searchView.setVisibility(View.GONE);
        searchIcon.setSelected(false);
    }

    public LinearLayout getSearchView() {
        return searchView;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        navigate(item.getItemId());
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            closeNavigationDrawer();
        } else if (isSearchOpen()) {
            closeSearchView();
        } else {
            super.onBackPressed();
        }
    }
}
