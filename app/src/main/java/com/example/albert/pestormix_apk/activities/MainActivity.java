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
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.fragments.HomeFragment;

public class MainActivity extends PestormixMasterActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private NavigationView drawer;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
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
        switch (id) {
            case R.id.navigation_cocktails:
                showSearchIcon();
                setToolbarTitleText("Cocktails");
                fragment = HomeFragment.getInstance();
                loadFragment(resId, fragment);
                break;
            case R.id.navigation_createCocktail:
                setToolbarTitleText("Create cocktail");
                break;
            case R.id.navigation_settings:
                setToolbarTitleText("Settings");
                break;
            case R.id.navigation_help:
                setToolbarTitleText("Help");
                break;
            case R.id.navigation_aboutTheAuthor:
                setToolbarTitleText("About the author");
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

    public void showSearchIcon() {
        findViewById(R.id.search_icon).setVisibility(View.VISIBLE);
    }

    public void hideSearchIcon() {
        findViewById(R.id.search_icon).setVisibility(View.GONE);
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
        } else {
            super.onBackPressed();
        }
    }
}
