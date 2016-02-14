package com.example.albert.pestormix_apk.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.fragments.DetailHelpFragment;
import com.example.albert.pestormix_apk.utils.Constants;


public class DetailHelpActivity extends PestormixMasterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeOrientationIfIsPhone();
        setContentView(R.layout.activity_detail_help);
        int id = getIntent().getIntExtra(Constants.EXTRA_QUESTION_ID, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(getString(R.string.title_help));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Fragment fragment = DetailHelpFragment.getInstance(id);
        loadFragment(R.id.detail_content, fragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityAnimation();
    }
}
