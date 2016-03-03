package com.example.albert.pestormix_apk.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.activities.ScanQrActivity;
import com.example.albert.pestormix_apk.adapters.CocktailAdapter;
import com.example.albert.pestormix_apk.application.PestormixMasterFragment;
import com.example.albert.pestormix_apk.controllers.CocktailController;
import com.example.albert.pestormix_apk.controllers.DataController;
import com.example.albert.pestormix_apk.controllers.NetworkController;
import com.example.albert.pestormix_apk.nfc.NfcController;
import com.example.albert.pestormix_apk.listeners.OnNfcDataReceived;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.utils.ActivityRequestCodes;
import com.example.albert.pestormix_apk.utils.Constants;

import java.util.List;

/**
 * Created by Albert on 24/01/2016.
 */
public class HomeFragment extends PestormixMasterFragment implements OnNfcDataReceived {


    private View mainView;
    private String cocktailName;
    private String glassName;
    private CocktailAdapter adapter;
    private List<String> cocktailsName;
    private ArrayAdapter<String> stringArrayAdapter;
    private ImageView nfc;
    private NfcController nfcController;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        configView();
    }

    private void configView() {
        Spinner glasses = (Spinner) mainView.findViewById(R.id.glass_spinner);
        ImageView qr = (ImageView) mainView.findViewById(R.id.qr_button);
        nfc = (ImageView) mainView.findViewById(R.id.nfc_button);
        final ListView cocktails = (ListView) mainView.findViewById(R.id.cocktails_list);
        nfcController = NfcController.getInstance(getActivity());

        adapter = new CocktailAdapter(getActivity(), CocktailController.getCocktails(getRealm()));
        cocktails.setAdapter(adapter);
        registerForContextMenu(cocktails);
        final List<String> glassesNames = DataController.getGlassesNames(getRealm());
        ArrayAdapter glassesAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_single_text_view, glassesNames);
        glasses.setAdapter(glassesAdapter);
        glasses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = glassesNames.get(position);
                showToast(name);
                glassName = name;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScanQrActivity.class);
                startActivityForResult(intent, ActivityRequestCodes.CODE_QR);
            }
        });
        nfcController.initNfcView(nfc);
        cocktails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = getStringOfTextView((TextView) view.findViewById(R.id.name));
                showConfirmOrder(name, false);
            }
        });
    }

    private void showConfirmOrder(final String cocktailName, final boolean remove) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.confirmOrder))
                .setMessage(getString(R.string.youre_asking) + cocktailName)
                .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean sended = NetworkController.send(getRealm(), cocktailName, glassName, remove);
                        if (sended) {
                            showToast(cocktailName + getString(R.string.send_ok));
                        } else {
                            showToast(getString(R.string.send_error) + cocktailName);
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (remove)
                            CocktailController.removeCocktailByName(getRealm(), cocktailName);
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
        switch (id) {
            case R.id.option_1: //Details
                showDetails(cocktailName);
                break;
            case R.id.option_2: //Edit
                Cocktail cocktail = CocktailController.getCocktailByName(getRealm(), cocktailName);
                updateCocktail(cocktail);
                break;
            case R.id.option_3: //Remove
                removeCocktail(cocktailName);
                break;
        }
        return true;
    }

    private void showDetails(String cocktailName) {
        Cocktail cocktail = CocktailController.getCocktailByName(getRealm(), cocktailName);
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

    private void removeCocktail(final String cocktailName) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.are_you_sure)
                .setMessage(R.string.cocktail_removed)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CocktailController.removeCocktailByName(getRealm(), cocktailName);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_view, menu);
        configureSearchView(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void configureSearchView(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) menuItem.getActionView();

        ImageView search_close_btn = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        search_close_btn.setImageResource(R.drawable.icon_close);

        SearchView.SearchAutoComplete searchText = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        cocktailsName = CocktailController.getCocktailsNames(getRealm());
        stringArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_single_text_view, cocktailsName);
        searchText.setTextColor(getResources().getColor(R.color.colorBackground));
        searchText.setThreshold(1);
        searchText.setHint(getString(R.string.cocktail_name));
        searchText.setAdapter(stringArrayAdapter);
        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = getStringOfTextView((TextView) view.findViewById(android.R.id.text1));
                hideKeyboard();
                showConfirmOrder(name, false);
            }
        });
    }

    @Override
    public void processNfcData(Tag mytag) {
        String data = nfcController.read(mytag, nfc);
        processData(data);
    }

    private void processData(String data) {
        Cocktail cocktail = CocktailController.processData(getMasterActivity(), data);
        if (cocktail != null) {
            CocktailController.addCocktailToDB(getRealm(), cocktail);
            showConfirmOrder(cocktail.getName(), true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCodes.CODE_QR) {
            if (resultCode == Activity.RESULT_OK) {
                String extra = data.getStringExtra(Constants.EXTRA_COCKTAIL);
                processData(extra);
            }
        }
    }
}
