package com.example.albert.pestormix_apk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.controllers.DataController;
import com.example.albert.pestormix_apk.utils.Constants;

public class SplashActivity extends PestormixMasterActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initApp();
            }
        }, 2000);

    }

    private void initApp() {
        if (getPestormixApplication().getBoolean(Constants.PREFERENCE_TUTORIAL_KEY, true)) {
            if (getPestormixApplication().getBoolean(Constants.PREFERENCE_INIT_DATA_KEY, true)) {
                getPestormixApplication().putBoolean(Constants.PREFERENCE_INIT_DATA_KEY, false);
                DataController.init(getRealm());
            }
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

    @Override
    public void onBackPressed() {
        handler.removeCallbacksAndMessages(null);
        super.onBackPressed();
    }
}
