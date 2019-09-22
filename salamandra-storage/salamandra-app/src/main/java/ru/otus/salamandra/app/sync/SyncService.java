package ru.otus.salamandra.app.sync;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import ru.otus.salamandra.app.route.SyncRoute;

public class SyncService {

    private CamelContext camelContext;

    public SyncService() throws Exception {
        camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new SyncRoute());
    }

    public void startSync() throws Exception {
        System.out.println("SYNC");
        camelContext.start();
    }

    public void stopSync() throws Exception {
        camelContext.stop();
    }



}
