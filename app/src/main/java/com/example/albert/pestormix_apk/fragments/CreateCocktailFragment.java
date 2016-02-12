package com.example.albert.pestormix_apk.fragments;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.activities.ManuallyActivity;
import com.example.albert.pestormix_apk.application.PestormixMasterFragment;
import com.example.albert.pestormix_apk.controllers.NfcController;
import com.example.albert.pestormix_apk.controllers.NfcReader;
import com.example.albert.pestormix_apk.listeners.OnNfcDataReceived;

/**
 * Created by Albert on 24/01/2016.
 */
public class CreateCocktailFragment extends PestormixMasterFragment implements OnNfcDataReceived {
    private View mainView;

    public static CreateCocktailFragment getInstance() {
        return new CreateCocktailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_cocktail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        configView();
    }

    private void configView() {
        final NfcController nfcController = NfcController.getInstance(getActivity());
        Button manually = (Button) mainView.findViewById(R.id.manually_button);
        Button qr = (Button) mainView.findViewById(R.id.qr_button);
        Button nfc = (Button) mainView.findViewById(R.id.nfc_button);

        manually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goManually();
            }
        });
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getString(R.string.qr_code));
            }
        });
        nfcController.initNfcView(nfc);
    }

    private void goManually() {
        Intent intent = new Intent(getActivity(), ManuallyActivity.class);
        startActivity(intent);
    }

    @Override
    public void processNfcData(Tag mytag) {
        String read = NfcReader.read(mytag);
        showToast(read);
    }
}
