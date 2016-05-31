package com.example.albert.pestormix_apk.repositories;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.backend.valveApi.model.ValveBean;
import com.example.albert.pestormix_apk.controllers.DataController;
import com.example.albert.pestormix_apk.models.Drink;
import com.example.albert.pestormix_apk.models.Valve;
import com.example.albert.pestormix_apk.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Albert on 02/02/2016.
 */
public abstract class ValveRepository {
    public static List<Valve> init() {
        List<Valve> valves = new ArrayList<>();
        for (int i = 0; i < Utils.getIntegerResource(R.integer.number_of_valves); i++) {
            Valve valve = new Valve();
            valve.setId(i);
            valve.setDrinkPosition(i);
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

    public static ValveBean toValveBean(Valve valve) {
        ValveBean valveBean = new ValveBean();
        valveBean.setId(String.valueOf(valve.getId()));
        valveBean.setDrinkName(valve.getDrinkName());
        valveBean.setDrinkAlcohol(valve.isDrinkAlcohol());
        return valveBean;
    }


    public static List<ValveBean> getValvesAsValvesBeen(List<Valve> valves) {
        List<ValveBean> valveBeen = new ArrayList<>();
        for (Valve valve : valves) {
            valveBeen.add(toValveBean(valve));
        }
        return valveBeen;
    }
}
