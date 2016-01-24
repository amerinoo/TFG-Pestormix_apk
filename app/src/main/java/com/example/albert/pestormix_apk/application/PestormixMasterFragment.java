package com.example.albert.pestormix_apk.application;

import android.app.Fragment;

import com.example.albert.pestormix_apk.application.PestormixMasterActivity;

/**
 * Created by Albert on 24/01/2016.
 */
public class PestormixMasterFragment extends Fragment {
    public void showToast(CharSequence text) {
        ((PestormixMasterActivity) getActivity()).showToast(text);
    }

    public void showToast(int resId) {
        ((PestormixMasterActivity) getActivity()).showToast(resId);
    }
}
