package com.example.albert.pestormix_apk.application;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.albert.pestormix_apk.R;

import io.realm.Realm;

/**
 * Created by Albert on 23/01/2016.
 */
public class PestormixMasterActivity extends AppCompatActivity {

    private Realm realm;

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

    public Realm getRealm() {
        if (realm == null) {
            realm = Realm.getInstance(this);
        }
        return realm;
    }

    public void startActivityAnimation() {
        overridePendingTransition(R.anim.nothing, R.anim.slide_out);
    }

    public void changeOrientationIfIsPhone() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (!isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
