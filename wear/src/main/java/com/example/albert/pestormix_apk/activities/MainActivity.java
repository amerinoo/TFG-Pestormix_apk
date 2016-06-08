package com.example.albert.pestormix_apk.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.widget.Toast;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.adapters.ListAdapter;
import com.example.albert.pestormix_apk.backend.cocktailApi.CocktailApi;
import com.example.albert.pestormix_apk.backend.cocktailApi.model.CocktailBean;
import com.example.albert.pestormix_apk.backend.valveApi.ValveApi;
import com.example.albert.pestormix_apk.backend.valveApi.model.ValveBean;
import com.example.albert.pestormixlibrary.ConfigurationException;
import com.example.albert.pestormixlibrary.NetworkController;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity implements WearableListView.ClickListener {

    private List<ValveBean> valveBeen = null;
    private MainActivity context;
    private WearableListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                // Get the list component from the layout of the activity
                listView =
                        (WearableListView) findViewById(R.id.list);

                // Set a click listener
                listView.setClickListener(context);
                getCocktails();
                getValves();
            }
        });
    }

    private void getValves() {
        new AsyncTask<Void, Void, List<ValveBean>>() {
            @Override
            protected List<ValveBean> doInBackground(Void... params) {
                ValveApi.Builder builder = new ValveApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null);
                ValveApi valveApi = builder.build();
                List<ValveBean> beanList = null;
                try {
                    beanList = valveApi.getValves("107431289723167670765").execute().getItems();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return beanList;
            }

            @Override
            protected void onPostExecute(List<ValveBean> valvesBeen) {
                if (valvesBeen != null) {
                    valveBeen = valvesBeen;
                } else
                    showToast("NULL Valves");
            }
        }.execute();
    }

    private void getCocktails() {
        new AsyncTask<Void, Void, List<CocktailBean>>() {
            @Override
            protected List<CocktailBean> doInBackground(Void... params) {
                CocktailApi.Builder builder = new CocktailApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null);
                CocktailApi cocktailApi = builder.build();
                List<CocktailBean> beanList = null;
                try {
                    beanList = cocktailApi.getCocktails("107431289723167670765").execute().getItems();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return beanList;
            }

            @Override
            protected void onPostExecute(List<CocktailBean> cocktailBeen) {
                if (cocktailBeen != null) {
                    // Assign an adapter to the list
                    listView.setAdapter(new ListAdapter(context, cocktailBeen));
                } else
                    showToast("NULL");
            }
        }.execute();
    }

    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
    private void showToast(int id) {
        Toast.makeText(MainActivity.this, id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(WearableListView.ViewHolder v) {
        Integer pos = (Integer) v.itemView.getTag();
        CocktailBean bean = ((ListAdapter) listView.getAdapter()).getElement(pos);
        Boolean send;
        try {
            send = NetworkController.send(valveBeen, bean.getDrinks(), 33);
            if (send) {
                showToast(String.format(getString(R.string.send_ok), bean.getName()));
            } else {
                showToast(String.format(getString(R.string.send_error), bean.getName()));
            }
        } catch (ConfigurationException e) {
            showToast(R.string.invalid_drink);
        }
    }

    @Override
    public void onTopEmptyRegionClick() {

    }
}
