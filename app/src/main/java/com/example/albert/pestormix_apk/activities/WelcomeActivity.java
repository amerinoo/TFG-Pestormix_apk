package com.example.albert.pestormix_apk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMaster;

public class WelcomeActivity extends PestormixMaster {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViewById(R.id.continue_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getPestormixApplication(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
