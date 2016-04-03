/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package com.example.Albert.pestormix_apk.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Albert on 24/03/2016.
 */
@Api(
        name = "cocktailApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.pestormix_apk.Albert.example.com",
                ownerName = "backend.pestormix_apk.Albert.example.com",
                packagePath = ""
        )
)
public class CocktailEndpoint {
    private static final Logger log = Logger.getLogger(CocktailEndpoint.class.getName());

    @ApiMethod(name = "insertCocktail")
    public void insertCocktail(@Named("id") String id, CocktailBean cocktailBean) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {
            Key parent = new Entity("User", id).getKey();
            Entity taskEntity = new Entity("Cocktail", cocktailBean.getName(), parent);
            taskEntity.setProperty("name", cocktailBean.getName());
            taskEntity.setProperty("description", cocktailBean.getDescription());
            taskEntity.setProperty("alcohol", cocktailBean.isAlcohol());
            taskEntity.setProperty("drinks", cocktailBean.getDrinks());
            datastoreService.put(taskEntity);
            txn.commit();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }

    @ApiMethod(name = "getCocktails")
    public List<CocktailBean> getCocktails(@Named("id") String id) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key parent = new Entity("User", id).getKey();
        List<CocktailBean> cocktails = new ArrayList<>();
        Query query = new Query("Cocktail").setAncestor(parent);
        List<Entity> results = datastoreService.prepare(query)
                .asList(FetchOptions.Builder.withDefaults());
        for (Entity entity : results) {
            CocktailBean cocktail = new CocktailBean();
            cocktail.setName((String) entity.getProperty("name"));
            cocktail.setDescription((String) entity.getProperty("description"));
            cocktail.setAlcohol((boolean) entity.getProperty("alcohol"));
            cocktail.setDrinks((String) entity.getProperty("drinks"));
            cocktails.add(cocktail);
        }
        return cocktails;
    }

    @ApiMethod(name = "clearCocktails")
    public void clearCocktails(@Named("id") String id) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key parent = new Entity("User", id).getKey();
        Query query = new Query("Cocktail").setAncestor(parent);
        List<Entity> results = datastoreService.prepare(query)
                .asList(FetchOptions.Builder.withDefaults());
        for (Entity entity : results) {
            datastoreService.delete(entity.getKey());
        }
    }
}
