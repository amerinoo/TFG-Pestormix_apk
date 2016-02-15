package com.example.albert.pestormix_apk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.activities.ConfigValvesActivity;
import com.example.albert.pestormix_apk.activities.MainActivity;
import com.example.albert.pestormix_apk.application.PestormixApplication;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.application.PestormixMasterFragment;
import com.example.albert.pestormix_apk.utils.Constants;

/**
 * Created by Albert on 24/01/2016.
 */
public class SettingsFragment extends PestormixMasterFragment {
    private View mainView;

    public static SettingsFragment getInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        configView();
    }

    private void configView() {
        mainView.findViewById(R.id.config_valves).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goConfigValves();
            }
        });
        mainView.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(R.string.login);
            }
        });
        final CheckBox checkBox = (CheckBox) mainView.findViewById(R.id.muse_activate);
        checkBox.setChecked(getPestormixApplication().getBoolean(Constants.PREFERENCE_MUSE_ACTIVATED, false));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean visible = checkBox.isChecked();
                getPestormixApplication().putBoolean(Constants.PREFERENCE_MUSE_ACTIVATED, visible);
                museVisible(visible);
            }
        });
    }

    private PestormixApplication getPestormixApplication() {
        return ((PestormixMasterActivity) getActivity()).getPestormixApplication();
    }

    private void goConfigValves() {
        Intent intent = new Intent(getActivity(), ConfigValvesActivity.class);
        startActivity(intent);
        startActivityAnimation();
    }

    private void museVisible(boolean visible) {
        ((MainActivity) getActivity()).museVisible(visible);
    }
}
