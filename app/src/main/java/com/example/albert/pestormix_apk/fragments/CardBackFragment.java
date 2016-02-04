package com.example.albert.pestormix_apk.fragments;

/**
 * Created by Albert on 05/12/2015.
 */


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.listeners.OnInformationClickListener;

/**
 * A fragment representing the back of the card.
 */
public class CardBackFragment extends Fragment {
    OnInformationClickListener mListener;
    private String description;

    public static CardBackFragment newInstance(String description) {
        CardBackFragment cardBackFragment = new CardBackFragment();
        cardBackFragment.description = description;
        return cardBackFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_back, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) view.findViewById(R.id.description)).setText(description);
        view.findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onInformationClick();
            }
        });
    }

    public void setListener(OnInformationClickListener mListener) {
        this.mListener = mListener;
    }
}
