package com.example.albert.pestormix_apk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.albert.pestormix_apk.application.PestormixApplication;

import java.io.ByteArrayOutputStream;

/**
 * Created by Albert on 22/03/2016.
 */
public abstract class Utils {

    public static String getStringResource(int id) {
        return PestormixApplication.getContext().getString(id);
    }

    public static void putBooleanPreference(String key, Boolean value) {
        PestormixApplication.getContext().putBoolean(key, value);
    }

    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
