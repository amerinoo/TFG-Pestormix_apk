package com.example.albert.pestormix_apk.controllers;

import com.example.albert.pestormix_apk.models.Drink;
import com.example.albert.pestormix_apk.models.Valve;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Albert on 02/02/2016.
 */
public class ValveController {
    public static List<Valve> init() {
        List<Valve> valves = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Valve valve = new Valve();
            valve.setId(i);
            valve.setDrinkPosition(0);
            valves.add(valve);
        }
        return valves;
    }

    public static List<Valve> getValves(Realm realm) {
        return DataController.getValves(realm);
    }

    public static void updateValve(Realm realm, Valve valve, Drink drink, int position) {
        DataController.updateValve(realm, valve, drink, position);
    }
}
