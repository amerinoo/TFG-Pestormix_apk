package com.example.albert.pestormix_apk.activities;

import android.os.Bundle;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMaster;

public class MainActivity extends PestormixMaster {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
