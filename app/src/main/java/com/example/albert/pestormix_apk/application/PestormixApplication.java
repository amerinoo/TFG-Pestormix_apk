package com.example.albert.pestormix_apk.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.albert.pestormix_apk.Utils.Constants;

/**
 * Created by Albert on 23/01/2016.
 */
public class PestormixApplication extends Application {

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
