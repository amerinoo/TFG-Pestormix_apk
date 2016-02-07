package com.example.albert.pestormix_apk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.activities.ConfigValvesActivity;
import com.example.albert.pestormix_apk.application.PestormixMasterFragment;

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
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.row_single_text_view, getResources().getStringArray(R.array.settings_array));
        ListView list = (ListView) mainView.findViewById(R.id.settings_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        goConfigValves();
                        break;
                    case 1:
                        break;
                }
            }
        });
    }

    private void goConfigValves() {
        Intent intent = new Intent(getActivity(), ConfigValvesActivity.class);
        startActivity(intent);
        startActivityAnimation();
    }
}
