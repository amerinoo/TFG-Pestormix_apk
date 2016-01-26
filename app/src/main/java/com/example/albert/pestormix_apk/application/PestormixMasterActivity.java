package com.example.albert.pestormix_apk.application;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Albert on 23/01/2016.
 */
public class PestormixMasterActivity extends AppCompatActivity {

    public PestormixApplication getPestormixApplication() {
        return (PestormixApplication) getApplication();
    }

    public void showToast(CharSequence text) {
        Toast toast = Toast.makeText(getPestormixApplication(), text, Toast.LENGTH_SHORT);
        toast.getView().findViewById(android.R.id.message).setBackgroundResource(android.R.color.transparent);
        toast.show();
    }

    public void showToast(int resId) {
        Toast toast = Toast.makeText(getPestormixApplication(), resId, Toast.LENGTH_SHORT);
        toast.getView().findViewById(android.R.id.message).setBackgroundResource(android.R.color.transparent);
        toast.show();
    }

    public void loadFragment(int resId, Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentManager.findFragmentById(resId) == null) {
            transaction.add(resId, fragment);
        } else {
            transaction.replace(resId, fragment);
        }
        transaction.commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
