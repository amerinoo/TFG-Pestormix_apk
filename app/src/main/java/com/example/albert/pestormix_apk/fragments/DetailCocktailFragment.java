package com.example.albert.pestormix_apk.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterFragment;
import com.example.albert.pestormix_apk.repositories.DrinkRepository;
import com.example.albert.pestormix_apk.models.Drink;

import java.util.List;
import java.util.Random;

/**
 * Created by Albert on 13/03/2016.
 */
public class DetailCocktailFragment extends PestormixMasterFragment {

    private String name;
    private String drinks;
    private View mainView;

    public static DetailCocktailFragment getInstance(String name, String drinks) {
        DetailCocktailFragment fragment = new DetailCocktailFragment();
        fragment.name = name;
        fragment.drinks = drinks;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_cocktail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        configView();
    }

    private void configView() {
        update(drinks);
    }

    public void update(String drinks) {
        ((TextView) mainView.findViewById(R.id.title)).setText(name);
        ((TextView) mainView.findViewById(R.id.drinks)).setText(drinks);
        LinearLayout alcohol = (LinearLayout) mainView.findViewById(R.id.alcohol);
        LinearLayout noAlcohol = (LinearLayout) mainView.findViewById(R.id.no_alcohol);

        List<Drink> drinkList = DrinkRepository.getDrinksFromString(getRealm(), drinks);
        for (Drink drink : drinkList) {
            if (drink.isAlcohol())
                alcohol.addView(getTextView(drink.getName()));
            else
                noAlcohol.addView(getTextView(drink.getName()));
        }
        if (alcohol.getChildCount() == 0) alcohol.setVisibility(View.GONE);
    }

    private TextView getTextView(String nom) {
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        textView.setText(nom);
        textView.setGravity(Gravity.CENTER);
        Random random = new Random();
        textView.setBackgroundColor(Color.rgb(random.nextInt(60), random.nextInt(254), random.nextInt(254)));
        return textView;
    }
}
