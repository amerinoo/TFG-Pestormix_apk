package com.example.albert.pestormix_apk.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.albert.pestormix_apk.utils.Constants;

/**
 * Created by Albert on 23/01/2016.
 */
public class PestormixApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public SharedPreferences getPestormixSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCES_KEY, Context.MODE_PRIVATE);
        return preferences;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getPestormixSharedPreferences().getBoolean(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        getPestormixSharedPreferences().edit().putBoolean(key, value).commit();
    }
}
