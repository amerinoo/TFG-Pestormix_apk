package com.example.albert.pestormix_apk.application;

import android.app.Fragment;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.activities.MainActivity;

import io.realm.Realm;

/**
 * Created by Albert on 24/01/2016.
 */
public class PestormixMasterFragment extends Fragment {
    public void showToast(CharSequence text) {
        ((PestormixMasterActivity) getActivity()).showToast(text);
    }

    public Realm getRealm() {
        return ((PestormixMasterActivity) getActivity()).getRealm();
    }

    public void showToast(int resId) {
        ((PestormixMasterActivity) getActivity()).showToast(resId);
    }

    public void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public void startActivityAnimation(){
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.nothing);
    }
}
