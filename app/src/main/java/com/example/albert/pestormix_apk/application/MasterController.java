package com.example.albert.pestormix_apk.application;

import android.content.res.Resources;

/**
 * Created by Sergi on 03/03/2016.
 */
public abstract class MasterController {
    public static Resources system = Resources.getSystem();

    public static String getStringResource(int id) {
        return system.getString(id);
    }
}
