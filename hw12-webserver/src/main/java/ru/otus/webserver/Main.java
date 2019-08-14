package ru.otus.webserver;

import org.eclipse.jetty.server.Server;
import ru.otus.cache.CacheEngineImpl;
import ru.otus.hibernate.config.HibernateConfig;
import ru.otus.hibernate.service.UserServiceWithCache;
import ru.otus.webserver.servlets.AdminController;
import ru.otus.webserver.servlets.UsersController;
import ru.otus.webserver.creator.HttpServerBuilder;

public class Main {

    private static int PORT = 8081;

    public static void main(String[] args) throws Exception {
        HttpServerBuilder builder = new HttpServerBuilder();

        builder.addServlet(new AdminController(), "/admin");
        builder.addServlet(
                new UsersController(
                        new UserServiceWithCache(HibernateConfig.getSessionFactory(),
                        new CacheEngineImpl<>(10, 100, 100, false))),
                "/users/*");

        Server httpServer = builder.build(PORT);

        httpServer.start();
        httpServer.join();
    }

}
