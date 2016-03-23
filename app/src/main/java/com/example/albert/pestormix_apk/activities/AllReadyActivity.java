package com.example.albert.pestormix_apk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterActivity;
import com.example.albert.pestormix_apk.utils.Constants;

public class AllReadyActivity extends PestormixMasterActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeOrientationIfIsPhone();
        setContentView(R.layout.activity_all_ready);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPestormixApplication().putBoolean(Constants.PREFERENCES_TUTORIAL_KEY, false);
                Intent intent = new Intent(AllReadyActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 8000);
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacksAndMessages(null);
        super.onBackPressed();
    }
}
