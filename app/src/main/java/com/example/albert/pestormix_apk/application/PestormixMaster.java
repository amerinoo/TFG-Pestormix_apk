package com.example.albert.pestormix_apk.application;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Albert on 23/01/2016.
 */
public class PestormixMaster extends AppCompatActivity {

    @Override
    public PestormixApplication getApplicationContext() {
        return (PestormixApplication) super.getApplicationContext();
    }
}
