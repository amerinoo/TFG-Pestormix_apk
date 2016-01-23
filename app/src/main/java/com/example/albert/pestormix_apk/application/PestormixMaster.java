package com.example.albert.pestormix_apk.application;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Albert on 23/01/2016.
 */
public class PestormixMaster extends AppCompatActivity {
    
    public PestormixApplication getPestormixApplication() {
        return (PestormixApplication) getApplication();
    }

    public void showToast(CharSequence text) {
        Toast.makeText(getPestormixApplication(), text, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int resId) {
        Toast.makeText(getPestormixApplication(), resId, Toast.LENGTH_SHORT).show();
    }
}
