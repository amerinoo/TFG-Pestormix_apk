package com.example.albert.pestormix_apk.controllers;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.view.View;

/**
 * Created by Albert on 08/02/2016.
 */
public class NfcController {
    private static NfcController nfcController;
    private NfcAdapter adapter;
    private Context context;

    public static NfcController getInstance(Context context) {
        if (nfcController == null) {
            nfcController = new NfcController(context);
        }
        return nfcController;
    }

    private NfcController(Context context) {
        this.context = context;
        this.adapter = NfcAdapter.getDefaultAdapter(context);
    }

    public NfcAdapter getAdapter() {
        return adapter;
    }

    public boolean hasAdapter() {
        return adapter != null;
    }

    public boolean isEnabled() {
        return adapter.isEnabled();
    }

    public void initNfcView(View v) {
        setOnClickListener(v);
        checkIfNeedDisable(v);
    }

    private void setOnClickListener(View v) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nfcController.isEnabled()) {
                } else {
                }
            }
        };
        v.setOnClickListener(listener);
    }

    private void checkIfNeedDisable(View v) {
        if (!nfcController.hasAdapter()) {
            disableView(v);
        }
    }

    private void disableView(View view) {
        view.setEnabled(false);
        view.setAlpha(0.5f);
    }
}
