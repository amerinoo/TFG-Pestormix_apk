package com.example.albert.pestormix_apk.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterFragment;

/**
 * Created by Albert on 24/01/2016.
 */
public class AboutAuthorFragment extends PestormixMasterFragment {
    public static AboutAuthorFragment getInstance() {
        return new AboutAuthorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_author, container, false);
    }
}
