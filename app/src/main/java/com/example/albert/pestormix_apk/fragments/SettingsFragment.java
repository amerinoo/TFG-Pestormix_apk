package com.example.albert.pestormix_apk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.activities.ConfigValvesActivity;
import com.example.albert.pestormix_apk.activities.MainActivity;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;

/**
 * Created by Albert on 24/01/2016.
 */
public class SettingsFragment extends PreferenceFragment {

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
        findPreference("valves").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                goConfigValves();
                return true;
            }
        });
        findPreference("login").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showToast(R.string.login_google);
                signInGoogle();
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
    }

    private void signInGoogle() {
        ((MainActivity) getActivity()).signIn();
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
        ((PestormixMasterActivity) getActivity()).showToast(resId);
    }
}
