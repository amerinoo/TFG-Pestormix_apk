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
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity implements WearableListView.ClickListener {

    private MainActivity context;
    private WearableListView listView;

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
            }
        });
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

    @Override
    public void onClick(WearableListView.ViewHolder v) {
        Integer pos = (Integer) v.itemView.getTag();
        CocktailBean bean = ((ListAdapter) listView.getAdapter()).getElement(pos);
        showToast(bean.getName());
    }

    @Override
    public void onTopEmptyRegionClick() {

    }
}
