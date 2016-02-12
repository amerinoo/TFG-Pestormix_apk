package com.example.albert.pestormix_apk.listeners;

import android.nfc.Tag;

/**
 * Created by Albert on 12/02/2016.
 */
public interface OnNfcDataReceived {
    void processNfcData(Tag mytag);
}
