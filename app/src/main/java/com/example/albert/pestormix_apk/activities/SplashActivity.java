package com.example.albert.pestormix_apk.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixApplication;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.backend.cocktailApi.CocktailApi;
import com.example.albert.pestormix_apk.backend.cocktailApi.model.CocktailBean;
import com.example.albert.pestormix_apk.controllers.CocktailController;
import com.example.albert.pestormix_apk.controllers.DataController;
import com.example.albert.pestormix_apk.gcm.RegistrationIntentService;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

public class SplashActivity extends PestormixMasterActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SplashActivity";
    public final String serverRootUrl = "https://pestormix-apk.appspot.com/_ah/api/"; //No cal perque ja esta desplegat
//    public final String serverRootUrl = "http://10.0.2.2:8080/_ah/api/";


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeOrientationIfIsPhone();
        setContentView(R.layout.activity_splash);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean sentToken = ((PestormixApplication) getApplication()).getBoolean(Constants.PREFERENCES_SENT_TOKEN_TO_SERVER_KEY, false);
                if (sentToken) {
                    initApp();
                } else {
                    showToast("I need network the first time, sorry");
                    finish();
                }
            }
        };
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getPestormixApplication().getBoolean(Constants.PREFERENCES_SENT_TOKEN_TO_SERVER_KEY, false)) {
                    initApp();
                } else {
                    registerReceiver();

                    if (checkPlayServices()) {
                        // Start IntentService to register this application with GCM.
                        Intent intent = new Intent(getApplicationContext(), RegistrationIntentService.class);
                        startService(intent);
                    }
                }
            }
        }, 2000);
    }

    private void initApp() {
        if (getPestormixApplication().getBoolean(Constants.PREFERENCES_TUTORIAL_KEY, true)) {
            if (getPestormixApplication().getBoolean(Constants.PREFERENCES_INIT_DATA_KEY, true)) {
                getPestormixApplication().putBoolean(Constants.PREFERENCES_INIT_DATA_KEY, false);
                DataController.init(getRealm());
            }
            openTutorial();
        } else {
            updateCocktails();
            openMain();
        }
    }

    private void updateCocktails() {
        new AsyncTask<Long, Void, Void>() {

            @Override
            protected Void doInBackground(Long... params) {
                CocktailApi cocktailApi = new CocktailApi.Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(), null)
                        .setRootUrl(serverRootUrl)
                        .build();
                List<Cocktail> cocktails = CocktailController.getCocktails(getRealm());
                for (Cocktail cocktail : cocktails) {
                    CocktailBean cocktailBean = new CocktailBean();
                    cocktailBean.setName(cocktail.getName());
                    cocktailBean.setDescription(cocktail.getDescription());
                    cocktailBean.setAlcohol(cocktail.isAlcohol());
                    cocktailBean.setDrinks(CocktailController.getDrinksAsString(cocktail));
                    try {
                        cocktailApi.insertCocktail((long) 1, cocktailBean).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute((long) 1);
    }

    private void openTutorial() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacksAndMessages(null);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Constants.ACTION_REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
