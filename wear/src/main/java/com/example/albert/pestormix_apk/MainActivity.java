package com.example.albert.pestormix_apk;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Toast;

import com.example.albert.pestormix_apk.backend.cocktailApi.CocktailApi;
import com.example.albert.pestormix_apk.backend.cocktailApi.model.CocktailBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                stub.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCocktails();
                    }
                });

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
                if (cocktailBeen != null)
                    showToast(String.valueOf(cocktailBeen.size()));
                else
                    showToast("NULL");
            }
        }.execute();
    }

    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
