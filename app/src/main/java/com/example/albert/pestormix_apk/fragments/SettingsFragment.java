package com.example.albert.pestormix_apk.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.activities.ConfigValvesActivity;
import com.example.albert.pestormix_apk.activities.MainActivity;
import com.example.albert.pestormix_apk.utils.Utils;
import com.example.albert.pestormixlibrary.Constants;

import java.util.Locale;

/**
 * Created by Albert on 24/01/2016.
 */
public class SettingsFragment extends PreferenceFragment {

    private Preference loginPreference;

    public static SettingsFragment getInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configView();
    }

    private void configView() {
        findPreference(getString(R.string.PREFERENCE_VALVES)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Locale locale = new Locale("ro");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getActivity().getBaseContext().getResources().updateConfiguration(config,
                        getActivity().getBaseContext().getResources().getDisplayMetrics());
                Intent i = getActivity().getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
//                goConfigValves();
                return true;
            }
        });
        loginPreference = findPreference(getString(R.string.PREFERENCE_LOGIN));
        addLoginTitle();
        loginPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (isUserLogged()) {
                    showToast(R.string.sign_out_google);
                    signOutGoogle();
                    preference.setTitle(R.string.sign_in_google);
                } else {
                    showToast(R.string.sign_in_google);
                    signInGoogle();
                    preference.setTitle(R.string.sign_out_google);
                }
                loginPreference.setEnabled(false);
                reenableLogin();
                return true;
            }
        });
        findPreference(getString(R.string.PREFERENCE_MUSE)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
                boolean visible = checkBoxPreference.isChecked();
                museVisible(visible);
                return false;
            }
        });
        findPreference(getString(R.string.PREFERENCE_LANGUAGE)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, final Object language) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Language settings");
                builder.setMessage("Are you sure you want to change the language settings?");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Locale locale = new Locale((String) language);
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getActivity().getBaseContext().getResources().updateConfiguration(config,
                                getActivity().getBaseContext().getResources().getDisplayMetrics());
                        Intent i = getActivity().getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                })
                        .setNegativeButton(android.R.string.no, null);

                builder.show();

                return true;
            }
        });
    }

    private void reenableLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginPreference.setEnabled(true);
            }
        }, 1000);
    }

    public void addLoginTitle() {
        if (isUserLogged()) {
            loginPreference.setTitle(R.string.sign_out_google);
        } else {
            loginPreference.setTitle(R.string.sign_in_google);
        }
    }

    private boolean isUserLogged() {
        return Utils.getBooleanPreference(Constants.PREFERENCES_USER_LOGGED, false);
    }

    private void signInGoogle() {
        ((MainActivity) getActivity()).signIn();
    }

    private void signOutGoogle() {
        ((MainActivity) getActivity()).signOut();
    }


    private void goConfigValves() {
        Intent intent = new Intent(getActivity(), ConfigValvesActivity.class);
        startActivity(intent);
        startActivityAnimation();
    }

    public void startActivityAnimation() {
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.nothing);
    }

    private void museVisible(boolean visible) {
        ((MainActivity) getActivity()).museVisible(visible);
    }

    public void showToast(int resId) {
        Utils.showToast(resId);
    }
}
