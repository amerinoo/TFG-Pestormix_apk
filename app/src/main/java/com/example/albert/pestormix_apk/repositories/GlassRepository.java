package com.example.albert.pestormix_apk.repositories;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.controllers.DataController;
import com.example.albert.pestormix_apk.models.Glass;
import com.example.albert.pestormix_apk.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Albert on 07/02/2016.
 */
public abstract class GlassRepository {
    public static List<Glass> init() {
        List<Glass> glasses = new ArrayList<>();
        List<Integer> list = getGlassesCapacities();
        for (Integer i : list) {
            Glass glass = new Glass();
            glass.setName(String.format(Utils.getStringResource(R.string.glass_name), i));
            glass.setCapacity(i);
            glasses.add(glass);
        }
        return glasses;
    }

    private static List<Integer> getGlassesCapacities() {
        List<Integer> integers = new ArrayList<>();
        integers.add(25);
        integers.add(33);
        integers.add(50);
        integers.add(100);
        return integers;
    }

    public static List<String> getGlassesNames(Realm realm) {
        List<Glass> glasses = DataController.getGlasses(realm);
        List<String> names = new ArrayList<>();
        for (Glass glass : glasses) {
            names.add(glass.getName());
        }
        return names;
    }

    public static Glass getGlasseByName(Realm realm, String name) {
        return DataController.getGlassesByName(realm, name);

    }
}
