package ru.otus.webserver.creator;

import org.eclipse.jetty.server.Server;
import ru.otus.cache.CacheEngineImpl;
import ru.otus.hibernate.config.HibernateConfig;
import ru.otus.hibernate.service.UserServiceWithCache;
import ru.otus.webserver.servlets.AdminController;
import ru.otus.webserver.servlets.UsersController;

import java.net.MalformedURLException;

public class ServerProcessing {

    private static final int maxElements = 10;
    private static final int lifeTimeMs = 100;
    private static final int idleTimeMs = 100;
    private static final boolean isEternal = false;

    public static Server createServer(int port) throws MalformedURLException {
        HttpServerBuilder builder = new HttpServerBuilder();

        builder.addServlet(new AdminController(), "/admin");
        builder.addServlet(
                new UsersController(
                        new UserServiceWithCache(HibernateConfig.getSessionFactory(),
                                new CacheEngineImpl<>(maxElements, lifeTimeMs, idleTimeMs, isEternal))),
                "/users/*");

        return builder.build(port);
    }

}
