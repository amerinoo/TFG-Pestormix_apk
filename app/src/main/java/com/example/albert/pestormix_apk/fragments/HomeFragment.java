package com.example.albert.pestormix_apk.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.activities.MainActivity;
import com.example.albert.pestormix_apk.activities.ManuallyActivity;
import com.example.albert.pestormix_apk.adapters.CocktailAdapter;
import com.example.albert.pestormix_apk.application.PestormixMasterFragment;
import com.example.albert.pestormix_apk.controllers.CocktailController;
import com.example.albert.pestormix_apk.controllers.DataController;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.utils.Constants;

import java.util.List;

/**
 * Created by Albert on 24/01/2016.
 */
public class HomeFragment extends PestormixMasterFragment {


    private View mainView;
    private String cocktailName;
    private CocktailAdapter adapter;
    private List<String> cocktailsName;
    private ArrayAdapter<String> stringArrayAdapter;

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
        LinearLayout searchView = ((MainActivity) getActivity()).getSearchView();
        final ListView cocktails = (ListView) mainView.findViewById(R.id.cocktails_list);

        AppCompatAutoCompleteTextView searchText = (AppCompatAutoCompleteTextView) searchView.findViewById(R.id.search_text);
        cocktailsName = DataController.getCocktailsNames(getRealm());
        stringArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, cocktailsName);
        searchText.setAdapter(stringArrayAdapter);
        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = getStringOfTextView((TextView) view.findViewById(android.R.id.text1));
                hideKeyboard();
                closeSearchView();
                showConfirmOrder(name);
            }
        });

        adapter = new CocktailAdapter(getActivity(), DataController.getCocktails(getRealm()));
        cocktails.setAdapter(adapter);
        registerForContextMenu(cocktails);
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
                DataController.generateCocktails(getRealm());
                adapter.update(getRealm());
                showToast(getString(R.string.nfc_tag));
            }
        });

        cocktails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = getStringOfTextView((TextView) view.findViewById(R.id.name));
                showConfirmOrder(name);

            }
        });
    }

    private void showConfirmOrder(String name) {
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == R.id.cocktails_list) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            CocktailAdapter adapter = (CocktailAdapter) ((ListView) view).getAdapter();
            cocktailName = adapter.getItem(acmi.position).getName();
            menu.setHeaderTitle(cocktailName);
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_cocktail, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        String itemName = item.getTitle().toString();
        switch (id) {
            case R.id.option_1: //Details
                showDetails(cocktailName);
                break;
            case R.id.option_2: //Edit
                updateCocktail(cocktailName);
                break;
            case R.id.option_3: //Remove
                removeCocktail(cocktailName);
                break;
        }
        return true;
    }

    private void showDetails(String cocktailName) {
        Cocktail cocktail = DataController.getCocktailByName(getRealm(), cocktailName);
        final AlertDialog dialog;
        View detailsView = LayoutInflater.from(getActivity()).inflate(R.layout.cocktail_details, null, false);
        ((TextView) detailsView.findViewById(R.id.name)).setText(cocktail.getName());
        if (cocktail.getDescription().equals("")) {
            ((TextView) detailsView.findViewById(R.id.description)).setText(R.string.missing_description);
        } else {
            ((TextView) detailsView.findViewById(R.id.description)).setText(cocktail.getDescription());
        }
        ((TextView) detailsView.findViewById(R.id.drinks)).setText(CocktailController.getDrinksAsString(cocktail, getString(R.string.drinks_detail_separator)));
        dialog = new AlertDialog.Builder(getActivity())
                .setView(detailsView)
                .create();
        detailsView.findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updateCocktail(String cocktailName) {
        Cocktail cocktail = DataController.getCocktailByName(getRealm(), cocktailName);
        Intent intent = new Intent(getActivity(), ManuallyActivity.class);
        intent.putExtra(Constants.EXTRA_COCKTAIL_NAME, cocktail.getName());
        intent.putExtra(Constants.EXTRA_COCKTAIL_DESCRIPTION, cocktail.getDescription());
        intent.putExtra(Constants.EXTRA_COCKTAIL_DRINKS, CocktailController.getDrinksAsString(cocktail));
        startActivity(intent);
    }

    private void removeCocktail(final String cocktailName) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.are_you_sure)
                .setMessage(R.string.cocktail_removed)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataController.removeCocktailByName(getRealm(), cocktailName);
                        adapter.update(getRealm());
                        cocktailsName.remove(cocktailName);
                        stringArrayAdapter.clear();
                        stringArrayAdapter.addAll(cocktailsName);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private String getStringOfTextView(TextView view) {
        return view.getText().toString();
    }
}
