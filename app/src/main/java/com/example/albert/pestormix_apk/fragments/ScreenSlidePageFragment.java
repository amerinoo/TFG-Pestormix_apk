package com.example.albert.pestormix_apk.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterFragment;
import com.example.albert.pestormix_apk.controllers.DrinkController;
import com.example.albert.pestormix_apk.listeners.OnInformationClickListener;
import com.example.albert.pestormix_apk.models.Drink;
import com.example.albert.pestormix_apk.utils.Constants;

/**
 * Created by Albert on 01/02/2016.
 */
public class ScreenSlidePageFragment extends PestormixMasterFragment implements OnInformationClickListener {

    private boolean mShowingBack;


    public static ScreenSlidePageFragment newInstance(Drink drink) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_DRINK_NAME, drink.getName());
        fragment.setArguments(args);
        return fragment;
    }

    Drink drink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String nameDrink = getArguments().getString(Constants.EXTRA_DRINK_NAME);
        drink = DrinkController.getDrinkByName(getRealm(), nameDrink);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_slide_drinks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.name)).setText(drink.getName());
        CardFrontFragment cardFrontFragment = CardFrontFragment.newInstance(drink.getImage());
        cardFrontFragment.setListener(this);
        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, cardFrontFragment)
                    .commit();

        }
    }

    private void flipCard() {

        if (mShowingBack) {
            getChildFragmentManager().popBackStack();
            mShowingBack = false;
            return;
        }
        CardBackFragment cardBackFragment = CardBackFragment.newInstance(drink.getDescription());
        cardBackFragment.setListener(this);
        mShowingBack = true;
        getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out
                        , R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.container, cardBackFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onPause() {
        for (int i = 0; i < getChildFragmentManager().getBackStackEntryCount(); i++)
            getChildFragmentManager().popBackStack();
        mShowingBack = false;
        super.onPause();
    }

    @Override
    public void onInformationClick() {
        flipCard();
    }
}
