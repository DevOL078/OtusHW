package ru.otus.hibernate.web;

import org.eclipse.jetty.server.Server;
import ru.otus.hibernate.web.servlets.AdminController;
import ru.otus.hibernate.web.servlets.UsersController;
import ru.otus.webserver.creator.HttpServerBuilder;

public class Main {

    private static int PORT = 8081;

    public static void main(String[] args) throws Exception {
        HttpServerBuilder builder = new HttpServerBuilder();

        builder.addServlet(new AdminController(), "/admin");
        builder.addServlet(new UsersController(), "/users/*");

        Server httpServer = builder.build(PORT);

        httpServer.start();
        httpServer.join();
    }

}
