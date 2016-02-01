package com.example.albert.pestormix_apk.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_valves, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            Log.e("FlipCard", "Entro");
            getChildFragmentManager().popBackStack();
            mShowingBack = false;
            return;
        }
        CardBackFragment cardBackFragment = CardBackFragment.newInstance(drink.getDescription());
        cardBackFragment.setListener(this);
        // Flip to the back.

        mShowingBack = true;

        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        getChildFragmentManager()
                .beginTransaction()

                        // Replace the default fragment animations with animator resources representing
                        // rotations when switching to the back of the card, as well as animator
                        // resources representing rotations when flipping back to the front (e.g. when
                        // the system Back button is pressed).

                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out
                        , R.animator.card_flip_left_in, R.animator.card_flip_left_out)

                        // Replace any fragments currently in the container view with a fragment
                        // representing the next page (indicated by the just-incremented currentPage
                        // variable).
                .replace(R.id.container, cardBackFragment)

                        // Add this transaction to the back stack, allowing users to press Back
                        // to get to the front of the card.
                .addToBackStack(null)

                        // Commit the transaction.
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
