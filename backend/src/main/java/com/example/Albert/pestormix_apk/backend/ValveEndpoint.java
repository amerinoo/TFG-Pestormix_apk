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
 * Created by Albert on 30/05/2016.
 */
@Api(
        name = "valveApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.pestormix_apk.Albert.example.com",
                ownerName = "backend.pestormix_apk.Albert.example.com",
                packagePath = ""
        )
)
public class ValveEndpoint {
    private static final Logger log = Logger.getLogger(ValveEndpoint.class.getName());

    @ApiMethod(name = "insertValve")
    public void insertValve(@Named("id") String id, ValveBean valveBean) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {
            Key parent = new Entity("User", id).getKey();
            Entity taskEntity = new Entity("Valve", valveBean.getId(), parent);
            taskEntity.setProperty("id", valveBean.getId());
            taskEntity.setProperty("drinkName", valveBean.getDrinkName());
            taskEntity.setProperty("drinkAlcohol", valveBean.isDrinkAlcohol());
            datastoreService.put(taskEntity);
            txn.commit();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }

    @ApiMethod(name = "getValves")
    public List<ValveBean> getValves(@Named("id") String id) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key parent = new Entity("User", id).getKey();
        List<ValveBean> valveBeen = new ArrayList<>();
        Query query = new Query("Valve").setAncestor(parent);
        List<Entity> results = datastoreService.prepare(query)
                .asList(FetchOptions.Builder.withDefaults());
        for (Entity entity : results) {
            ValveBean valveBean = new ValveBean();
            valveBean.setId((int) entity.getProperty("id"));
            valveBean.setDrinkName((String) entity.getProperty("drinkName"));
            valveBean.setDrinkAlcohol((boolean) entity.getProperty("drinkAlcohol"));
            valveBeen.add(valveBean);
        }
        return valveBeen;
    }
}
