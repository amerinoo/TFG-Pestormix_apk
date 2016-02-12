package com.example.albert.pestormix_apk.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.controllers.NfcController;
import com.example.albert.pestormix_apk.fragments.AboutAuthorFragment;
import com.example.albert.pestormix_apk.fragments.CreateCocktailFragment;
import com.example.albert.pestormix_apk.fragments.HelpFragment;
import com.example.albert.pestormix_apk.fragments.HomeFragment;
import com.example.albert.pestormix_apk.fragments.SettingsFragment;

public class MainActivity extends PestormixMasterActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private NavigationView drawer;
    private DrawerLayout drawerLayout;
    Fragment fragment;

    private ActionBarDrawerToggle drawerToggle;
    private TextView toolbarTitle;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        Menu menu = toolbar.getMenu();
        getMenuInflater().inflate(R.menu.menu_search_view, menu);
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

        nfcAdapter = NfcController.getInstance(this).getAdapter();
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
    }

    private void navigate(int id) {
        int resId = R.id.main_content;
        closeNavigationDrawer();
        switch (id) {
            case R.id.navigation_cocktails:
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
            exit();
        }
    }

    private void exit() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.information)
                .setMessage(R.string.quit)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Tag mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (fragment instanceof HomeFragment) {
                ((HomeFragment) fragment).processNfcData(mytag);
            } else if (fragment instanceof CreateCocktailFragment) {
                ((CreateCocktailFragment) fragment).processNfcData(mytag);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, nfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, nfcAdapter);

        super.onPause();
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
}
