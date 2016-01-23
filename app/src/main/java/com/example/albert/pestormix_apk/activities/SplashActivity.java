package com.example.albert.pestormix_apk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMaster;
import com.example.albert.pestormix_apk.utils.Constants;

public class SplashActivity extends PestormixMaster {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initApp();
            }
        }, 2000);
    }

    private void initApp() {
        if (getPestormixApplication().getBoolean(Constants.PREFERENCE_TUTORIAL_KEY, true)) {
            openTutorial();
        } else {
            openMain();
        }
    }


    private void openTutorial() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void openMain() {
        Intent intent = new Intent(getPestormixApplication(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
