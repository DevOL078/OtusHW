package ru.otus.salamandra.app.route;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import ru.otus.salamandra.app.config.RouteConfig;

public class RouteService {

    private CamelContext camelContext;

    public RouteService() throws Exception {
        camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new RouteConfig());
    }

    public void startRoute() throws Exception {
        System.out.println("START ROUTE");
        camelContext.start();
    }

    public void stopRoute() throws Exception {
        camelContext.stop();
    }

}
