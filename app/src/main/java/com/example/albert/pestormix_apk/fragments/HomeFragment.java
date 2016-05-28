package com.example.albert.pestormix_apk.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.example.albert.pestormix_apk.controllers.NetworkController;
import com.example.albert.pestormix_apk.listeners.OnNfcDataReceived;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.models.Drink;
import com.example.albert.pestormix_apk.models.Glass;
import com.example.albert.pestormix_apk.models.Valve;
import com.example.albert.pestormix_apk.nfc.NfcController;
import com.example.albert.pestormix_apk.repositories.CocktailRepository;
import com.example.albert.pestormix_apk.repositories.DrinkRepository;
import com.example.albert.pestormix_apk.repositories.GlassRepository;
import com.example.albert.pestormix_apk.repositories.ValveRepository;
import com.example.albert.pestormix_apk.utils.ActivityRequestCodes;
import com.example.albert.pestormix_apk.utils.Constants;
import com.example.albert.pestormix_apk.utils.Utils;

import java.util.List;
import java.util.Locale;

/**
 * Created by Albert on 24/01/2016.
 */
public class HomeFragment extends PestormixMasterFragment implements OnNfcDataReceived {

    private View mainView;
    private String cocktailName;
    private int glassCapacity;
    private CocktailAdapter adapter;
    private List<String> cocktailsName;
    private ArrayAdapter<String> stringArrayAdapter;
    private ImageView nfc;
    private NfcController nfcController;
    private BroadcastReceiver broadcastReceiver;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        configSwipe();
        configGlasses();
        configNfc();
        configQr();
        configCocktails();
        configSpeech();
    }

    private void configSpeech() {
        mainView.findViewById(R.id.speech_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    private void configSwipe() {
        swipeRefreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.fragment_home_screen);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().sendBroadcast(new Intent(Constants.ACTION_START_SYNC_WITH_REMOTE));
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                configCocktails();
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_PULL_COMPLETED);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private void configNfc() {
        nfc = (ImageView) mainView.findViewById(R.id.nfc_button);
        nfcController = NfcController.getInstance(getActivity());
        nfcController.initNfcView(nfc);
    }

    private void configQr() {
        ImageView qr = (ImageView) mainView.findViewById(R.id.qr_button);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScanQrActivity.class);
                startActivityForResult(intent, ActivityRequestCodes.CODE_QR);
            }
        });
    }

    private void configCocktails() {
        final ListView cocktails = (ListView) mainView.findViewById(R.id.cocktails_list);
        cocktails.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (cocktails == null || cocktails.getChildCount() == 0) ?
                                0 : cocktails.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
        adapter = new CocktailAdapter(getActivity(), CocktailRepository.getCocktails(getRealm()));
        cocktails.setAdapter(adapter);
        registerForContextMenu(cocktails);
        cocktails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = getStringOfTextView((TextView) view.findViewById(R.id.name));
                showConfirmOrder(name);
            }
        });
    }

    private void configGlasses() {
        Spinner glasses = (Spinner) mainView.findViewById(R.id.glass_spinner);
        final List<String> glassesNames = GlassRepository.getGlassesNames(getRealm());
        ArrayAdapter glassesAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_single_text_view, glassesNames);
        glasses.setAdapter(glassesAdapter);
        glasses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = glassesNames.get(position);
                Glass glass = GlassRepository.getGlasseByName(getRealm(), name);
                glassCapacity = glass.getCapacity();
                showToast(String.valueOf(glass.getCapacity()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void showConfirmOrder(String cocktailName) {
        Cocktail cocktail = CocktailRepository.getCocktailByName(getRealm(), cocktailName);
        showConfirmOrder(cocktail);
    }

    private void showConfirmOrder(final Cocktail cocktail) {
        final AlertDialog dialog;
        View detailsView = LayoutInflater.from(getActivity()).inflate(R.layout.confirm_order, null, false);
        ((TextView) detailsView.findViewById(R.id.name)).setText(cocktail.getName());
        ((TextView) detailsView.findViewById(R.id.drinks)).setText(CocktailRepository.getDrinksAsString(cocktail, getString(R.string.drinks_detail_separator)));

        dialog = new AlertDialog.Builder(getActivity())
                .setView(detailsView)
                .create();
        detailsView.findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Valve> valves = ValveRepository.getValves(getRealm());
                Boolean sended = NetworkController.send(valves, cocktail, glassCapacity);
                if (sended) {
                    showToast(cocktail.getName() + getString(R.string.send_ok));
                } else {
                    showToast(getString(R.string.send_error) + cocktail.getName());
                }
                dialog.dismiss();
            }
        });
        detailsView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == R.id.cocktails_list) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
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
                Cocktail cocktail = CocktailRepository.getCocktailByName(getRealm(), cocktailName);
                updateCocktail(cocktail);
                break;
            case R.id.option_3: //Remove
                removeCocktail(cocktailName);
                break;
        }
        return true;
    }

    private void showDetails(String cocktailName) {
        Cocktail cocktail = CocktailRepository.getCocktailByName(getRealm(), cocktailName);
        final AlertDialog dialog;

        View detailsView = LayoutInflater.from(getActivity()).inflate(R.layout.cocktail_details, null, false);
        ((TextView) detailsView.findViewById(R.id.name)).setText(cocktail.getName());
        if (cocktail.getDescription().equals("")) {
            ((TextView) detailsView.findViewById(R.id.description)).setText(R.string.missing_description);
        } else {
            ((TextView) detailsView.findViewById(R.id.description)).setText(cocktail.getDescription());
        }
        ((TextView) detailsView.findViewById(R.id.drinks)).setText(CocktailRepository.getDrinksAsString(cocktail, getString(R.string.drinks_detail_separator)));

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
                        CocktailRepository.removeCocktailByName(getRealm(), cocktailName);
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
        cocktailsName = CocktailRepository.getCocktailsNames(getRealm());
        stringArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_single_text_view, cocktailsName);
        searchText.setTextColor(Utils.getColorResource(getActivity(), R.color.colorBackground));
        searchText.setThreshold(1);
        searchText.setHint(getString(R.string.cocktail_name));
        searchText.setAdapter(stringArrayAdapter);
        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = getStringOfTextView((TextView) view.findViewById(android.R.id.text1));
                hideKeyboard();
                showConfirmOrder(name);
            }
        });
    }

    @Override
    public void processNfcData(Tag mytag) {
        String data = nfcController.read(mytag, nfc);
        processData(data);
    }

    /**
     * Create cocktail with NFC or QR data
     *
     * @param data NFC or QR data
     */
    private void processData(String data) {
        Cocktail cocktail = CocktailRepository.processData(data);
        showConfirmOrder(cocktail);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCodes.CODE_QR) {
            if (resultCode == Activity.RESULT_OK) {
                String extra = data.getStringExtra(Constants.EXTRA_COCKTAIL);
                processData(extra);
            }
        } else if (requestCode == ActivityRequestCodes.CODE_SPEECH_INPUT) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String order = result.get(0).toLowerCase();
                processSpeechOrder(order);
            }
        }
    }

    private void processSpeechOrder(String order) {
        List<Drink> drinks = DrinkRepository.getDrinks(getRealm());
        StringBuilder cocktailBuilder = new StringBuilder("Pestormix,Speech Cocktail,");
        boolean oneOrMoreDrinks = false;
        for (Drink drink : drinks) {
            if (order.contains(drink.getName().toLowerCase())) {
                oneOrMoreDrinks = true;
                cocktailBuilder.append(",").append(drink.getName());
            }
        }
        if (oneOrMoreDrinks) processData(cocktailBuilder.toString());
        else showToast("You have to say valid drinks");
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            startActivityForResult(intent, ActivityRequestCodes.CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            showToast("Speecho not supported");
        }
    }
}
