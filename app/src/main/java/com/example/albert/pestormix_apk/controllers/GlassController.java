package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.models.Glass;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Albert on 07/02/2016.
 */
public abstract class GlassController {
    public static List<Glass> init() {
        List<Glass> glasses = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Glass glass = new Glass();
            glass.setName("Glass " + i);
            glasses.add(glass);
        }
        return glasses;
    }

    public static List<String> getGlassesNames(Realm realm) {
        return DataController.getGlassesNames(realm);
    }
}
