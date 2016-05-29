package com.example.albert.pestormix_apk.application;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.activities.ManuallyActivity;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.repositories.CocktailRepository;
import com.example.albert.pestormix_apk.utils.Constants;
import com.example.albert.pestormix_apk.utils.Utils;

import io.realm.Realm;

/**
 * Created by Albert on 24/01/2016.
 */
public class PestormixMasterFragment extends Fragment {
    public void showToast(CharSequence text) {
        Utils.showToast(text);
    }

    public void showToast(int resId) {
        Utils.showToast(resId);
    }

    public Realm getRealm() {
        return Utils.getRealm();
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void startActivityAnimation() {
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.nothing);
    }

    public void updateCocktail(Cocktail cocktail) {
        Intent intent = new Intent(getActivity(), ManuallyActivity.class);
        intent.putExtra(Constants.EXTRA_COCKTAIL_NAME, cocktail.getName());
        intent.putExtra(Constants.EXTRA_COCKTAIL_DESCRIPTION, cocktail.getDescription());
        intent.putExtra(Constants.EXTRA_COCKTAIL_DRINKS, CocktailRepository.getDrinksAsString(cocktail, true));
        startActivity(intent);
    }
}
