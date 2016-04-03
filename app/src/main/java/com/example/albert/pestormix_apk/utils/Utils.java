package com.example.albert.pestormix_apk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.widget.Toast;

import com.example.albert.pestormix_apk.application.PestormixApplication;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;

/**
 * Created by Albert on 22/03/2016.
 */
public abstract class Utils {

    public static Realm getRealm() {
        return PestormixApplication.getContext().getRealm();
    }

    public static String getStringResource(int id) {
        return PestormixApplication.getContext().getString(id);
    }
    public static final int getColorResource(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
    public static boolean getBooleanPreference(String key, Boolean defValue) {
        return PestormixApplication.getContext().getBoolean(key, defValue);
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

    public static void showToast(CharSequence text) {
        Toast toast = Toast.makeText(PestormixApplication.getContext(), text, Toast.LENGTH_SHORT);
        toast.getView().findViewById(android.R.id.message).setBackgroundResource(android.R.color.transparent);
        toast.show();
    }

    public static void showToast(int resId) {
        Toast toast = Toast.makeText(PestormixApplication.getContext(), resId, Toast.LENGTH_SHORT);
        toast.getView().findViewById(android.R.id.message).setBackgroundResource(android.R.color.transparent);
        toast.show();
    }

}
