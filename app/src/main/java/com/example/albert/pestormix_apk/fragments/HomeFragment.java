package com.example.albert.pestormix_apk.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.adapters.CocktailAdapter;
import com.example.albert.pestormix_apk.application.PestormixMasterFragment;
import com.example.albert.pestormix_apk.controllers.DataController;

/**
 * Created by Albert on 24/01/2016.
 */
public class HomeFragment extends PestormixMasterFragment {


    private View mainView;

    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        configView();
    }

    private void configView() {
        Spinner glasses = (Spinner) mainView.findViewById(R.id.glass_spinner);
        ImageButton qr = (ImageButton) mainView.findViewById(R.id.qr_button);
        ImageButton nfc = (ImageButton) mainView.findViewById(R.id.nfc_button);
        ListView cocktails = (ListView) mainView.findViewById(R.id.cocktails_list);
        final CocktailAdapter adapter = new CocktailAdapter(getActivity(), DataController.getCocktails(getRealm()));
        cocktails.setAdapter(adapter);

        glasses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] stringArray = getResources().getStringArray(R.array.glass_array);
                showToast(stringArray[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataController.removeAllCocktails(getRealm());
                adapter.update(getRealm());
                showToast(getString(R.string.qr_code));
            }
        });
        nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getString(R.string.nfc_tag));
            }
        });

        cocktails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.confirmOrder))
                        .setMessage(getString(R.string.youre_asking) + name)
                        .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showToast(getString(R.string.accept));
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showToast(getString(R.string.cancel));
                            }
                        }).show();

            }
        });
    }
}
