package com.example.albert.pestormix_apk.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import com.example.albert.pestormix_apk.R;

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
                    enableNfc();
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

    private void enableNfc() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.information)
                .setMessage(R.string.activate_nfc)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            context.startActivity(intent);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();

    }
}
