package com.example.albert.pestormix_apk.fragments;

import android.app.Activity;
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
import com.example.albert.pestormix_apk.activities.ScanQrActivity;
import com.example.albert.pestormix_apk.application.PestormixMasterFragment;
import com.example.albert.pestormix_apk.repositories.CocktailRepository;
import com.example.albert.pestormix_apk.nfc.NfcController;
import com.example.albert.pestormix_apk.listeners.OnNfcDataReceived;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.utils.ActivityRequestCodes;
import com.example.albert.pestormixlibrary.Constants;

/**
 * Created by Albert on 24/01/2016.
 */
public class CreateCocktailFragment extends PestormixMasterFragment implements OnNfcDataReceived {
    private View mainView;
    private Button nfc;
    private NfcController nfcController;

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
        nfcController = NfcController.getInstance(getActivity());
        Button manually = (Button) mainView.findViewById(R.id.manually_button);
        Button qr = (Button) mainView.findViewById(R.id.qr_button);
        nfc = (Button) mainView.findViewById(R.id.nfc_button);

        manually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goManually();
            }
        });
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScanQrActivity.class);
                startActivityForResult(intent, ActivityRequestCodes.CODE_QR);
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
        String data = nfcController.read(mytag, nfc);
        processData(data);
    }

    private void processData(String data) {
        Cocktail cocktail = CocktailRepository.processData(data);
        if (cocktail != null) {
            updateCocktail(cocktail);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCodes.CODE_QR) {
            if (resultCode == Activity.RESULT_OK) {
                String extra = data.getStringExtra(Constants.EXTRA_COCKTAIL);
                processData(extra);
            }
        }
    }
}
