package com.example.albert.pestormix_apk.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.fragments.DetailHelpFragment;
import com.example.albert.pestormix_apk.utils.Constants;

public class DetailHelpActivity extends PestormixMasterActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_help);
        int position = getIntent().getIntExtra(Constants.EXTRA_POSITION, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Fragment fragment = DetailHelpFragment.getInstance(position);
        loadFragment(R.id.detail_content, fragment);
    }
}
