package com.example.albert.pestormix_apk.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.fragments.AboutAuthorFragment;
import com.example.albert.pestormix_apk.fragments.CreateCocktailFragment;
import com.example.albert.pestormix_apk.fragments.HelpFragment;
import com.example.albert.pestormix_apk.fragments.HomeFragment;
import com.example.albert.pestormix_apk.fragments.MuseFragment;
import com.example.albert.pestormix_apk.fragments.SettingsFragment;
import com.example.albert.pestormix_apk.nfc.NfcController;
import com.example.albert.pestormix_apk.repositories.CocktailRepository;
import com.example.albert.pestormixlibrary.Constants;
import com.example.albert.pestormix_apk.utils.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends PestormixMasterActivity implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    Fragment fragment;
    private NavigationView drawer;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private TextView toolbarTitle;
    private NfcAdapter nfcAdapter;
    private GoogleApiClient mGoogleApiClient;

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        if (adapter != null) {
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
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        if (adapter != null)
            adapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeOrientationIfIsPhone();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initToolbar();
        initNavigationDrawer(toolbar);
        checkUserInformation();
        museVisible(getPestormixApplication().getBoolean(getString(R.string.PREFERENCE_MUSE), false));
        nfcAdapter = NfcController.getInstance(this).getAdapter();
        initGoogleApiClient();
    }

    private void checkUserInformation() {
        Bitmap image = Utils.stringToBitmap(getPestormixApplication().getString(Constants.PREFERENCES_USER_IMAGE, null));
        String name = getPestormixApplication().getString(Constants.PREFERENCES_USER_NAME, null);
        if (name != null && image != null) addHeader(name, image);
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        Menu menu = toolbar.getMenu();
        getMenuInflater().inflate(R.menu.menu_search_view, menu);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initNavigationDrawer(Toolbar toolbar) {
        drawer = (NavigationView) findViewById(R.id.main_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawer.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        int firtsScreen = R.id.navigation_cocktails;
        navigate(firtsScreen);
        drawer.getMenu().findItem(firtsScreen).setChecked(true);
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
            case R.id.navigation_muse:
                setToolbarTitleText(getText(R.string.title_muse));
                fragment = MuseFragment.getInstance();
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

    public void museVisible(boolean visible) {
        drawer.getMenu().findItem(R.id.navigation_muse).setVisible(visible);
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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        showToast(connectionResult.toString());
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        refreshUserInformation(Constants.DEFAULT_USER_ID,
                                Constants.DEFAULT_USER_NAME, null, false);
                        removeHeader();
                        CocktailRepository.restartCocktails(getRealm());
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        Log.d(TAG, "code:" + result.getStatus().getStatusCode());
        Log.d(TAG, "message:" + result.getStatus().getStatusMessage());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Uri url = acct.getPhotoUrl();
            String id = acct.getId();
            String displayName = acct.getDisplayName();
            Bitmap userImage = getUserImage(url);
            refreshUserInformation(id, displayName, userImage, true);
        } else {
            // Signed out, show unauthenticated UI.
            Utils.putBooleanPreference(Constants.PREFERENCES_USER_LOGGED, false);
            FragmentManager fragmentManager = getFragmentManager();
            SettingsFragment fragment = (SettingsFragment) fragmentManager.findFragmentById(R.id.main_content);
            fragment.addLoginTitle();
            signOut();
            showToast(R.string.error_login);
        }
    }

    private Bitmap getUserImage(Uri url) {
        Bitmap bitmap = null;
        if (url == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_person);
        } else {
            String urlString = url.toString();
            try {
                bitmap = new AsyncTask<String, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(String... urls) {
                        String urldisplay = urls[0];
                        Bitmap mIcon11 = null;
                        try {

                            InputStream in = new URL(urldisplay).openStream();
                            mIcon11 = BitmapFactory.decodeStream(in);

                        } catch (Exception e) {
                            Log.e("Error", e.getMessage());
                            e.printStackTrace();
                        }
                        return mIcon11;
                    }

                }.execute(urlString).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    protected void refreshUserInformation(String id, String displayName, Bitmap bitmap, boolean logged) {
        if (bitmap != null) {
            addHeader(displayName, bitmap);
        }
        saveUserInformationToPreferences(id, displayName, bitmap,logged);
        sendBroadcast(new Intent(Constants.ACTION_START_SYNC_WITH_REMOTE));
    }

    private void saveUserInformationToPreferences(String id, String name, Bitmap bitmap, boolean logged) {
        String image = null;
        if (bitmap != null)
            image = Utils.bitmapToString(bitmap);
        getPestormixApplication().putBoolean(Constants.PREFERENCES_USER_LOGGED, logged);
        getPestormixApplication().putString(Constants.PREFERENCES_USER_NAME, name);
        getPestormixApplication().putString(Constants.PREFERENCES_USER_IMAGE, image);
        getPestormixApplication().setUserId(id);
    }

    private void addHeader(String name, Bitmap image) {
        View view = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        ((ImageView) view.findViewById(R.id.image)).setImageBitmap(image);
        ((TextView) view.findViewById(R.id.name)).setText(name);
        drawer.addHeaderView(view);
    }

    private void removeHeader() {
        if (drawer.getHeaderCount() > 0) {
            drawer.removeHeaderView(drawer.getHeaderView(0));
        }
    }
}
