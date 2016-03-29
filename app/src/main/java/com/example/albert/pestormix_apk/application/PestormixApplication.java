package com.example.albert.pestormix_apk.application;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.albert.pestormix_apk.backend.cocktailApi.model.CocktailBean;
import com.example.albert.pestormix_apk.cocktailEndpoint.CocktailBag;
import com.example.albert.pestormix_apk.cocktailEndpoint.CocktailBagFactory;
import com.example.albert.pestormix_apk.models.Cocktail;
import com.example.albert.pestormix_apk.repositories.CocktailRepository;
import com.example.albert.pestormix_apk.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Albert on 23/01/2016.
 */
public class PestormixApplication extends Application {

    private final static String TAG = PestormixApplication.class.getSimpleName();
    private static PestormixApplication context;
    private boolean m_pulling = false;
    private boolean m_pushing = false;
    private int pushQueue = 0;
    private CocktailBag cocktailBag;
    private BroadcastReceiver m_broadcastReceiver;
    private Realm realm;

    public static PestormixApplication getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PestormixApplication.context = this;
        this.cocktailBag = CocktailBagFactory.getCocktailBag(this);
        configBroadcast();
    }

    private void configBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_START_SYNC_WITH_REMOTE);
        intentFilter.addAction(Constants.ACTION_START_SYNC_TO_REMOTE);
        intentFilter.addAction(Constants.ACTION_START_SYNC_FROM_REMOTE);
        intentFilter.addAction(Constants.ACTION_ASYNC_FAILED);

        if (m_broadcastReceiver == null) {
            m_broadcastReceiver = new BroadcastReceiverExtension();
            registerReceiver(m_broadcastReceiver, intentFilter);
        }
    }

    public Realm getRealm() {
        if (realm == null) {
            realm = Realm.getInstance(this);
        }
        return realm;
    }

    public SharedPreferences getPestormixSharedPreferences() {
        return getSharedPreferences(Constants.PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getPestormixSharedPreferences().getBoolean(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        getPestormixSharedPreferences().edit().putBoolean(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return getPestormixSharedPreferences().getString(key, defValue);
    }

    public void putString(String key, String value) {
        getPestormixSharedPreferences().edit().putString(key, value).commit();
    }


    @Override
    public void onTerminate() {
        if (null != m_broadcastReceiver) {
            unregisterReceiver(m_broadcastReceiver);
        }

        super.onTerminate();
    }

    public boolean syncInProgress() {
        return m_pulling || m_pushing;
    }

    private void syncWithRemote() {
        if (getBoolean(Constants.PREFERENCES_NEED_TO_PUSH, false)) {
            Log.d(TAG, "needToPush = true; pushing.");
            pushToRemote();
        } else {
            Log.d(TAG, "needToPush = false; pulling.");
            pullFromRemote();
        }
    }

    private void pushToRemote() {
        pushQueue += 1;
        putBoolean(Constants.PREFERENCES_NEED_TO_PUSH, true);

        if (m_pulling) {
            Log.d(TAG, "app is pulling right now. don't start push.");
        } else {
            Log.i(TAG, "Working online; should push if file revisions match");
            backgroundPushToRemote();
        }
    }

    private void pullFromRemote() {

        putBoolean(Constants.PREFERENCES_NEED_TO_PUSH, false);

        if (m_pushing) {
            Log.d(TAG, "app is pushing right now. don't start pull.");
        } else {
            Log.i(TAG, "Working online; should pull file");
            backgroundPullFromRemote();
        }
    }

    void backgroundPushToRemote() {
        if (!(m_pushing || m_pulling)) {
            cocktailBag.reload();
            new AsyncPushTask().execute();
        }
    }

    private void backgroundPullFromRemote() {
        m_pulling = true;
        cocktailBag.reload();
        new AsyncTask<Void, Void, List<CocktailBean>>() {
            @Override
            protected List<CocktailBean> doInBackground(Void... params) {
                List<CocktailBean> cocktails;
                try {
                    Log.d(TAG, "start cocktailBag.pullFromRemote");
                    cocktails = cocktailBag.pullFromRemote(getUserId());
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    return null;
                }
                return cocktails;
            }

            @Override
            protected void onPostExecute(List<CocktailBean> result) {
                Log.d(TAG, "post cocktailBag.pullFromRemote " + (result == null));

                m_pulling = false;

                if (result == null) {
                    sendBroadcast(new Intent(Constants.ACTION_ASYNC_FAILED));
                } else {
                    CocktailRepository.removeAllCocktails(getRealm());
                    List<Cocktail> cocktails = new ArrayList<>();
                    for (CocktailBean cocktailBean : result) {
                        Cocktail cocktail = new Cocktail();
                        cocktail.setName(cocktailBean.getName());
                        cocktail.setDescription(cocktailBean.getDescription());
                        cocktail.setAlcohol(cocktailBean.getAlcohol());
                        CocktailRepository.setDrinksFromString(getRealm(),
                                cocktail, cocktailBean.getDrinks());
                        cocktails.add(cocktail);
                    }
                    CocktailRepository.updateCocktails(getRealm(), cocktails);
                    sendBroadcast(new Intent(Constants.ACTION_PULL_COMPLETED));
                }
            }
        }.execute();
    }

    public Long getUserId() {
        return Long.parseLong(getString(Constants.PREFERENCES_USER_ID, "1"));
    }

    public void setUserId(Long userId) {
        String id = String.valueOf(userId);
        putString(Constants.PREFERENCES_USER_ID, id);
    }

    private class AsyncPushTask extends AsyncTask<Void, Void, Integer> {
        static final int SUCCESS = 0;
        static final int ERROR = 1;

        @Override
        protected void onPreExecute() {
            m_pushing = true;
            pushQueue = 0;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                Log.d(TAG, "start cocktailBag.pushToRemote");
                cocktailBag.pushToRemote(getUserId());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return ERROR;
            }
            return SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.d(TAG, "post cocktailBag.pushToRemote " + result);
            m_pushing = false;
            if (result == SUCCESS) {
                if (pushQueue > 0) {
                    m_pushing = true;
                    Log.d(TAG, "pushQueue == " + pushQueue + ". Need to push again.");
                    new AsyncPushTask().execute();
                } else {
                    Log.d(TAG, "cocktailBag.pushToRemote done");
                    putBoolean(Constants.PREFERENCES_NEED_TO_PUSH, false);
                    // Push is complete. Now do a pull in case the remote
                    // done.txt has changed.
                    pullFromRemote();
                }
            } else {
                sendBroadcast(new Intent(Constants.ACTION_ASYNC_FAILED));
            }
        }
    }

    private final class BroadcastReceiverExtension extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equalsIgnoreCase(Constants.ACTION_START_SYNC_WITH_REMOTE)) {
                syncWithRemote();
            } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION_START_SYNC_TO_REMOTE)) {
                pushToRemote();
            } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION_START_SYNC_FROM_REMOTE)) {
                pullFromRemote();
            } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION_ASYNC_FAILED)) {
                m_pulling = false;
                m_pushing = false;
            }
        }
    }
}
