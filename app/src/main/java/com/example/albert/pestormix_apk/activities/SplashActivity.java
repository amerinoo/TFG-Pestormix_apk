package com.example.albert.pestormix_apk.activities;

import android.os.Bundle;
import android.os.Handler;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.Utils.Constants;
import com.example.albert.pestormix_apk.application.PestormixMaster;

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
        }, 1500);
    }

    private void initApp() {
        if (getPestormixApplication().getBoolean(Constants.PREFERENCE_TUTORIAL_KEY, true)) {
            getPestormixApplication().putBoolean(Constants.PREFERENCE_TUTORIAL_KEY, false);
            openTutorial();
        } else {
            openMain();
        }
    }


    private void openTutorial() {
        showToast("Open Tutorial");
    }

    private void openMain() {
        showToast("Open Main");
    }

}
